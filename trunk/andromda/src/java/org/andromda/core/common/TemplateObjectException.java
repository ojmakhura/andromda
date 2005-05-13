package org.andromda.core.common;


/**
 * Wraps any exception that occurs when configuring/processing a template object
 *
 * @see org.andromda.core.common.TemplateObject
 */
public class TemplateObjectException
    extends RuntimeException
{
    /**
     * Constructs an instance of TemplateObjectException.
     *
     * @param parent
     */
    public TemplateObjectException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TemplateObjectException.
     *
     * @param message
     */
    public TemplateObjectException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TemplateObjectException.
     *
     * @param message
     * @param parent
     */
    public TemplateObjectException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}