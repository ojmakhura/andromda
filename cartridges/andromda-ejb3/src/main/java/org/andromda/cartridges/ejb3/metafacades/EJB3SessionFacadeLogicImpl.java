package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getJndiName()
     */
    protected java.lang.String handleGetJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getJndiNamePrefix());
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
     * Gets the <code>jndiNamePrefix</code> for this EJB.
     *
     * @return the EJB Jndi name prefix.
     */
    protected String getJndiNamePrefix()
    {
        String prefix = null;
        if (this.isConfiguredProperty(EJB3Globals.JNDI_NAME_PREFIX))
        {
            prefix = (String)this.getConfiguredProperty(EJB3Globals.JNDI_NAME_PREFIX);
        }
        return prefix;
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
                String.valueOf(this.getConfiguredProperty(EJB3Globals.SESSION_DEFAULT_VIEW_TYPE)));
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
        String transactionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isNotBlank(transactionType))
        {
            transactionType = EJB3MetafacadeUtils.convertTransactionType(transactionType);
        }
        else
        {
            transactionType = transactionType =
                    String.valueOf(this.getConfiguredProperty(EJB3Globals.TRANSACTION_TYPE));
        }
        return transactionType;
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetPersistenceContextUnitName()
     */
    protected String handleGetPersistenceContextUnitName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME);
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
                        && EJB3SessionFacade.class.isAssignableFrom(targetElement.getClass())
                                && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_PERSISTENCE_CONTEXT));
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
    protected String handleGetAttributesAsList(Collection attributes, boolean includeTypes, boolean includeNames)
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
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.PERSISTENCE_CONTAINER)));
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
        String rolesAllowedStr = null;
        final String tmpRoles = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_ROLES_ALLOWED);
        if (StringUtils.isNotBlank(tmpRoles))
        {
            StringBuffer rolesAllowed = new StringBuffer();
            final String[] roles = StringUtils.split(tmpRoles, ',');
            for (int i = 0; i < roles.length; i++)
            {
                if (i > 0)
                {
                    rolesAllowed.append(", ");
                }
                rolesAllowed.append('"');
                rolesAllowed.append(roles[i]);
                rolesAllowed.append('"');
            }
            rolesAllowedStr = rolesAllowed.toString();
        }
        return rolesAllowedStr;
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetSecurityDomain()
     */
    protected String handleGetSecurityDomain()
    {
        String securityDomain = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_DOMAIN);
        if (StringUtils.isBlank(securityDomain))
        {
            securityDomain = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.SECURITY_DOMAIN)));
        }
        return securityDomain;
    }

}