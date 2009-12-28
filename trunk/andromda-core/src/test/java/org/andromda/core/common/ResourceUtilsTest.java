package org.andromda.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.net.URL;
import java.util.List;
import org.junit.Test;

/**
 * JUnit test for {@link org.andromda.core.common.ResourceUtils}
 *
 * @author Chad Brandon
 */
public class ResourceUtilsTest
{
  /**
   * @throws Exception
   */
  @Test
  public void testGetDirectoryContentsZeroLevels()
        throws Exception
    {
        URL resource = ResourceUtilsTest.class.getResource("ResourceUtilsTestDir");
        assertNotNull(resource);
        List resources = ResourceUtils.getDirectoryContents(resource, 0);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        assertEquals(
            5,
            resources.size());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetDirectoryContentsTwoLevels()
        throws Exception
    {
        URL resource = ResourceUtilsTest.class.getResource("ResourceUtilsTestDir/one/two");
        assertNotNull(resource);
        List resources = ResourceUtils.getDirectoryContents(resource, 2);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        assertEquals(
            5,
            resources.size());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testResolveClasspathResource()
        throws Exception
    {
        URL resource = ResourceUtils.resolveClasspathResource(
            "classpath:" + ResourceUtilsTest.class.getPackage().getName().replace('.', '/') + '/' +
            "ResourceUtilsTestDir/one.zip!/two.zip!/three.zip!/file.txt" );
        assertNotNull(resource);
    }
}