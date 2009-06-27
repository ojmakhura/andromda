package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Element;
import org.eclipse.uml2.State;
import org.eclipse.uml2.Transition;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EventFacade.
 * UML1.4 Event are mapped to UML2 Activity (because UML2 Event doesn't contain
 * parameter)
 *
 * @see org.andromda.metafacades.uml.EventFacade
 */
public class EventFacadeLogicImpl
    extends EventFacadeLogic
{
    public EventFacadeLogicImpl(
        final org.eclipse.uml2.Activity metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getTransition()
     */
    protected java.lang.Object handleGetTransition()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof Transition)
        {
            return owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getParameters()
     */
    protected java.util.Collection handleGetParameters()
    {
        return this.metaObject.getParameters();
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getState()
     */
    protected java.lang.Object handleGetState()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof State)
        {
            return owner;
        }
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