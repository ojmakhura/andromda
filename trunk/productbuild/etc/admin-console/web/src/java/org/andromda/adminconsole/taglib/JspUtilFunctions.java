package org.andromda.adminconsole.taglib;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;
import org.andromda.adminconsole.db.RowData;

public class JspUtilFunctions
{
    public final static String getInsertJsp(AdminConsoleConfigurator configurator, Column column, String parameterName, RowData rowData)
    {
        return configurator.getInsertJsp(column, parameterName, rowData);
    }

    public final static String getUpdateJsp(AdminConsoleConfigurator configurator, Column column, String parameterName, RowData rowData)
    {
        return configurator.getUpdateJsp(column, parameterName, rowData);
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
