package org.andromda.cartridges.hibernate;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Hibernate profile. Contains
 * the profile information (tagged values, and stereotypes)
 * for the Hibernate cartridge.
 *
 * @author Chad Brandon
 */
public class HibernateProfile extends UMLProfile
{

    /* ----------------- Stereotypes -------------------- */


    /* ----------------- Tagged Values -------------------- */

    /**
     * Stores a hibernate query string
     */
    public static final String TAGGEDVALUE_HIBERNATE_QUERY = "@andromda.hibernate.query";

    /**
     * Stores the name of an index for an hibernate entity attribute.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INDEX = "@andromda.hibernate.index";
    
    /**
     * Stores whether or not the column is unique.
     */
    public static final String TAGGEDVALUE_HIBERNATE_UNIQUE = "@andromda.hibernate.unique";

    /**
     * Stores the viewtype of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "@andromda.ejb.viewType";

}
