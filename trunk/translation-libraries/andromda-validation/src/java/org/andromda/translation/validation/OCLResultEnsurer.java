package org.andromda.translation.validation;

/**
 * <p>
 *    Ensures that the result of the OCL expression is a boolean value. This is
 *    necessary because some expressions' results will actually be wrapped in an
 *    instance of <code>java.lang.Object</code> because of the fact that
 *    OCLIntropector is used.
 * </p>
 * 
 * @author Chad Brandon
 */
public class OCLResultEnsurer
{
    /**
     * Does nothing but return the passed in <code>result</code> argument.
     * 
     * @param result the result.
     */
    public static boolean ensure(boolean result)
    {
        return result;
    }

    /**
     * Converts the passed in <code>result</code> to a 
     * <code>boolean</code> value and returns it. If
     * <code>result</code> is null, false will be assumed.
     * 
     * @param result
     * @return
     */
    public static boolean ensure(Object result)
    {
        return result != null
            && Boolean.valueOf(result.toString()).booleanValue();
    }
}