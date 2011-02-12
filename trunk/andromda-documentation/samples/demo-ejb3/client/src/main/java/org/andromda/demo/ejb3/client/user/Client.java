package org.andromda.demo.ejb3.client.user;

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
    
    
    
    
    public void insertUserWithoutCascade()
    {
        System.out.println("Inserting user...");
        
        User user = new User("joee", "Justin Oee");
        
        Account account = new Account("joee");
        AccountManagerDelegate amd = new AccountManagerDelegate(prop);
        long id = 0;
        try
        {
            id = amd.addAccount(account);
        }
        catch (AccountException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (amd != null)
            {
                amd.close();
            }
        }

        System.out.println("Account: " + id + ", " + account.getName());
        
        user.setAccount(account);
        
        UserManagerDelegate ums = new UserManagerDelegate(prop);
        try
        {
            ums.addUser(user);
        } 
        catch (UserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (ums != null)
            {
                ums.close();
            }
        }
        
        System.out.println("Insert complete.");
    }
    
    
    
    
    public void insertUserWithCascade()
    {
        System.out.println("Inserting user...");
        
        User user = new User("vaka", "Vance Karimi");
        Account account = new Account("vancek");
        user.setAccount(account);
        
        UserManagerDelegate ums = new UserManagerDelegate(prop);
        try
        {
            ums.addUser(user);
        } 
        catch (UserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (ums != null)
            {
                ums.close();
            }
        }
        
        System.out.println("Insert complete.");
    }
    
    
    
    
    public void getUser()
    {
        System.out.println("getting user...");
        
        UserManagerDelegate umd = new UserManagerDelegate(prop);
        try
        {
            User user = umd.getUser("justino");
            System.out.println("User: " + user.getPrincipalId() + ", " + user.getName() + ", " + user.toString());
            System.out.println("Account info: " + user.getAccount().getId() + ", " + user.getAccount().getName());
        } 
        catch (UserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            umd.close();
        }
        
    }

    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        Client client = new Client();
        client.init();
        client.insertUserWithCascade();
    }

}
