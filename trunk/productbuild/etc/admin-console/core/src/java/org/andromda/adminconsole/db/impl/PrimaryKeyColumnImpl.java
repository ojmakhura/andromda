package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.PrimaryKeyColumn;
import org.andromda.adminconsole.db.Table;

public class PrimaryKeyColumnImpl extends ColumnImpl implements PrimaryKeyColumn
{
    public PrimaryKeyColumnImpl(Table table, String name, int sqlType)
    {
        super(table, name, sqlType);
    }
}
