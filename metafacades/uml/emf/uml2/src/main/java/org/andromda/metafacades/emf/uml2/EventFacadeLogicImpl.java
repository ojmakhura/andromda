package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EventFacade.
 *
 * @see org.andromda.metafacades.uml.EventFacade
 */
public class EventFacadeLogicImpl
    extends EventFacadeLogic
{
    public EventFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getTransition()
     */
    protected java.lang.Object handleGetTransition()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getParameters()
     */
    protected java.util.Collection handleGetParameters()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getState()
     */
    protected java.lang.Object handleGetState()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        Object validationOwner = this.getTransition();
        if (validationOwner == null)
        {
            validationOwner = this.getState();
        }
        return validationOwner;
    }
}