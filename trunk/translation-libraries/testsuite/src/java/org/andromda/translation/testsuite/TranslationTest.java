package org.andromda.translation.testsuite;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Represents a TranslatorTest object loaded and
 * executed by the ExpressionTranslatorTest object.
 * 
 * @author Chad Brandon
 */
public class TranslationTest {
	
	private String translation;
	private Map expressionConfigs = new LinkedHashMap();
    private URL uri;
	
    /**
     * Sets the name of the translator for which this TranslationTest
     * will be used to test.
     * 
     * @param translation the name the translation to test.
     */
	public void setTranslation(String translation) {
		this.translation = translation;
	}   
	  
	/**
     * Returns the name of the translator, for which 
     * this TranslationTest will be used to test. 
     * 
	 * @return String
	 */
	public String getTranslation() {
		String methodName = "getTranslation";
		if(this.translation == null) {
			throw new TranslationTestProcessorException(methodName
				+ " - translation can not be null");
		}
		return this.translation;
	}
	
    /**
     * Adds an ExpressionTest to this TranslationTest.
     * 
     * @param config a ExpressionTest instance.
     */
	public void addExpression(ExpressionTest config) {
		this.expressionConfigs.put(config.getFrom(), config);
	}
	
    /**
     * Returns all the ExpressionTest objects
     * in a Map keyed by the from element body.
     * 
     * @return Map
     */
	public Map getExpressionConfigs() {
		return this.expressionConfigs;
	}

	/**
     * Gets the URI for the test which this TranslationTest was loaded from.
     * 
	 * @return Returns the uri.
	 */
	public URL getUri() {
		return uri;
	}

	/**
     * Sets the URI for the test which this TranslationTest was loaded from.
     * 
	 * @param uri The uri to set.
	 */
	protected void setUri(URL uri) {
		this.uri = uri;
	}

}
