package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.PseudostateFacade;

import java.util.Collection;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
 */
public class StrutsActivityGraphLogicImpl
    extends StrutsActivityGraphLogic
{
    public StrutsActivityGraphLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetFirstAction()
    {
        Object firstAction = null;
        final Collection initialStates = getInitialStates();
        if (!initialStates.isEmpty())
        {
            final PseudostateFacade initialState = (PseudostateFacade)initialStates.iterator().next();
            final Collection outgoing = initialState.getOutgoing();
            firstAction = (outgoing.isEmpty()) ? null : outgoing.iterator().next();
        }
        return firstAction;
    }
}
