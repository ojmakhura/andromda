package org.andromda.samples.carrental.webutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * <p>This class helps to convert values from Struts form beans
 * from their display representation to their internal representation.</p>
 * 
 * <p>UML2EJB automatically inserts calls to the member functions
 * of this class into special getters and setters of the generated
 * form bean classes.</p>
 * 
 * @author Matthias Bohlen
 */
public class FormFieldMapper
{
    private static DateFormat simpleDateFormat =
        SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);

    /**
     * Converts from a string to a java.util.Date.
     * @param s the string
     * @return Date the converted Date
     * @throws ParseException if the syntax is not correct
     */
    public static Date mapToDate(String s)
        throws ParseException
    {
        return simpleDateFormat.parse(s);
    }

    /**
     * Converts from a Date to a String.
     * @param d the Date
     * @return String the converted String
     */
    public static String mapFromDate(Date d)
    {
        return simpleDateFormat.format(d);
    }

    /**
     * Converts a String to an int.
     * @param s the String
     * @return int the converted int
     */
    public static int mapToInt(String s)
    {
        return Integer.parseInt(s);
    }

    /**
     * Coverts an int to a String.
     * @param i the int
     * @return String the converted String
     */
    public static String mapFromInt(int i)
    {
        return String.valueOf(i);
    }
}
