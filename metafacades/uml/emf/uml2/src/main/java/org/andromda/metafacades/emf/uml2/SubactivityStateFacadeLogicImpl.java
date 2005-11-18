package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.SubactivityStateFacade.
 *
 * @see org.andromda.metafacades.uml.SubactivityStateFacade
 */
public class SubactivityStateFacadeLogicImpl
    extends SubactivityStateFacadeLogic
{
    public SubactivityStateFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#isDynamic()
     */
    protected boolean handleIsDynamic()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#getSubmachine()
     */
    protected java.lang.Object handleGetSubmachine()
    {
        // TODO: add your implementation here!
        return null;
    }
}