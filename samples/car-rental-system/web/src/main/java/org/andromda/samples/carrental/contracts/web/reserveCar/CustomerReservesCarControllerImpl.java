// license-header java merge-point
package org.andromda.samples.carrental.contracts.web.reserveCar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController
 */
public class CustomerReservesCarControllerImpl extends CustomerReservesCarController
{
	/**
	 * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController#searchForReservations(org.andromda.samples.carrental.contracts.web.reserveCar.SearchForReservationsForm)
	 */
	@Override
	public void searchForReservations(SearchForReservationsForm form)
			throws Throwable {
		// populating the table with a dummy list
		form.setReservations(reservationsDummyList);
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setComfortClass("comfortClass-test");

		List<SelectItem> confortClass = new ArrayList<SelectItem>();
		confortClass.add(new SelectItem("comfortClass-1"));
		confortClass.add(new SelectItem("comfortClass-2"));
		confortClass.add(new SelectItem("comfortClass-3"));
		confortClass.add(new SelectItem("comfortClass-4"));
		confortClass.add(new SelectItem("comfortClass-5"));
		form.setComfortClassBackingList(confortClass);
	}

	/**
	 * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController#reserve(org.andromda.samples.carrental.contracts.web.reserveCar.ReserveForm)
	 */
	@Override
	public void reserve(ReserveForm form) throws Throwable {
		// setting a date
		form.setReservationDate(new Date());
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setComfortClass("comfortClass-test");
		List<SelectItem> confortClass = new ArrayList<SelectItem>();
		confortClass.add(new SelectItem("comfortClass-1"));
		confortClass.add(new SelectItem("comfortClass-2"));
		confortClass.add(new SelectItem("comfortClass-3"));
		confortClass.add(new SelectItem("comfortClass-4"));
		confortClass.add(new SelectItem("comfortClass-5"));
		form.setComfortClassBackingList(confortClass);
	}

	
	/**
	 * @see org.andromda.samples.carrental.contracts.web.reserveCar.CustomerReservesCarController#deleteReservation(org.andromda.samples.carrental.contracts.web.reserveCar.DeleteReservationForm)
	 */
	@Override
	public void deleteReservation(DeleteReservationForm form) throws Throwable {
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setIdReservation("idReservation-test");
	}
	
    /**
     * This dummy variable is used to populate the "reservations" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection reservationsDummyList =
        Arrays.asList(new ReservationsDummy("reservationDate-1", "comfortClass-1", "idReservation-1"),
            new ReservationsDummy("reservationDate-2", "comfortClass-2", "idReservation-2"),
            new ReservationsDummy("reservationDate-3", "comfortClass-3", "idReservation-3"),
            new ReservationsDummy("reservationDate-4", "comfortClass-4", "idReservation-4"),
            new ReservationsDummy("reservationDate-5", "comfortClass-5", "idReservation-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class ReservationsDummy implements Serializable
    {
        private static final long serialVersionUID = -2085538380562710068L;

        private String reservationDate = null;
        private String comfortClass = null;
        private String idReservation = null;

        public ReservationsDummy(String reservationDate, String comfortClass, String idReservation)
        {
            this.reservationDate = reservationDate;
            this.comfortClass = comfortClass;
            this.idReservation = idReservation;
        }

        public void setReservationDate(String reservationDate)
        {
            this.reservationDate = reservationDate;
        }

        public String getReservationDate()
        {
            return this.reservationDate;
        }

        public void setComfortClass(String comfortClass)
        {
            this.comfortClass = comfortClass;
        }

        public String getComfortClass()
        {
            return this.comfortClass;
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
