package org.andromda.adminconsole.config;

import org.andromda.adminconsole.config.xml.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

public class AdminConsoleConfigurator implements Serializable
{
    public final static String FILE_NAME = "admin-console.cfg.xml";
    private final static String DEFAULT_CFG = "default.cfg.xml";

    private boolean unconfiguredTablesAvailable = false;
    private boolean arbitraryUrlAllowed = false;

    private final List knownDatabaseUrls = new ArrayList();
    private final List knownDatabaseTableNames = new ArrayList();

    private final Map tableCache = new HashMap();
    private final Map columnCache = new HashMap();

    /**
     * Constructs the configuration by first trying to load the file <code>admin-console.cfg.xml</code>
     * from the classpath, it that one could not be found the file <code>default.cfg.xml</code> will be loaded.
     *
     * @throws Exception when the configuration could not be loaded
     */
    public AdminConsoleConfigurator() throws Exception
    {
        AdminConsole configuration = loadConfiguration(FILE_NAME);
        if (configuration == null)
        {
            configuration = loadConfiguration(DEFAULT_CFG);
        }
        if (configuration == null)
        {
            throw new Exception("No configuration could be found, please put "+FILE_NAME+" on the classpath");
        }
        initialize(configuration);
    }

    public TableConfiguration getTableConfiguration(String tableName)
    {
        TableConfiguration configuration = (TableConfiguration) tableCache.get(tableName);
        if (configuration == null)
        {
            configuration = new TableConfiguration();
            configuration.setName(tableName);
            tableCache.put(tableName, configuration);
        }
        return configuration;
    }

    public ColumnConfiguration getColumnConfiguration(String tableName, String columnName)
    {
        ColumnConfiguration configuration = (ColumnConfiguration) columnCache.get(new ColumnCacheKey(tableName, columnName));
        if (configuration == null)
        {
            configuration = new ColumnConfiguration();
            configuration.setName(columnName);
            columnCache.put(columnName, configuration);
        }
        return configuration;
    }

    public List getKnownDatabaseUrls()
    {
        return Collections.unmodifiableList(knownDatabaseUrls);
    }

    public List getKnownDatabaseTableNames()
    {
        return Collections.unmodifiableList(knownDatabaseTableNames);
    }

    public boolean isUnconfiguredTablesAvailable()
    {
        return unconfiguredTablesAvailable;
    }

    public boolean isArbitraryUrlAllowed()
    {
        return arbitraryUrlAllowed;
    }

    private static AdminConsole loadConfiguration(String fileName) throws IOException
    {
        AdminConsole adminConsole = null;

        InputStream instream = null;
        Reader reader = null;

        try
        {
            instream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (instream != null)
            {
                reader = new InputStreamReader(instream);
                adminConsole = AdminConsole.unmarshal(reader);
            }
        }
        catch(Exception e)
        {
            // do nothing, let this method silently return
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader!=null) reader.close();
                if (instream!=null) instream.close();
            }
            catch (Exception e)
            {
                throw new IOException("Resources could not properly be closed");
            }
        }

        if (adminConsole != null && adminConsole.isValid() == false)
        {
            throw new RuntimeException("Loaded configuration file violates its XML Schema");
        }
        return adminConsole;
    }

    /**
     * Argument must not be null.
     */
    private void initialize(AdminConsole configuration)
    {
        this.arbitraryUrlAllowed = readArbitraryUrlAllowed(configuration);
        this.unconfiguredTablesAvailable = readUnconfiguredTablesAvailable(configuration);

        knownDatabaseUrls.clear();
        readKnownDatabaseUrls(knownDatabaseUrls, configuration);

        knownDatabaseTableNames.clear();
        readKnownTableNames(knownDatabaseTableNames, configuration);

        tableCache.clear();
        columnCache.clear();
        readTableConfigurations(tableCache, columnCache, configuration);
    }

    private static void readKnownDatabaseUrls(List urls, AdminConsole configuration)
    {
        ConsoleConfiguration consoleConfiguration = configuration.getConsoleConfiguration();
        if (consoleConfiguration != null)
        {
            DatabaseUrls databaseUrls = consoleConfiguration.getDatabaseUrls();
            if (databaseUrls != null)
            {
                Url[] urlArray = databaseUrls.getUrl();
                for (int i = 0; i < urlArray.length; i++)
                {
                    Url url = urlArray[i];
                    urls.add(new DbUrl(url.getName(), url.getContent()));
                }
            }
        }
    }

    public static class DbUrl
    {
        private String name = null;
        private String value = null;

        public DbUrl(String name, String value)
        {
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return (StringUtils.isBlank(name)) ? value : name;
        }

        public String getValue()
        {
            return value;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof DbUrl)) return false;

            final DbUrl dbUrl = (DbUrl) o;

            if (name != null ? !name.equals(dbUrl.name) : dbUrl.name != null) return false;
            if (value != null ? !value.equals(dbUrl.value) : dbUrl.value != null) return false;

            return true;
        }

        public int hashCode()
        {
            int result;
            result = (name != null ? name.hashCode() : 0);
            result = 29 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    private static void readKnownTableNames(List names, AdminConsole configuration)
    {
        TableConfiguration[] tableConfiguration = configuration.getTables().getTableConfiguration();
        for (int i = 0; i < tableConfiguration.length; i++)
        {
            TableConfiguration tableConfig = tableConfiguration[i];
            names.add(tableConfig.getName());
        }
    }

    private static boolean readUnconfiguredTablesAvailable(AdminConsole configuration)
    {
        // path cannot throw a NullPointerException, guaranteed by the XML Schema
        return configuration.getTables().getAllowUnconfigured();
    }

    /**
     * Returns true if the user is allowed to enter any arbitrary URL.
     */
    private static boolean readArbitraryUrlAllowed(AdminConsole configuration)
    {
        // path cannot throw a NullPointerException, guaranteed by the XML Schema
        return configuration.getConsoleConfiguration().getDatabaseUrls().getAllowUserSpecified();
    }

    private static void readTableConfigurations(Map tableMap, Map columnMap, AdminConsole configuration)
    {
        Tables tables = configuration.getTables();

        if (tables != null)
        {
            TableConfiguration[] tableConfigurations = tables.getTableConfiguration();

            if (tableConfigurations != null && tableConfigurations.length>0)
            {
                for (int i = 0; i < tableConfigurations.length; i++)
                {
                    TableConfiguration tableConfiguration = tableConfigurations[i];
                    tableMap.put(tableConfiguration.getName(), tableConfiguration);

                    readColumnConfigurations(
                            tableConfiguration.getName(), columnMap, tableConfiguration.getColumnConfiguration());
                }
            }
        }
    }

    private static void readColumnConfigurations(String tableName, Map columnMap, ColumnConfiguration[] columnConfigurations)
    {
        if (columnConfigurations != null && columnConfigurations.length>0)
        {
            for (int i = 0; i < columnConfigurations.length; i++)
            {
                ColumnConfiguration columnConfiguration = columnConfigurations[i];
                columnMap.put(new ColumnCacheKey(tableName, columnConfiguration.getName()), columnConfiguration);
            }
        }
    }

    private static class ColumnCacheKey
    {
        private String tableName = null;
        private String columnName = null;

        public ColumnCacheKey(String tableName, String columnName)
        {
            this.tableName = tableName;
            this.columnName = columnName;
        }

        public String getTableName()
        {
            return tableName;
        }

        public String getColumnName()
        {
            return columnName;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof ColumnCacheKey)) return false;

            final ColumnCacheKey columnCacheKey = (ColumnCacheKey) o;

            if (columnName != null ? !columnName.equals(columnCacheKey.columnName) : columnCacheKey.columnName != null) return false;
            if (tableName != null ? !tableName.equals(columnCacheKey.tableName) : columnCacheKey.tableName != null) return false;

            return true;
        }

        public int hashCode()
        {
            int result;
            result = (tableName != null ? tableName.hashCode() : 0);
            result = 29 * result + (columnName != null ? columnName.hashCode() : 0);
            return result;
        }
    }
}