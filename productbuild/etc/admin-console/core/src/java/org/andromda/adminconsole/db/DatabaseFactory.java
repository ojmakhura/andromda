package org.andromda.adminconsole.db;

import org.andromda.adminconsole.db.impl.DatabaseImpl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DatabaseFactory
{
    public static Database create(String driverFQName, String url, String schema, String user, String password) throws Exception
    {
        Class driverClass = null;

        try
        {
            driverClass = Class.forName(driverFQName);
        }
        catch (Exception e)
        {
            throw new Exception("Database driver could not be found: "+driverFQName, e);
        }

        Object driverObject = null;

        try
        {
            driverObject = driverClass.newInstance();
        }
        catch (Exception e)
        {
            throw new Exception("Database driver could not be instantiated: "+driverFQName, e);
        }

        Driver driver = null;

        try
        {
            driver = (Driver)driverObject;
        }
        catch (Exception e)
        {
            throw new Exception("Database driver is not a JDBC-compliant driver: "+driverFQName, e);
        }

        return create(driver, url, schema, user, password);

    }

    public static Database create(String url, String schema, String user, String password) throws Exception
    {
        Driver driver = locateIdealDriver(url);
        return create(driver, url, schema, user, password);
    }

    public static Database create(Driver driver, String url, String schema, String user, String password) throws Exception
    {
        DriverManager.registerDriver(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        return new DatabaseImpl(connection.getMetaData(), null, schema);
    }

    private static Driver locateIdealDriver(String url) throws Exception
    {
        // find all drivers
        Class[] allDriverClasses = java.sql.Driver.class.getClasses();

        // check which of them are already registered
        List registeredDrivers = Collections.list(DriverManager.getDrivers());
        List registeredDriverClasses = new ArrayList();
        for (int i = 0; i < registeredDrivers.size(); i++)
        {
            Driver driver = (Driver) registeredDrivers.get(i);
            registeredDriverClasses.add(driver.getClass());
        }
        boolean[] wasRegistered = new boolean[allDriverClasses.length];
        for (int i = 0; i < allDriverClasses.length; i++)
        {
            Class driverClass = allDriverClasses[i];
            wasRegistered[i] = registeredDrivers.contains(driverClass);
        }

        // instantiate and register all of the known drivers
        Driver[] allDriverInstances = new Driver[allDriverClasses.length];
        for (int i = 0; i < allDriverClasses.length; i++)
        {
            Class driverClass = allDriverClasses[i];
            allDriverInstances[i] = (Driver)driverClass.newInstance();
        }
        // register once all of them have been instantiated, in order to avoid
        // the need for rollback in case of exception
        for (int i = 0; i < allDriverInstances.length; i++)
        {
            Driver driverInstance = allDriverInstances[i];
            if (wasRegistered[i] == false)
            {
                DriverManager.registerDriver(driverInstance);
            }
        }

        // search the suitable driver for the argument URL
        Driver driver = DriverManager.getDriver(url);

        // unregister all other drivers that were not registered before
        for (int i = 0; i < allDriverInstances.length; i++)
        {
            Driver driverInstance = allDriverInstances[i];
            if ( (driverInstance.equals(driver) == false) && (wasRegistered[i] == false) )
            {
                DriverManager.deregisterDriver(driverInstance);
            }
        }

        if (driver == null)
        {
            throw new Exception("A suitable JDBC driver for this URL could not be located: "+url);
        }

        return driver;
    }
}
