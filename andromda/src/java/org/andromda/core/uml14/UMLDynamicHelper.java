package org.andromda.core.uml14;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Extends the UMLDefaultHelper with a set of operations that are useful
 * for exploring the dynamic parts of UML v1.4 based object models.
 *
 * @author <a href="mailto:draftdog@users.sourceforge.net">Wouter Zoons</a>
 */
public class UMLDynamicHelper extends UMLDefaultHelper
{
    /**
     * Returns a collection containing all the activity graphs found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActivityGraph</code>.
     *
     * @return the ActivityGraph instances found in the UML model
     */
    public Collection getAllActivityGraphs()
    {
        return model.getActivityGraphs().getActivityGraph().refAllOfType();
    }

    /**
     * Returns a collection containing all the state machines found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.StateMachine</code>.
     *
     * @return the StateMachine instances found in the UML model
     */
    public Collection getAllStateMachines()
    {
        return model.getStateMachines().getStateMachine().refAllOfType();
    }

    /**
     * Returns a collection containing all the use-cases found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.usecases.UseCase</code>.
     *
     * @return the UseCase instances found in the UML model
     */
    public Collection getAllUseCases()
    {
        return model.getUseCases().getUseCase().refAllOfType();
    }

    /**
     * Returns a collection containing all the action states found in the
     * UML model.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActionState</code>.
     *
     * @return the ActionState instances found in the UML model
     */
    public Collection getAllActionStates()
    {
        return model.getActivityGraphs().getActionState().refAllOfType();
    }

    /**
     * Returns a collection containing all the activity graphs found associated
     * with the argument use-case.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActivityGraph</code>.
     *
     * @param useCase the use-case to query, may not be <code>null</code>
     * @return the ActivityGraph instances found associated with the argument use-case
     */
    public Collection getActivityGraphs(UseCase useCase)
    {
        return filter(useCase.getOwnedElement(), activityGraphFilter);
    }

    /**
     * Returns the state machine (such as an activity graph) to which the argument state vertex belongs.
     *
     * @param stateVertex may not be <code>null</code>
     * @return the associated state machine
     */
    public StateMachine getStateMachineContext(StateVertex stateVertex)
    {
        return stateVertex.getContainer().getStateMachine();
    }

    /**
     * Returns the collection of FinalState instances found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.FinalState</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the FinalState instances found in the StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.FinalState
     */
    public Collection getFinalStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, finalStateFilter);
    }

    /**
     * Returns the collection of Transition instances found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Transition</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the Transition instances found in the StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Transition
     */
    public Collection getTransitions(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, transitionFilter);
    }

    /**
     * Returns the collection of Pseudostate instances of kind 'initial'
     * found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the collection of initial states found in the StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     */
    public Collection getInitialStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, initialStateFilter);
    }

    /**
     * Returns the collection of ObjectFlowState instances found in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState</code>
     *
     * @param stateMachine an StateMachine instance, may not be <code>null</code>
     * @return the FinalState instances found in the StateMachine
     * @see org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
     */
    public Collection getObjectFlowStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, objectFlowStateFilter);
    }

    /**
     * Returns a Collection containing the Pseudostate instances of kind 'choice'
     * that are model elements in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the Pseudostate instances of kind 'choice'
     *    found in the argument StateMachine
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     */
    public Collection getChoices(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, choicePseudostateFilter);
    }

    /**
     * Returns a Collection containing the action states that are
     * model elements in the argument StateMachine.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActionState</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the ActionState instances found in the argument StateMachine
     * @see org.omg.uml.behavioralelements.activitygraphs.ActionState
     */
    public Collection getActionStates(StateMachine stateMachine)
    {
        return getSubvertices(stateMachine, actionStateFilter);
    }

    /**
     * Returns a collection of vertices that are contained in the argument
     * StateMachine.
     * <p>
     * The CollectionFilter decides which vertices are being filtered out.
     *
     * @param stateMachine The graph where the look for vertices, may not be <code>null</code>
     * @param collectionFilter the filter that decides which vertices to ignore, may not be <code>null</code>
     * @return A Collection containing only
     *    <code>org.omg.uml.behavioralelements.statemachines.StateVertex</code> instances.
     * @see org.omg.uml.behavioralelements.statemachines.StateVertex
     */
    public Collection getSubvertices(StateMachine stateMachine, CollectionFilter collectionFilter)
    {
        CompositeState compositeState = (CompositeState) stateMachine.getTop();
        return filter(compositeState.getSubvertex(), collectionFilter);
    }

    /**
     * Filters the specified collection using the argument filter.
     *
     * @param collection The collection to filter, may not be <code>null</code>
     * @param collectionFilter The filter to apply, may not be <code>null</code>
     * @return A subset of the argument collection, filtered out as desired
     */
    public Collection filter(Collection collection, CollectionFilter collectionFilter)
    {
        final LinkedList filteredCollection = new LinkedList();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.accept(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

    /**
     * A filter used to keep only decision points.
     *
     * @see #isDecisionPoint(Object object)
     */
    public final CollectionFilter decisionPointsFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isDecisionPoint(object);
            }
        };

    /**
     * A filter used to keep only merge points.
     *
     * @see #isMergePoint(Object object)
     */
    public final CollectionFilter mergePointsFilter =
        new CollectionFilter() {
            public boolean accept(Object object)
            {
                return isMergePoint(object);
            }
        };

    /**
     * A filter used to keep only ObjectFlowState instances.
     */
    public final CollectionFilter objectFlowStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isObjectFlowState(object);
            }
        };

    /**
     * A filter used to keep only Pseudostates of kind 'initial'.
     */
    public final CollectionFilter initialStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isInitialState(object);
            }
        };

    /**
     * A filter used to keep only Pseudostates of kind 'choice'.
     */
    public final CollectionFilter choicePseudostateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isChoice(object);
            }
        };

    /**
     * A filter used to keep only ActionStates.
     */
    public final CollectionFilter actionStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isActionState(object);
            }
        };

    /**
     * A filter used to keep only FinalStates.
     */
    public final CollectionFilter finalStateFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isFinalState(object);
            }
        };

    /**
     * A filter used to keep only Transitions.
     */
    public final CollectionFilter transitionFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isTransition(object);
            }
        };

    /**
     * A filter used to keep only ActivityGraphs.
     */
    public final CollectionFilter activityGraphFilter =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isActivityGraph(object);
            }
        };

    /**
     * Returns the first outgoing transition.
     */
    public Transition getNextStateTransition(StateVertex stateVertex)
    {
        return (Transition) stateVertex.getOutgoing().iterator().next();
    }

    /**
     * Basically the same as
     * <code>getNextStateTransition(StateVertex stateVertex)</code>, but ignores
     * any Pseudostates.
     * <p>
     * This way you can more easily get the actual Pseudostate.Choice, ObjectFlowState, FinalState
     * or ActionState.
     * <p>
     * This is actually a workaround for the fact that there is no support for recursion
     * in VTL: any number of joins may be connected like this.
     *
     * @param stateVertex A state with only one outgoing transition,
     * @return the last state reached starting from the argument state
     * @see #getNextStateTransition(StateVertex stateVertex)
     */
    public StateVertex getLastTransitionTarget(StateVertex stateVertex)
    {
        if (isActionState(stateVertex) || isDecisionPoint(stateVertex) ||
            isObjectFlowState(stateVertex) || isFinalState(stateVertex))
        {
            return stateVertex;
        }
        else
        {
            return getLastTransitionTarget(getNextStateTransition(stateVertex).getTarget());
        }
    }

    /**
     * Basically the same as <code>getLastTransitionTarget(StateVertex stateVertex)</code>, but this method will
     * only stop when it meets either an action state or a final state.
     *
     * @param stateVertex A state with only one outgoing transition,
     * @return the last action state or final state reached starting from the argument state
     * @see #getLastTransitionTarget(StateVertex stateVertex)
     */
    public StateVertex getLastTransitionState(StateVertex stateVertex)
    {
        StateVertex end = getLastTransitionTarget(stateVertex);

        if (isActionState(end) || isFinalState(end))
        {
            return end;
        }
        else
        {
            return getLastTransitionState(getNextStateTransition(end).getTarget());
        }
    }

    /**
     * Returns <code>true</code> if the argument is a Transition instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Transition instance, <code>false</code>
     *    in any other case.
     */
    public boolean isTransition(Object object)
    {
        return (object instanceof Transition);
    }

    /**
     * Returns <code>true</code> if the argument is an ActivityGraph instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is an ActivityGraph instance, <code>false</code>
     *    in any other case.
     */
    public boolean isActivityGraph(Object object)
    {
        return (object instanceof ActivityGraph);
    }

    /**
     * Returns <code>true</code> if the argument is an ActionState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is an ActionState instance, <code>false</code>
     *    in any other case.
     */
    public boolean isActionState(Object object)
    {
        return (object instanceof ActionState);
    }

    /**
     * Returns <code>true</code> if the argument is a FinalState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a FinalState instance, <code>false</code>
     *    in any other case.
     */
    public boolean isFinalState(Object object)
    {
        return (object instanceof FinalState);
    }

    /**
     * Returns <code>true</code> if the argument is a ObjectFlowState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a ObjectFlowState instance, <code>false</code>
     *    in any other case.
     */
    public boolean isObjectFlowState(Object object)
    {
        return (object instanceof ObjectFlowState);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'choice', <code>false</code> in any other case.
     * <p>
     * Please note that as well decision points as merges are represented using
     * a choice pseudostate. Their difference lies in the number of incoming and
     * outgoing transitions.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'choice', <code>false</code> in any other case.
     */
    public boolean isChoice(Object object)
    {
        return PseudostateKindEnum.PK_CHOICE.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument state vertex is a pseudostate of kind 'choice', and it has
     * more than 1 incoming transition.
     * <p>
     * Such a pseudostate would be used as a merge state in a UML diagram.
     *
     * @param object a choice pseudostate
     * @return <code>true</code> if there is more than 1 incoming transition, <code>false</code> otherwise
     * @see #isChoice(Object object)
     */
    public boolean isMergePoint(Object object)
    {
        if (isPseudostate(object))
        {
            Pseudostate pseudostate = (Pseudostate)object;
            return (isChoice(pseudostate) && (pseudostate.getIncoming().size() > 1));
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if the argument state vertex is a pseudostate of kind 'choice', and it has
     * more than 1 outgoing transition.
     * <p>
     * Such a pseudostate would be used as a decision point in a UML diagram.
     *
     * @param object a choice pseudostate
     * @return <code>true</code> if there is more than 1 ougoing transition, <code>false</code> otherwise
     * @see #isChoice(Object object)
     */
    public boolean isDecisionPoint(Object object)
    {
        if (isPseudostate(object))
        {
            Pseudostate pseudostate = (Pseudostate)object;
            return (isChoice(pseudostate) && (pseudostate.getOutgoing().size() > 1));
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns a Collection containing the decision points that are
     * model elements in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the decision points found in the argument ActivityGraph
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     * @see #isDecisionPoint(Object object)
     */
    public Collection getDecisionPoints(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, decisionPointsFilter);
    }

    /**
     * Returns a Collection containing the merge points that are
     * model elements in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the merge points found in the argument ActivityGraph
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     * @see #isMergePoint(Object object)
     */
    public Collection getMergePoints(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, mergePointsFilter);

    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'initial', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'choice', <code>false</code> in any other case.
     */
    public boolean isInitialState(Object object)
    {
        return PseudostateKindEnum.PK_INITIAL.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'join', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'join', <code>false</code> in any other case.
     */
    public boolean isJoin(Object object)
    {
        return PseudostateKindEnum.PK_JOIN.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'fork', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'fork', <code>false</code> in any other case.
     */
    public boolean isFork(Object object)
    {
        return PseudostateKindEnum.PK_FORK.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a StateVertex instance
     * of kind 'fork', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a StateVertex instance
     *    of kind 'fork', <code>false</code> in any other case.
     */
    public boolean isStateVertex(Object object)
    {
        return (object instanceof StateVertex);
    }

    public boolean isStateMachine(Object object)
    {
        return (object instanceof StateMachine);
    }

    public boolean isUseCase(Object object)
    {
        return (object instanceof UseCase);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    <code>false</code> in any other case.
     */
    public boolean isPseudostate(Object object)
    {
        return (object instanceof Pseudostate);
    }

    /**
     * Returns the kind of Pseudostate the argument is, if the argument is no
     * Pseudostate instance this method will return <code>null</code>.
     * <p>
     * In short, possible return values are
     * <ul>
     *    <li>PseudostateKindEnum.PK_CHOICE
     *    <li>PseudostateKindEnum.PK_DEEP_HISTORY
     *    <li>PseudostateKindEnum.PK_FORK
     *    <li>PseudostateKindEnum.PK_INITIAL
     *    <li>PseudostateKindEnum.PK_JOIN
     *    <li>PseudostateKindEnum.PK_JUNCTION
     *    <li>PseudostateKindEnum.PK_SHALLOW_HISTORY
     *    <li><code>null</code>
     * </ul>
     * @param object an argument to test, may be <code>null</code>
     * @return the pseudostate kind, or <code>null</code>
     */
    protected PseudostateKind getPseudostateKind(Object object)
    {
        return isPseudostate(object) ? ((Pseudostate) object).getKind() : null;
    }

}
