package org.andromda.cartridges.interfaces;

/**
 * This exception is thrown when a special situation is encountered within an
 * AndroMDA cartridge.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class CartridgeException extends RuntimeException
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
     * @param arg0
     */
    public CartridgeException(String arg0)
    {
        super(arg0);
    }

    /**
     * Constructor for CartridgeException.
     * @param arg0
     * @param arg1
     */
    public CartridgeException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

    /**
     * Constructor for CartridgeException.
     * @param arg0
     */
    public CartridgeException(Throwable arg0)
    {
        super(arg0);
    }

}
