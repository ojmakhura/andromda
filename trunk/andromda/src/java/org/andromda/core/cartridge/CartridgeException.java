package org.andromda.core.cartridge;


/**
 * This exception is thrown when a special situation is encountered within an AndroMDA cartridge.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public class CartridgeException
    extends RuntimeException
{
    /**
     * Constructor for CartridgeException.
     */
    public CartridgeException()
    {
        super();
    }

    /**
     * Constructor for CartridgeException.
     *
     * @param message
     */
    public CartridgeException(String message)
    {
        super(message);
    }

    /**
     * Constructor for CartridgeException.
     *
     * @param message
     * @param parent
     */
    public CartridgeException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for CartridgeException.
     *
     * @param parent
     */
    public CartridgeException(Throwable parent)
    {
        super(parent);
    }
}