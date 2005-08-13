package org.andromda.cartridges.bpm4struts;

import org.andromda.core.profile.Profile;


/**
 * Contains the BPM4Struts profile.
 *
 * @author Wouter Zoons
 */
public class Bpm4StrutsProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    public static final String STEREOTYPE_EXCEPTION = profile.get("FRONT_END_EXCEPTION");

    /* ----------------- Tagged Values -------------------- */
    public static final String TAGGEDVALUE_ACTION_TYPE = profile.get("ACTION_TYPE");
    public static final String TAGGEDVALUE_ACTION_RESETTABLE = profile.get("ACTION_RESETTABLE");
    public static final String TAGGEDVALUE_ACTION_SUCCESS_MESSAGE = profile.get("ACTION_SUCCESS_MESSAGE");
    public static final String TAGGEDVALUE_ACTION_WARNING_MESSAGE = profile.get("ACTION_WARNING_MESSAGE");
    public static final String TAGGEDVALUE_ACTION_FORM_SCOPE = profile.get("ACTION_FORM_SCOPE");
    public static final String TAGGEDVALUE_ACTION_FORM_KEY = profile.get("ACTION_FORM_KEY");
    public static final String TAGGEDVALUE_ACTION_TABLELINK = profile.get("ACTION_TABLELINK");
    public static final String TAGGEDVALUE_INPUT_COLUMN_COUNT = profile.get("INPUT_COLUMN_COUNT");
    public static final String TAGGEDVALUE_INPUT_ROW_COUNT = profile.get("INPUT_ROW_COUNT");
    public static final String TAGGEDVALUE_INPUT_REQUIRED = profile.get("INPUT_REQUIRED");
    public static final String TAGGEDVALUE_INPUT_READONLY = profile.get("INPUT_READONLY");
    public static final String TAGGEDVALUE_INPUT_FORMAT = profile.get("INPUT_FORMAT");
    public static final String TAGGEDVALUE_INPUT_TYPE = profile.get("INPUT_TYPE");
    public static final String TAGGEDVALUE_INPUT_MULTIBOX = profile.get("INPUT_MULTIBOX");
    public static final String TAGGEDVALUE_INPUT_RADIO = profile.get("INPUT_RADIO");
    public static final String TAGGEDVALUE_INPUT_VALIDWHEN = profile.get("INPUT_VALIDWHEN");
    public static final String TAGGEDVALUE_INPUT_VALIDATORS = profile.get("INPUT_VALIDATORS");
    public static final String TAGGEDVALUE_INPUT_CALENDAR = profile.get("INPUT_CALENDAR");
    public static final String TAGGEDVALUE_INPUT_RESET = profile.get("INPUT_RESET");
    public static final String TAGGEDVALUE_TABLE_COLUMNS = profile.get("TABLE_COLUMNS");
    public static final String TAGGEDVALUE_TABLE_MAXROWS = profile.get("TABLE_MAXROWS");
    public static final String TAGGEDVALUE_TABLE_EXPORT = profile.get("TABLE_EXPORT");
    public static final String TAGGEDVALUE_TABLE_SORTABLE = profile.get("TABLE_SORTABLE");
    public static final String TAGGEDVALUE_TABLE_DECORATOR = profile.get("TABLE_DECORATOR");
    public static final String TAGGEDVALUE_EXCEPTION_TYPE = profile.get("EXCEPTION_TYPE");
    public static final String TAGGEDVALUE_ACTION_REDIRECT = profile.get("ACTION_REDIRECT");

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

    /* ----------------- Default Values ------------------- */
    public static final String TAGGEDVALUE_INPUT_DEFAULT_REQUIRED = "true";
    public static final String TAGGEDVALUE_EXCEPTION_DEFAULT_TYPE = "java.lang.Exception";
    public static final String TAGGEDVALUE_ACTION_FORM_DEFAULT_KEY = "form";
    public static final String TAGGEDVALUE_ACTION_TYPE_HYPERLINK = "hyperlink";
    public static final String TAGGEDVALUE_ACTION_TYPE_FORM = "form";
    public static final String TAGGEDVALUE_ACTION_TYPE_IMAGE = "image";
    public static final String TAGGEDVALUE_ACTION_TYPE_TABLE = "table";
    public static final String TAGGEDVALUE_ACTION_DEFAULT_TYPE = TAGGEDVALUE_ACTION_TYPE_FORM;
    public static final String TAGGEDVALUE_INPUT_TYPE_TEXT = "text";
    public static final String TAGGEDVALUE_INPUT_TYPE_PLAINTEXT = "plaintext";
    public static final String TAGGEDVALUE_INPUT_TYPE_TEXTAREA = "textarea";
    public static final String TAGGEDVALUE_INPUT_TYPE_RADIO = "radio";
    public static final String TAGGEDVALUE_INPUT_TYPE_CHECKBOX = "checkbox";
    public static final String TAGGEDVALUE_INPUT_TYPE_HIDDEN = "hidden";
    public static final String TAGGEDVALUE_INPUT_TYPE_SELECT = "select";
    public static final String TAGGEDVALUE_INPUT_TYPE_PASSWORD = "password";
    public static final String TAGGEDVALUE_INPUT_TYPE_MULTIBOX = "multibox";
    public static final String TAGGEDVALUE_INPUT_TYPE_LINK = "link";
    public static final String TAGGEDVALUE_INPUT_TYPE_FILE = "file";
    public static final int TAGGEDVALUE_INPUT_TYPE_OPTION_DEFAULT_COUNT = 3;
    public static final int TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT = 15;
    public static final boolean TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE = true;
    public static final boolean TAGGEDVALUE_TABLE_EXPORTABLE_DEFAULT_VALUE = true;

    private Bpm4StrutsProfile()
    {
    }
}