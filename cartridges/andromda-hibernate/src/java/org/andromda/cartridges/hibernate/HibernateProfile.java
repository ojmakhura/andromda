package org.andromda.cartridges.hibernate;

import org.andromda.core.common.Profile;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Hibernate profile. Contains the profile information (tagged values, and
 * stereotypes) for the Hibernate cartridge.
 * 
 * @author Chad Brandon
 * @author Carlos Cuenca
 */
public class HibernateProfile
    extends UMLProfile
{
    
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */

    /* ----------------- Tagged Values -------------------- */

    /**
     * Stores a hibernate query string
     */
    public static final String TAGGEDVALUE_HIBERNATE_QUERY = profile.get("HIBERNATE_QUERY");

    /**
     * Stores the viewtype of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = profile.get("EJB_VIEWTYPE");

    /**
     * Stores the aggregation kind (lazy/eager) of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_HIBERNATE_LAZY = profile.get("HIBERNATE_LAZY");

    /**
     * Support for hibernate inheritance strategy, supported values are
     * <ul>
     * <li>class : one table per base class</li>
     * <li>subclass : one table per subclass</li>
     * <li>concrete : one table per class, subclasses may implement subclass or
     * joined-subclass</li>
     * <li>interface : generate interface and put attributes etc on subclasses</li>
     * </ul>
     * See Hibernate documentation for specific details.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = profile.get("HIBERNATE_INHERITANCE");

    /**
     * Defines outer join fetching on many to one and one to one associations
     */
    public static final String TAGGEDVALUE_HIBERNATE_OUTER_JOIN = profile.get("HIBERNATE_OUTER_JOIN");
    /**
     * Defines if a query within a finder method should use the cache
     */
    public static final String TAGGEDVALUE_HIBERNATE_USE_QUERY_CACHE = profile.get("HIBERNATE_USE_QUERY_CACHE");
    /**
     * Defines the cache type for the Entity
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_CACHE = profile.get("HIBERNATE_ENTITY_CACHE");
}
