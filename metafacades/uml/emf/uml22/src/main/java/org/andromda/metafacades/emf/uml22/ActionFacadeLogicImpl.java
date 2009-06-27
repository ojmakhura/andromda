package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.TransitionFacade;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ActionFacade.
 *
 * @see org.andromda.metafacades.uml.ActionFacade
 */
public class ActionFacadeLogicImpl
    extends ActionFacadeLogic
{
    /**
     * @param metaObjectIn
     * @param context
     */
    public ActionFacadeLogicImpl(
        final Action metaObjectIn,
        final String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ActionFacade#getTransition()
     */
    @Override
    protected Transition handleGetTransition()
    {
        final Element owner = this.metaObject.getActivity().getOwner();
        return (Transition)(owner instanceof Transition ? owner : null);
    }

    /**
     * @see org.andromda.metafacades.uml.ActionFacade#getActionState()
     */
    @Override
    protected State handleGetActionState()
    {
        final Element owner = this.metaObject.getActivity().getOwner();
        return (State)(owner instanceof State ? owner : null);
    }

    /**
     * @return validationOwner = getActionState()
     */
    //@Override
    public TransitionFacade handleGetValidationOwner()
    {
        Object validationOwner = getTransition();

        if (validationOwner == null)
        {
            validationOwner = getActionState();
        }

        return (TransitionFacade)validationOwner;
    }
}
