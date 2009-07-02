package org.andromda.translation.ocl.validation;

import java.lang.reflect.Method;
import org.andromda.core.common.Introspector;
import org.andromda.translation.ocl.syntax.OCLPatterns;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Dynamically invokes operation and property calls on specified <strong>elements</code>.
 *
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public final class OCLIntrospector
{
    private static final Logger logger = Logger.getLogger(OCLIntrospector.class);

    /**
     * Invokes the given <code>feature</code> on the <code>element</code>. Its expected that the feature is either an
     * operation or a property.
     */
    public static final Object invoke(
        final Object element,
        String feature)
    {
        Object result = null;
        try
        {
            feature = StringUtils.trimToEmpty(feature);
            if (OCLPatterns.isOperation(feature))
            {
                result = invoke(element, feature, null);
            }
            else
            {
                result = Introspector.instance().getProperty(element, feature);
            }
        }
        catch (final NullPointerException exception)
        {
            // ignore (the result will just be null)
        }
        catch (final OCLIntrospectorException throwable)
        {
            // Dont catch our own exceptions.
            // Otherwise get Exception/Cause chain which
            // can hide the original exception.
            throw throwable;
        }
        catch (Throwable throwable)
        {
            throwable = getRootCause(throwable);

            // If cause is an OCLIntrospectorException re-throw 
            // the exception rather than creating a new one.
            if (throwable instanceof OCLIntrospectorException)
            {
                throw (OCLIntrospectorException)throwable;
            }
            throw new OCLIntrospectorException(throwable);
        }
        return result;
    }

    /**
     * Invokes the given <code>feature</code> on the specified <code>element</code> taking the given
     * <code>arguments</code>. If <code>arguments</code> is null its expected that the feature is an empty operation.
     */
    public static Object invoke(
        final Object element,
        String feature,
        final Object[] arguments)
    {
        Object result = null;
        try
        {
            // check for parenthesis
            int parenIndex = feature.indexOf('(');
            if (parenIndex != -1)
            {
                feature = feature.substring(0, parenIndex).trim();
            }
            result = invokeMethod(element, feature, arguments);
        }
        catch (final NullPointerException exception)
        {
            // ignore (the result will just be null)
        }
        catch (Throwable throwable)
        {
            // At least output the location where the error happened, not the entire stack trace.
            StackTraceElement[] trace = throwable.getStackTrace();
            String location = " AT " + trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
            if (throwable.getMessage()!=null)
            {
                location += " " + throwable.getMessage();
            }
            /*final String message =
                "Error invoking feature '" + feature + "' on element '" + element + "' with arguments '" +
                StringUtils.join(arguments, ',') + "'";*/
            throwable = getRootCause(throwable);
            logger.error("OCLIntrospector " + throwable + " invoking " + element + " METHOD " + feature + " WITH " + StringUtils.join(arguments, ',') + location);
            throw new OCLIntrospectorException(throwable);
        }
        return result;
    }

    private static final Object invokeMethod(
        final Object element,
        final String methodName,
        final Object[] arguments)
        throws Exception
    {
        Object property = null;

        if (element != null && methodName != null && !methodName.isEmpty())
        {
            Class[] argumentTypes = getObjectTypes(arguments);

            final Method method = element.getClass().getMethod(methodName, argumentTypes);
            property = method.invoke(element, arguments);
        }

        return property;
    }

    private static final Class[] getObjectTypes(final Object[] objects)
    {
        Class[] objectTypes = null;
        if (objects != null)
        {
            objectTypes = new Class[objects.length];
            for (int ctr = 0; ctr < objects.length; ctr++)
            {
                final Object object = objects[ctr];
                if (object != null)
                {
                    objectTypes[ctr] = object.getClass();
                }
            }
        }
        return objectTypes;
    }

    /**
     * Attempts to retrieve the root cause of the exception, if it can not be
     * found, the <code>throwable</code> itself is returned.
     *
     * @param throwable the exception from which to retrieve the root cause.
     * @return the root cause of the exception
     */
    private static final Throwable getRootCause(Throwable throwable)
    {
        Throwable root = ExceptionUtils.getRootCause(throwable);
        if (root != null)
        {
            throwable = root;
        }
        return throwable;
    }
}