package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade.
 * 
 * @see org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade
 */
public class EJBEntityAttributeFacadeLogicImpl
    extends EJBEntityAttributeFacadeLogic
    implements org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade
{
    // ---------------- constructor -------------------------------

    public EJBEntityAttributeFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade#getTransactionType()
     */
    public java.lang.String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(
            EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE,
            true);
    }
}