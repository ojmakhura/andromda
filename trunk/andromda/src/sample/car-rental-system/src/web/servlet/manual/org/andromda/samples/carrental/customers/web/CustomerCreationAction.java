package org.andromda.samples.carrental.customers.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class CustomerCreationAction extends Action
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

        CustomerCreationForm myForm = (CustomerCreationForm) form;

        try
        {
            CustomerServiceHome ash = CustomerServiceUtil.getHome();
            CustomerService cs = ash.create();

            String customerId =
                cs.createCustomer(
                    myForm.getCustomerName(),
                    myForm.getCustomerNo(),
                    myForm.getPassword());

            cs.remove();

            if (customerId == null)
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.creating.customer"));
                return (mapping.findForward("failure"));
            }

            return (mapping.findForward("success"));
        }
        catch (Exception e)
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.creating.customer"));
            return (mapping.findForward("failure"));
        }
    }
}
