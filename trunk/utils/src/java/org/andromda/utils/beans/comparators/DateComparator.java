package org.andromda.utils.beans.comparators;

import java.io.Serializable;

import java.util.Comparator;
import java.util.Date;


/**
 * Used to sort by Date values
 *
 * @author Chad Brandon
 */
class DateComparator
    implements Comparator,
        Serializable
{
    /**
     * Used to sort Date values, both objects are assumed to be assignable
     * to java.util.Date
     */
    public int compare(
        final Object objectA,
        final Object objectB)
    {
        final Date aAsDate = (Date)objectA;
        final Date bAsDate = (Date)objectB;
        int result = 0;

        if (bAsDate.after(aAsDate))
        {
            // set result to a negative integer if the first argument of this 
            // method is less than the second
            result = -1;
        }
        else if (aAsDate.after(bAsDate))
        {
            // set result to a positive integer if the first argument of this 
            // method is greater than the second
            result = 1;
        }
        return result;
    }
}