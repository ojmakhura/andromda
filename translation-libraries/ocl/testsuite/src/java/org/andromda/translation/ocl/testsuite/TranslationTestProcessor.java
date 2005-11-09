package org.andromda.translation.ocl.testsuite;

import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.andromda.core.AndroMDA;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Repository;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.Repositories;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.core.translation.TranslationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * This object is used to test Translations during development.
 *
 * @author Chad Brandon
 */
public class TranslationTestProcessor
    extends TestCase
{
    private static Logger logger = Logger.getLogger(TranslationTestProcessor.class);

    /**
     * The shared instance of this class.
     */
    private static TranslationTestProcessor instance;

    /**
     * Gets the shared instance of this class.
     *
     * @return the shared instance of this class.
     */
    public static final TranslationTestProcessor instance()
    {
        if (instance == null)
        {
            instance = new TranslationTestProcessor();
        }
        return instance;
    }

    private TranslationTestProcessor()
    {
        super();
    }

    /**
     * Sets whether or not to use the trace translator.
     *
     * @param useTraceTranslator true/false
     */
    public void setUseTraceTranslator(final boolean useTraceTranslator)
    {
        this.useTraceTranslator = useTraceTranslator;
    }

    /**
     * Indicates whether or not the TraceTranslator will run instead
     * of the specified translator. This is helpful, in allowing us to see which
     * expressions are being parsed in what order, etc.
     */
    private boolean useTraceTranslator;

    /**
     * Thbe name of the translation to test.
     */
    private String translationName;

    /**
     * Sets the name of the translation to test.
     *
     * @param translationName the name of the translation to test.
     */
    public void setTranslationName(final String translationName)
    {
        this.translationName = translationName;
    }

    /**
     * The location of the directory that contains the test source.
     */
    private String testSourceDirectory;
    ;

    /**
     * Sets the location of the directory that contains the test souce.
     *
     * @param testSourceDirectory
     */
    public void setTestSourceDirectory(final String testSourceDirectory)
    {
        this.testSourceDirectory = testSourceDirectory;
    }

    /**
     * Handles the discovering of the translation tests.
     */
    private static final TranslationTestDiscoverer testDiscoverer = TranslationTestDiscoverer.instance();

    /**
     * The translation that is currently being tested.
     */
    private String testTranslation = null;

    /**
     * Basic constructor - called by the test runners.
     */
    private TranslationTestProcessor(String testName)
    {
        super(testName);
    }

    /**
     * The test result
     */
    private TestResult testResult;

    /**
     * Sets the test result in which the result of the run will be stored.
     *
     * @param testResult the test result instance.
     */
    public void setResult(final TestResult testResult)
    {
        this.testResult = testResult;
    }

    /**
     * Runs the test suite.
     *
     * @see junit.framework.TestCase#run()
     */
    public void runSuite()
    {
        if (this.testResult == null)
        {
            throw new TranslationTestProcessorException(
                "You must set the test result before attempting to run the suite");
        }
        final AndroMDA andromda = AndroMDA.newInstance();
        MetafacadeFactory factory = MetafacadeFactory.getInstance();
        andromda.initialize(this.configuration);
        factory.setNamespace(Namespaces.DEFAULT);
        if (this.model == null)
        {
            final Repositories repositoriesContainer = Repositories.instance();
            final Repository[] repositories = this.configuration.getRepositories();
            if (repositories != null && repositories.length > 0)
            {
                final int numberOfRepositories = repositories.length;
                for (int ctr = 0; ctr < numberOfRepositories; ctr++)
                {
                    final Repository repository = repositories[ctr];
                    final Model[] models = repository.getModels();
                    if (models != null)
                    {
                        // - we just load only the first model (since it doesn't
                        // make sense
                        // to test with more than one model)
                        repositoriesContainer.loadModel(models[0]);
                        final RepositoryFacade repositoryImplementation =
                            repositoriesContainer.getImplementation(repository.getName());
                        this.model = repositoryImplementation.getModel();

                        // - make sure the factory has access to the model
                        factory.setModel(this.model);
                    }
                }
            }
        }
        this.getSuite().run(this.testResult);
        andromda.shutdown();
    }

    /**
     * Assembles and retrieves the test suite of all known transation-library tests.
     *
     * @return non-null test suite
     */
    private TestSuite getSuite()
    {
        testDiscoverer.discoverTests(this.testSourceDirectory);
        final Map tests = testDiscoverer.getTests();
        final TestSuite suite = new TestSuite();
        for (final Iterator iterator = tests.keySet().iterator(); iterator.hasNext();)
        {
            final TranslationTestProcessor unitTest = new TranslationTestProcessor("testTranslation");

            // - pass on the variables to each test
            unitTest.setConfiguration(this.configuration);
            unitTest.setTestTranslation((String)iterator.next());
            unitTest.model = this.model;
            suite.addTest(unitTest);
        }
        return suite;
    }

    private Configuration configuration;

    /**
     * Sets AndroMDA configuration instance.
     *
     * @param configuration the AndroMDA configuration instance.
     */
    public void setConfiguration(final Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Sets the value for the test translation which is the translation that
     * will be tested.
     *
     * @param testTranslation
     */
    private void setTestTranslation(String testTranslation)
    {
        this.testTranslation = testTranslation;
    }

    /**
     * The model that was loaded.
     */
    private ModelAccessFacade model;

    /**
     * Finds the classifier having <code>fullyQualifiedName</code> in the
     * model.
     *
     * @param translation the translation we're using
     * @param expression the expression from which we'll find the model element.
     * @return Object the found model element.
     */
    protected Object findModelElement(
        String translation,
        String expression)
    {
        final String methodName = "TranslationTestProcessor.findClassifier";
        Object element = null;
        if (StringUtils.isNotEmpty(expression))
        {
            if (this.model == null)
            {
                throw new RuntimeException(methodName + " could not retrieve model from repository");
            }

            ContextElementFinder finder = new ContextElementFinder(model);
            finder.translate(
                translation,
                expression,
                null);
            element = finder.getContextElement();

            if (element == null)
            {
                final String message =
                    "No element found in model in expression --> '" + expression +
                    "', please check your model or your TranslationTest file";
                logger.error("ERROR! " + message);
                TestCase.fail(message);
            }
        }
        return element;
    }

    /**
     * Tests the current translation set in the currentTestTranslation property.
     */
    public void testTranslation()
    {
        String translation = this.testTranslation;

        if (this.shouldTest(translation))
        {
            if (logger.isInfoEnabled())
            {
                logger.info("testing translation --> '" + translation + "'");
            }

            TranslationTest test = testDiscoverer.getTest(translation);

            Map expressions = test.getExpressionConfigs();

            if (expressions != null)
            {
                Iterator expressionIt = expressions.keySet().iterator();
                while (expressionIt.hasNext())
                {
                    String fromExpression = (String)expressionIt.next();

                    // if the fromExpression body isn't defined, skip expression
                    // test
                    if (StringUtils.isEmpty(fromExpression))
                    {
                        if (logger.isInfoEnabled())
                        {
                            logger.info(
                                "No body for the 'from' element was defined " + "within translation test --> '" +
                                test.getUri() + "', please define the body of this element with " +
                                "the expression you want to translate from");
                        }
                        continue;
                    }

                    Expression translated;
                    if (useTraceTranslator)
                    {
                        translated = TraceTranslator.getInstance().translate(
                                translation,
                                fromExpression,
                                null);
                    }
                    else
                    {
                        final ExpressionTest expressionConfig = (ExpressionTest)expressions.get(fromExpression);
                        String toExpression = expressionConfig.getTo();

                        Object modelElement = null;

                        // - only find the model element if we have a model
                        // defined in our AndroMDA configuration
                        final Repository[] repositories = this.configuration.getRepositories();
                        if (repositories != null && repositories.length > 0)
                        {
                            modelElement = this.findModelElement(
                                    translation,
                                    fromExpression);
                        }
                        else
                        {
                            logger.info("No repositories defined in configuration, not finding for model elements");
                        }

                        translated =
                            ExpressionTranslator.instance().translate(
                                translation,
                                fromExpression,
                                modelElement);

                        if (translated != null)
                        {
                            // remove the extra whitespace from both so as to
                            // have an accurrate comarison
                            toExpression = TranslationUtils.removeExtraWhitespace(toExpression);
                            if (logger.isInfoEnabled())
                            {
                                logger.info("translated: --> '" + translated.getTranslatedExpression() + "'");
                                logger.info("expected:   --> '" + toExpression + "'");
                            }
                            TestCase.assertEquals(
                                toExpression,
                                translated.getTranslatedExpression());
                        }
                    }
                }
            }
        }
        else
        {
            if (logger.isInfoEnabled())
            {
                logger.info("skipping translation --> '" + translation + "'");
            }
        }
    }

    /**
     * This method returns true if we should allow the translation to be tested.
     * This is so we can specify on the command line, the translation to be
     * tested, if we don't want all to be tested.
     *
     * @param translation
     * @return boolean
     */
    private boolean shouldTest(String translation)
    {
        translation = StringUtils.trimToEmpty(translation);
        return StringUtils.isEmpty(this.translationName) ||
        (StringUtils.isNotEmpty(this.translationName) && this.translationName.equals(translation));
    }
    
    /**
     * Shuts down this instance.
     */
    public void shutdown()
    {
        testDiscoverer.shutdown();
        instance = null;
    }
}