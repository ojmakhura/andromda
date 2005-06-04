package org.andromda.core.common;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;


/**
 * Tests the {@link org.andromda.core.common.Profile}
 * class.
 *
 * @author Chad Brandon
 */
public class ProfileTest
    extends TestCase
{
    public void testGetProfileValues()
    {
        Namespace namespace = new Namespace();
        namespace.setName(Namespaces.DEFAULT);
        URL profileOverrideResource = ProfileTest.class.getResource("/META-INF/profile/andromda-profile-override.xml");
        assertNotNull(profileOverrideResource);
        Property property = new Property();
        property.setName(NamespaceProperties.PROFILE_MAPPINGS_URI);
        property.setValue(profileOverrideResource.toString());
        namespace.addProperty(property);
        Namespaces.instance().addNamespace(namespace);
        Profile profile = Profile.instance();
        assertEquals(
            "TestFrom1",
            profile.get("TEST_FROM_1"));
        assertEquals(
            "TestFrom2",
            profile.get("TEST_FROM_2"));
        assertEquals(
            "TestFrom3",
            profile.get("TEST_FROM_3"));
        assertEquals(
            "TestFromOverride",
            profile.get("TEST_FROM_OVERRIDE"));
    }
}