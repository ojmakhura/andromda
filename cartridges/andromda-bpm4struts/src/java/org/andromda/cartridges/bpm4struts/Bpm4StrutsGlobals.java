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
     * The namespace property use to identify the type of backend services, accessors to those
     * services will be generated in the front-end controllers.
     */
    public final static String PROPERTY_BACKEND_TYPE = "backendType";
}
