package org.andromda.cartridges.meta;

import org.andromda.core.common.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The Meta profile. Contains the profile information (tagged values, and stereotypes) for the Meta cartridge.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class MetaProfile
    extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */

    /**
     * From standard UML, only used, not defined in this profile!
     */
    public static final String STEREOTYPE_METACLASS = profile.get("METACLASS");

    /**
     * Defines the <code>metafacade</code> stereotype. A metafacade is a facade around a {@link #STEREOTYPE_METACLASS}.
     */
    public static final String STEREOTYPE_METAFACADE = profile.get("METAFACADE");

    /* ---------------- Tagged Values ------------------ */

    /**
     * Defines the precedence for generalizations when using multiple inheritance.
     */
    public static final String TAGGEDVALUE_GENERALIZATION_PRECEDENCE = profile.get("GENERALIZATION_PRECEDENCE");
}