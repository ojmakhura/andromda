package org.andromda.core.common;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * ExceptionRecorder provides a function to record an exception to a file along
 * with the trace data if active.
 * 
 * @author Martin West
 */
public class ExceptionRecorder
{

    /** Andromda version * */
    public static String andromda_version = "not set";

    /** The exceptions directory name:exceptions. */
    public static String exceptionDirectoryName = ".";

    /** The exceptions directory, initialized to exceptions. */
    public static File exceptionDirectory = null;

    private static final SimpleDateFormat cvDateFormat = new SimpleDateFormat(
        "yyMMddHHmmss");

    private static final Random ran = new Random();

    /** Private constructor, this class is not intended to be instantiated. */
    private ExceptionRecorder()
    {}

    /**
     * record writes out the exception to a file along with trace data if
     * active. The file name is of the form sYYMMDDHHMMSS <_nn>.exc where YY..SS
     * is the timestamp <_nn>is an ascending sequence number when multiple
     * exceptions occur in the same second. Returns the filename of the
     * generated exception report.
     * 
     * @param Exception to record.
     */
    public static String record(Throwable th)
    {
        return record("", th, "S");
    }

    /**
     * record writes out the exception to a file along with trace data if
     * active. The file name is of the form sYYMMDDHHMMSS <_nn>.exc where YY..SS
     * is the timestamp <_nn>is an ascending sequence number when multiple
     * exceptions occur in the same second. Returns the filename of the
     * generated exception report.
     * 
     * @param Message to log with the exception report.
     * @param Exception to record.
     */
    public static String record(String errorMessage, Throwable th)
    {
        return record(errorMessage, th, "S");
    }

    /**
     * record writes out the exception to a file along with trace data if
     * active. The file name is of the form sYYMMDDHHMMSS <_nn>.exc where YY..SS
     * is the timestamp <_nn>is an ascending sequence number when multiple
     * exceptions occur in the same second.
     * 
     * @param msg diagnostic message
     * @param th exception to record.
     * @param prefix for the file name.
     */
    public static String record(String message, Throwable th, String prefix)
    {
        PrintWriter writer;
        String tempName = null;
        String result = null;
        File exceptionFile;
        try
        {
            tempName = getUniqueName(prefix);
            exceptionFile = new File(exceptionDirectory, tempName);
            result = exceptionFile.getCanonicalPath();
            writer = new PrintWriter(new FileWriter(exceptionFile));
            writer.println("*** AndroMDA Exception Recording ***");
            writer.println("Andromda Version:" + getAndromdaVersion());
            writer.println("Error:" + message);
            writer.println("Main exception:" + th.getMessage());
            Throwable cause = ExceptionUtils.getRootCause(th);
            writer.println("Root exception:" + cause.getMessage());
            cause.printStackTrace(writer);
            writer.close();
        }
        catch (Throwable ex)
        {
            System.err
                .println("ExceptionRecorder.record error recording exception:"
                    + th);
            th.printStackTrace(System.err);
            System.err.println("ExceptionRecorder.record Exception:" + ex);
            ex.printStackTrace(System.err);
        } // End catch
        return result;
    } // end of method record

    /** Get a unique file name. */
    protected static synchronized String getUniqueName(String prefix)
    {
        String tempName = prefix + cvDateFormat.format(new Date()) + ".exc";
        int suffix = 0;
        File exceptionFile = new File(exceptionDirectory, tempName);
        while (exceptionFile.exists())
        {
            tempName = prefix + cvDateFormat.format(new Date()) + "_"
                + suffix++ + ".exc";
            exceptionFile = new File(exceptionDirectory, tempName);
            // Give another user an opportunity to
            // grab a file name. Use a random delay to
            // introduce variability
            try
            {
                Thread.sleep(Math.abs(ran.nextInt() % 100));
            }
            catch (InterruptedException e1)
            {
                // ignore
            }
        } // end while

        // Grab the file name, there is a window when we
        // are writing the file, that some one else in
        // a different VM could get the same file name.
        try
        {
            RandomAccessFile tmpfile;
            tmpfile = new RandomAccessFile(exceptionFile, "rw");
            tmpfile.writeChar('t');
            tmpfile.close();
        }
        catch (Exception e)
        {
            // ignore
        }
        return tempName;
    } // end method getUniqueName

    static
    {
        /* initialize the exceptionDirectory */
        try
        {
            exceptionDirectory = new File(exceptionDirectoryName);
            if (!exceptionDirectory.exists())
            {
                exceptionDirectory.mkdir();
            }
        }
        catch (Throwable e)
        {}
        finally
        {
            if (exceptionDirectory == null)
            {
                exceptionDirectory = new File(".");
            }
        }
    }

    /**
     * Method getExceptionDirectory.
     */
    public static File getExceptionDirectory()
    {
        return exceptionDirectory;
    }

    /**
     * @return Returns the andromda_version.
     */
    public static String getAndromdaVersion()
    {
        return andromda_version;
    }

    /**
     * @param andromda_version The andromda_version to set.
     */
    public static void setAndromdaVersion(String andromda_version)
    {
        ExceptionRecorder.andromda_version = andromda_version;
    }
}