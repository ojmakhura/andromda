package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.core.metafacade.MetafacadeBase;

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
    private Object targetUseCase = null;

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
        if (Bpm4StrutsProfile.ENABLE_CACHE && targetUseCase != null) return targetUseCase;

        Object useCaseObject = null;
        final String name = getName();

        if (name != null)
        {
            final Collection useCases = getModel().getAllUseCases();
            for (Iterator iterator = useCases.iterator(); (useCaseObject==null && iterator.hasNext());)
            {
                UseCaseFacade useCase = (UseCaseFacade) iterator.next();
                if (useCase instanceof StrutsUseCase)
                {
                    if (name.equalsIgnoreCase(useCase.getName()))
                        useCaseObject = useCase;
                }
            }
        }

        final Collection allUseCases = getModel().getAllUseCases();
        for (Iterator iterator = allUseCases.iterator(); (useCaseObject==null && iterator.hasNext());)
        {
            ModelElementFacade facade = (ModelElementFacade) iterator.next();
            if (facade.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_APPLICATION))
                useCaseObject = facade;
        }

        return targetUseCase = useCaseObject;
    }
}
