package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.Database;
import org.andromda.adminconsole.db.DatabasePool;
import org.andromda.adminconsole.db.Table;
import org.andromda.adminconsole.db.TableType;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseImpl extends DatabaseObject implements Database
{
    private DatabaseMetaData metaData = null;
    private DatabasePool pool = null;

    private String catalog = null;
    private String schema = null;

    public DatabaseImpl(DatabaseMetaData metaData, String catalog, String schema)
    {
        this.metaData = metaData;
        this.pool = new DatabasePoolImpl();
        this.catalog = catalog;
        this.schema = schema;
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

    public Table[] getTables(TableType type) throws SQLException
    {
        return getTables(new TableType[]{type});
    }

    public Table[] getTables(TableType[] types) throws SQLException
    {
        String[] typeValues = null;

        if ((types != null) && (types.length > 0))
        {
            typeValues = new String[types.length];

            for (int i = 0; i < typeValues.length; i++)
            {
                typeValues[i] = types[i].getValue();
            }
        }

        List tables = new ArrayList();

        ResultSet tablesSet = null;
        try
        {
            tablesSet = metaData.getTables(catalog, schema, "%", typeValues);
            while (tablesSet.next())
            {
                String tableName = tablesSet.getString("TABLE_NAME");
                TableType tableType = TableType.get(tablesSet.getString("TABLE_TYPE"));
                tables.add(new TableImpl(this, tableName, tableType));
            }
        }
        finally
        {
            if (tablesSet != null)
            {
                try
                {
                    tablesSet.close();
                }
                catch (SQLException e)
                {
                    throw new RuntimeException("Unable to close resultset", e);
                }
            }
        }

        return (Table[]) tables.toArray(new Table[tables.size()]);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof DatabaseImpl)) return false;

        final DatabaseImpl database = (DatabaseImpl) o;

        if (catalog != null ? !catalog.equals(database.catalog) : database.catalog != null) return false;
        if (schema != null ? !schema.equals(database.schema) : database.schema != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (catalog != null ? catalog.hashCode() : 0);
        result = 29 * result + (schema != null ? schema.hashCode() : 0);
        return result;
    }
}
