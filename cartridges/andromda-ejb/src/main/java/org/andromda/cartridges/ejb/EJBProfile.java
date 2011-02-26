package org.andromda.cartridges.ejb;

import org.andromda.metafacades.uml.UMLProfile;


/**
 * The EJB profile. Contains the profile information (tagged values, and stereotypes) for the EJB cartridge.
 *
 * @author Chad Brandon
 */
public class EJBProfile
    extends UMLProfile
{
    /* ----------------- Stereotypes -------------------- */
    /**
     * CreateMethod
     */
    public static final String STEREOTYPE_CREATE_METHOD = "CreateMethod";
    /**
     * SelectMethod
     */
    public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";
    /**
     * EnvEntry
     */
    public static final String STEREOTYPE_ENV_ENTRY = "EnvEntry";

    /**
     * Represents a reference to a value object.
     */
    public static final String STEREOTYPE_VALUE_REF = "ValueRef";

    /* ----------------- Tagged Values -------------------- */
    /**
     * andromda_ejb_generateCMR
     */
    public static final String TAGGEDVALUE_GENERATE_CMR = "andromda_ejb_generateCMR";
    /**
     * andromda_ejb_query
     */
    public static final String TAGGEDVALUE_EJB_QUERY = "andromda_ejb_query";
    /**
     * andromda_ejb_viewType
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "andromda_ejb_viewType";
    /**
     * andromda_ejb_transaction_type
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = "andromda_ejb_transaction_type";
    /**
     * andromda_ejb_noSyntheticCreateMethod
     */
    public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD = "andromda_ejb_noSyntheticCreateMethod";
}