package org.andromda.samples.carrental.contracts.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.andromda.samples.carrental.contracts.ContractService;
import org.andromda.samples.carrental.contracts.ContractServiceHome;
import org.andromda.samples.carrental.contracts.ContractServiceUtil;
import org.andromda.samples.carrental.contracts.NotEnoughAvailableException;

/**
 *
 *
 *
 */
public class CarReservationAction extends Action
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

        ReservationForm myForm = (ReservationForm) form;

        try
        {
            ContractServiceHome csh = ContractServiceUtil.getHome();
            ContractService cs = csh.create();

            HttpSession session = request.getSession();
            String customerId =
                (String) session.getAttribute("idLoggedInCustomer");

            cs.reserve(
                customerId,
                myForm.getComfortClass(),
                myForm.getDecodedReservationDate());

            cs.remove();

            // return control
            return (mapping.findForward("success"));
        }
        catch (NotEnoughAvailableException ne)
        {
            errors.add(
                ActionErrors.GLOBAL_ERROR,
                new ActionError("error.CustomerReservationAction.notenoughcars"));
            saveErrors(request, errors);
            return (mapping.findForward("failure"));
        }
        catch (Exception e)
        {
            throw new ServletException(e.getMessage());
            // or ... return (mapping.findForward("failure"));
        }
    }
}
