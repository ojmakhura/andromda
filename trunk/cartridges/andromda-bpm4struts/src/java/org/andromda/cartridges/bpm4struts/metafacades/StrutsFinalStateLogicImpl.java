package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.ModelElementFacade;
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
    // ---------------- constructor -------------------------------

    public StrutsFinalStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // ------------- relations ------------------
    public String handleGetFullPath()
    {
        StrutsUseCase useCase = getTargetUseCase();
        return (useCase != null) ? useCase.getActionPath() : "";
    }

    protected Object handleGetTargetUseCase()
    {
        Object useCaseObject = null;
        final String name = getName();

        // first check if there is a hyperlink from this final state to a use-case
        // this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_HYPERLINK);
        if (taggedValue != null)
        {
            if (taggedValue instanceof StrutsActivityGraph)
            {
                return ((StrutsActivityGraph) taggedValue).getUseCase();
            }
            else if (taggedValue instanceof StrutsUseCase)
            {
                return taggedValue;
            }
        }

        // maybe the name points to a use-case ?
        if (name != null)
        {
            final Collection useCases = getModel().getAllUseCases();
            for (Iterator iterator = useCases.iterator(); (useCaseObject == null && iterator.hasNext());)
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
        for (Iterator iterator = allUseCases.iterator(); (useCaseObject == null && iterator.hasNext());)
        {
            ModelElementFacade facade = (ModelElementFacade) iterator.next();
            if (facade.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_APPLICATION))
                useCaseObject = facade;
        }

        return useCaseObject;
    }
}
