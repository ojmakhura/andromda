package org.andromda.cartridges.ejb;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * The EJB profile. Contains
 * the profile information (tagged values, and stereotypes) 
 * for the EJB cartridge.
 * 
 * @author Chad Brandon
 */
public class EJBProfile extends UMLProfile {
	
	/* ----------------- Stereotypes -------------------- */
	
	public static final String STEREOTYPE_CREATE_METHOD = "CreateMethod";
	
	public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";
	
	public static final String STEREOTYPE_ENV_ENTRY = "EnvEntry";
	
	/* ----------------- Tagged Values -------------------- */

	public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
	
	public static final String TAGGEDVALUE_EJB_QUERY = "@andromda.ejb.query";	
	
}
