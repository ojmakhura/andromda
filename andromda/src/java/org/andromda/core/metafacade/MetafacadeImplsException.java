package org.andromda.core.metafacade;

/**
 * Any unchecked exception that will be thrown when 
 * a MetafacadeImpls processing error occurs.
 */
public class MetafacadeImplsException extends RuntimeException {

	/**
	 * Constructs an instance of MetafacadeImplsException.
	 * 
	 * @param th
	 */
	public MetafacadeImplsException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of MetafacadeImplsException.
	 * 
	 * @param msg
	 */
	public MetafacadeImplsException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of MetafacadeImplsException.
	 * 
	 * @param msg
	 * @param th
	 */
	public MetafacadeImplsException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}