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

        return doSelect(queryBuffer.toString(), -1);
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
        int counter = 0;
        boolean unbounded = (maxReturnCount > 0);

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next() && (unbounded || counter<maxReturnCount))
            {
                Object primaryKey = resultSet.getObject(0);
                Object columnValue = resultSet.getObject(1);
                values.add(new ForeignValue(primaryKey, columnValue));
                counter++;
            }
        }
        finally
        {
            close(statement);
            close(resultSet);
            close(connection);
        }

        return values;
    }

/* maybe we'll need this in the future, at this time we don't

    private String foreignKeyName = null;
    private String primaryKeyName = null;
    private ForeignKeyDeleteRule deleteRule = null;
    private ForeignKeyUpdateRule updateRule = null;

    public ForeignKeyColumnImpl(Table table, String name, int sqlType, String foreignKeyName, String primaryKeyName, ForeignKeyDeleteRule deleteRule, ForeignKeyUpdateRule updateRule, String importedTableName)
    {
        super(table, name, sqlType);
        this.foreignKeyName = foreignKeyName;
        this.primaryKeyName = primaryKeyName;
        this.deleteRule = deleteRule;
        this.updateRule = updateRule;
        this.importedTableName = importedTableName;
    }

    public String getForeignKeyName()
    {
        return foreignKeyName;
    }

    public String getPrimaryKeyName()
    {
        return primaryKeyName;
    }

    public ForeignKeyDeleteRule getDeleteRule()
    {
        return deleteRule;
    }

    public ForeignKeyUpdateRule getUpdateRule()
    {
        return updateRule;
    }

    public String getImportedTableName()
    {
        return importedTableName;
    }
*/

}
