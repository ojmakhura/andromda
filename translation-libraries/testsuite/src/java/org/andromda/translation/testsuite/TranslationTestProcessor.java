package org.andromda.translation.testsuite;

import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.core.translation.TranslationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * This object is used to test Translations during 
 * development.
 */
public class TranslationTestProcessor extends TestCase {
	
	private static Logger logger = Logger.getLogger(TranslationTestProcessor.class);
    
    /**
     * If this is specified as a system property, then 
     * the TraceTranslator will run instead of the specified translator.
     * This is helpful, in allowing us to see which expressions are being 
     * parsed in what order, etc.
     */
    private boolean useTraceTranslator = 
        StringUtils.isNotEmpty(System.getProperty("trace.expression"));
        
    /**
     * If this is specified as a system property, then 
     * the only this translation will be tested (If .more than one
     * TestTranslation-* file is found)
     */
    private String translationName =
        StringUtils.trimToEmpty(System.getProperty("translation.name"));
    
    /**
     * Flag indicating whether or not we want to perform model validation
     * when running the tests.
     */
    private boolean modelValidation = 
        Boolean.valueOf(
            StringUtils.trimToEmpty(
                System.getProperty("model.validation"))).booleanValue();
	
	private ModelAccessFacade model = null;
    
    /**
     * In charge of discovering the translation tests.
     */
    private static final TranslationTestDiscoverer testDiscoverer = 
        TranslationTestDiscoverer.instance();
	
	/**
	 * The translation that is currently being tested.
	 */
	private String testTranslation = null;
	
	/**
	 * Basic constructor - called by the test runners.
	 */
	private TranslationTestProcessor(String testName) {
		super(testName);	    	
	}                  
	
	/**   
	 * Assembles test suite if all known tests
	 * @return non-null test suite
	 */
	public static TestSuite suite() throws Exception {	
		XmlObjectFactory.setDefaultValidating(false);
        ExpressionTranslator.instance().initialize();
        testDiscoverer.discoverTests();
        Map tests = testDiscoverer.getTests();        
		TestSuite suite = new TestSuite();
		Iterator testIt = tests.keySet().iterator();
		while (testIt.hasNext()) {
			TranslationTestProcessor unitTest = new TranslationTestProcessor("testTranslation");
			unitTest.setTestTranslation((String)testIt.next());
			suite.addTest(unitTest);
		}	
		return suite;
	}
	
	/**
	 * Sets the value for the test translation which is the translation
	 * that will be tested.
	 * @param testTranslation
	 */
	private void setTestTranslation(String testTranslation) {
		this.testTranslation = testTranslation;
	}

	/**
	 * Finds the classifier having <code>fullyQualifiedName</code> in the model.
	 * @param translation the translation we're using
     * @param expression the expression from which we'll find the model element.
	 * @return Object the found model element.
	 */
	protected Object findModelElement(String translation, String expression) {
		final String methodName = "TranslationTestProcessor.findClassifier";
		Object element = null;
		if(StringUtils.isNotEmpty(expression)) {
			model = ModelLoader.instance().getModel();
			if(model == null) {
				throw new RuntimeException(methodName 
					+ " - model can not be null");
			}
            
            ContextElementFinder finder = new ContextElementFinder(model);
            finder.translate(translation, null, expression);
            element = finder.getContextElement();

			if(element == null) {
                String errMsg = 
                    "No element found in model in expression --> '" 
                    + expression
                    + "', please check your model or your TranslationTest file";
                logger.error("ERROR! " + errMsg);
                TestCase.fail(errMsg);
			}
		}
		return element;
	}
	
	/**
	 * Tests the current translation set in the currentTestTranslation
	 * property.
	 */
	public void testTranslation() {
		
		String translation = this.testTranslation;
        
        if(this.shouldTest(translation)) {
		
    		if(logger.isInfoEnabled()) {
    			logger.info("testing translation --> '" + translation + "'");
    		}
    		
    		TranslationTest test = testDiscoverer.getTest(translation);
    		
    		Map expressions = test.getExpressionConfigs();
            
    		if(expressions != null) {
    			Iterator expressionIt = expressions.keySet().iterator();
    			while (expressionIt.hasNext()) {
                    
                    String fromExpression = (String)expressionIt.next();
                    
                    //if the fromExpression body isn't defined, skip expression test
                    if(StringUtils.isEmpty(fromExpression)) {
                        if(logger.isInfoEnabled()) {
                            logger.info("No body for the 'from' element was defined "
                                + "within translation test --> '" 
                                + test.getUri() 
                                + "', please define the body of this element with " 
                                + "the expression you want to translate from");
                        }
                        continue;
                    }
                     
    				Expression translated;
    				if(useTraceTranslator) {
    					translated = TraceTranslator.getInstance().translate(translation, null, fromExpression);
    				} else {
    				
    					ExpressionTest expressionConfig = 
    						(ExpressionTest)expressions.get(fromExpression);
    					String toExpression = expressionConfig.getTo();
    				
                        Object modelElement = null;
                        // only find the model element if modelValidation
                        // is set to true
                        if (this.modelValidation) {
                            modelElement = this.findModelElement(
                                translation,
                                fromExpression);
                        }
                        
    					translated = ExpressionTranslator.instance().translate(
    						translation, 
    						modelElement, 
							fromExpression);	
    					
                        if(translated != null) {
        					//remove the extra whitespace from both so as to have an accurrate comarison
        					toExpression = TranslationUtils.removeExtraWhitespace(toExpression);
        					if(logger.isInfoEnabled()) {
        						logger.info("translated: --> '" + translated.getTranslatedExpression() + "'");
        						logger.info("expected:   --> '" + toExpression + "'");
        					}
        					TestCase.assertEquals(toExpression, translated.getTranslatedExpression());
                        }
                    }
				}
    		}
        } else {
        	if(logger.isInfoEnabled()) {
        		logger.info("skipping translation --> '" + translation + "'");
            }
        }
	}    
    
    /**
     * This method returns true if we should allow the translation
     * to be tested. This is so we can specify on the command line,
     * the translation to be tested, if we don't want all to be tested.
     * @param translation
     * @return boolean
     */
    private boolean shouldTest(String translation) {
        translation = StringUtils.trimToEmpty(translation);
        return StringUtils.isEmpty(this.translationName) || 
            (StringUtils.isNotEmpty(this.translationName) && 
             this.translationName.equals(translation));
    }
	
	/** 
	 * Runs the test suite 
	 */
	public static void main(String[] args) {
		try {
			junit.textui.TestRunner.run(suite());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}
}
