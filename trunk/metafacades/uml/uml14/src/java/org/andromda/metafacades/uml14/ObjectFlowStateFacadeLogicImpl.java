package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class ObjectFlowStateFacadeLogicImpl
       extends ObjectFlowStateFacadeLogic
       implements org.andromda.metafacades.uml.ObjectFlowStateFacade
{
    // ---------------- constructor -------------------------------
    
    public ObjectFlowStateFacadeLogicImpl (org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState metaObject, String context)
    {
        super (metaObject, context);
    }
    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ObjectFlowStateDecorator ...

    // ------------- relations ------------------

   /**
    *
    */
    public Object handleGetStateMachine()
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
        else
        {
            stateMachine = compositeState.getStateMachine();
        }

        return stateMachine;
    }

    // ------------------------------------------------------------
    
}
