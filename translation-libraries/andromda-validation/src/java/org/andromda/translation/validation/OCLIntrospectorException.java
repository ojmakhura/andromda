package org.andromda.translation.validation;

import org.apache.commons.lang.exception.ExceptionUtils;


/**
 * Thrown when any unexpected error occurs during execution
 * of the OCLIntrospector.
 * 
 * @author Chad Brandon
 */
public class OCLIntrospectorException
    extends RuntimeException
{

    /**
     * Constructs an instance of OCLIntrospectorException
     * taking the <code>parent</code> Throwable.
     * 
     * @param parent
     */
    public OCLIntrospectorException(Throwable parent)
    {
        super(parent);
    }
     
}
