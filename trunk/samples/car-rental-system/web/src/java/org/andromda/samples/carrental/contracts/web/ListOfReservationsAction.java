package org.andromda.samples.carrental.contracts.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.andromda.samples.carrental.contracts.ContractService;
import org.andromda.samples.carrental.contracts.ContractServiceHome;
import org.andromda.samples.carrental.contracts.ContractServiceUtil;
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
public class ListOfReservationsAction extends Action
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

            myForm.setReservationList(
                ReservationDataFormatter.format(
                    cs.searchForReservationsOfCustomer(customerId),
                    getLocale(request)));

            cs.remove();

            if (myForm.getDecodedReservationDate() == null)
            {
                myForm.setDecodedReservationDate(new Date());
            }

            // return control
            return (mapping.findForward("success"));
        }
        catch (Exception e)
        {
            throw new ServletException(e.getMessage());
            // or ... return (mapping.findForward("failure"));
        }
    }
}
