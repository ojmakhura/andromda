package org.andromda.demo.ejb3.client.mobile;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.mobile.Mobile;
import org.andromda.demo.ejb3.mobile.MobileException;
import org.andromda.demo.ejb3.mobile.MobileServiceDelegate;
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
		prop.put("java.naming.provider.url", "localhost:1099");
	}
	
    
    
    
    public void insertMobile1()
    {
        System.out.println("Inserting mobile...");
        
        UserManagerDelegate manager = new UserManagerDelegate(prop);
        User user = null;
        try
        {
            user = manager.getUser("vaka");
        }
        catch (UserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
        
        Mobile mobile = new Mobile("0411222333", "Optus", user);
        
        MobileServiceDelegate msd = new MobileServiceDelegate(prop);
        try
        {
            msd.addMobile(mobile);
        } catch (MobileException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            msd.close();
        }
        
        System.out.println("Insert complete.");
    }

    
    
    
    public void insertMobile2()
    {
        System.out.println("Inserting mobile...");
        
        Mobile mobile = new Mobile("0411222444", "Telstra");
        
        MobileServiceDelegate msd = new MobileServiceDelegate(prop);
        try
        {
            msd.addMobile(mobile);
        } catch (MobileException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            msd.close();
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
		client.insertMobile1();
	}

}
