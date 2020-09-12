package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;

import org.andromda.cartridges.ejb.EJBProfile;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade
 */
public class EJBEntityAttributeFacadeLogicImpl extends EJBEntityAttributeFacadeLogic {
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBEntityAttributeFacadeLogicImpl(Object metaObject, String context) {
        super(metaObject, context);
    }

    /**
     * @return findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true)
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityAttributeFacade#getTransactionType()
     */
    protected String handleGetTransactionType() {
        return (String) this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    public String getGetterName() {
        return "get" + StringUtils.capitalize(super.getName());
    }
}