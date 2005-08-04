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
    public static final String TAGGEDVALUE_ACTION_FORM_SCOPE = profile.get("ACTION_FORM_SCOPE");
    public static final String TAGGEDVALUE_ACTION_TABLELINK = profile.get("ACTION_TABLELINK");
    public static final String TAGGEDVALUE_ACTION_TYPE = profile.get("ACTION_TYPE");
    public static final String TAGGEDVALUE_ACTION_RESETTABLE = profile.get("ACTION_RESETTABLE");
    public static final String TAGGEDVALUE_INPUT_TYPE = profile.get("INPUT_TYPE");
    public static final String TAGGEDVALUE_INPUT_TABLELINK = profile.get("INPUT_TABLELINK");
    public static final String TAGGEDVALUE_TABLE_COLUMNS = profile.get("TABLE_COLUMNS");
    public static final String TAGGEDVALUE_ACTION_REDIRECT = profile.get("ACTION_REDIRECT");
    public static final String TAGGEDVALUE_INPUT_REQUIRED = profile.get("INPUT_REQUIRED");
    public static final String TAGGEDVALUE_INPUT_READONLY = profile.get("INPUT_READONLY");
    public static final String TAGGEDVALUE_INPUT_FORMAT = profile.get("INPUT_FORMAT");

}