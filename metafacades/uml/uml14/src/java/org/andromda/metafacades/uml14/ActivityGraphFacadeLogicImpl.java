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

    protected Object handleGetUseCase()
    {
        UseCase stateMachineUseCase = null;

        final Collection useCases = UML14MetafacadeUtils.getModel().getUseCases().getUseCase().refAllOfType();
        for (Iterator useCaseIterator = useCases.iterator();
             useCaseIterator.hasNext() && stateMachineUseCase == null;)
        {
            // loop over all use-cases
            final UseCase useCase = (UseCase)useCaseIterator.next();
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

    public Object getValidationOwner()
    {
        Object validationOwner = getUseCase();

        if (validationOwner == null)
        {
            validationOwner = getStateMachineContext();
        }
        if (validationOwner == null)
        {
            validationOwner = getPackage();
        }

        return validationOwner;
    }
}