package org.andromda.core.profile;

import org.andromda.core.common.NamespaceProperties;

/**
 * <p>
 * Used to load and retrieve stereotype mapping values.  
 * Alows us to decouple the stereotype names form the code,
 * so that its very easy to override default stereotype names.
 * </p>
 * @author Chad Brandon
 */
public class Stereotypes
    extends ProfileValues
{
    /**
     * The shared instance of this class.
     */
    private static final Stereotypes instance = new Stereotypes();

    /**
     * Gets the shared instance of this class.
     * 
     * @return the shared instance.
     */
    public static Stereotypes instance()
    {
        return instance;
    }

    private static final String DEFAULT_FILE_NAME = "andromda-stereotypes.xml";

    /**
     * The default constructor. NOTE: normally you'll want to retrieve the
     * shared instance of this class using {@link #instance()}.
     */
    public Stereotypes()
    {
        super(NamespaceProperties.STEREOTYPES_URI, DEFAULT_FILE_NAME);
    }
}
