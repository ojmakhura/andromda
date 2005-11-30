package org.andromda.maven.plugin.andromdapp.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Method;

import java.net.URL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.maven.plugin.andromdapp.SchemaManagement;
import org.andromda.maven.plugin.andromdapp.SchemaManagementException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * A Hibernate management object.
 *
 * @author Chad Brandon
 */
public abstract class HibernateSchemaManagement
    implements SchemaManagement
{
    protected static Logger logger = Logger.getLogger(HibernateSchemaManagement.class);

    /**
     * The Hibernate 2 schema export class name.
     */
    protected static final String HIBERNATE_2_SCHEMA_EXPORT = "net.sf.hibernate.tool.hbm2ddl.SchemaExport";

    /**
     * The Hibernate 3 schema export class name.
     */
    protected static final String HIBERNATE_3_SCHEMA_EXPORT = "org.hibernate.tool.hbm2ddl.SchemaExport";

    /**
     * The Hibernate version
     */
    private String version;

    /**
     * Sets the version of Hibernate to target.
     *
     * @param version the version of Hibernate to target.
     */
    public void setVersion(final String version)
    {
        this.version = version;
    }

    /**
     * Attempts to retrieve the schema export class.
     *
     * @return the schema export class.
     */
    protected Class getSchemaExportClass()
    {
        Class hibernateClass = null;
        try
        {
            hibernateClass = ClassUtils.loadClass(HIBERNATE_3_SCHEMA_EXPORT);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            // - ignore, means we can't find the Hibernate 3 class
        }
        try
        {
            hibernateClass = ClassUtils.loadClass(HIBERNATE_2_SCHEMA_EXPORT);
        }
        catch (Exception exception)
        {
            // - ignore, means we can't find the Hibernate 3 class
        }
        if (hibernateClass == null)
        {
            throw new RuntimeException("There appear to be no Hibernate 2 or 3 jars are your classpath");
        }
        return hibernateClass;
    }

    /**
     * Returns the current Hibernate version this management object
     * is targetting.
     *
     * @return the Hibernate version (2 or 3).
     */
    protected String getVersion()
    {
        return this.version;
    }

    /**
     * Stores the path to which output should be written.
     */
    private String outputPath;

    /**
     * Sets the path to which the output should be written.
     *
     * @param outputPath
     */
    public void setOutputPath(final String outputPath)
    {
        this.outputPath = outputPath;
    }

    /**
     * Attempts to retrieve the given property with the given <code>name</code>.
     * If the property isn't found an exception is thrown.
     *
     * @param properties the properties from which to retrieve the property.
     * @param name the name of the property to retrieve.
     * @return the value of the property.
     */
    protected String getRequiredProperty(
        final Map properties,
        final String name)
    {
        final String value = ObjectUtils.toString(properties.get(name));
        if (value == null || value.trim().length() == 0)
        {
            throw new SchemaManagementException("The '" + name + "' must be specified");
        }
        return value;
    }

    /**
     * Gets the path to which output should be written.
     *
     * @return the output path.
     */
    protected String getOutputPath()
    {
        return this.outputPath;
    }

    /**
     * The statement end character.
     */
    private static final String STATEMENT_END = ";";

    /**
     * @see org.andromda.maven.plugin.andromdapp.SchemaManagement#execute(java.sql.Connection, java.util.Map)
     */
    public void execute(
        Connection connection,
        java.util.Map options)
        throws Exception
    {
        final String hibernateDialect = "hibernate.dialect";
        System.setProperty(
            hibernateDialect,
            this.getRequiredProperty(
                options,
                hibernateDialect));
        final String[] arguments = (String[])this.getArguments(options).toArray(new String[0]);
        final Class schemaExportClass = this.getSchemaExportClass();
        final Method method = schemaExportClass.getMethod(
                "main",
                new Class[] {String[].class});
        method.invoke(
            schemaExportClass,
            new Object[] {arguments});

        final String createOutputPath = this.getExecutionOuputPath(options);

        final URL sqlUrl = ResourceUtils.toURL(createOutputPath);
        if (sqlUrl != null)
        {
            this.successes = 0;
            this.failures = 0;
            final Statement statement = connection.createStatement();
            final InputStream stream = sqlUrl.openStream();
            final BufferedReader resourceInput = new BufferedReader(new InputStreamReader(stream));
            StringBuffer sql = new StringBuffer();
            for (String line = resourceInput.readLine(); line != null; line = resourceInput.readLine())
            {
                if (line.startsWith("//"))
                {
                    continue;
                }
                if (line.startsWith("--"))
                {
                    continue;
                }
                sql.append(line);
                if (line.endsWith(STATEMENT_END))
                {
                    this.executeSql(
                        statement,
                        sql.toString().replaceAll(
                            STATEMENT_END,
                            ""));
                    sql = new StringBuffer();
                }
                sql.append("\n");
            }
            resourceInput.close();
            if (statement != null)
            {
                statement.close();
            }
            if (connection != null)
            {
                connection.close();
            }
        }
        final String count = String.valueOf((this.successes + this.failures)).toString();
        logger.info(" Executed " + count + " SQL statements");
        logger.info(" Failures: " + this.failures);
        logger.info(" Successes: " + this.successes);
    }

    /**
     * Returns the path of the execution output file.
     *
     * @param options the options from which to retrieve properties.
     * @return the output path.
     */
    protected abstract String getExecutionOuputPath(final Map options);

    /**
     * Stores the count of statements that were executed successfully.
     */
    private int successes;

    /**
     * Stores the count of statements that failed.
     */
    private int failures;

    /**
     * Executes the given <code>sql</code>, using the given <code>statement</code>.
     *
     * @param statement the statement to use to execute the SQL.
     * @param sql the SQL to execute.
     * @throws SQLException
     */
    private void executeSql(
        final Statement statement,
        final String sql)
    {
        logger.info(sql.trim());
        try
        {
            statement.execute(sql.toString());
            this.successes++;
        }
        catch (final SQLException exception)
        {
            this.failures++;
            logger.warn(exception.toString());
        }
    }

    /**
     * Retrieves the arguments common to all Hibernate schema management
     * tasks.
     *
     * @param options the options from which to retrieve any required properties.
     * @return the list of common arguments.
     */
    private List getArguments(final Map options)
    {
        final List mappingFiles =
            this.getMappingFilesList(
                this.getRequiredProperty(
                    options,
                    "mappingFileExtension"),
                this.getRequiredProperty(
                    options,
                    "mappingsLocation"));
        final String[] args = new String[] {"--text", "--quiet", "--delimiter=;", "--format"};
        final List arguments = new ArrayList(Arrays.asList(args));
        arguments.addAll(mappingFiles);
        this.addArguments(
            options,
            arguments);
        return arguments;
    }

    /**
     * Adds any arguments required by the specialized class.
     *
     * @param options any options from which to retrieve argument values.
     * @param arguments the list of arguments to add.
     */
    protected abstract void addArguments(
        final Map options,
        final List arguments);

    /**
     * Retrieves all mapping files having the given <code>extension</code>
     * existing in the <code>baseDirectory</code> or any of its sub directories.
     *
     * @param extension the mapping file extension
     * @param baseDirectory the directory from which to perform the search.
     * @return the list of mapping files
     */
    protected List getMappingFilesList(
        final String extension,
        final String baseDirectory)
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDirectory);
        scanner.setIncludes(new String[] {"**/*." + extension});
        scanner.setExcludes(null);
        scanner.scan();

        final List files = new ArrayList();
        for (final Iterator iterator = Arrays.asList(scanner.getIncludedFiles()).iterator(); iterator.hasNext();)
        {
            final String path = (String)iterator.next();
            files.add(new File(
                    baseDirectory,
                    path).toString());
        }
        return files;
    }
}