package org.andromda.cartridges.support.webservice.client;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;


/**
 * A webservice client using Axis2 libraries that allows you to invoke operations on wrapped
 * style services.
 *
 * @author Chad Brandon
 */
public class WebServiceClient
{
    /**
     * The actual class to use for the webservice.
     */
    private Class serviceClass;

    /**
     * The WSDL definition.
     */
    private Definition definition;

    /**
     * The WSDL url.
     */
    private String wsdlUrl;

    /**
     * The monitor used for synchronization of the definition (to make it thread-safe).
     */
    private final Object definitionMonitor = new Object();

    /**
     * The underlying service client.
     */
    private ServiceClient serviceClient;

    /**
     * The optional TypeMapper instance to set.
     */
    private TypeMapper typeMapper = new DefaultTypeMapper();

    /**
     * Constructs a new client taking a WSDL and serviceClass
     *
     * @param wsdlUrl the URL to the WSDL for the service.
     * @param serviceClass the class used for communicating with the service (i.e. can be a regular java object).
     */
    public WebServiceClient(
        final String wsdlUrl,
        final Class serviceClass)
    {
        this(wsdlUrl, serviceClass, null, null);
    }

    /**
     * Constructs a new client taking a WSDL, the serviceClass and username and password.
     *
     * @param wsdlUrl the URL to the WSDL for the service.
     * @param serviceClass the class used for communicating with the service (i.e. can be a regular java object).
     * @param username the username to access to the service (if its protected by basic auth).
     * @param password the password to access the service (if its protected by basic auth).
     */
    public WebServiceClient(
        final String wsdlUrl,
        final Class serviceClass,
        final String username,
        final String password)
    {
        this(wsdlUrl, null, serviceClass, username, password);
    }

    /**
     * Constructs a new client taking a WSDL, endpointAddress, serviceClass and username and password.
     *
     * @param wsdlUrl the URL to the WSDL for the service.
     * @param endpointAddress the "end point" or "port" address of the service (if null, then the one in the WSDL will be used).
     * @param serviceClass the class used for communicating with the service (i.e. can be a regular java object).
     * @param username the username to access to the service (if its protected by basic auth).
     * @param password the password to access the service (if its protected by basic auth).
     */
    public WebServiceClient(
        final String wsdlUrl,
        final String endpointAddress,
        final Class serviceClass,
        final String username,
        final String password)
    {
        try
        {
            this.serviceClient = new ServiceClient();
            this.serviceClass = serviceClass;
            this.wsdlUrl = wsdlUrl;
            final WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
            if (StringUtils.isNotBlank(username))
            {
                final Authenticator authenticator = new Authenticator();
                final List<String> authorizationSchemes = new ArrayList<String>();
                authorizationSchemes.add(Authenticator.BASIC);
                authenticator.setAuthSchemes(authorizationSchemes);
                authenticator.setUsername(username);
                authenticator.setPassword(password);
                authenticator.setPreemptiveAuthentication(true);
                this.serviceClient.getOptions().setProperty(
                    HTTPConstants.AUTHENTICATE,
                    authenticator);
                synchronized (this.definitionMonitor)
                {
                    this.definition =
                        this.readProtectedWsdl(
                            reader,
                            wsdlUrl,
                            username,
                            password);
                }
            }
            else
            {
                synchronized (this.definitionMonitor)
                {
                    this.definition = reader.readWSDL(wsdlUrl);
                }
            }
            String portAddress;
            if (StringUtils.isNotBlank(endpointAddress))
            {
                portAddress = endpointAddress;
            }
            else
            {
                portAddress = this.findEndPointAddress();
            }
            serviceClient.setTargetEPR(new EndpointReference(portAddress));
        }
        catch (Exception exception)
        {
            this.handleException(exception);
        }
    }

    /**
     * Sets the optional object creator implementation.
     *
     * @param typeMapper the type mapper used for mapping types.
     */
    public void setTypeMapper(final TypeMapper typeMapper)
    {
        if (typeMapper == null)
        {
            throw new IllegalArgumentException("'typeMapper' can not be null");
        }
        this.typeMapper = typeMapper;
    }

    /**
     * Sets the timeout in seconds.
     *
     * @param seconds
     */
    public void setTimeout(long seconds)
    {
        this.serviceClient.getOptions().setTimeOutInMilliSeconds(seconds * 1000);
    }

    /**
     * Reads a WSDL protected by basic authentication.
     *
     * @param reader the WSDL reader to use for reading the WSDL.
     * @param address the address of the WSDL.
     * @param username the username to authenticate with.
     * @param password the password to authenticate with.
     * @return the WSDL definition
     */
    private Definition readProtectedWsdl(
        final WSDLReader reader,
        String address,
        String username,
        String password)
    {
        Definition definition = null;
        try
        {
            final org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            final org.apache.commons.httpclient.params.HttpClientParams params =
                new org.apache.commons.httpclient.params.HttpClientParams();
            params.setAuthenticationPreemptive(true);
            client.setParams(params);

            final org.apache.commons.httpclient.Credentials credentials =
                new org.apache.commons.httpclient.UsernamePasswordCredentials(username, password);
            final org.apache.commons.httpclient.auth.AuthScope scope =
                new org.apache.commons.httpclient.auth.AuthScope(
                    new URL(address).getHost(),
                    org.apache.commons.httpclient.auth.AuthScope.ANY_PORT,
                    org.apache.commons.httpclient.auth.AuthScope.ANY_REALM);
            client.getState().setCredentials(
                scope,
                credentials);

            final org.apache.commons.httpclient.methods.GetMethod get =
                new org.apache.commons.httpclient.methods.GetMethod(address);
            get.setDoAuthentication(true);

            int status = client.executeMethod(get);
            if (status == 404)
            {
                throw new WebServiceClientException("WSDL could not be found at: '" + address + "'");
            }
            InputSource inputSource = null;
            boolean authenticated = status > 0 && status < 400;
            if (authenticated)
            {
                inputSource = new InputSource(get.getResponseBodyAsStream());
            }
            else
            {
                throw new WebServiceClientException("Could not authenticate user: '" + username + "' to WSDL: '" +
                    address + "'");
            }
            definition =
                reader.readWSDL(
                    address,
                    inputSource);
            get.releaseConnection();
        }
        catch (Exception exception)
        {
            this.handleException(exception);
        }
        return definition;
    }

    /**
     * Finds the end point address of the service.
     *
     * @return the service end point address.
     */
    private String findEndPointAddress()
    {
        String address = null;
        Map services;
        synchronized (this.definitionMonitor)
        {
            services = this.definition.getServices();
        }
        if (services != null)
        {
            for (final Iterator iterator = services.keySet().iterator(); iterator.hasNext();)
            {
                final QName name = (QName)iterator.next();
                if (this.serviceClass.getSimpleName().equals(name.getLocalPart()))
                {
                    final Service service = (Service)services.get(name);
                    final Map ports = service.getPorts();
                    for (final Iterator portIterator = ports.keySet().iterator(); portIterator.hasNext();)
                    {
                        final String portName = (String)portIterator.next();
                        final Port port = (Port)ports.get(portName);
                        for (final Iterator addressIterator = port.getExtensibilityElements().iterator();
                            addressIterator.hasNext();)
                        {
                            final Object element = addressIterator.next();
                            if (element instanceof SOAPAddress)
                            {
                                address = ((SOAPAddress)element).getLocationURI();
                            }
                        }
                    }
                }
            }
        }
        return address;
    }

    /**
     * Invokes the operation identified by the given <code>operationName</code> with the
     * given <code>arguments</code>.
     *
     * @param operationName the name of the operation to invoke.
     * @param the arguments of the operation.
     */
    public Object invokeBlocking(
        String operationName,
        Object[] arguments)
    {
        final Method method = this.getMethod(
                operationName,
                arguments);
        OMElement omElement = this.getOperationElement(method, arguments);
        Object result = null;
        try
        {
            OMElement response = this.serviceClient.sendReceive(omElement);
            if (method.getReturnType() != void.class)
            {
                result =
                    Axis2ClientUtils.deserialize(
                        response.getFirstElement(),
                        method.getReturnType(),
                        this.typeMapper);
            }
            omElement = null;
            response = null;
        }
        catch (Exception exception)
        {
            this.handleException(exception);
        }
        return result;
    }

    /**
     * Retrieves the operation element from the internal WSDL definition.  If it can't be found
     * an exception is thrown indicating the name of the operation not found.
     *
     * @param method the method to invoke.
     * @param arguments the arguments of the method
     * @return the found operation element
     */
    private OMElement getOperationElement(final Method method, Object[] arguments)
    {
        OMElement element = null;
        synchronized (this.definitionMonitor)
        {
            element =
                Axis2ClientUtils.getOperationOMElement(
                    this.definition,
                    method,
                    arguments,
                    this.typeMapper);
        }
        if (element == null)
        {
            throw new WebServiceClientException("No operation named '" + method.getName()
                + "' was found in WSDL: " + this.wsdlUrl);
        }
        return element;
    }

    /**
     * Invoke the nonblocking/Asynchronous call
     *
     * @param operationName
     * @param arguments
     * @param callback
     */
    public void invokeNonBlocking(
        String operationName,
        Object[] arguments,
        Callback callback)
    {
        final Method method = this.getMethod(
                operationName,
                arguments);
        OMElement omElement = this.getOperationElement(method, arguments);
        try
        {
            this.serviceClient.sendReceiveNonBlocking(
                omElement,
                callback);
        }
        catch (AxisFault exception)
        {
            this.handleException(exception);
        }
        omElement = null;
    }

    public void invokeRobust(
        String operationName,
        Object[] arguments)
    {
        final Method method = this.getMethod(
                operationName,
                arguments);
        OMElement omElement = this.getOperationElement(method, arguments);
        try
        {
            this.serviceClient.sendRobust(omElement);
        }
        catch (AxisFault exception)
        {
            this.handleException(exception);
        }
        omElement = null;
    }

    /**
     * Reclaims any resources used by the client.
     */
    public void cleanup()
    {
        try
        {
            this.serviceClient.cleanup();
        }
        catch (AxisFault exception)
        {
            this.handleException(exception);
        }
    }

    /**
     * Stores the methods found on the {@link #serviceClass}.
     */
    private Map<String, Method> methods = new HashMap<String, Method>();

    private Method getMethod(
        final String name,
        final Object[] arguments)
    {
        Method found = methods.get(name);
        if (found == null)
        {
            for (final Iterator iterator = this.getAllMethods().iterator(); iterator.hasNext();)
            {
                final Method method = (Method)iterator.next();
                if (method.getName().equals(name) && arguments.length == method.getParameterTypes().length)
                {
                    found = method;
                    this.methods.put(
                        name,
                        found);
                    break;
                }
            }
        }
        return found;
    }

    /**
     * Loads all methods from the given <code>clazz</code> (this includes
     * all super class methods, public, private and protected).
     *
     * @param clazz the class to retrieve the methods.
     * @return the loaded methods.
     */
    private List getAllMethods()
    {
        final Set<Method> methods = new LinkedHashSet<Method>();
        loadMethods(
            this.serviceClass,
            methods);
        return new ArrayList<Method>(methods);
    }

    /**
     * Loads all methods from the given <code>clazz</code> (this includes
     * all super class methods).
     *
     * @param methods the list to load full of methods.
     * @param clazz the class to retrieve the methods.
     */
    private void loadMethods(
        final Class clazz,
        final Set<Method> methods)
    {
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        if (clazz.getSuperclass() != null)
        {
            loadMethods(
                clazz.getSuperclass(),
                methods);
        }
    }

    /**
     * Appropriate wraps or just re-throws the exception if already an instance
     * of {@link WebServiceClientException}.
     *
     * @param exception the exception to wrap or re-throw as a WebServiceClientException
     */
    private void handleException(Exception exception)
    {
        if (!(exception instanceof WebServiceClientException))
        {
            exception = new WebServiceClientException(exception);
        }
        throw (WebServiceClientException)exception;
    }
}