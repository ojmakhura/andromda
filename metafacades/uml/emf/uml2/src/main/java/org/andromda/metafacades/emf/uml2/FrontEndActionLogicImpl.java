package org.andromda.metafacades.emf.uml2;

import java.util.Collection;

import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndAction.
 *
 * @see org.andromda.metafacades.uml.FrontEndAction
 */
public class FrontEndActionLogicImpl
    extends FrontEndActionLogic
{
    public FrontEndActionLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#isUseCaseStart()
     */
    protected boolean handleIsUseCaseStart()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getInput()
     */
    protected java.lang.Object handleGetInput()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getParameters()
     */
    protected java.util.List handleGetParameters()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDeferredOperations()
     */
    protected java.util.List handleGetDeferredOperations()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getController()
     */
    protected java.lang.Object handleGetController()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    protected java.util.List handleGetActionStates()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDecisionTransitions()
     */
    protected java.util.List handleGetDecisionTransitions()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTransitions()
     */
    protected java.util.List handleGetTransitions()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getFormFields()
     */
    protected java.util.List handleGetFormFields()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionForwards()
     */
    protected java.util.List handleGetActionForwards()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTargetViews()
     */
    protected java.util.List handleGetTargetViews()
    {
        // TODO: add your implementation here!
        return null;
    }

    protected ParameterFacade handleFindParameter(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected void handleCopyTaggedValues(ModelElementFacade element)
    {
        // TODO Auto-generated method stub
        
    }

    protected Object handleGetTemplateParameter(String parameterName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }
}