package org.andromda.maven.plugin.andromdapp;

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
    public void execute(java.util.Map options) throws Exception;
}
