package org.andromda.maven.plugin.andromdapp.hibernate;

import org.andromda.core.common.ResourceWriter;
import org.andromda.core.common.Constants;

import java.util.Map;
import java.util.List;
import java.util.Random;
import java.io.File;

/**
 * Provides the ability to validate a schema from Hibernate
 * mapping files.
 *
 * @author Wouter Zoons
 */
public class HibernateValidateSchema
    extends HibernateSchemaManagement
{
    private static final String HIBERNATE_PROPERTIES_TEMP_DIRECTORY =
        Constants.TEMPORARY_DIRECTORY + "andromdapp/hibernate-schema-validate";

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionClassName()
     */
    protected String getExecutionClassName()
    {
        return "SchemaValidator";
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#getExecutionOuputPath(java.util.Map)
     */
    protected String getExecutionOuputPath(final Map options)
    {
        return null;
    }

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#addArguments(java.util.Map, java.util.List)
     */
    protected void addArguments(final Map options, final List arguments) throws Exception
    {
        final String driverClass = this.getRequiredProperty(
                options,
                "jdbcDriver");
        final String connectionUrl = this.getRequiredProperty(
                options,
                "jdbcConnectionUrl");
        final String username = this.getRequiredProperty(
                options,
                "jdbcUsername");
        final String password = this.getRequiredProperty(
                options,
                "jdbcPassword");
        final StringBuffer contents = new StringBuffer();
        contents.append("hibernate.connection.driver_class=").append(driverClass).append('\n');
        contents.append("hibernate.connection.url=").append(connectionUrl).append('\n');
        contents.append("hibernate.connection.username=").append(username).append('\n');
        contents.append("hibernate.connection.password=").append(password).append('\n');
        final File temporaryProperitesFile =
            new File(HIBERNATE_PROPERTIES_TEMP_DIRECTORY, String.valueOf(new Random().nextDouble()));
        temporaryProperitesFile.deleteOnExit();
        ResourceWriter.instance().writeStringToFile(
            contents.toString(),
            temporaryProperitesFile.toString());
        arguments.add("--properties=" + temporaryProperitesFile);
    }
}
