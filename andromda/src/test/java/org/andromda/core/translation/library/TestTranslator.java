package org.andromda.core.translation.library;

import org.andromda.core.translation.Expression;
import org.andromda.core.translation.Translator;


/**
 * A Test Translator
 *
 * @author Chad Brandon
 */
public class TestTranslator
    implements Translator
{
    /**
     * @see org.andromda.core.translation.Translator#translate(java.lang.String,
     *      java.lang.Object, java.lang.String)
     */
    public Expression translate(
        String translationLibrary,
        String expression,
        Object contextElement)
    {
        return null;
    }
}