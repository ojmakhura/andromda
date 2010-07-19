package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.utils.StringUtilsHelper;

/**
 *
 */
public class StrutsSessionObjectLogicImpl
    extends StrutsSessionObjectLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsSessionObjectLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsSessionObjectLogic#handleGetSessionKey()
     */
    protected String handleGetSessionKey()
    {
        return StringUtilsHelper.lowerCamelCaseName(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsSessionObjectLogic#handleGetFullPath()
     */
    protected String handleGetFullPath()
    {
        return '/' + getFullyQualifiedName().replace('.', '/');
    }
}
