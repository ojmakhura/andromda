package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Element;
import org.eclipse.uml2.State;
import org.eclipse.uml2.Transition;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ActionFacade.
 *
 * @see org.andromda.metafacades.uml.ActionFacade
 */
public class ActionFacadeLogicImpl
    extends ActionFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ActionFacadeLogicImpl(
        final org.eclipse.uml2.Action metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return owner
     * @see org.andromda.metafacades.uml.ActionFacade#getTransition()
     */
    protected Object handleGetTransition()
    {
        final Element owner = this.metaObject.getActivity().getOwner();
        return owner instanceof Transition ? owner : null;
    }

    /**
     * @return owner
     * @see org.andromda.metafacades.uml.ActionFacade#getActionState()
     */
    protected Object handleGetActionState()
    {
        final Element owner = this.metaObject.getActivity().getOwner();
        return owner instanceof State ? owner : null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        Object validationOwner = getTransition();

        if (validationOwner == null)
        {
            validationOwner = getActionState();
        }

        return validationOwner;
    }
}