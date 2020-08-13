package org.andromda.cartridges.jsf2.metafacades;

import java.util.Objects;

import org.andromda.cartridges.jsf2.JSFGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf2.metafacades.JSFEnumeration.
 *
 * @see org.andromda.cartridges.jsf2.metafacades.JSFEnumeration
 */
public class JSFEnumerationLogicImpl
    extends JSFEnumerationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFEnumerationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return converterName
     * @see org.andromda.cartridges.jsf2.metafacades.JSFEnumeration#getConverterName()
     */
    protected String handleGetConverterName()
    {
        return StringUtils.replace(
            Objects.toString(this.getConfiguredProperty(JSFGlobals.CONVERTER_PATTERN), ""),
            "{0}",
            this.getName());
    }

    /**
     * @return getPackageName() + "." + getConverterName()
     * @see org.andromda.cartridges.jsf2.metafacades.JSFEnumeration#getFullyQualifiedConverterName()
     */
    protected String handleGetFullyQualifiedConverterName()
    {
        return this.getPackageName() + "." + this.getConverterName();
    }

    /**
     * @return getFullyQualifiedConverterName().replace('.', '/')
     * @see org.andromda.cartridges.jsf2.metafacades.JSFEnumeration#getConverterPath()
     */
    protected String handleGetConverterPath()
    {
        return this.getFullyQualifiedConverterName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(getName())
     * @see org.andromda.cartridges.jsf2.metafacades.JSFEnumeration#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }
}
