package org.andromda.cartridges.webservice;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * <p>
 * The Metafacade profile. Contains the profile information (tagged values, and
 * stereotypes) for the Metafacade cartridge.
 * </p>
 */
public class WebServiceProfile extends UMLProfile
{

    /**
     * Shouldn't be instantiated.
     */
    private WebServiceProfile()
    {}

    /* ---------------- Tagged Values ------------------- */

    /**
     * <p>
     * Defines the style of the web service to be generated (i.e. wrapped, etc.)
     * </p>
     */
    public static final java.lang.String TAGGEDVALUE_WEBSERVICE_STYLE = "@andromda.webService.style";

    /**
     * <p>
     * The use of the service to be generated (i.e. literal, encoded).
     * </p>
     */
    public static final java.lang.String TAGGEDVALUE_WEBSERVICE_USE = "@andromda.webService.use";

    /**
     * <p>
     * Stores the provider of the service (i.e. RPC, EJB, etc)
     * </p>
     */
    public static final java.lang.String TAGGEDVALUE_WEBSERVICE_PROVIDER = "@andromda.webService.provider";

}
