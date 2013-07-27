package org.andromda.cartridges.jsf2;

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
    private static final Profile PROFILE = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    /** FRONT_END_REGISTRATION */
    public static final String STEREOTYPE_FRONT_END_REGISTRATION = PROFILE.get("FRONT_END_REGISTRATION");
    /** FRONT_END_NAVIGATION */
    public static final String STEREOTYPE_FRONT_END_NAVIGATION = PROFILE.get("FRONT_END_NAVIGATION");
    /** MANAGEABLE_ATTRIBUTE */
    public static final String STEREOTYPE_MANAGEABLE_ATTRIBUTE = PROFILE.get("MANAGEABLE_ATTRIBUTE");

    /* ----------------- Tagged Values -------------------- */
    /** ACTION_FORM_SCOPE */
    public static final String TAGGEDVALUE_ACTION_FORM_SCOPE = PROFILE.get("ACTION_FORM_SCOPE");
    /** ACTION_FORM_KEY */
    public static final String TAGGEDVALUE_ACTION_FORM_KEY = PROFILE.get("ACTION_FORM_KEY");
    /** ACTION_TABLELINK */
    public static final String TAGGEDVALUE_ACTION_TABLELINK = PROFILE.get("ACTION_TABLELINK");
    /** ACTION_TYPE */
    public static final String TAGGEDVALUE_ACTION_TYPE = PROFILE.get("ACTION_TYPE");
    /** ACTION_RESETTABLE */
    public static final String TAGGEDVALUE_ACTION_RESETTABLE = PROFILE.get("ACTION_RESETTABLE");
    /** INPUT_TYPE */
    public static final String TAGGEDVALUE_INPUT_TYPE = PROFILE.get("INPUT_TYPE");
    /** INPUT_TABLELINK */
    public static final String TAGGEDVALUE_INPUT_TABLELINK = PROFILE.get("INPUT_TABLELINK");
    /** EXCEPTION_TYPE */
    public static final String TAGGEDVALUE_EXCEPTION_TYPE = PROFILE.get("EXCEPTION_TYPE");
    /** ACTION_REDIRECT */
    public static final String TAGGEDVALUE_ACTION_REDIRECT = PROFILE.get("ACTION_REDIRECT");
    /** INPUT_REQUIRED */
    public static final String TAGGEDVALUE_INPUT_REQUIRED = PROFILE.get("INPUT_REQUIRED");
    /** INPUT_READONLY */
    public static final String TAGGEDVALUE_INPUT_READONLY = PROFILE.get("INPUT_READONLY");
    /** INPUT_VALIDWHEN */
    public static final String TAGGEDVALUE_INPUT_VALIDWHEN = PROFILE.get("INPUT_VALIDWHEN");
    /** INPUT_EQUAL */
    public static final String TAGGEDVALUE_INPUT_EQUAL = PROFILE.get("INPUT_EQUAL");
    /** INPUT_TABLE_IDENTIFIER_COLUMNS */
    public static final String TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS
        = PROFILE.get("INPUT_TABLE_IDENTIFIER_COLUMNS");
    /** INPUT_VALIDATORS */
    public static final String TAGGEDVALUE_INPUT_VALIDATORS = PROFILE.get("INPUT_VALIDATORS");
    /** INPUT_FORMAT */
    public static final String TAGGEDVALUE_INPUT_FORMAT = PROFILE.get("INPUT_FORMAT");
    /** INPUT_RESET */
    public static final String TAGGEDVALUE_INPUT_RESET = PROFILE.get("INPUT_RESET");
    /** TABLE_PAGEABLE */
    public static final String TAGGEDVALUE_TABLE_PAGEABLE = PROFILE.get("TABLE_PAGEABLE");
    /** VIEW_TYPE */
    public static final String TAGGEDVALUE_VIEW_TYPE = PROFILE.get("VIEW_TYPE");
    /** ACTION_FORM_RESET */
    public static final String TAGGEDVALUE_ACTION_FORM_RESET = PROFILE.get("ACTION_FORM_RESET");
    /** ACTION_SUCCESS_MESSAGE */
    public static final String TAGGEDVALUE_ACTION_SUCCESS_MESSAGE = PROFILE.get("ACTION_SUCCESS_MESSAGE");
    /** ACTION_WARNING_MESSAGE */
    public static final String TAGGEDVALUE_ACTION_WARNING_MESSAGE = PROFILE.get("ACTION_WARNING_MESSAGE");
    /** INPUT_COLUMN_COUNT */
    public static final String TAGGEDVALUE_INPUT_COLUMN_COUNT = PROFILE.get("INPUT_COLUMN_COUNT");
    /** INPUT_ROW_COUNT */
    public static final String TAGGEDVALUE_INPUT_ROW_COUNT = PROFILE.get("INPUT_ROW_COUNT");
    /** TABLE_COLUMNS */
    public static final String TAGGEDVALUE_TABLE_COLUMNS = PROFILE.get("TABLE_COLUMNS");
    /** TABLE_MAXROWS */
    public static final String TAGGEDVALUE_TABLE_MAXROWS = PROFILE.get("TABLE_MAXROWS");
    /** TABLE_EXPORT */
    public static final String TAGGEDVALUE_TABLE_EXPORT = PROFILE.get("TABLE_EXPORT");
    /** TABLE_SORTABLE */
    public static final String TAGGEDVALUE_TABLE_SORTABLE = PROFILE.get("TABLE_SORTABLE");
    
    /** Implementation */
    public static final String ANDROMDA_MANAGEABLE_IMPLEMENTATION = PROFILE.get("MANAGEABLE_IMPLEMENTATION");
    /** SEARCHABLE */
    public static final String ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE = PROFILE.get("ATTRIBUTE_SEARCHABLE");
    /** EDITABLE */
    public static final String ANDROMDA_MANAGEABLE_ATTRIBUTE_IGNORE = PROFILE.get("ATTRIBUTE_IGNORE");
    /** DISPLAY */
    public static final String ANDROMDA_MANAGEABLE_ATTRIBUTE_DISPLAY = PROFILE.get("ATTRIBUTE_DISPLAY");

    /* ----------------- Data Types -------------------- */
    /** CHARACTER_TYPE */
    public static final String CHARACTER_TYPE_NAME = PROFILE.get("CHARACTER_TYPE");
    /** BYTE_TYPE */
    public static final String BYTE_TYPE_NAME = PROFILE.get("BYTE_TYPE");
    /** SHORT_TYPE */
    public static final String SHORT_TYPE_NAME = PROFILE.get("SHORT_TYPE");
    /** INTEGER_TYPE */
    public static final String INTEGER_TYPE_NAME = PROFILE.get("INTEGER_TYPE");
    /** LONG_TYPE */
    public static final String LONG_TYPE_NAME = PROFILE.get("LONG_TYPE");
    /** FLOAT_TYPE */
    public static final String FLOAT_TYPE_NAME = PROFILE.get("FLOAT_TYPE");
    /** DOUBLE_TYPE */
    public static final String DOUBLE_TYPE_NAME = PROFILE.get("DOUBLE_TYPE");
    /** URL_TYPE */
    public static final String URL_TYPE_NAME = PROFILE.get("URL_TYPE");
    /** TIME_TYPE */
    public static final String TIME_TYPE_NAME = PROFILE.get("TIME_TYPE");

    /* ----------------- Default Values ----------------- */
    /** hidden */
    public static final String TAGGEDVALUE_INPUT_TYPE_HIDDEN = "hidden";
    /** text */
    public static final String TAGGEDVALUE_INPUT_TYPE_TEXT = "text";
    /** false */
    public static final boolean TAGGEDVALUE_MANAGEABLE_IMPLEMENTATION_DEFAULT_VALUE = false;
    /** true */
    public static final boolean TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE = true;
    /** true */
    public static final boolean TAGGEDVALUE_TABLE_EXPORTABLE_DEFAULT_VALUE = true;
    /** 15 */
    public static final int TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT = 15;
}
