package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.utils.StringUtilsHelper;


public class StrutsSessionObjectLogicImpl
    extends StrutsSessionObjectLogic
{
    public StrutsSessionObjectLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected java.lang.String handleGetSessionKey()
    {
        return StringUtilsHelper.lowerCamelCaseName(getName());
    }

    protected java.lang.String handleGetFullPath()
    {
        return '/' + getFullyQualifiedName().replace('.', '/');
    }

}
