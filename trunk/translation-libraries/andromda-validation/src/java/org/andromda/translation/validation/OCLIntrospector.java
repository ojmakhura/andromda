package org.andromda.translation.validation;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

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
            return PropertyUtils.getProperty(element, feature);

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
            return MethodUtils.invokeMethod(element, feature, arguments);
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
}
