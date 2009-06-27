package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Element;
import org.eclipse.uml2.StateMachine;
import org.eclipse.uml2.UseCase;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ActivityGraphFacade. It seem strange that
 * ActivityGraph are mapped with StateMachine. UML 1.4's ActivityGraph are an
 * extension of State Machine, whereas UML2's Activity is like petri-nets. This
 * explain this choice.
 *
 * @see org.andromda.metafacades.uml.ActivityGraphFacade
 */
public class ActivityGraphFacadeLogicImpl
    extends ActivityGraphFacadeLogic
{
    public ActivityGraphFacadeLogicImpl(
        final StateMachine metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getActionStates()
     */
    protected java.util.Collection handleGetActionStates()
    {
        // There is no action states within uml2's statemachine.
        // But "simple" states will do the jobs.
        return this.getStates();
    }

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getObjectFlowStates()
     */
    protected java.util.Collection handleGetObjectFlowStates()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getUseCase()
     */
    protected java.lang.Object handleGetUseCase()
    {
        Element owner = (this.metaObject).getOwner();
        if (owner instanceof UseCase)
        {
            return owner;
        }

        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ActivityGraphFacade#getPartitions()
     */
    protected java.util.Collection handleGetPartitions()
    {
        // Since we mapped ActivityGraph to StateMachine, dividers are Regions,
        // not Partitions
        return (this.metaObject).getRegions();
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