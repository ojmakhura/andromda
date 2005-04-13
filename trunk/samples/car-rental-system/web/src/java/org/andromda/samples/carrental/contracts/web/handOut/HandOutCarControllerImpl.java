package org.andromda.samples.carrental.contracts.web.handOut;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController
 */
public class HandOutCarControllerImpl
        extends HandOutCarController
{
    /**
     * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController#loadAvailableCars(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.contracts.web.handOut.LoadAvailableCarsForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String loadAvailableCars(ActionMapping mapping,
                                                    org.andromda.samples.carrental.contracts.web.handOut.LoadAvailableCarsForm form,
                                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setIdReservation("idReservation-test");
        form.setAvailableCars(availableCarsDummyList);
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController#searchForReservationsOfCustomer(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.contracts.web.handOut.SearchForReservationsOfCustomerForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchForReservationsOfCustomer(ActionMapping mapping,
                                                      org.andromda.samples.carrental.contracts.web.handOut.SearchForReservationsOfCustomerForm form,
                                                      HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setCustomerReservations(customerReservationsDummyList);
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.handOut.HandOutCarController#saveSelectedCar(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.contracts.web.handOut.SaveSelectedCarForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void saveSelectedCar(ActionMapping mapping,
                                      org.andromda.samples.carrental.contracts.web.handOut.SaveSelectedCarForm form,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setId("id-test");
    }

    /**
     * This dummy variable is used to populate the "availableCars" table. You may delete it when you add you own code in
     * this controller.
     */
    private final java.util.Collection availableCarsDummyList = java.util.Arrays.asList(new Object[]{
        new AvailableCarsDummy("inventoryNo-1", "registrationNo-1", "id-1"),
        new AvailableCarsDummy("inventoryNo-2", "registrationNo-2", "id-2"),
        new AvailableCarsDummy("inventoryNo-3", "registrationNo-3", "id-3"),
        new AvailableCarsDummy("inventoryNo-4", "registrationNo-4", "id-4"),
        new AvailableCarsDummy("inventoryNo-5", "registrationNo-5", "id-5")});

    /**
     * This inner class is used in the dummy implementation in order to get the web application running without any
     * manual programming. You may delete this class when you add you own code in this controller.
     */
    public final class AvailableCarsDummy
            implements java.io.Serializable
    {
        private String inventoryNo = null;
        private String registrationNo = null;
        private String id = null;

        public AvailableCarsDummy(String inventoryNo, String registrationNo, String id)
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
     * This dummy variable is used to populate the "customerReservations" table. You may delete it when you add you own
     * code in this controller.
     */
    private final java.util.Collection customerReservationsDummyList = java.util.Arrays.asList(new Object[]{
        new CustomerReservationsDummy("comfortClass-1", "reservationDate-1", "idReservation-1"),
        new CustomerReservationsDummy("comfortClass-2", "reservationDate-2", "idReservation-2"),
        new CustomerReservationsDummy("comfortClass-3", "reservationDate-3", "idReservation-3"),
        new CustomerReservationsDummy("comfortClass-4", "reservationDate-4", "idReservation-4"),
        new CustomerReservationsDummy("comfortClass-5", "reservationDate-5", "idReservation-5")});

    /**
     * This inner class is used in the dummy implementation in order to get the web application running without any
     * manual programming. You may delete this class when you add you own code in this controller.
     */
    public final class CustomerReservationsDummy
            implements java.io.Serializable
    {
        private String comfortClass = null;
        private String reservationDate = null;
        private String idReservation = null;

        public CustomerReservationsDummy(String comfortClass, String reservationDate, String idReservation)
        {
            this.comfortClass = comfortClass;
            this.reservationDate = reservationDate;
            this.idReservation = idReservation;
        }

        public void setComfortClass(String comfortClass)
        {
            this.comfortClass = comfortClass;
        }

        public String getComfortClass()
        {
            return this.comfortClass;
        }

        public void setReservationDate(String reservationDate)
        {
            this.reservationDate = reservationDate;
        }

        public String getReservationDate()
        {
            return this.reservationDate;
        }

        public void setIdReservation(String idReservation)
        {
            this.idReservation = idReservation;
        }

        public String getIdReservation()
        {
            return this.idReservation;
        }

    }

}
