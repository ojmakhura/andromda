package org.andromda.adminconsole.db;

public interface PrimaryKeyColumn extends Column
{
    public String getPrimaryKeyName();

//    public ForeignKeyColumn[] getExportedKeyColumns();
}
