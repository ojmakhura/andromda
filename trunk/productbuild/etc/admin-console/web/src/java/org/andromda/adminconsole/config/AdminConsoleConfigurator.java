package org.andromda.adminconsole.config;

import org.andromda.adminconsole.config.xml.AdminConsole;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.*;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminConsoleConfigurator
{
    public final static String FILE_NAME = "admin-console.cfg.xml";
    private final static String DEFAULT_CFG = "default.cfg.xml";

    private AdminConsole configuration = null;
    private final WidgetRenderer widgetRenderer = new WidgetRenderer();

    private final Map tableCache = new HashMap();
    private final Map columnCache = new HashMap();
    private final Map jspCache = new HashMap(); // @todo: use

    public AdminConsoleConfigurator(Reader reader) throws Exception
    {
        try
        {
            configuration = AdminConsole.unmarshal(reader);
        }
        catch(Exception e)
        {
            throw new Exception("Unable to initialize configuration: "+e);
        }
    }

    public AdminConsoleConfigurator() throws Exception
    {
        try
        {
            InputStream instream = Thread.currentThread().getContextClassLoader().getResourceAsStream(AdminConsoleConfigurator.DEFAULT_CFG);
            Reader reader = new InputStreamReader(instream);
            configuration = AdminConsole.unmarshal(reader);
            reader.close();
            instream.close();
        }
        catch(Exception e)
        {
            throw new Exception("Unable to initialize configuration: "+e);
        }
    }

    public TableConfiguration getConfiguration(Table table)
    {
        TableConfiguration tableConfiguration = (TableConfiguration) tableCache.get(table);

        if (tableConfiguration == null)
        {
            final String tableName = table.getName();
            TableConfiguration[] tableConfigurations = configuration.getTableConfiguration();
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

    private String getJsp(Column column, String parameterName, Object value, boolean readOnly)
    {
        String displayJsp = null;

        if (column.isBooleanType())
        {
            displayJsp = widgetRenderer.renderCheckbox(parameterName, value, readOnly);
        }
        else
        {
            if (readOnly)
            {
                displayJsp = String.valueOf(value);
            }
            else
            {
                ColumnConfiguration configuration = getConfiguration(column);

                if (column instanceof ForeignKeyColumn)
                {
                    boolean resolveForeignKeys = this.configuration.getConsoleConfiguration().getResolveForeignKeys();

                    if (resolveForeignKeys)
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
                                displayJsp = widgetRenderer.renderSelect(parameterName, value, values, values, readOnly);
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
                                displayJsp = widgetRenderer.renderSelect(parameterName, value, values, labels, readOnly);
                            }
                        }
                        catch (SQLException e)
                        {
                            throw new RuntimeException("Unable to resolve foreign key values to table: "+primaryKeyTable);
                        }
                    }
                    else
                    {
                        displayJsp = widgetRenderer.renderTextfield(parameterName, value, readOnly);
                    }
                }
                else if (configuration.getValueCount() > 0)
                {
                    Object[] values = configuration.getValue();
                    displayJsp = widgetRenderer.renderSelect(parameterName, value, values, values, readOnly);
                }
                else
                {
                    displayJsp = widgetRenderer.renderTextfield(parameterName, value, readOnly);
                }
            }
        }
        return displayJsp;
    }

    public String getUpdateJsp(Column column, String parameterName, RowData rowData)
    {
        ColumnConfiguration configuration = getConfiguration(column);
        return getJsp(column, parameterName, rowData.get(column.getName()), !configuration.getUpdateable());
    }

    public String getInsertJsp(Column column, String parameterName)
    {
        ColumnConfiguration configuration = getConfiguration(column);
        return getJsp(column, parameterName, "", !configuration.getInsertable());
    }

    public String getInsertJsp(Column column, String parameterName, RowData rowData)
    {
        ColumnConfiguration configuration = getConfiguration(column);
        return getJsp(column, parameterName, (rowData==null)?"":rowData.get(column.getName()), !configuration.getInsertable());
    }
}