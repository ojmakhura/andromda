// license-header java merge-point
package org.andromda.samples.carrental.customers.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.customers.web.login.CustomerLogInController
 */
public class CustomerLogInControllerImpl extends CustomerLogInController
{
    /**
     * @see org.andromda.samples.carrental.customers.web.login.CustomerLogInController#authenticateAsCustomer(org.apache.struts.action.ActionMapping, AuthenticateAsCustomerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String authenticateAsCustomer(ActionMapping mapping, AuthenticateAsCustomerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setPassword("password-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setCustomerNo("customerNo-test");
        return null;
    }
}
