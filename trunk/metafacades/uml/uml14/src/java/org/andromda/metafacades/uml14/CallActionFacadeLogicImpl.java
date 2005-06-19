package org.andromda.metafacades.uml14;



/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallActionFacade
 */
public class CallActionFacadeLogicImpl
    extends CallActionFacadeLogic
{
    public CallActionFacadeLogicImpl(
        org.omg.uml.behavioralelements.commonbehavior.CallAction metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetOperation()
    {
        return metaObject.getOperation();
    }
}