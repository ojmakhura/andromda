package org.andromda.maven.plugin.andromdapp;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This just wraps a regular JDBC driver so that we can successfully load
 * a JDBC driver without having to use the System class loader.
 *
 * @author Chad Brandon
 */
public class JdbcDriverWrapper
    implements Driver
{
    private Driver driver;

    /**
     * @param driver
     */
    public JdbcDriverWrapper(Driver driver)
    {
        this.driver = driver;
    }

    /**
     * @see java.sql.Driver#acceptsURL(String)
     */
    public boolean acceptsURL(String url)
        throws SQLException
    {
        return this.driver.acceptsURL(url);
    }

    /**
     * @see java.sql.Driver#connect(String, java.util.Properties)
     */
    public Connection connect(
        String url,
        Properties properties)
        throws SQLException
    {
        return this.driver.connect(
            url,
            properties);
    }

    /**
     * @see java.sql.Driver#getMajorVersion()
     */
    public int getMajorVersion()
    {
        return this.driver.getMajorVersion();
    }

    /**
     * @see java.sql.Driver#getMinorVersion()
     */
    public int getMinorVersion()
    {
        return this.driver.getMinorVersion();
    }

    /**
     * @see java.sql.Driver#getPropertyInfo(String, java.util.Properties)
     */
    public DriverPropertyInfo[] getPropertyInfo(
        String url,
        Properties properties)
        throws SQLException
    {
        return this.driver.getPropertyInfo(
            url,
            properties);
    }

    /**
     * @see java.sql.Driver#jdbcCompliant()
     */
    public boolean jdbcCompliant()
    {
        return this.driver.jdbcCompliant();
    }

    /**
     * @see java.sql.Driver#getParentLogger()
     */
    // See http://stackoverflow.com/questions/8503338/new-method-added-in-javax-sql-commondatasource-in-1-7
    // and https://forums.oracle.com/message/10089368
    //@Override // This means it must be compiled with JDK7
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        Logger logger = null;
        try
        {
            // Throws a runtime exception is this method is not in the runtime JDBC driver
            // Use reflection to determine if the method exists at runtime
            //logger = this.driver.getParentLogger(); // When JDK6 is no longer supported
            Method method = this.driver.getClass().getMethod("getParentLogger");
            logger = (Logger)method.invoke(this.driver);
        }
        catch (Exception e)
        {
            // Not using JDK7, method does not exist, ignore or return UnsupportedOperation.
            // A better approach would be to use something other than java.util.Logger
        }
        return logger;
    }
}