package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
 */
public class StrutsActivityGraphLogicImpl
        extends StrutsActivityGraphLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
{
    private Object firstAction = null;
    private Object useCase = null;
    private Object controller = null;

    // ---------------- constructor -------------------------------
    
    public StrutsActivityGraphLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }
    // ------------- relations ------------------

    protected Object handleGetFirstAction()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && firstAction != null) return firstAction;

        PseudostateFacade initialState = (PseudostateFacade) getInitialStates().iterator().next();
        return firstAction = initialState.getOutgoing().iterator().next();
    }

    protected Object handleGetUseCase()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && useCase != null) return useCase;

        Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsUseCase)
            {
                StrutsUseCase strutsUseCase = (StrutsUseCase) obj;
                if (this.equals(strutsUseCase.getActivityGraph()))
                {
                    return useCase = strutsUseCase;
                }
            }
        }
        return null;
    }

    protected Object handleGetController()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && controller != null) return controller;

        final ModelElementFacade contextElement = getContextElement();
        return controller = (contextElement instanceof ClassifierFacade) ? contextElement : null;
    }
}
