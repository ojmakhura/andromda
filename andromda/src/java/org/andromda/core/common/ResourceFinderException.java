package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during
 * the execution of the ResourceFinder
 */
public class ResourceFinderException extends RuntimeException {

	/**
	 * Constructs an instance of ResourceFinderException.
	 * 
	 * @param th
	 */
	public ResourceFinderException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of ResourceFinderException.
	 * 
	 * @param msg
	 */
	public ResourceFinderException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of ResourceFinderException.
	 * 
	 * @param msg
	 * @param th
	 */
	public ResourceFinderException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
