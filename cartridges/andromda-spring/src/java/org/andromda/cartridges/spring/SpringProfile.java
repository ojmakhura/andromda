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

    /**
     * Stores the hibernate lazy attribute for relationships.
     */
    public static final String TAGGEDVALUE_HIBERNATE_LAZY = "@andromda.hibernate.lazy";
    
    /**
     * Stores the location of where a modeled entity operation should be generated 
     * (<code>dao</code> or <code>entity</code>).
     */
    public static final String TAGGEDVALUE_ENTITY_OPERATION_LOCATION = "@andromda.spring.entity.operation.location";

}
