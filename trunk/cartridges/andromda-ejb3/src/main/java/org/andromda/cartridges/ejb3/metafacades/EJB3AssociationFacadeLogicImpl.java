package org.andromda.cartridges.ejb3.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3AssociationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3AssociationFacade
 */
public class EJB3AssociationFacadeLogicImpl
    extends EJB3AssociationFacadeLogic
{

    // ---------------- constructor -------------------------------

	public EJB3AssociationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------

    /**
     * Returns the EJB3 cartridge specific table name for the association
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