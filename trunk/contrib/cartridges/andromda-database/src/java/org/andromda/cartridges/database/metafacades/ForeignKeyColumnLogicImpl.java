package org.andromda.cartridges.database.metafacades;

import java.util.Random;

import org.andromda.cartridges.database.DatabaseGlobals;
import org.andromda.cartridges.database.DatabaseProfile;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.core.common.StringUtilsHelper;
import org.apache.log4j.Priority;

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

    protected Boolean handleGetConsoleHide()
    {
        Boolean hide = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_HIDE);
        if (taggedValue != null)
            hide = Boolean.valueOf(String.valueOf(taggedValue));

        return hide;
    }

    protected Boolean handleGetConsoleSortable()
    {
        Boolean sortable = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_SORTABLE);
        if (taggedValue != null)
            sortable = Boolean.valueOf(String.valueOf(taggedValue));

        return sortable;
    }

    protected Boolean handleGetConsoleExportable()
    {
        Boolean exportable = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_EXPORTABLE);
        if (taggedValue != null)
            exportable = Boolean.valueOf(String.valueOf(taggedValue));

        return exportable;
    }

    protected Integer handleGetConsoleSize()
    {
        Integer size = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_SIZE);
        if (taggedValue != null)
        {
            try
            {
                size = new Integer(String.valueOf(taggedValue));
            }
            catch (NumberFormatException e)
            {
                if (logger.isEnabledFor(Priority.WARN))
                {
                    logger.warn(
                            "Invalid " + DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_SIZE +
                            " value on column "+getFullyQualifiedName()+", ignoring: not an integer", e);
                }
                size = null;
            }
        }

        return size;
    }

    protected Boolean handleGetConsoleUpdateable()
    {
        Boolean updateable = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_UPDATEABLE);
        if (taggedValue != null)
            updateable = Boolean.valueOf(String.valueOf(taggedValue));

        return updateable;
    }

    protected Boolean handleGetConsoleResolveForeignKeys()
    {
        Boolean resolveForeignKeys = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_RESOLVEFK);
        if (taggedValue != null)
            resolveForeignKeys = Boolean.valueOf(String.valueOf(taggedValue));

        return resolveForeignKeys;
    }

    protected String handleGetConsoleDisplayName()
    {
        String displayName = null;

        Object taggedValue = findTaggedValue(DatabaseProfile.TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_DISPLAYNAME);
        if (taggedValue == null)
        {
            displayName = StringUtilsHelper.toPhrase(getName());
        }
        else
        {
            displayName = String.valueOf(taggedValue);
        }

        return displayName;
    }
}
