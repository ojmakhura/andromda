package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;
import java.util.List;
import org.andromda.cartridges.ejb.EJBGlobals;
import org.andromda.cartridges.ejb.EJBProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade
 */
public class EJBSessionFacadeLogicImpl
        extends EJBSessionFacadeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBSessionFacadeLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @param follow 
     * @return getCreateMethods
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getCreateMethods(boolean)
     */
    protected java.util.Collection handleGetCreateMethods(boolean follow)
    {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @return EJBMetafacadeUtils.getHomeInterfaceName(this)
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getHomeInterfaceName()
     */
    protected String handleGetHomeInterfaceName()
    {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @return EJBMetafacadeUtils.getViewType(this)
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getViewType()
     */
    protected String handleGetViewType()
    {
        return EJBMetafacadeUtils.getViewType(this);
    }

    protected List handleGetInheritedInstanceAttributes()
    {
        return EJBMetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    protected List handleGetAllInstanceAttributes()
    {
        return EJBMetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @param follow 
     * @return getEnvironmentEntries
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getEnvironmentEntries(boolean)
     */
    protected Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJBMetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @param follow 
     * @return getConstants
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getConstants(boolean)
     */
    protected Collection handleGetConstants(boolean follow)
    {
        return EJBMetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @return jndiName
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getJndiName()
     */
    protected String handleGetJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append('/');
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
        if (this.isConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX))
        {
            prefix = (String)this.getConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX);
        }
        return prefix;
    }

    /**
     * @return !isStateless
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#isStateful()
     */
    protected boolean handleIsStateful()
    {
        return !isStateless();
    }

    /**
     * @return getAllInstanceAttributes().isEmpty()
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacadeLogic#isStateless()
     */
    protected boolean handleIsStateless()
    {
        return this.getAllInstanceAttributes() == null || this.getAllInstanceAttributes().isEmpty();
    }

    /**
     * @return type Stateful/Stateless
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getType()
     */
    protected String handleGetType()
    {
        String type = "Stateful";
        if (this.isStateless())
        {
            type = "Stateless";
        }
        return type;
    }

    /**
     * @return EJBMetafacadeUtils.allowSyntheticCreateMethod(this)
     * @see org.andromda.cartridges.ejb.metafacades.EJBMetafacadeUtils#allowSyntheticCreateMethod(org.andromda.metafacades.uml.ClassifierFacade)
     */
    protected boolean handleIsSyntheticCreateMethodAllowed()
    {
        return EJBMetafacadeUtils.allowSyntheticCreateMethod(this);
    }

    /**
     * @return businessOperation
     * @see org.andromda.metafacades.uml.Entity#getBusinessOperations()
     */
    protected Collection handleGetBusinessOperations()
    {
        Collection operations = super.getOperations();
        CollectionUtils.filter(operations, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean businessOperation = false;
                if (EJBOperationFacade.class.isAssignableFrom(object.getClass()))
                {
                    businessOperation = ((EJBOperationFacade)object).isBusinessOperation();
                }
                return businessOperation;
            }
        });
        return operations;
    }

    /**
     * @return transactionType
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getTransactionType()
     */
    protected String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJBGlobals.TRANSACTION_TYPE));
        }
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = "Required";
        }
        return transactionType;
    }
}