/*
 * Created on 24-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExceptionRecorderTest extends TestCase
{

    /*
     * Class under test for String record(String, Throwable, String)
     */
    public void testRecordStringThrowableString()
    {
        Exception ex1 = new Exception( "ExceptionRecorder Test");
        Exception ex2 = new Exception( ex1 );
        String filename = ExceptionRecorder.record( "Test message", ex2, "test" );
        File excFile = new File( filename );
        assertTrue( "exception file not created:" + excFile, excFile.exists());
        FileReader fr = null;
        try
        {
            fr = new FileReader( excFile );
            BufferedReader br = new BufferedReader( fr );
            String inline;
            inline = br.readLine();
            assertTrue( "First line not header line", ExceptionRecorder.FILE_HEADER.equals(inline));
        } catch (FileNotFoundException e)
        {
            fail(e.getMessage());
        } catch (IOException e)
        {
            fail(e.getMessage());
        } finally {
            try
            {
                fr.close();
            } catch (Exception e)
            {
                // ignore
            }
            try
            {
                excFile.delete();
            } catch (Exception e)
            {
                // ignore
            }
        }
}


}
