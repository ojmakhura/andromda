package org.andromda.cartridges.meta;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Meta profile. Contains the profile information 
 * (tagged values, and stereotypes) for the Meta cartridge.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class MetaProfile extends UMLProfile
{
    /* ----------------- Stereotypes -------------------- */

    public static final String STEREOTYPE_METACLASS  = "metaclass"; // from standard UML, only used, not defined in this profile!
    public static final String STEREOTYPE_METAFACADE = "metafacade";

    /* ----------------- Tagged Values -------------------- */

    public static final String TAGGEDVALUE_METAFACADE_INTERFACEPACKAGE = "@andromda.metafacade.interfacepackage";
    public static final String TAGGEDVALUE_METAFACADE_BASECLASS        = "@andromda.metafacade.baseclassname";

}
