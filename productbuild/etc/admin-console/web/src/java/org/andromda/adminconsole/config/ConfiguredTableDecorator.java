package org.andromda.adminconsole.config;

import org.andromda.adminconsole.config.xml.TableConfiguration;
import org.andromda.adminconsole.db.Table;
import org.displaytag.decorator.TableDecorator;

public class ConfiguredTableDecorator extends TableDecorator
{
    private Table table = null;
    private TableConfiguration tableConfiguration = null;

    public ConfiguredTableDecorator(Table table, TableConfiguration tableConfiguration)
    {
        this.table = table;
        this.tableConfiguration = tableConfiguration;
    }
}
