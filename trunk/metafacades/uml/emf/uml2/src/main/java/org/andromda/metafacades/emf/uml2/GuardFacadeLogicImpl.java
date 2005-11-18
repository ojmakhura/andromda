package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.GuardFacade.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 */
public class GuardFacadeLogicImpl
    extends GuardFacadeLogic
{
    public GuardFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GuardFacade#getBody()
     */
    protected java.lang.String handleGetBody()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.GuardFacade#getTransition()
     */
    protected java.lang.Object handleGetTransition()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getTransition();
    }
}