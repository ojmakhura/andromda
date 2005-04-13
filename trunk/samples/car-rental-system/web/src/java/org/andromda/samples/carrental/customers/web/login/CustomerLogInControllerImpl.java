package org.andromda.samples.carrental.customers.web.login;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.customers.web.login.CustomerLogInController
 */
public class CustomerLogInControllerImpl
        extends CustomerLogInController
{
    /**
     * @see org.andromda.samples.carrental.customers.web.login.CustomerLogInController#authenticateAsCustomer(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.customers.web.login.AuthenticateAsCustomerForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String authenticateAsCustomer(ActionMapping mapping,
                                                         org.andromda.samples.carrental.customers.web.login.AuthenticateAsCustomerForm form,
                                                         HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        String result = "success";
        // TODO add proper login check
        form.setCustomerNo("Enter customerNo");
        form.setPassword("");
        return result;
    }

}
