package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.Collection;

import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringService.
 * 
 * @author Chad Brandon
 * @author Peter Friese
 * @see org.andromda.cartridges.spring.metafacades.SpringService
 */
public class SpringServiceLogicImpl
    extends SpringServiceLogic
    implements org.andromda.cartridges.spring.metafacades.SpringService
{
    // ---------------- constructor -------------------------------

    public SpringServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbJndiName()
     */
    protected java.lang.String handleGetEjbJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this
            .getEjbJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbImplementationName()
     */
    protected java.lang.String handleGetEjbImplementationName()
    {
        return this.getName() + SpringGlobals.EJB_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#etImplementationName()
     */
    protected java.lang.String handleGetImplementationName()
    {
        return this.getName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedEjbImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getEjbPackageName(),
            this.getName(),
            SpringGlobals.EJB_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedEjbName()
     */
    protected java.lang.String handleGetFullyQualifiedEjbName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getEjbPackageName(), this.getName(), null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBaseName()
     */
    protected java.lang.String handleGetBaseName()
    {
        return this.getName() + SpringGlobals.SERVICE_BASE_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedBaseName()
     */
    protected java.lang.String handleGetFullyQualifiedBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.SERVICE_BASE_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageName()
     */
    protected java.lang.String handleGetEjbPackageName()
    {
        return MessageFormat.format(
            this.getEjbPackageNamePattern(),
            new Object[]
            {
                StringUtils.trimToEmpty(this.getPackageName())
            });
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageNamePath()
     */
    protected java.lang.String handleGetEjbPackageNamePath()
    {
        return this.getEjbPackageName().replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBeanName()
     */
    protected java.lang.String handleGetBeanName()
    {
        return this.getBeanName(false);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBeanName(boolean)
     */
    protected java.lang.String handleGetBeanName(boolean targetSuffix)
    {
        StringBuffer beanName = new StringBuffer(StringUtils
            .uncapitalize(StringUtils.trimToEmpty(this.getName())));
        if (targetSuffix)
        {
            beanName.append(SpringGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanName.toString();
    }

    /**
     * Gets the <code>ejbPackageNamePattern</code> for this EJB.
     * 
     * @return the defined package pattern.
     */
    protected String getEjbPackageNamePattern()
    {
        return (String)this.getConfiguredProperty("ejbPackageNamePattern");
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for this EJB.
     * 
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty("ejbJndiNamePrefix");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getWebServiceDelegatorName()
     */
    protected String handleGetWebServiceDelegatorName()
    {
        return this.getName() + SpringGlobals.WEB_SERVICE_DELEGATOR_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedWebServiceDelegatorName()
     */
    protected String handleGetFullyQualifiedWebServiceDelegatorName()
    {
        return this.getFullyQualifiedName()
            + SpringGlobals.WEB_SERVICE_DELEGATOR_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isWebService()
     */
    protected boolean handleIsWebService()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotable()
     */
    protected boolean handleIsServiceRemotable()
    {
        String serviceRemotingType = this.getServiceRemotingType();
        if (serviceRemotingType != null)
        {
            boolean result = !serviceRemotingType.equalsIgnoreCase("none");
            return result;
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemotingType()
     */
    protected String handleGetServiceRemotingType()
    {
        String serviceRemotingType = StringUtils.trimToEmpty(String
            .valueOf(this.getConfiguredProperty("serviceRemotingType")));
        String result = SpringMetafacadeUtils.getServiceRemotingType(
            this,
            serviceRemotingType);
        return result;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemoteServicePort()
     */
    protected String handleGetServiceRemotePort()
    {
        String serviceRemotePort = StringUtils.trimToEmpty(String.valueOf(this
            .getConfiguredProperty("serviceRemotePort")));
        return SpringMetafacadeUtils.getServiceRemotePort(
            this,
            serviceRemotePort);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemoteServiceUrl()
     */
    protected String handleGetServiceRemoteUrl()
    {
        String serviceRemoteServer = StringUtils.trimToEmpty(String
            .valueOf(this.getConfiguredProperty("serviceRemoteServer")));

        String serviceRemoteContext = StringUtils.trimToEmpty(String
            .valueOf(this.getConfiguredProperty("serviceRemoteContext")));

        String serviceRemotePort = this.getServiceRemotePort();

        String serviceRemotingType = this.getServiceRemotingType();

        String result = "";

        if ("none".equalsIgnoreCase(serviceRemotingType))
        {
            // nothing
        }
        else if ("httpinvoker".equalsIgnoreCase(serviceRemotingType)
            || "hessian".equalsIgnoreCase(serviceRemotingType)
            || "burlap".equalsIgnoreCase(serviceRemotingType))
        {
            // server
            result = "http://" + serviceRemoteServer;
            // port
            if (serviceRemotePort.length() > 1)
            {
                result += ":" + serviceRemotePort;
            }
            // context
            if (serviceRemoteContext.length() > 1)
            {
                result += "/" + serviceRemoteContext;
            }
            // service name
            result += "/" + getName();
        }
        else if ("rmi".equalsIgnoreCase(serviceRemotingType))
        {
            // server
            result = "rmi://" + serviceRemoteServer;
            // port
            if (serviceRemotePort.length() > 1)
            {
                result += ":" + serviceRemotePort;
            }
            // service name
            result += "/" + getName();
        }
        return result;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getWebServiceOperations()
     */
    protected Collection handleGetWebServiceOperations()
    {
        return new FilteredCollection(this.getOperations())
        {
            public boolean evaluate(Object object)
            {
                return ((SpringServiceOperation)object).isWebserviceExposed();
            }
        };
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getDefaultExceptionName()
     */
    protected String handleGetDefaultExceptionName()
    {
        String name = StringUtils.trimToEmpty(String.valueOf(this
            .getConfiguredProperty("defaultServiceExceptionNamePattern")));
        return name.replaceAll("\\{0\\}", this.getName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedDefaultExceptionName()
     */
    protected String handleGetFullyQualifiedDefaultExceptionName()
    {
        String fullyQualifiedName = "java.lang.RuntimeException";
        if (this.isAllowDefaultServiceException())
        {
            fullyQualifiedName = this.getPackageName() + '.'
                + this.getDefaultExceptionName();
        }
        return fullyQualifiedName;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceLogic#handleIsAllowDefaultServiceException()
     */
    protected boolean handleIsAllowDefaultServiceException()
    {
        return Boolean.valueOf(
            String.valueOf(this
                .getConfiguredProperty("defaultServiceExceptions")))
            .booleanValue();
    }

}