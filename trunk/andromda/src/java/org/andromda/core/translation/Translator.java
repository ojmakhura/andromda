package org.andromda.core.translation;


/**
 * Provides expression translation capabilities. Every expression translator must implement this interface.
 *
 * @author Chad Brandon
 */
public interface Translator
{
    /**
     * Translates the expression into a translated Expression instance.
     *
     * @param translationLibrary the library and translation to lookup perform the translation (i.e. sql.Oracle9i -->
     *                           library to use would be "sql" and translation from the sql library would be
     *                           'Oracle9i').
     * @param contextElement     the optional element in the model to which the expression applies (the context element
     *                           of an OCL expression for example).
     * @param expression         the expression (OCL, etc) to translate.
     * @return Expression the expression containing the translated result.
     * @see org.andromda.core.translation.Expression
     */
    public Expression translate(
        String translationLibrary,
        String expression,
        Object contextElement);
}