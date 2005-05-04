package org.andromda.cartridges.database.metafacades;

import java.util.Random;

import org.andromda.cartridges.database.DatabaseGlobals;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.ForeignKeyColumn.
 * 
 * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn
 */
public class ForeignKeyColumnLogicImpl
    extends ForeignKeyColumnLogic
    implements org.andromda.cartridges.database.metafacades.ForeignKeyColumn
{

    // ---------------- constructor -------------------------------

    public ForeignKeyColumnLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#isCascadeDelete()
     */
    protected boolean handleIsCascadeDelete()
    {
        return this.isComposition() || this.isMany2Many();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getTable()
     */
    protected java.lang.Object handleGetTable()
    {
        Object table = null;
        if (!this.isMany2Many())
        {
            table = this.getOtherEnd().getType();
        }
        return table;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getAssociationTable()
     */
    protected java.lang.Object handleGetAssociationTable()
    {
        Object table = null;
        if (this.isMany2Many())
        {
            table = this.getAssociation();
        }
        return table;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getImportedColumn()
     */
    protected java.lang.Object handleGetImportedTable()
    {
        return this.getType();
    }

    private final static Random RANDOM = new Random();

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getDummyLoadValue(int)
     */
    protected String handleGetDummyLoadValue(int index)
    {
        String initialLoadValue = null;
        Column importedColumn = this.getImportedTable().getPrimaryKeyColumn();
        if (importedColumn != null)
        {
            int randomValue = RANDOM.nextInt(Math.abs(importedColumn.getTable().getDummyLoadSize())) + 1;
            initialLoadValue = importedColumn.getDummyLoadValue(randomValue);
        }
        return initialLoadValue;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getConstraintName()
     */
    protected String handleGetConstraintName()
    {
        return this.getIdentifierName(
            DatabaseGlobals.FOREIGN_KEY_CONSTRAINT_PREFIX);
    }

    /**
     * Returns the actual table (i.e. the table or association table depending
     * on what type of association this foreign key column represents.
     * 
     * @return the table or association table.
     */
    private String getIdentifierName(String prefixProperty)
    {
        ModelElementFacade table = this.getTable();
        if (this.getAssociationTable() != null)
        {
            table = this.getAssociationTable();
        }
        return DatabaseMetafacadeUtils.toSqlIdentifierName(
            this.getConfiguredProperty(prefixProperty), 
            this.getImportedTable(), 
            table, 
            this.getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getIndexName()
     */
    protected String handleGetIndexName()
    {
        return this.getIdentifierName(DatabaseGlobals.INDEX_PREFIX);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getTableName()
     */
    protected String handleGetTableName()
    {
        StringBuffer tableName = new StringBuffer();
        if (this.getAssociationTable() != null)
        {
            tableName.append(this.getAssociationTable().getTableName());
        }
        else
        {
            tableName.append(this.getTable().getTableName());
        }
        return tableName.toString();
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    private Short getMaxSqlNameLength()
    {
        return Short
            .valueOf((String)this
                .getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ForeignKeyColumn#getImportedPrimaryKeyColumn()
     */
    protected Object handleGetImportedPrimaryKeyColumn()
    {
        return this.getImportedTable().getPrimaryKeyColumn();
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
