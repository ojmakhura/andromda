package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;


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

    protected String handleGetFullPath()
    {
        StrutsUseCase useCase = getTargetUseCase();
        return (useCase != null) ? useCase.getActionPath() : "";
    }

    protected Object handleGetTargetUseCase()
    {
        Object targetUseCase = null;

        // first check if there is a hyperlink from this final state to a use-case
        // this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK);
        if (taggedValue != null)
        {
            if (taggedValue instanceof StrutsActivityGraph)
            {
                targetUseCase = ((StrutsActivityGraph) taggedValue).getUseCase();
            }
            else if (taggedValue instanceof StrutsUseCase)
            {
                targetUseCase = taggedValue;
            }
        }
        else // maybe the name points to a use-case ?
        {
            final String name = super.getName();
            if (StringUtils.isNotBlank(name))
            {
                UseCaseFacade useCase = getModel().findUseCaseByName(name);
                if (useCase instanceof StrutsUseCase)
                {
                    targetUseCase = useCase;
                }
            }
        }
        return targetUseCase;
    }

    protected Collection handleGetActions()
    {
        Set actions = new HashSet();
        Collection incoming = this.getIncoming();

        for (Iterator incomingIterator = incoming.iterator(); incomingIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward) incomingIterator.next();
            actions.addAll(forward.getActions());
        }
        return actions;
    }
}
