/*
 * Created on 24-Jan-2005
 *
 */
package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * A simple test to check the operation of the ExceptionRecorder.
 * 
 * @author martin
 * 
 */
public class ExceptionRecorderTest extends TestCase
{

    /*
     * Test that a .exc file is created and it has at least the file header
     * string.
     */
    public void testRecordStringThrowableString()
    {
        Exception ex1 = new Exception("ExceptionRecorder Test");
        Exception ex2 = new Exception(ex1);
        String filename = ExceptionRecorder.record("Test message", ex2, "test");
        File excFile = new File(filename);
        assertTrue("exception file not created:" + excFile, excFile.exists());
        FileReader fr = null;
        try
        {
            fr = new FileReader(excFile);
            BufferedReader br = new BufferedReader(fr);
            String inline;
            inline = br.readLine();
            assertTrue("First line not header line",
                    ExceptionRecorder.FILE_HEADER.equals(inline));
        } catch (FileNotFoundException e)
        {
            fail(e.getMessage());
        } catch (IOException e)
        {
            fail(e.getMessage());
        } finally
        {
            try
            {
                // Close the file.
                fr.close();
            } catch (Exception e)
            {
                // ignore
            }
            try
            {
                // Clean up since the .exc gets created
                // in the andromda directory and not a
                // target directory.
                excFile.delete();
            } catch (Exception e)
            {
                // ignore
            }
        }
    }

}
