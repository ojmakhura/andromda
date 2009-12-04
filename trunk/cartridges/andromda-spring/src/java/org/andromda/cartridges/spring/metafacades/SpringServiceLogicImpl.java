package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringService.
 *
 * @author Chad Brandon
 * @author Peter Friese
 * @author Jens Vagts
 * @see org.andromda.cartridges.spring.metafacades.SpringService
 */
public class SpringServiceLogicImpl
    extends SpringServiceLogic
{
    /**
     * Public constructor for SpringServiceLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringService
     */
    public SpringServiceLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return EjbJndiName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbJndiName()
     */
    protected String handleGetEjbJndiName()
    {
        StringBuilder jndiName = new StringBuilder();
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
     * @return EjbJndiName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbLocalJndiName()
     */
    protected String handleGetEjbLocalJndiName()
    {
        StringBuilder jndiName = new StringBuilder();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getEjbJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(SpringMetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getName(),
                (EJB_BOTH_VIEW.equalsIgnoreCase(this.getEjbViewType()) ? "Local" : null)));
        return jndiName.toString();
    }

    /**
     * @return getName() + SpringGlobals.EJB_IMPLEMENTATION_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbImplementationName()
     */
    protected String handleGetEjbImplementationName()
    {
        return this.getName() + SpringGlobals.EJB_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @return getName() + SpringGlobals.IMPLEMENTATION_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @return FullyQualifiedEjbImplementationName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    protected String handleGetFullyQualifiedEjbImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getEjbPackageName(),
            this.getName(),
            SpringGlobals.EJB_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @return FullyQualifiedEjbName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedEjbName()
     */
    protected String handleGetFullyQualifiedEjbName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getEjbPackageName(),
            this.getName(),
            null);
    }

    /**
     * add "Local" to local ejb name when viewtype = "both"
     * @return FullyQualifiedLocalEjbName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedLocalEjbName()
     */
    protected String handleGetFullyQualifiedLocalEjbName()
    {
        //add "Local" to local ejb name when viewtype = "both",
        //to prevent name clashing with remote interface naming
        return SpringMetafacadeUtils.getFullyQualifiedName(
                this.getEjbPackageName(),
                this.getName(),
                (EJB_BOTH_VIEW.equalsIgnoreCase(this.getEjbViewType()) ? "Local" : null));
    }

    /**
     * @return FullyQualifiedImplementationName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    protected String handleGetFullyQualifiedImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getImplementationPackageName(),
            this.getName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @return ImplementationPackageName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getImplementationPackageName()
     */
	protected String handleGetImplementationPackageName() {
        String implementationPackageName =
            MessageFormat.format(
                this.getImplemenationPackageNamePattern(),
                    StringUtils.trimToEmpty(this.getPackageName()));
        if (StringUtils.isBlank(this.getPackageName()))
        {
        	implementationPackageName = implementationPackageName.replaceAll(
                    "^\\.",
                    "");
        }
        return implementationPackageName;
	}

    /**
     * @return ImplementationPackagePath
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getImplementationPackagePath()
     */
	protected String handleGetImplementationPackagePath()
	{
		 return this.getImplementationPackageName().replace(
		            '.',
		            '/');
	}
	
    /**
     * Gets the <code>implementationPackageNamePattern</code> for this SpringService.
     *
     * @return the defined package pattern.
     */
    protected String getImplemenationPackageNamePattern()
    {
        return (String)this.getConfiguredProperty(SpringGlobals.IMPLEMENTATION_PACKAGE_NAME_PATTERN);
    }
	
    /**
     * @return getName() + SpringGlobals.SERVICE_BASE_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBaseName()
     */
    protected String handleGetBaseName()
    {
        return this.getName() + SpringGlobals.SERVICE_BASE_SUFFIX;
    }

    /**
     * @return FullyQualifiedBaseName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedBaseName()
     */
    protected String handleGetFullyQualifiedBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getImplementationPackageName(),
            this.getName(),
            SpringGlobals.SERVICE_BASE_SUFFIX);
    }

    /**
     * @return EjbPackageName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageName()
     */
    protected String handleGetEjbPackageName()
    {
        String ejbPackageName =
            MessageFormat.format(
                this.getEjbPackageNamePattern(),
                    StringUtils.trimToEmpty(this.getPackageName()));
        if (StringUtils.isBlank(this.getPackageName()))
        {
            ejbPackageName = ejbPackageName.replaceAll(
                    "^\\.",
                    "");
        }
        return ejbPackageName;
    }

    /**
     * @return getEjbPackageName().replace('.', '/')
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageNamePath()
     */
    protected String handleGetEjbPackageNamePath()
    {
        return this.getEjbPackageName().replace(
            '.',
            '/');
    }

    /**
     * @return getBeanName(false)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return this.getBeanName(false);
    }

    /**
     * @param targetSuffix 
     * @return BeanName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBeanName(boolean)
     */
    protected String handleGetBeanName(boolean targetSuffix)
    {
        StringBuilder beanName = new StringBuilder(String.valueOf(this.getConfiguredProperty(SpringGlobals.BEAN_NAME_PREFIX)));
        beanName.append(StringUtils.uncapitalize(StringUtils.trimToEmpty(this.getName())));
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
        return (String)this.getConfiguredProperty(SpringGlobals.EJB_PACKAGE_NAME_PATTERN);
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for this EJB.
     *
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        return this.isConfiguredProperty(SpringGlobals.EJB_JNDI_NAME_PREFIX)
            ? ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.EJB_JNDI_NAME_PREFIX)) : null;
    }

    /**
     * @return getName() + SpringGlobals.WEB_SERVICE_DELEGATOR_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getWebServiceDelegatorName()
     */
    protected String handleGetWebServiceDelegatorName()
    {
        return this.getName() + SpringGlobals.WEB_SERVICE_DELEGATOR_SUFFIX;
    }

    /**
     * @return FullyQualifiedWebServiceDelegatorName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedWebServiceDelegatorName()
     */
    protected String handleGetFullyQualifiedWebServiceDelegatorName()
    {
		return SpringMetafacadeUtils.getFullyQualifiedName(
		          this.getImplementationPackageName(),
		          this.getName(),
		          SpringGlobals.WEB_SERVICE_DELEGATOR_SUFFIX);    	
    }

    /**
     * @return getWebServiceOperations().isEmpty()
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isWebService()
     */
    protected boolean handleIsWebService()
    {
        boolean webService = this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE);
        if (!webService)
        {
            webService = !this.getWebServiceOperations().isEmpty();
        }
        return webService;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceLogic#handleGetInterceptors()
     */
    protected String[] handleGetInterceptors()
    {
        String serviceInterceptorString =
            this.isConfiguredProperty(SpringGlobals.SERVICE_INTERCEPTORS) ? ObjectUtils.toString(this
                .getConfiguredProperty(SpringGlobals.SERVICE_INTERCEPTORS)) : null;
        String[] interceptors = null;
        if (StringUtils.isNotEmpty(serviceInterceptorString))
        {
            interceptors = serviceInterceptorString.split(",");
        }
        return SpringMetafacadeUtils.getServiceInterceptors(this, interceptors);
    }

    /**
     * @return !this.isPrivate() && !this.isRemotingTypeNone()
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotable()
     */
    protected boolean handleIsRemotable()
    {
        return !this.isPrivate() && !this.isRemotingTypeNone();
    }

    /**
     * Gets the remoting type for this service.
     */
    private String getRemotingType()
    {
        final String serviceRemotingType =
            StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTING_TYPE)));
        return SpringMetafacadeUtils.getServiceRemotingType(this, serviceRemotingType);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceLogic#handleGetRemoteServer()
     */
    protected String handleGetRemoteServer()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTE_SERVER)));
    }

    /**
     * @return getServiceRemotePort(this, serviceRemotePort)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemotePort()
     */
    protected String handleGetRemotePort()
    {
        String serviceRemotePort =
            StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTE_PORT)));
        return SpringMetafacadeUtils.getServiceRemotePort(this, serviceRemotePort);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceLogic#handleGetRemoteContext()
     */
    protected String handleGetRemoteContext()
    {
        return this.isConfiguredProperty(SpringGlobals.SERVICE_REMOTE_CONTEXT)
            ? ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTE_CONTEXT)) : "";
    }

    /**
     * Checks whether this service has a remote port assigned.
     *
     * @return <code>true</code> if the service has a remote port, <code>false</code> otherwise.
     */
    private boolean hasServiceRemotePort()
    {
        final String serviceRemotePort = this.getRemotePort();
        return StringUtils.isNotEmpty(serviceRemotePort);
    }

    /**
     * Checks whether the service has a remote context assigned.
     *
     * @return <code>true</code> if the service has a remote context, <code>false</code> otherwise.
     */
    private boolean hasServiceRemoteContext()
    {
        final String serviceRemoteContext = this.getRemoteContext();
        return StringUtils.isNotEmpty(serviceRemoteContext);
    }

    /**
     * @return RemoteUrl
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getRemoteUrl()
     */
    protected String handleGetRemoteUrl()
    {
        String result = "";

        String propertyPrefix = ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.CONFIG_PROPERTY_PREFIX));

        if (this.isRemotingTypeNone())
        {
            // nothing
        }
        else if (this.isRemotingTypeHttpInvoker() || this.isRemotingTypeHessian() || this.isRemotingTypeBurlap())
        {
            // server
            result = "${" + propertyPrefix + "remoteHttpScheme}://${" + propertyPrefix + "remoteServer}";

            // port
            if (hasServiceRemotePort())
            {
                result += ":${" + propertyPrefix + "remotePort}";
            }

            // context
            if (hasServiceRemoteContext())
            {
                result += "/${" + propertyPrefix + "remoteContext}";
            }

            // service name
            result += '/' + getName();
        }
        else if (this.isRemotingTypeLingo())
        {
            result = "${" + propertyPrefix + "remoteTcpScheme}://${" + propertyPrefix + "remoteServer}";
            result += "/${" + propertyPrefix + "remotePortJMS}";
        }
        else if (this.isRemotingTypeRmi())
        {
            // server
            result = "${" + propertyPrefix + "remoteRmiScheme}://${" + propertyPrefix + "remoteServer}";

            // port
            if (hasServiceRemotePort())
            {
                result += ":${" + propertyPrefix + "remotePort}";
            }

            // service name
            result += '/' + getName();
        }
        return result;
    }

    /**
     * @return getOperations() instanceof SpringServiceOperation && isWebserviceExposed()
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getWebServiceOperations()
     */
    protected Collection handleGetWebServiceOperations()
    {
        Collection<OperationFacade> operations = this.getOperations();
        if (!this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE))
        {
            operations = new FilteredCollection(operations)
                {
                    public boolean evaluate(Object object)
                    {
                        return object instanceof SpringServiceOperation && ((SpringServiceOperation)object).isWebserviceExposed();
                    }
                };
        }
        return operations;
    }

    /**
     * Override to retrieve any abstract operations from an abstract
     * generalization.
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    public Collection<OperationFacade> getOperations()
    {
        final Collection<OperationFacade> operations = super.getOperations();
        if (!this.isAbstract())
        {
            for (ClassifierFacade generalization = (ClassifierFacade)this.getGeneralization(); generalization != null;
                generalization = (ClassifierFacade)generalization.getGeneralization())
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
                                    operations.add((OperationFacade)object);
                                }
                            }
                        });
                }
            }
        }
        return operations;
    }

    /**
     * @return DefaultExceptionName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getDefaultExceptionName()
     */
    protected String handleGetDefaultExceptionName()
    {
        String name =
            StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.DEFAULT_SERVICE_EXCEPTION_NAME_PATTERN)));
        return name.replaceAll("\\{0\\}", this.getName());
    }

    /**
     * @return FullyQualifiedDefaultExceptionName
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedDefaultExceptionName()
     */
    protected String handleGetFullyQualifiedDefaultExceptionName()
    {
        StringBuilder fullyQualifiedName = new StringBuilder("RuntimeException");
        if (this.isAllowDefaultServiceException())
        {
            fullyQualifiedName = new StringBuilder();
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
     * @return getConfiguredProperty(SpringGlobals.DEFAULT_SERVICE_EXCEPTIONS)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isAllowDefaultServiceException()
     */
    protected boolean handleIsAllowDefaultServiceException()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(SpringGlobals.DEFAULT_SERVICE_EXCEPTIONS)));
    }

    /**
     * @return getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_RMI)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeRmi()
     */
    protected boolean handleIsRemotingTypeRmi()
    {
        return SpringGlobals.REMOTING_PROTOCOL_RMI.equalsIgnoreCase(this.getRemotingType());
    }

    /**
     * @return getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_BURLAP)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeBurlap()
     */
    protected boolean handleIsRemotingTypeBurlap()
    {
        return SpringGlobals.REMOTING_PROTOCOL_BURLAP.equalsIgnoreCase(this.getRemotingType());
    }

    /**
     * @return getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_HESSIAN)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeHessian()
     */
    protected boolean handleIsRemotingTypeHessian()
    {
        return SpringGlobals.REMOTING_PROTOCOL_HESSIAN.equalsIgnoreCase(this.getRemotingType());
    }

    /**
     * @return getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_HTTPINVOKER)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeHttpInvoker()
     */
    protected boolean handleIsRemotingTypeHttpInvoker()
    {
        return SpringGlobals.REMOTING_PROTOCOL_HTTPINVOKER.equalsIgnoreCase(this.getRemotingType());
    }

    /**
     * @return getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_LINGO)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeLingo()
     */
    protected boolean handleIsRemotingTypeLingo()
    {
        return SpringGlobals.REMOTING_PROTOCOL_LINGO.equalsIgnoreCase(this.getRemotingType());
    }

    /**
     * @return getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_NONE)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRemotingTypeNone()
     */
    protected boolean handleIsRemotingTypeNone()
    {
        return SpringGlobals.REMOTING_PROTOCOL_NONE.equalsIgnoreCase(this.getRemotingType());
    }

    /**
     * Stores the namespace property indicating whether or not the hibernate
     * interceptor is enabled for this service.
     */
    private static final String HIBERNATE_INTERCEPTOR_ENABLED = "serviceHibernateInterceptorEnabled";

    /**
     * @return getConfiguredProperty(HIBERNATE_INTERCEPTOR_ENABLED)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isHibernateInterceptorEnabled()
     */
    protected boolean handleIsHibernateInterceptorEnabled()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(HIBERNATE_INTERCEPTOR_ENABLED)));
    }

    /**
     * Stores the view type for an EJB service.
     */
    private static final String EJB_VIEW_TYPE = "ejbViewType";

    /**
     * Gets the view for this service (if wrapped by an EJB).
     */
    private String getEjbViewType()
    {
        Object value = this.findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_VIEW_TYPE);
        if (value == null)
        {
            value = this.getConfiguredProperty(EJB_VIEW_TYPE);
        }
        return ObjectUtils.toString(value);
    }

    /**
     * The three EJB view type values.
     */
    private static final String EJB_REMOTE_VIEW = "remote";
    private static final String EJB_LOCAL_VIEW = "local";
    private static final String EJB_BOTH_VIEW = "both";

    /**
     * @return getEjbViewType().equalsIgnoreCase(EJB_REMOTE_VIEW) || EJB_BOTH_VIEW
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isEjbRemoteView()
     */
    protected boolean handleIsEjbRemoteView()
    {
        return (EJB_REMOTE_VIEW.equalsIgnoreCase(this.getEjbViewType())
                || EJB_BOTH_VIEW.equalsIgnoreCase(this.getEjbViewType()));
    }

    /**
     * @return getEjbViewType().equalsIgnoreCase(EJB_LOCAL_VIEW) || EJB_BOTH_VIEW
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isEjbLocalView()
     */
    protected boolean handleIsEjbLocalView()
    {
        return (EJB_LOCAL_VIEW.equalsIgnoreCase(this.getEjbViewType())
                || EJB_BOTH_VIEW.equalsIgnoreCase(this.getEjbViewType()));
    }

    /**
     * @return EjbTransactionType "Container" or "Bean"
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbTransactionType()
     */
    protected String handleGetEjbTransactionType()
    {
        String transactionType;
        final boolean ejbTransactionsEnabled =
            BooleanUtils.toBoolean(
                ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.EJB_TRANSACTIONS_ENABLED)));
        if (ejbTransactionsEnabled)
        {
            transactionType = "Container";
        }
        else
        {
            transactionType = "Bean";
        }
        return transactionType;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceLogic#handleIsConfigonly()
     */
    protected boolean handleIsConfigonly()
    {
        String value = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_SERVICE_CONFIG_ONLY);
        return BooleanUtils.toBoolean(StringUtils.trimToEmpty(value));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceLogic#handleIsPrivate()
     */
    protected boolean handleIsPrivate()
    {
        String value = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_SERVICE_PRIVATE);
        return BooleanUtils.toBoolean(StringUtils.trimToEmpty(value));
    }

    /**
     * @return getWebServiceAttachmentHandlerCall(SpringGlobals.WEBSERVICE_OUTGOING_ATTACHMENT_HANDLER_CALL_PATTERN)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getWebServiceOutgoingAttachmentHandlerCall()
     */
    protected String handleGetWebServiceOutgoingAttachmentHandlerCall()
    {
        return this.getWebServiceAttachmentHandlerCall(SpringGlobals.WEBSERVICE_OUTGOING_ATTACHMENT_HANDLER_CALL_PATTERN);
    }

    /**
     * @return getWebServiceAttachmentHandlerCall(SpringGlobals.WEBSERVICE_INCOMING_ATTACHMENT_HANDLER_CALL_PATTERN)
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getWebServiceIncomingAttachmentHandlerCall()
     */
    protected String handleGetWebServiceIncomingAttachmentHandlerCall()
    {
        return this.getWebServiceAttachmentHandlerCall(SpringGlobals.WEBSERVICE_INCOMING_ATTACHMENT_HANDLER_CALL_PATTERN);
    }

    /**
     * Constructs the webservice attachment handler call or returns null if
     * one isn't found or is not appropriate it for the service (i.e. it isn't
     * a webservice).
     *
     * @param patternProperty the property defining the pattern type.
     * @return the call value.
     */
    private String getWebServiceAttachmentHandlerCall(final String patternProperty)
    {
        String call = null;
        if (this.isWebService())
        {
            final String value = ObjectUtils.toString(this.getConfiguredProperty(patternProperty));
            if (StringUtils.isNotBlank(value))
            {
                call = value;
            }
        }
        return call;
    }

    /**
     * @return getConfiguredProperty(SpringGlobals.RICH_CLIENT).equalsIgnoreCase("true")
     * @see org.andromda.cartridges.spring.metafacades.SpringService#isRichClient()
     */
    protected boolean handleIsRichClient()
    {
        final String richClient = StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.RICH_CLIENT)));
        return "true".equalsIgnoreCase(richClient);
    }

    /**
     * @inheritDoc
     */
    protected String handleGetInitMethodName()
    {
        String result = "";
        for (OperationFacade operation : getOperations())
        {
            if(((SpringServiceOperation)operation).isInitMethod())
            {
                result = operation.getName();
                break;
            }
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    protected String handleGetDestroyMethodName()
    {
        String result = "";
        for (OperationFacade operation : getOperations())
        {
            if(((SpringServiceOperation)operation).isDestroyMethod())
            {
                result = operation.getName();
                break;
            }
        }
        return result;
    }
}