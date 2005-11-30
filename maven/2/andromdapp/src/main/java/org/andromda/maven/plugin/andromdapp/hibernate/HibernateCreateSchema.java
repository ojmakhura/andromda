package org.andromda.maven.plugin.andromdapp.hibernate;

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
        arguments.add("--output=" + this.getRequiredProperty(
                options,
                CREATE_OUTPUT_PATH));
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
}