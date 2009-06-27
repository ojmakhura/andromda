package org.andromda.maven.plugin.andromdapp.hibernate;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Provides the ability to create a schema from Hibernate
 * mapping files.
 *
 * @author Chad Brandon
 */
public class HibernateCreateSchema
    extends HibernateSchemaManagement
{
    private static final String CREATE_OUTPUT_PATH = "createOutputPath";

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#addArguments(java.util.Map, java.util.List)
     */
    protected void addArguments(
        final Map options,
        final List arguments)
    {
        String outputPath = this.getRequiredProperty(
            options,
            CREATE_OUTPUT_PATH);
        final File file = new File(outputPath);
        final File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        arguments.add("--output=" + outputPath);
        arguments.add("--text");
        arguments.add("--quiet");
        arguments.add("--create"); //don't generate drop statements (the HibernateDropSchema does this) 
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionOuputPath(java.util.Map)
     */
    protected String getExecutionOuputPath(final Map options)
    {
        return this.getRequiredProperty(
            options,
            CREATE_OUTPUT_PATH);
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionClassName()
     */
    protected String getExecutionClassName()
    {
        return "SchemaExport";
    }
}