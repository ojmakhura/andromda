// license-header java merge-point
package org.andromda.samples.carrental.admins.web.main;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.admins.web.main.AdministrationController
 */
public class AdministrationControllerImpl extends AdministrationController
{
    /**
     * @see org.andromda.samples.carrental.admins.web.main.AdministrationController#loadCarAndCarTypes(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.admins.web.main.LoadCarAndCarTypesForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadCarAndCarTypes(ActionMapping mapping, org.andromda.samples.carrental.admins.web.main.LoadCarAndCarTypesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setCars(carsDummyList);
        // populating the table with a dummy list
        form.setCarTypes(carTypesDummyList);
    }

    /**
     * This dummy variable is used to populate the "cars" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final java.util.Collection carsDummyList =
        java.util.Arrays.asList(new CarsDummy("inventoryNo-1", "registrationNo-1", "id-1"),
                new CarsDummy("inventoryNo-2", "registrationNo-2", "id-2"),
                new CarsDummy("inventoryNo-3", "registrationNo-3", "id-3"),
                new CarsDummy("inventoryNo-4", "registrationNo-4", "id-4"),
                new CarsDummy("inventoryNo-5", "registrationNo-5", "id-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class CarsDummy implements java.io.Serializable
    {
        private String inventoryNo = null;
        private String registrationNo = null;
        private String id = null;

        public CarsDummy(String inventoryNo, String registrationNo, String id)
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
    /**
     * This dummy variable is used to populate the "carTypes" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final java.util.Collection carTypesDummyList =
        java.util.Arrays.asList(new CarTypesDummy("comfortClass-1", "identifier-1", "manufacter-1", "id-1"),
                new CarTypesDummy("comfortClass-2", "identifier-2", "manufacter-2", "id-2"),
                new CarTypesDummy("comfortClass-3", "identifier-3", "manufacter-3", "id-3"),
                new CarTypesDummy("comfortClass-4", "identifier-4", "manufacter-4", "id-4"),
                new CarTypesDummy("comfortClass-5", "identifier-5", "manufacter-5", "id-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class CarTypesDummy implements java.io.Serializable
    {
        private String comfortClass = null;
        private String identifier = null;
        private String manufacter = null;
        private String id = null;

        public CarTypesDummy(String comfortClass, String identifier, String manufacter, String id)
        {
            this.comfortClass = comfortClass;
            this.identifier = identifier;
            this.manufacter = manufacter;
            this.id = id;
        }
        
        public void setComfortClass(String comfortClass)
        {
            this.comfortClass = comfortClass;
        }

        public String getComfortClass()
        {
            return this.comfortClass;
        }
        
        public void setIdentifier(String identifier)
        {
            this.identifier = identifier;
        }

        public String getIdentifier()
        {
            return this.identifier;
        }
        
        public void setManufacter(String manufacter)
        {
            this.manufacter = manufacter;
        }

        public String getManufacter()
        {
            return this.manufacter;
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