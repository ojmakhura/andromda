package org.andromda.maven.plugins.cartridge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
     * The actual report contents.
     */
    private StringBuffer report = new StringBuffer();

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
        numberOfFailures++;
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
        this.report.append("-------------------------------------------------------------------------------");
        this.report.append(newLine);
        this.report.append(name + " Test Suite");
        this.report.append(newLine);
        this.report.append("-------------------------------------------------------------------------------");
        this.report.append(newLine);
    }

    /**
     * Signifies the test suite ended and returns the footer message of the
     * test.
     *
     * @param test the test suite being run.
     * @return the test footer.
     */
    String endTestSuite(Test test)
    {
        final double elapsed = ((System.currentTimeMillis() - this.startTime) / 1000.0);
        final StringBuffer footer = new StringBuffer("Tests: " + String.valueOf(this.numberOfTests) + ", ");
        footer.append("Failures: " + String.valueOf(this.numberOfFailures) + ", ");
        footer.append("Errors: " + String.valueOf(this.numberOfErrors) + ", ");
        footer.append("Time elapsed: " + elapsed).append(" sec");
        footer.append(newLine);
        footer.append(newLine);
        this.report.append(footer);
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
        return footer.toString();
    }
}