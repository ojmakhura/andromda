
package org.andromda.cartridges.interfaces;


/**
 * Wraps any unexpected exception when using a
 * TemplateConfiguration instance.
 * 
 * @see org.andromda.cartridges.interfaces.TemplateConfiguration
 */
public class TemplateConfigurationException extends RuntimeException {

	/**
	 * Constructs an instance of TemplateConfigurationException.
	 * 
	 * @param th
	 */
	public TemplateConfigurationException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of TemplateConfigurationException.
	 * 
	 * @param msg
	 */
	public TemplateConfigurationException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of TemplateConfigurationException.
	 * 
	 * @param msg
	 * @param th
	 */
	public TemplateConfigurationException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
