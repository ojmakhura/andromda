package org.andromda.core.common;

/**
 * Any unchecked exception that will be thrown during execution
 * of the XmlObjectFactory.
 */
public class XmlObjectFactoryException extends RuntimeException {

	/**
	 * Constructs an instance of XmlObjectFactoryException.
	 * 
	 * @param th
	 */
	public XmlObjectFactoryException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of XmlObjectFactoryException.
	 * 
	 * @param msg
	 */
	public XmlObjectFactoryException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of XmlObjectFactoryException.
	 * 
	 * @param msg
	 * @param th
	 */
	public XmlObjectFactoryException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}