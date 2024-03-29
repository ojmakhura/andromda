package org.andromda.translation.ocl.testsuite;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * Finds all translation tests from the current base directory up.
 *
 * @author Chad Brandon
 */
public class TranslationTestDiscoverer
{
    private static final Logger logger = Logger.getLogger(TranslationTestDiscoverer.class);

    /**
     * This is the prefix of translation tests, each translation test must start with this in order to be found.
     */
    private static final String TEST_PREFIX = "TranslationTest-";

    /**
     * Names of ignored files when file.isDirectory()
     */
    private static final List<String> IGNORED_DIRECTORIES = Arrays.asList("CVS", ".svn");

    /**
     * Stores the discovered translation tests.
     */
    private Map<String, TranslationTest> translationTests = new LinkedHashMap<String, TranslationTest>();

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
     * @param directory
     */
    public void discoverTests(final String directory)
    {
        if (StringUtils.isBlank(directory))
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
                    logger.debug("no files or directories found in directory '" + currentDirectory + '\'');
                }
            } else
            {
                for (String filename : files)
                {
                    File file = new File(currentDirectory, filename);
                    if (StringUtils.trimToEmpty(file.getName()).startsWith(TEST_PREFIX))
                    {
                        final URL testUrl = file.toURI().toURL();
                        if (logger.isInfoEnabled())
                        {
                            logger.info("found translation test --> '" + testUrl + '\'');
                        }

                        TranslationTest test =
                                (TranslationTest) XmlObjectFactory.getInstance(TranslationTest.class).getObject(testUrl);
                        test.setUri(testUrl);
                        this.translationTests.put(
                                test.getTranslation(),
                                test);
                    } else if (file.isDirectory() && !IGNORED_DIRECTORIES.contains(file.getName()))
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
        return this.translationTests.get(StringUtils.trimToEmpty(translation));
    }

    /**
     * Returns the discovered translation tests keyed by <code>translation<code>.
     * @return translationTests
     */
    public Map<String, TranslationTest> getTests()
    {
        return this.translationTests;
    }

    /**
     * Shuts down this instance and releases any resources.
     */
    public void shutdown()
    {
        this.translationTests.clear();
        TranslationTestDiscoverer.instance = null;
    }
}