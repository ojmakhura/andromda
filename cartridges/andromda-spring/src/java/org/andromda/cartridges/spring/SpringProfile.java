package org.andromda.cartridges.spring;

import org.andromda.core.common.Profile;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Spring profile. Contains the profile information (tagged values, and
 * stereotypes) for the Spring cartridge.
 * 
 * @author Chad Brandon
 */
public class SpringProfile
    extends UMLProfile
{

    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */

    /* ----------------- Tagged Values -------------------- */

    /**
     * Stores a hibernate query.
     */
    public static final String TAGGEDVALUE_HIBERNATE_QUERY = profile
        .get("HIBERNATE_QUERY");

    /**
     * Stores the hibernate generator class.
     */
    public static final String TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS = profile
        .get("HIBERNATE_GENERATOR_CLASS");

    /**
     * Stores the hibernate lazy attribute for relationships.
     */
    public static final String TAGGEDVALUE_HIBERNATE_LAZY = profile
        .get("HIBERNATE_LAZY");

    /**
     * Stores the hibernate lazy attribute for relationships.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = profile
        .get("HIBERNATE_INHERITANCE");

}
