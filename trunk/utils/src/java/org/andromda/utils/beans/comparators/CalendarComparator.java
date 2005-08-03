package org.andromda.utils.beans.comparators;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Used to sort by Calendar values
 * 
 * @author Chad Brandon
 */
class CalendarComparator implements Comparator, Serializable  {
	
	/**
	 * Used to sort Calendar values, both objects are assumed to be assignable
	 * to java.util.Calendar
	 */
	public int compare(Object objectA, Object objectB) {
		Calendar aAsCalendar = (Calendar)objectA;
		Calendar bAsCalendar = (Calendar)objectB;
		int result = 0;
		
		if (bAsCalendar.after(aAsCalendar)) {
			// set result to a negative integer if the first argument of this 
			// method is less than the second
			result = -1;
		} else if (aAsCalendar.after(bAsCalendar)) {
			// set result to a positive integer if the first argument of this 
			// method is greater than the second
			result = 1;	
		}
		return result;
	}
	

}
