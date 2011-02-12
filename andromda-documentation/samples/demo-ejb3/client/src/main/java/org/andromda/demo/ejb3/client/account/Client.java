package org.andromda.demo.ejb3.client.account;

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
        prop.put("java.naming.provider.url", "localhost");
    }
    
    
    
    
    public void insertAccount()
    {
        System.out.println("Inserting account...");
        
        Account account = new Account("SheytanKarimi");
        
        AccountManagerDelegate ams = new AccountManagerDelegate(prop);
        try
        {
            ams.addAccount(account);
        } catch (AccountException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ams.close();
        }
        
        System.out.println("Insert complete.");
    }

    
    
    
    
    public void deleteAccount()
    {
        System.out.println("deleting account...");
        
        AccountManagerDelegate ams = new AccountManagerDelegate(prop);
        try
        {
            ams.deleteAccount(2);
        } 
        catch (AccountException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ams.close();
        }
        
        System.out.println("delete complete.");
    }
    
    
    
    
    public void getAccount()
    {
        System.out.println("getting account...");
        
        AccountManagerDelegate ams = new AccountManagerDelegate(prop);
        try
        {
            Account account = ams.getAccount(1);
            System.out.println("Account " + account.getId() + ", " + account.getName() + ", " + account.toString());
        } 
        catch (AccountException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ams.close();
        }
    }

    
    
    
    
    public void insertAccountManual()
    {
        System.out.println("Inserting account...");
        
        Account account = new Account("VanceKarimi");

        InitialContext ctx = null;
        try
        {
            ctx = new InitialContext(prop);
            AccountManagerRemote manager = (AccountManagerRemote)ctx.lookup(AccountManagerRemote.class.getName());
            manager.addAccount(account);
        } 
        catch (NamingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (AccountException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (ctx != null)
            {
                try
                {
                    ctx.close();
                } catch (NamingException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
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
        client.insertAccount();
    }

}
