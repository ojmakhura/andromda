package org.andromda.demo.ejb3.client.ticket;

import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.ticket.EmailTicket;
import org.andromda.demo.ejb3.ticket.PaperTicket;
import org.andromda.demo.ejb3.ticket.TicketException;
import org.andromda.demo.ejb3.ticket.TicketManagerDelegate;

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
	
    
    
    
    public void insertEmailTicket()
    {
        System.out.println("Inserting email ticket...");
        
        EmailTicket ticket = new EmailTicket("TwoTribes", "Party", new Date());
        
        TicketManagerDelegate manager = new TicketManagerDelegate(prop);
        try
        {
            manager.addEmailTicket(ticket);
        } catch (TicketException e)
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

    
    
    
    
    public void insertPaperTicket()
    {
        System.out.println("Inserting paper ticket...");
        
        PaperTicket ticket = new PaperTicket("TwoTribes", "Party", new Date(), 3);
        
        TicketManagerDelegate manager = new TicketManagerDelegate(prop);
        try
        {
            manager.addPaperTicket(ticket);
        } catch (TicketException e)
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

    
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
    {
		Client client = new Client();
		client.init();
		client.insertEmailTicket();
        client.insertPaperTicket();
	}

}
