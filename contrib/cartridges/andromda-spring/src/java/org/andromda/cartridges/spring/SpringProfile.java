package org.andromda.cartridges.spring;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Spring profile. Contains
 * the profile information (tagged values, and stereotypes) 
 * for the Spring cartridge.
 * 
 * @author Chad Brandon
 */
public class SpringProfile extends UMLProfile {
	
	/* ----------------- Stereotypes -------------------- */
	
	
	/* ----------------- Tagged Values -------------------- */
	
    /**
     * Stores a hibernate query.
     */
	public static final String TAGGEDVALUE_HIBERNATE_QUERY = "@andromda.hibernate.query";	
    
    /**
     * Stores the hibernate generator class.
     */
    public static final String TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS = "@andromda.hibernate.generator.class";
	
}
