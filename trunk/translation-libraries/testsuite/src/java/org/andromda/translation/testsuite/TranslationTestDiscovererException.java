package org.andromda.translation.testsuite;

/**
 * Any unchecked exception that will be thrown when an unexpected error occurs
 * during TranslationTestDiscoverer processing.
 */
public class TranslationTestDiscovererException
    extends RuntimeException
{

    /**
     * Constructs an instance of TranslationTestDiscovererException.
     * 
     * @param th
     */
    public TranslationTestDiscovererException(
        Throwable th)
    {
        super(th);
    }

    /**
     * Constructs an instance of TranslationTestDiscovererException.
     * 
     * @param msg
     */
    public TranslationTestDiscovererException(
        String msg)
    {
        super(msg);
    }

    /**
     * Constructs an instance of TranslationTestDiscovererException.
     * 
     * @param msg
     * @param th
     */
    public TranslationTestDiscovererException(
        String msg,
        Throwable th)
    {
        super(msg, th);
    }

}