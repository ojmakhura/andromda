package org.andromda.core.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
     * @param overwrite if true, replaces the file (if it exists), otherwise
     *        adds to the contents of the file.
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
     * @param overwrite if true, replaces the file (if it exists), otherwise
     *        adds to the contents of the file.
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
}