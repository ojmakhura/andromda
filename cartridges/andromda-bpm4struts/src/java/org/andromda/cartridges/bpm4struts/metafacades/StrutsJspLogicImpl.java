package org.andromda.cartridges.bpm4struts.metafacades;

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
        ClassifierFacade classifier = (ClassifierFacade) getActivityGraph().getContextElement();
        return classifier.getPackageName();
    }

    public String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getUseCase().getName() + ' ' + getName());
    }

    public String handleGetTitleKey()
    {
        return getMessageKey() + ".title";
    }

    public String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String handleGetDocumentationKey()
    {
        return getMessageKey() + ".documentation";
    }

    public String handleGetDocumentationValue()
    {
        return StringUtilsHelper.toResourceMessage(getDocumentation(""));
    }

    public String handleGetFullPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/');
    }
    // ------------- relations ------------------

    protected Object handleGetUseCase()
    {
        final ActivityGraphFacade graph = getActivityGraph();
        if (graph instanceof StrutsActivityGraph)
        {
            return ((StrutsActivityGraph) graph).getUseCase();
        }
        return null;
    }

    protected Collection handleGetActions()
    {
        return getOutgoing();
    }

    public StrutsForward getForward()
    {
        return (StrutsForward)shieldedElement(getOutgoing().iterator().next());
    }

    protected Collection handleGetPageVariables()
    {
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

        return variablesMap.values();
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
        final Collection incomingActionsList = new LinkedList();
        collectIncomingActions(this, new HashSet(), incomingActionsList);
        return incomingActionsList;
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

/*  @todo: TEMPORARILY COMMENTED OUT -- needs verification that isCaseStart() forms are not populated, but I think they are
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
