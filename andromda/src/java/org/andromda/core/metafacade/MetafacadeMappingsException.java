package org.andromda.core.metafacade;

/**
 * Any unchecked exception that will be thrown when 
 * a MetafacadeMappings processing error occurs.
 */
public class MetafacadeMappingsException extends RuntimeException {

	/**
	 * Constructs an instance of MetafacadeMappingsException.
	 * 
	 * @param th
	 */
	public MetafacadeMappingsException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of MetafacadeMappingsException.
	 * 
	 * @param msg
	 */
	public MetafacadeMappingsException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of MetafacadeMappingsException.
	 * 
	 * @param msg
	 * @param th
	 */
	public MetafacadeMappingsException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}