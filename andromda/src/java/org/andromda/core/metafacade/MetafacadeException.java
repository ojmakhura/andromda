package org.andromda.core.metafacade;

/**
 * Any unchecked exception that will be thrown when 
 * a Metafacade processing error occurs.
 */
public class MetafacadeException extends RuntimeException {

	/**
	 * Constructs an instance of MetafacadeException.
	 * 
	 * @param th
	 */
	public MetafacadeException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of MetafacadeException.
	 * 
	 * @param msg
	 */
	public MetafacadeException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of MetafacadeException.
	 * 
	 * @param msg
	 * @param th
	 */
	public MetafacadeException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}