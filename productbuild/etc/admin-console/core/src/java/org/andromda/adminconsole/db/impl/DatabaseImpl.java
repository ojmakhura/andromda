package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseImpl extends DatabaseObject implements Database
{
    private DatabaseMetaData metaData = null;
    private DatabasePool pool = null;
    private List allTables = null;

    private String catalog = null;
    private String schema = null;

    public DatabaseImpl(DatabaseMetaData metaData, String catalog, String schema)
    {
        this.metaData = metaData;
        this.pool = new DatabasePoolImpl();
        this.catalog = catalog;
        this.schema = schema;

        loadMetaData();
    }

    private void loadMetaData()
    {
        // read all tables
        allTables = readAllTables();

        for (Iterator iterator = allTables.iterator(); iterator.hasNext();)
        {
            TableImpl table = (TableImpl) iterator.next();
            registerForeignKeysToPrimaryKeys(table);
        }
    }

    /**
     * Reads all tables for this database.
     */
    private List readAllTables()
    {
        List allTables = new ArrayList();
        ResultSet tables = null;
        try
        {
            // get the list of tables for this database
            tables = metaData.getTables(catalog, schema, "%", new String[] {"TABLE"});
            while (tables.next())
            {
                // read the name
                String tableName = tables.getString("TABLE_NAME");

                // create a new table instance
                Table table = new TableImpl(this, tableName);
                // store it in the list to return
                allTables.add(table);
            }
            return allTables;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error while reading all tables",e);
        }
        finally
        {
            close(tables);
        }
    }

    /**
     * Loops over the primary keys of the argument table and locates all columns importing the
     * corresponding primary key column.
     */
    private void registerForeignKeysToPrimaryKeys(TableImpl pkTable)
    {
        ResultSet exportedKeys = null;

        try
        {
            // get those columns that are a foreign key to the argument table
            // (they 'import' it's primary key column, so this table 'exports' it)
            exportedKeys = getMetaData().getExportedKeys(catalog, schema, pkTable.getName());
            while (exportedKeys.next())
            {
                // get the referencing table name and foreign key column name
                String fkTableName = exportedKeys.getString("FKTABLE_NAME");
                String fkColumnName = exportedKeys.getString("FKCOLUMN_NAME");
                // get the name of the column it is referencing
                String pkColumnName = exportedKeys.getString("PKCOLUMN_NAME");

                // this should never be null, since the metadata just returned the table's name
                Table fkTable = (Table)pool.findTable(fkTableName);
                // this should never be null, since the metadata just returned the column's name
                // it should also be an foreign key column, otherwise there's a bug in the code
                ForeignKeyColumn fkColumn = (ForeignKeyColumn) fkTable.getColumn(fkColumnName);
                // again, unless there's a bug in the code this should nicely return the proper primary key column
                // we need to cast to the implementation class because we need a non-public feature
                PrimaryKeyColumnImpl pkColumn = (PrimaryKeyColumnImpl) pkTable.getColumn(pkColumnName);

                // assign the foreign key column to the set of exported key columns
                pkColumn.addExportedKeyColumn(fkColumn);

                // also register the tables properly
                pkTable.addImportingTable(fkTable);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error while registering foreign keys to primary keys",e);
        }
        finally
        {
            close(exportedKeys);
        }
    }

    public DatabaseMetaData getMetaData()
    {
        return metaData;
    }

    public DatabasePool getPool()
    {
        return pool;
    }

    public String getCatalog()
    {
        return catalog;
    }

    public String getSchema()
    {
        return schema;
    }

    public Table[] getTables()
    {
        // we return a different instance of the array each time so that modifications would not cause weird behavior
        return (Table[]) allTables.toArray(new Table[allTables.size()]);
    }
}
