package org.andromda.samples.carrental.customers.web.registerCustomer;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController
 */
public class RegisterCustomerControllerImpl
        extends RegisterCustomerController
{
    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#createCustomer(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.customers.web.registerCustomer.CreateCustomerForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String createCustomer(ActionMapping mapping,
                                                 org.andromda.samples.carrental.customers.web.registerCustomer.CreateCustomerForm form,
                                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setName("name-test");
        form.setCustomerNo("customerNo-test");
        form.setPassword("password-test");
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#searchAllCustomers(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.customers.web.registerCustomer.SearchAllCustomersForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.util.Collection searchAllCustomers(ActionMapping mapping,
                                                         org.andromda.samples.carrental.customers.web.registerCustomer.SearchAllCustomersForm form,
                                                         HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setCustomers(customersDummyList);
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#loadExistingCustomers(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.customers.web.registerCustomer.LoadExistingCustomersForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadExistingCustomers(ActionMapping mapping,
                                            org.andromda.samples.carrental.customers.web.registerCustomer.LoadExistingCustomersForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setCustomerNo("customerNo-test");
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#loadExistingDrivers(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.customers.web.registerCustomer.LoadExistingDriversForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadExistingDrivers(ActionMapping mapping,
                                          org.andromda.samples.carrental.customers.web.registerCustomer.LoadExistingDriversForm form,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setDrivers(driversDummyList);
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#saveDriver(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.customers.web.registerCustomer.SaveDriverForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void saveDriver(ActionMapping mapping,
                                 org.andromda.samples.carrental.customers.web.registerCustomer.SaveDriverForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setBirthDateAsDate(new java.util.Date());
        form.setLicenseIssuedBy("licenseIssuedBy-test");
        form.setLicenseNo("licenseNo-test");
        form.setName("name-test");
        form.setSurname("surname-test");
    }

    /**
     * This dummy variable is used to populate the "customers" table. You may delete it when you add you own code in
     * this controller.
     */
    private final java.util.Collection customersDummyList = java.util.Arrays.asList(new Object[]{
        new CustomersDummy("customerNo-1", "name-1"), new CustomersDummy("customerNo-2", "name-2"),
        new CustomersDummy("customerNo-3", "name-3"), new CustomersDummy("customerNo-4", "name-4"),
        new CustomersDummy("customerNo-5", "name-5")});

    /**
     * This inner class is used in the dummy implementation in order to get the web application running without any
     * manual programming. You may delete this class when you add you own code in this controller.
     */
    public final class CustomersDummy
            implements java.io.Serializable
    {
        private String customerNo = null;
        private String name = null;

        public CustomersDummy(String customerNo, String name)
        {
            this.customerNo = customerNo;
            this.name = name;
        }

        public void setCustomerNo(String customerNo)
        {
            this.customerNo = customerNo;
        }

        public String getCustomerNo()
        {
            return this.customerNo;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

    }

    /**
     * This dummy variable is used to populate the "drivers" table. You may delete it when you add you own code in this
     * controller.
     */
    private final java.util.Collection driversDummyList = java.util.Arrays.asList(new Object[]{
        new DriversDummy("name-1", "surname-1", "licenseNo-1"), new DriversDummy("name-2", "surname-2", "licenseNo-2"),
        new DriversDummy("name-3", "surname-3", "licenseNo-3"), new DriversDummy("name-4", "surname-4", "licenseNo-4"),
        new DriversDummy("name-5", "surname-5", "licenseNo-5")});

    /**
     * This inner class is used in the dummy implementation in order to get the web application running without any
     * manual programming. You may delete this class when you add you own code in this controller.
     */
    public final class DriversDummy
            implements java.io.Serializable
    {
        private String name = null;
        private String surname = null;
        private String licenseNo = null;

        public DriversDummy(String name, String surname, String licenseNo)
        {
            this.name = name;
            this.surname = surname;
            this.licenseNo = licenseNo;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

        public void setSurname(String surname)
        {
            this.surname = surname;
        }

        public String getSurname()
        {
            return this.surname;
        }

        public void setLicenseNo(String licenseNo)
        {
            this.licenseNo = licenseNo;
        }

        public String getLicenseNo()
        {
            return this.licenseNo;
        }

    }

}
