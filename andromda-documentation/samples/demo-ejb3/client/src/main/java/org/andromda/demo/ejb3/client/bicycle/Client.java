package org.andromda.demo.ejb3.client.bicycle;

import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.andromda.demo.ejb3.bicycle.Bicycle;
import org.andromda.demo.ejb3.bicycle.BicycleException;
import org.andromda.demo.ejb3.bicycle.BicycleServiceDelegate;
import org.andromda.demo.ejb3.employee.Employee;
import org.andromda.demo.ejb3.employee.EmployeeContractType;
import org.andromda.demo.ejb3.employee.EmployeeDeptCode;
import org.andromda.demo.ejb3.employee.EmployeeException;
import org.andromda.demo.ejb3.employee.EmployeeServiceDelegate;

public class Client 
{
    private AppCallbackHandler handler = null;
    private LoginContext lc = null;
    private String principalId = "admin";
    private String password = "admin";
    
	private Properties prop;
	
	public void init() 
    {
		prop = new Properties();
		prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		prop.put("java.naming.provider.url", "localhost:1099");
        
        System.setProperty("java.security.auth.login.config", "./auth.conf");
	}
	
    
    
    
    public void insertBicycle()
    {
        System.out.println("Inserting bicycle...");
        
        Bicycle bicycle = new Bicycle("mountain", 24, "road");
        
        try
        {
            login();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        
        BicycleServiceDelegate manager = new BicycleServiceDelegate(prop);
        try
        {
            manager.addBicycle(bicycle);
        } catch (BicycleException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
            
            try
            {
                logout();
            }
            catch (LoginException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        System.out.println("Insert complete.");
    }
    
    
    
    
    
    public void getBicycle()
    {
        System.out.println("getting bicycle...");
        
        try
        {
            login();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        
        BicycleServiceDelegate manager = new BicycleServiceDelegate(prop);
        try
        {
            Bicycle bicycle = manager.getBicycle(1);
            System.out.println("bicycle " + bicycle.getId() + ", " + bicycle.getType() + ", " + bicycle.getGears() + ", " + bicycle.getTyreTypes());
        } 
        catch (BicycleException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
            
            try
            {
                logout();
            }
            catch (LoginException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    
    
    
    private void login()
        throws LoginException
    {
        handler = new AppCallbackHandler(principalId, password.toCharArray());
        lc = new LoginContext("ejb3demo2", handler);
        System.out.println("Authenticating username[" + principalId + "] password[" + new String(password) + "]");
        lc.login();
    }
    
    
    
    
    public void logout() 
        throws LoginException 
    {
        
        if (lc != null) {
            lc.logout();
        } else {
            System.out.println("LoginContext is undefined");
        }
    }
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
    {
		Client client = new Client();
		client.init();
		client.getBicycle();
	}

    
    /**
     * CallbackHandler class for JAAS authentication
     * 
     * @author VanceKarimi
     * @version 1.0, 10/12/2004
     */
    private static class AppCallbackHandler 
        implements CallbackHandler {
        
        private String username;
        private char[] passwd;

        public AppCallbackHandler(String username, char[] passwd) {
            this.username = username;
            this.passwd = passwd;
        }

        public void handle(Callback[] callbacks) 
            throws java.io.IOException, UnsupportedCallbackException {
            
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof NameCallback) {
                    NameCallback nc = (NameCallback) callbacks[i];
                    nc.setName(username);
                } else if (callbacks[i] instanceof PasswordCallback) {
                    PasswordCallback pc = (PasswordCallback) callbacks[i];
                    pc.setPassword(passwd);
                } else {
                    throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
                }
            }
        }
    }
}
