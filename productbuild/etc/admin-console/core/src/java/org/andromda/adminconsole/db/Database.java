package org.andromda.adminconsole.db;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface Database
{
    public String getCatalog();

    public String getSchema();

    public DatabaseMetaData getMetaData();

    public DatabasePool getPool();

    public Table[] getTables(TableType type) throws SQLException;

    public Table[] getTables(TableType[] types) throws SQLException;
}
