package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
       extends StateVertexFacadeLogic
       implements org.andromda.metafacades.uml.StateVertexFacade
{
    // ---------------- constructor -------------------------------
    
    public StateVertexFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.StateVertex metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    protected Object handleGetActivityGraph()
    {
        return metaObject.getContainer().getStateMachine();
    }

    protected Collection handleGetIncoming()
    {
        return metaObject.getIncoming();
    }

    protected Collection handleGetOutgoing()
    {
        return metaObject.getOutgoing();
    }
}
