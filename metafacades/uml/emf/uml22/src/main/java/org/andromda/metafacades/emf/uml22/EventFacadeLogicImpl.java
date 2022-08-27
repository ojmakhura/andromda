package org.andromda.metafacades.emf.uml22;

import java.util.Collection;

import org.andromda.metafacades.uml.AttributeFacade;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

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
    private static final long serialVersionUID = 34L;
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
        final Element owner = this.metaObject.getOwner();
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
        return this.metaObject.getOwnedParameters();
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getState()
     */
    @Override
    protected State handleGetState()
    {
        final Element owner = this.metaObject.getOwner();
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

    @Override
    protected Collection<AttributeFacade> handleGetAttributes() {
        
        // TODO Auto-generated method stub
        return this.shieldedElements(UmlUtilities.getAttributes(
            this.metaObject,
            false));
    }
}
