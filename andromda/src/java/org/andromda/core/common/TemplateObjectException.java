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
     * @param parent the parent throwable
     */
    public TemplateObjectException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TemplateObjectException.
     *
     * @param message the exception message
     */
    public TemplateObjectException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TemplateObjectException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public TemplateObjectException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}