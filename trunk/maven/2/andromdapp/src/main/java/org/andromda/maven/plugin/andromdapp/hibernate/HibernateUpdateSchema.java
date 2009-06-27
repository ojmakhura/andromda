package org.andromda.maven.plugin.andromdapp.hibernate;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.andromda.core.common.Constants;
import org.andromda.core.common.ResourceWriter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;


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
     * The temporary directory.
     */
    private static final String HIBERNATE_PROPERTIES_TEMP_DIRECTORY =
        Constants.TEMPORARY_DIRECTORY + "andromdapp/hibernate-schema-update";

    /**
     * @see org.andromda.maven.plugin.andromdapp.hibernate.HibernateSchemaManagement#addArguments(java.util.Map, java.util.List)
     */
    protected void addArguments(
        final Map options,
        final List arguments)
        throws Exception
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
        contents.append("hibernate.connection.driver_class=" + driverClass + "\n");
        contents.append("hibernate.connection.url=" + connectionUrl + "\n");
        contents.append("hibernate.connection.username=" + username + "\n");
        contents.append("hibernate.connection.password=" + password + "\n");
        final File temporaryProperitesFile =
            new File(HIBERNATE_PROPERTIES_TEMP_DIRECTORY, new Random().nextDouble() + "");
        temporaryProperitesFile.deleteOnExit();
        ResourceWriter.instance().writeStringToFile(
            contents.toString(),
            temporaryProperitesFile.toString());
        arguments.add("--properties=" + temporaryProperitesFile);
    }

    /**
     * The class loader containing the jdbc driver.
     */
    private ClassLoader jdbcDriverJarLoader = null;

    /**
     * Sets the current context class loader from the given
     * <code>jdbcDriverJar</code>
     *
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     */
    protected ClassLoader getJdbcDriverJarLoader(final Map options)
        throws MalformedURLException
    {
        final String jdbcDriverJar = this.getRequiredProperty(
                options,
                "jdbcDriverJar");
        if (jdbcDriverJarLoader == null)
        {
            jdbcDriverJarLoader = new URLClassLoader(new URL[] {new File(jdbcDriverJar).toURI().toURL()});
        }
        return jdbcDriverJarLoader;
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