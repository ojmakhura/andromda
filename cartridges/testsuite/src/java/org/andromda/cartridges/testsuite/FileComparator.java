package org.andromda.cartridges.testsuite;

import java.io.File;

import org.andromda.core.common.ResourceUtils;

import junit.framework.TestCase;

/**
 * Compares two files. It checks if both file do exist and if the APIs of both
 * files are equal.
 * 
 * @author Chad Brandon
 */
public class FileComparator
    extends TestCase
{
    private File expectedFile;
    private File actualFile;

    public FileComparator(
        String methodName,
        File expectedFile,
        File actualFile)
    {
        super();
        this.setName(methodName);
        this.expectedFile = expectedFile;
        this.actualFile = actualFile;
    }

    public void testEquals()
    {
        assertTrue("expected file <" + expectedFile.getPath()
            + "> doesn't exist", expectedFile.exists());
        assertTrue(
            "actual file <" + actualFile.getPath() + "> doesn't exist",
            actualFile.exists());
        this.testContentsEqual();
    }

    /**
     * Loads both the <code>actual</code> and <code>expected</code> files
     * and tests the contents for equality.
     */
    protected void testContentsEqual()
    {
        try
        {
            String actualContents = ResourceUtils.getContents(actualFile
                .toURL());
            String expectedContents = ResourceUtils.getContents(expectedFile
                .toURL());
            String message = "actual file <" + actualFile + "> does not match,";
            assertEquals(message, expectedContents.trim(), actualContents
                .trim());
        }
        catch (Throwable th)
        {
            fail(th.toString());
        }
    }
}