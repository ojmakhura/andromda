package org.andromda.adminconsole.taglib;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;
import org.andromda.adminconsole.db.RowData;

public class JspUtilFunctions
{
    public final static String getInsertJsp(AdminConsoleConfigurator configurator, Column column, Object value)
    {
        return configurator.getInsertJsp(column, column.getName(), value, null);
    }

    public final static String getUpdateJsp(AdminConsoleConfigurator configurator, Column column, RowData rowData, Integer index)
    {
        return configurator.getUpdateJsp(
                column, index.intValue() + ":" + column.getName(), rowData,
                "onchange='document.getElementById(\"change-"+index+"\").checked=true;'");
    }

    public final static ColumnConfiguration getConfiguration(AdminConsoleConfigurator configurator, Column column)
    {
        return configurator.getConfiguration(column);
    }

    public final static TableConfiguration getConfiguration(AdminConsoleConfigurator configurator, Table table)
    {
        return configurator.getConfiguration(table);
    }
}
