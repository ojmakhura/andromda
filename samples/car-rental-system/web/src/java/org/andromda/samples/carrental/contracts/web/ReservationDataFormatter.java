package org.andromda.samples.carrental.contracts.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.andromda.samples.carrental.contracts.ReservationData;


/**
 * Formats reservation data so that it can be displayed nicely on a web page.
 * 
 * @author Matthias Bohlen
 * 
 */
public class ReservationDataFormatter
{
    private String comfortClass;
    private String reservationDate;

    public ReservationDataFormatter(ReservationData rd, Locale loc)
    {
        DateFormat sdf = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, loc);
        comfortClass = rd.getComfortClass();
        reservationDate = sdf.format(rd.getReservationDate());
    }

    public String getComfortClass()
    {
        return comfortClass;
    }

    public String getReservationDate()
    {
        return reservationDate;
    }
    
    public static Collection format (Collection reservierungen, Locale loc)
    {
        ArrayList ergebnis = new ArrayList();
        
        for (Iterator it = reservierungen.iterator();  it.hasNext();  )
        {
            ReservationData rd = (ReservationData) it.next();
            ergebnis.add(new ReservationDataFormatter (rd, loc));
        }
        
        return ergebnis;
    }
}