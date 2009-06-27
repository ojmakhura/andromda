package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.core.Operation;
import org.omg.uml.behavioralelements.commonbehavior.CallAction;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallActionFacade
 * @author Bob Fields
 */
public class CallActionFacadeLogicImpl
    extends CallActionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public CallActionFacadeLogicImpl(
        CallAction metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected Operation handleGetOperation()
    {
        return metaObject.getOperation();
    }
}