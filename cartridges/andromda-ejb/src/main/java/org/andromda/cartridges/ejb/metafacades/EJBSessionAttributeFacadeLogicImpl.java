package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.EJBSessionAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionAttributeFacade
 */
public class EJBSessionAttributeFacadeLogicImpl
        extends EJBSessionAttributeFacadeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBSessionAttributeFacadeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionAttributeFacadeLogic#handleGetTransactionType()
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionAttributeFacade#getTransactionType()
     */
    protected String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }
}