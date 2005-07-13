package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndExceptionHandler.
 *
 * @see org.andromda.metafacades.uml.FrontEndExceptionHandler
 */
public class FrontEndExceptionHandlerLogicImpl
    extends FrontEndExceptionHandlerLogic
{
    public FrontEndExceptionHandlerLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndExceptionHandler#isFrontEndException()
     */
    protected boolean handleIsFrontEndException()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_EXCEPTION);
    }
}