package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFEnumeration.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFEnumeration
 */
public class JSFEnumerationLogicImpl
    extends JSFEnumerationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFEnumerationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return converterName
     * @see org.andromda.cartridges.jsf.metafacades.JSFEnumeration#getConverterName()
     */
    protected String handleGetConverterName()
    {
        return StringUtils.replace(
            ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.CONVERTER_PATTERN)),
            "{0}",
            this.getName());
    }

    /**
     * @return getPackageName() + "." + getConverterName()
     * @see org.andromda.cartridges.jsf.metafacades.JSFEnumeration#getFullyQualifiedConverterName()
     */
    protected String handleGetFullyQualifiedConverterName()
    {
        return this.getPackageName() + "." + this.getConverterName();
    }

    /**
     * @return getFullyQualifiedConverterName().replace('.', '/')
     * @see org.andromda.cartridges.jsf.metafacades.JSFEnumeration#getConverterPath()
     */
    protected String handleGetConverterPath()
    {
        return this.getFullyQualifiedConverterName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFEnumeration#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

}