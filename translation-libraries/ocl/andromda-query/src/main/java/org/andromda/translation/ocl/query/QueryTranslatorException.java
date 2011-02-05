package org.andromda.translation.ocl.query;

import org.andromda.core.translation.TranslatorException;

/**
 * Any unchecked exception that will be thrown during QueryTranslator processing.
 *
 * @author Chad Brandon
 */
public class QueryTranslatorException
        extends TranslatorException
{
    private static final long serialVersionUID = 34L;
    /**
     * Constructs a QueryTranslatorException
     *
     * @param th
     */
    public QueryTranslatorException(Throwable th)
    {
        super(th);
    }

    /**
     * Constructs a QueryTranslatorException
     *
     * @param msg
     */
    public QueryTranslatorException(String msg)
    {
        super(msg);
    }

    /**
     * Constructs a QueryTranslatorException
     *
     * @param msg
     * @param th
     */
    public QueryTranslatorException(String msg, Throwable th)
    {
        super(msg, th);
    }

}
