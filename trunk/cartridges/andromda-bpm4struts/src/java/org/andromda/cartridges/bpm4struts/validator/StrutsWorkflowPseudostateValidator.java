package org.andromda.cartridges.bpm4struts.validator;

import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.andromda.core.uml14.UMLDynamicHelper;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class StrutsWorkflowPseudostateValidator
{
    private UseCase workflow = null;
    private StrutsModelValidator modelValidator = null;
    private final Collection validationMessages = new LinkedList();

    public StrutsWorkflowPseudostateValidator(UseCase workflow, StrutsModelValidator modelValidator)
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
            Collection workflowPseudostates = dynamicHelper.getPseudostates(stateMachine);
            Iterator stateIterator = workflowPseudostates.iterator();
            while (stateIterator.hasNext())
            {
                Pseudostate pseudostate = (Pseudostate) stateIterator.next();
                validate(pseudostate);
            }
        }
        else
        {

        }

        return Collections.unmodifiableCollection(validationMessages);
    }

    /**
     * Initial state :
     * <ul>
     *  <li>can have only one outgoing transition, no incoming transitions
     *  <li>cannot be anonymous
     * </ul>
     * <p>
     * Merge point :
     * <ul>
     *  <li>outgoing transitions should not have guards nor triggers
     * </ul>
     * <p>
     * Decision point :
     * <ul>
     *  <li>all outgoing transitions must have triggers (guards not allowed)
     * </ul>
     * <p>
     * All others :
     * <ul>
     *  <li>are not recognized pseudostates
     * </ul>
     * <p>
     */
    private void validate(Pseudostate pseudostate)
    {
        final StrutsScriptHelper helper = modelValidator.getHelper();
        final UMLDynamicHelper dynamicHelper = helper.getDynamicHelper();

        if (dynamicHelper.isInitialState(pseudostate))
        {
            int incomingCount = pseudostate.getIncoming().size();
            int outgoingCount = pseudostate.getOutgoing().size();

            if (incomingCount > 0)
                validationMessages.add(new ValidationWarning(workflow, "Initial state has incoming transitions"));
            if (outgoingCount != 1)
                validationMessages.add(new ValidationWarning(workflow, "Initial state must have only one outgoing transition"));
        }
        else if (dynamicHelper.isMergePoint(pseudostate))
        {
            Collection outgoing = pseudostate.getOutgoing();
            for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
            {
                Transition transition = (Transition) iterator.next();
                if (transition.getGuard() != null)
                    validationMessages.add(new ValidationWarning(workflow, "Merge points should not have outgoing transitions with a guard"));
                if (transition.getTrigger() != null)
                    validationMessages.add(new ValidationWarning(workflow, "Merge points should not have outgoing transitions with a trigger"));
            }
        }
        else if (dynamicHelper.isDecisionPoint(pseudostate))
        {
            int guardedCount = 0;
            int triggeredCount = 0;

            Collection outgoing = pseudostate.getOutgoing();
            for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
            {
                Transition transition = (Transition) iterator.next();
                if (transition.getGuard() != null) guardedCount++;
                if (transition.getTrigger() != null) triggeredCount++;
            }

            if ((guardedCount != 0) && (guardedCount != outgoing.size()))
                validationMessages.add(new ValidationWarning(workflow, "Guarded transitions not allowed in workflows"));
            if ((triggeredCount != 0) && (triggeredCount != outgoing.size()))
                validationMessages.add(new ValidationError(workflow, "A decision point's outgoing transitions should all be triggered"));
        }
        else
        {
            validationMessages.add(new ValidationError(workflow, "Invalid pseudostate, must be one of: InitialState, DecisionPoint (one-to-many), MergePoint (many-to-one), found ("+pseudostate.getName()+"): "+pseudostate));
        }
    }
}
