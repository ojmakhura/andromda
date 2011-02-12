package org.andromda.demo.ejb3.client.vehicle;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.user.User;
import org.andromda.demo.ejb3.user.UserException;
import org.andromda.demo.ejb3.user.UserManagerDelegate;
import org.andromda.demo.ejb3.vehicle.Car;
import org.andromda.demo.ejb3.vehicle.Motocycle;
import org.andromda.demo.ejb3.vehicle.VehicleException;
import org.andromda.demo.ejb3.vehicle.VehicleManagerDelegate;

public class Client 
{

    private Properties prop;
    
    public void init() 
    {
        prop = new Properties();
        prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        prop.put("java.naming.provider.url", "localhost:1099");
    }
    
    
    
    
    
    public void insertMotorcycle()
    {
        System.out.println("Inserting motorcycle...");
        
        Motocycle mc = new Motocycle("1AAA222", "Mitsubishi", "EVOIX", "A");
        
        VehicleManagerDelegate vms = new VehicleManagerDelegate(prop);
        try
        {
            vms.addMotorcycle(mc);
        } 
        catch (VehicleException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            vms.close();
        }
        
        System.out.println("Insert complete.");
    }

    
    
    
    
    public void insertCar()
    {
        System.out.println("Inserting car...");
        
        Car car = new Car("2BBB1111", "Subaru", "Forrester", "AWD", 4);
        
        VehicleManagerDelegate vms = new VehicleManagerDelegate(prop);
        try
        {
            vms.addCar(car);
        } 
        catch (VehicleException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            vms.close();
        }
        
        System.out.println("Insert complete.");
    }
    
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        Client client = new Client();
        client.init();
        client.insertCar();
    }

}
