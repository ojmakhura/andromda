package org.andromda.cartridges.bpm4struts;

import org.andromda.core.profile.Profile;



/**
 * Contains the BPM4Struts profile.
 *
 * @author Wouter Zoons
 */
public final class Bpm4StrutsProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile PROFILE = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    /** FRONT_END_EXCEPTION */
    public static final String STEREOTYPE_EXCEPTION = PROFILE.get("FRONT_END_EXCEPTION");

    /* ----------------- Tagged Values -------------------- */
    /** ACTION_TYPE */
    public static final String TAGGEDVALUE_ACTION_TYPE = PROFILE.get("ACTION_TYPE");
    /** ACTION_RESETTABLE */
    public static final String TAGGEDVALUE_ACTION_RESETTABLE = PROFILE.get("ACTION_RESETTABLE");
    /** ACTION_SUCCESS_MESSAGE */
    public static final String TAGGEDVALUE_ACTION_SUCCESS_MESSAGE = PROFILE.get("ACTION_SUCCESS_MESSAGE");
    /** ACTION_WARNING_MESSAGE */
    public static final String TAGGEDVALUE_ACTION_WARNING_MESSAGE = PROFILE.get("ACTION_WARNING_MESSAGE");
    /** ACTION_FORM_SCOPE */
    public static final String TAGGEDVALUE_ACTION_FORM_SCOPE = PROFILE.get("ACTION_FORM_SCOPE");
    /** ACTION_FORM_KEY */
    public static final String TAGGEDVALUE_ACTION_FORM_KEY = PROFILE.get("ACTION_FORM_KEY");
    /** ACTION_TABLELINK */
    public static final String TAGGEDVALUE_ACTION_TABLELINK = PROFILE.get("ACTION_TABLELINK");
    /** INPUT_COLUMN_COUNT */
    public static final String TAGGEDVALUE_INPUT_COLUMN_COUNT = PROFILE.get("INPUT_COLUMN_COUNT");
    /** INPUT_ROW_COUNT */
    public static final String TAGGEDVALUE_INPUT_ROW_COUNT = PROFILE.get("INPUT_ROW_COUNT");
    /** INPUT_REQUIRED */
    public static final String TAGGEDVALUE_INPUT_REQUIRED = PROFILE.get("INPUT_REQUIRED");
    /** INPUT_READONLY */
    public static final String TAGGEDVALUE_INPUT_READONLY = PROFILE.get("INPUT_READONLY");
    /** INPUT_FORMAT */
    public static final String TAGGEDVALUE_INPUT_FORMAT = PROFILE.get("INPUT_FORMAT");
    /** INPUT_TYPE */
    public static final String TAGGEDVALUE_INPUT_TYPE = PROFILE.get("INPUT_TYPE");
    /** INPUT_MULTIBOX */
    public static final String TAGGEDVALUE_INPUT_MULTIBOX = PROFILE.get("INPUT_MULTIBOX");
    /** INPUT_RADIO */
    public static final String TAGGEDVALUE_INPUT_RADIO = PROFILE.get("INPUT_RADIO");
    /** INPUT_VALIDWHEN */
    public static final String TAGGEDVALUE_INPUT_VALIDWHEN = PROFILE.get("INPUT_VALIDWHEN");
    /** INPUT_VALIDATORS */
    public static final String TAGGEDVALUE_INPUT_VALIDATORS = PROFILE.get("INPUT_VALIDATORS");
    /** INPUT_CALENDAR */
    public static final String TAGGEDVALUE_INPUT_CALENDAR = PROFILE.get("INPUT_CALENDAR");
    /** INPUT_RESET */
    public static final String TAGGEDVALUE_INPUT_RESET = PROFILE.get("INPUT_RESET");
    /** TABLE_COLUMNS */
    public static final String TAGGEDVALUE_TABLE_COLUMNS = PROFILE.get("TABLE_COLUMNS");
    /** TABLE_MAXROWS */
    public static final String TAGGEDVALUE_TABLE_MAXROWS = PROFILE.get("TABLE_MAXROWS");
    /** TABLE_EXPORT */
    public static final String TAGGEDVALUE_TABLE_EXPORT = PROFILE.get("TABLE_EXPORT");
    /** TABLE_SORTABLE */
    public static final String TAGGEDVALUE_TABLE_SORTABLE = PROFILE.get("TABLE_SORTABLE");
    /** TABLE_DECORATOR */
    public static final String TAGGEDVALUE_TABLE_DECORATOR = PROFILE.get("TABLE_DECORATOR");
    /** EXCEPTION_TYPE */
    public static final String TAGGEDVALUE_EXCEPTION_TYPE = PROFILE.get("EXCEPTION_TYPE");
    /** ACTION_REDIRECT */
    public static final String TAGGEDVALUE_ACTION_REDIRECT = PROFILE.get("ACTION_REDIRECT");

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

    /* ----------------- Default Values ------------------- */
    /** true */
    public static final String TAGGEDVALUE_INPUT_DEFAULT_REQUIRED = "true";
    /** java.lang.Exception */
    public static final String TAGGEDVALUE_EXCEPTION_DEFAULT_TYPE = "java.lang.Exception";
    /** form */
    public static final String TAGGEDVALUE_ACTION_FORM_DEFAULT_KEY = "form";
    /** hyperlink */
    public static final String TAGGEDVALUE_ACTION_TYPE_HYPERLINK = "hyperlink";
    /** form */
    public static final String TAGGEDVALUE_ACTION_TYPE_FORM = "form";
    /** image */
    public static final String TAGGEDVALUE_ACTION_TYPE_IMAGE = "image";
    /** table */
    public static final String TAGGEDVALUE_ACTION_TYPE_TABLE = "table";
    /** TAGGEDVALUE_ACTION_TYPE_FORM */
    public static final String TAGGEDVALUE_ACTION_DEFAULT_TYPE = TAGGEDVALUE_ACTION_TYPE_FORM;
    /** text */
    public static final String TAGGEDVALUE_INPUT_TYPE_TEXT = "text";
    /** plaintext */
    public static final String TAGGEDVALUE_INPUT_TYPE_PLAINTEXT = "plaintext";
    /** textarea */
    public static final String TAGGEDVALUE_INPUT_TYPE_TEXTAREA = "textarea";
    /** radio */
    public static final String TAGGEDVALUE_INPUT_TYPE_RADIO = "radio";
    /** checkbox */
    public static final String TAGGEDVALUE_INPUT_TYPE_CHECKBOX = "checkbox";
    /** hidden */
    public static final String TAGGEDVALUE_INPUT_TYPE_HIDDEN = "hidden";
    /** select */
    public static final String TAGGEDVALUE_INPUT_TYPE_SELECT = "select";
    /** password */
    public static final String TAGGEDVALUE_INPUT_TYPE_PASSWORD = "password";
    /** multibox */
    public static final String TAGGEDVALUE_INPUT_TYPE_MULTIBOX = "multibox";
    /** link */
    public static final String TAGGEDVALUE_INPUT_TYPE_LINK = "link";
    /** file */
    public static final String TAGGEDVALUE_INPUT_TYPE_FILE = "file";
    /** 3 */
    public static final int TAGGEDVALUE_INPUT_TYPE_OPTION_DEFAULT_COUNT = 3;
    /** 15 */
    public static final int TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT = 15;
    /** true */
    public static final boolean TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE = true;
    /** true */
    public static final boolean TAGGEDVALUE_TABLE_EXPORTABLE_DEFAULT_VALUE = true;

    private Bpm4StrutsProfile()
    {
    }
}
