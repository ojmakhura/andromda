package org.andromda.cartridges.database.metafacades;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.AssociationTable.
 * 
 * @see org.andromda.cartridges.database.metafacades.AssociationTable
 */
public class AssociationTableLogicImpl
    extends AssociationTableLogic
    implements org.andromda.cartridges.database.metafacades.AssociationTable
{
    // ---------------- constructor -------------------------------

    public AssociationTableLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#getForeignKeyColumns()
     */
    protected java.util.Collection handleGetForeignKeyColumns()
    {
        return this.getAssociationEnds();
    }

}
