package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.*;

import java.sql.*;
import java.util.*;
import java.sql.Date;

public class TableImpl extends DatabaseObject implements Table
{
    private String name = null;
    private TableType type = null;

    private final Map columns = new LinkedHashMap();
    private final Map primaryKeys = new LinkedHashMap();
    private final Map foreignKeys = new LinkedHashMap();

    private Database database = null;

    public TableImpl(Database database, String name)
    {
        this.name = name;
        this.database = database;
        this.refresh(true);
    }

    public TableImpl(Database database, String name, TableType type)
    {
        this.name = name;
        this.database = database;
        this.type = type;
        this.refresh(false);
    }

    public String getName()
    {
        return name;
    }

    public TableType getType()
    {
        return type;
    }

    public Database getDatabase()
    {
        return database;
    }

    protected DatabaseMetaData getMetaData()
    {
        return getDatabase().getMetaData();
    }

    protected String getSchema()
    {
        return getDatabase().getSchema();
    }

    protected String getCatalog()
    {
        return getDatabase().getCatalog();
    }

    public int getColumnCount()
    {
        return columns.size();
    }

    public String[] getColumnNames()
    {
        return (String[]) columns.keySet().toArray(new String[columns.size()]);
    }

    public Column[] getColumns()
    {
        return (Column[]) columns.values().toArray(new Column[columns.size()]);
    }

    public int getPrimaryKeyColumnCount()
    {
        return primaryKeys.size();
    }

    public String[] getPrimaryKeyColumnNames()
    {
        return (String[]) primaryKeys.keySet().toArray(new String[primaryKeys.size()]);
    }

    public PrimaryKeyColumn[] getPrimaryKeyColumns()
    {
        return (PrimaryKeyColumn[]) primaryKeys.values().toArray(new PrimaryKeyColumn[primaryKeys.size()]);
    }

    public int getForeignKeyColumnCount()
    {
        return foreignKeys.size();
    }

    public String[] getForeignKeyColumnNames()
    {
        return (String[]) foreignKeys.keySet().toArray(new String[foreignKeys.size()]);
    }

    public ForeignKeyColumn[] getForeignKeyColumns()
    {
        return (ForeignKeyColumn[]) foreignKeys.values().toArray(new ForeignKeyColumn[foreignKeys.size()]);
    }

    public void refresh()
    {
        refresh(true);
    }

    private void refresh(boolean refreshType)
    {
        getDatabase().getPool().register(this);

        try
        {
            ResultSet resultSet = null;

            // TYPE
            if (refreshType)
            {
                try
                {
                    resultSet = getMetaData().getTables(getCatalog(), getSchema(), name, null);
                    if (resultSet.next())
                        type = TableType.get(resultSet.getString("TABLE_TYPE"));
                    else
                        throw new RuntimeException("Unable to retrieve table type: " + name);
                }
                finally
                { close(resultSet); }
            }

            // COLUMNS
            columns.clear();
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), name, "%");
                while (resultSet.next())
                {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    Column column = getDatabase().getPool().findColumn(getName(), columnName);
                    if (column == null)
                    {
                        int dataType = resultSet.getInt("DATA_TYPE");
                        column = new ColumnImpl(this, columnName, dataType);
                    }
                    columns.put(columnName, column);
                }
            }
            finally
            { close(resultSet); }

            // PRIMARY KEYS
            primaryKeys.clear();
            try
            {
                resultSet = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), name);
                while (resultSet.next())
                {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    Column column = getColumn(columnName);
                    // create a new, more specific, instance
                    column = new PrimaryKeyColumnImpl(this, columnName, column.getSqlType());
                    primaryKeys.put(column.getName(), column);
                    columns.put(column.getName(), column);
                }
            }
            finally
            { close(resultSet); }

            // FOREIGN KEYS
            foreignKeys.clear();
            try
            {
                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), name);
                while (resultSet.next())
                {
                    String columnName = resultSet.getString("FKCOLUMN_NAME");
                    Column column = getColumn(columnName);
                    // create a new, more specific, instance
                    column = new ForeignKeyColumnImpl(this, columnName, column.getSqlType());
                    foreignKeys.put(column.getName(), column);
                    columns.put(column.getName(), column);
                }
            }
            finally
            { close(resultSet); }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Unable to refresh table: " + getName());
        }
    }

    private int insertRow(String[] columnNames, Object[] parameters) throws SQLException
    {
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("INSERT INTO ");
        queryBuffer.append(getName());

        queryBuffer.append(" (");

        for (int i = 0; i < columnNames.length; i++)
        {
            if (i>0) queryBuffer.append(',');
            queryBuffer.append(columnNames[i]);
        }

        queryBuffer.append(") values (");

        for (int i = 0; i < parameters.length; i++)
        {
            if (i>0) queryBuffer.append(',');
            queryBuffer.append('?');
        }

        queryBuffer.append(")");

        return executeUpdate(queryBuffer.toString(), columnNames, parameters);
    }

    public int insertRow(RowData rowData) throws SQLException
    {
        String[] columnNames = new String[rowData.size()];
        Object[] parameters = new Object[rowData.size()];

        int index = 0;
        for (Iterator iterator=rowData.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            columnNames[index] = String.valueOf(entry.getKey());
            parameters[index] = entry.getValue();
            index++;
        }
        return insertRow(columnNames, parameters);
    }

    public List findAllRows() throws SQLException
    {
        String query = "SELECT * FROM " + getName();
        return executeQuery(query);
    }

    public List findRows(Criterion criterion) throws SQLException
    {
        StringBuffer queryBuffer = new StringBuffer();

        queryBuffer.append("SELECT * FROM ");
        queryBuffer.append(getName());
        queryBuffer.append(" WHERE ");
        queryBuffer.append(criterion.toSqlString());

        return executeQuery(queryBuffer.toString());
    }

    public int updateRow(RowData rowData, Criterion criterion) throws SQLException
    {
        StringBuffer queryBuffer = new StringBuffer();

        queryBuffer.append("UPDATE ");
        queryBuffer.append(getName());
        queryBuffer.append(" SET ");

        String[] columnNames = new String[rowData.size()];
        Object[] values = new Object[rowData.size()];
        int index = 0;
        for (Iterator iterator = rowData.entrySet().iterator(); iterator.hasNext();index++)
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            columnNames[index] = (String)entry.getKey();
            values[index] = entry.getValue();
            if (index>0) queryBuffer.append(',');
            queryBuffer.append(entry.getKey());
            queryBuffer.append("=?");
        }

        if (criterion != null)
        {
            queryBuffer.append(" WHERE ");
            queryBuffer.append(criterion.toSqlString());
        }

        return executeUpdate(queryBuffer.toString(), columnNames, values);
    }

/*
    public int deleteRow(RowData rowData) throws SQLException
    {
        StringBuffer queryBuffer = new StringBuffer();

        queryBuffer.append("DELETE FROM ");
        queryBuffer.append(getName());
        queryBuffer.append(" WHERE ");
        queryBuffer.append(criterion.toSqlString());

        return executeUpdate(queryBuffer.toString());
    }
*/

    public int deleteRow(Criterion criterion) throws SQLException
    {
        StringBuffer queryBuffer = new StringBuffer();

        queryBuffer.append("DELETE FROM ");
        queryBuffer.append(getName());
        queryBuffer.append(" WHERE ");
        queryBuffer.append(criterion.toSqlString());

        return executeUpdate(queryBuffer.toString());
    }

    private int executeUpdate(String query) throws SQLException
    {
        return executeUpdate(query, null, null);
    }

    private Column getColumn(String columnName)
    {
        return (Column)columns.get(columnName);
    }

    private void setParameter(PreparedStatement statement, int index, Column column, Object parameter) throws SQLException
    {
        if (parameter instanceof String) statement.setString(index, (String)parameter);
        else if (parameter == null) statement.setNull(index, column.getSqlType());
        else if (parameter instanceof Integer) statement.setInt(index, ((Integer)parameter).intValue());
        else if (parameter instanceof Long) statement.setLong(index, ((Long)parameter).longValue());
        else if (parameter instanceof Boolean) statement.setBoolean(index, ((Boolean)parameter).booleanValue());
        else if (parameter instanceof Date) statement.setDate(index, (Date)parameter);
        else if (parameter instanceof Double) statement.setDouble(index, ((Double)parameter).doubleValue());
        else if (parameter instanceof Float) statement.setFloat(index, ((Float)parameter).floatValue());
        else if (parameter instanceof Short) statement.setShort(index, ((Short)parameter).shortValue());
        else if (parameter instanceof Character) statement.setString(index, ((Character)parameter).toString());
        else throw new IllegalArgumentException(
                "Invalid object type, cannot set into prepared statement: "+parameter.getClass());
    }

    private int executeUpdate(String query, String[] columnNames, Object[] parameters) throws SQLException
    {
        Connection connection = getMetaData().getConnection();
        PreparedStatement statement = null;
        try
        {
            statement = connection.prepareStatement(query);

            if (columnNames != null && parameters != null)
            {
                for (int i = 0; i < parameters.length; i++)
                {
                    setParameter(statement, i+1, getColumn(columnNames[i]), parameters[i]);
                }
            }

            int count = statement.executeUpdate();
            return count;
        }
        finally { close(statement); }
    }

    private List executeQuery(String query) throws SQLException
    {
        Connection connection = getMetaData().getConnection();

        List rows = new ArrayList();

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                String[] columnNames = getColumnNames();
                Object[] columnValues = new Object[columnNames.length];
                for (int i = 0; i < columnValues.length; i++)
                {
                    int columnType = resultSetMetaData.getColumnType(i+1);
                    columnValues[i] = getObject(resultSet, i+1, columnType);
                }
                rows.add(new RowData(this,columnValues));
            }
        }
        finally
        {
            close(statement);
            close(resultSet);
        }

        return rows;
    }

    private Object getObject(ResultSet resultSet, int index, int type) throws SQLException
    {
        switch (type)
        {
            case Types.VARCHAR:
                return resultSet.getString(index);
            case Types.BIGINT:
                return new Long(resultSet.getLong(index));
            case Types.BINARY:
                return Boolean.valueOf(resultSet.getBoolean(index));
            case Types.BIT:
                return Boolean.valueOf(resultSet.getBoolean(index));
            case Types.BOOLEAN:
                return Boolean.valueOf(resultSet.getBoolean(index));
            case Types.CHAR:
                return resultSet.getString(index);
            case Types.DATE:
                return resultSet.getDate(index);
            case Types.DECIMAL:
                return new Integer(resultSet.getInt(index));
            case Types.DOUBLE:
                return new Double(resultSet.getDouble(index));
            case Types.FLOAT:
                return new Float(resultSet.getFloat(index));
            case Types.INTEGER:
                return new Integer(resultSet.getInt(index));
            case Types.JAVA_OBJECT:
                return resultSet.getObject(index);
            case Types.LONGVARCHAR:
                return resultSet.getString(index);
            case Types.NUMERIC:
                return new Integer(resultSet.getInt(index));
            case Types.REAL:
                return new Double(resultSet.getDouble(index));
            case Types.SMALLINT:
                return new Short(resultSet.getShort(index));
            case Types.TIME:
                return resultSet.getDate(index);
            case Types.TIMESTAMP:
                return resultSet.getDate(index);
            case Types.TINYINT:
                return new Short(resultSet.getShort(index));
            case Types.OTHER:
                return resultSet.getObject(index);
            default:
                throw new IllegalArgumentException("Unsupported SQL Type: " + type);
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getName());
        buffer.append(':');

        Column[] columns = getColumns();
        for (int i = 0; i < columns.length; i++)
        {
            if (i > 0) buffer.append(',');
            buffer.append(columns[i]);
        }
        return buffer.toString();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof TableImpl)) return false;

        final TableImpl table = (TableImpl) o;

        if (database != null ? !database.equals(table.database) : table.database != null) return false;
        if (!name.equals(table.name)) return false;
        if (type != null ? !type.equals(table.type) : table.type != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = name.hashCode();
        result = 29 * result + (type != null ? type.hashCode() : 0);
        result = 29 * result + (database != null ? database.hashCode() : 0);
        return result;
    }
}
