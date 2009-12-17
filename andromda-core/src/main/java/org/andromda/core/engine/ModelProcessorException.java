package org.andromda.core.engine;


/**
 * Any unchecked exception that will be thrown during the execution of the
 * ModelProcessor
 */
public class ModelProcessorException
    extends RuntimeException
{
    /**
     * Constructs an instance of ModelProcessorException.
     *
     * @param parent the parent throwable.
     */
    public ModelProcessorException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ModelProcessorException.
     *
     * @param message the exception message.
     */
    public ModelProcessorException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ModelProcessorException.
     *
     * @param message the exception message.
     * @param parent the parent throwable.
     */
    public ModelProcessorException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}