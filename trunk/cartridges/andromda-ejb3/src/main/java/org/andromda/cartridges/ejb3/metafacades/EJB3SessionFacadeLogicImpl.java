package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade
 */
public class EJB3SessionFacadeLogicImpl
    extends EJB3SessionFacadeLogic
{

    /**
     * The property which stores the pattern defining the service bean name.
     */
    public static final String SERVICE_NAME_PATTERN = "serviceNamePattern";
    
    /**
     * The property which stores the pattern defining the service bean local interface name.
     */
    public static final String SERVICE_LOCAL_INTERFACE_NAME_PATTERN = "serviceLocalInterfaceNamePattern";

    /**
     * The property which stores the pattern defining the service bean remote interface name.
     */
    private static final String SERVICE_REMOTE_INTERFACE_NAME_PATTERN = "serviceRemoteInterfaceName";
    
    /**
     * The property which stores the pattern defining the service bean listener callback name.
     */
    private static final String SERVICE_LISTENER_NAME_PATTERN = "serviceListenerNamePattern";

    /**
     * The property which stores the pattern defining the service bean implementation name.
     */
    private static final String SERVICE_IMPLEMENTATION_NAME_PATTERN = "serviceImplementationNamePattern";

    /**
     * The property which stores the pattern defining the service bean delegate class name.
     */
    private static final String SERVICE_DELEGATE_NAME_PATTERN = "serviceDelegateNamePattern";
    
    /**
     * The property which stores the pattern defining the default service bean
     * exception class name.
     */
    private static final String SERVICE_DEFAULT_EXCEPTION_NAME_PATTERN = "defaultServiceExceptionNamePattern";
    
    /**
     * The property that stores the persistence container name.
     */
    public static final String PERSISTENCE_CONTAINER = "persistenceContainerName";
    
    /**
     * The property which stores the persistence context unit name associated with the default
     * Entity Manager.
     */
    private static final String PERSISTENCE_CONTEXT_UNIT_NAME = "persistenceContextUnitName";
    
    /**
     * The default view type accessability for the session bean.
     */
    public static final String SERVICE_DEFAULT_VIEW_TYPE = "serviceViewType";
    
    /**
     * The property that stores whether default service exceptions are permitted.
     */
    public static final String ALLOW_DEFAULT_SERVICE_EXCEPTION = "allowDefaultServiceException";
    
    /**
     * The property that stores the JNDI name prefix.
     */
    public static final String SERVICE_JNDI_NAME_PREFIX = "jndiNamePrefix";
    
    // ---------------- constructor -------------------------------
	
    public EJB3SessionFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // ---------------- methods -------------------------------
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#isSyntheticCreateMethodAllowed()
     */
    protected boolean handleIsSyntheticCreateMethodAllowed()
    {
    	return EJB3MetafacadeUtils.allowSyntheticCreateMethod(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getBusinessOperations()
     */
    protected java.util.Collection handleGetBusinessOperations()
    {
        Collection operations = super.getOperations();
        CollectionUtils.filter(operations, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean businessOperation = false;
                if (EJB3SessionOperationFacade.class.isAssignableFrom(object.getClass()))
                {
                    businessOperation = ((EJB3SessionOperationFacade)object).isBusinessOperation();
                }
                return businessOperation;
            }
        });
        return operations;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getAllInstanceAttributes()
     */
    protected java.util.List handleGetAllInstanceAttributes()
    {
    	return EJB3MetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getInheritedInstanceAttributes()
     */
    protected java.util.List handleGetInheritedInstanceAttributes()
    {
    	return EJB3MetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetJndiNameRemote()
     */
    protected String handleGetJndiNameRemote()
    {
        String jndiName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_JNDI_NAME_REMOTE);
        if (StringUtils.isNotBlank(jndiName))
        {
            jndiName = this.getJndiNamePrefix() + "/" + jndiName;
        }
        return jndiName;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetJndiNameLocal()
     */
    protected String handleGetJndiNameLocal()
    {
        String jndiName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_JNDI_NAME_Local);
        if (StringUtils.isNotBlank(jndiName))
        {
            jndiName = this.getJndiNamePrefix() + "/" + jndiName;
        }
        return jndiName;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetJndiNamePrefix()
     */
    protected String handleGetJndiNamePrefix()
    {
        return this.isConfiguredProperty(SERVICE_JNDI_NAME_PREFIX) ? 
                ObjectUtils.toString(this.getConfiguredProperty(SERVICE_JNDI_NAME_PREFIX)) : null;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#isStateful()
     */
    protected boolean handleIsStateful()
    {
        boolean isStateful = false;
        String sessionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_TYPE);
        if (StringUtils.isBlank(sessionType))
        {
            isStateful = !isStateless();
        }
        else
        {
            isStateful = sessionType.equalsIgnoreCase(EJB3Globals.SERVICE_TYPE_STATEFUL);
        }
        return isStateful;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#isStateless()
     */
    protected boolean handleIsStateless()
    {
        boolean isStateless = false;
        String sessionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_TYPE);
        if (StringUtils.isBlank(sessionType))
        {
    	   isStateless = this.getAllInstanceAttributes() == null || this.getAllInstanceAttributes().isEmpty();
        }
        else
        {
            isStateless = sessionType.equalsIgnoreCase(EJB3Globals.SERVICE_TYPE_STATELESS);
        }
        return isStateless;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getType()
     */
    protected java.lang.String handleGetType()
    {
        String sessionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_TYPE);
        if (StringUtils.isBlank(sessionType))
        {
            sessionType = "Stateful";
            if (this.isStateless())
            {
                sessionType = "Stateless";
            }
        }
        return sessionType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getViewType()
     */
    protected java.lang.String handleGetViewType()
    {
    	return EJB3MetafacadeUtils.getViewType(this, 
                String.valueOf(this.getConfiguredProperty(SERVICE_DEFAULT_VIEW_TYPE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsViewTypeLocal()
     */
    protected boolean handleIsViewTypeLocal()
    {
        boolean isLocal = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_LOCAL) ||
                this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_BOTH))
        {
            isLocal = true;
        }
        return isLocal;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsViewTypeRemote()
     */
    protected boolean handleIsViewTypeRemote()
    {
        boolean isRemote = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_REMOTE) || 
                this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_BOTH))
        {
            isRemote = true;
        }
        return isRemote;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getHomeInterfaceName()
     */
    protected java.lang.String handleGetHomeInterfaceName()
    {
        return EJB3MetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
        return EJB3MetafacadeUtils.getTransactionType(this, 
                String.valueOf(this.getConfiguredProperty(EJB3Globals.TRANSACTION_TYPE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getCreateMethods(boolean)
     */
    protected java.util.Collection handleGetCreateMethods(boolean follow)
    {
    	return EJB3MetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getEnvironmentEntries(boolean)
     */
    protected java.util.Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJB3MetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getConstants(boolean)
     */
    protected java.util.Collection handleGetConstants(boolean follow)
    {
        return EJB3MetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceName()
     */
    protected String handleGetServiceName()
    {
        String serviceNamePattern = (String)this.getConfiguredProperty(SERVICE_NAME_PATTERN);

        return MessageFormat.format(
                serviceNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceLocalInterfaceName()
     */
    protected String handleGetServiceLocalInterfaceName()
    {
        String serviceLocalInterfaceNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_LOCAL_INTERFACE_NAME_PATTERN);

        return MessageFormat.format(
                serviceLocalInterfaceNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceRemoteInterfaceName()
     */
    protected String handleGetServiceRemoteInterfaceName()
    {
        String serviceRemoteInterfaceNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_REMOTE_INTERFACE_NAME_PATTERN);

        return MessageFormat.format(
                serviceRemoteInterfaceNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceImplementationName()
     */
    protected String handleGetServiceImplementationName()
    {
        String serviceImplementationNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_IMPLEMENTATION_NAME_PATTERN);

        return MessageFormat.format(
                serviceImplementationNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceListenerName()
     */
    protected String handleGetServiceListenerName()
    {
        String serviceListenerNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_LISTENER_NAME_PATTERN);

        return MessageFormat.format(
                serviceListenerNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }


    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceDelegateName()
     */
    protected String handleGetServiceDelegateName()
    {
        String serviceDelegateNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_DELEGATE_NAME_PATTERN);

        return MessageFormat.format(
                serviceDelegateNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceName()
     */
    protected String handleGetFullyQualifiedServiceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.
     *      EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceImplementationName()
     */
    protected String handleGetFullyQualifiedServiceImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceImplementationName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceListenerName()
     */
    protected String handleGetFullyQualifiedServiceListenerName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceListenerName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.
     *      EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceLocalInterfaceName()
     */
    protected String handleGetFullyQualifiedServiceLocalInterfaceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceLocalInterfaceName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.
     *      EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceRemoteInterfaceName()
     */
    protected String handleGetFullyQualifiedServiceRemoteInterfaceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceRemoteInterfaceName(),
                null);
    }


    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceDelegateName()
     */
    protected String handleGetFullyQualifiedServiceDelegateName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceDelegateName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetPersistenceContextUnitName()
     */
    protected String handleGetPersistenceContextUnitName()
    {
        String unitName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME);
        if (StringUtils.isBlank(unitName))
        {
            unitName = StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(PERSISTENCE_CONTEXT_UNIT_NAME)));
        }
        return unitName;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetPersistenceContextType()
     */
    protected String handleGetPersistenceContextType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetPersistenceContextReferences()
     */
    protected Collection handleGetPersistenceContextReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                return (targetElement != null 
                        && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_PERSISTENCE_CONTEXT));
            }
        });
        CollectionUtils.transform(references, new Transformer()
        {
            public Object transform(final Object object)
            {
                return ((DependencyFacade)object).getTargetElement();
            }
        });
        return references;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#getServiceReferences()
     * 
     * Returns the Collection of DependencyFacades where the target is a Service ONLY.
     */
    public Collection getServiceReferences()
    {
        Collection references = super.getServiceReferences();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                return targetElement.hasStereotype(EJB3Profile.STEREOTYPE_SERVICE);
            }
        });
        return references;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#
     *      handleGetAttributesAsList(java.util.Collection, boolean, boolean)
     */
    protected String handleGetAttributesAsList(
            Collection attributes, 
            boolean includeTypes, 
            boolean includeNames)
    {
        if (!includeNames && !includeTypes || attributes == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        String separator = "";

        for (final Iterator it = attributes.iterator(); it.hasNext();)
        {
            EJB3SessionAttributeFacade attr = (EJB3SessionAttributeFacade)it.next();
            sb.append(separator);
            separator = ", ";
            if (includeTypes)
            {
                sb.append(attr.getType().getFullyQualifiedName());
                sb.append(" ");
            }
            if (includeNames)
            {
                sb.append(attr.getName());
            }  
        }
        return sb.toString();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetPersistenceContainer()
     */
    protected String handleGetPersistenceContainer()
    {
        return StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(PERSISTENCE_CONTAINER)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsPersistenceContainerJboss()
     */
    protected boolean handleIsPersistenceContainerJboss()
    {
        return getPersistenceContainer().equalsIgnoreCase(EJB3Globals.PERSISTENCE_CONTAINER_JBOSS);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsPersistenceContainerWeblogic()
     */
    protected boolean handleIsPersistenceContainerWeblogic()
    {
        return getPersistenceContainer().equalsIgnoreCase(EJB3Globals.PERSISTENCE_CONTAINER_WEBLOGIC);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetRolesAllowed()
     */
    protected String handleGetRolesAllowed()
    {
        StringBuffer rolesAllowed = null;
        String separator = "";
        
        for (final Iterator iter = this.getNonRunAsRoles().iterator(); iter.hasNext(); )
        {
            if (rolesAllowed == null)
            {
                rolesAllowed = new StringBuffer();
            }
            rolesAllowed.append(separator);
            Role role = (Role)iter.next();
            rolesAllowed.append('"');
            rolesAllowed.append(role.getName());
            rolesAllowed.append('"');
            separator = ", ";
        }
        return rolesAllowed != null ? rolesAllowed.toString() : null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsPermitAll()
     */
    protected boolean handleIsPermitAll()
    {
        boolean permitAll = false;
        String permitAllStr = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_PERMIT_ALL);
        if (StringUtils.isNotBlank(permitAllStr))
        {
            permitAll = BooleanUtils.toBoolean(permitAllStr);
        }
        return permitAll;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsDenyAll()
     */
    protected boolean handleIsDenyAll()
    {
        boolean denyAll = false;
        String denyAllStr = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_DENY_ALL);
        if (StringUtils.isNotBlank(denyAllStr))
        {
            denyAll = BooleanUtils.toBoolean(denyAllStr);
        }
        return denyAll;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetSecurityRealm()
     */
    protected String handleGetSecurityRealm()
    {
        String securityRealm = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_REALM);
        if (StringUtils.isBlank(securityRealm))
        {
            securityRealm = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.SECURITY_REALM)));
        }
        return securityRealm;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsSecurityEnabled()
     */
    protected boolean handleIsSecurityEnabled()
    {
        return StringUtils.isNotBlank(this.getSecurityRealm());
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetRunAs()
     */
    protected String handleGetRunAs()
    {
        String runAsRole = null;
        DependencyFacade dependency = (DependencyFacade)CollectionUtils.find(
            this.getTargetDependencies(), 
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null 
                            && dependency.getSourceElement() != null 
                            && dependency.getSourceElement() instanceof Role 
                            && dependency.hasStereotype(EJB3Profile.STEREOTYPE_SECURITY_RUNAS);
                }
            });
        if (dependency != null)
        {
            Role role = (Role)dependency.getSourceElement();
            runAsRole = role.getName();
        }
        return runAsRole;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetTransactionManagement()
     */
    protected String handleGetTransactionManagement()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsTransactionManagementBean()
     */
    protected boolean handleIsTransactionManagementBean()
    {
        return StringUtils.equalsIgnoreCase(getTransactionManagement(), EJB3Globals.TRANSACTION_MANAGEMENT_BEAN);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetResourceUserTransactionReferences()
     */
    protected Collection handleGetResourceUserTransactionReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                ModelElementFacade targetElement = dependency.getTargetElement();
                return (targetElement != null 
                        && EJB3SessionFacade.class.isAssignableFrom(targetElement.getClass())
                                && dependency.hasStereotype(EJB3Profile.STEREOTYPE_RESOURCE_REF)
                                && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_USER_TRANSACTION));
            }
        });
        return references;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetResourceDataSourceReferences()
     */
    protected Collection handleGetResourceDataSourceReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                ModelElementFacade targetElement = dependency.getTargetElement();
                return (targetElement != null 
                        && EJB3SessionFacade.class.isAssignableFrom(targetElement.getClass())
                                && dependency.hasStereotype(EJB3Profile.STEREOTYPE_RESOURCE_REF)
                                && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_DATA_SOURCE));
            }
        });
        return references;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetMessageDrivenReferences()
     */
    protected Collection handleGetMessageDrivenReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                ModelElementFacade targetElement = dependency.getTargetElement();
                return (targetElement != null 
                        && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_MESSAGE_DRIVEN));
            }
        });
        return references;
    }

    /*(
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetInterceptorReferences()
     */
    protected Collection handleGetInterceptorReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                ModelElementFacade targetElement = dependency.getTargetElement();
                return (targetElement != null && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_INTERCEPTOR));
            }
        });
        CollectionUtils.transform(references, new Transformer()
        {
            public Object transform(final Object object)
            {
                return ((DependencyFacade)object).getTargetElement();
            }
        });
        return references;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetNonRunAsRoles()
     */
    protected Collection handleGetNonRunAsRoles()
    {
        Collection roles = this.getTargetDependencies();
        CollectionUtils.filter(roles, new Predicate()
        {
            public boolean evaluate(final Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                return dependency != null 
                        && dependency.getSourceElement() != null 
                        && dependency.getSourceElement() instanceof Role 
                        && !dependency.hasStereotype(EJB3Profile.STEREOTYPE_SECURITY_RUNAS);
            }
        });
        CollectionUtils.transform(roles, new Transformer()
        {
            public Object transform(final Object object)
            {
                return ((DependencyFacade)object).getSourceElement();
            }
        });
        final Collection allRoles = new LinkedHashSet(roles);
        // add all roles which are generalizations of this one
        CollectionUtils.forAllDo(roles, new Closure()
        {
            public void execute(final Object object)
            {
                allRoles.addAll(((Role)object).getAllSpecializations());
            }
        });
        return allRoles;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetDefaultExceptionName()
     */
    protected String handleGetDefaultExceptionName()
    {
        String defaultExceptionNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_DEFAULT_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                defaultExceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#
     *      handleGetFullyQualifiedDefaultExceptionName()
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsAllowDefaultServiceException()
     */
    protected boolean handleIsAllowDefaultServiceException()
    {
        return Boolean.valueOf(
                String.valueOf(this.getConfiguredProperty(ALLOW_DEFAULT_SERVICE_EXCEPTION))).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleIsListenerEnabled()
     */
    protected boolean handleIsListenerEnabled()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_LISTENER);
    }
}