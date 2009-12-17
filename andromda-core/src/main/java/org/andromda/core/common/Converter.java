package org.andromda.core.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;


/**
 * A class used for converting simple types to other types (i.e.
 * java.lang.String -> java.lang.Integer, etc).
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class Converter
{
    /**
     * The prefix of the 'valueOf' method available on wrapper classes.
     */
    private static final String VALUE_OF_METHOD_NAME = "valueOf";

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(Converter.class);

    /**
     * Attempts to convert the <code>object</code> to the <code>expectedType</code>.
     *
     * @param object the object to convert.
     * @param expectedType the type to which it should be converted.
     * @return the converted object
     */
    public static Object convert(
        Object object,
        Class expectedType)
    {
        Method method = null;
        try
        {
            if (expectedType == String.class)
            {
                object = object.toString();
            }
            else if (expectedType == Class.class)
            {
                object = ClassUtils.loadClass(object.toString());
            }
            else
            {
                final Class originalType = expectedType;
                if (expectedType.isPrimitive())
                {
                    expectedType = (Class)primitiveWrappers.get(expectedType);
                }
                try
                {
                    method = expectedType.getDeclaredMethod(
                            VALUE_OF_METHOD_NAME,
                            new Class[] {object.getClass()});
                    object = method.invoke(
                            expectedType,
                            object);
                }
                catch (final NoSuchMethodException exception)
                {
                    // - ignore
                }

                // - if we couldn't find the method try with the constructor
                if (method == null)
                {
                    Constructor constructor;
                    try
                    {
                        constructor = expectedType.getConstructor(new Class[] {originalType});
                        object = constructor.newInstance(object);
                    }
                    catch (final NoSuchMethodException exception)
                    {
                        throw new IntrospectorException("Could not convert '" + object + "' to type '" +
                            expectedType.getName() + '\'');
                    }
                }
            }
        }
        catch (Throwable throwable)
        {
            if (throwable.getCause()!=null)
            {
                throwable = throwable.getCause();
            }
            // At least output the location where the error happened, not the entire stack trace.
            StackTraceElement[] trace = throwable.getStackTrace();
            String location = " AT " + trace[0].getClassName() + '.' + trace[0].getMethodName() + ':' + trace[0].getLineNumber();
            if (throwable.getMessage()!=null)
            {
                location += ' ' + throwable.getMessage();
            }
            logger.error("Converter " + throwable + " invoking " + object + " METHOD " + method + " WITH " + expectedType.getName() + location);
            throw new IntrospectorException(throwable);
        }
        return object;
    }

    /**
     * Stores each primitive and its associated wrapper class.
     */
    private static final Map primitiveWrappers = new HashMap();

    /**
     * Initialize the primitiveWrappers.
     */
    static
    {
        primitiveWrappers.put(
            boolean.class,
            Boolean.class);
        primitiveWrappers.put(
            int.class,
            Integer.class);
        primitiveWrappers.put(
            long.class,
            Long.class);
        primitiveWrappers.put(
            short.class,
            Short.class);
        primitiveWrappers.put(
            byte.class,
            Byte.class);
        primitiveWrappers.put(
            float.class,
            Float.class);
        primitiveWrappers.put(
            double.class,
            Double.class);
        primitiveWrappers.put(
            char.class,
            Character.class);
    }
}