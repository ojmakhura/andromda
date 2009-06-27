package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
 */
public class StrutsExceptionHandlerLogicImpl
    extends StrutsExceptionHandlerLogic
{
    public StrutsExceptionHandlerLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsFrontEndException()
    {
        return this.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_EXCEPTION);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionKey()()
     */
    protected java.lang.String handleGetExceptionKey()
    {
        final String type = getExceptionType();
        final int dotIndex = type.lastIndexOf('.');

        // the dot may not be the last character
        return StringUtilsHelper.toResourceMessageKey((dotIndex < type.length() - 1)
            ? type.substring(dotIndex + 1) : type);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionType()()
     */
    protected java.lang.String handleGetExceptionType()
    {
        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_EXCEPTION_TYPE);
        String type = value == null ? null : value.toString();
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGEDVALUE_EXCEPTION_DEFAULT_TYPE;
        }
        return type;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionPath()()
     */
    protected java.lang.String handleGetExceptionPath()
    {
        final StateVertexFacade target = getTarget();
        if (target instanceof StrutsJsp)
            return ((StrutsJsp)target).getFullPath() + ".jsp";
        else if (target instanceof StrutsFinalState)
            return ((StrutsFinalState)target).getFullPath();
        else
            return "";
    }

    protected String handleGetMessageKey()
    {
        final UseCaseFacade useCase = this.getUseCase();
        return useCase != null ? StringUtilsHelper.toResourceMessageKey(useCase.getName()) : null;
    }
}
