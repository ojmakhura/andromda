// license-header java merge-point
package org.andromda.samples.carrental.customers;



/**
 * @see org.andromda.samples.carrental.customers.CustomerServiceBean
 */
public class CustomerServiceBeanImpl extends CustomerServiceBean
{
    /**
     * @see org.andromda.samples.carrental.customers.CustomerService#createCustomer(java.lang.String, java.lang.String,
            *      java.lang.String)
     */
    public java.lang.String createCustomer(java.lang.String name, java.lang.String customerNo,
                                           java.lang.String password)
            throws org.andromda.samples.carrental.customers.CustomerException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.CustomerService#addDriver(java.lang.String,
            *      org.andromda.samples.carrental.customers.DriverData)
     */
    public java.lang.String addDriver(java.lang.String customerId,
                                      org.andromda.samples.carrental.customers.DriverData driverData)
            throws org.andromda.samples.carrental.customers.CustomerException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.CustomerService#searchAllCustomers()
     */
    public java.util.Collection searchAllCustomers() throws org.andromda.samples.carrental.customers.CustomerException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.CustomerService#authenticateAsCustomer(java.lang.String,
            *      java.lang.String)
     */
    public java.lang.String authenticateAsCustomer(java.lang.String customerNo, java.lang.String password)
            throws org.andromda.samples.carrental.customers.CustomerException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

}
