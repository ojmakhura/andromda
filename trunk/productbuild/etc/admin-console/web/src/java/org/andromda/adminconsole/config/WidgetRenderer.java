package org.andromda.adminconsole.config;

import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.ForeignKeyColumn;
import org.andromda.adminconsole.db.RowData;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.SQLException;

public class WidgetRenderer implements Serializable
{
    public static String renderTextfield(String parameterName, Object value, boolean readOnly, String custom)
    {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("<input type=\"text\" name=\"");
        buffer.append(parameterName);
        buffer.append("\" value=\"");
        buffer.append(value);
        buffer.append('\"');
        if (readOnly)
        {
            buffer.append(" readonly=\"readonly\"");
        }
        if (custom != null)
        {
            buffer.append(' ');
            buffer.append(custom);
        }
        buffer.append("/>");

        return buffer.toString();
    }

    private static boolean isTrue(Object object)
    {
        boolean isTrue;

        if (object instanceof Boolean) isTrue = ((Boolean)object).booleanValue();
        else if (object instanceof String) isTrue = StringUtils.isNotBlank((String)object);
        else isTrue = (object!=null);

        return isTrue;
    }

    public static String renderCheckbox(String parameterName, Object value, boolean readOnly, String custom)
    {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("<input type=\"checkbox\" name=\"");
        buffer.append(parameterName);
        buffer.append("\" value=\"true\"");
        if (isTrue(value))
        {
            buffer.append(" checked=\"checked\"");
        }
        if (readOnly)
        {
            buffer.append(" disabled=\"disabled\"");
        }
        if (custom != null)
        {
            buffer.append(' ');
            buffer.append(custom);
        }
        buffer.append("/>");

        return buffer.toString();
    }

    public static String renderSelect(String parameterName, Object value, Object[] values, Object[] labels, boolean readOnly, String custom)
    {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("<select name=\"");
        buffer.append(parameterName);
        buffer.append('\"');
        if (readOnly)
        {
            buffer.append(" disabled=\"disabled\"");
        }
        if (custom != null)
        {
            buffer.append(' ');
            buffer.append(custom);
        }
        buffer.append('>');

        if (values != null)
        {
            buffer.append("<option value=\"\"></option>");
            for (int i = 0; i < values.length; i++)
            {
                buffer.append("<option");
                if (values[i].equals(value)) buffer.append(" selected=\"selected\"");
                buffer.append(" value=\"");
                buffer.append(values[i]);
                buffer.append("\">");
                buffer.append(labels[i]);
                buffer.append("</option>");
            }
        }
        buffer.append("</select>");

        return buffer.toString();
    }

    public static String getJsp(
            ForeignKeyColumn column,
            TableConfiguration foreignTableConfiguration, ColumnConfiguration columnConfiguration,
            String parameterName, Object value, boolean readOnly, String custom)
    {
        String displayJsp = null;

        if (column.isBooleanType())
        {
            // this is extremely unlikely because it doesn't make sense to use a boolean
            // as a foreign key value
            displayJsp = renderCheckbox(parameterName, value, readOnly, custom);
        }
        else
        {
            if (readOnly)
            {
                displayJsp = String.valueOf(value);
            }
            else // so this field is editable
            {
                // may be null
                final String displayName = foreignTableConfiguration.getDisplayColumn();

                if (columnConfiguration.getResolveFk())   // do we need to resolve foreign keys ?
                {
                    try
                    {
                        final Object[] foreignValues = column.getForeignValues(displayName).toArray();
                        final Object[] primaryKeys = new Object[foreignValues.length];
                        final Object[] columnValues = new Object[foreignValues.length];
                        for (int i = 0; i < foreignValues.length; i++)
                        {
                            ForeignKeyColumn.ForeignValue foreignValue = (ForeignKeyColumn.ForeignValue)foreignValues[i];
                            primaryKeys[i] = foreignValue.getPrimaryKey();
                            columnValues[i] = foreignValue.getColumnValue();
                        }

                        displayJsp = renderSelect(parameterName, value, primaryKeys, columnValues, readOnly, custom);
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException("Unable to resolve foreign keys", e);
                    }
                }
                else
                {
                    try
                    {
                        final ForeignKeyColumn.ForeignValue foreignValue = column.getForeignValue(value);
                        displayJsp = renderTextfield(parameterName, foreignValue.getColumnValue(), readOnly, custom);
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException("Unable to resolve foreign key", e);
                    }
                }
            }
        }
        return displayJsp;
    }

    public static String getJsp(
            Column column, ColumnConfiguration columnConfiguration,
            String parameterName, Object value, boolean readOnly, String custom)
    {
        String displayJsp = null;

        if (column.isBooleanType())
        {
            displayJsp = renderCheckbox(parameterName, value, readOnly, custom);
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
                    displayJsp = renderSelect(parameterName, value, values, values, readOnly, custom);
                }
                else
                {
                    displayJsp = renderTextfield(parameterName, value, readOnly, custom);
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
    public static String getUpdateJsp(Column column, ColumnConfiguration columnConfiguration, String parameterName, RowData rowData, String custom)
    {
        return getJsp(column, columnConfiguration, parameterName, rowData.get(column.getName()), !columnConfiguration.getUpdateable(), custom);
    }

    /**
     * Returns a JSP fragment suitable for an input field for inserting the argument column.
     *
     * @param custom A custom set of attributes that will be included in the generated fragment.
     */
    public static String getInsertJsp(Column column, ColumnConfiguration columnConfiguration, String parameterName, String custom)
    {
        return getJsp(column, columnConfiguration, parameterName, "", false, custom);
    }

    /**
     * Returns a JSP fragment suitable for an input field for updating the argument column. The value can be set.
     *
     * @param custom A custom set of attributes that will be included in the generated fragment.
     */
    public static String getInsertJsp(Column column, ColumnConfiguration columnConfiguration, String parameterName, Object value, String custom)
    {
        return getJsp(column, columnConfiguration, parameterName, (value==null)?"":value, false, custom);
    }


}
