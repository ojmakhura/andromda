package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.DatabasePool;
import org.andromda.adminconsole.db.Table;

import java.util.HashMap;
import java.util.Map;

public class DatabasePoolImpl implements DatabasePool
{
    private final Map tablePool = new HashMap();

    public Table findTable(String tableName)
    {
        return (Table) tablePool.get(tableName);
    }

    public Column findColumn(String tableName, String columnName)
    {
        Column tableColumn = null;

        Table table = findTable(tableName);
        if (table != null)
        {
            Column[] columns = table.getColumns();
            for (int i = 0; i < columns.length && tableColumn == null; i++)
            {
                Column column = columns[i];
                if (columnName.equals(column.getName()))
                {
                    tableColumn = column;
                }
            }
        }

        return tableColumn;
    }

    public void register(Table table)
    {
        tablePool.put(table.getName(), table);
    }

    public void deregister(Table table)
    {
        tablePool.remove(table.getName());
    }
}
