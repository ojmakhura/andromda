package org.andromda.translation.testsuite;

/**
 * Any unchecked exception that will be thrown when an unexpected
 * ContextElementFinder error occurs.
 */
public class ContextElementFinderException
    extends RuntimeException
{

    /**
     * Constructs an instance of ContextElementFinderException.
     * 
     * @param th
     */
    public ContextElementFinderException(
        Throwable th)
    {
        super(th);
    }

    /**
     * Constructs an instance of ContextElementFinderException.
     * 
     * @param msg
     */
    public ContextElementFinderException(
        String msg)
    {
        super(msg);
    }

    /**
     * Constructs an instance of ContextElementFinderException.
     * 
     * @param msg
     * @param th
     */
    public ContextElementFinderException(
        String msg,
        Throwable th)
    {
        super(msg, th);
    }

}