package org.andromda.samples.carrental.customers.web;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.andromda.samples.carrental.customers.CustomerService;
import org.andromda.samples.carrental.customers.CustomerServiceHome;
import org.andromda.samples.carrental.customers.CustomerServiceUtil;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 *
 *
 */
public class ListCustomersAction extends Action
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
            CustomerServiceHome csh = CustomerServiceUtil.getHome();
            CustomerService cs = csh.create();
            Collection customerData = cs.searchAllCustomers();
            myForm.setExistingCustomers(customerData);
            
            return (mapping.findForward("success"));
        }
        catch (Exception e)
        {
            throw new ServletException(e.getMessage());
            // or ... return (mapping.findForward("failure"));
        }
    }
}
