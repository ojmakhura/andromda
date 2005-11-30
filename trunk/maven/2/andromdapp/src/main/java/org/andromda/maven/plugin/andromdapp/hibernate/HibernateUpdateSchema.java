package org.andromda.maven.plugin.andromdapp.hibernate;

import java.util.List;
import java.util.Map;


/**
 * Provides the ability to create a schema from Hibernate
 * mapping files.
 *
 * @author Chad Brandon
 */
public class HibernateUpdateSchema
    extends HibernateSchemaManagement
{
    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#addArguments(java.util.Map, java.util.List)
     */
    protected void addArguments(
        final Map options,
        final List arguments)
    {
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionOuputPath(java.util.Map)
     */
    protected String getExecutionOuputPath(final Map options)
    {
        return null;
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionClassName()
     */
    protected String getExecutionClassName()
    {
        return "SchemaUpdate";
    }
}