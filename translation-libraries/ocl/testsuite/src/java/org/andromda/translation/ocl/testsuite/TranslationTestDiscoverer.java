package org.andromda.translation.ocl.testsuite;

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
    private static Logger logger = Logger.getLogger(TranslationTestDiscoverer.class);

    /**
     * This is the prefix of translation tests, each translation test must start with this in order to be found.
     */
    private static final String TEST_PREFIX = "TranslationTest-";

    /**
     * Stores the discovered translation tests.
     */
    private Map translationTests = new LinkedHashMap();

    /**
     * The shared instance
     */
    private static TranslationTestDiscoverer instance;

    /**
     * Gets the shared instance of this TranslationTestDiscoverer.
     *
     * @return the shared TranslationTestDiscoverer.
     */
    public static TranslationTestDiscoverer instance()
    {
        if (instance == null)
        {
            instance = new TranslationTestDiscoverer();
        }
        return instance;
    }

    /**
     * This method discovers all translation tests within the given <code>directory</code>.
     */
    public void discoverTests(final String directory)
    {
        if (directory == null || directory.trim().length() == 0)
        {
            throw new TranslationTestDiscovererException("The 'directory' " +
                " was not specified, please specify this value with the location from which to" +
                " begin the discovery of translation test files");
        }
        if (this.translationTests.isEmpty())
        {
            this.discoverTests(new File(directory));
        }
    }

    /**
     * This method discovers all translation tests within the <code>currentDirectory</code>, it travels down the
     * directory structure looking for files that have a prefix of 'TranslationTest-'.
     */
    private void discoverTests(final File currentDirectory)
    {
        try
        {
            final String[] files = currentDirectory.list();
            if (files == null || files.length == 0)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("no files or directories found in directory '" + currentDirectory + "'");
                }
            }
            else
            {
                for (int ctr = 0; ctr < files.length; ctr++)
                {
                    File file = new File(currentDirectory, files[ctr]);
                    if (StringUtils.trimToEmpty(file.getName()).startsWith(TEST_PREFIX))
                    {
                        final URL testUrl = file.toURL();
                        if (logger.isInfoEnabled())
                        {
                            logger.info("found translation test --> '" + testUrl + "'");
                        }

                        TranslationTest test =
                            (TranslationTest)XmlObjectFactory.getInstance(TranslationTest.class).getObject(testUrl);
                        test.setUri(testUrl);
                        this.translationTests.put(
                            test.getTranslation(),
                            test);
                    }
                    else if (file.isDirectory())
                    {
                        this.discoverTests(file);
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            logger.error(throwable);
            throw new TranslationTestDiscovererException(throwable);
        }
    }

    /**
     * Returns the TranslationTest for the given <code>translation</code> (if one can be found), otherwise returns
     * null.
     *
     * @param translation the name of the translation
     * @return TranslationTest
     */
    public TranslationTest getTest(String translation)
    {
        return (TranslationTest)this.translationTests.get(StringUtils.trimToEmpty(translation));
    }

    /**
     * Returns the discovered translation tests keyed by <code>translation<code>.
     */
    public Map getTests()
    {
        return this.translationTests;
    }

    /**
     * Shuts down this instance and releases any resources.
     */
    public void shutdown()
    {
        this.translationTests.clear();
        instance = null;
    }
}