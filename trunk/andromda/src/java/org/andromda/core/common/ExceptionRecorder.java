package org.andromda.core.common;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * ExceptionRecorder provides a function to record an exception to a file along
 * with the trace data if active.
 * </p>
 * 
 * @author Martin West
 */
public class ExceptionRecorder
{
    private static final Logger logger = Logger
        .getLogger(ExceptionRecorder.class);

    /** File header constant */
    static final String FILE_HEADER = "------- AndroMDA Exception Recording -------";

    /** Run line system constant */
    static final String RUN_SYSTEM = "Run system .....: ";
    /** Run line jdk constant */
    static final String RUN_JDK = "Run jdk ........: ";
    /** Information not available constant */
    static final String INFORMATION_UNAVAILABLE = " unavailable";

    /** The exceptions directory name:exceptions. */
    private static String exceptionDirectoryName = ".";

    /** The exceptions directory, initialized to exceptions. */
    private static File exceptionDirectory = null;

    private static final SimpleDateFormat cvDateFormat = new SimpleDateFormat(
        "yyMMddHHmmss");

    private static final Random random = new Random();

    /** Private constructor, this class is not intended to be instantiated. */
    private ExceptionRecorder()
    {}

    /**
     * <p>
     * Writes out the exception to a file along with trace data if active. The
     * file name is of the form sYYMMDDHHMMSS <_nn>.exc where YY..SS is the
     * timestamp <_nn>is an ascending sequence number when multiple exceptions
     * occur in the same second. Returns the filename of the generated exception
     * report.
     * </p>
     * 
     * @param Exception to record.
     */
    public static String record(Throwable throwable)
    {
        return record("", throwable, "S");
    }

    /**
     * <p>
     * Writes out the exception to a file along with trace data if active. The
     * file name is of the form sYYMMDDHHMMSS <_nn>.exc where YY..SS is the
     * timestamp <_nn>is an ascending sequence number when multiple exceptions
     * occur in the same second. Returns the filename of the generated exception
     * report.
     * </p>
     * 
     * @param errorMessage to log with the exception report.
     * @param throwable to record.
     */
    public static String record(String errorMessage, Throwable throwable)
    {
        return record(errorMessage, throwable, "S");
    }

    /**
     * The default prefix given, if prefix in
     * {@link #record(String, Throwable, String)is null.
     */
    private static final String DEFAULT_PREFIX = "andromda";

    /**
     * <p>
     * Writes out the exception to a file along with trace data if active. The
     * file name is of the form sYYMMDDHHMMSS <_nn>.exc where YY..SS is the
     * timestamp <_nn>is an ascending sequence number when multiple exceptions
     * occur in the same second.
     * </p>
     * 
     * @param message diagnostic message
     * @param throwable exception to record.
     * @param prefix for the file name.
     */
    public static String record(
        String message,
        Throwable throwable,
        String prefix)
    {
        PrintWriter writer;
        String uniqueName = null;
        String result = null;
        File exceptionFile;
        if (StringUtils.isEmpty(prefix))
        {
            prefix = DEFAULT_PREFIX;
        }
        try
        {
            BuildInformation buildInformation = BuildInformation.instance();
            uniqueName = getUniqueName(prefix);
            exceptionFile = new File(exceptionDirectory, uniqueName);
            result = exceptionFile.getCanonicalPath();
            writer = new PrintWriter(new FileWriter(exceptionFile));
            writer.println(FILE_HEADER);
            writer.println("Version ........: "
                + buildInformation.getBuildVersion());
            writer.println("Error ..........: " + message);
            writer.println("Build ..........: "
                + buildInformation.getBuildDate());
            writer.println("Build system ...: "
                + buildInformation.getBuildSystem());
            writer.println("Build jdk ......: "
                + buildInformation.getBuildJdk());
            writer.println("Build builder ..: "
                + buildInformation.getBuildBuilder());
            // Place in try/catch in case system is protected.
            try
            {
                writer.println(RUN_SYSTEM + System.getProperty("os.name")
                    + System.getProperty("os.version"));
                writer.println(RUN_JDK + System.getProperty("java.vm.vendor")
                    + System.getProperty("java.vm.version"));
            }
            catch (Exception ex)
            {
                // ignore
                writer.println(RUN_SYSTEM + INFORMATION_UNAVAILABLE);
                writer.println(RUN_JDK + INFORMATION_UNAVAILABLE);
            }
            writer.println("Main Exception .: " + throwable.getMessage());
            Throwable cause = ExceptionUtils.getRootCause(throwable);
            if (cause == null)
            {
                cause = throwable;
            }
            writer.println("Root Exception --> " + cause);
            cause.printStackTrace(writer);
            writer.close();
            AndroMDALogger.error("Exception recorded in --> '" + result + "'");
        }
        catch (Throwable th)
        {
            final String errorMessage = "ExceptionRecorder.record error recording exception --> '"
                + throwable + "'";
            logger.error(errorMessage, th);
        } // End catch
        return result;
    } // end of method record

    /**
     * The suffix to give the recorded exception files.
     */
    private static final String SUFFIX = ".exc";

    /**
     * Gets a unique file name.
     */
    protected static synchronized String getUniqueName(String prefix)
    {
        String uniqueName = prefix + cvDateFormat.format(new Date()) + SUFFIX;
        int suffix = 0;
        File exceptionFile = new File(exceptionDirectory, uniqueName);
        while (exceptionFile.exists())
        {
            uniqueName = prefix + cvDateFormat.format(new Date()) + "_"
                + suffix++ + SUFFIX;
            exceptionFile = new File(exceptionDirectory, uniqueName);
            // Give another user an opportunity to
            // grab a file name. Use a random delay to
            // introduce variability
            try
            {
                Thread.sleep(Math.abs(random.nextInt() % 100));
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
            RandomAccessFile file;
            file = new RandomAccessFile(exceptionFile, "rw");
            file.writeChar('t');
            file.close();
        }
        catch (Exception ex)
        {
            // ignore
        }
        return uniqueName;
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
        catch (Throwable th)
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
     * Returns the directory to which the exceptions are written.
     * 
     * @return the exception directory as a java.io.File instance.
     */
    public static File getExceptionDirectory()
    {
        return exceptionDirectory;
    }
}