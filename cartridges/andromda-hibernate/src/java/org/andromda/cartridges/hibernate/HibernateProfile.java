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
     * Stores the viewtype of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "@andromda.ejb.viewType";

    /**
     * Stores the aggregation kind (lazy/eager) of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_HIBERNATE_LAZY = "@andromda.hibernate.lazy";

	/**
	 * Support for hibernate inheritance strategy, supported values are
	 * <ul>
	 * <li>class : one table per base class
	 * <li>subclass : one table per subclass
	 * <li>concrete : one table per class, subclasses may implement subclass or joined-subclass
	 * <li>interface : generate interface and put attributes etc on subclasses
	 * </ul>
	 * See Hibernate documentation for specific details. 
	 */
	public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = "@andromda.hibernate.inheritance";
	/** Value for one Table per root class */
	public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE_CLASS = "class";
	/** Value for joined-subclass */
	public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE_SUBCLASS = "subclass";
	/** Value for one Table per concrete class */
	public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE_CONRETE = "concrete";
	/** Value make Entity an interface, delegate attributes to subclasses. */
	public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE_INTERFACE = "interface";

}
