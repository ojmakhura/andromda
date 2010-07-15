package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.SubactivityStateFacade.
 *
 * @see org.andromda.metafacades.uml.SubactivityStateFacade
 */
public class SubactivityStateFacadeLogicImpl
    extends SubactivityStateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public SubactivityStateFacadeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return false
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#isDynamic()
     */
    protected boolean handleIsDynamic()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @return null
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#getSubmachine()
     */
    protected Object handleGetSubmachine()
    {
        // TODO: add your implementation here!
        return null;
    }
}