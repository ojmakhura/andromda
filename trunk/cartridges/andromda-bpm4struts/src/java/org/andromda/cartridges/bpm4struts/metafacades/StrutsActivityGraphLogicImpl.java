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
    
    protected Object handleGetController()
    {
        Object controller = null;

        final ModelElementFacade contextElement = getContextElement();
        if (contextElement instanceof StrutsController)
        {
            controller = contextElement;
        }

        /*
         * for those tools not supporting setting the context of an activity graph (such as Poseidon)
         * an alternative is implemented: a tagged value on the controller, specifying the name of the use-case
         *
         * It is also allowed to set a hyperlink from the controller to the usecase
         */
        if (controller == null)
        {
            final UseCaseFacade useCase = this.getUseCase();
            if (useCase != null)
            {
                final String useCaseName = useCase.getName();
                controller = getModel().findClassWithTaggedValueOrHyperlink(
                        UMLProfile.TAGGEDVALUE_PRESENTATION_CONTROLLER_USECASE, useCaseName);
            }
        }

        return controller;
    }
}
