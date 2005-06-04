package org.andromda.core.translation;


/**
 * Any unchecked exception that will be thrown when a translation exception occurs.
 */
public class TranslatorException
    extends RuntimeException
{
    /**
     * Constructs an instance of TranslatorException.
     *
     * @param parent
     */
    public TranslatorException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TranslatorException.
     *
     * @param message
     */
    public TranslatorException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TranslatorException.
     *
     * @param message
     * @param parent
     */
    public TranslatorException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}