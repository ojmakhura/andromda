package org.andromda.cartridges.database.metafacades;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.cartridges.database.DatabaseGlobals;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityAssociationEndFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.DataColumn.
 * 
 * @see org.andromda.cartridges.database.metafacades.DataColumn
 */
public class ColumnLogicImpl
    extends ColumnLogic
    implements org.andromda.cartridges.database.metafacades.Column
{
    // ---------------- constructor -------------------------------

    public ColumnLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.DataColumn#getTable()
     */
    protected java.lang.Object handleGetTable()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.PrimaryKeyColumn#getExportedColumns()
     */
    protected java.util.Collection handleGetExportedColumns()
    {
        Collection exportedColumns = new HashSet();

        Collection associationEnds = getTable().getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = 
                (AssociationEndFacade)iterator.next();
            if (associationEnd.isOne2Many())
            {
                exportedColumns.add(associationEnd.getOtherEnd());
            }
        }

        return exportedColumns;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.PrimaryKeyColumn#getImportingTables()
     */
    protected java.util.Collection handleGetImportingTables()
    {
        Collection importingTables = new HashSet();

        Collection exportedColumns = getExportedColumns();
        for (Iterator iterator = exportedColumns.iterator(); iterator.hasNext();)
        {
            ForeignKeyColumn foreignKeyColumn = 
                (ForeignKeyColumn)iterator.next();
            importingTables.add(foreignKeyColumn.getTable());
        }

        return importingTables;
    }

    private final static DateFormat DATE_FORMATTER = new SimpleDateFormat(
        "yyyy-MM-dd");

    /**
     * @see org.andromda.cartridges.database.metafacades.NonForeignKeyColumn#getDummyLoadValue(int)
     */
    protected String handleGetDummyLoadValue(int index)
    {
        String initialLoadValue = null;

        final String type = getType().getFullyQualifiedName(true);

        if ("datatype.String".equals(type))
        {
            initialLoadValue = '\'' + getName() + '-' + index + '\'';
        }
        else if ("datatype.boolean".equals(type))
        {
            initialLoadValue = String.valueOf(index % 2 == 0);
        }
        else if ("datatype.int".equals(type) || "datatype.Integer".equals(type)
            || "datatype.short".equalsIgnoreCase(type)
            || "datatype.long".equalsIgnoreCase(type))
        {
            initialLoadValue = String.valueOf(index);
        }
        else if ("datatype.float".equalsIgnoreCase(type)
            || "datatype.double".equalsIgnoreCase(type))
        {
            initialLoadValue = String.valueOf(index) + ".555";
        }
        else if ("datatype.char".equals(type)
            || "datatype.Character".equals(type))
        {
            initialLoadValue = '\'' + Character.toString((char)index) + '\'';
        }
        else if ("datatype.Date".equals(type)
            || "datatype.DateTime".equals(type))
        {
            initialLoadValue = '\'' + DATE_FORMATTER.format(new Date()) + '\'';
        }
        else
        {
            initialLoadValue = "error";
        }

        final String maxLengthString = getColumnLength();
        int maxLength = 0;
        int dummyValueLength = initialLoadValue.length();

        try
        {
            maxLength = Integer.parseInt(maxLengthString);
        }
        catch (Exception ex)
        {
            maxLength = 0;
        }

        if (maxLength > 0 && dummyValueLength > maxLength)
        {
            initialLoadValue = initialLoadValue.substring(0, maxLength);
        }

        return initialLoadValue;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.NonForeignKeyColumn#isPrimaryKey()
     */
    protected boolean handleIsPrimaryKey()
    {
        return isIdentifier();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ColumnLogic#handleGetTableName()
     */
    protected String handleGetTableName()
    {
        String tableName = null;
        if (this.getTable() != null)
        {
            tableName = this.getTable().getTableName();
        }
        return tableName;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ColumnLogic#handleGetConstraintName()
     */
    protected String handleGetConstraintName()
    {
        return this
            .getIdentifierName(DatabaseGlobals.FOREIGN_KEY_CONSTRAINT_PREFIX);
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
        return DatabaseMetafacadeUtils.toSqlIdentifierName(
            this.getConfiguredProperty(prefixProperty),
            this.getImportedTable(),
            table,
            this.getMaxSqlNameLength());
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    private Short getMaxSqlNameLength()
    {
        return Short.valueOf((String)this.getConfiguredProperty(
            UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.ColumnLogic#handleGetImportedTable()
     */
    protected Object handleGetImportedTable()
    {
        Table table = null;
        EntityAssociationEndFacade end = this.getForeignIdentifierEnd();
        if (end != null
            && EntityAssociationEndFacade.class.isAssignableFrom(
                end.getOtherEnd().getClass()))
        {
            table = (Table)end.getOtherEnd().getType();
        }
        return table;
    }
    
    /**
     * @see org.andromda.cartridges.database.metafacades.Column#isCascadeDelete()
     */
    protected boolean handleIsCascadeDelete()
    {
        boolean cascadeDelete = false;
        AssociationEndFacade end = this.getForeignIdentifierEnd();
        if (end != null)
        {
            cascadeDelete = end.isComposition();
        }
        return cascadeDelete;
    }
    
    /**
     * Gets the association end that has the foreign identifier
     * flag set (if there is one).  
     * @return the foreign identifier association end or null if
     *         on can't be found.
     */
    private EntityAssociationEndFacade getForeignIdentifierEnd()
    {
        EntityAssociationEndFacade end = (EntityAssociationEndFacade)CollectionUtils
            .find(this.getOwner().getAssociationEnds(), new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    AssociationEndFacade end = (AssociationEndFacade)object;
                    boolean valid = false;
                    if (end != null
                        && EntityAssociationEndFacade.class.isAssignableFrom(
                            end.getClass()))
                    {
                        valid = ((EntityAssociationEndFacade)
                            end).isForeignIdentifier();
                    }
                    return valid;
                }
            });
        return end;
    }
}
