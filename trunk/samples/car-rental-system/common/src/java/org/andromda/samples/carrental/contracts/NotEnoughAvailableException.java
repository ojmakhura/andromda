package org.andromda.samples.carrental.contracts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>This exception is thrown when there are not enough cars of a certain
 * comfort class available on a given date.</p>
 * 
 * <p>It extends ContractException so that it is possible to
 * throw an instance of it from a method in a class of the
 * "Contracts" component.</p>
 * 
 * @author Matthias Bohlen
 *
 */
public class NotEnoughAvailableException extends ContractException
{
    private String comfortClass;
    private Date reservationDate;

    /**
     * Constructor NotEnoughAvailableException.
     * @param comfortClass the comfort class of the unavailable cars
     * @param reservationDate the date for which the reservation was requested
     */
    public NotEnoughAvailableException(
        String comfortClass,
        Date reservationDate)
    {
        this.comfortClass = comfortClass;
        this.reservationDate = reservationDate;
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage()
    {
        DateFormat sdf =
            SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);

        return "Not enough cars of class "
            + comfortClass
            + " available on "
            + sdf.format(reservationDate);
    }
}
