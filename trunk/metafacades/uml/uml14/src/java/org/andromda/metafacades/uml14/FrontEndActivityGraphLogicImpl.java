package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndActivityGraph.
 *
 * @see org.andromda.metafacades.uml.FrontEndActivityGraph
 * @author Bob Fields
 */
public class FrontEndActivityGraphLogicImpl
    extends FrontEndActivityGraphLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActivityGraphLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getUseCase() instanceof FrontEndUseCase;
    }
    
    /**
     * Retrieves the usecase that owns this activity.
     * 
     * @see org.andromda.metafacades.uml14.ActivityGraphFacadeLogic#handleGetUseCase()
     */
    protected Object handleGetUseCase()
    {
        Object useCase = super.handleGetUseCase();
        if (useCase == null)
        {
            useCase = getModel().findUseCaseWithTaggedValueOrHyperlink(UMLProfile.TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY,
                    getName());
        }
        return useCase;
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getInitialAction()
     */
    @Override
    protected TransitionFacade handleGetInitialAction()
    {
        TransitionFacade firstAction = null;
        final Collection<PseudostateFacade> initialStates = getInitialStates();
        if (!initialStates.isEmpty())
        {
            final PseudostateFacade initialState = (PseudostateFacade)initialStates.iterator().next();
            final Collection<TransitionFacade> outgoings = initialState.getOutgoings();
            firstAction = outgoings.isEmpty() ? null : outgoings.iterator().next();
        }
        return firstAction;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getController()
     */
    @Override
    protected Object handleGetController()
    {
        Object controller = null;

        final ModelElementFacade contextElement = this.getContextElement();
        if (contextElement instanceof FrontEndController)
        {
            controller = contextElement;
        }

         // - for those tools not supporting setting the context of an activity graph (such as Poseidon)
         //   an alternative is implemented: a tagged value on the controller, specifying the name of the use-case
         // 
         // It is also allowed to set a hyperlink from the controller to the usecase
        if (controller == null)
        {
            final UseCaseFacade useCase = this.getUseCase();
            if (useCase != null)
            {
                final String useCaseName = useCase.getName();
                controller = this.getModel().findClassWithTaggedValueOrHyperlink(
                        UMLProfile.TAGGEDVALUE_PRESENTATION_CONTROLLER_USECASE, useCaseName);
            }
        }
        return controller;
    }

}