package org.andromda.core.cartridge.template;

/**
 * Wraps any unexpected exception when using a Template instance.
 * 
 * @see org.andromda.core.cartridge.template.Template
 */
public class TemplateException
    extends RuntimeException
{
    /**
     * Constructs an instance of TemplateException.
     * 
     * @param th
     */
    public TemplateException(
        Throwable th)
    {
        super(th);
    }

    /**
     * Constructs an instance of TemplateException.
     * 
     * @param message the exception message
     */
    public TemplateException(
        String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TemplateException.
     * 
     * @param message the exception message
     * @param th the parent exception
     */
    public TemplateException(
        String message,
        Throwable th)
    {
        super(message, th);
    }
}