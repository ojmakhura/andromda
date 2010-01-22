// license-header java merge-point
package org.andromda.samples.carrental.customers.web.registerCustomer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController
 */
public class RegisterCustomerControllerImpl extends RegisterCustomerController
{
    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#createCustomer(org.apache.struts.action.ActionMapping, CreateCustomerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String createCustomer(ActionMapping mapping, CreateCustomerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setPassword("password-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setCustomerNo("customerNo-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setName("name-test");
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#searchAllCustomers(org.apache.struts.action.ActionMapping, SearchAllCustomersForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final Collection searchAllCustomers(ActionMapping mapping, SearchAllCustomersForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setCustomers(customersDummyList);
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#loadExistingCustomers(org.apache.struts.action.ActionMapping, LoadExistingCustomersForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadExistingCustomers(ActionMapping mapping, LoadExistingCustomersForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setCustomerNo("customerNo-test");
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#loadExistingDrivers(org.apache.struts.action.ActionMapping, LoadExistingDriversForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadExistingDrivers(ActionMapping mapping, LoadExistingDriversForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setDrivers(driversDummyList);
    }

    /**
     * @see org.andromda.samples.carrental.customers.web.registerCustomer.RegisterCustomerController#saveDriver(org.apache.struts.action.ActionMapping, SaveDriverForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void saveDriver(ActionMapping mapping, SaveDriverForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setLicenseIssuedBy("licenseIssuedBy-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setSurname("surname-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setLicenseNo("licenseNo-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setName("name-test");
        // setting a date
        form.setBirthDateAsDate(new Date());
    }

    /**
     * This dummy variable is used to populate the "customers" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection customersDummyList =
        Arrays.asList(new CustomersDummy("customerNo-1", "name-1"),
            new CustomersDummy("customerNo-2", "name-2"),
            new CustomersDummy("customerNo-3", "name-3"),
            new CustomersDummy("customerNo-4", "name-4"),
            new CustomersDummy("customerNo-5", "name-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class CustomersDummy implements Serializable
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
     * This dummy variable is used to populate the "drivers" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection driversDummyList =
        Arrays.asList(new DriversDummy("name-1", "surname-1", "licenseNo-1"),
            new DriversDummy("name-2", "surname-2", "licenseNo-2"),
            new DriversDummy("name-3", "surname-3", "licenseNo-3"),
            new DriversDummy("name-4", "surname-4", "licenseNo-4"),
            new DriversDummy("name-5", "surname-5", "licenseNo-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class DriversDummy implements Serializable
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
