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
    /** FRONT_END_REGISTRATION */
    public static final String STEREOTYPE_FRONT_END_REGISTRATION = profile.get("FRONT_END_REGISTRATION");
    /** FRONT_END_NAVIGATION */
    public static final String STEREOTYPE_FRONT_END_NAVIGATION = profile.get("FRONT_END_NAVIGATION");

    /* ----------------- Tagged Values -------------------- */
    /** ACTION_FORM_SCOPE */
    public static final String TAGGEDVALUE_ACTION_FORM_SCOPE = profile.get("ACTION_FORM_SCOPE");
    /** ACTION_FORM_KEY */
    public static final String TAGGEDVALUE_ACTION_FORM_KEY = profile.get("ACTION_FORM_KEY");
    /** ACTION_TABLELINK */
    public static final String TAGGEDVALUE_ACTION_TABLELINK = profile.get("ACTION_TABLELINK");
    /** ACTION_TYPE */
    public static final String TAGGEDVALUE_ACTION_TYPE = profile.get("ACTION_TYPE");
    /** ACTION_RESETTABLE */
    public static final String TAGGEDVALUE_ACTION_RESETTABLE = profile.get("ACTION_RESETTABLE");
    /** INPUT_TYPE */
    public static final String TAGGEDVALUE_INPUT_TYPE = profile.get("INPUT_TYPE");
    /** INPUT_TABLELINK */
    public static final String TAGGEDVALUE_INPUT_TABLELINK = profile.get("INPUT_TABLELINK");
    /** EXCEPTION_TYPE */
    public static final String TAGGEDVALUE_EXCEPTION_TYPE = profile.get("EXCEPTION_TYPE");
    /** ACTION_REDIRECT */
    public static final String TAGGEDVALUE_ACTION_REDIRECT = profile.get("ACTION_REDIRECT");
    /** INPUT_REQUIRED */
    public static final String TAGGEDVALUE_INPUT_REQUIRED = profile.get("INPUT_REQUIRED");
    /** INPUT_READONLY */
    public static final String TAGGEDVALUE_INPUT_READONLY = profile.get("INPUT_READONLY");
    /** INPUT_VALIDWHEN */
    public static final String TAGGEDVALUE_INPUT_VALIDWHEN = profile.get("INPUT_VALIDWHEN");
    /** INPUT_EQUAL */
    public static final String TAGGEDVALUE_INPUT_EQUAL = profile.get("INPUT_EQUAL");
    /** INPUT_TABLE_IDENTIFIER_COLUMNS */
    public static final String TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS = profile.get("INPUT_TABLE_IDENTIFIER_COLUMNS");
    /** INPUT_VALIDATORS */
    public static final String TAGGEDVALUE_INPUT_VALIDATORS = profile.get("INPUT_VALIDATORS");
    /** INPUT_FORMAT */
    public static final String TAGGEDVALUE_INPUT_FORMAT = profile.get("INPUT_FORMAT");
    /** INPUT_RESET */
    public static final String TAGGEDVALUE_INPUT_RESET = profile.get("INPUT_RESET");
    /** TABLE_PAGEABLE */
    public static final String TAGGEDVALUE_TABLE_PAGEABLE = profile.get("TABLE_PAGEABLE");
    /** VIEW_TYPE */
    public static final String TAGGEDVALUE_VIEW_TYPE = profile.get("VIEW_TYPE");
    /** ACTION_FORM_RESET */
    public static final String TAGGEDVALUE_ACTION_FORM_RESET = profile.get("ACTION_FORM_RESET");
    /** ACTION_SUCCESS_MESSAGE */
    public static final String TAGGEDVALUE_ACTION_SUCCESS_MESSAGE = profile.get("ACTION_SUCCESS_MESSAGE");
    /** ACTION_WARNING_MESSAGE */
    public static final String TAGGEDVALUE_ACTION_WARNING_MESSAGE = profile.get("ACTION_WARNING_MESSAGE");
    /** INPUT_COLUMN_COUNT */
    public static final String TAGGEDVALUE_INPUT_COLUMN_COUNT = profile.get("INPUT_COLUMN_COUNT");
    /** INPUT_ROW_COUNT */
    public static final String TAGGEDVALUE_INPUT_ROW_COUNT = profile.get("INPUT_ROW_COUNT");
    /** TABLE_COLUMNS */
    public static final String TAGGEDVALUE_TABLE_COLUMNS = profile.get("TABLE_COLUMNS");
    /** TABLE_MAXROWS */
    public static final String TAGGEDVALUE_TABLE_MAXROWS = profile.get("TABLE_MAXROWS");
    /** TABLE_EXPORT */
    public static final String TAGGEDVALUE_TABLE_EXPORT = profile.get("TABLE_EXPORT");
    /** TABLE_SORTABLE */
    public static final String TAGGEDVALUE_TABLE_SORTABLE = profile.get("TABLE_SORTABLE");

    /* ----------------- Data Types -------------------- */
    /** CHARACTER_TYPE */
    public static final String CHARACTER_TYPE_NAME = profile.get("CHARACTER_TYPE");
    /** BYTE_TYPE */
    public static final String BYTE_TYPE_NAME = profile.get("BYTE_TYPE");
    /** SHORT_TYPE */
    public static final String SHORT_TYPE_NAME = profile.get("SHORT_TYPE");
    /** INTEGER_TYPE */
    public static final String INTEGER_TYPE_NAME = profile.get("INTEGER_TYPE");
    /** LONG_TYPE */
    public static final String LONG_TYPE_NAME = profile.get("LONG_TYPE");
    /** FLOAT_TYPE */
    public static final String FLOAT_TYPE_NAME = profile.get("FLOAT_TYPE");
    /** DOUBLE_TYPE */
    public static final String DOUBLE_TYPE_NAME = profile.get("DOUBLE_TYPE");
    /** URL_TYPE */
    public static final String URL_TYPE_NAME = profile.get("URL_TYPE");
    /** TIME_TYPE */
    public static final String TIME_TYPE_NAME = profile.get("TIME_TYPE");

    /* ----------------- Default Values ----------------- */
    /** hidden */
    public static final String TAGGEDVALUE_INPUT_TYPE_HIDDEN = "hidden";
    /** text */
    public static final String TAGGEDVALUE_INPUT_TYPE_TEXT = "text";
    /** true */
    public static final boolean TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE = true;
    /** true */
    public static final boolean TAGGEDVALUE_TABLE_EXPORTABLE_DEFAULT_VALUE = true;
    /** 15 */
    public static final int TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT = 15;
}