package org.andromda.cartridges.bpm4struts;

/**
 * Stores the BPM4Struts Global variables.
 * 
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class Bpm4StrutsGlobals
{
    /**
     * Stores the default date format when dates are formmated.
     */
    public final static String PROPERTY_DEFAULT_DATEFORMAT = "defaultDateFormat";
    
    /**
     * Contains the default value for whether or not all forwards
     * should perform a HTTP redirect or not.
     */
    public final static String PROPERTY_DEFAULT_ACTION_REDIRECT = "defaultActionRedirect";

    /**
     * The namespace property use to identify the pattern used to construct the backend
     * service's accessor.
     */
    public final static String PROPERTY_SERVICE_ACCESSOR_PATTERN = "serviceAccessorPattern";
    
    /**
     * The namespace property use to identify the pattern used to construct the backend
     * service's package name.
     */
    public final static String PROPERTY_SERVICE_PACKAGE_NAME_PATTERN = "servicePackageNamePattern";
}
