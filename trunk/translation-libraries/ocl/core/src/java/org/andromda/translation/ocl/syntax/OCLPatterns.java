package org.andromda.translation.ocl.syntax;

import org.andromda.core.translation.TranslationUtils;
import org.apache.log4j.Logger;

/**
 * Contains the patterns matching constructs within the OCL language.
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
    public static final String NAVIGATIONAL_PATH = "(\\w+|\\s*\\.\\s*)+";

    /**
     * Indicates if this <code>expression</code> is an operation.
     * 
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isOperation(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(OPERATION);
    }

    private static final String OPERATION = ".+\\(.*\\).*";

    /**
     * Indicates if this <code>expression</code> is a collection (->)
     * operation call.
     * 
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isCollectionOperationCall(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(COLLECTION_CALL);
    }

    private static final String COLLECTION_CALL = ".+->.+";

    private static final Logger logger = Logger.getLogger(OCLPatterns.class);
    
    /**
     * Indicates if this <code>expression</code> is a collection operation
     * result navigational path (->someOperation().some.path)
     * 
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isCollectionOperationResultNavigationalPath(
        Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(
            COLLECTION_CALL_RESULT_NAVIGATIONAL_PATH);
    }

    private static final String COLLECTION_CALL_RESULT_NAVIGATIONAL_PATH = ".+->"
        + OPERATION + NAVIGATIONAL_PATH;
}
