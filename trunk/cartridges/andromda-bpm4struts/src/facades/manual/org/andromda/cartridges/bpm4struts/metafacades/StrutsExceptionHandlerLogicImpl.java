package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.core.common.StringUtilsHelper;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
 */
public class StrutsExceptionHandlerLogicImpl
        extends StrutsExceptionHandlerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
{
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
        final String type = getExceptionType();
        final int dotIndex = type.lastIndexOf('.');

        return StringUtilsHelper.toResourceMessageKey(
                (dotIndex < type.length() - 1)   // the dot may not be the last character
                    ?  type.substring(dotIndex + 1)
                    :  type );
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionType()()
     */
    public java.lang.String getExceptionType()
    {
        String type = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_TYPE);
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE;
        }
        return type;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionPath()()
     */
    public java.lang.String getExceptionPath()
    {
        final StateVertexFacade target = getTarget();
        return (target instanceof StrutsJsp)
            ? ((StrutsJsp)target).getFullPath() + ".jsp"
            : "";
    }

    // ------------- relations ------------------

}
