package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class FinalStateFacadeLogicImpl
       extends FinalStateFacadeLogic
       implements org.andromda.metafacades.uml.FinalStateFacade
{
    // ---------------- constructor -------------------------------
    
    public FinalStateFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.FinalState metaObject, String context)
    {
        super (metaObject, context);
    }
    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class FinalStateDecorator ...

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
