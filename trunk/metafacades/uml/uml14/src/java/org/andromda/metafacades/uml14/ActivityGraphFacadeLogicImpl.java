package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ActivityGraphFacadeLogicImpl
    extends ActivityGraphFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ActivityGraphFacadeLogicImpl(
        ActivityGraph metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getActionStates()
     */
    @Override
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
    @Override
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
    @Override
    protected Object handleGetUseCase()
    {
        UseCase stateMachineUseCase = null;

        final Collection<UseCase> useCases = UML14MetafacadeUtils.getModel().getUseCases().getUseCase().refAllOfType();
        for (final Iterator<UseCase> useCaseIterator = useCases.iterator();
             useCaseIterator.hasNext() && stateMachineUseCase == null;)
        {
            // loop over all use-cases
            final UseCase useCase = useCaseIterator.next();
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
    @Override
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