package org.andromda.maven.plugin.andromdapp;

import java.sql.Connection;
import java.util.Map;

/**
 * Represents a schema management task.
 * 
 * @author Chad Brandon
 */
public interface SchemaManagement
{
    /**
     * Executes the schema related task and returns the resulting SQL script (if applicable).
     * 
     * @param connection the connection if required to execute the task.
     * @param options the options used during execution.
     * @return SQL script
     * 
     * @throws Exception
     */
    public String execute(Connection connection, Map options) throws Exception;
}
