package org.andromda.core.translation;


/**
 * Any unchecked exception that will be thrown when an Expression exception occurs.
 */
public class ExpressionException extends RuntimeException {

	/**
	 * Constructs an instance of ExpressionException.
	 * 
	 * @param th
	 */
	public ExpressionException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of ExpressionException.
	 * 
	 * @param msg
	 */
	public ExpressionException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of ExpressionException.
	 * 
	 * @param msg
	 * @param th
	 */
	public ExpressionException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
