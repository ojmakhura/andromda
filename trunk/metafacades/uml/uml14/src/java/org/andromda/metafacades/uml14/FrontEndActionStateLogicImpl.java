package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.CallEventFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndExceptionHandler;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.TransitionFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndActionState.
 *
 * @see org.andromda.metafacades.uml.FrontEndActionState
 */
public class FrontEndActionStateLogicImpl
    extends FrontEndActionStateLogic
{
    public FrontEndActionStateLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#isServerSide()
     */
    protected boolean handleIsServerSide()
    {
        return !(this instanceof FrontEndView);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getActionMethodName()
     */
    protected java.lang.String handleGetActionMethodName()
    {
        return '_' + StringUtilsHelper.lowerCamelCaseName(getName());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof FrontEndActivityGraph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getForward()
     */
    protected Object handleGetForward()
    {
        Object forward = null;

        for (final Iterator iterator = this.getOutgoing().iterator(); iterator.hasNext() && forward == null;)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
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
    protected List handleGetControllerCalls()
    {
        final List controllerCallsList = new ArrayList();
        final Collection deferrableEvents = this.getDeferrableEvents();
        for (final Iterator iterator = deferrableEvents.iterator(); iterator.hasNext();)
        {
            final EventFacade event = (EventFacade)iterator.next();
            if (event instanceof CallEventFacade)
            {
                final Object operationObject = ((CallEventFacade)event).getOperation();
                if (operationObject != null)
                {
                    controllerCallsList.add(operationObject);
                }
            }
            else if (event instanceof FrontEndEvent)
            {
                final Object callObject = ((FrontEndEvent)event).getControllerCall();
                if (callObject != null)
                {
                    controllerCallsList.add(callObject);
                }
            }
        }
        return controllerCallsList;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActionState#getExceptions()
     */
    protected List handleGetExceptions()
    {
        final Set exceptions = new HashSet();
        final Collection outgoing = getOutgoing();
        for (final Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
            if (transition instanceof FrontEndExceptionHandler)
            {
                exceptions.add(transition);
            }
        }
        return new ArrayList(exceptions);
    }
}