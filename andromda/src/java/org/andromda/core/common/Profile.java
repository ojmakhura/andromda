package org.andromda.core.common;

import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;

import java.net.URL;

/**
 * <p/>
 * This class provides the ability to load profile mapping files from default locations as well as easily load profile
 * mappings files that will override the default profile values. This allows us to decouple all profile information from
 * the actual code and allows users to override default profile values (i.e. stereotype names can be anything the user
 * would like, instead of forcing them to users our naming conventions). </p>
 *
 * @author Chad Brandon
 */
public class Profile
{

    /**
     * The shared instance of this class.
     */
    private static final Profile instance = new Profile();

    /**
     * Gets the shared instance of this class.
     *
     * @return the shared instance.
     */
    public static final Profile instance()
    {
        return instance;
    }

    /**
     * The default constructor. NOTE: normally you'll want to retrieve the shared instance of this class using {@link
     * #instance()}.
     */
    public Profile()
    {
        this.profileMappings = this.getMappings();
    }

    /**
     * Gets the profile value for the given <code>from</code> value. Returns the <code>from</code> if the profile value
     * can not be found.
     *
     * @param from the <code>from</code> value of the mapped profile value.
     * @return the mapped profile value.
     */
    public String get(final String from)
    {
        String value = from;
        if (this.profileMappings != null)
        {
            value = this.profileMappings.getTo(from);
        }
        return StringUtils.trimToEmpty(value);
    }

    /**
     * Stores the profile values.
     */
    private Mappings profileMappings = null;

    /**
     * The location to which default profiles are stored. If the {@link NamespaceProperties#MERGE_MAPPINGS_URI}isn't
     * defined then profile mappings are found here.
     */
    private static final String DEFAULT_LOCATION = "META-INF/andromda-profile.xml";

    /**
     * Attempts to retrieve the Mappings instance for the given <code>mappingsUri</code> belonging to the given
     * <code>namespace</code>.
     *
     * @param mappingsProperty the name of the namespace property that will provide the ability to override any default
     *                         default profile values.
     * @param defaultFileName  the name of the file to search for that contains the default profile values.
     */
    private final Mappings getMappings()
    {
        final String defaultLocation = DEFAULT_LOCATION;
        Mappings mappings = null;
        final URL[] profileResources = ResourceFinder.findResources(defaultLocation);
        if (profileResources != null && profileResources.length > 0)
        {
            for (int ctr = 0; ctr < profileResources.length; ctr++)
            {
                final URL profileResource = profileResources[ctr];
                if (mappings == null)
                {
                    mappings = Mappings.getInstance(profileResource);
                }
                else
                {
                    mappings.addMappings(Mappings.getInstance(profileResource));
                }
            }
        }
        final Property mappingsUri = Namespaces.instance().findNamespaceProperty(
            Namespaces.DEFAULT,
            NamespaceProperties.PROFILE_MAPPINGS_URI,
            false);
        final String mappingsUriValue = StringUtils.trimToEmpty(mappingsUri != null ? mappingsUri.getValue() : null);
        if (StringUtils.isNotEmpty(mappingsUriValue))
        {
            if (mappings == null)
            {
                mappings = Mappings.getInstance(mappingsUriValue);
            }
            else
            {
                mappings.addMappings(Mappings.getInstance(mappingsUriValue));
            }
        }
        if (mappings == null)
        {
            AndroMDALogger.warn("Profile resources could not be found --> '" + defaultLocation + "'");
        }
        return mappings;
    }
}
