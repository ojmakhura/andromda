package org.andromda.samples.carrental.customers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

public class CustomerServiceBeanImpl
    extends CustomerServiceBean
    implements SessionBean
{
    // concrete business methods that were declared
    // abstract in class CustomerServiceBean ...
    
    /**
     * @see org.andromda.samples.carrental.customers.CustomerServiceBean#createCustomer(String, String, String)
     */
    public java.lang.String createCustomer(
        java.lang.String name,
        java.lang.String customerNo,
        java.lang.String password)
        throws CustomerException
    {
        try
        {
            CustomerLocalHome clh = getCustomerLocalHome();
            Customer cl = clh.create(name, customerNo, password);
            return cl.getId();
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (CreateException e)
        {
            throw new EJBException(e);
        }
    }

    /**
     * @see org.andromda.samples.carrental.customers.CustomerServiceBean#addDriver(String, DriverData)
     */
    public java.lang.String addDriver(
        java.lang.String customerId,
        DriverData driverData)
        throws CustomerException
    {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see de.mbohlen.uml2ejb.samples.carrental.customers.CustomerServiceBean#searchAllCustomers()
     */
    public java.util.Collection searchAllCustomers()
        throws CustomerException
    {
        try
        {
            CustomerLocalHome clh = getCustomerLocalHome();
            Collection customers = clh.findAll();

            ArrayList result = new ArrayList();
            for (Iterator it = customers.iterator(); it.hasNext();)
            {
                Customer c = (Customer) it.next();
                result.add(getCustomerData(c));
            }

            return result;
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (FinderException e)
        {
            throw new EJBException(e);
        }
    }
    
    /**
     * Extracts the data from a Customer object.
     * @param c the Customer
     * @return CustomerData the data
     */
    private CustomerData getCustomerData(Customer c)
    {
        return new CustomerData(c.getId(), c.getName(), c.getCustomerNo(), c.getPassword());
    }

    /**
     * @see de.mbohlen.uml2ejb.samples.carrental.customers.CustomerServiceBean#authenticateAsCustomer(String, String)
     */
    public java.lang.String authenticateAsCustomer(
        java.lang.String customerNo,
        java.lang.String password)
        throws CustomerException
    {
        try
        {
            CustomerLocalHome clh = getCustomerLocalHome();
            
            Collection customers = clh.findByCustomerNo(customerNo);
            if (customers.size() != 1)
            {
                throw new CustomerException(
                    "Customer # "
                        + customerNo
                        + " could not be found");
            }
            
            Customer theCustomer =
                (Customer) customers.iterator().next();
            if (theCustomer.getPassword().equals(password))
            {
                return theCustomer.getId();
            }
            
            return null;
        }
        catch (NamingException e)
        {
            throw new EJBException (e);
        }
        catch (FinderException e)
        {
            throw new EJBException (e);
        }
    }

    // ---------- the usual session bean stuff... ------------

    private SessionContext context;

    public void setSessionContext(SessionContext ctx)
    {
        //Log.trace("CustomerServiceBean.setSessionContext...");
        context = ctx;
    }

    public void ejbRemove()
    {
        //Log.trace(
        //    "CustomerServiceBean.ejbRemove...");
    }

    public void ejbPassivate()
    {
        //Log.trace("CustomerServiceBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("CustomerServiceBean.ejbActivate...");
    }
}
