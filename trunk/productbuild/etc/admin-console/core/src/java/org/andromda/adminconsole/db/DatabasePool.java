package org.andromda.adminconsole.db;

public interface DatabasePool
{
    public Table findTable(String tableName);

    public Column findColumn(String table, String columnName);

    public void register(Table table);

    public void deregister(Table table);

}
