package org.andromda.cartridges.database.metafacades;

import org.andromda.cartridges.database.DatabaseProfile;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.database.metafacades.Table.
 *
 * @see org.andromda.cartridges.database.metafacades.Table
 */
public class TableLogicImpl
       extends TableLogic
       implements org.andromda.cartridges.database.metafacades.Table
{
    // ---------------- constructor -------------------------------

    public TableLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getInitialLoadSize()
     */
    protected int handleGetInitialLoadSize()
    {
        int initialLoadSize = 0;

        try
        {
            String initialLoadSizeString = (String)findTaggedValue(DatabaseProfile.TAGGEDVALUE_DUMMYLOAD_SIZE);
            initialLoadSize = Integer.parseInt(initialLoadSizeString);
        }
        catch (Exception e)
        {
            initialLoadSize = DatabaseProfile.DUMMY_LOAD_SIZE_DEFAULT;
        }

        return initialLoadSize;
    }
    
    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getForeignKeyColumns()
     */
    protected java.util.Collection handleGetForeignKeyColumns()
    {
        Collection foreignKeyColumns = new ArrayList();

        Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            AssociationEndFacade otherAssociationEnd = associationEnd.getOtherEnd();
            if (associationEnd.isMany2One() && otherAssociationEnd.isNavigable())
            {
                foreignKeyColumns.add( otherAssociationEnd );
            }
        }

        return foreignKeyColumns;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getPrimaryKeyColumns()
     */
    protected Object handleGetPrimaryKeyColumn()
    {
        Collection identifiers = getIdentifiers();
        return (identifiers.isEmpty()) ? null : identifiers.iterator().next();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getImportingTables()
     */
    protected java.util.Collection handleGetImportingTables()
    {
        Collection importingTables = new HashSet();

        Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            if (associationEnd.isOne2Many())
            {
                importingTables.add(associationEnd.getOtherEnd().getType());
            }
        }

        return importingTables;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Table#getImportedTables()
     */
    protected java.util.Collection handleGetImportedTables()
    {
        Collection importedTables = new HashSet();

        Collection foreignKeyColumns = getForeignKeyColumns();
        for (Iterator iterator = foreignKeyColumns.iterator(); iterator.hasNext();)
        {
            ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) iterator.next();
            importedTables.add(foreignKeyColumn.getTable());
        }

        return importedTables;
    }

    protected boolean handleIsForeignKeyColumnsPresent()
    {
        return getForeignKeyColumns().isEmpty() == false;
    }

    protected String handleGetPrimaryKeyConstraintName()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("XPK");
        buffer.append(getTableName());

        return EntityMetafacadeUtils.ensureMaximumNameLength(buffer.toString(), getMaxSqlNameLength());
    }

    protected Collection handleGetNonForeignKeyColumns()
    {
        return getAttributes();
    }
}
