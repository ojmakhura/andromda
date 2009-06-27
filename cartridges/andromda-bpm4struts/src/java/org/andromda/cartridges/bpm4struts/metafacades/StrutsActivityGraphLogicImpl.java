package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;
import org.andromda.metafacades.uml.PseudostateFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
 * @author Bob Fields
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
            final Collection outgoings = initialState.getOutgoings();
            firstAction = (outgoings.isEmpty()) ? null : outgoings.iterator().next();
        }
        return firstAction;
    }
}
