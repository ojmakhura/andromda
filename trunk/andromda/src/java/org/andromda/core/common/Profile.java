package org.andromda.core.common;

import java.net.URL;
import java.util.Iterator;

import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.mapping.Mapping;
import org.andromda.core.mapping.Mappings;


/**
 * <p>
 * This class provides the ability to load profile mapping files from default locations as well as easily load profile
 * mappings files that will override the default profile values. This allows us to decouple all profile information from
 * the actual code and allows users to override default profile values (i.e. stereotype names can be anything the user
 * would like, instead of forcing them to users our naming conventions). </p>
 *
 * @deprecated this is deprecated and will be removed after 3.1 (please use {org.andromda.core.profile.Profile} instead)
 * you must all move your META-INF/andromda-profile.xml to META-INF/andromda/profile.xml as well.
 * @author Chad Brandon
 */
public class Profile
{
    /**
     * The shared instance of this class.
     */
    private static Profile instance;

    /**
     * Gets the shared instance of this class.
     *
     * @return the shared instance.
     */
    public static final Profile instance()
    {
        if (instance == null)
        {
            instance = new Profile();
        }
        return instance;
    }

    private Profile()
    {
        this.discoverMappings();

        // - do not allow instantiation
    }

    /**
     * The shared underlying profile instance.
     */
    private final org.andromda.core.profile.Profile profile = org.andromda.core.profile.Profile.instance();

    /**
     * Gets the profile value for the given <code>from</code> value. Returns the <code>from</code> if the profile value
     * can not be found.
     *
     * @param from the <code>from</code> value of the mapped profile value.
     * @return the mapped profile value.
     */
    public String get(final String name)
    {
        return profile.get(name);
    }

    /**
     * The location to which default profiles are stored. If the {@link NamespaceProperties#MERGE_MAPPINGS_URI}isn't
     * defined then profile mappings are found here.
     */
    private static final String DEFAULT_LOCATION = "META-INF/andromda-profile.xml";

    /**
     * Attempts to retrieve the Mappings instance for the given <code>mappingsUri</code> belonging to the given
     * <code>namespace</code>.
     */
    private final void discoverMappings()
    {
        final String defaultLocation = DEFAULT_LOCATION;
        final URL[] profileResources = ResourceFinder.findResources(defaultLocation);
        if (profileResources != null && profileResources.length > 0)
        {
            for (int ctr = 0; ctr < profileResources.length; ctr++)
            {
                final URL profileResource = profileResources[ctr];
                AndroMDALogger.warn(
                    "WARNING!! '" + profileResource +
                    "' is using the deprecated profile format, please upgrade to the new one");
                Mappings mappings = Mappings.getInstance(profileResource);
                for (final Iterator iterator = mappings.getMappings().iterator(); iterator.hasNext();)
                {
                    final Mapping mapping = (Mapping)iterator.next();
                    if (!mapping.getFroms().isEmpty())
                    {
                        final String from = (String)mapping.getFroms().iterator().next();
                        final String to = mapping.getTo();
                        profile.addElement(
                            mappings.getName(),
                            from,
                            to);
                    }
                }
            }
        }
    }
}