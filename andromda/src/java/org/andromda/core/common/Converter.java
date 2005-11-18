package org.andromda.core.common;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;


/**
 * A class used for converting simple types to other types (i.e.
 * java.lang.String -> java.lang.Integer, etc).
 *
 * @author Chad Brandon
 */
public class Converter
{
    /**
     * The prefix of the 'valueOf' method available on wrapper classes.
     */
    private static final String VALUE_OF_METHOD_NAME = "valueOf";

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
        try
        {
            if (expectedType == String.class)
            {
                object = object.toString();
            }
            else
            {
                if (expectedType.isPrimitive())
                {
                    expectedType = (Class)primitiveWrappers.get(expectedType);
                }
                Method valueOfMethod = null;
                try
                {
                    valueOfMethod =
                        expectedType.getDeclaredMethod(
                            VALUE_OF_METHOD_NAME,
                            new Class[] {object.getClass()});
                }
                catch (final NoSuchMethodException exception)
                {
                    throw new IntrospectorException("Could not convert '" + object + "' to type '" +
                        expectedType.getName() + "'");
                }
                object = valueOfMethod.invoke(
                        expectedType,
                        new Object[] {object});
            }
        }
        catch (final Throwable throwable)
        {
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