package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.UseCaseFacade;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
 */
public class StrutsFinalStateLogicImpl
        extends StrutsFinalStateLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
{
    private String fullPath = null;

    // ---------------- constructor -------------------------------
    
    public StrutsFinalStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // ------------- relations ------------------
    public String getFullPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && fullPath != null) return fullPath;

        final String name = getName();

        if (name != null)
        {
            final Collection useCases = getModel().getAllUseCases();
            for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
            {
                UseCaseFacade useCase = (UseCaseFacade) iterator.next();
                if (useCase instanceof StrutsUseCase)
                {
                    StrutsUseCase strutsUseCase = (StrutsUseCase) useCase;
                    if (name.equalsIgnoreCase(strutsUseCase.getName()))
                        return fullPath = ((StrutsUseCase) useCase).getActionPath();
                }
            }
        }

        return fullPath = "";
    }
}
