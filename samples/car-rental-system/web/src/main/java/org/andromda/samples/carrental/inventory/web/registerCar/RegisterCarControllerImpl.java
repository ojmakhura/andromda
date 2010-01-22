// license-header java merge-point
package org.andromda.samples.carrental.inventory.web.registerCar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.inventory.web.registerCar.RegisterCarController
 */
public class RegisterCarControllerImpl extends RegisterCarController
{
    /**
     * @see org.andromda.samples.carrental.inventory.web.registerCar.RegisterCarController#createCar(org.apache.struts.action.ActionMapping, CreateCarForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String createCar(ActionMapping mapping, CreateCarForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setCarTypeId("carTypeId-test");
        form.setCarTypeIdValueList(new Object[] {"carTypeId-1", "carTypeId-2", "carTypeId-3", "carTypeId-4", "carTypeId-5"});
        form.setCarTypeIdLabelList(form.getCarTypeIdValueList());
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.inventory.web.registerCar.RegisterCarController#searchAllCars(org.apache.struts.action.ActionMapping, SearchAllCarsForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchAllCars(ActionMapping mapping, SearchAllCarsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setCarTypeId("carTypeId-test");
        form.setCarTypeIdValueList(new Object[] {"carTypeId-1", "carTypeId-2", "carTypeId-3", "carTypeId-4", "carTypeId-5"});
        form.setCarTypeIdLabelList(form.getCarTypeIdValueList());
        // populating the table with a dummy list
        form.setExistingcars(existingcarsDummyList);
    }

    /**
     * This dummy variable is used to populate the "existingcars" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection existingcarsDummyList =
        Arrays.asList(new ExistingcarsDummy("inventoryNo-1", "registrationNo-1", "id-1"),
            new ExistingcarsDummy("inventoryNo-2", "registrationNo-2", "id-2"),
            new ExistingcarsDummy("inventoryNo-3", "registrationNo-3", "id-3"),
            new ExistingcarsDummy("inventoryNo-4", "registrationNo-4", "id-4"),
            new ExistingcarsDummy("inventoryNo-5", "registrationNo-5", "id-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class ExistingcarsDummy implements Serializable
    {
        private String inventoryNo = null;
        private String registrationNo = null;
        private String id = null;

        public ExistingcarsDummy(String inventoryNo, String registrationNo, String id)
        {
            this.inventoryNo = inventoryNo;
            this.registrationNo = registrationNo;
            this.id = id;
        }
        
        public void setInventoryNo(String inventoryNo)
        {
            this.inventoryNo = inventoryNo;
        }

        public String getInventoryNo()
        {
            return this.inventoryNo;
        }
        
        public void setRegistrationNo(String registrationNo)
        {
            this.registrationNo = registrationNo;
        }

        public String getRegistrationNo()
        {
            return this.registrationNo;
        }
        
        public void setId(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return this.id;
        }
        
    }
}
