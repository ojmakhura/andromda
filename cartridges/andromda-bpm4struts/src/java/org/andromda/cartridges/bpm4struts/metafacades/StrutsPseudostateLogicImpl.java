package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate
 */
public class StrutsPseudostateLogicImpl
        extends StrutsPseudostateLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate
{
    // ---------------- constructor -------------------------------

    public StrutsPseudostateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsPseudostate ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate#getActionMethodName()()
     */
    public java.lang.String handleGetActionMethodName()
    {
        final String methodName = getName();
        return (methodName == null) ? "a" + System.currentTimeMillis() : StringUtilsHelper.toJavaMethodName(methodName);
    }

    // ------------- relations ------------------

}
