package org.andromda.adminconsole.config;

import org.andromda.adminconsole.config.xml.AdminConsole;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.config.xml.Value;
import org.andromda.adminconsole.config.xml.types.ColumnConfigurationWidgetType;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class AdminConsoleConfigurator
{
    public final static String FILE_NAME = "admin-console.cfg.xml";

    private AdminConsole configuration = null;
    private final Map tableCache = new HashMap();
    private final Map columnCache = new HashMap();
    private final Map jspCache = new HashMap();

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
                if (column.isBooleanType()) columnConfiguration.setWidget(ColumnConfigurationWidgetType.CHECKBOX);
                else if (column.isNumericType()) columnConfiguration.setSize(5);
            }
            columnCache.put(column, columnConfiguration);
        }
        return columnConfiguration;
    }

    public String getJsp(Column column, Object value)
    {
        String jsp = null;//= (String) jspCache.get(column);

        if (jsp == null)
        {
            ColumnConfiguration config = getConfiguration(column);

            StringBuffer buffer = new StringBuffer();

            ColumnConfigurationWidgetType widget = config.getWidget();
            if (ColumnConfigurationWidgetType.PLAIN.equals(widget))
            {
                buffer.append(value);
            }
            else if (ColumnConfigurationWidgetType.CHECKBOX.equals(widget))
            {
                buffer.append("<input type=\"checkbox\" name=\"");
                buffer.append(column.getName());
                buffer.append('\"');
                if ( (value instanceof Boolean && ((Boolean)value).booleanValue()) || value!=null )
                {
                    buffer.append(" checked");
                }
                if (config.getEditable() == false) buffer.append(" disabled");
                buffer.append("\"/>");
            }
            else if (ColumnConfigurationWidgetType.TEXTFIELD.equals(widget))
            {
                buffer.append("<input type=\"text\" name=\"");
                buffer.append(column.getName());
                buffer.append("\" value=\"");
                buffer.append(value);
                buffer.append("\" size=\"");
                buffer.append(config.getSize());
                buffer.append('\"');
                if (config.getEditable() == false) buffer.append(" readonly");
                buffer.append("\"/>");
            }
            else if (ColumnConfigurationWidgetType.SELECT.equals(widget))
            {
                buffer.append("<select name=\"");
                buffer.append(column.getName());
                buffer.append('\"');
                if (config.getEditable() == false) buffer.append(" disabled");
                buffer.append("\">");

                boolean valueListed = false;
                Value[] values = config.getValue();
                for (int i = 0; i < values.length; i++)
                {
                    Value optionValue = values[i];
                    if (!valueListed && optionValue.getName().equals(value))
                    {
                        valueListed = true;
                        buffer.append("<option selected value=\"");
                    }
                    else
                    {
                        buffer.append("<option value=\"");
                    }
                    buffer.append(optionValue.getName());
                    buffer.append("\">");
                    buffer.append(optionValue.getName());
                    buffer.append("</option>");
                }
                if (valueListed == false)
                {
                    buffer.append("<option value=\"");
                    buffer.append(value);
                    buffer.append("\">");
                    buffer.append(value);
                    buffer.append("</option>");
                }
                buffer.append("</html:select>");
            }
            jsp = buffer.toString();
        }
        return jsp;
    }
}