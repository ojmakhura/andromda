package org.andromda.core.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * I/O Utilities for writing files.
 * 
 * @author Chad Brandon
 */
public class OutputUtils
{
    private static Logger logger = Logger.getLogger(OutputUtils.class);

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param file the file to which to write.
     * @throws IOException
     */
    public static void writeStringToFile(String string, File file)
        throws IOException
    {
        final String methodName = "OutputUtils.writeStringToFile";
        ExceptionUtils.checkNull(methodName, "file", file);
        writeStringToFile(string, file.toString());
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation the location of the file which to write.
     * @throws IOException
     */
    public static void writeStringToFile(String string, String fileLocation)
        throws IOException
    {
        final String methodName = "OutputUtils.writeStringToFile";
        if (string == null)
        {
            string = "";
        }
        ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);

        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName + " with string '" + string
                + "' and fileLocation '" + fileLocation + "'");
        File file = new File(fileLocation);
        File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(string.getBytes());
        stream.flush();
        stream.close();
        stream = null;
    }

    /**
     * Writes the URL contents to a file specified by the fileLocation argument.
     * 
     * @param url the URL to read
     * @param fileLocation the location which to write.
     */
    public static void writeUrlToFile(URL url, String fileLocation)
        throws IOException
    {
        String methodName = "writeUrlToFile";
        ExceptionUtils.checkNull(methodName, "url", url);
        ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);

        InputStream inputStream = url.openStream();

        File file = new File(fileLocation);
        File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        //make any directories that don't exist
        FileOutputStream stream = new FileOutputStream(file);
        for (int ctr = inputStream.read(); ctr != -1; ctr = inputStream.read())
        {
            stream.write(ctr);
        }
        stream.flush();
        stream.close();
        stream = null;
    }
}