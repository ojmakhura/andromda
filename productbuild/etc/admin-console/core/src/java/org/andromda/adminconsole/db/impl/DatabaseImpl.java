package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.Database;
import org.andromda.adminconsole.db.Table;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseImpl extends DatabaseObject implements Database
{
    private final Map tableMap = new HashMap();

    private DatabaseMetaData metaData = null;
    private String catalog = null;
    private String schema = null;

    public DatabaseImpl(DatabaseMetaData metaData, String catalog, String schema)
    {
        this.metaData = metaData;
        this.catalog = catalog;
        this.schema = schema;
    }

    public String[] getAllTableNames()
    {
        String[] allTableNames = null;

        try
        {
            List namesList = new ArrayList();

            ResultSet tables = metaData.getTables(catalog, schema, "%", new String[] { "TABLE" });
            while (tables.next())
            {
                String name = tables.getString("TABLE_NAME");
                namesList.add(name);
            }

            allTableNames = (String[]) namesList.toArray(new String[namesList.size()]);
        }
        catch (Exception e)
        {
            // @todo: log this exception
            allTableNames = new String[0];
        }

        return allTableNames;
    }

    public Table findTable(String tableName)
    {
        Table table = null;

        if (tableMap.containsKey(tableName))
        {
            table = (Table)tableMap.get(table);
        }
        else
        {
            table = new TableImpl(this, tableName);
            tableMap.put(tableName, table);
        }

        return table;
    }

    public DatabaseMetaData getMetaData()
    {
        return metaData;
    }

    public String getCatalog()
    {
        return catalog;
    }

    public String getSchema()
    {
        return schema;
    }

}
