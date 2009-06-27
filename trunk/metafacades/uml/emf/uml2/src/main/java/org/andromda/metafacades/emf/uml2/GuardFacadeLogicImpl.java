package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Constraint;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Transition;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.GuardFacade.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 */
public class GuardFacadeLogicImpl
    extends GuardFacadeLogic
{
    public GuardFacadeLogicImpl(
        final Constraint metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GuardFacade#getBody()
     */
    protected java.lang.String handleGetBody()
    {
        String body = null;
        if (this.metaObject.getSpecification() != null)
        {
            body = this.metaObject.getSpecification().stringValue();
        }
        return body;
    }

    public Object getValidationOwner()
    {
        return getTransition();
    }

    /**
     * @see org.andromda.metafacades.uml.GuardFacade#getTransition()
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
}