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
     * @param msg
     */
    public TemplateException(
        String msg)
    {
        super(msg);
    }

    /**
     * Constructs an instance of TemplateException.
     * 
     * @param msg
     * @param th
     */
    public TemplateException(
        String msg,
        Throwable th)
    {
        super(msg, th);
    }
}