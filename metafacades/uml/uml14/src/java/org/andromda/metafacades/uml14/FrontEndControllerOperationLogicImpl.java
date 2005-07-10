package org.andromda.metafacades.uml14;

import java.util.ArrayList;

import org.andromda.metafacades.uml.ClassifierFacade;
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
     * Override to return the owner's package as the package name.
     * 
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetPackageName()
     */
    public String handleGetPackageName()
    {
        final ClassifierFacade owner = this.getOwner();
        return owner != null ? owner.getPackageName() : "";
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
        return new ArrayList(this.getArguments());
    }
}