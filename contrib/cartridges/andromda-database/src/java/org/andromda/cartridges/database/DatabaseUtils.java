package org.andromda.cartridges.database;

import org.andromda.cartridges.database.metafacades.ForeignKeyColumn;
import org.andromda.cartridges.database.metafacades.Table;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;

public class DatabaseUtils
{
    /**
     * Returns the table's columns that are not present in the collection.
     *
     * @param table cannot be null
     * @param columns if null this method will simply return all columns found on this table
     */
    public static Collection getOtherColumns(Table table, Collection columns)
    {
        final Collection otherColumns = new ArrayList(table.getColumns());

        if (columns != null)
        {
            otherColumns.removeAll(columns);
        }

        return otherColumns;
    }

    /**
     * <p>
     *  Returns an ordered map in which the keys are the tables in the order they need to be processed, the values
     *  (if not-null) represent this table's foreignkey columns that need to be updated after all tables have had
     *  their data inserted.
     * </p>
     * <p>
     *  This is interesting when generating a script that will populate a database with dummy data, some table
     *  rely on other tables (via foreign keys) and there for the targetted tables must contain data before the
     *  source table does.
     * </p>
     * <p>
     *  Sometimes a foreign key column can be nullable, in that case this map might value those columns as values,
     *  this denotes they should be updated once all data have been inserted.
     * </p>
     * <p>
     *  This operation will do the best it can to resolve any cycles, but nevertheless, it might happen that an
     *  endless loop is detected. If that happens it means you should consider allowing a nullable foreign key column
     *  in one or more of the remaining tables.
     * </p>
     *
     * @param tables cannot be null
     */
    public static Map resolveOrderedTableMap(Collection tables)
    {
        final Map orderedTableMap = new LinkedHashMap();

        // we will be removing element from this collection, and don't want to affect the argument
        tables = new HashSet(tables);
        boolean tableProcessed = true;  // initialization in order to enter the while-loop
        while (tables.isEmpty() == false)
        {
            // if no table has been processed in the previous cycle it means we won't in this cycle neither
            if (tableProcessed == false)
            {
                // we're in an unresolvable situation
                throw new RuntimeException("Cyclic table relationships detected between: "+tables);
            }

            tableProcessed = collectInsertableTables(tables, orderedTableMap);
            if (tableProcessed == false)
            {
                tableProcessed = resolveUpdateableTables(tables, orderedTableMap);
            }
        }

        return orderedTableMap;
    }

    /**
     * <p>
     *  Collects those tables that can completely and safely be inserted, each one of those tables will be
     *  moved from the collection into the map.
     * </p>
     * <p>
     *  It is safe to insert a table when non of the foreign key columns import a table that hasn't been processed.
     * </p>
     * <p>
     *  The remaining tables represent a cyclic graph and need to be resolved by analyzing the nullability of their
     * foreign key columns.
     * </p>
     * <p>
     *  When this method inserts a table into the map it assigns null to it.
     * </p>
     *
     * @param tablesToProcess cannot be null
     * @param processedTableMap cannot be null
     * @return true when a table has been moved into the map
     * @see #resolveUpdateableTables
     */
    private static boolean collectInsertableTables(Collection tablesToProcess, Map processedTableMap)
    {
        boolean inserted = false;

        for (Iterator tableIterator = tablesToProcess.iterator(); tableIterator.hasNext();)
        {
            final Table table = (Table) tableIterator.next();
            if (isInsertable(table, processedTableMap))
            {
                tableIterator.remove();
                processedTableMap.put(table, null);
                inserted = true;
            }
        }

        return inserted;
    }

    /**
     * Checks whether the table can safely be inserted into the map, this means all its foreign key columns
     * import tables that are already present in the map.
     *
     * @param table cannot be null
     * @param processedTableMap cannot be null
     * @return true if this table is insertable, false otherwise
     */
    private static boolean isInsertable(Table table, Map processedTableMap)
    {
        boolean insertable = true;

        Collection foreignKeyColumns = table.getForeignKeyColumns();
        for (Iterator foreignKeyIterator = foreignKeyColumns.iterator(); foreignKeyIterator.hasNext() && insertable;)
        {
            final ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) foreignKeyIterator.next();
            if (!processedTableMap.containsKey(foreignKeyColumn.getImportedTable()))
            {
                insertable = false;
            }
        }

        return insertable;
    }

    /**
     * <p>
     *  Analyzes the tables by looking at their foreign key columns. This method will look for tables which
     *  non-nullable foreign key columns reference only known tables. If none can be found it means that
     *  it is impossible to populate them using dummy data.
     * </p>
     * <p>
     *  This method stops resolving as soon as it was possible to move a table into the map, this allows the
     *  <code>collectInsertableTables(..)</code> method to find new tables that can have their data inserted in
     *  one shot (which is more performant).
     * </p>
     * <p>
     *  When this method inserts a table into the map it assigns to it all columns that cannot be inserted in one shot
     *  and need to be updated after all tables have been populated (this is optional though).
     * </p>
     *
     * @return true if a table has been been resolved, false otherwise
     * @see #collectInsertableTables
     */
    private static boolean resolveUpdateableTables(Collection tablesToProcess, Map processedTableMap)
    {
        boolean resolved = false;

        for (Iterator tableIterator = tablesToProcess.iterator(); tableIterator.hasNext() && !resolved;)
        {
            final Table table = (Table) tableIterator.next();
            Collection updateableColumns = getUpdateableColumns(table, processedTableMap);
            if (updateableColumns.isEmpty() == false)
            {
                tableIterator.remove();
                processedTableMap.put(table, updateableColumns);
                resolved = true;
            }
        }

        return resolved;
    }

    /**
     * <p>
     *  Checks whether it is possible to update the foreign keys once the insert has been done.
     * </p>
     * <p>
     *  This method returns those foreign key columns that are nullable and import a table that is not present
     *  in the map.
     * </p>
     *
     * @param table cannot be null
     * @param processedTableMap cannot be null
     * @return nullable foreign key columns that import tables not in the map
     */
    private static Collection getUpdateableColumns(Table table, Map processedTableMap)
    {
        final Collection updateableColumns = new ArrayList();

        Collection foreignKeyColumns = table.getForeignKeyColumns();
        for (Iterator foreignKeyIterator = foreignKeyColumns.iterator(); foreignKeyIterator.hasNext();)
        {
            ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) foreignKeyIterator.next();

            // only update those ones that really can't be inserted the first time
            // (nullable foreign key columns that import a table not present in the map)
            if (!foreignKeyColumn.isRequired() && !processedTableMap.containsKey(foreignKeyColumn.getImportedTable()))
            {
                updateableColumns.add(foreignKeyColumn);
            }
        }
        return updateableColumns;
    }
}
