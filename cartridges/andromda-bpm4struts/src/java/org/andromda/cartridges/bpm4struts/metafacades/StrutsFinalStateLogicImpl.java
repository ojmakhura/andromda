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
    public StrutsFinalStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    public String getName()
    {
        String name = super.getName();

        if (name == null)
        {
            StrutsUseCase useCase = getTargetUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }

        return name;
    }

    public String handleGetFullPath()
    {
        StrutsUseCase useCase = getTargetUseCase();
        return (useCase != null) ? useCase.getActionPath() : "";
    }

    private Object targetUseCase = null;
    protected Object handleGetTargetUseCase()
    {
        if (targetUseCase == null)
        {
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
            final String name = super.getName();
            if (name != null)
            {
                final Collection useCases = getModel().getAllUseCases();
                for (Iterator iterator = useCases.iterator(); (targetUseCase == null && iterator.hasNext());)
                {
                    UseCaseFacade useCase = (UseCaseFacade) iterator.next();
                    if (useCase instanceof StrutsUseCase)
                    {
                        if (name.equalsIgnoreCase(useCase.getName()))
                            targetUseCase = useCase;
                    }
                }
            }
        }
        return targetUseCase;
    }
}
