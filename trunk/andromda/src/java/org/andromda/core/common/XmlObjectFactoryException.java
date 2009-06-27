package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during execution of the XmlObjectFactory.
 */
public class XmlObjectFactoryException
    extends RuntimeException
{
    /**
     * Constructs an instance of XmlObjectFactoryException.
     *
     * @param parent
     */
    public XmlObjectFactoryException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of XmlObjectFactoryException.
     *
     * @param message
     */
    public XmlObjectFactoryException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of XmlObjectFactoryException.
     *
     * @param message
     * @param parent
     */
    public XmlObjectFactoryException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}