package org.andromda.samples.carrental.inventory.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.andromda.samples.carrental.inventory.CarTypeData;
import org.andromda.samples.carrental.inventory.InventoryService;
import org.andromda.samples.carrental.inventory.InventoryServiceHome;
import org.andromda.samples.carrental.inventory.InventoryServiceUtil;
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
public class CarTypeCreationAction extends Action
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

        CarTypeCreationForm myForm = (CarTypeCreationForm) form;

        try
        {
            InventoryServiceHome ish = InventoryServiceUtil.getHome();
            InventoryService is = ish.create();

            CarTypeData ctd =
                new CarTypeData(
                    null,
                    myForm.getManufacturer(),
                    myForm.getIdentifier(),
                    myForm.getOrderNo(),
                    myForm.getComfortClass());

            String carTypeId = is.createCarType(ctd);
            
            is.remove();  // do not need service bean any more
            
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
