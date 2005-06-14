package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBGlobals;
import org.andromda.cartridges.ejb.EJBProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade
 */
public class EJBSessionFacadeLogicImpl
        extends EJBSessionFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EJBSessionFacadeLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getCreateMethods(boolean)
     */
    protected java.util.Collection handleGetCreateMethods(boolean follow)
    {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getHomeInterfaceName()
     */
    protected java.lang.String handleGetHomeInterfaceName()
    {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getViewType()
     */
    protected java.lang.String handleGetViewType()
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
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getEnvironmentEntries(boolean)
     */
    protected Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJBMetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getConstants(boolean)
     */
    protected Collection handleGetConstants(boolean follow)
    {
        return EJBMetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSession#getJndiName()
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
        if (this.isConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX))
        {
            prefix = (String)this.getConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX);
        }
        return prefix;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#isStateful()
     */
    protected boolean handleIsStateful()
    {
        return !isStateless();
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacadeLogic#isStateless()
     */
    protected boolean handleIsStateless()
    {
        return this.getAllInstanceAttributes() == null || this.getAllInstanceAttributes().isEmpty();
    }

    /**
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
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#allowSyntheticCreateMethod()
     */
    protected boolean handleIsSyntheticCreateMethodAllowed()
    {
        return EJBMetafacadeUtils.allowSyntheticCreateMethod(this);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getBusinessOperations()
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
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = transactionType =
                    String.valueOf(this.getConfiguredProperty(EJBGlobals.TRANSACTION_TYPE));
        }
        return transactionType;
    }
}