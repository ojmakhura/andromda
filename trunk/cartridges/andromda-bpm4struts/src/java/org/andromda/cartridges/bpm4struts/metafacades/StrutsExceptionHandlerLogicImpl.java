package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.StateVertexFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
 */
public class StrutsExceptionHandlerLogicImpl
        extends StrutsExceptionHandlerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
{
    private String exceptionKey = null;
    private String exceptionPath = null;
    private String exceptionType = null;

    // ---------------- constructor -------------------------------

    public StrutsExceptionHandlerLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsExceptionHandler ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionKey()()
     */
    public java.lang.String getExceptionKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && exceptionKey != null) return exceptionKey;

        final String type = getExceptionType();
        final int dotIndex = type.lastIndexOf('.');

        return exceptionKey = StringUtilsHelper.toResourceMessageKey((dotIndex < type.length() - 1)   // the dot may not be the last character
                ? type.substring(dotIndex + 1)
                : type);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionType()()
     */
    public java.lang.String getExceptionType()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && exceptionType != null) return exceptionType;

        String type = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_TYPE).toString();
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE;
        }
        return exceptionType = type;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionPath()()
     */
    public java.lang.String getExceptionPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && exceptionPath != null) return exceptionPath;

        final StateVertexFacade target = getTarget();
        if (target instanceof StrutsJsp)
            exceptionPath = ((StrutsJsp) target).getFullPath() + ".jsp";
        else if (target instanceof StrutsFinalState)
            exceptionPath = ((StrutsFinalState) target).getFullPath() + ".do";
        else
            exceptionPath = "";

        return exceptionPath;
    }

    // ------------- relations ------------------

}
