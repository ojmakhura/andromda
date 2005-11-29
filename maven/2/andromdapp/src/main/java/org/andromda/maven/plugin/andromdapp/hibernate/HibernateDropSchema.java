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
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#addArguments(java.util.List)
     */
    protected void addArguments(
        final Map options,
        final List arguments)
    {
        arguments.add("--output=" + this.getRequiredProperty(
                options,
                "dropOutputPath"));
        arguments.add("--drop");
    }
}