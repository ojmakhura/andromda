
package org.andromda.core.mapping;


/**
 * Any error occurring during processing of Mappings should
 * through this exception.
 */
public class MappingsException extends RuntimeException {

	/**
	 * Constructs an instance of MappingsException.
	 * 
	 * @param th
	 */
	public MappingsException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of MappingsException.
	 * 
	 * @param msg
	 */
	public MappingsException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of MappingsException.
	 * 
	 * @param msg
	 * @param th
	 */
	public MappingsException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
