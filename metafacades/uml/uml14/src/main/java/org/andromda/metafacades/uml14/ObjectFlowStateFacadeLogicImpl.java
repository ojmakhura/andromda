package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.foundation.core.Classifier;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ObjectFlowStateFacadeLogicImpl
    extends ObjectFlowStateFacadeLogic
{
    private static final long serialVersionUID = 744240821518303756L;

    /**
     * @param metaObject
     * @param context
     */
    public ObjectFlowStateFacadeLogicImpl(
        ObjectFlowState metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.StateVertexFacadeLogicImpl#handleGetStateMachine()
     */
    @Override
    protected StateMachine handleGetStateMachine()
    {
        StateMachine stateMachine = null;
        CompositeState compositeState = metaObject.getContainer();

        if (compositeState != null)
        {
            while (compositeState != null)
            {
                stateMachine = compositeState.getStateMachine();
                compositeState = compositeState.getContainer();
            }
        }

        return stateMachine;
    }

    /**
     * @see org.andromda.metafacades.uml14.ObjectFlowStateFacadeLogic#handleGetType()
     */
    protected Classifier handleGetType()
    {
        return metaObject.getType();
    }
}