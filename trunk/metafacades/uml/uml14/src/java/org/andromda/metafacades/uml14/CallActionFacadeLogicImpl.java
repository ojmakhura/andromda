package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.commonbehavior.CallAction;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallActionFacade
 */
public class CallActionFacadeLogicImpl
       extends CallActionFacadeLogic
       implements org.andromda.metafacades.uml.CallActionFacade
{
    // ---------------- constructor -------------------------------
    
    public CallActionFacadeLogicImpl (org.omg.uml.behavioralelements.commonbehavior.CallAction metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    protected Object handleGetOperation()
    {
        CallAction callAction = metaObject;
        return callAction.getOperation();
    }
}
