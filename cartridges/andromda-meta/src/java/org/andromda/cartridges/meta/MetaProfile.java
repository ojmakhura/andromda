package org.andromda.cartridges.meta;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Meta profile. Contains the profile information (tagged values, and
 * stereotypes) for the Meta cartridge.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public class MetaProfile
    extends UMLProfile
{
    /* ----------------- Stereotypes -------------------- */

    /**
     * From standard UML, only used, not defined in this profile!
     */
    public static final String STEREOTYPE_METACLASS = "metaclass"; 
    
    /**
     * Defines the <code>metafacade</code> stereotype. A metafacade is a 
     * facade around a {@link #STEREOTYPE_METACLASS}.
     */
    public static final String STEREOTYPE_METAFACADE = "metafacade";

}