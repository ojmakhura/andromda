package org.andromda.samples.carrental.admins.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.andromda.samples.carrental.admins.AdminService;
import org.andromda.samples.carrental.admins.AdminServiceHome;
import org.andromda.samples.carrental.admins.AdminServiceUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Describes the login process for the administrator.
 *
 */
public class AdminLoginAction extends Action
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

        AdminLoginForm myForm = (AdminLoginForm) form;

        try
        {
            AdminServiceHome ash = AdminServiceUtil.getHome();
            AdminService as = ash.create();

            String adminId =
                as.authenticateAsAdministrator(myForm.getName(), myForm.getAccountNo(), myForm.getPassword());
            as.remove();

            if (adminId == null)
            {
                return (new ActionForward(mapping.getInput()));
            }

            // Keep the ID of the logged-in administrator in the servlet session
            HttpSession session = request.getSession();
            session.setAttribute("idLoggedInAdmin", adminId);

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
            throw new ServletException(e.getMessage());
            // or ... return (mapping.findForward("failure"));
        }
    }
}
