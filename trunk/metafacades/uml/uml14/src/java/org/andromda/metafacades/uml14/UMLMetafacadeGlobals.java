package org.andromda.metafacades.uml14;

/**
 * Stores globals for the UML 1.4 metafacades.
 * 
 * @author Chad Brandon
 */
public class UMLMetafacadeGlobals
{
    /**
     * The seperator used for package seperation in the generated metafacades.
     */
    public static final char PACKAGE_SEPERATOR = '.';
    
    /**
     * Prefix used to match constraint kinds.
     */
    public static final String CONSTRAINT_KIND_PREFIX_PATTERN = ".*\\s*";

    /**
     * Suffix used to match constraint kinds.
     */
    public static final String CONSTRAINT_KIND_SUFFIX_PATTERN = "\\s*.*:.*";
}