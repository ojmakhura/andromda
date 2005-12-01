package org.andromda.maven.plugin.andromdapp;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;

import java.util.Properties;


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

    public JdbcDriverWrapper(Driver driver)
    {
        this.driver = driver;
    }

    /**
     * @see java.sql.Driver#acceptsURL(java.lang.String)
     */
    public boolean acceptsURL(String url)
        throws SQLException
    {
        return this.driver.acceptsURL(url);
    }

    /**
     * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
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
     * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
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
}