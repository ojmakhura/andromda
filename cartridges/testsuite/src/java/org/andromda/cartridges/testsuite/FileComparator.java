package org.andromda.cartridges.testsuite;

import junit.framework.TestCase;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Compares two files. It checks if both file do exist and if the contents of both files are equal.
 *
 * @author Chad Brandon
 */
public class FileComparator
        extends TestCase
{
    private File expectedFile;
    private File actualFile;
    private boolean binary;

    /**
     * Constructs a new instance of the FileComparator.
     *
     * @param testName     the name of the test to run
     * @param expectedFile the location of the expected file
     * @param actualFile   the location of the actual file.
     * @param binary       whether or not the file is binary, if it is binary contents of the binary are not compared as
     *                     Strings but as binary files.
     */
    public FileComparator(String testName, File expectedFile, File actualFile, boolean binary)
    {
        super();
        this.setName(testName);
        this.expectedFile = expectedFile;
        this.actualFile = actualFile;
        this.binary = binary;
    }

    public void testEquals()
    {
        assertTrue("expected file <" + expectedFile.getPath() + "> doesn't exist", expectedFile.exists());
        assertTrue("actual file <" + actualFile.getPath() + "> doesn't exist", actualFile.exists());
        this.testContentsEqual();
    }

    /**
     * Loads both the <code>actual</code> and <code>expected</code> files and tests the contents for equality.
     */
    protected void testContentsEqual()
    {
        try
        {
            String actualContents = ResourceUtils.getContents(actualFile.toURL());
            String expectedContents = ResourceUtils.getContents(expectedFile.toURL());
            String message = "actual file <" + actualFile + "> does not match,";
            if (this.binary)
            {
                assertTrue(message, FileUtils.contentEquals(expectedFile, actualFile));
            }
            else
            {
                assertEquals(message, expectedContents.trim(), actualContents.trim());
            }
        }
        catch (Throwable th)
        {
            fail(th.toString());
        }
    }
}