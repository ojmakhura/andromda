package org.andromda.demo.ejb3.client.email;

import java.util.Properties;

import org.andromda.demo.ejb3.email.EmailSenderException;
import org.andromda.demo.ejb3.email.EmailSenderServiceDelegate;

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
    
    
    
    
    public void sendEmail()
    {
        System.out.println("Sending email...");
        
        EmailSenderServiceDelegate essd = new EmailSenderServiceDelegate(prop);
        try
        {
            essd.sendEmail("Vance", "Vance", "Test", "This is a test message to check MDB");
            essd.sendEmail("Vance", "Vance", "Test", "This is a test message to check MDB2");
            essd.sendEmail("Vance", "Vance", "Test", "This is a test message to check MDB3");
            essd.sendEmail("Vance", "Vance", "Test", "This is a test message to check MDB4");
        }
        catch (EmailSenderException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (essd != null)
            {
                essd.close();
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
        client.sendEmail();
    }

}
