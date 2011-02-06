package org.andromda.cartridges.webservice.metafacades;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.cartridges.webservice.WebServiceUtils;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebServiceOperation.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation
 * @author Bob Fields
 */
public class WebServiceOperationLogicImpl
    extends WebServiceOperationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public WebServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) or hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION)
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#isExposed()
     */
    protected boolean handleIsExposed()
    {
        return this.getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) ||
        this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

    /**
     * The prefix given to the test implementation operation names.
     */
    private static final String TEST_IMPLEMENTATION_OPERATION_NAME_PREFIX =
        "testImplementationOperationNamePrefix";

    /**
     * Gets the test implementation operation name prefix.
     */
    private String getTestImplementationOperationNamePrefix()
    {
        return String.valueOf(
            this.getConfiguredProperty(TEST_IMPLEMENTATION_OPERATION_NAME_PREFIX));
    }

    /**
     * @return getTestImplementationOperationNamePrefix() + StringUtils.capitalize(this.getTestName())
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationName()
     */
    protected String handleGetTestImplementationName()
    {
        return this.getTestImplementationOperationNamePrefix() +
        StringUtils.capitalize(this.getTestName());
    }

    /**
     * @return "this." + this.getTestImplementationSignature()
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationCall()
     */
    protected String handleGetTestImplementationCall()
    {
        return "this." + this.getTestImplementationSignature();
    }

    /**
     * @return this.getTestImplementationOperationNamePrefix() + StringUtils.capitalize(this.getTestSignature())
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationSignature()
     */
    protected String handleGetTestImplementationSignature()
    {
        return this.getTestImplementationOperationNamePrefix() +
        StringUtils.capitalize(this.getTestSignature());
    }

    /**
     * The prefix given to the junit test operations.
     */
    private static final String TEST_NAME_PREFIX = "test";

    /**
     * @return TEST_NAME_PREFIX + StringUtils.capitalize(this.getName())
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestName()
     */
    protected String handleGetTestName()
    {
        return TEST_NAME_PREFIX + StringUtils.capitalize(this.getName());
    }

    /**
     * @return "this." + this.getSignature()
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestCall()
     */
    protected String handleGetTestCall()
    {
        return "this." + this.getSignature();
    }

    /**
     * @return this.getTestName() + "()"
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestSignature()
     */
    protected String handleGetTestSignature()
    {
        return this.getTestName() + "()";
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_PARAMETER_STYLE = "defaultParameterStyle";
    private static final String DEFAULT = "default";
    private static final String EMPTY_STRING = "";
    private static final String BOOLEAN_FALSE = "false";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#handleGetParameterStyle()
     */
    @Override
    protected String handleGetParameterStyle()
    {
        String style = (String)this.findTaggedValue(WebServiceGlobals.WEB_SERVICE_PARAMETER_STYLE);
        if (StringUtils.isEmpty(style) || style.equals(DEFAULT))
        {
            style = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_PARAMETER_STYLE));
        }
        if (StringUtils.isEmpty(style) || style.equals(DEFAULT))
        {
            style = "wrapped";
        }
        return style;
    }
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestCacheType()
     */
    @Override
    protected String handleGetRestCacheType()
    {
        String cacheType = (String)this.findTaggedValue(WebServiceGlobals.CACHE_TYPE);
        if (!this.isRest() || StringUtils.isBlank(cacheType) || cacheType.equals(DEFAULT))
        {
            cacheType = EMPTY_STRING;
        }
        return cacheType;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestConsumes()
     */
    @Override
    protected String handleGetRestConsumes()
    {
        String consumes = (String)this.findTaggedValue(WebServiceGlobals.REST_CONSUMES);
        if (!this.isRest() || StringUtils.isBlank(consumes) || consumes.equals(DEFAULT))
        {
            consumes = EMPTY_STRING;
        }
        else
        {
            consumes = translateMediaType(consumes);
        }
        return consumes;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestPartType()
     */
    @Override
    protected String handleGetRestPartType()
    {
        String partType = (String)this.findTaggedValue(WebServiceGlobals.REST_PART_TYPE);
        if (!this.isRest() || StringUtils.isBlank(partType) || partType.equals(DEFAULT))
        {
            partType = EMPTY_STRING;
        }
        return partType;
    }

    private static final String SLASH = "/";
    private static final String QUOTE = "\"";
    private static final String LBRACKET = "{";
    private static final String RBRACKET = "}";
    /**
     * Create default REST URL of /methodname/parameter/{parameter}/
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestPath()
     */
    @Override
    protected String handleGetRestPath()
    {
        String path = (String)this.findTaggedValue(WebServiceGlobals.REST_PATH);
        StringBuilder pathBuffer = new StringBuilder();
        if (!this.isRest() || StringUtils.isBlank(path) || path.equals(DEFAULT))
        {
            pathBuffer.append(QUOTE).append(SLASH).append(this.getName().toLowerCase()).append(SLASH);
            //path = SLASH + this.getName().toLowerCase() + SLASH;
            Iterator<ParameterFacade> parameters = this.getArguments().iterator();
            while (parameters.hasNext())
            {
                ParameterFacade param = parameters.next();
                //if (WebServiceUtils.isSimpleType(param))
                //{
                    String paramName = param.getName();
                    pathBuffer.append(paramName).append(SLASH).append(LBRACKET).append(paramName).append(RBRACKET).append(SLASH);
                //}
            }
            pathBuffer.append(QUOTE);
        }
        else
        {
            if (StringUtils.isBlank(path))
            {
                path = EMPTY_STRING;
            }
            pathBuffer.append(path);
            if (!path.startsWith(QUOTE))
            {
                pathBuffer.insert(0, QUOTE);
            }
            if (!path.endsWith(QUOTE) || path.length()<2)
            {
                pathBuffer.append(QUOTE);
            }
        }
        return pathBuffer.toString();
    }

    /**
     * Create test REST URL of /methodname/parameter/{parameter}/
     * Substitutes test values for parameters
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestPath()
     */
    @Override
    protected String handleGetRestTestPath()
    {
        String path = (String)this.findTaggedValue(WebServiceGlobals.REST_PATH);
        StringBuilder pathBuffer = new StringBuilder();
        WebServiceLogic service = (WebServiceLogic)this.getService();
        String servicePath = service.getRestPath();
        WebServiceUtils wsutils = new WebServiceUtils();
        if (!this.isRest() || StringUtils.isBlank(path) || path.equals(DEFAULT))
        {
            pathBuffer.append(SLASH).append(this.getName().toLowerCase()).append(SLASH);
            Iterator<ParameterFacade> parameters = this.getArguments().iterator();
            while (parameters.hasNext())
            {
                ParameterFacade param = parameters.next();
                //System.out.println("handleGetRestTestPath param=" + param.getName() + " servicePath=" + servicePath + " value=" + wsutils.createConstructor(param));
                if (WebServiceUtils.isSimpleType(param))
                {
                    String paramValue = wsutils.createConstructor(param);
                    // Only use the value if constructor returns new Class()
                    if (paramValue.indexOf('(') > 0)
                    {
                        paramValue = paramValue.substring(paramValue.indexOf('(')+1, paramValue.indexOf(')'));
                    }
                    pathBuffer.append(param.getName()).append(SLASH).append(paramValue).append(SLASH);
                }
            }
            path = pathBuffer.toString();
        }
        else
        {
            if (StringUtils.isBlank(path))
            {
                path = EMPTY_STRING;
            }
            // StringBuffer doesn't have replace(String, String) API
            path = pathBuffer.append(path).toString();
            Iterator<ParameterFacade> parameters = this.getArguments().iterator();
            while (parameters.hasNext())
            {
                ParameterFacade param = parameters.next();
                if (WebServiceUtils.isSimpleType(param))
                {
                    String paramValue = wsutils.createConstructor(param).replace("\"", "");
                    if (paramValue.indexOf('(') > 0)
                    {
                        paramValue = paramValue.substring(paramValue.indexOf('(')+1, paramValue.indexOf(')'));
                    }
                    path = StringUtils.replace(path, LBRACKET + param.getName() + RBRACKET, paramValue);
                }
                //System.out.println("handleGetRestTestPath param=" + param.getName() + " servicePath=" + servicePath + " value=" + wsutils.createConstructor(param) + " path=" + path);
            }
        }
        path = servicePath + path;
        path = path.replaceAll("\"", "");
        path = path.replaceAll("//", "/");
        return path;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestProduces()
     */
    @Override
    protected String handleGetRestProduces()
    {
        String produces = (String)this.findTaggedValue(WebServiceGlobals.REST_PRODUCES);
        // default types: text for simple types, XML for complex types
        if (!this.isRest() || produces == DEFAULT)
        {
            // See if the service class has REST_produces attribute set...
            produces = (String)this.getOwner().findTaggedValue(WebServiceGlobals.REST_PRODUCES);
            if (produces == DEFAULT)
            {
                // Default produces type for simple or complex return types
                if (WebServiceUtils.isSimpleType(this.getReturnType()))
                {
                    produces = "text/plain";
                }
                else
                {
                    produces = "application/xml";
                }
            }
        }
        else
        {
            produces = translateMediaType(produces);
        }
        return produces;
    }

    /**
     * Returns map of ProviderMediaType enumeration values to Provider/Consumer text
     */
    private static Map<String, String> getMediaTranslation()
    {
        final Map<String, String> mediaMap = new HashMap<String, String>();
        mediaMap.put("default", QUOTE + "application/xml" + QUOTE);
        mediaMap.put("ApplicationAtom", QUOTE + "application/atom+xml" + QUOTE);
        mediaMap.put("ApplicationAtomEntry", QUOTE + "application/atom+xml;type=entry" + QUOTE);
        mediaMap.put("ApplicationAtomXML", QUOTE + "application/atom+xml" + QUOTE);
        mediaMap.put("ApplicationFastinfoset", QUOTE + "application/fastinfoset" + QUOTE);
        mediaMap.put("ApplicationFormURLEncoded", QUOTE + "application/x-www-form-urlencoded" + QUOTE);
        mediaMap.put("ApplicationJSON", QUOTE + "application/json" + QUOTE);
        mediaMap.put("ApplicationOctetStream", QUOTE + "application/octet-stream" + QUOTE);
        mediaMap.put("ApplicationSvgXML", QUOTE + "application/svg+xml" + QUOTE);
        mediaMap.put("ApplicationXhtmlXML", QUOTE + "application/xhtml+xml" + QUOTE);
        mediaMap.put("ApplicationXML", QUOTE + "application/xml" + QUOTE);
        mediaMap.put("ApplicationXMLAll", QUOTE + "application/*+xml" + QUOTE);
        mediaMap.put("ApplicationYaml", QUOTE + "application/yaml" + QUOTE);
        mediaMap.put("MultipartAlternative", QUOTE + "multipart/alternative" + QUOTE);
        mediaMap.put("MultipartFixed", QUOTE + "multipart/fixed" + QUOTE);
        mediaMap.put("MultipartFormData", QUOTE + "multipart/form-data" + QUOTE);
        mediaMap.put("MultipartMixed", QUOTE + "multipart/mixed" + QUOTE);
        mediaMap.put("MultipartRelated", QUOTE + "multipart/related" + QUOTE);
        mediaMap.put("TextPlain", QUOTE + "text/plain" + QUOTE);
        mediaMap.put("TextXML", QUOTE + "text/xml" + QUOTE);
        mediaMap.put("TextXYaml", QUOTE + "text/xyaml" + QUOTE);
        mediaMap.put("TextYaml", QUOTE + "text/xml" + QUOTE);
        mediaMap.put("Wildcard", QUOTE + "*/*" + QUOTE);
        return mediaMap;
    }

    /**
     * Translates Media Enumeration Type into string for produces/consumes annotation
     * @param input ProviderMediaType Enumeration value to be translated for Annotation
     * @return getMediaTranslation().get(input)
     */
    public static String translateMediaType(String input)
    {
        String output = null;
        if (StringUtils.isBlank(input) || input.equals(DEFAULT) || !getMediaTranslation().containsKey(input))
        {
            output = getMediaTranslation().get(DEFAULT);
        }
        else
        {
            output = getMediaTranslation().get(input);
        }
        return output;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestProvider()
     */
    @Override
    protected String handleGetRestProvider()
    {
        String provider = (String)this.findTaggedValue(WebServiceGlobals.REST_PROVIDER);
        if (!this.isRest() || StringUtils.isBlank(provider) || provider.equals(DEFAULT))
        {
            provider = EMPTY_STRING;
        }
        return provider;
    }

    private static final String GET = "@javax.ws.rs.GET";
    private static final String AT = "@javax.ws.rs.";
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestRequestType()
     */
    @Override
    protected String handleGetRestRequestType()
    {
        String requestType = (String)this.findTaggedValue(WebServiceGlobals.REST_REQUEST_TYPE);
        if (!this.isRest() || StringUtils.isBlank(requestType) || requestType.equals(DEFAULT))
        {
            requestType = GET;
        }
        else if (!requestType.startsWith(AT))
        {
            requestType = AT + requestType;
        }
        return requestType;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRestRequestType()
     */
    @Override
    protected int handleGetRestSuspend()
    {
        String suspend = (String)this.findTaggedValue(WebServiceGlobals.REST_SUSPEND);
        if (!this.isRest() || StringUtils.isBlank(suspend) || suspend.equals(DEFAULT) || !StringUtils.isNumeric(suspend))
        {
            return 0;
        }
        return Integer.parseInt(suspend);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRolesAllowed()
     */
    @Override
    protected String handleGetRolesAllowed()
    {
        String rolesAllowed = (String)this.findTaggedValue(WebServiceGlobals.REST_ROLES_ALLOWED);
        if (!this.isRest() || StringUtils.isBlank(rolesAllowed) || rolesAllowed.equals(DEFAULT))
        {
            rolesAllowed = EMPTY_STRING;
        }
        return rolesAllowed;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#handleIsRest()
     */
    @Override
    protected boolean handleIsRest()
    {
        String rest = (String)this.findTaggedValue(WebServiceGlobals.REST);
        if (StringUtils.isBlank(rest) || rest.equals(DEFAULT))
        {
            rest = (String)this.getOwner().findTaggedValue(WebServiceGlobals.REST);
            if (StringUtils.isBlank(rest) || rest.equals(DEFAULT))
            {
                rest = BOOLEAN_FALSE;
            }
        }
        return Boolean.valueOf(rest);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#getRolesAllowed()
     */
    @Override
    protected boolean handleIsRestEncoded()
    {
        String restEncoded = (String)this.findTaggedValue(WebServiceGlobals.REST_ENCODED);
        if (!this.isRest() || StringUtils.isBlank(restEncoded) || restEncoded.equals(DEFAULT))
        {
            restEncoded = BOOLEAN_FALSE;
        }
        return Boolean.valueOf(restEncoded);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#isRestAtom()
     */
    @Override
    protected boolean handleIsRestAtom()
    {
        WebServiceLogic service = (WebServiceLogic)this.getService();
        boolean rest = this.isRest();
        boolean restAtom = false;
        if (rest)
        {
            restAtom = this.getRestProduces().contains("atom");
            if (!restAtom)
            {
                restAtom = service.getRestProduces().indexOf("atom") > -1;
            }
        }
        return restAtom;
    }

    /**
     * Return the value from WebServiceOperation andromda_webservice_operationName, or just the operation.name
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogic#handleGetOperationName()
     */
    @Override
    protected String handleGetOperationName()
    {
        String serviceName = (String)this.findTaggedValue(WebServiceGlobals.WEB_SERVICE_NAME);
        if (StringUtils.isBlank(serviceName))
        {
            serviceName = this.getName();
        }
        return serviceName;
    }
}