package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.commonbehavior.CallAction;
import org.omg.uml.foundation.core.Operation;

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

    /**
     * @see org.andromda.metafacades.uml14.CallActionFacadeLogic#handleGetOperation()
     */
    protected Operation handleGetOperation()
    {
        return metaObject.getOperation();
    }
}