package org.andromda.adminconsole.config;

import org.andromda.adminconsole.config.xml.*;
import org.andromda.adminconsole.db.*;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class AdminConsoleConfigurator implements Serializable
{
    public final static String FILE_NAME = "admin-console.cfg.xml";
    private final static String DEFAULT_CFG = "default.cfg.xml";

    private AdminConsole configuration = null;
    private final WidgetRenderer widgetRenderer = new WidgetRenderer();

    private List knownUrls = null;
    private List knownTables = null;

    private final Map tableCache = new HashMap();
    private final Map columnCache = new HashMap();
    //private final Map jspCache = new HashMap(); // @todo: use

    /**
     * Constructs the configuration by first trying to load the file <code>admin-console.cfg.xml</code>
     * from the classpath, it that one could not be found the file <code>default.cfg.xml</code> will be loaded.
     *
     * @throws Exception when the configuration could not be loaded
     */
    public AdminConsoleConfigurator() throws Exception
    {
        configuration = loadConfiguration(FILE_NAME);
        if (configuration == null)
        {
            configuration = loadConfiguration(DEFAULT_CFG);
        }
        if (configuration == null)
        {
            throw new Exception("No configuration could be found, please put "+FILE_NAME+" on the classpath");
        }
    }

    /**
     * Returns the list of URLs as configured.
     * @return a List of String instances
     */
    public List getKnownDatabaseUrls()
    {
        if (knownUrls == null)
        {
            knownUrls = new ArrayList();
            ConsoleConfiguration consoleConfiguration = configuration.getConsoleConfiguration();
            if (consoleConfiguration != null)
            {
                DatabaseUrls databaseUrls = consoleConfiguration.getDatabaseUrls();
                if (databaseUrls != null)
                {
                    knownUrls.addAll(Arrays.asList(databaseUrls.getUrl()));
                }
            }
            if (knownUrls == null)
            {
                knownUrls = Collections.EMPTY_LIST;
            }
        }
        return knownUrls;
    }

    /**
     * Returns true if tables that are not configured should also be accessible.
     */
    public boolean isUnconfiguredTablesAvailable()
    {
        // path cannot throw a NullPointerException, guaranteed by the XML Schema
        return configuration.getTables().getAllowUnconfigured();
    }

    /**
     * Returns true if the user is allowed to enter any arbitrary URL.
     */
    public boolean isArbitraryUrlAllowed()
    {
        // path cannot throw a NullPointerException, guaranteed by the XML Schema
        return configuration.getConsoleConfiguration().getDatabaseUrls().getAllowUserSpecified();
    }

    /**
     * Returns the list of tables names as configured.
     *
     * @return a List of String instances
     */
    public List getKnownTableNames()
    {
        if (knownTables == null)
        {
            knownTables = new ArrayList();
            TableConfiguration[] tableConfiguration = configuration.getTables().getTableConfiguration();
            for (int i = 0; i < tableConfiguration.length; i++)
            {
                TableConfiguration tableConfig = tableConfiguration[i];
                knownTables.add(tableConfig.getName());
            }
        }
        return knownTables;
    }

    /**
     * Returns the configuration for the argument table.
     */
    public TableConfiguration getConfiguration(Table table)
    {
        TableConfiguration tableConfiguration = (TableConfiguration) tableCache.get(table);

        if (tableConfiguration == null && configuration.getTables() != null)
        {
            final String tableName = table.getName();
            TableConfiguration[] tableConfigurations = configuration.getTables().getTableConfiguration();
            for (int i = 0; i < tableConfigurations.length && tableConfiguration==null; i++)
                if (tableName.equals(tableConfigurations[i].getName()))
                    tableConfiguration = tableConfigurations[i];

            if (tableConfiguration == null)
            {
                tableConfiguration = new TableConfiguration();
                tableCache.put(table, tableConfiguration);
            }
        }
        return tableConfiguration;
    }

    /**
     * Returns the configuration for the argument column.
     */
    public ColumnConfiguration getConfiguration(Column column)
    {
        ColumnConfiguration columnConfiguration = (ColumnConfiguration) columnCache.get(column);

        if (columnConfiguration == null)
        {
            final String columnName = column.getName();
            final Table table = column.getTable();
            ColumnConfiguration[] columnConfigurations = getConfiguration(table).getColumnConfiguration();
            for (int i = 0; i < columnConfigurations.length && columnConfiguration==null; i++)
                if (columnName.equals(columnConfigurations[i].getName()))
                    columnConfiguration = columnConfigurations[i];

            // if this table has not been configured we can derive some settings from the metadata, overriding defaults
            if (columnConfiguration == null)
            {
                columnConfiguration = new ColumnConfiguration();
                columnConfiguration.setName(columnName);
                columnConfiguration.setSize(column.getSize());
                if (column.isNumericType()) columnConfiguration.setSize(5);
            }
            columnCache.put(column, columnConfiguration);
        }
        return columnConfiguration;
    }

    private String getJsp(Column column, String parameterName, Object value, boolean readOnly, String custom)
    {
        String displayJsp = null;

        if (column.isBooleanType())
        {
            displayJsp = widgetRenderer.renderCheckbox(parameterName, value, readOnly, custom);
        }
        else
        {
            if (readOnly)
            {
                if (column.isForeignKeyColumn())
                {
                    ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) column;
                    PrimaryKeyColumn primaryKeyColumn = foreignKeyColumn.getImportedKeyColumn();
                    Table pkTable = primaryKeyColumn.getTable();
                    TableConfiguration pkTableConfig = getConfiguration(pkTable);

                    String displayColumnName = pkTableConfig.getDisplayColumn();

                    if (displayColumnName == null)
                    {
                        displayJsp = String.valueOf(value);
                    }
                    else
                    {
                        try
                        {
                            List rows = pkTable.findRows(Expression.equal(primaryKeyColumn, value));
                            if (rows.isEmpty())
                            {
                                displayJsp = "?";
                            }
                            else if (rows.size() > 1)
                            {
                                // @todo: verify this for composite keys (I think it will not work)
                                throw new RuntimeException("Too many rows found for foreign key");
                            }
                            else
                            {
                                RowData rowData = (RowData) rows.get(0);
                                displayJsp = String.valueOf(rowData.get(displayColumnName));
                            }
                        }
                        catch (SQLException e)
                        {
                            throw new RuntimeException("Unable to load primary key table rows: "+pkTable.getName());
                        }
                    }
                }
                else
                {
                    displayJsp = String.valueOf(value);
                }
            }
            else
            {
                ColumnConfiguration configuration = getConfiguration(column);

                if (column.isForeignKeyColumn())
                {
                    if (configuration.getResolveFk())
                    {
                        ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) column;
                        PrimaryKeyColumn primaryKeyColumn = foreignKeyColumn.getImportedKeyColumn();
                        String primaryKeyColumnName = primaryKeyColumn.getName();
                        Table primaryKeyTable = primaryKeyColumn.getTable();

                        TableConfiguration tableConfiguration = getConfiguration(primaryKeyTable);
                        String displayColumnName = tableConfiguration.getDisplayColumn();

                        try
                        {
                            final List rows = primaryKeyTable.findAllRows();

                            if (displayColumnName == null)
                            {
                                final Object[] values = new Object[rows.size()];
                                for (int i = 0; i < rows.size(); i++)
                                {
                                    RowData rowData = (RowData) rows.get(i);
                                    values[i] = rowData.get(primaryKeyColumnName);
                                }
                                displayJsp = widgetRenderer.renderSelect(parameterName, value, values, values, readOnly, custom);
                            }
                            else
                            {
                                final Object[] values = new Object[rows.size()];
                                final Object[] labels = new Object[rows.size()];
                                for (int i = 0; i < rows.size(); i++)
                                {
                                    RowData rowData = (RowData) rows.get(i);
                                    values[i] = rowData.get(primaryKeyColumnName);
                                    labels[i] = rowData.get(displayColumnName);
                                }
                                displayJsp = widgetRenderer.renderSelect(parameterName, value, values, labels, readOnly, custom);
                            }
                        }
                        catch (SQLException e)
                        {
                            throw new RuntimeException("Unable to resolve foreign key values to table: "+primaryKeyTable);
                        }
                    }
                    else
                    {
                        displayJsp = widgetRenderer.renderTextfield(parameterName, value, readOnly, custom);
                    }
                }
                else if (configuration.getValueCount() > 0)
                {
                    Object[] values = configuration.getValue();
                    displayJsp = widgetRenderer.renderSelect(parameterName, value, values, values, readOnly, custom);
                }
                else
                {
                    displayJsp = widgetRenderer.renderTextfield(parameterName, value, readOnly, custom);
                }
            }
        }
        return displayJsp;
    }

    /**
     * Returns a JSP fragment suitable for an input field for updating the argument column.
     *
     * @param custom A custom set of attributes that will be included in the generated fragment.
     */
    public String getUpdateJsp(Column column, String parameterName, RowData rowData, String custom)
    {
        ColumnConfiguration configuration = getConfiguration(column);
        return getJsp(column, parameterName, rowData.get(column.getName()), !configuration.getUpdateable(), custom);
    }

    /**
     * Returns a JSP fragment suitable for an input field for inserting the argument column.
     *
     * @param custom A custom set of attributes that will be included in the generated fragment.
     */
    public String getInsertJsp(Column column, String parameterName, String custom)
    {
        return getJsp(column, parameterName, "", false, custom);
    }

    /**
     * Returns a JSP fragment suitable for an input field for updating the argument column. The value can be set.
     *
     * @param custom A custom set of attributes that will be included in the generated fragment.
     */
    public String getInsertJsp(Column column, String parameterName, Object value, String custom)
    {
        return getJsp(column, parameterName, (value==null)?"":value, false, custom);
    }

    private AdminConsole loadConfiguration(String fileName) throws IOException
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

}