package org.andromda.metafacades.emf.uml2;

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
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndActionState.
 *
 * @see org.andromda.metafacades.uml.FrontEndActionState
 * @author Bob Fields
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
     * @return instanceof FrontEndView
     * @see org.andromda.metafacades.uml.FrontEndActionState#isServerSide()
     */
    protected boolean handleIsServerSide()
    {
        return !(this.THIS() instanceof FrontEndView);
    }

    /**
     * @return '_' + lowerCamelCaseName(getName())
     * @see org.andromda.metafacades.uml.FrontEndActionState#getActionMethodName()
     */
    protected String handleGetActionMethodName()
    {
        return '_' + StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return instanceof FrontEndActivityGraph
     * @see org.andromda.metafacades.uml.FrontEndActionState#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof FrontEndActivityGraph;
    }

    /**
     * @return getOutgoings() instanceof FrontEndExceptionHandler
     * @see org.andromda.metafacades.uml.FrontEndActionState#getForward()
     */
    protected Object handleGetForward()
    {
        Object forward = null;

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
     * @return getDeferrableEvents().getOperations() or getControllerCalls()
     * @see org.andromda.metafacades.uml.FrontEndActionState#getControllerCalls()
     */
    protected List handleGetControllerCalls()
    {
        final List controllerCallsList = new ArrayList();
        final Collection deferrableEvents = this.getDeferrableEvents();
        for (final Iterator iterator = deferrableEvents.iterator(); iterator
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
     * @return getOutgoings() instanceof FrontEndExceptionHandler
     * @see org.andromda.metafacades.uml.FrontEndActionState#getExceptions()
     */
    protected List handleGetExceptions()
    {
        final Set exceptions = new LinkedHashSet();
        final Collection outgoing = this.getOutgoings();
        for (final Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator
                    .next();
            if (transition instanceof FrontEndExceptionHandler)
            {
                exceptions.add(transition);
            }
        }
        return new ArrayList(exceptions);
    }

    /**
     * @return getStateMachine().getUseCase().getActions()
     * @see org.andromda.metafacades.uml.FrontEndActionState#getContainerActions()
     */
    protected List handleGetContainerActions()
    {
        final Collection actionSet = new LinkedHashSet();

        final StateMachineFacade stateMachineFacade = this.getStateMachine();
        if (stateMachineFacade instanceof ActivityGraphFacade)
        {
            final ActivityGraphFacade activityGraph = (ActivityGraphFacade)stateMachineFacade;
            final UseCaseFacade useCase = activityGraph.getUseCase();

            if (useCase instanceof FrontEndUseCase)
            {
                final Collection actions = ((FrontEndUseCase)useCase)
                        .getActions();
                for (final Iterator actionIterator = actions.iterator(); actionIterator
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