package org.andromda.core.uml14;

import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.andromda.core.common.CollectionFilter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;

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
     * Filters the argument non-null collection from all objects that are no ModelElement instances that do not have
     * a stereotype with the argument stereotype name.
     *
     * @param collection A collection of objects that will be filtered, any Object may be found inside, even
     *  <code>null</code>.
     * @param stereotypeName The name of the stereotype on which to filter the collection
     * @return A Collection of that is a subset of the argument collection, all elements are guarantueed
     *  to be ModelElement instances that have at least one Stereotype with the specified name.
     */
    public Collection filterWithStereotypeName(Collection collection, final String stereotypeName)
    {
        final CollectionFilter stereotypeFilter =
            new CollectionFilter()
            {
                public boolean accept(Object object)
                {
                    return hasStereotypeWithName(object, stereotypeName);
                }
            };
        return filter(collection, stereotypeFilter);
    }

    /**
     * Returns the state machine (such as an activity graph) to which the argument state vertex belongs.
     *
     * @param stateVertex may not be <code>null</code>
     * @return the associated state machine
     */
    public StateMachine getStateMachine(StateVertex stateVertex)
    {
        return stateVertex.getContainer().getStateMachine();
    }

    /**
     * Returns the collection of FinalState instances found in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.FinalState</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the FinalState instances found in the activity graph
     * @see org.omg.uml.behavioralelements.statemachines.FinalState
     */
    public Collection getFinalStates(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, finalStateFilter);
    }

    /**
     * Returns the collection of Pseudostate instances of kind 'initial'
     * found in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the collection of initial states found in the activity graph
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     */
    public Collection getInitialStates(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, initialStateFilter);
    }

    /**
     * Returns the collection of ObjectFlowState instances found in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the FinalState instances found in the activity graph
     * @see org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState
     */
    public Collection getObjectFlowStates(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, objectFlowStateFilter);
    }

    /**
     * Returns a Collection containing the Pseudostate instances of kind 'choice'
     * that are model elements in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the Pseudostate instances of kind 'choice'
     *    found in the argument ActivityGraph
     * @see org.omg.uml.behavioralelements.statemachines.Pseudostate
     */
    public Collection getChoices(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, choicePseudostateFilter);
    }

    /**
     * Returns a Collection containing the action states that are
     * model elements in the argument activity graph.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.activitygraphs.ActionState</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the ActionState instances found in the argument ActivityGraph
     * @see org.omg.uml.behavioralelements.activitygraphs.ActionState
     */
    public Collection getActionStates(ActivityGraph activityGraph)
    {
        return getSubvertices(activityGraph, actionStateFilter);
    }

    /**
     * Returns a collection of vertices that are contained in the argument
     * ActivityGraph.
     * <p>
     * The CollectionFilter decides which vertices are being filtered out.
     *
     * @param activityGraph The graph where the look for vertices, may not be <code>null</code>
     * @param collectionFilter the filter that decides which vertices to ignore, may not be <code>null</code>
     * @return A Collection containing only
     *    <code>org.omg.uml.behavioralelements.statemachines.StateVertex</code> instances.
     * @see org.omg.uml.behavioralelements.statemachines.StateVertex
     */
    public Collection getSubvertices(ActivityGraph activityGraph, CollectionFilter collectionFilter)
    {
        CompositeState compositeState = (CompositeState) activityGraph.getTop();
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

    public boolean hasStereotypeWithName(Object object, String stereotypeName)
    {
        if ((object instanceof ModelElement) && (stereotypeName != null))
        {
            ModelElement modelElement = (ModelElement) object;
            Stereotype stereotype = (Stereotype) modelElement.getStereotype().iterator().next();
            if (stereotype == null)
            {
                return false;
            }
            else
            {
                return (stereotypeName.equals(stereotype.getName()));
            }
        }
        else
        {
            return false;
        }
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
