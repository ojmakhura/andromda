package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;


public class StrutsSessionObjectLogicImpl
        extends StrutsSessionObjectLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsSessionObject
{
    public StrutsSessionObjectLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected java.lang.String handleGetSessionKey()
    {
        return "andromda.bpm4struts." + StringUtilsHelper.toResourceMessageKey(getFullyQualifiedName());
    }

    protected java.lang.String handleGetFullPath()
    {
        return '/' + getFullyQualifiedName().replace('.', '/');
    }

}
