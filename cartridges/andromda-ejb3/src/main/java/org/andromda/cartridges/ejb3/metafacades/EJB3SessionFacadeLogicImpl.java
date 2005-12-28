package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
                if (EJB3OperationFacade.class.isAssignableFrom(object.getClass()))
                {
                    businessOperation = ((EJB3OperationFacade)object).isBusinessOperation();
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
        return !isStateless();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#isStateless()
     */
    protected boolean handleIsStateless()
    {
    	return this.getAllInstanceAttributes() == null || this.getAllInstanceAttributes().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getType()
     */
    protected java.lang.String handleGetType()
    {
        String type = "Stateful";
        if (this.isStateless())
        {
            type = "Stateless";
        }
        return type;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade#getViewType()
     */
    protected java.lang.String handleGetViewType()
    {
    	return EJB3MetafacadeUtils.getViewType(this);
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
        if (StringUtils.isBlank(transactionType))
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

    /* (non-Javadoc)
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetServiceName()
     */
    protected String handleGetServiceName()
    {
        String serviceNamePattern = (String)this.getConfiguredProperty(SERVICE_NAME_PATTERN);

        return MessageFormat.format(
                serviceNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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

    /* (non-Javadoc)
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceListener()
     */
    protected String handleGetFullyQualifiedServiceListener()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceListenerName(),
                null);
    }

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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

}