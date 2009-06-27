package org.andromda.maven.plugin.andromdapp.hibernate;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Provides the ability to drop a schema from Hibernate
 * mapping files.
 *
 * @author Chad Brandon
 */
public class HibernateDropSchema
    extends HibernateSchemaManagement
{
    /**
     * The drop output path.
     */
    private static final String DROP_OUTPUT_PATH = "dropOutputPath";

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#addArguments(java.util.List)
     */
    protected void addArguments(
        final Map options,
        final List arguments)
    {
        final String outputPath = this.getRequiredProperty(
            options,
            DROP_OUTPUT_PATH);
        final File file = new File(outputPath);
        final File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        arguments.add("--output=" + outputPath);
        arguments.add("--text");
        arguments.add("--quiet");
        arguments.add("--drop");
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionOuputPath(java.util.Map)
     */
    protected String getExecutionOuputPath(final Map options)
    {
        return this.getRequiredProperty(
            options,
            DROP_OUTPUT_PATH);
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionClassName()
     */
    protected String getExecutionClassName()
    {
        return "SchemaExport";
    }
}