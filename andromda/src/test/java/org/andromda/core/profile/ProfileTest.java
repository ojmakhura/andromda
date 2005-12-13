package org.andromda.core.profile;

import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.namespace.NamespaceComponents;


/**
 * Tests {@link org.andromda.core.profile.Profile}
 * @author Chad Brandon
 */
public class ProfileTest
    extends TestCase
{
    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        NamespaceComponents.instance().discover();
        Collection profiles = ComponentContainer.instance().findComponentsOfType(Profile.class);
        assertNotNull(profiles);
        assertEquals(
            1,
            profiles.size());
        Profile profile = (Profile)profiles.iterator().next();
        Profile.instance().setNamespace(profile.getNamespace());
    }

    public void testGet()
    {
        assertEquals(
            "Entity",
            Profile.instance().get("ENTITY"));
        assertEquals(
            "@andromda.tagged.value",
            Profile.instance().get("ANDROMDA_TAGGED_VALUE"));
        assertEquals(
            "datatype::String",
            Profile.instance().get("STRING_TYPE"));
    }
    
    public void testOverride()
    {
        Namespace namespace = new Namespace();
        namespace.setName(Namespaces.DEFAULT);
        Namespaces.instance().addNamespace(namespace);
        URL profileOverrideResource = ProfileTest.class.getResource("/META-INF/profile/andromda-profile-override.xml");
        assertNotNull(profileOverrideResource);
        Property property = new Property();
        property.setName(NamespaceProperties.PROFILE_MAPPINGS_URI);
        property.setValue(profileOverrideResource.toString());
        namespace.addProperty(property);
        Profile.instance().refresh();
        assertEquals(
            "New Entity",
            Profile.instance().get("ENTITY"));
        assertEquals(
            "TestFromOverride",
            Profile.instance().get("TEST_FROM_OVERRIDE"));
        // - shutdown the profile instance so that we don't affect other tests.
        Profile.instance().shutdown();
    }
}