package org.andromda.maven.plugin.andromdapp;

import java.sql.Connection;

/**
 * Represents a schema management task.
 * 
 * @author Chad Brandon
 */
public interface SchemaManagement
{
    /**
     * Executes the schema related task.
     * 
     * @param options the options used during execution.
     * 
     * @throws Exception
     */
    public void execute(Connection connection, java.util.Map options) throws Exception;
}
