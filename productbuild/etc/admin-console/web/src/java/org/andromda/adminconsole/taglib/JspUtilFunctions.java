package org.andromda.adminconsole.taglib;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.config.WidgetRenderer;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;
import org.andromda.adminconsole.db.RowData;
import org.andromda.adminconsole.db.ForeignKeyColumn;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.sql.SQLException;

public class JspUtilFunctions
{
    public final static boolean contains(Collection collection, Object object)
    {
        return (collection == null) ? false : collection.contains(object);
    }

    public final static Object renderColumnValue(Column column, RowData row)
    {
        Object value = null;

        if (column instanceof ForeignKeyColumn)
        {
            try
            {
                ForeignKeyColumn.ForeignValue foreignValue = ((ForeignKeyColumn)column).getForeignValue(row.get(column.getName()));
                value = foreignValue.getColumnValue();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                value = row.get(column.getName());
            }
        }
        else
        {
            value = row.get(column.getName());
        }

        return value;
    }

    public final static String renderUpdateWidget(
            AdminConsoleConfigurator configurator, Column column, RowData row, Integer index)
    {
        ColumnConfiguration columnConfiguration = getConfiguration(configurator, column);
        return renderJsp(configurator, column, index + ":" + column.getName(), row.get(column.getName()),
                !columnConfiguration.getUpdateable(),
                "onchange='document.getElementById(change-\""+index+").checked=true;'");
    }

    public final static String renderInsertWidget(
            AdminConsoleConfigurator configurator, Column column, Object value)
    {
        return renderJsp(configurator, column, column.getName(), value, false, null);
    }

    private final static String renderJsp(
            AdminConsoleConfigurator configurator, Column column,
            String parameterName, Object value, boolean readOnly, String customCode)
    {
        String displayJsp = null;

        if (column.isBooleanType())
        {
            // this is extremely unlikely because it doesn't make sense to use a boolean
            // as a foreign key value
            displayJsp = WidgetRenderer.renderCheckbox(parameterName, value, readOnly, customCode);
        }
        else
        {
            final ColumnConfiguration columnConfiguration = getConfiguration(configurator, column);

            if (column instanceof ForeignKeyColumn)
            {
                ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn)column;
                if (columnConfiguration.getResolveFk())   // do we need to resolve foreign keys ?
                {
                    try
                    {
                        final TableConfiguration tableConfiguration = configurator.getTableConfiguration(foreignKeyColumn.getImportedTableName());
                        final String displayName = tableConfiguration.getDisplayColumn();

                        // displayName may be null, in this case the primary key name will be assumed
                        final Object[] foreignValues = foreignKeyColumn.getForeignValues(displayName).toArray();
                        final Object[] primaryKeys = new Object[foreignValues.length];
                        final Object[] columnValues = new Object[foreignValues.length];
                        for (int i = 0; i < foreignValues.length; i++)
                        {
                            ForeignKeyColumn.ForeignValue foreignValue = (ForeignKeyColumn.ForeignValue)foreignValues[i];
                            primaryKeys[i] = foreignValue.getPrimaryKey();
                            columnValues[i] = foreignValue.getColumnValue();
                        }

                        displayJsp = WidgetRenderer.renderSelect(parameterName, value, primaryKeys, columnValues, readOnly, customCode);
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException("Unable to resolve foreign keys: "+e, e);
                    }
                }
                else
                {
                    try
                    {
                        if (value != null && StringUtils.isNotBlank(String.valueOf(value)))
                        {
                            final ForeignKeyColumn.ForeignValue foreignValue = foreignKeyColumn.getForeignValue(value);
                            displayJsp = WidgetRenderer.renderTextfield(parameterName, foreignValue.getColumnValue(), readOnly, customCode);
                        }
                        else
                        {
                            displayJsp = WidgetRenderer.renderTextfield(parameterName, "", readOnly, customCode);
                        }
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException("Unable to resolve foreign key: "+e, e);
                    }
                }
            }
            else
            {
                if (readOnly)
                {
                    displayJsp = String.valueOf(value);
                }
                else
                {
                    if (columnConfiguration.getValueCount() > 0)
                    {
                        Object[] values = columnConfiguration.getValue();
                        displayJsp = WidgetRenderer.renderSelect(parameterName, value, values, values,
                                readOnly, customCode);
                    }
                    else
                    {
                        displayJsp = WidgetRenderer.renderTextfield(parameterName, value,
                                readOnly, customCode);
                    }
                }
            }
        }
        return displayJsp;
    }

    public final static ColumnConfiguration getConfiguration(AdminConsoleConfigurator configurator, Column column)
    {
        return configurator.getColumnConfiguration(column.getTable().getName(), column.getName());
    }

    public final static TableConfiguration getConfiguration(AdminConsoleConfigurator configurator, Table table)
    {
        return configurator.getTableConfiguration(table.getName());
    }
}
