
package org.andromda.core.cartridge;


/**
 * Wraps any exception that occurs when configuring/processing a 
 * template object
 * 
 * @see org.andromda.core.cartridge.TemplateObject
 */
public class TemplateObjectException extends RuntimeException {

	/**
	 * Constructs an instance of TemplateObjectException.
	 * 
	 * @param th
	 */
	public TemplateObjectException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of TemplateObjectException.
	 * 
	 * @param msg
	 */
	public TemplateObjectException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of TemplateObjectException.
	 * 
	 * @param msg
	 * @param th
	 */
	public TemplateObjectException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
