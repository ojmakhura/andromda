package org.andromda.utils.beans.comparators;


/**
 * Any unchecked exception when dealing with the comparators
 * will throw this exception
 */
public class ComparatorException extends RuntimeException {

	public ComparatorException(Throwable th){
		super(th);
	}
	
	public ComparatorException(String msg) {
		super(msg);	
	}
	
	public ComparatorException(String msg, Throwable th) {
		super(msg, th);	
	}
	
}
