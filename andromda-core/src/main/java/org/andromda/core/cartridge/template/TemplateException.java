package org.andromda.core.cartridge.template;

/**
 * Wraps any unexpected exception when using a Template instance.
 *
 * @see org.andromda.core.cartridge.template.Template
 */
public class TemplateException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;
    /**
     * Constructs an instance of TemplateException.
     *
     * @param parent the parent exception
     */
    public TemplateException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TemplateException.
     *
     * @param message the exception message
     */
    public TemplateException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TemplateException.
     *
     * @param message the exception message
     * @param parent  the parent exception
     */
    public TemplateException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}
