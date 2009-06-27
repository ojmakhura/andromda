package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.StateMachine;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndActivityGraph.
 *
 * @see org.andromda.metafacades.uml.FrontEndActivityGraph
 */
public class FrontEndActivityGraphLogicImpl
    extends FrontEndActivityGraphLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActivityGraphLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
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
     * @see org.andromda.metafacades.emf.uml22.ActivityGraphFacadeLogic#handleGetUseCase()
     */
    @Override
    protected Object handleGetUseCase()
    {
        Object useCase = super.handleGetUseCase();
        if (useCase == null)
        {
            useCase =
                this.getModel().findUseCaseWithTaggedValueOrHyperlink(
                    UMLProfile.TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY,
                    this.getName());
        }
        return useCase;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getInitialAction()
     */
    @Override
    protected Object handleGetInitialAction()
    {
        Object firstAction = null;
        final Collection<PseudostateFacade> initialStates = this.getInitialStates();
        if (!initialStates.isEmpty())
        {
            final PseudostateFacade initialState = (PseudostateFacade)initialStates.iterator().next();
            final Collection<TransitionFacade> outgoing = initialState.getOutgoings();
            firstAction = outgoing.isEmpty() ? null : outgoing.iterator().next();
        }
        return (FrontEndAction)this.shieldedElement(firstAction);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getController()
     */
    @Override
    protected Object handleGetController()
    {
        // Take the first class inside the FSM
        Class controller = null;
        for (Iterator<NamedElement> it = ((StateMachine)this.metaObject).getOwnedMembers().iterator();
            it.hasNext() && controller == null;)
        {
            Object next = it.next();
            if (next instanceof Class)
            {
                controller = (Class)next;
            }
        }
        return (FrontEndController)this.shieldedElement(controller);
    }
}
