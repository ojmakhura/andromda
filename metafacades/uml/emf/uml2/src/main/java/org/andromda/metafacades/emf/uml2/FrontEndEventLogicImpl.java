package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.TransitionFacade;
import org.eclipse.uml2.Activity;
import org.eclipse.uml2.CallOperationAction;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Operation;
import org.eclipse.uml2.Transition;
import org.eclipse.uml2.UseCase;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndEvent.
 *
 * @see org.andromda.metafacades.uml.FrontEndEvent
 */
public class FrontEndEventLogicImpl
    extends FrontEndEventLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndEventLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return isContainedInFrontEndUseCase
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        // Be careful. Should return true only when it has an owning transition
        // contained in frontend usecase
        // from UML1.4: return this.getTransition() instanceof FrontEndForward;
        // Causes stack overflow...
        Element owner = (Element)this.metaObject;
        if (!(owner.getOwner() instanceof Transition))
        {
            return false;
        }
        while (owner != null)
        {
            if (owner instanceof UseCase)
            {
                if (this.shieldedElement(owner) instanceof FrontEndUseCase)
                {
                    return true;
                }
            }
            owner = owner.getOwner();
        }
        return false;
    }

    /**
     * @return getControllerCalls().get(0)
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    protected Object handleGetControllerCall()
    {
        final List operations = this.getControllerCalls();
        return operations.isEmpty() ? null : operations.iterator().next();
    }

    /**
     * get every operation from each CallOperationAction instance.
     * @return controllerCalls
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCalls()
     */
    public List handleGetControllerCalls()
    {
        // - get every operation from each CallOperationAction instance.
        // - Note: this is the same implementation as CallEvent.getOperationCall()
        final Activity activity = (Activity)this.metaObject;
        final List operations = new ArrayList();
        Collection nodes = activity.getNodes();
        for (final Iterator iterator = nodes.iterator(); iterator.hasNext();)
        {
            final Object nextNode = iterator.next();
            if (nextNode instanceof CallOperationAction)
            {
                final Operation operation = ((CallOperationAction)nextNode).getOperation();
                if (operation != null)
                {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

    /**
     * @return getTransition() instanceof FrontEndAction
     * @see org.andromda.metafacades.uml.FrontEndEvent#getAction()
     */
    protected Object handleGetAction()
    {
        FrontEndAction action = null;
        TransitionFacade transition = this.getTransition();
        if (transition instanceof FrontEndAction)
        {
            action = (FrontEndAction)transition;
        }
        return action;
    }
}