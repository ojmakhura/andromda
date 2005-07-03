package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.usecases.UseCase;


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

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getActionStates()
     */
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

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getObjectFlowStates()
     */
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

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        UseCase stateMachineUseCase = null;

        final Collection useCases = UML14MetafacadeUtils.getModel().getUseCases().getUseCase().refAllOfType();
        for (final Iterator useCaseIterator = useCases.iterator();
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

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getPartitions()
     */
    protected Collection handleGetPartitions()
    {
        return metaObject.getPartition();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
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