package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during
 * the execution of ComponentContainer.
 */
public class ComponentContainerException extends RuntimeException {

	/**
	 * Constructs an instance of ComponentContainerException.
	 * 
	 * @param th
	 */
	public ComponentContainerException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of ComponentContainerException.
	 * 
	 * @param msg
	 */
	public ComponentContainerException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of ComponentContainerException.
	 * 
	 * @param msg
	 * @param th
	 */
	public ComponentContainerException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
