package org.andromda.samples.carrental.customers.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.andromda.samples.carrental.customers.CustomerService;
import org.andromda.samples.carrental.customers.CustomerServiceHome;
import org.andromda.samples.carrental.customers.CustomerServiceUtil;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 *
 *
 *
 */
public class CustomerLoginAction extends Action
{

    /**
     * @see Action#perform(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward perform(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException
    {
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();

        CustomerLoginForm myForm = (CustomerLoginForm) form;

        try
        {
            CustomerServiceHome csh = CustomerServiceUtil.getHome();
            CustomerService cs = csh.create();

            String customerId =
                cs.authenticateAsCustomer(
                    myForm.getCustomerNumber(),
                    myForm.getPassword());
            cs.remove();

            if (customerId == null)
            {
                errors.add(
                    ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.CustomerLoginAction.auth"));
                saveErrors(request, errors);
                return (mapping.findForward("failure"));
            }

            // Keep the ID of the logged-in administrator in the servlet session
            HttpSession session = request.getSession();
            session.setAttribute("idLoggedInCustomer", customerId);

            // Remove the form bean so that the old data
            // do not re-appear in the login form when the user
            // hits the "back" button.
            if (mapping.getAttribute() != null)
            {
                if ("request".equals(mapping.getScope()))
                    request.removeAttribute(mapping.getAttribute());
            }

            // Return to the URI for success
            return (mapping.findForward("success"));
        }
        catch (Exception e)
        {
            errors.add(
                ActionErrors.GLOBAL_ERROR,
                new ActionError("error.CustomerLoginAction.auth"));
            saveErrors(request, errors);
            return (mapping.findForward("failure"));
        }
    }
}
