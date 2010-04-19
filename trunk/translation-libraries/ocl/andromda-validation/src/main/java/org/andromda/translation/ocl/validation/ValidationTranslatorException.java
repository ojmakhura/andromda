package org.andromda.translation.ocl.validation;

import org.andromda.core.translation.TranslatorException;

/**
 * Any unchecked exception that will be thrown during ValidationTranslator processing.
 *
 * @author Chad Brandon
 */
public class ValidationTranslatorException
        extends TranslatorException
{

    /**
     * Constructs a ValidationTranslatorException
     *
     * @param th
     */
    public ValidationTranslatorException(Throwable th)
    {
        super(th);
    }

    /**
     * Constructs a ValidationTranslatorException
     *
     * @param msg
     */
    public ValidationTranslatorException(String msg)
    {
        super(msg);
    }

    /**
     * Constructs a ValidationTranslatorException
     *
     * @param msg
     * @param th
     */
    public ValidationTranslatorException(String msg, Throwable th)
    {
        super(msg, th);
    }

}
