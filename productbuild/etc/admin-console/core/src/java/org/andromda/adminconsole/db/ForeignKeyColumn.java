package org.andromda.adminconsole.db;

public interface ForeignKeyColumn extends Column
{
    public String getForeignKeyName();

    public String getPrimaryKeyName();

    public ForeignKeyUpdateRule getUpdateRule();

    public ForeignKeyDeleteRule getDeleteRule();

    public PrimaryKeyColumn getImportedKeyColumn();
}
