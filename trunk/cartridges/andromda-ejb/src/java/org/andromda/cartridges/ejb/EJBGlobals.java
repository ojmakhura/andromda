package org.andromda.cartridges.ejb;

/**
 * Stores Globals specific to the EJB cartridge.
 *
 * @author Chad Brandon
 */
public class EJBGlobals
{
    /**
     * Stores the default EJB transaction type.
     */
    public static final String TRANSACTION_TYPE = "transactionType";

    /**
     * The pattern to use for determining the package name for EJBs.
     */
    public static final String JNDI_NAME_PREFIX = "jndiNamePrefix";
}
