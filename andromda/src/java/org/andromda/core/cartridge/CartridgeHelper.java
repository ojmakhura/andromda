package org.andromda.core.cartridge;

import org.apache.commons.lang.StringUtils;

/**
 * This is a universal helper class to assist cartridges.
 */
public class CartridgeHelper
{
    /**
     * Returns the suffix used for getter and setter property accessor methods.
     * 'test' returns 'Test', but 'tEst' return 'tEst'. As per JDK specification.
     * (Please note that this is not true for entity beans) 
     *
     * @param propertyName the property name
     * @return the suffix
     */
    public static String getPropertyAccessorSuffix(String propertyName)
    {
        String suffix = null;

        if (StringUtils.isBlank(propertyName))  // empty argument, return it again
        {
            suffix = propertyName;
        }
        else
        {
            if (propertyName.length() > 1)  // single character
            {
                char secondChar = propertyName.charAt(1);
                if (secondChar >= 'A' && secondChar <= 'Z') // uppercase
                {
                    suffix = propertyName;
                }
                else    // lowercase
                {
                    suffix = StringUtils.capitalize(propertyName);
                }
            }
            else    // more than a single characters
            {
                suffix = StringUtils.capitalize(propertyName);
            }
        }
        return suffix;
    }
}
