package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.EJBAssociationFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationFacade
 */
public class EJBAssociationFacadeLogicImpl
        extends EJBAssociationFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EJBAssociationFacadeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // --------------- attributes ---------------------
    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationFacadeLogic#handleGetTableName()
     */
    public String getTableName()
    {
        String tableName = super.getTableName();
        if (getName().toLowerCase().startsWith(tableName.toLowerCase()))
        {
            tableName = getRelationName().replaceAll("-", "_").toUpperCase();
        }
        return tableName;
    }
}