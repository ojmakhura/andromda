package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashSet;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
 */
public class StrutsJspLogicImpl
        extends StrutsJspLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsJsp
{
    private String packageName = null;
    private String fullPath = null;
    private String titleKey = null;
    private String titleValue = null;
    private Collection actions = null;
    private Collection pageVariables = null;
    private Object useCase = null;
    private Object forward = null;
    private Collection incomingActions = null;

    // ---------------- constructor -------------------------------
    
    public StrutsJspLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsJsp ...

    // from org.andromda.metafacades.uml.ModelElementFacade
    public String getPackageName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && packageName != null) return packageName;
        ClassifierFacade classifier = (ClassifierFacade) getActivityGraph().getContextElement();
        return packageName = classifier.getPackageName();
    }

    public String getTitleKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && titleKey != null) return titleKey;
        return titleKey = StringUtilsHelper.toResourceMessageKey(getUseCase().getName() + ' ' + getName()) + ".title";
    }

    public String getTitleValue()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && titleValue != null) return titleValue;
        return titleValue = StringUtilsHelper.toPhrase(getName());
    }

    public String getFullPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && fullPath != null) return fullPath;
        return fullPath = '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/');
    }
    // ------------- relations ------------------

    protected Object handleGetUseCase()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && useCase != null) return useCase;

        final ActivityGraphFacade graph = getActivityGraph();
        if (graph instanceof StrutsActivityGraph)
        {
            return useCase = ((StrutsActivityGraph) graph).getUseCase();
        }
        return null;
    }

    protected Collection handleGetActions()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actions != null) return actions;
        return actions = getOutgoing();
    }

    protected Object handleGetForward()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && forward != null) return forward;
        return forward = getOutgoing().iterator().next();
    }

    protected Collection handleGetPageVariables()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && pageVariables != null) return pageVariables;

        final Collection variables = new LinkedList();
        final Collection incoming = getIncoming();
        for (Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            EventFacade trigger = transition.getTrigger();
            if (trigger != null)
                variables.addAll(trigger.getParameters());
        }
        return pageVariables = variables;
    }

    protected Collection handleGetIncomingActions()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && incomingActions != null) return incomingActions;

        final Collection incomingActionsList = new LinkedList();
        collectIncomingActions(this, new HashSet(), incomingActionsList);
        return incomingActions = incomingActionsList;
    }

    private void collectIncomingActions(StateVertexFacade stateVertex, Collection processedTransitions, Collection actions)
    {
        final Collection incomingTransitions = stateVertex.getIncoming();
        for (Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
        {
            TransitionFacade incomingTransition = (TransitionFacade) iterator.next();
            collectIncomingActions(incomingTransition, processedTransitions, actions);
        }
    }

    private void collectIncomingActions(TransitionFacade transition, Collection processedTransitions, Collection actions)
    {
        if (!processedTransitions.contains(transition))
        {
            processedTransitions.add(transition);
            if (transition instanceof StrutsAction)
            {
                actions.add(transition);

/*  todo: TEMPORARILY COMMENTED OUT -- needs verification that isCaseStart() forms are not populated, but I think they are
                if (((StrutsAction)transition).isUseCaseStart())
                {
                    Collection finalStates = getUseCase().getFinalStates();
                    for (Iterator iterator = finalStates.iterator(); iterator.hasNext();)
                    {
                        FinalStateFacade finalState = (FinalStateFacade) iterator.next();
                        collectIncomingActions(finalState, processedTransitions, actions);
                    }
                }
*/
            }
            else
            {
                Collection incomingTransitions = transition.getSource().getIncoming();
                for (Iterator iterator = incomingTransitions.iterator(); iterator.hasNext();)
                {
                    TransitionFacade incomingTransition = (TransitionFacade) iterator.next();
                    collectIncomingActions(incomingTransition, processedTransitions, actions);
                }
            }
        }
    }
}
