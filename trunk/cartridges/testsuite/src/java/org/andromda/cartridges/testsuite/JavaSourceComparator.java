package org.andromda.cartridges.testsuite;

import java.io.File;

import xjavadoc.codeunit.CodeTestCase;

/**
 * Compares two Java source files. It checks if both file do exist and if the
 * APIs of both files are equal.
 * 
 * @author Ralf Wirdemann
 * @author Chad Brandon
 */
public class JavaSourceComparator
    extends CodeTestCase
{
    private File expectedFile;
    private File actualFile;

    public JavaSourceComparator(
        String methodName,
        File expectedFile,
        File actualFile)
    {
        super();
        this.setName(methodName);
        this.expectedFile = expectedFile;
        this.actualFile = actualFile;
    }

    public void testAPIEquals()
    {
        assertTrue("expected file <" + expectedFile.getPath()
            + "> doesn't exist", expectedFile.exists());
        assertTrue(
            "actual file <" + actualFile.getPath() + "> doesn't exist",
            actualFile.exists());
        super.assertApiEquals(expectedFile, actualFile);
    }
}