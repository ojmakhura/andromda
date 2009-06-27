package org.andromda.core.templateengine;


/**
 * Any unchecked exception that will be thrown when any processing by a TemplateEngine occurs..
 */
public class TemplateEngineException
    extends RuntimeException
{
    /**
     * Constructs an instance of TemplateEngineException.
     *
     * @param parent
     */
    public TemplateEngineException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TemplateEngineException.
     *
     * @param message
     */
    public TemplateEngineException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TemplateEngineException.
     *
     * @param message
     * @param parent
     */
    public TemplateEngineException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}