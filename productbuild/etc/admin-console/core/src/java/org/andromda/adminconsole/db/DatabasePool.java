package org.andromda.adminconsole.db;

import java.io.Serializable;

public interface DatabasePool extends Serializable
{
    public Table findTable(String tableName);

    public Column findColumn(String table, String columnName);

    public void register(Table table);

    public void deregister(Table table);

}
