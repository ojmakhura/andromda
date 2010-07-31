package org.andromda.demo.ejb3.client.rental;

import java.util.Properties;

import org.andromda.demo.ejb3.email.EmailSenderException;
import org.andromda.demo.ejb3.email.EmailSenderServiceDelegate;
import org.andromda.demo.ejb3.rental.CarType;
import org.andromda.demo.ejb3.rental.RentalCar;
import org.andromda.demo.ejb3.rental.RentalServiceDelegate;
import org.andromda.demo.ejb3.rental.RentalServiceException;

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
	
    
    
    
    public void rentCar()
    {
        System.out.println("Start renting car...");
        
        RentalCar car = new RentalCar("ABC111", "Ford Falcon", CarType.SEDAN);
        
        RentalServiceDelegate manager = new RentalServiceDelegate(prop);
        try
        {
            manager.processRental(car, 1);
        }
        catch (RentalServiceException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (manager != null)
            {
                manager.close();
            }
        }
        
        System.out.println("Send complete.");
    }

    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
    {
		Client client = new Client();
		client.init();
		client.rentCar();
	}

}
