package org.andromda.cartridges.database.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.database.DatabaseGlobals;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.database.metafacades.AssociationTable.
 * 
 * @see org.andromda.cartridges.database.metafacades.AssociationTable
 * @author Chad Brandon
 * @author Wouter Zoons
 * @author Juan Carlos Gastélum Rocha
 */
public class AssociationTableLogicImpl extends AssociationTableLogic implements
                org.andromda.cartridges.database.metafacades.AssociationTable
{
    // ---------------- constructor -------------------------------

    public AssociationTableLogicImpl(Object metaObject, String context)
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

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#getPrimaryKeyColumns()
     */
    protected String handleGetPrimaryKeyColumns()
    {
        final StringBuffer columns = new StringBuffer();
        for (Iterator endIterator = this.getAssociationEnds().iterator(); endIterator.hasNext();)
        {
            Object object = endIterator.next();
            if (EntityAssociationEnd.class.isAssignableFrom(object.getClass()))
            {
                columns.append(((EntityAssociationEnd) object).getColumnName());
                if (endIterator.hasNext())
                {
                    columns.append(", ");
                }
            }
        }
        return columns.toString();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#getPrimaryKeyConstraintName()
     */
    protected String handleGetPrimaryKeyConstraintName()
    {
        return DatabaseMetafacadeUtils.toSqlIdentifierName(this
                        .getConfiguredProperty(DatabaseGlobals.PRIMARY_KEY_CONSTRAINT_PREFIX), this, this
                        .getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#isForeignKeyColumnsPresent()
     */
    protected boolean handleIsForeignKeyColumnsPresent()
    {
        return !this.getForeignKeyColumns().isEmpty();
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    public Short getMaxSqlNameLength()
    {
        return Short.valueOf((String) this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * Returns the dummy load size for the association table. The dummy load size of a association table is the dummy
     * load size of one of the association ends
     */
    public int getDummyLoadSize()
    {
        int dummyLoadSize = -1;

        // Get the initial load size for this association table, the load size of one of the association ends
        dummyLoadSize = ((ForeignKeyColumn) this.getForeignKeyColumns().toArray()[0]).getImportedTable()
                        .getDummyLoadSize();

        return dummyLoadSize;
    }

    /**
     * Returns a flag than indicate if the association table is an enumeration. Actually only returns false, due to an
     * association table can not be an enumeration. So, this service is only for compatibility purposes with the service
     * of the same name in a Table. Both values are used in the same way in the velocity template.
     */
    public boolean isEnumeration()
    {
        return false;
    }

    /**
     * Returns the collection of the none foreign keys that are presented in the association table. Actually only
     * returns an empty collection, due to an association table can not have non foreign key columns. So, this service
     * is only for compatibility purposes with the service of the same name in a Table. Both values are used in the same
     * way in the velocity template.
     */
    public Collection getNonForeignKeyColumns()
    {
        Collection nonForeignKeyColumns;

        nonForeignKeyColumns = new ArrayList();

        nonForeignKeyColumns.clear();

        return nonForeignKeyColumns;
    }
}
