package org.andromda.adminconsole.db.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Serializable;

public abstract class DatabaseObject implements Serializable
{
    protected static void close(PreparedStatement statement)
    {
        if (statement != null)
        {
            try
            {
                statement.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException("Unable to close prepared statement", e);
            }
        }
    }

    protected static void close(ResultSet resultSet)
    {
        if (resultSet != null)
        {
            try
            {
                resultSet.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException("Unable to close resultset", e);
            }
        }
    }

}
