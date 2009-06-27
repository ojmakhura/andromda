package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CallEventFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndExceptionHandler;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndActionState.
 * 
 * @see org.andromda.metafacades.uml.FrontEndActionState
 */
public class FrontEndActionStateLogicImpl extends FrontEndActionStateLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActionStateLogicImpl(final Object metaObject, final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#isServerSide()
     */
    @Override
    protected boolean handleIsServerSide()
    {
        return !(this.THIS() instanceof FrontEndView);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getActionMethodName()
     */
    @Override
    protected String handleGetActionMethodName()
    {
        return '_' + StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof FrontEndActivityGraph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getForward()
     */
    @Override
    protected TransitionFacade handleGetForward()
    {
        TransitionFacade forward = null;

        for (final Iterator iterator = this.getOutgoings().iterator(); iterator
                .hasNext()
                && forward == null;)
        {
            final TransitionFacade transition = (TransitionFacade)iterator
                    .next();
            if (!(transition instanceof FrontEndExceptionHandler))
            {
                forward = transition;
            }
        }
        return forward;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getControllerCalls()
     */
    @Override
    protected List<OperationFacade> handleGetControllerCalls()
    {
        final List<OperationFacade> controllerCallsList = new ArrayList<OperationFacade>();
        final Collection<EventFacade> deferrableEvents = this.getDeferrableEvents();
        for (final Iterator<EventFacade> iterator = deferrableEvents.iterator(); iterator
                .hasNext();)
        {
            final EventFacade event = (EventFacade)iterator.next();
            if (event instanceof CallEventFacade)
            {
                controllerCallsList.addAll(((CallEventFacade)event).getOperations());
            }
            else if (event instanceof FrontEndEvent)
            {
                controllerCallsList.addAll(((FrontEndEvent)event).getControllerCalls());
            }
        }
        return controllerCallsList;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getExceptions()
     */
    @Override
    protected List<FrontEndExceptionHandler> handleGetExceptions()
    {
        final Set<FrontEndExceptionHandler> exceptions = new LinkedHashSet<FrontEndExceptionHandler>();
        final Collection<TransitionFacade> outgoing = this.getOutgoings();
        for (final Iterator<TransitionFacade> iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator
                    .next();
            if (transition instanceof FrontEndExceptionHandler)
            {
                exceptions.add((FrontEndExceptionHandler)transition);
            }
        }
        return new ArrayList<FrontEndExceptionHandler>(exceptions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getContainerActions()
     */
    @Override
    protected List<FrontEndAction> handleGetContainerActions()
    {
        final Collection<FrontEndAction> actionSet = new LinkedHashSet();

        final StateMachineFacade stateMachineFacade = this.getStateMachine();
        if (stateMachineFacade instanceof ActivityGraphFacade)
        {
            final ActivityGraphFacade activityGraph = (ActivityGraphFacade)stateMachineFacade;
            final UseCaseFacade useCase = activityGraph.getUseCase();

            if (useCase instanceof FrontEndUseCase)
            {
                final Collection<FrontEndAction> actions = ((FrontEndUseCase)useCase)
                        .getActions();
                for (final Iterator<FrontEndAction> actionIterator = actions.iterator(); actionIterator
                        .hasNext();)
                {
                    final FrontEndAction action = (FrontEndAction)actionIterator
                            .next();
                    if (action.getActionStates().contains(this))
                    {
                        actionSet.add(action);
                    }
                }
            }
        }
        return new ArrayList(actionSet);
    }

}
