package org.andromda.cartridges.database;

/**
 * Stores Globals specific to the Database cartridge.
 *
 * @author Wouter Zoons
 */
public class DatabaseGlobals
{
    /**
     * Sets the multiplier for the dummy data size, with this you can easily resize the database load.
     */
    public static final String DUMMYLOAD_MULTIPLIER = "dummyLoadMultiplier";
    public static final float DUMMYLOAD_MULTIPIER_DEFAULT = 1;
}
