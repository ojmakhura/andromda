package org.andromda.core.templateengine;

/**
 * Any unchecked exception that will be thrown when 
 * any processing by a TemplateEngine occurs..
 */
public class TemplateEngineException extends RuntimeException {

	/**
	 * Constructs an instance of TemplateEngineException.
	 * 
	 * @param th
	 */
	public TemplateEngineException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of TemplateEngineException.
	 * 
	 * @param msg
	 */
	public TemplateEngineException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of TemplateEngineException.
	 * 
	 * @param msg
	 * @param th
	 */
	public TemplateEngineException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}