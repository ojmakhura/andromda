package org.andromda.cartridges.database.metafacades;

import java.util.Random;

import org.andromda.metafacades.uml.EntityMetafacadeUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.database.metafacades.ForeignKeyColumn.
 *
 * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn
 */
public class ForeignKeyColumnLogicImpl
       extends ForeignKeyColumnLogic
       implements org.andromda.cartridges.database.metafacades.ForeignKeyColumn
{

    // ---------------- constructor -------------------------------

    public ForeignKeyColumnLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#isCascadeDelete()
     */
    protected boolean handleIsCascadeDelete()
    {
        return this.isComposition();
    }
    
    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getTable()
     */
    protected java.lang.Object handleGetTable()
    {
        return getOtherEnd().getType();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getImportedColumn()
     */
    protected java.lang.Object handleGetImportedTable()
    {
        return getType();
    }
    
    private final static Random RANDOM = new Random();

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getDummyLoadValue(int)
     */
    protected String handleGetDummyLoadValue(int index)
    {
        String initialLoadValue = null;

        NonForeignKeyColumn importedColumn = getImportedTable().getPrimaryKeyColumn();

        if (importedColumn != null)
        {
            int randomValue = RANDOM.nextInt(importedColumn.getTable().getDummyLoadSize()) + 1;
            initialLoadValue = importedColumn.getDummyLoadValue(randomValue);
        }

        return initialLoadValue;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getConstraintName()
     */
    protected String handleGetConstraintName()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("FK");
        buffer.append(getImportedTable().getTableName());
        buffer.append(getTable().getTableName());

        return EntityMetafacadeUtils.ensureMaximumNameLength(buffer.toString(), getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getIndexName()
     */
    protected String handleGetIndexName()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("IDX");
        buffer.append(getImportedTable().getTableName());
        buffer.append(getTable().getTableName());

        return EntityMetafacadeUtils.ensureMaximumNameLength(buffer.toString(), getMaxSqlNameLength());
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    private Short getMaxSqlNameLength()
    {
        return Short.valueOf((String)this.getConfiguredProperty("maxSqlNameLength"));
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getImportedPrimaryKeyColumn()
     */
    protected Object handleGetImportedPrimaryKeyColumn()
    {
        return getImportedTable().getPrimaryKeyColumn();
    }
    
    /**
     * Override to make many-to-many relations always required.
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    public boolean isRequired()
    {
        return super.isRequired() || super.isMany2Many();
    }
}
