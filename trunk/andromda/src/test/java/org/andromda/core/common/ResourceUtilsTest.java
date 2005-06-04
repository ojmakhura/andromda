package org.andromda.core.common;

import java.net.URL;

import java.util.List;

import junit.framework.TestCase;


/**
 * JUnit test for {@link org.andromda.core.common.ResourceUtils}
 *
 * @author Chad Brandon
 */
public class ResourceUtilsTest
    extends TestCase
{
    public void testGetDirectoryContentsZeroLevels()
        throws Exception
    {
        URL resource = ResourceUtilsTest.class.getResource("ResourceUtilsTestDir");
        assertNotNull(resource);
        List resources = ResourceUtils.getDirectoryContents(resource, 0);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        assertEquals(
            4,
            resources.size());
    }

    public void testGetDirectoryContentsTwoLevels()
        throws Exception
    {
        URL resource = ResourceUtilsTest.class.getResource("ResourceUtilsTestDir/one/two");
        assertNotNull(resource);
        List resources = ResourceUtils.getDirectoryContents(resource, 2);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        assertEquals(
            4,
            resources.size());
    }
}