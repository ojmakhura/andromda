package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.activitygraphs.SubactivityState;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.SubactivityStateFacade.
 *
 * @see org.andromda.metafacades.uml.SubactivityStateFacade
 * @author Bob Fields
 */
public class SubactivityStateFacadeLogicImpl
    extends SubactivityStateFacadeLogic
{
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public SubactivityStateFacadeLogicImpl(
        SubactivityState metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#getSubmachine()
     */
    @Override
    protected StateMachine handleGetSubmachine()
    {
        return metaObject.getSubmachine();
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#isDynamic()
     */
    @Override
    protected boolean handleIsDynamic()
    {
        return metaObject.isDynamic();
    }
}