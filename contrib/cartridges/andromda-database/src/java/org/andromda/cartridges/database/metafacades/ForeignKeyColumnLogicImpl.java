package org.andromda.cartridges.database.metafacades;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.cartridges.database.DatabaseProfile;

import java.util.Random;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.database.metafacades.ForeignKeyColumn.
 *
 * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn
 */
public class ForeignKeyColumnLogicImpl
       extends ForeignKeyColumnLogic
       implements org.andromda.cartridges.database.metafacades.ForeignKeyColumn
{
    private final static Random RANDOM = new Random();

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
        boolean isCascadeDelete = false;
        isCascadeDelete = this.getOtherEnd().isComposition();
        AssociationEndFacade otherEnd = this.getOtherEnd();
        // check for assignable so we don't get class cast exceptions
        if (otherEnd != null
            && EntityFacade.class.isAssignableFrom(otherEnd.getType()
                .getClass()))
            if (otherEnd.isOne2One()
                && ((EntityFacade)otherEnd.getType()).isChild())
            {
                isCascadeDelete = this.isComposition();
            }
        return isCascadeDelete;
    }
    
    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getInitialLoadReferences()
     */
    protected int handleGetInitialLoadReferences()
    {
        int initialLoadReferences = 0;

        try
        {
            String initialLoadReferencesString = (String)findTaggedValue(DatabaseProfile.INITIAL_LOAD_REFERENCES);
            initialLoadReferences = Integer.parseInt(initialLoadReferencesString);
        }
        catch (Exception e)
        {
            initialLoadReferences = DatabaseProfile.INITIAL_LOAD_REFERENCES_DEFAULT;
        }

        return initialLoadReferences;
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

    protected String handleGetInitialLoadValue(int index)
    {
        String initialLoadValue = null;

        NonForeignKeyColumn importedColumn = getImportedTable().getPrimaryKeyColumn();

        if (importedColumn != null)
        {
            int randomValue = RANDOM.nextInt(importedColumn.getTable().getInitialLoadSize()) + 1;
            initialLoadValue = importedColumn.getInitialLoadValue(randomValue);
        }

        return initialLoadValue;
    }

    protected String handleGetConstraintName()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("FK");
        buffer.append(getImportedTable().getTableName());
        buffer.append(getTable().getTableName());

        return EntityMetafacadeUtils.ensureMaximumNameLength(buffer.toString(), getMaxSqlNameLength());
    }

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

    protected Object handleGetImportedPrimaryKeyColumn()
    {
        return getImportedTable().getPrimaryKeyColumn();
    }
}
