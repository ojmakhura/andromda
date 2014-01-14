// license-header java merge-point
package org.andromda.samples.carrental.inventory.web.registerCarType;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * @see org.andromda.samples.carrental.inventory.web.registerCarType.RegisterCarTypeController
 */
public class RegisterCarTypeControllerImpl extends RegisterCarTypeController
{

	/**
	 * @see org.andromda.samples.carrental.inventory.web.registerCarType.RegisterCarTypeController#createCarType(org.andromda.samples.carrental.inventory.web.registerCarType.CreateCarTypeForm)
	 */
	@Override
	public void createCarType(CreateCarTypeForm form) throws Throwable {
		// this property receives a default value, just to have the application running on dummy data
        form.setManufacter("manufacter-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setComfortClass("comfortClass-test");
        List<SelectItem> confortClass = new ArrayList<SelectItem>();
		confortClass.add(new SelectItem("comfortClass-1"));
		confortClass.add(new SelectItem("comfortClass-2"));
		confortClass.add(new SelectItem("comfortClass-3"));
		confortClass.add(new SelectItem("comfortClass-4"));
		confortClass.add(new SelectItem("comfortClass-5"));
		form.setComfortClassBackingList(confortClass);
        // this property receives a default value, just to have the application running on dummy data
        form.setIdentifier("identifier-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setOrderNo("orderNo-test");
		
	}

	/**
	 * @see org.andromda.samples.carrental.inventory.web.registerCarType.RegisterCarTypeController#searchAllCarTypes(org.andromda.samples.carrental.inventory.web.registerCarType.SearchAllCarTypesForm)
	 */
	@Override
	public void searchAllCarTypes(SearchAllCarTypesForm form) throws Throwable {
		// this property receives a default value, just to have the application running on dummy data
        form.setComfortClass("comfortClass-test");
        List<SelectItem> confortClass = new ArrayList<SelectItem>();
		confortClass.add(new SelectItem("comfortClass-1"));
		confortClass.add(new SelectItem("comfortClass-2"));
		confortClass.add(new SelectItem("comfortClass-3"));
		confortClass.add(new SelectItem("comfortClass-4"));
		confortClass.add(new SelectItem("comfortClass-5"));
		form.setComfortClassBackingList(confortClass);
	}
}
