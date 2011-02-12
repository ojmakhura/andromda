package org.andromda.demo.ejb3.client.rider;

import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.bicycle.Bicycle;
import org.andromda.demo.ejb3.camera.Camera;
import org.andromda.demo.ejb3.camera.CameraException;
import org.andromda.demo.ejb3.camera.CameraManagerDelegate;
import org.andromda.demo.ejb3.camera.CameraPK;
import org.andromda.demo.ejb3.rider.Rider;
import org.andromda.demo.ejb3.rider.RiderException;
import org.andromda.demo.ejb3.rider.RiderServiceDelegate;

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
    
    
    
    
    public void insertRider()
    {
        System.out.println("Inserting rider...");
        
        Rider rider = new Rider("vance", "male");
        
        Bicycle bicycle = new Bicycle("mountain", 24, "off-road");
        rider.setBicycle(bicycle);
        
        RiderServiceDelegate manager = new RiderServiceDelegate(prop);
        try
        {
            manager.addRider(rider);
        } catch (RiderException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
        
        System.out.println("Insert complete.");
    }
    
    
    
    
    public void getRider()
    {
        System.out.println("getting rider...");
        
        RiderServiceDelegate ams = new RiderServiceDelegate(prop);
        try
        {
            Rider rider = ams.getRider(1);
            System.out.println("Rider " + rider.getId() + ", " + rider.getName() + ", " + rider.getGender());
        } 
        catch (RiderException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ams.close();
        }
    }

    
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        Client client = new Client();
        client.init();
        client.insertRider();
    }

}
