package org.andromda.samples.carrental.contracts;


public class ContractException
    extends Exception
{

    /**
     * Constructor for VertraegeException.
     */
    public ContractException()
    {
        super();
    }

    /**
     * Constructor for ContractException.
     * @param message
     */
    public ContractException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ContractException.
     * @param message
     * @param cause
     */
    public ContractException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for ContractException.
     * @param cause
     */
    public ContractException(Throwable cause)
    {
        super(cause);
    }

}
