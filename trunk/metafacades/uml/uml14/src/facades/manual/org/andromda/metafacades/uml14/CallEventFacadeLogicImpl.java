package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl
        extends CallEventFacadeLogic
        implements org.andromda.metafacades.uml.CallEventFacade
{
    // ---------------- constructor -------------------------------
    
    public CallEventFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.CallEvent metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }
    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    public java.lang.Object handleGetOperation()
    {
        return metaObject.getOperation();
    }

}
