package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class TableImpl extends DatabaseObject implements Table
{
    private String name = null;
    private Database database = null;
    private TableType type = null;

    private final Map columns = new LinkedHashMap();
    private final Map primaryKeyColumns = new LinkedHashMap();
    private final Map foreignKeyColumns = new LinkedHashMap();
    private final Set importingTableNames = new HashSet();

    public TableImpl(Database database, String name)
    {
        this.name = name;
        this.database = database;
        this.loadMetaData();
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
        return primaryKeyColumns.size();
    }

    public String[] getPrimaryKeyColumnNames()
    {
        return (String[]) primaryKeyColumns.keySet().toArray(new String[primaryKeyColumns.size()]);
    }

    public PrimaryKeyColumn[] getPrimaryKeyColumns()
    {
        return (PrimaryKeyColumn[]) primaryKeyColumns.values().toArray(new PrimaryKeyColumn[primaryKeyColumns.size()]);
    }

    public int getForeignKeyColumnCount()
    {
        return foreignKeyColumns.size();
    }

    public String[] getForeignKeyColumnNames()
    {
        return (String[]) foreignKeyColumns.keySet().toArray(new String[foreignKeyColumns.size()]);
    }

    public ForeignKeyColumn[] getForeignKeyColumns()
    {
        return (ForeignKeyColumn[]) foreignKeyColumns.values().toArray(new ForeignKeyColumn[foreignKeyColumns.size()]);
    }

    public String[] getImportingTableNames()
    {
        return (String[]) importingTableNames.toArray(new String[importingTableNames.size()]);
    }

    public int getImportingTablesCount()
    {
        return importingTableNames.size();
    }

    private void loadMetaData()
    {
        try
        {
            ResultSet resultSet = null;

            // TYPE
            try
            {
                resultSet = getMetaData().getTables(getCatalog(), getSchema(), name, null);
                if (resultSet.next())
                    type = TableType.get(resultSet.getString("TABLE_TYPE"));
                else
                    throw new RuntimeException("Unable to retrieve table type: " + name);
            }
            finally
            {
                close(resultSet);
            }

            // COLUMNS
            columns.clear();
            try
            {
                resultSet = getMetaData().getColumns(getCatalog(), getSchema(), name, "%");
                while (resultSet.next())
                {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    Column column = getColumn(columnName);
                    if (column == null)
                    {
                        int dataType = resultSet.getInt("DATA_TYPE");
                        column = new ColumnImpl(this, columnName, dataType);
                    }
                    columns.put(columnName, column);
                }
            }
            finally
            {
                close(resultSet);
            }

            // PRIMARY KEYS
            primaryKeyColumns.clear();
            try
            {
                resultSet = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), name);
                while (resultSet.next())
                {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    Column column = getColumn(columnName);
                    // create a new, more specific, instance
                    column = new PrimaryKeyColumnImpl(this, columnName, column.getSqlType());
                    primaryKeyColumns.put(column.getName(), column);
                    columns.put(column.getName(), column);
                }
            }
            finally
            {
                close(resultSet);
            }

            // FOREIGN KEYS
            foreignKeyColumns.clear();
            try
            {
                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), name);
                while (resultSet.next())
                {
                    String columnName = resultSet.getString("FKCOLUMN_NAME");
                    Column column = getColumn(columnName);

/*
                    String foreignKeyName = resultSet.getString("FK_NAME");
                    String primaryKeyName = resultSet.getString("PK_NAME");
                    ForeignKeyDeleteRule deleteRule = ForeignKeyDeleteRule.get(resultSet.getInt("DELETE_RULE"));
                    ForeignKeyUpdateRule updateRule = ForeignKeyUpdateRule.get(resultSet.getInt("UPDATE_RULE"));
*/

                    String importedTableName = resultSet.getString("PKTABLE_NAME");
                    String importedColumnName = resultSet.getString("PKCOLUMN_NAME");

                    // create a new, more specific, instance
                    ForeignKeyColumn foreignKeyColum =
                            new ForeignKeyColumnImpl(
                                    this, columnName, column.getSqlType(),
                                    importedTableName, importedColumnName );
/*
                    ForeignKeyColumn foreignKeyColum =
                            new ForeignKeyColumnImpl(
                                    this, columnName, getColumn(columnName).getSqlType(),
                                    foreignKeyName, primaryKeyName, deleteRule, updateRule, importedTableName);
*/
                    foreignKeyColumns.put(foreignKeyColum.getName(), foreignKeyColum);
                    columns.put(foreignKeyColum.getName(), foreignKeyColum);
                }
            }
            finally
            {
                close(resultSet);
            }

            // TABLES IMPORTING THIS ONE
            importingTableNames.clear();
            try
            {
                resultSet = getMetaData().getExportedKeys(getCatalog(), getSchema(), name);
                while (resultSet.next())
                {
                    String tableName = resultSet.getString("FKTABLE_NAME");
                    importingTableNames.add(tableName);
                }
            }
            finally
            {
                close(resultSet);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Unable to refresh table: " + getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        return findAllRows(0);
    }

    public List findAllRows(int maximum) throws SQLException
    {
        String query = "SELECT * FROM " + getName();
        return executeQuery(query, maximum);
    }

    public List findRows(Criterion criterion) throws SQLException
    {
        return findRows(criterion, 0);
    }

    public List findRows(Criterion criterion, int maximum) throws SQLException
    {
        StringBuffer queryBuffer = new StringBuffer();

        queryBuffer.append("SELECT * FROM ");
        queryBuffer.append(getName());
        queryBuffer.append(" WHERE ");
        queryBuffer.append(criterion.toSqlString());

        return executeQuery(queryBuffer.toString(), maximum);
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

    public Column getColumn(String columnName)
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
        finally
        {
            close(statement);
        }
    }

    private List executeQuery(String query, int maximum) throws SQLException
    {
        Connection connection = getMetaData().getConnection();

        List rows = new ArrayList();

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.prepareStatement(query);
            statement.setMaxRows(Math.max(0,maximum));
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
