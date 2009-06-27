// license-header java merge-point
package org.andromda.samples.carrental.inventory.web.registerCarType;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.inventory.web.registerCarType.RegisterCarTypeController
 */
public class RegisterCarTypeControllerImpl extends RegisterCarTypeController
{
    /**
     * @see org.andromda.samples.carrental.inventory.web.registerCarType.RegisterCarTypeController#createCarType(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.inventory.web.registerCarType.CreateCarTypeForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void createCarType(ActionMapping mapping, org.andromda.samples.carrental.inventory.web.registerCarType.CreateCarTypeForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setManufacter("manufacter-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setComfortClass("comfortClass-test");
        form.setComfortClassValueList(new Object[] {"comfortClass-1", "comfortClass-2", "comfortClass-3", "comfortClass-4", "comfortClass-5"});
        form.setComfortClassLabelList(form.getComfortClassValueList());
        // this property receives a default value, just to have the application running on dummy data
        form.setIdentifier("identifier-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setOrderNo("orderNo-test");
    }

    /**
     * @see org.andromda.samples.carrental.inventory.web.registerCarType.RegisterCarTypeController#searchAllCarTypes(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.inventory.web.registerCarType.SearchAllCarTypesForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchAllCarTypes(ActionMapping mapping, org.andromda.samples.carrental.inventory.web.registerCarType.SearchAllCarTypesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setComfortClass("comfortClass-test");
        form.setComfortClassValueList(new Object[] {"comfortClass-1", "comfortClass-2", "comfortClass-3", "comfortClass-4", "comfortClass-5"});
        form.setComfortClassLabelList(form.getComfortClassValueList());
    }

}