package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
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
    private String actionMethodName = null;

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
    public java.lang.String getActionMethodName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionMethodName != null) return actionMethodName;

        final String methodName = getName();
        return actionMethodName = (methodName == null) ? "a" + System.currentTimeMillis() : StringUtilsHelper.toJavaMethodName(methodName);
    }

    // ------------- relations ------------------

}
