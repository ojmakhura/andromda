package org.andromda.samples.carrental.customers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

public class CustomerServiceBeanImpl
    extends CustomerServiceBean
    implements javax.ejb.SessionBean
{
    // concrete business methods that were declared
    // abstract in class CustomerServiceBean ...

    protected java.lang.String handleCreateCustomer(
        cirrus.hibernate.Session sess,
        java.lang.String name,
        java.lang.String customerNo,
        java.lang.String password)
        throws CustomerException
    {
        try
        {
            Customer cl =
                CustomerFactory.create(name, customerNo, password);
            return (String) sess.save(cl);
        }
        catch (Exception e)
        {
            throw new EJBException(e);
        }
    }

    protected java.lang.String handleAddDriver(
        cirrus.hibernate.Session sess,
        java.lang.String customerId,
        org.andromda.samples.carrental.customers.DriverData driverData)
        throws CustomerException
    {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    protected java.util.Collection handleSearchAllCustomers(
        cirrus.hibernate.Session sess)
        throws CustomerException
    {
        try
        {
            Collection customers = CustomerFactory.findAll(sess);

            ArrayList result = new ArrayList();
            for (Iterator it = customers.iterator(); it.hasNext();)
            {
                Customer c = (Customer) it.next();
                result.add(getCustomerData(c));
            }

            return result;
        }
        catch (Exception e)
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

    protected java.lang.String handleAuthenticateAsCustomer(
        cirrus.hibernate.Session sess,
        java.lang.String customerNo,
        java.lang.String password)
        throws CustomerException
    {
        try
        {
            Collection customers = CustomerFactory.findByCustomerNo(sess, customerNo);
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
        catch (Exception e)
        {
            throw new EJBException (e);
        }
    }

    // ---------- the usual session bean stuff... ------------

    public void setSessionContext(javax.ejb.SessionContext ctx)
    {
        //Log.trace("CustomerServiceBean.setSessionContext...");
        super.setSessionContext(ctx);
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
