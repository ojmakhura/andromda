package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ClassifierFacade;

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
    // ---------------- constructor -------------------------------
    
    public StrutsActivityGraphLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }
    // ------------- relations ------------------

    protected Object handleGetFirstAction()
    {
        PseudostateFacade initialState = (PseudostateFacade)getInitialStates().iterator().next();
        return initialState.getOutgoing().iterator().next();
    }

    protected Object handleGetUseCase()
    {
        Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsUseCase)
            {
                StrutsUseCase useCase = (StrutsUseCase)obj;
                if (this.equals(useCase.getActivityGraph()))
                {
                    return useCase;
                }
            }
        }
        return null;
    }

    protected Object handleGetController()
    {
        final ModelElementFacade contextElement = getContextElement();
        return (contextElement instanceof ClassifierFacade) ? contextElement : null;
    }
}
