package org.andromda.cartridges.spring.metafacades;

import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities specific to dealing with the Spring cartridge metafacades.
 * 
 * @author Chad Brandon
 */
class SpringMetafacadeUtils
{
    /**
     * Creates a fully qualified name from the given <code>packageName</code>,
     * <code>name</code>, and <code>suffix</code>.
     * 
     * @param packageName the name of the model element package.
     * @param name the name of the model element.
     * @param suffix the suffix to append.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(
        String packageName,
        String name,
        String suffix)
    {
        StringBuffer fullyQualifiedName = new StringBuffer(StringUtils
            .trimToEmpty(packageName));
        fullyQualifiedName.append(".");
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        return fullyQualifiedName.toString();
    }
}