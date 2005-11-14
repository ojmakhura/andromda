package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState
 */
public class StrutsActionStateLogicImpl
    extends StrutsActionStateLogic
{
    public StrutsActionStateLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden because StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndActionState#getContainerActions()
     */
    public List getContainerActions()
    {
        final Collection actionSet = new LinkedHashSet();

        final StateMachineFacade stateMachineFacade = this.getStateMachine();
        if (stateMachineFacade instanceof ActivityGraphFacade)
        {
            final ActivityGraphFacade activityGraph = (ActivityGraphFacade)stateMachineFacade;
            final UseCaseFacade useCase = activityGraph.getUseCase();

            if (useCase instanceof StrutsUseCase)
            {
                final Collection actions = ((StrutsUseCase)useCase).getActions();
                for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    final StrutsAction action = (StrutsAction)actionIterator.next();
                    if (action.getActionStates().contains(this))
                    {
                        actionSet.add(action);
                    }
                }
            }
        }
        return new ArrayList(actionSet);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getExceptions()
     */
    public java.util.List getExceptions()
    {
        final Map exceptionsMap = new LinkedHashMap();
        final Collection outgoing = getOutgoing();
        for (final Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
            if (transition instanceof StrutsExceptionHandler)
            {
                exceptionsMap.put(((StrutsExceptionHandler)transition).getExceptionKey(), transition);
            }
        }
        return new ArrayList(exceptionsMap.values());
    }
}
