package org.andromda.cartridges.bpm4jsf;

/**
 * Stores globals for the BPM4JSF cartridge metafacades.
 * 
 * @author Chad Brandon
 */
public class BPM4JSFGlobals
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
     * Represents a form action type.
     */
    public static final String ACTION_TYPE_FORM = "form";
}
