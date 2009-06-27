package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.StateMachineFacade;

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
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#isDynamic()
     */
    @Override
    protected boolean handleIsDynamic()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#getSubmachine()
     */
    @Override
    protected StateMachineFacade handleGetSubmachine()
    {
        // TODO: add your implementation here!
        return null;
    }
}
