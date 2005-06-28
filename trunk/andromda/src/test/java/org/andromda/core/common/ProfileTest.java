package org.andromda.core.common;

import junit.framework.TestCase;


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
        final String namespaceName = "test";
        org.andromda.core.profile.Profile.instance().setNamespace(namespaceName);
        Profile profile = Profile.instance();
        assertEquals(
            "TestFrom1",
            profile.get("TEST_FROM_1"));
        assertEquals(
            "TestFrom2",
            profile.get("TEST_FROM_2"));
    }
}