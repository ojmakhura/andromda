package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

import java.util.*;


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
    private String messageKey = null;
    private String titleKey = null;
    private String titleValue = null;
    private Collection actions = null;
    private Collection pageVariables = null;
    private Object useCase = null;
    private Object forward = null;
    private Collection incomingActions = null;
    private String documentationKey = null;
    private String documentationValue = null;

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

    public String getMessageKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && messageKey != null) return messageKey;
        return messageKey = StringUtilsHelper.toResourceMessageKey(getUseCase().getName() + ' ' + getName());
    }

    public String getTitleKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && titleKey != null) return titleKey;
        return titleKey = getMessageKey() + ".title";
    }

    public String getTitleValue()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && titleValue != null) return titleValue;
        return titleValue = StringUtilsHelper.toPhrase(getName());
    }

    public String getDocumentationKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && documentationKey != null) return documentationKey;
        return documentationKey = getMessageKey() + ".documentation";
    }

    public String getDocumentationValue()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && documentationValue != null) return documentationValue;
        return documentationValue = StringUtilsHelper.toResourceMessage(getDocumentation(""));
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

        final Map variablesMap = new HashMap();

        final Collection incoming = getIncoming();
        for (Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            EventFacade trigger = transition.getTrigger();
            if (trigger != null)
                collectByName(trigger.getParameters(), variablesMap);
        }

/*
        final Collection actions = getActions();
        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) iterator.next();
            collectByName(action.getActionParameters(), variablesMap);
        }
*/

        return pageVariables = variablesMap.values();
    }

    private void collectByName(Collection modelElements, Map elementMap)
    {
        for (Iterator iterator = modelElements.iterator(); iterator.hasNext();)
        {
            ModelElementFacade modelElement = (ModelElementFacade) iterator.next();
            elementMap.put(modelElement.getName(), modelElement);
        }
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
