package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.CallOperationAction;


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
    public CallActionFacadeLogicImpl(
        final CallOperationAction metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallActionFacade#getOperation()
     */
    protected java.lang.Object handleGetOperation()
    {
        return this.metaObject.getOperation();
    }
}