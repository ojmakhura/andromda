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

        StrutsUseCase useCase = getTargetUseCase();
        return (useCase != null) ? useCase.getActionPath() : "";
    }

    protected Object handleGetTargetUseCase()
    {
        final String name = getName();
        if (name != null)
        {
            final Collection useCases = getModel().getAllUseCases();
            for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
            {
                UseCaseFacade useCase = (UseCaseFacade) iterator.next();
                if (useCase instanceof StrutsUseCase)
                {
                    if (name.equalsIgnoreCase(useCase.getName()))
                        return useCase;
                }
            }
        }
        return null;
    }
}
