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
 * @author Bob Fields
 */
public class StrutsActionStateLogicImpl
    extends StrutsActionStateLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsActionStateLogicImpl(
        Object metaObject,
        String context)
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
        final Collection<StrutsAction> actionSet = new LinkedHashSet<StrutsAction>();

        final StateMachineFacade stateMachineFacade = this.getStateMachine();
        if (stateMachineFacade instanceof ActivityGraphFacade)
        {
            final ActivityGraphFacade activityGraph = (ActivityGraphFacade)stateMachineFacade;
            final UseCaseFacade useCase = activityGraph.getUseCase();

            if (useCase instanceof StrutsUseCase)
            {
                final Collection actions = ((StrutsUseCase)useCase).getActions();
                for (final Iterator<StrutsAction> actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    final StrutsAction action = actionIterator.next();
                    if (action.getActionStates().contains(this))
                    {
                        actionSet.add(action);
                    }
                }
            }
        }
        return new ArrayList<StrutsAction>(actionSet);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getExceptions()
     */
    public List getExceptions()
    {
        final Map<String, TransitionFacade> exceptionsMap = new LinkedHashMap<String, TransitionFacade>();
        final Collection<TransitionFacade> outgoings = getOutgoings();
        for (final Iterator<TransitionFacade> iterator = outgoings.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = iterator.next();
            if (transition instanceof StrutsExceptionHandler)
            {
                exceptionsMap.put(((StrutsExceptionHandler)transition).getExceptionKey(), transition);
            }
        }
        return new ArrayList<TransitionFacade>(exceptionsMap.values());
    }
}
