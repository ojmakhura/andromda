package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during
 * the execution of the PluginDiscoverer
 */
public class PluginDiscovererException extends RuntimeException {

	/**
	 * Constructs an instance of PluginDiscovererException.
	 * 
	 * @param th
	 */
	public PluginDiscovererException(Throwable th){
		super(th);
	}
	
	/**
	 * Constructs an instance of PluginDiscovererException.
	 * 
	 * @param msg
	 */
	public PluginDiscovererException(String msg) {
		super(msg);	
	}
	
	/**
	 * Constructs an instance of PluginDiscovererException.
	 * 
	 * @param msg
	 * @param th
	 */
	public PluginDiscovererException(String msg, Throwable th) {
		super(msg, th);	
	}   
	
}
