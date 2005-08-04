package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndPseudostate;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StateVertexFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndControllerOperation.
 *
 * @see org.andromda.metafacades.uml.FrontEndControllerOperation
 */
public class FrontEndControllerOperationLogicImpl
    extends FrontEndControllerOperationLogic
{
    public FrontEndControllerOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Override to return the owner's package as the package name.
     *
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetPackageName()
     */
    public String handleGetPackageName()
    {
        final ClassifierFacade owner = this.getOwner();
        return owner != null ? owner.getPackageName() : "";
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isOwnerIsController()
     */
    protected boolean handleIsOwnerIsController()
    {
        return this.getOwner() instanceof FrontEndController;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getFormFields()
     */
    protected java.util.List handleGetFormFields()
    {
        return new ArrayList(this.getArguments());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getActivityGraph()
     */
    protected Object handleGetActivityGraph()
    {
        Object graph = null;

        final ClassifierFacade owner = getOwner();
        if (owner instanceof FrontEndController)
        {
            final FrontEndController controller = (FrontEndController)owner;
            if (controller != null)
            {
                final FrontEndUseCase useCase = controller.getUseCase();
                if (useCase != null)
                {
                    graph = useCase.getActivityGraph();
                }
            }
        }
        return graph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getDeferringActions()
     */
    protected java.util.List handleGetDeferringActions()
    {
        final Collection deferringActions = new HashSet();

        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (final Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final Object actionStateObject = actionStateIterator.next();
                if (actionStateObject instanceof FrontEndActionState)
                {
                    final FrontEndActionState actionState = (FrontEndActionState)actionStateObject;
                    final Collection controllerCalls = actionState.getControllerCalls();
                    for (final Iterator controllerCallIterator = controllerCalls.iterator();
                        controllerCallIterator.hasNext();)
                    {
                        final OperationFacade operation = (OperationFacade)controllerCallIterator.next();
                        if (this.equals(operation))
                        {
                            deferringActions.addAll(actionState.getContainerActions());
                        }
                    }
                }
            }

            final Collection transitions = graph.getTransitions();
            for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                final FrontEndForward transition = (FrontEndForward)transitionIterator.next();
                final EventFacade event = transition.getTrigger();
                if (event instanceof FrontEndEvent)
                {
                    final FrontEndEvent trigger = (FrontEndEvent)event;
                    final FrontEndControllerOperation operation = trigger.getControllerCall();
                    if (this.equals(operation))
                    {
                        // we have two types of controller calls: the ones in action states and the ones for decisions
                        final StateVertexFacade source = transition.getSource();
                        if (source instanceof FrontEndActionState)
                        {
                            final FrontEndActionState sourceActionState = (FrontEndActionState)source;
                            deferringActions.addAll(sourceActionState.getContainerActions());
                        }

                        // test for decision
                        final StateVertexFacade target = transition.getTarget();
                        if (target instanceof FrontEndPseudostate)
                        {
                            final FrontEndPseudostate targetPseudoState = (FrontEndPseudostate)target;
                            if (targetPseudoState.isDecisionPoint())
                            {
                                deferringActions.addAll(targetPseudoState.getContainerActions());
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList(deferringActions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isAllArgumentsHaveFormFields()
     */
    protected boolean handleIsAllArgumentsHaveFormFields()
    {
        final Collection arguments = this.getArguments();
        System.out.println("the args: " + arguments);
        final Collection deferringActions = this.getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (final Iterator iterator = arguments.iterator(); iterator.hasNext() && allArgumentsHaveFormFields;)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            final String parameterName = parameter.getName();
            final String parameterType = parameter.getFullyQualifiedName();

            boolean actionMissingField = false;
            for (final Iterator actionIterator = deferringActions.iterator();
                actionIterator.hasNext() && !actionMissingField;)
            {
                final FrontEndAction action = (FrontEndAction)actionIterator.next();
                final Collection actionFormFields = action.getFormFields();

                boolean fieldPresent = false;
                for (final Iterator fieldIterator = actionFormFields.iterator();
                    fieldIterator.hasNext() && !fieldPresent;)
                {
                    final ModelElementFacade field = (ModelElementFacade)fieldIterator.next();
                    if (parameterName.equals(field.getName()) && parameterType.equals(field.getFullyQualifiedName()))
                    {
                        fieldPresent = true;
                    }
                }
                actionMissingField = !fieldPresent;
            }
            allArgumentsHaveFormFields = !actionMissingField;
        }
        return allArgumentsHaveFormFields;
    }
}