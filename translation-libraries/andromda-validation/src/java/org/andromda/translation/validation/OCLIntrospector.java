package org.andromda.translation.validation;

import org.apache.commons.beanutils.MethodUtils;

/**
 * @todo document
 */
public class OCLIntrospector
{
    /**
     * @todo document
     */
    public Object invoke(Object element, String feature)
    {
        return invoke(element, feature, null);
    }

    /**
     * returns null in case of problems
     * @todo document
     */
    public Object invoke(Object element, String feature, Object[] arguments)
    {
        try
        {
            return MethodUtils.invokeMethod(element, feature, arguments);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
