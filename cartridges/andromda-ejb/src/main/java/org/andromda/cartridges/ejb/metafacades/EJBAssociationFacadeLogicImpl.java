package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;

import org.andromda.cartridges.ejb.EJBProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.ejb.metafacades.EJBAssociationFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationFacade
 */
public class EJBAssociationFacadeLogicImpl extends EJBAssociationFacadeLogic {
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBAssociationFacadeLogicImpl(Object metaObject, String context) {
        super(metaObject, context);
    }

    // --------------- attributes ---------------------
    /**
     * @return findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE)
     * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationFacade#getTransactionType()
     */
    protected String handleGetTransactionType() {
        return (String) this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
    }

    /**
     * @return tableName
     * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationFacadeLogic#getTableName()
     */
    public String getTableName() {
        String tableName = super.getTableName();
        if (getName().toLowerCase().startsWith(tableName.toLowerCase())) {
            tableName = getRelationName().replaceAll("-", "_").toUpperCase();
        }
        return tableName;
    }
}