package org.andromda.translation.ocl.syntax;

import org.andromda.core.translation.TranslationUtils;

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
    static final String SCOPE_PATH = "[\\w+|::]+";

    /**
     * Matches on the pattern of a navigational path (i.e. person.name.first)
     */
    static final String NAVIGATIONAL_PATH = "[\\w*[\\.]*]+";

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
     * Indicates if this <code>expression</code> is a collection (->) operation call.
     *
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isCollectionOperationCall(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(COLLECTION_CALL);
    }

    private static final String COLLECTION_CALL = ".+->.+";

    /**
     * Indicates if this <code>expression</code> is a collection operation result navigational path
     * (->someOperation().some.path)
     *
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isCollectionOperationResultNavigationalPath(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(COLLECTION_CALL_RESULT_NAVIGATIONAL_PATH);
    }
    
    /**
     * Indicates if this <code>expression</code> is a navigational path
     * (some.path)
     *
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isNavigationalPath(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(NAVIGATIONAL_PATH);
    }

    private static final String COLLECTION_CALL_RESULT_NAVIGATIONAL_PATH = ".+->" + OPERATION + NAVIGATIONAL_PATH;
    
    /**
     * Pattern for <em>and</em> and <em>or</em> expression matching.
     */
    private static final String AND_OR_OR_OPERATOR = "or\\s+.*|and\\s+.*";
    
    /**
     * Indicates if this <code>expression</code> is an <em>and</em>
     * or <em>or</em> expression (or some.path, and some.path, etc)
     *
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isAndOrOrExpression(Object expression)
    {
        return TranslationUtils.trimToEmpty(expression).matches(AND_OR_OR_OPERATOR);
    }
}
