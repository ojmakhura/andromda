package org.andromda.cartridges.bpm4struts;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Contains utilities for bpm4struts.
 * 
 * @author Wouter Zoons
 */
public class Bpm4StrutsUtils
{
    /**
     * Creates and returns a List from an <code>enumeration</code>.
     * 
     * @param enumeration the enumeration from which to create the List.
     * 
     * @return the new List.
     */
    public static List listEnumeration(Enumeration enumeration)
    {
        return Collections.list(enumeration);
    }

}
