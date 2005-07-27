package org.andromda.cartridges.bpm4jsf;

import org.andromda.core.profile.Profile;


/**
 * Contains the BPM4Struts profile.
 *
 * @author Chad Brandon
 */
public class BPM4JSFProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    /* ----------------- Tagged Values -------------------- */
    public static final String TAGGEDVALUE_FORM_SCOPE = profile.get("FORM_SCOPE");
}