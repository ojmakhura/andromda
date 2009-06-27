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
     * The default number of columns to render for input fields.
     */
    public static final String PROPERTY_DEFAULT_INPUT_COLUMN_COUNT = "defaultInputColumnCount";

    /**
     * The default number of rows to render for textarea fields.
     */
    public static final String PROPERTY_DEFAULT_INPUT_ROW_COUNT = "defaultInputRowCount";

    /**
     * Stores the default date format when dates are formatted.
     */
    public static final String PROPERTY_DEFAULT_DATEFORMAT = "defaultDateFormat";

    /**
     * Stores the default time format when times are formatted.
     */
    public static final String PROPERTY_DEFAULT_TIMEFORMAT = "defaultTimeFormat";

    /**
     * Are date and time format to be strictly applied to the entered input ?
     */
    public static final String PROPERTY_STRICT_DATETIMEFORMAT = "strictDateTimeFormat";

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

    /**
     * Denotes the way application resource messages ought to be generated.
     * When messages are normalized it means that elements with the same name
     * will reuse the same label, even if they are located in entirely different
     * use-cases or pages.
     * <p>
     * This results in resource bundles that are not only smaller in size but
     * also more straightforward to translate. The downside is that it will be
     * less evident to customize labels for certain fields (which is rarely the
     * case anyway).
     * <p>
     * For backward compatibility reasons this flag is disabled by default.
     */
    public static final String PROPERTY_NORMALIZE_MESSAGES = "normalizeMessages";

    /**
     * Instructs the cartridge not to include hidden fields in the validation process.
     */
    public static final String DISABLE_VALIDATION_FOR_HIDDEN_FORM_FIELDS = "disableValidationForHiddenFormFields";

    /**
     * The suffix for form names.
     */
    public static final String FORM_SUFFIX = "Form";

    /**
     * The suffix for form implementation names.
     */
    public static final String FORM_IMPLEMENTATION_SUFFIX = FORM_SUFFIX + "Impl";

    /**
     * The namespace property used to identify the pattern used to construct the backend service's package name.
     */
    public static final String SERVICE_PACKAGE_NAME_PATTERN = "servicePackageNamePattern";

    /**
     * The namespace property used to identify the pattern used to construct the backend service's accessor.
     */
    public static final String SERVICE_ACCESSOR_PATTERN = "serviceAccessorPattern";
}