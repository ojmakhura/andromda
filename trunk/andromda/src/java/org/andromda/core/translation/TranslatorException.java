package org.andromda.core.translation;


/**
 * Any unchecked exception that will be thrown when a translation exception occurs.
 */
public class TranslatorException extends RuntimeException {

	/**
	 * Constructs an instance of TranslatorException.
	 * 
	 * @param th
	 */
	public TranslatorException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of TranslatorException.
	 * 
	 * @param msg
	 */
	public TranslatorException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of TranslatorException.
	 * 
	 * @param msg
	 * @param th
	 */
	public TranslatorException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
