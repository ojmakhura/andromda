package org.andromda.metafacades.uml14;

import java.util.Collections;
import java.util.List;
import org.andromda.metafacades.uml.OperationFacade;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.behavioralelements.statemachines.CallEvent;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.CallEventFacade
 * @author Bob Fields
 */
public class CallEventFacadeLogicImpl
    extends CallEventFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public CallEventFacadeLogicImpl(
        CallEvent metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    @Override
    public Operation handleGetOperation()
    {
        return metaObject.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperations()
     */
    @Override
    protected List<OperationFacade> handleGetOperations()
    {
        final Object operation = this.getOperation();
        return operation == null ? Collections.EMPTY_LIST : Collections.singletonList(operation);
    }
}