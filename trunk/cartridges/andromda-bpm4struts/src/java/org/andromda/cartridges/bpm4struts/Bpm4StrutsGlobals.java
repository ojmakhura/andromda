package org.andromda.cartridges.bpm4struts;

/**
 * Stores the BPM4Struts Global variables.
 *
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class Bpm4StrutsGlobals
{
    /**
     * Stores the default date format when dates are formatted.
     */
    public static final String PROPERTY_DEFAULT_DATEFORMAT = "defaultDateFormat";

    /**
     * Contains the default value for whether or not all forwards should perform a HTTP redirect or not.
     */
    public static final String PROPERTY_DEFAULT_ACTION_REDIRECT = "defaultActionRedirect";

    /**
     * The namespace property used to identify the action path prefix, which may very well be empty.
     */
    public static final String PROPERTY_ACTION_PATH_PREFIX = "actionPathPrefix";

    /**
     * The namespace property used to toggle the automatic generation of table decorators for displaytag.
     */
    public static final String PROPERTY_GENERATE_TABLE_DECORATORS = "generateTableDecorators";

    /**
     * The suffix used to append to decorator class names.
     */
    public static final String PROPERTY_TABLE_DECORATOR_SUFFIX = "tableDecoratorSuffix";

    /**
     * A space-separated list of types to which displaytag table are to be exported by default.
     */
    public static final String PROPERTY_DEFAULT_TABLE_EXPORT_TYPES = "defaultTableExportTypes";

    /**
     * Stores the scope of the "form" attribute when executing an action.
     */
    public static final String PROPERTY_ACTION_FORM_SCOPE = "actionFormScope";
}
