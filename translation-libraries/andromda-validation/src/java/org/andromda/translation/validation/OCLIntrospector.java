package org.andromda.translation.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

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
            return getProperty(element, feature);

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

    private static Object getProperty(Object element, String propertyName) throws Exception
    {
        Object property = null;

        if (element != null || property != null || propertyName.length() > 0)
        {
            property = invokeGet(element, propertyName);

            if (property == null)
            {
                property = invokeIs(element, propertyName);
            }

            if (property == null)
            {
                property = invokeField(element, propertyName);
            }
        }

        return property;
    }

    private static Object invokeGet(Object element, String propertyName) throws Exception
    {
        Object property = null;

        try
        {
            Method method = element.getClass().getMethod("get"+StringUtils.capitalize(propertyName), null);
            property = method.invoke(element, null);
        }
        catch (NoSuchMethodException e)
        {
            // not a prob
        }
        catch (InvocationTargetException e)
        {
            // not a prob
        }

        return property;
    }

    private static Object invokeIs(Object element, String propertyName) throws Exception
    {
        Object property = null;

        try
        {
            Method method = element.getClass().getMethod("is"+StringUtils.capitalize(propertyName), null);
            property = method.invoke(element, null);
        }
        catch (NoSuchMethodException e)
        {
            // not a prob
        }
        catch (InvocationTargetException e)
        {
            // not a prob
        }

        return property;
    }

    private static Object invokeField(Object element, String propertyName) throws Exception
    {
        Object property = null;

        try
        {
            Field field = element.getClass().getField(propertyName);
            property = field.get(element);
        }
        catch (NoSuchFieldException e)
        {
            // not a prob
        }

        return property;
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
