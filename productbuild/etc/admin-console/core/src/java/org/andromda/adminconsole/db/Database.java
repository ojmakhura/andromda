package org.andromda.adminconsole.db;

import java.io.Serializable;
import java.sql.DatabaseMetaData;

public interface Database extends Serializable
{
    public String getCatalog();

    public String getSchema();

    public DatabaseMetaData getMetaData();

    public String[] getAllTableNames();

    public Table findTable(String tableName);
}
