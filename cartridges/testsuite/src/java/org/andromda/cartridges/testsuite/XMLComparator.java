package org.andromda.cartridges.testsuite;

import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

import org.custommonkey.xmlunit.XMLTestCase;

/**
 * Compares two XML-Files.
 * 
 * @author Ralf Wirdemann
 * @author Chad Brandon
 */
public class XMLComparator
    extends XMLTestCase
{

    private File expectedFile;
    private File actualFile;

    public XMLComparator(
        String s,
        File expectedFile,
        File actualFile)
    {
        super(s);
        this.expectedFile = expectedFile;
        this.actualFile = actualFile;
    }

    public void testXMLEquals()
    {
        try
        {
            assertTrue("expected file <" + expectedFile.getPath()
                + "> doesn't exist", expectedFile.exists());
            assertTrue("actual file <" + actualFile.getPath()
                + "> doesn't exist", actualFile.exists());
            assertXMLEqual(
                new FileReader(expectedFile.getAbsolutePath()),
                new FileReader(actualFile.getAbsolutePath()));
        }
        catch (Throwable th)
        {
            TestCase.fail(th.toString());
        }
    }
}