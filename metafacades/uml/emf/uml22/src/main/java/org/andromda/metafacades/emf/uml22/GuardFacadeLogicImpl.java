package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Transition;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.GuardFacade.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 */
public class GuardFacadeLogicImpl
    extends GuardFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public GuardFacadeLogicImpl(
        final Constraint metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GuardFacade#getBody()
     */
    @Override
    protected String handleGetBody()
    {
        String body = null;
        if (this.metaObject.getSpecification() != null)
        {
            body = this.metaObject.getSpecification().stringValue();
        }
        return body;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return getTransition();
    }

    /**
     * @see org.andromda.metafacades.uml.GuardFacade#getTransition()
     */
    @Override
    protected Transition handleGetTransition()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof Transition)
        {
            return (Transition)owner;
        }
        return null;
    }
}
