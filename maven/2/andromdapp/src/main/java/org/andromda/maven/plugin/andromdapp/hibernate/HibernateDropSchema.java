package org.andromda.maven.plugin.andromdapp.hibernate;

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
        arguments.add("--output=" + this.getRequiredProperty(
                options,
                DROP_OUTPUT_PATH));
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
}