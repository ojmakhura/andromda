package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade
 */
public class EJBEntityAttributeFacadeLogicImpl
        extends EJBEntityAttributeFacadeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBEntityAttributeFacadeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true)
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade#getTransactionType()
     */
    protected String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    public java.lang.String getGetterName()
    {
        return "get" + StringUtils.capitalize(super.getName());
    }
}