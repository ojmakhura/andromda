package org.andromda.cartridges.bpm4struts.validator;

import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.State;
import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.andromda.core.uml14.UMLDynamicHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collections;

public class StrutsWorkflowStateValidator
{
    private UseCase workflow = null;
    private StrutsModelValidator modelValidator = null;
    private final Collection validationMessages = new LinkedList();

    public StrutsWorkflowStateValidator(UseCase workflow, StrutsModelValidator modelValidator)
    {
        this.workflow = workflow;
        this.modelValidator = modelValidator;
    }

    public Collection validate()
    {
        validationMessages.clear();

        final StrutsScriptHelper helper = modelValidator.getHelper();
        final UMLDynamicHelper dynamicHelper = helper.getDynamicHelper();

        Iterator stateMachineIterator = dynamicHelper.getStateMachines(workflow).iterator();
        if (stateMachineIterator.hasNext())
        {
            StateMachine stateMachine = (StateMachine)stateMachineIterator.next();
            Collection workflowStates = dynamicHelper.getStates(stateMachine);
            Iterator stateIterator = workflowStates.iterator();
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

    private void validate(State state)
    {

    }
}

