package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.Objects;

import org.andromda.cartridges.web.CartridgeWebGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEnumeration.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEnumeration
 */
public class ThymeleafEnumerationLogicImpl
    extends ThymeleafEnumerationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafEnumerationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return converterName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEnumeration#getConverterName()
     */
    protected String handleGetConverterName()
    {
        return StringUtils.replace(
            Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.CONVERTER_PATTERN), ""),
            "{0}",
            this.getName());
    }

    /**
     * @return getPackageName() + "." + getConverterName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEnumeration#getFullyQualifiedConverterName()
     */
    protected String handleGetFullyQualifiedConverterName()
    {
        return this.getPackageName() + "." + this.getConverterName();
    }

    /**
     * @return getFullyQualifiedConverterName().replace('.', '/')
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEnumeration#getConverterPath()
     */
    protected String handleGetConverterPath()
    {
        return this.getFullyQualifiedConverterName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEnumeration#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }
}
