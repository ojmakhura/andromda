package org.andromda.demo.ejb3.client.book;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.book.Book;
import org.andromda.demo.ejb3.book.BookException;
import org.andromda.demo.ejb3.book.BookServiceDelegate;
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
	
    
    
    
    public void insertBook()
    {
        System.out.println("Inserting book...");
        
        Book book = new Book("Sheytan", 300);
        
        BookServiceDelegate serviceDelegate = new BookServiceDelegate(prop);
        try
        {
            serviceDelegate.addBook(book);
        } catch (BookException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            serviceDelegate.close();
        }
        
        System.out.println("Insert complete.");
    }

    
    
    
    
    public void deleteBook()
    {
        System.out.println("deleting book...");
        
        BookServiceDelegate serviceDelegate = new BookServiceDelegate(prop);
        try
        {
            serviceDelegate.deleteBook(1);
        } 
        catch (BookException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            serviceDelegate.close();
        }
        
        System.out.println("delete complete.");
    }

    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
    {
		Client client = new Client();
		client.init();
		client.deleteBook();
	}

}
