package org.andromda.maven.plugins.cartridge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;


/**
 * Formats the cartridge test results into the correct format.
 *
 * @author Chad Brandon
 */
public class CartridgeTestFormatter
    implements TestListener
{
    /**
     * Stores the report file location.
     */
    private File reportFile;

    /**
     * Stores the contents of the report.
     */
    private StringWriter report;

    /**
     * The print writer for the report.
     */
    private PrintWriter reportWriter;

    public CartridgeTestFormatter()
    {
        this.report = new StringWriter();
        this.reportWriter = new PrintWriter(this.report);
    }

    /**
     * Sets the file that will contain the report results (i.e.
     * the results of the cartridge test run).
     *
     * @param reportFile the file to which the report output will be written.
     */
    public void setReportFile(final File reportFile)
    {
        this.reportFile = reportFile;
    }

    /**
     * Keeps track of the number of errors.
     */
    private int numberOfErrors = 0;

    /**
     * @see junit.framework.TestListener#addError(junit.framework.Test, java.lang.Throwable)
     */
    public void addError(
        Test test,
        Throwable throwable)
    {
        this.numberOfErrors++;
        formatError(
            "\tCaused an ERROR",
            test,
            throwable);
    }

    /**
     * Keeps track of the number of failures.
     */
    private int numberOfFailures = 0;

    /**
     * @see junit.framework.TestListener#addFailure(junit.framework.Test, junit.framework.AssertionFailedError)
     */
    public void addFailure(
        Test test,
        AssertionFailedError failure)
    {
        this.numberOfFailures++;
        formatError(
            "\tFAILED",
            test,
            failure);
    }

    public void endTest(Test test)
    {
    }

    /**
     * Keeps track of the number of tests run.
     */
    private int numberOfTests = 0;

    public void startTest(Test test)
    {
        this.numberOfTests++;
    }

    /**
     * Stores the start time of the test suite
     */
    private long startTime = 0;

    /**
     * Stores the new line separator.
     */
    private static final String newLine = System.getProperty("line.separator");

    /**
     * The testsuite started.
     */
    public void startTestSuite(final String name)
    {
        startTime = System.currentTimeMillis();
        this.reportWriter.println("-------------------------------------------------------------------------------");
        this.reportWriter.println(name + " Test Suite");
        this.reportWriter.println("-------------------------------------------------------------------------------");
    }

    private void formatError(
        String type,
        Test test,
        Throwable throwable)
    {
        this.reportWriter.println(type);
        this.reportWriter.println(throwable.getMessage());
        throwable.printStackTrace(this.reportWriter);
    }

    /**
     * Signifies the test suite ended and returns the summary of the
     * test.
     *
     * @param test the test suite being run.
     * @return the test summary.
     */
    String endTestSuite(Test test)
    {
        final double elapsed = ((System.currentTimeMillis() - this.startTime) / 1000.0);
        final StringBuffer summary = new StringBuffer("Tests: " + String.valueOf(this.numberOfTests) + ", ");
        summary.append("Failures: " + String.valueOf(this.numberOfFailures) + ", ");
        summary.append("Errors: " + String.valueOf(this.numberOfErrors) + ", ");
        summary.append("Time elapsed: " + elapsed).append(" sec");
        summary.append(newLine);
        summary.append(newLine);
        this.reportWriter.print(summary);
        if (this.reportFile != null)
        {
            try
            {
                final File parent = this.reportFile.getParentFile();
                if (parent != null && !parent.exists())
                {
                    parent.mkdirs();
                }
                final FileWriter writer = new FileWriter(this.reportFile);
                writer.write(report.toString());
                writer.flush();
                writer.close();
            }
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
            }
        }
        return summary.toString();
    }
}