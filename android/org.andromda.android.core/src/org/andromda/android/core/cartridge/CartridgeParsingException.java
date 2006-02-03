package org.andromda.android.core.cartridge;

/**
 * Thrown if the cartridge descriptor(s) could be parsed correctly.
 * 
 * @author Peter Friese
 * @since 16.11.2005
 */
public class CartridgeParsingException
        extends Exception
{

    /** <code>serialVersionUID</code> for this exception. */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public CartridgeParsingException()
    {
        super();
    }

    /**
     * Creates a new CartridgeParsingException.
     * 
     * @param message The exception message.
     */
    public CartridgeParsingException(final String message)
    {
        super(message);
    }

    /**
     * Creates a new CartridgeParsingException.
     * 
     * @param cause The original reason for the exception.
     */
    public CartridgeParsingException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates a new CartridgeParsingException.
     * 
     * @param message The exception message.
     * @param cause The original reason for this exception.
     */
    public CartridgeParsingException(final String message,
        final Throwable cause)
    {
        super(message, cause);
    }

}
