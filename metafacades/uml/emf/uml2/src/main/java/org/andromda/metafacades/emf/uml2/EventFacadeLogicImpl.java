package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import org.eclipse.uml2.Activity;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Parameter;
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
    /**
     * @param metaObject
     * @param context
     */
    public EventFacadeLogicImpl(
        final Activity metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getTransition()
     */
    @Override
    protected Transition handleGetTransition()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof Transition)
        {
            return (Transition) owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getParameters()
     */
    @Override
    protected Collection<Parameter> handleGetParameters()
    {
        // TODO getOwnedParameters?
        return this.metaObject.getParameters();
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getState()
     */
    @Override
    protected State handleGetState()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof State)
        {
            return (State)owner;
        }
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
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