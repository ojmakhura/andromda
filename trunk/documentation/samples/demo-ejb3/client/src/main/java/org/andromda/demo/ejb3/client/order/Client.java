package org.andromda.demo.ejb3.client.order;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.order.OrderInfo;
import org.andromda.demo.ejb3.order.OrderException;
import org.andromda.demo.ejb3.order.OrderManagerDelegate;
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
	
    
    
    
    public void insertOrder()
    {
        System.out.println("Inserting order...");
        
        OrderInfo order = new OrderInfo("Test Order7", 7);
        
        OrderManagerDelegate manager = new OrderManagerDelegate(prop);
        try
        {
            manager.addOrder(order);
        } catch (OrderException e)
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

    
    
    
    
    public void getAllAccounts()
    {
        System.out.println("getting all accounts...");
        
        OrderManagerDelegate manager = new OrderManagerDelegate(prop);
        try
        {
            Collection<OrderInfo> orderInfos = manager.getAllOrders();
            for (OrderInfo order : orderInfos)
            {
                System.out.println("Order " + order.getId() + ", " + order.getDescription() + ", " + order.getVolume());
            }

        } 
        catch (OrderException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
    }

    
    
    
    
    public void getAccountsStartingIndex()
    {
        int index = 3;
        
        System.out.println("getting accounts at index " + index + " ...");
        
        OrderManagerDelegate manager = new OrderManagerDelegate(prop);
        try
        {
            Collection<OrderInfo> orders = manager.getOrders(index);
            for (OrderInfo order : orders)
            {
                System.out.println("Order " + order.getId() + ", " + order.getDescription() + ", " + order.getVolume());
            }

        } 
        catch (OrderException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
    }
    
    
    
    
    
    public void getAccountsStartingIndexAndMaxCount()
    {
        int index = 3;
        int max = 3;
        
        System.out.println("getting accounts at index " + index + " and max " + max + " ...");
        
        OrderManagerDelegate manager = new OrderManagerDelegate(prop);
        try
        {
            Collection<OrderInfo> orders = manager.getOrders(index, max);
            for (OrderInfo order : orders)
            {
                System.out.println("Order " + order.getId() + ", " + order.getDescription() + ", " + order.getVolume());
            }

        } 
        catch (OrderException e)
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
		client.getAccountsStartingIndex();
	}

}
