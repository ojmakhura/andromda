package org.andromda.maven.plugin.andromdapp;


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
     * 
     * @throws Exception
     */
    public String execute(java.sql.Connection connection, java.util.Map options) throws Exception;
}
