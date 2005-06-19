package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl
    extends CallEventFacadeLogic
{
    public CallEventFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.CallEvent metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    public java.lang.Object handleGetOperation()
    {
        return metaObject.getOperation();
    }
}