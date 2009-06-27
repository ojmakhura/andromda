package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Operation;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.CallActionFacade. Note: It's not used in
 * BPM4Struts.
 *
 * @see org.andromda.metafacades.uml.CallActionFacade
 */
public class CallActionFacadeLogicImpl
    extends CallActionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public CallActionFacadeLogicImpl(
        final CallOperationAction metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallActionFacade#getOperation()
     */
    @Override
    protected Operation handleGetOperation()
    {
        return this.metaObject.getOperation();
    }
}
