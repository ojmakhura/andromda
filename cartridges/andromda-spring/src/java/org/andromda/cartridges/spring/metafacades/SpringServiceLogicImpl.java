package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.Collection;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringService.
 *
 * @author Chad Brandon
 * @author Peter Friese
 * @see org.andromda.cartridges.spring.metafacades.SpringService
 */
public class SpringServiceLogicImpl
        extends SpringServiceLogic
{

    public SpringServiceLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbJndiName()
     */
    protected java.lang.String handleGetEjbJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getEjbJndiNamePrefix());
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
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getImplementationName()
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
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getEjbPackageName(), this.getName(),
                SpringGlobals.EJB_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedEjbName()
     */
    protected java.lang.String handleGetFullyQualifiedEjbName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getEjbPackageName(), this.getName(), null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getName(),
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
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getName(),
                SpringGlobals.SERVICE_BASE_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageName()
     */
    protected java.lang.String handleGetEjbPackageName()
    {
        String ejbPackageName = MessageFormat.format(this.getEjbPackageNamePattern(), new Object[]{
            StringUtils.trimToEmpty(this.getPackageName())});
        if (StringUtils.isBlank(this.getPackageName()))
        {
            ejbPackageName = ejbPackageName.replaceAll("^\\.", "");
        }
        return ejbPackageName;
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
        StringBuffer beanName = new StringBuffer(StringUtils.uncapitalize(StringUtils.trimToEmpty(this.getName())));
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
        final String property = "ejbJndiNamePrefix";
        return this.isConfiguredProperty(property) ? ObjectUtils.toString(this.getConfiguredProperty(property)) : null;
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
        return this.getFullyQualifiedName() + SpringGlobals.WEB_SERVICE_DELEGATOR_SUFFIX;
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
    protected boolean handleIsRemotable()
    {
        return !this.isRemotingTypeNone();
    }

    /**
     * Gets the remoting type for this service.
     */
    private String getRemotingType()
    {
        String serviceRemotingType = StringUtils.trimToEmpty(String.valueOf(
                this.getConfiguredProperty("serviceRemotingType")));
        String result = SpringMetafacadeUtils.getServiceRemotingType(this, serviceRemotingType);
        return result;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemotePort()
     */
    protected String handleGetRemotePort()
    {
        String serviceRemotePort = StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(
                "serviceRemotePort")));
        return SpringMetafacadeUtils.getServiceRemotePort(this, serviceRemotePort);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemoteUrl()
     */
    protected String handleGetRemoteUrl()
    {
        String serviceRemoteServer = StringUtils.trimToEmpty(String.valueOf(
                this.getConfiguredProperty("serviceRemoteServer")));

        final String property = "serviceRemoteContext";
        
        final String serviceRemoteContext = this.isConfiguredProperty(property) ? 
            ObjectUtils.toString(this.getConfiguredProperty(property)) : "";

        String serviceRemotePort = this.getRemotePort();

        String result = "";

        if (this.isRemotingTypeNone())
        {
            // nothing
        }
        else if (this.isRemotingTypeHttpInvoker() || this.isRemotingTypeHessian() || this.isRemotingTypeBurlap())
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
        else if (this.isRemotingTypeRmi())
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
     * Override to retrieve any abstract operations from an abstract generalization.
     * 
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    public Collection getOperations()
    {
       final Collection operations = super.getOperations(); 
       if (!this.isAbstract())
        {
            for (ClassifierFacade generalization = (ClassifierFacade)this.getGeneralization(); 
                 generalization != null; generalization = (ClassifierFacade)generalization.getGeneralization())
            {
                if (generalization.isAbstract())
                {
                    CollectionUtils.forAllDo(
                        generalization.getOperations(),
                        new Closure()
                        {
                            public void execute(Object object)
                            {
                                if (((OperationFacade)object).isAbstract())
                                {
                                    operations.add(object);
                                }
                            }
                        });
                }
            }
        }
       return operations;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getDefaultExceptionName()
     */
    protected String handleGetDefaultExceptionName()
    {
        String name = StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(
                "defaultServiceExceptionNamePattern")));
        return name.replaceAll("\\{0\\}", this.getName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedDefaultExceptionName()
     */
    protected String handleGetFullyQualifiedDefaultExceptionName()
    {
        StringBuffer fullyQualifiedName = new StringBuffer("java.lang.RuntimeException");
        if (this.isAllowDefaultServiceException())
        {
            fullyQualifiedName = new StringBuffer();
            if (StringUtils.isNotBlank(this.getPackageName()))
            {
                fullyQualifiedName.append(this.getPackageName());
                fullyQualifiedName.append('.');
            }
            fullyQualifiedName.append(this.getDefaultExceptionName());
        }
        return fullyQualifiedName.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#handleIsAllowDefaultServiceException()
     */
    protected boolean handleIsAllowDefaultServiceException()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty("defaultServiceExceptions"))).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeRmi()
     */
    protected boolean handleIsRemotingTypeRmi()
    {
        return this.getRemotingType().equalsIgnoreCase("rmi");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeBurlap()
     */
    protected boolean handleIsRemotingTypeBurlap()
    {
        return this.getRemotingType().equalsIgnoreCase("burlap");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeHessian()
     */
    protected boolean handleIsRemotingTypeHessian()
    {
        return this.getRemotingType().equalsIgnoreCase("hessian");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeHttpInvoker()
     */
    protected boolean handleIsRemotingTypeHttpInvoker()
    {
        return this.getRemotingType().equalsIgnoreCase("httpinvoker");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeNone()
     */
    protected boolean handleIsRemotingTypeNone()
    {
        return this.getRemotingType().equalsIgnoreCase("none");
    }

    /**
     * Stores the namespace property indicating whether or not the hibernate interceptor is enabled for this service.
     */
    private static final String HIBERNATE_INTERCEPTOR_ENABLED = "serviceHibernateInterceptorEnabled";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#IsHibernateInterceptorEnabled()
     */
    protected boolean handleIsHibernateInterceptorEnabled()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(HIBERNATE_INTERCEPTOR_ENABLED))).booleanValue();
    }
    
    /**
     * Stores the view type for an EJB service.
     */
    private static final String EJB_VIEW_TYPE = "ejbViewType";

    /**
     * Gets the view for this service (if an EJB).
     */
    private String getEjbViewType()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(EJB_VIEW_TYPE));
    }
    
    /**
     * The value when an EJB service has a remote view.
     */
    private static final String EJB_REMOTE_VIEW = "remote";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isEjbRemoteView()
     */
    protected boolean handleIsEjbRemoteView()
    {
        return this.getEjbViewType().equalsIgnoreCase(EJB_REMOTE_VIEW);
    }
    
    

}