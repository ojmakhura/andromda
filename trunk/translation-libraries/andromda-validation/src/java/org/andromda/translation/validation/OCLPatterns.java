package org.andromda.translation.validation;

/**
 * Contains the patterns matching constructs within the OCL
 * language.
 * 
 * @author Chad Brandon
 */
public class OCLPatterns
{
    /**
     * Matches on the pattern of a scope path (i.e. java :: lang :: Integer).
     */
    public static final String SCOPE_PATH = "(\\w+|\\s*::\\s*)*";
    
    /**
     * Matches on the pattern of a navigational path (i.e. person.name.first)
     */
    public static final String NAVIGATIONAL_PATH = "(\\w+|\\s*\\.\\s*)*";
    
    /**
     * Matches on operations (i.e. oclKindOf
     */
    private static final String OPERATION = ".*\\(.*\\).*";
    
    /**
     * Indicates if this <code>expression</code> is an operation.
     * 
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isOperation(Object expression)
    {
        return String.valueOf(expression).matches(OPERATION);
    }
}
