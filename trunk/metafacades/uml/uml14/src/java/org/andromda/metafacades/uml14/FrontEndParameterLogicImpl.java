package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.TransitionFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndParameter.
 *
 * @see org.andromda.metafacades.uml.FrontEndParameter
 */
public class FrontEndParameterLogicImpl
    extends FrontEndParameterLogic
{
    public FrontEndParameterLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isControllerOperationArgument()
     */
    protected boolean handleIsControllerOperationArgument()
    {
        return this.getControllerOperation() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getControllerOperation()
     */
    protected Object handleGetControllerOperation()
    {
        return this.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getEvent() instanceof FrontEndEvent || this.getOperation() instanceof FrontEndControllerOperation;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getView()
     */
    protected Object handleGetView()
    {
        Object view = null;
        final EventFacade event = this.getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction)
            {
                final FrontEndAction action = (FrontEndAction)transition;
                view = action.getInput();
            }
            else if (transition instanceof FrontEndForward)
            {
                final FrontEndForward forward = (FrontEndForward)transition;
                if (forward.isEnteringView())
                {
                    view = forward.getTarget();
                }
            }
        }
        return view;
    }
}