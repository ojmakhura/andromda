package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.ejb.metafacades.EJBOperationFacade.
 * 
 * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade
 */
public class EJBOperationFacadeLogicImpl
    extends EJBOperationFacadeLogic
    implements org.andromda.cartridges.ejb.metafacades.EJBOperationFacade
{
    // ---------------- constructor -------------------------------

    public EJBOperationFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade#getTransactionType()
     */
    public java.lang.String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(
            EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE,
            true);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade#isBusinessOperation()
     */
    public boolean handleIsBusinessOperation()
    {
        return !this.hasStereotype(EJBProfile.STEREOTYPE_CREATE_METHOD)
            && !this.hasStereotype(EJBProfile.STEREOTYPE_FINDER_METHOD)
            && !this.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade#isSelectMethod()
     */
    public boolean handleIsSelectMethod()
    {
        return this.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD);
    }
}