package org.andromda.utils.beans.comparators;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Used to sort Boolean values
 *
 * @author Chad Brandon
 */
class BooleanComparator
    implements Comparator,
        Serializable
{
    /**
     * Used to sort Boolean values, both objects are assumed
     * to be Boolean instances.
     * @param objectA 
     * @param objectB 
     * @return compare result
     */
    public int compare(
        Object objectA,
        Object objectB)
    {
        Boolean aAsBoolean = (Boolean)objectA;
        Boolean bAsBoolean = (Boolean)objectB;
        int result = 0;
        if (aAsBoolean.booleanValue() && !bAsBoolean.booleanValue())
        {
            result = 1;
        }
        else if (!aAsBoolean.booleanValue() && bAsBoolean.booleanValue())
        {
            result = -1;
        }
        return result;
    }
}