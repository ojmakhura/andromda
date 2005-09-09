package org.andromda.cartridges.jsf;

/**
 * Stores globals for the JSF cartridge metafacades.
 * 
 * @author Chad Brandon
 */
public class JSFGlobals
{
    
    /**
     * Denotes the way application resource messages ought to be generated.
     * When messages are normalized it means that elements with the same name
     * will reuse the same label, even if they are located in entirely different
     * use-cases or pages.
     * <p/>
     * This results in resource bundles that are not only smaller in size but
     * also more straightforward to translate. The downside is that it will be
     * less evident to customize labels for certain fields (which is rarely the
     * case anyway).
     * <p/>
     */
    public static final String NORMALIZE_MESSAGES = "normalizeMessages";
    
    /**
     * The pattern for constructing the form name.
     */
    public static final String FORM_PATTERN = "formPattern";
    
    /**
     * Contains the default value for whether or not all forwards should perform a HTTP redirect or not.
     */
    public static final String DEFAULT_ACTION_REDIRECT = "defaultActionRedirect";
    
    /**
     * The pattern for constructing the form implementation name.
     */
    public static final String FORM_IMPLEMENTATION_PATTERN = "formImplementationPattern";
    
    /**
     * The pattern for constructing the bean name under which the form is stored.
     */
    public static final String FORM_BEAN_PATTERN = "formBeanPattern";
    
    /**
     * Stores the default form scope which can be overriden with a tagged value.
     */
    public static final String FORM_SCOPE = "formScope";
    
    /**
     * Stores the pattern used for constructing the controller implementation name.
     */
    public static final String CONTROLLER_IMPLEMENTATION_PATTERN = "controllerImplementationPattern";
    
    /**
     * The suffix given to title message keys.
     */
    public static final String TITLE_MESSAGE_KEY_SUFFIX = "title";
    
    /**
     * The suffix given to the documentation message keys.
     */
    public static final String DOCUMENTATION_MESSAGE_KEY_SUFFIX = "documentation";
    
    /**
     * The namespace property used to identify the pattern used to construct the backend service's accessor.
     */
    public static final String SERVICE_ACCESSOR_PATTERN = "serviceAccessorPattern";
    
    /**
     * The namespace property used to identify the pattern used to construct the backend service's package name.
     */
    public static final String SERVICE_PACKAGE_NAME_PATTERN = "servicePackageNamePattern";
    
    /**
     * Represents a hyperlink action type.
     */
    public static final String ACTION_TYPE_HYPERLINK = "hyperlink";
    
    /**
     * Represents a popup action type.
     */
    public static final String VIEW_TYPE_POPUP = "popup";
    
    /**
     * Represents a form action type.
     */
    public static final String ACTION_TYPE_FORM = "form";
    
    /**
     * Represents a table action type.
     */
    public static final String ACTION_TYPE_TABLE = "table";
    
    /**
     * Represents an image action type.
     */
    public static final String ACTION_TYPE_IMAGE = "image";
    
    /**
     * Stores the default date format when dates are formatted.
     */
    public static final String PROPERTY_DEFAULT_DATEFORMAT = "defaultDateFormat";

    /**
     * Stores the default time format when times are formatted.
     */
    public static final String PROPERTY_DEFAULT_TIMEFORMAT = "defaultTimeFormat";
    
    /**
     * The default key under which the action form is stored.
     */
    public static final String ACTION_FORM_KEY = "actionFormKey";
    
    /**
     * The pattern used for constructing the name of the filter that performs view form population.
     */
    public static final String VIEW_POPULATOR_PATTERN = "viewPopulatorPattern";
    
    /**
     * The pattern used for constructing a parameter's backing list name.  A backing list
     * is used when you want to select the value of the parameter from a list (typically 
     * used for drop-down select input types).
     */
    public static final String BACKING_LIST_PATTERN = "backingListPattern";
    
    /**
     * The pattern used for constructing the label list name (stores the list
     * of possible parameter value labels).
     */
    public static final String LABEL_LIST_PATTERN = "labelListPattern";
    
    /**
     * The pattern used for constructing the values list name (stores the list of
     * possible parameter values when selecting from a list).
     */
    public static final String VALUE_LIST_PATTERN = "valueListPattern";
    
    /**
     * The item count for dummy arrays.
     */
    public static final int DUMMY_ARRAY_COUNT = 5;
}