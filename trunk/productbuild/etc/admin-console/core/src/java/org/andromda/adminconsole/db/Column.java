package org.andromda.adminconsole.db;

import java.io.Serializable;

public interface Column extends Serializable
{
    public String getName();

    public boolean isNullable();

    public Class getType();

    public int getSqlType();

    public int getSize();

    public String getRemarks();

    public int getOrdinalPosition();

    public Table getTable();

    public boolean isBooleanType();

    public boolean isNumericType();

    public boolean isStringType();

    public boolean isForeignKeyColumn();

    public boolean isPrimaryKeyColumn();
}
