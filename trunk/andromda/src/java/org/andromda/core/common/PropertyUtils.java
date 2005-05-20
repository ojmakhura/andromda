package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * Contains utilities for dealing with properties on object's with the AndroMDA core.
 *
 * @author Chad Brandon
 */
public class PropertyUtils
{
    /**
     * <p/>
     * Indicates whether or not the given <code>object</code> contains a valid property with the given <code>name</code>
     * and <code>value</code>. </p>
     * <p/>
     * A valid property means the following: <ul> <li>It exists on the object</li> <li>It is not null on the object</li>
     * <li>If its a boolean value, then it evaluates to <code>true</code> <li> </ul> All other possibilities return
     * <code>false</code> </p>
     *
     * @param object the object to test for the valid property.
     * @param name   the name of the propery for which to test.
     * @param value  the value to evaluate against.
     * @return true/false
     */
    public static boolean containsValidProperty(final Object object, final String name, final String value)
    {
        boolean valid = false;
        try
        {
            if (org.apache.commons.beanutils.PropertyUtils.isReadable(object, name))
            {
                final Object propertyValue = org.apache.commons.beanutils.PropertyUtils.getProperty(object, name);
                valid = propertyValue != null;
                // if valid is still true, and the propertyValue
                // is not null
                if (valid)
                {
                    // if it's a collection then we check to see if the
                    // collection is not empty
                    if (propertyValue instanceof Collection)
                    {
                        valid = !((Collection)propertyValue).isEmpty();
                    }
                    else
                    {
                        final String valueAsString = String.valueOf(propertyValue);
                        if (StringUtils.isNotEmpty(value))
                        {
                            valid = valueAsString.equals(value);
                        }
                        else if (propertyValue instanceof Boolean)
                        {
                            valid = Boolean.valueOf(valueAsString).booleanValue();
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            valid = false;
        }
        return valid;
    }
    
    /**
     * Sets the property having the given <code>name</code> on the <code>object</code>
     * with the given <code>value</code>.
     * 
     * @param object the object on which to set the property.
     * @param name the name of the property to populate.
     * @param value the value to give the property.
     */
    public static void setProperty(final Object object, final String name, final Object value)
        throws Exception
    {
        if (org.apache.commons.beanutils.PropertyUtils.isReadable(object, name))
        {
            org.apache.commons.beanutils.BeanUtils.setProperty(object, name, value);
        }
    }
}
