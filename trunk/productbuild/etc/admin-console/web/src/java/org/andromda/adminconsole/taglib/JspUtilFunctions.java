package org.andromda.adminconsole.taglib;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.config.WidgetRenderer;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;
import org.andromda.adminconsole.db.RowData;

import java.util.Collection;

public class JspUtilFunctions
{
    public final static boolean contains(Collection collection, Object object)
    {
        return (collection == null) ? false : collection.contains(object);
    }

    public final static String getInsertJsp(AdminConsoleConfigurator configurator, Column column, Object value)
    {
        ColumnConfiguration columnConfiguration = getConfiguration(configurator, column);
        return WidgetRenderer.getInsertJsp(column, columnConfiguration, column.getName(), value, null);
    }

    public final static String getUpdateJsp(AdminConsoleConfigurator configurator, Column column, RowData rowData, Integer index)
    {
        ColumnConfiguration columnConfiguration = getConfiguration(configurator, column);
        return WidgetRenderer.getUpdateJsp(
                column, columnConfiguration, index.intValue() + ":" + column.getName(), rowData,
                "onchange='document.getElementById(\"change-"+index+"\").checked=true;'");
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
