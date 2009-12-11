package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
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

    /**
     * Public constructor for JSFEnumerationLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.jsf.metafacades.JSFEnumeration
     */
    public JSFEnumerationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return ConverterName
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
     * @return getPackageName() + "." + this.getConverterName()
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

}