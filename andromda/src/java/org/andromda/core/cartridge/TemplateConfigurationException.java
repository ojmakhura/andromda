
package org.andromda.core.cartridge;


/**
 * Wraps any unexpected exception when using a
 * TemplateConfiguration instance.
 * 
 * @see org.andromda.core.cartridge.TemplateConfiguration
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
