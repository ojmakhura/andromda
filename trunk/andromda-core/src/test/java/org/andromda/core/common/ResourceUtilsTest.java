package org.andromda.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
            6,
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
            6,
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

    /**
     * Test for ResourceUtils.writeUrlToFile method 
     * @throws Exception
     */
    @Test
    public void testWriteUrlToFile()
        throws Exception
    {
        URL resource = ResourceUtils.resolveClasspathResource(
            "classpath:" + ResourceUtilsTest.class.getPackage().getName().replace('.', '/') + '/' +
            "ResourceUtilsTestDir/backblue.gif" );
        assertNotNull(resource);
        final File tempFile = File.createTempFile("andromda", "writeUrlToFileTest");
        assertNotNull(tempFile);
        tempFile.deleteOnExit();

        ResourceUtils.writeUrlToFile(resource, tempFile.getName());
        new File(tempFile.getName()).deleteOnExit();

        final InputStream inputStream = resource.openStream();
        assertNotNull(inputStream);
        final InputStream inputStream2 = new FileInputStream(tempFile.getName());
        assertNotNull(inputStream2);

        assertTrue(IOUtils.contentEquals(inputStream, inputStream2));

        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(inputStream2);
    }    
}
