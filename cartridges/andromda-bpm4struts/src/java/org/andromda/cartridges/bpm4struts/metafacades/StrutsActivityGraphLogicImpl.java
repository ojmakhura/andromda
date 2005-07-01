package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;

import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
 */
public class StrutsActivityGraphLogicImpl
        extends StrutsActivityGraphLogic
{
    public StrutsActivityGraphLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetFirstAction()
    {
        Object firstAction = null;
        final Collection initialStates = getInitialStates();
        if (initialStates.isEmpty() == false)
        {
            final PseudostateFacade initialState = (PseudostateFacade)initialStates.iterator().next();
            final Collection outgoing = initialState.getOutgoing();
            firstAction = (outgoing.isEmpty()) ? null : outgoing.iterator().next();
        }
        return firstAction;
    }
}
