package org.andromda.cartridges.bpm4struts.validator;

import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.andromda.core.uml14.UMLDynamicHelper;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class StrutsUseCaseStateValidator
{
    private UseCase useCase = null;
    private StrutsModelValidator modelValidator = null;
    private final Collection validationMessages = new LinkedList();

    public StrutsUseCaseStateValidator(UseCase useCase, StrutsModelValidator modelValidator)
    {
        this.useCase = useCase;
        this.modelValidator = modelValidator;
    }

    public Collection validate()
    {
        validationMessages.clear();

        final StrutsScriptHelper helper = modelValidator.getHelper();
        final UMLDynamicHelper dynamicHelper = helper.getDynamicHelper();

        Iterator stateMachineIterator = dynamicHelper.getStateMachines(useCase).iterator();
        if (stateMachineIterator.hasNext())
        {
            StateMachine stateMachine = (StateMachine)stateMachineIterator.next();
            Collection useCaseStates = dynamicHelper.getStates(stateMachine);
            Iterator stateIterator = useCaseStates.iterator();
            while (stateIterator.hasNext())
            {
                State state = (State) stateIterator.next();
                validate(state);
            }
        }
        else
        {

        }

        return Collections.unmodifiableCollection(validationMessages);
    }

    /**
     * Final state :
     * <ul>
     *  <li>name must be workflow trigger
     *  <li>only one incoming and no outgoing transitions
     * </ul>
     * <p>
     * Object flow state :
     * <ul>
     *  <li>must have name, type and state
     *  <li>one incoming and one outgoing transition
     * </ul>
     * <p>
     * Action state :
     * <ul>
     *  <li>name must be workflow trigger
     *  <li>one incoming and one outgoing transition
     * </ul>
     */
    private void validate(State state)
    {
        final StrutsScriptHelper helper = modelValidator.getHelper();
        final UMLDynamicHelper dynamicHelper = helper.getDynamicHelper();

        int incomingCount = state.getIncoming().size();
        int outgoingCount = state.getOutgoing().size();

        if (dynamicHelper.isFinalState(state))
        {
            if (outgoingCount > 0)
                validationMessages.add(new ValidationWarning(useCase, "FinalState has outgoing transitions"));
            if (incomingCount != 1)
                validationMessages.add(new ValidationWarning(useCase, "FinalState must have only one incoming transition"));

            UseCase nextUseCase = helper.findNextUseCaseInWorkflow(useCase, state.getName());
            if (nextUseCase == null)
                validationMessages.add(new ValidationError(useCase, "FinalState name should reflect workflow transition trigger"));
        }
        else if (dynamicHelper.isObjectFlowState(state))
        {
            if (incomingCount != 1)
                validationMessages.add(new ValidationWarning(useCase, "ObjectFlowState must have only one incoming transition"));
            if (outgoingCount != 1)
                validationMessages.add(new ValidationWarning(useCase, "ObjectFlowState must have only one outgoing transition"));

            ObjectFlowState objectFlowState = (ObjectFlowState)state;

            if (objectFlowState.getName() == null)
                validationMessages.add(new ValidationError(useCase, "ObjectFlowState must have a name"));
            if (objectFlowState.getType() == null)
                validationMessages.add(new ValidationError(useCase, "ObjectFlowState must have a type"));
            if (helper.getObjectFlowStateState(objectFlowState) == null)
                validationMessages.add(new ValidationError(useCase, "ObjectFlowState must have a state"));
        }
        else if (dynamicHelper.isActionState(state))
        {
            if (incomingCount != 1)
                validationMessages.add(new ValidationWarning(useCase, "ActionState must have only one incoming transition"));
            if (outgoingCount != 1)
                validationMessages.add(new ValidationWarning(useCase, "ActionState must have only one outgoing transition"));
        }
        else
        {
            validationMessages.add(new ValidationError(useCase, "Invalid state, must be one of: FinalState, ObjectFlowState, ActionState"));
        }
    }
}
