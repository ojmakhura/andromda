package org.andromda.maven.plugin.andromdapp.hibernate;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.maven.plugin.andromdapp.SchemaManagement;
import org.andromda.maven.plugin.andromdapp.SchemaManagementException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
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
    protected static final String HIBERNATE_2_PACKAGE = "net.sf.hibernate.tool.hbm2ddl";

    /**
     * The Hibernate 3 schema export class name.
     */
    protected static final String HIBERNATE_3_PACKAGE = "org.hibernate.tool.hbm2ddl";

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
     * Retrieves the class that performs the execution work.
     *
     * @return the Hibernate class that performs the work.
     */
    protected Class getExecutionClass()
    {
        Class hibernateClass = null;
        String hibernate2ClassName = null;
        String hibernate3ClassName = null;
        try
        {
            hibernate3ClassName = HIBERNATE_3_PACKAGE + "." + this.getExecutionClassName();
            hibernateClass = ClassUtils.loadClass(hibernate3ClassName);
        }
        catch (Exception exception)
        {
            // - ignore, means we can't find the Hibernate 3 class
        }
        try
        {
            hibernate2ClassName = HIBERNATE_2_PACKAGE + "." + this.getExecutionClassName();
            hibernateClass = ClassUtils.loadClass(hibernate2ClassName);
        }
        catch (Exception exception)
        {
            // - ignore, means we can't find the Hibernate 2 class
        }
        if (hibernateClass == null)
        {
            throw new RuntimeException(
                "There appear to be no Hibernate 2 or 3 jars are your classpath, because neither '" +
                hibernate2ClassName + "', nor '" + hibernate3ClassName + "' could be found");
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
        if (StringUtils.isBlank(value))
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
     * Returns the name of the class that performs the execution.
     *
     * @return the execution class.
     */
    protected abstract String getExecutionClassName();

    /**
     * @see org.andromda.maven.plugin.andromdapp.SchemaManagement#execute(java.sql.Connection, java.util.Map)
     */
    public String execute(
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
        final Class executionClass = this.getExecutionClass();
        final Method method = executionClass.getMethod(
                "main",
                new Class[] {String[].class});
        method.invoke(
            executionClass,
            new Object[] {arguments});

        return this.getExecutionOuputPath(options);
    }

    /**
     * Returns the path of the execution output file.
     *
     * @param options the options from which to retrieve properties.
     * @return the output path.
     */
    protected abstract String getExecutionOuputPath(final Map options);

    /**
     * Retrieves the arguments common to all Hibernate schema management
     * tasks.
     *
     * @param options the options from which to retrieve any required properties.
     * @return the list of common arguments.
     */
    private List getArguments(final Map options)
        throws Exception
    {
        final List mappingFiles =
            this.getMappingFilesList(
                this.getRequiredProperty(
                    options,
                    "mappingFileExtension"),
                this.getRequiredProperty(
                    options,
                    "mappingsLocation"));
        final String[] args = new String[] {"--delimiter=;", "--format"};
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
        final List arguments) throws Exception;

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