package org.andromda.core;


/**
 * Any unchecked exception that will be thrown during
 * the execution of the ModelProcessor
 */
public class ModelProcessorException extends RuntimeException {

	/**
	 * Constructs an instance of ModelProcessorException.
	 * 
	 * @param th
	 */
	public ModelProcessorException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of ModelProcessorException.
	 * 
	 * @param msg
	 */
	public ModelProcessorException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of ModelProcessorException.
	 * 
	 * @param msg
	 * @param th
	 */
	public ModelProcessorException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
