package org.andromda.adminconsole.db;

public interface Column extends Refreshable
{
    public String getName();

    public boolean isNullable();

    public Class getType();

    public int getSize();

    public String getRemarks();

    public int getOrdinalPosition();

    public Table getTable();

    public boolean isBooleanType();

    public boolean isNumericType();

    public boolean isStringType();
}
