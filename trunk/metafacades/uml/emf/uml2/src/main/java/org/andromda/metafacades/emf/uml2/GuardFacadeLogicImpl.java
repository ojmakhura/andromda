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
    private static final long serialVersionUID = 34L;
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
     * @return metaObject.getSpecification().stringValue()
     * @see org.andromda.metafacades.uml.GuardFacade#getBody()
     */
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
    public Object getValidationOwner()
    {
        return getTransition();
    }

    /**
     * @return owner
     * @see org.andromda.metafacades.uml.GuardFacade#getTransition()
     */
    protected Object handleGetTransition()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof Transition)
        {
            return owner;
        }
        return null;
    }
}