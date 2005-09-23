package org.andromda.cartridges.jsf;

import org.andromda.core.profile.Profile;


/**
 * Contains the AndroMDA JSF profile.
 *
 * @author Chad Brandon
 */
public class JSFProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    public static final String STEREOTYPE_FRONT_END_REGISTRATION = profile.get("FRONT_END_REGISTRATION");
    
    /* ----------------- Tagged Values -------------------- */
    public static final String TAGGEDVALUE_ACTION_FORM_SCOPE = profile.get("ACTION_FORM_SCOPE");
    public static final String TAGGEDVALUE_ACTION_FORM_KEY = profile.get("ACTION_FORM_KEY");
    public static final String TAGGEDVALUE_ACTION_TABLELINK = profile.get("ACTION_TABLELINK");
    public static final String TAGGEDVALUE_ACTION_TYPE = profile.get("ACTION_TYPE");
    public static final String TAGGEDVALUE_ACTION_RESETTABLE = profile.get("ACTION_RESETTABLE");
    public static final String TAGGEDVALUE_INPUT_TYPE = profile.get("INPUT_TYPE");
    public static final String TAGGEDVALUE_INPUT_TABLELINK = profile.get("INPUT_TABLELINK");
    public static final String TAGGEDVALUE_ACTION_REDIRECT = profile.get("ACTION_REDIRECT");
    public static final String TAGGEDVALUE_INPUT_REQUIRED = profile.get("INPUT_REQUIRED");
    public static final String TAGGEDVALUE_INPUT_READONLY = profile.get("INPUT_READONLY");
    public static final String TAGGEDVALUE_INPUT_VALIDWHEN = profile.get("INPUT_VALIDWHEN");
    public static final String TAGGEDVALUE_INPUT_EQUAL = profile.get("INPUT_EQUAL");
    public static final String TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS = profile.get("INPUT_TABLE_IDENTIFIER_COLUMNS");
    public static final String TAGGEDVALUE_INPUT_VALIDATORS = profile.get("INPUT_VALIDATORS");
    public static final String TAGGEDVALUE_INPUT_FORMAT = profile.get("INPUT_FORMAT");
    public static final String TAGGEDVALUE_INPUT_RESET = profile.get("INPUT_RESET");
    public static final String TAGGEDVALUE_VIEW_TYPE = profile.get("VIEW_TYPE");
    public static final String TAGGEDVALUE_ACTION_FORM_RESET = profile.get("ACTION_FORM_RESET");

    /* ----------------- Data Types -------------------- */
    public static final String CHARACTER_TYPE_NAME = profile.get("CHARACTER_TYPE");
    public static final String BYTE_TYPE_NAME = profile.get("BYTE_TYPE");
    public static final String SHORT_TYPE_NAME = profile.get("SHORT_TYPE");
    public static final String INTEGER_TYPE_NAME = profile.get("INTEGER_TYPE");
    public static final String LONG_TYPE_NAME = profile.get("LONG_TYPE");
    public static final String FLOAT_TYPE_NAME = profile.get("FLOAT_TYPE");
    public static final String DOUBLE_TYPE_NAME = profile.get("DOUBLE_TYPE");
    public static final String URL_TYPE_NAME = profile.get("URL_TYPE");
    public static final String TIME_TYPE_NAME = profile.get("TIME_TYPE");
}