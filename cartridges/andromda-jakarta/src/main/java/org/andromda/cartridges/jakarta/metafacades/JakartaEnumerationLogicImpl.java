package org.andromda.cartridges.jakarta.metafacades;

import java.util.Objects;

import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaEnumeration.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaEnumeration
 */
public class JakartaEnumerationLogicImpl
    extends JakartaEnumerationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaEnumerationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return converterName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEnumeration#getConverterName()
     */
    protected String handleGetConverterName()
    {
        return StringUtils.replace(
            Objects.toString(this.getConfiguredProperty(JakartaGlobals.CONVERTER_PATTERN), ""),
            "{0}",
            this.getName());
    }

    /**
     * @return getPackageName() + "." + getConverterName()
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEnumeration#getFullyQualifiedConverterName()
     */
    protected String handleGetFullyQualifiedConverterName()
    {
        return this.getPackageName() + "." + this.getConverterName();
    }

    /**
     * @return getFullyQualifiedConverterName().replace('.', '/')
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEnumeration#getConverterPath()
     */
    protected String handleGetConverterPath()
    {
        return this.getFullyQualifiedConverterName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(getName())
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEnumeration#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }
}
