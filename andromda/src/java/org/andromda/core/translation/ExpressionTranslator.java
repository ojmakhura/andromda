package org.andromda.core.translation;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.StdoutLogger;
import org.andromda.core.translation.library.LibraryTranslation;
import org.andromda.core.translation.library.LibraryTranslationFinder;
import org.apache.log4j.Logger;

/**
 * The <strong>OCL</strong> translator class that all translations
 * are performed through. This is the entry point to OCL translation.
 * 
 * @author Chad Brandon
 */
public class ExpressionTranslator {
	
	private static Logger logger = Logger.getLogger(ExpressionTranslator.class);
    
    private static ExpressionTranslator translator = new ExpressionTranslator();
    
    /**
     * Gets the shared ExpressionTranslator instance.
     * 
     * @return ExpressionTranslator.
     */
    public static ExpressionTranslator instance() {
    	return translator;
    }
    
    /**
     * Initializes the ExpressionTranslator. This 
     * <strong>MUST</strong> be called to find and
     * loal all available translation-libraries.
     */
    public void initialize() {
        //configure the logger
        StdoutLogger.configure(); 
        //discover plugins
        PluginDiscoverer.instance().discoverPlugins();      
    }
    
	/**
	 * Performs translation of the <code>expression</code> by looking 
     * up the <code>translationName</code> from the available 
     * Translation-Libraries found on the classpath.  
     * 
     * @param translationName the name of the translation to use for translating
     * (i.e. a translationName like 'query.EJB-QL' would mean use the <code>EJB-QL</code> 
     * translation from the <code>query</code> library.
     * @param contextElement the element which provides the context of this expression. This
     *        is passed from the model.  This can be null.
     * @param expression the actual expression to translate.
     * 
     * @return Expression the resulting expression instance which contains 
     *         the translated expression as well as additional information
     *         about the expression.
	 */
	public Expression translate(
		String translationName, 
        Object contextElement, 
        String expression) {
		final String methodName = "ExpressionTranslator.translate";
		ExceptionUtils.checkEmpty(methodName, "translationName", translationName);
		ExceptionUtils.checkEmpty(methodName, "expression", expression);
		
		Expression translatedOcl = null;
		try {
			LibraryTranslation libraryTranslation = 
				LibraryTranslationFinder.findLibraryTranslation(translationName);
			
			if (libraryTranslation != null) {
				Translator translator = libraryTranslation.getTranslator();
				translatedOcl = 
                    translator.translate(
                        translationName, 
                        contextElement, 
                        expression);
			} else {
				logger.error("ERROR! No translation found with name --> '" 
                    + translationName + "'");
			}
			
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName 
				+ " with translationName '" + translationName 
				+ "', contextElement '" 
				+ contextElement 
				+ "' and expression '" 
				+ expression + "'";
			logger.error(errMsg, ex);
 			throw new TranslatorException(errMsg, ex);
		}
		return translatedOcl;
	}
	
	
	
}