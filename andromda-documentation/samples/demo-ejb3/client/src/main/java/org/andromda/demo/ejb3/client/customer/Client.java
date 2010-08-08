/**
 * 
 */
package org.andromda.demo.ejb3.client.customer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

import org.andromda.demo.ejb3.customer.Customer;
import org.andromda.demo.ejb3.customer.CustomerException;
import org.andromda.demo.ejb3.customer.CustomerServiceWSInterface;
import org.jboss.ws.jaxrpc.ServiceFactoryImpl;

/**
 * @author VanceKarimi
 *
 */
public class Client
{
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            new Client().start1();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private CustomerServiceWSInterface getTicketService()
        throws NamingException, MalformedURLException, ServiceException
    {
        URL wsdlURL = new URL("http://localhost/ejb3demo-ws/services/CustomerService?wsdl");

        ServiceFactoryImpl factory = new ServiceFactoryImpl();
        
        //QName serviceName = new QName("http://org.andromda.demo3.ejb3.test/TicketService", "TicketServiceWSInterface");
        //Service service = factory.createService(wsdlURL, null);
        
        File mappingFile = new File("M:/workspace3.2/ejb3demo/jaxrpc-mapping.xml");
        Service service = factory.createService(wsdlURL, null, mappingFile.toURL());
        
        CustomerServiceWSInterface port = (CustomerServiceWSInterface)service.getPort(CustomerServiceWSInterface.class);
        return port;
    }
    
    /**
     * 
     */
    private void start1() 
        throws Exception
    {
        CustomerServiceWSInterface ws = getTicketService();
        
        Customer customer = new Customer();
        customer.setName("vance");
        customer.setUsername("vancek");
        customer.setPassword("vk");
        ws.addCustomer(customer);
        System.out.println("Customer Id added = " + customer.getId());
    }

    
    private void start2()
        throws Exception
    {
        Properties prop = new Properties();
        prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        prop.put("java.naming.provider.url", "localhost");
        
        CustomerServiceWSInterface ws = getTicketService();
        try
        {
            Customer[] customer = ws.getAllCustomers();
            
            for (int i = 0; i < customer.length; i++)
            {
                System.out.println("Customer = " + customer[i].getId() + ", " + customer[i].getName());
            }
        }
        catch (CustomerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
