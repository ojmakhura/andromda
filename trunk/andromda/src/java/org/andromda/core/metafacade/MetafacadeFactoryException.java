package org.andromda.core.metafacade;

/**
 * Any unchecked exception that will be thrown when 
 * a MetafacadeFactory processing error occurs.
 */
public class MetafacadeFactoryException extends RuntimeException {

	/**
	 * Constructs an instance of MetafacadeFactoryException.
	 * 
	 * @param th
	 */
	public MetafacadeFactoryException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of MetafacadeFactoryException.
	 * 
	 * @param msg
	 */
	public MetafacadeFactoryException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of MetafacadeFactoryException.
	 * 
	 * @param msg
	 * @param th
	 */
	public MetafacadeFactoryException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}