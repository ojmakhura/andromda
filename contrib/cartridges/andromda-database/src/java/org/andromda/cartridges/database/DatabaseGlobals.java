package org.andromda.cartridges.database;

/**
 * Stores Globals specific to the Database cartridge.
 *
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class DatabaseGlobals
{
    /**
     * Sets the multiplier for the dummy data size, with this you can easily resize the database load.
     */
    public static final String DUMMYLOAD_MULTIPLIER = "dummyLoadMultiplier";
    
    /**
     * The default dummy load multiplier.
     */
    public static final float DUMMYLOAD_MULTIPIER_DEFAULT = 1;
    
    /**
     * The namespace property that stores the primary key constraint prefix.
     */
    public static final String PRIMARY_KEY_CONSTRAINT_PREFIX = "primaryKeyConstraintPrefix";
}
