package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.ForeignKeyColumn;
import org.andromda.adminconsole.db.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ForeignKeyColumnImpl extends ColumnImpl implements ForeignKeyColumn
{
    private String importedTableName = null;
    private String importedColumnName = null;

    public ForeignKeyColumnImpl(Table table, String name, int sqlType, String importedTableName, String importedColumnName)
    {
        super(table, name, sqlType);
        this.importedTableName = importedTableName;
        this.importedColumnName = importedColumnName;
    }

    public String getImportedTableName()
    {
        return importedTableName;
    }

    public String getImportedColumnName()
    {
        return importedColumnName;
    }

    public ForeignValue getForeignValue(Object value) throws SQLException
    {
        return getForeignValue(null, value);
    }

    public ForeignValue getForeignValue(String columnName, Object value) throws SQLException
    {
        // no need to query the DB when given the ID you just want to have the ID back
        if (columnName == null)
        {
            return new ForeignValue(value,value);
        }

        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT ");
        queryBuffer.append(getImportedColumnName());
        queryBuffer.append(',');
        queryBuffer.append(columnName);
        queryBuffer.append(" FROM ");
        queryBuffer.append(getImportedTableName());
        queryBuffer.append(" WHERE ");
        queryBuffer.append(getImportedColumnName());
        queryBuffer.append('=');
        queryBuffer.append(String.valueOf(value));

        List foreignValues = doSelect(queryBuffer.toString(), 1);
        return (foreignValues.isEmpty()) ? null : (ForeignValue)foreignValues.get(0);
    }

    public List getForeignValues() throws SQLException
    {
        return getForeignValues(null);
    }

    public List getForeignValues(String columnName) throws SQLException
    {
        if (columnName == null)
        {
            columnName = getImportedColumnName();
        }

        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT ");
        queryBuffer.append(getImportedColumnName());
        queryBuffer.append(',');
        queryBuffer.append(columnName);
        queryBuffer.append(" FROM ");
        queryBuffer.append(getImportedTableName());

        return doSelect(queryBuffer.toString(), 0);
    }

    /**
     * @param the query is expected to be in SQL and should yield the return of two columns, of which the first
     *  one represents the primary key and the other is any column (possibly the same as the first)
     * @param maxReturnCount the maximum number of values to return, a non-positive integer denotes unbounded
     */
    private List doSelect(String query, int maxReturnCount) throws SQLException
    {
        Connection connection = getMetaData().getConnection();

        List values = new ArrayList();

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.prepareStatement(query);
            statement.setMaxRows(maxReturnCount);
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Object primaryKey = resultSet.getObject(1);
                Object columnValue = resultSet.getObject(2);
                values.add(new ForeignValue(primaryKey, columnValue));
            }
        }
        finally
        {
            close(statement);
            close(resultSet);
        }

        return values;
    }
}
