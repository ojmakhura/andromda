package org.andromda.translation.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Dynamically invokes operation and property calls on specified
 * <strong>elements</code>.
 *
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class OCLIntrospector
{

    private static final Logger logger = Logger
        .getLogger(OCLIntrospector.class);

    /**
     * Used to match on operation feature patterns, which helps us to determine
     * whether or not to invoke the feature call as a property or operation on
     * an element.
     */
    public static final String OPERATION_FEATURE = ".*\\(.*\\).*";

    /**
     * Invokes the given <code>feature</code> on the <code>element</code>.
     * Its expected that the feature is either an operation or a property.
     */
    public static Object invoke(Object element, String feature)
    {
        try
        {
            feature = StringUtils.trimToEmpty(feature);
            if (feature.matches(OPERATION_FEATURE))
            {
                return invoke(element, feature, null);
            }
            return getNestedProperty(element, feature);

        }
        catch (Throwable th)
        {
            final String errMsg = "Error invoking feature '" + feature
                + "' on element '" + element + "'";
            th = ExceptionUtils.getRootCause(th);
            logger.error(errMsg, th);
            throw new OCLIntrospectorException(th);
        }
    }

    /**
     * Invokes the given <code>feature</code> on the specified
     * <code>element</code> taking the given <code>arguments</code>. If
     * <code>arguments</code> is null its expected that the feature is an
     * empty operation.
     */
    public static Object invoke(
        Object element,
        String feature,
        Object[] arguments)
    {
        try
        {
            // check for parenthesis
            int parenIndex = feature.indexOf('(');
            if (parenIndex != -1)
            {
                feature = feature.substring(0, parenIndex).trim();
            }
            return invokeMethod(element, feature, arguments);
        }
        catch (Throwable th)
        {
            final String errMsg = "Error invoking feature '" + feature
                + "' on element '" + element + "' with arguments '"
                + StringUtils.join(arguments, ',') + "'";
            th = ExceptionUtils.getRootCause(th);
            logger.error(errMsg, th);
            throw new OCLIntrospectorException(th);
        }
    }

    private static Object getNestedProperty(Object element, String propertyName) throws Exception
    {
        Object property = null;

        if (element != null && propertyName != null && propertyName.length() > 0)
        {
            int dotIndex = propertyName.indexOf('.');
            if (dotIndex == -1)
            {
                property = getProperty(element, propertyName);
            }
            else
            {
                if (dotIndex >= propertyName.length())
                {
                    throw new Exception("Malformed property call: "+propertyName);
                }
                Object nextInstance = getProperty(element, propertyName.substring(0, dotIndex));
                property = getNestedProperty(nextInstance, propertyName.substring(dotIndex+1));
            }
        }

        return property;
    }

    private static Object getProperty(Object element, String propertyName) throws Exception
    {
        Object property = null;
        boolean tryNextCall = true;

        if (element != null || propertyName != null || propertyName.length() > 0)
        {
            try
            {
                property = invokeGet(element, propertyName);
                tryNextCall = false;
            }
            catch (NoSuchMethodException e)
            {
                // try next call
            }
            catch (InvocationTargetException e)
            {
                // try next call
            }
            catch (IllegalAccessException e)
            {
                // try next call
            }
            catch (SecurityException e)
            {
                // try next call
            }

            if (tryNextCall)
            {
                try
                {
                    property = invokeIs(element, propertyName);
                    tryNextCall = false;
                }
                catch (NoSuchMethodException e)
                {
                    // try next call
                }
                catch (InvocationTargetException e)
                {
                    // try next call
                }
                catch (IllegalAccessException e)
                {
                    // try next call
                }
                catch (SecurityException e)
                {
                    // try next call
                }
            }

            if (tryNextCall)
            {
                try
                {
                    property = invokeField(element, propertyName);
                    tryNextCall = false;
                }
                catch (NoSuchFieldException e)
                {
                    // try next call
                }
                catch (IllegalAccessException e)
                {
                    // try next call
                }
                catch (SecurityException e)
                {
                    // try next call
                }
            }
        }

        return property;
    }

    private static Object invokeGet(Object element, String propertyName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SecurityException
    {
        Method method = element.getClass().getMethod("get"+StringUtils.capitalize(propertyName), null);
        return method.invoke(element, null);
    }

    private static Object invokeIs(Object element, String propertyName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SecurityException
    {
        Method method = element.getClass().getMethod("is"+StringUtils.capitalize(propertyName), null);
        return method.invoke(element, null);
    }

    private static Object invokeField(Object element, String propertyName)
            throws NoSuchFieldException, IllegalAccessException, SecurityException
    {
        Field field = element.getClass().getField(propertyName);
        return field.get(element);
    }


    private static Object invokeMethod(Object element, String methodName, Object[] arguments) throws Exception
    {
        Object property = null;

        if (element != null && methodName != null && methodName.length() > 0)
        {
            Class[] argumentTypes = getObjectTypes(arguments);

            Method method = element.getClass().getMethod(methodName, argumentTypes);
            property = method.invoke(element, arguments);
        }

        return property;
    }

    private static Class[] getObjectTypes(Object[] objects)
    {
        Class[] objectTypes = null;

        if (objects != null)
        {
            objectTypes = new Class[objects.length];
            for (int i = 0; i < objects.length; i++)
            {
                Object object = objects[i];
                if (object != null)
                {
                    objectTypes[i] = object.getClass();
                }
            }
        }

        return objectTypes;
    }
}
