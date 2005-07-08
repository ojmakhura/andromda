package org.andromda.metafacades.uml14;

import java.util.ArrayList;

import org.andromda.metafacades.uml.FrontEndController;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndControllerOperation.
 *
 * @see org.andromda.metafacades.uml.FrontEndControllerOperation
 */
public class FrontEndControllerOperationLogicImpl
    extends FrontEndControllerOperationLogic
{
    public FrontEndControllerOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isOwnerIsController()
     */
    protected boolean handleIsOwnerIsController()
    {
        return this.getOwner() instanceof FrontEndController;
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getFormFields()
     */
    protected java.util.List handleGetFormFields()
    {
        return new ArrayList(this.getParameters());
    }
}