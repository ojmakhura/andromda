package org.andromda.translation.testsuite;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Finds all translation tests from the current base directory up.
 * 
 * @author Chad Brandon
 */
public class TranslationTestDiscoverer
{

    private static Logger logger = Logger
        .getLogger(TranslationTestDiscoverer.class);

    private static final String CURRENT_DIRECTORY = "current.directory";

    /**
     * If this is specified as a system property, then the TraceTranslator will
     * run instead of the specified translator. This is helpful, in allowing us
     * to see which expressions are being parsed in what order, etc.
     */
    private File currentDirectory;

    /**
     * This is the prefix of translation tests, each translation test must start
     * with this in order to be found.
     */
    private static final String TEST_PREFIX = "TranslationTest-";

    private Map translationTests = null;

    /**
     * Constructs an instance of TranslationTestDiscoverer.
     */
    public TranslationTestDiscoverer()
    {
        String currentDirectory = System.getProperty(CURRENT_DIRECTORY);
        if (StringUtils.isEmpty(currentDirectory))
        {
            throw new TranslationTestDiscovererException("Current directory"
                + " has not be set, please specify the system property '"
                + CURRENT_DIRECTORY + "' with the location from which to"
                + " begin the discovery of translation test files");
        }
        this.currentDirectory = new File(currentDirectory);
        this.translationTests = new LinkedHashMap();
    }

    /**
     * The shared instance
     */
    private static final TranslationTestDiscoverer testFinder = new TranslationTestDiscoverer();

    /**
     * Gets the shared instance of this TranslationTestDiscoverer.
     * 
     * @return the shared TranslationTestDiscoverer.
     */
    public static TranslationTestDiscoverer instance()
    {
        return testFinder;
    }

    /**
     * This method discovers all translation tests within the current directory
     * as specified by the system property <code>current.directory</code>.
     */
    public void discoverTests()
    {
        if (this.translationTests.isEmpty())
        {
            this.discoverTests(this.currentDirectory);
        }
    }

    /**
     * This method discovers all translation tests within the
     * <code>currentDirectory</code>, it travels down the directory structure
     * looking for files that have a prefix of 'TranslationTest-'.
     */
    private void discoverTests(File currentDirectory)
    {
        final String methodName = "TranslationTestDiscoverer.discoverTests";
        try
        {
            String files[] = currentDirectory.list();
            if (files == null || files.length == 0)
            {
                if (logger.isDebugEnabled())
                    logger.debug("no files or directories found in directory '"
                        + currentDirectory + "'");
            }
            else
            {
                for (int i = 0; i < files.length; i++)
                {
                    File file = new File(currentDirectory, files[i]);
                    if (StringUtils.trimToEmpty(file.getName()).startsWith(
                        TEST_PREFIX))
                    {
                        URL testUrl = file.toURL();
                        if (logger.isInfoEnabled())
                            logger.info("found translation test --> '"
                                + testUrl + "'");

                        TranslationTest test = (TranslationTest)XmlObjectFactory
                            .getInstance(TranslationTest.class).getObject(
                                testUrl);
                        test.setUri(testUrl);
                        this.translationTests.put(test.getTranslation(), test);
                    }
                    else if (file.isDirectory())
                    {
                        this.discoverTests(file);
                    }
                }
            }
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new TranslationTestDiscovererException(errMsg, th);
        }
    }

    /**
     * Returns the TranslationTest for the given <code>translation</code> (if
     * one can be found), otherwise returns null.
     * 
     * @param translation the name of the translation
     * @return TranslationTest
     */
    public TranslationTest getTest(String translation)
    {
        return (TranslationTest)this.translationTests.get(StringUtils
            .trimToEmpty(translation));
    }

    /**
     * Returns the discovered translation tests keyed by
     * <code>translation<code>.
     */
    public Map getTests()
    {
        return this.translationTests;
    }
}