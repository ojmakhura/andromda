package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
 */
public class StrutsFinalStateLogicImpl
        extends StrutsFinalStateLogic
{
    public StrutsFinalStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacad#getName()
     */
    public String getName()
    {
        String name = super.getName();

        if (name == null)
        {
            final UseCaseFacade useCase = this.getTargetUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }

        return name;
    }

    protected String handleGetFullPath()
    {
        String fullPath = null;

        StrutsUseCase useCase = (StrutsUseCase)this.getTargetUseCase();
        if (useCase == null)
        {
            // perhaps this final state links outside of the UML model
            final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_WEBPAGELINK);
            if (taggedValue == null)
            {
                String name = getName();
                if (name != null && (name.startsWith("/") || name.startsWith("http://")))
                {
                    fullPath = name;
                }
            }
            else
            {
                fullPath = String.valueOf(taggedValue);
            }
        }
        else
        {
            fullPath = useCase.getActionPath() + ".do";
        }

        return fullPath;
    }

    /**
     * 
     * @see org.andromda.metafacades.uml.FrontEndFinalState#getTargetUseCase()
     */
    public FrontEndUseCase getTargetUseCase()
    {
        FrontEndUseCase targetUseCase = null;

        // first check if there is a hyperlink from this final state to a use-case
        // this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK);
        if (taggedValue != null)
        {
            if (taggedValue instanceof StrutsActivityGraph)
            {
                targetUseCase = (FrontEndUseCase)((StrutsActivityGraph)taggedValue).getUseCase();
            }
            else if (taggedValue instanceof StrutsUseCase)
            {
                targetUseCase = (FrontEndUseCase)taggedValue;
            }
        }
        else // maybe the name points to a use-case ?
        {
            final String name = super.getName();
            if (StringUtils.isNotBlank(name))
            {
                UseCaseFacade useCase = getModel().findUseCaseByName(name);
                if (useCase instanceof FrontEndUseCase)
                {
                    targetUseCase = (FrontEndUseCase)useCase;
                }
            }
        }
        return targetUseCase;
    }

    protected List handleGetActions()
    {
        Set actions = new HashSet();
        Collection incoming = this.getIncoming();

        for (final Iterator incomingIterator = incoming.iterator(); incomingIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward)incomingIterator.next();
            actions.addAll(forward.getActions());
        }
        return new ArrayList(actions);
    }

    protected List handleGetInterUseCaseParameters()
    {
        // we don't want to list parameters with the same name to we use a hash map
        final Map parameterMap = new HashMap();

        final Collection transitions = getIncoming();
        for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
        {
            final StrutsForward forward = (StrutsForward)transitionIterator.next();
            final List forwardParameters = forward.getForwardParameters();
            for (int i = 0; i < forwardParameters.size(); i++)
            {
                final ModelElementFacade parameter = (ModelElementFacade)forwardParameters.get(i);
                parameterMap.put(parameter.getName(), parameter);
            }
        }

        return new ArrayList(parameterMap.values());
    }
}
