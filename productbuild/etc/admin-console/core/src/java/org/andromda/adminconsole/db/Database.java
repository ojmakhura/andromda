package org.andromda.adminconsole.db;

import java.io.Serializable;
import java.sql.DatabaseMetaData;

/**
 * Represent a database.
 */
public interface Database extends Serializable
{
    /**
     * @return the database catalog, may be <code>null</code>
     */
    public String getCatalog();

    /**
     * @return the database schema, may be <code>null</code>
     */
    public String getSchema();

    /**
     * @return the database metadata
     */
    public DatabaseMetaData getMetaData();

    /**
     * @return the names of all tables in this database
     */
    public String[] getAllTableNames();

    /**
     * @return the table with the specified name in this database, may be <code>null</code>
     *  in case the table cannot be located
     */
    public Table findTable(String tableName);
}
