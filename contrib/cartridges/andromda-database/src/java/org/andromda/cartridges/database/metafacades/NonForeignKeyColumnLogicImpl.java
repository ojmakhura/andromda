package org.andromda.cartridges.database.metafacades;

import org.andromda.metafacades.uml.AssociationEndFacade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.database.metafacades.DataColumn.
 *
 * @see org.andromda.cartridges.database.metafacades.DataColumn
 */
public class NonForeignKeyColumnLogicImpl
       extends NonForeignKeyColumnLogic
       implements org.andromda.cartridges.database.metafacades.NonForeignKeyColumn
{
    // ---------------- constructor -------------------------------

    public NonForeignKeyColumnLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.database.metafacades.DataColumn#getTable()
     */
    protected java.lang.Object handleGetTable()
    {
        return getOwner();
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
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
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
            ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) iterator.next();
            importingTables.add(foreignKeyColumn.getTable());
        }

        return importingTables;
    }

    private final static DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

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
        else if ("datatype.int".equals(type) || "datatype.Integer".equals(type) || "datatype.short".equalsIgnoreCase(type) || "datatype.long".equalsIgnoreCase(type))
        {
            initialLoadValue = String.valueOf(index);
        }
        else if ("datatype.float".equalsIgnoreCase(type) || "datatype.double".equalsIgnoreCase(type))
        {
            initialLoadValue = String.valueOf(index) + ".555";
        }
        else if ("datatype.char".equals(type) || "datatype.Character".equals(type))
        {
            initialLoadValue = '\'' + Character.toString((char)index) + '\'';
        }
        else if ("datatype.Date".equals(type) || "datatype.DateTime".equals(type))
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
        catch (Exception e)
        {
            maxLength = 0;
        }

        if (maxLength >0 && dummyValueLength > maxLength)
        {
            initialLoadValue = initialLoadValue.substring(0, maxLength);
        }

        return initialLoadValue;
    }

    protected boolean handleIsPrimaryKey()
    {
        return isIdentifier();
    }
}
