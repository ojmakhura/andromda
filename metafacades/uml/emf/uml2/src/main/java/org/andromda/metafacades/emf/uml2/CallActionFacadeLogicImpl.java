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
    private static final long serialVersionUID = 34L;
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
     * @return metaObject.getOperation()
     * @see org.andromda.metafacades.uml.CallActionFacade#getOperation()
     */
    protected Object handleGetOperation()
    {
        return this.metaObject.getOperation();
    }
}