package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.Table;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ColumnImpl extends DatabaseObject implements Column
{
    private String name = null;
    private boolean nullable = false;
    private Class type = null;
    private int size = 0;
    private String remarks = null;
    private int ordinalPosition = 0;

    private Table table = null;

    public ColumnImpl(Table table, String name)
    {
        this.table = table;
        this.name = name;
        this.doRefresh();
    }

    protected String getCatalog()
    {
        return getTable().getDatabase().getCatalog();
    }

    protected String getSchema()
    {
        return getTable().getDatabase().getSchema();
    }

    protected DatabaseMetaData getMetaData()
    {
        return getTable().getDatabase().getMetaData();
    }

    public Table getTable()
    {
        return table;
    }

    public String getName()
    {
        return name;
    }

    public boolean isNullable()
    {
        return nullable;
    }

    public Class getType()
    {
        return type;
    }

    public int getSize()
    {
        return size;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public int getOrdinalPosition()
    {
        return ordinalPosition;
    }

    public boolean isComparable()
    {
        return Comparable.class.isAssignableFrom(getType());
    }

    public void refresh()
    {
        doRefresh();
    }

    private void doRefresh()
    {
        try
        {
            ResultSet resultSet = null;

            // TYPE
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table.getName(), name);
                if (resultSet.next())
                    type = toJavaType(resultSet.getInt("DATA_TYPE"));
                else
                    throw new RuntimeException("Unable to retrieve column type: " + name);
            }
            finally
            { close(resultSet); }

            // REMARKS
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table.getName(), name);
                if (resultSet.next())
                    remarks = resultSet.getString("REMARKS");
                else
                    throw new RuntimeException("Unable to retrieve column remarks: " + name);
            }
            finally
            { close(resultSet); }

            // ORDINAL POSITION
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table.getName(), name);
                if (resultSet.next())
                    ordinalPosition = resultSet.getInt("ORDINAL_POSITION");
                else
                    throw new RuntimeException("Unable to retrieve column ordinal position: " + name);
            }
            finally
            { close(resultSet); }

            // SIZE
            try
            {
                ResultSet columnSet = getMetaData().getColumns(getCatalog(), getSchema(), table.getName(), name);
                if (columnSet.next())
                    size = columnSet.getInt("COLUMN_SIZE");
                else
                    throw new RuntimeException("Unable to retrieve column size: " + name);
            }
            finally
            { close(resultSet); }

            // NULLABLE
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table.getName(), name);
                if (resultSet.next())
                    nullable = resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                else
                    throw new RuntimeException("Unable to retrieve column nullable: " + name);
            }
            finally
            { close(resultSet); }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Unable to refresh table: " + getName());
        }
    }

    protected Class toJavaType(int type)
    {
        switch (type)
        {
            case Types.VARCHAR:
                return java.lang.String.class;
            case Types.BIGINT:
                return java.lang.Long.class;
            case Types.BINARY:
                return java.lang.Boolean.class;
            case Types.BIT:
                return java.lang.Boolean.class;
            case Types.BOOLEAN:
                return java.lang.Boolean.class;
            case Types.CHAR:
                return java.lang.Character.class;
            case Types.DATE:
                return java.util.Date.class;
            case Types.DECIMAL:
                return java.lang.Integer.class;
            case Types.DOUBLE:
                return java.lang.Double.class;
            case Types.FLOAT:
                return java.lang.Float.class;
            case Types.INTEGER:
                return java.lang.Integer.class;
            case Types.JAVA_OBJECT:
                return java.lang.Object.class;
            case Types.LONGVARCHAR:
                return java.lang.String.class;
            case Types.NUMERIC:
                return java.lang.Integer.class;
            case Types.REAL:
                return java.lang.Double.class;
            case Types.SMALLINT:
                return java.lang.Short.class;
            case Types.TIME:
                return java.util.Date.class;
            case Types.TIMESTAMP:
                return java.util.Date.class;
            case Types.TINYINT:
                return java.lang.Short.class;
            case Types.OTHER:
                return java.lang.Object.class;
            default:
                throw new IllegalArgumentException("Unsupported SQL Type: " + type);
        }
    }

    public String toString()
    {
        return getName();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ColumnImpl)) return false;

        final ColumnImpl column = (ColumnImpl) o;

        if (name != null ? !name.equals(column.name) : column.name != null) return false;
        if (table != null ? !table.equals(column.table) : column.table != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (table != null ? table.hashCode() : 0);
        return result;
    }
}
