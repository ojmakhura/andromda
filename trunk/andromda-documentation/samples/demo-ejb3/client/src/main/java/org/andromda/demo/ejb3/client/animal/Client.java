package org.andromda.demo.ejb3.client.animal;

import java.util.Collection;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.animal.Animal;
import org.andromda.demo.ejb3.animal.AnimalCreateException;
import org.andromda.demo.ejb3.animal.AnimalServiceDelegate;
import org.andromda.demo.ejb3.user.User;
import org.andromda.demo.ejb3.user.UserException;
import org.andromda.demo.ejb3.user.UserManagerDelegate;
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
		prop.put("java.naming.provider.url", "localhost");
	}
	
    
    
    
    public void insertAnimal()
    {
        System.out.println("Inserting animal...");
        
        Animal animal = new Animal("sheep", "farm", false);
        
        AnimalServiceDelegate manager = new AnimalServiceDelegate(prop);
        try
        {
            manager.addAnimal(animal);
        } catch (AnimalCreateException e)
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
    
    
    
    
    public void getAnimals()
    {
        System.out.println("getting animals...");
        
        AnimalServiceDelegate manager = new AnimalServiceDelegate(prop);
        try
        {
            Collection<Animal> animals = manager.getAllAnimals();
            for (Animal animal : animals)
            {
                System.out.println("Account " + animal.getName() + ", " + animal.getType() + ", " + animal.getCarnivor());
            }
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
    }

    
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
    {
		Client client = new Client();
		client.init();
		client.getAnimals();
	}

}
