package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
        final Collection parameters = new ArrayList();
        // for quick lookup we use a hashset for the argument names, we only consider parameters with a name
        // which is also present in this set
        final Set argumentNames = new HashSet();
        final Collection arguments = this.getArguments();
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final ModelElementFacade element = (ModelElementFacade)argumentIterator.next();
            argumentNames.add(element.getName());
        }
        // - get all actions deferring to this operation
        final List deferringActions = this.getDeferringActions();
        for (final Iterator iterator = deferringActions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();
            // store the action parameters
            parameters.addAll(action.getFormFields());
            // get all forwards and overwrite when we find a table (or add when not yet present)
            final List forwards = action.getActionForwards();
            for (final Iterator forwardIterator = forwards.iterator(); forwardIterator.hasNext();)
            {
                final FrontEndForward forward = (FrontEndForward)forwardIterator.next();
                // - only consider forwards directly entering a view
                if (forward.isEnteringView())
                {
                    parameters.addAll(forward.getForwardParameters());
                }
            }
        }

        parameters.addAll(arguments);
        final List uniqueParameters = UML14MetafacadeUtils.removeDuplicatesAndCopyTaggedValues(parameters);
        // - remove any parameters that are not arguments
        for (final Iterator iterator = uniqueParameters.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade parameter = (ModelElementFacade)iterator.next();
            final String name = parameter.getName();
            if (name != null)
            {
                if (!argumentNames.contains(parameter.getName()))
                {
                    iterator.remove();
                }
            }
        }
        return uniqueParameters;
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
        final Collection deferringActions = this.getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (final Iterator iterator = arguments.iterator(); iterator.hasNext() && allArgumentsHaveFormFields;)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            final String parameterName = parameter.getName();
            final ClassifierFacade parameterType = parameter.getType();
            final String parameterTypeName = parameterType != null ? parameterType.getFullyQualifiedName() : "";

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
                    final ParameterFacade field = (ParameterFacade)fieldIterator.next();
                    final ClassifierFacade fieldType = field.getType();
                    final String fieldTypeName = fieldType != null ? fieldType.getFullyQualifiedName() : "";
                    if (parameterName.equals(field.getName()) && parameterTypeName.equals(fieldTypeName))
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