package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.metafacades.uml.PseudostateFacade;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;


/**
 * Metaclass facade implementation.
 */
public class ActivityGraphFacadeLogicImpl
    extends ActivityGraphFacadeLogic
{
    public ActivityGraphFacadeLogicImpl(
        org.omg.uml.behavioralelements.activitygraphs.ActivityGraph metaObject,
        String context)
    {
        super(metaObject, context);
    }

    public Collection handleGetInitialStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Pseudostate) &&
                    (PseudostateKindEnum.PK_INITIAL.equals(((Pseudostate)object).getKind()));
                }
            };
        return getSubvertices(filter);
    }

    public Collection handleGetPseudostates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Pseudostate);
                }
            };
        return getSubvertices(filter);
    }

    public Collection handleGetActionStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof ActionState;
                }
            };
        return getSubvertices(filter);
    }

    public Collection handleGetObjectFlowStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof ObjectFlowState;
                }
            };
        return getSubvertices(filter);
    }

    public Collection handleGetFinalStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof FinalState;
                }
            };
        return getSubvertices(filter);
    }

    protected Collection getSubvertices(Predicate collectionFilter)
    {
        CompositeState compositeState = (CompositeState)metaObject.getTop();
        return filter(
            compositeState.getSubvertex(),
            collectionFilter);
    }

    private Collection filter(
        Collection collection,
        Predicate collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.evaluate(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

    protected Object handleGetContextElement()
    {
        return metaObject.getContext();
    }

    protected Collection handleGetTransitions()
    {
        return metaObject.getTransitions();
    }

    protected Object handleGetUseCase()
    {
        UseCase stateMachineUseCase = null;

        Collection useCases = UML14MetafacadeUtils.getModel().getUseCases().getUseCase().refAllOfType();
        for (Iterator useCaseIterator = useCases.iterator(); useCaseIterator.hasNext() && stateMachineUseCase == null;)
        {
            // loop over all use-cases
            UseCase useCase = (UseCase)useCaseIterator.next();
            if (useCase.getOwnedElement().contains(metaObject))
            {
                stateMachineUseCase = useCase;
            }
        }

        return stateMachineUseCase;
    }

    protected Collection handleGetPartitions()
    {
        return metaObject.getPartition();
    }

    protected Object handleGetInitialTransition()
    {
        Object transition = null;

        final PseudostateFacade initialState = getInitialState();
        if (initialState != null)
        {
            final Collection transitions = initialState.getOutgoing();
            if (!transitions.isEmpty())
            {
                transition = transitions.iterator().next();
            }
        }

        return transition;
    }

    protected Object handleGetInitialState()
    {
        Object initialState = null;

        final Collection initialStates = getInitialStates();
        if (!initialStates.isEmpty())
        {
            initialState = initialStates.iterator().next();
        }

        return initialState;
    }

    public Object getValidationOwner()
    {
        Object validationOwner = getUseCase();

        if (validationOwner == null)
        {
            validationOwner = getActivityGraphContext();
        }
        if (validationOwner == null)
        {
            validationOwner = getPackage();
        }

        return validationOwner;
    }
}