package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.TransitionFacade;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UseCase;

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
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        // Be careful. Should return true only when it has an owning transition
        // contained in front end usecase
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
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    @Override
    protected FrontEndControllerOperation handleGetControllerCall()
    {
        final List<FrontEndControllerOperation> operations = this.getControllerCalls();
        return (operations.isEmpty() ? null : operations.iterator().next());
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCalls()
     */
    @Override
    public List<Operation> handleGetControllerCalls()
    {
        // - get every operation from each CallOperationAction instance.
        // - Note: this is the same implementation as CallEvent.getOperationCall()
        final Activity activity = (Activity)this.metaObject;
        final List<Operation> operations = new ArrayList();
        Collection<ActivityNode> nodes = activity.getNodes();
        for (final Iterator<ActivityNode> iterator = nodes.iterator(); iterator.hasNext();)
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
     * @see org.andromda.metafacades.uml.FrontEndEvent#getAction()
     */
    @Override
    protected FrontEndAction handleGetAction()
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
