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
        ActivityGraphFacade graph = getActivityGraph();
        return (graph instanceof StrutsActivityGraph)
                ? ((StrutsActivityGraph) graph).getUseCase().getPackageName()
                : graph.getPackageName();
    }

    public String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getUseCase().getName() + ' ' + getName());
    }

    public String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
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
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return (value == null) ? "" : value;
    }

    public String handleGetOnlineHelpKey()
    {
        return getMessageKey() + ".online.help";
    }

    public String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No page documentation has been specified" : value);
        buffer.append(crlf);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    public String handleGetFullPath()
    {
        return '/' + (getPackageName() + '.' + StringUtilsHelper.toWebFileName(getName())).replace('.', '/');
    }

    public boolean handleIsValidationRequired()
    {
        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) actionIterator.next();
            if (action.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    public boolean handleIsDateFieldPresent()
    {
        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) actionIterator.next();
            if (action.isDateFieldPresent())
            {
                return true;
            }
        }
        return false;
    }

    public boolean handleIsCalendarRequired()
    {
        final Collection actions = getActions();
        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) actionIterator.next();
            if (action.isCalendarRequired())
            {
                return true;
            }
        }
        return false;
    }

    // ------------- relations ------------------

    protected Collection handleGetAllActionParameters()
    {
        final Collection actionParameters = new ArrayList();
        final Collection actions = getActions();
        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) iterator.next();
            actionParameters.addAll(action.getActionParameters());
        }
        return actionParameters;
    }

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
        final Collection actions = new ArrayList();
        final Collection outgoing = getOutgoing();

        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsAction)
                actions.add(object);
        }

        return actions;
    }

    protected Collection handleGetNonActionForwards()
    {
        final Collection actions = new ArrayList();
        final Collection outgoing = getOutgoing();

        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (!(object instanceof StrutsAction))
                actions.add(object);
        }

        return actions;
    }

    public StrutsForward getForward()
    {
        return (StrutsForward) shieldedElement(getOutgoing().iterator().next());
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
        final Collection incomingActionsList = new ArrayList();
        collectIncomingActions(this, new HashSet(), incomingActionsList);
        return incomingActionsList;
    }

    public int handleGetTabCount()
    {
        if (this.isTabbed())
        {
            final Collection actions = this.getActions();
            int maxValue = 0;

            for (Iterator iterator = actions.iterator(); iterator.hasNext();)
            {
                StrutsAction action = (StrutsAction) iterator.next();
                maxValue = Math.max(maxValue, action.getTabIndex());
            }
            return maxValue + 1;    // we add one because we're counting from [1..n]
        }
        return 0;
    }

    public Collection handleGetTabActions(int index)
    {
        if (index < 0)
            throw new IndexOutOfBoundsException("Minimum tab-index value is zero");

        if (index >= this.getTabCount())
            throw new IndexOutOfBoundsException("Maximum tab-index value is the number of available tabs minus one");

        final Collection actions = this.getActions();
        final Collection tabActions = new ArrayList();

        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            StrutsAction action = (StrutsAction) iterator.next();
            if (action.getTabIndex() == index)
            {
                tabActions.add(action);
            }
        }

        return tabActions;
    }

    public boolean handleIsTabbed()
    {
        final Collection actions = this.getActions();

        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            Object actionObject = iterator.next();
            if (actionObject instanceof StrutsAction)
            {
                StrutsAction action = (StrutsAction) actionObject;
                if (action.getTabIndex() >= 0) return true;
            }
        }
        return false;
    }

    public Collection handleGetNonTabActions()
    {
        final Collection nonTabbedActions = new ArrayList();
        final Collection actions = this.getActions();

        for (Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            Object actionObject = iterator.next();
            if (actionObject instanceof StrutsAction)
            {
                StrutsAction action = (StrutsAction) actionObject;
                if (!action.isTabbed())
                    nonTabbedActions.add(action);
            }
        }
        return nonTabbedActions;
    }

    public Map handleGetTabMap()
    {
        final Map tabMap = new LinkedHashMap();

        final int tabCount = this.getTabCount();
        for (int i = 0; i < tabCount; i++)
        {
            final Collection tabActions = this.getTabActions(i);
            tabMap.put(String.valueOf(i), tabActions);
        }

        return tabMap;
    }

    public String handleGetTabName(int tabIndex)
    {
        final Collection tabActions = this.getTabActions(tabIndex);
        final StringBuffer buffer = new StringBuffer();

        boolean needsSeparator = false;
        for (Iterator iterator = tabActions.iterator(); iterator.hasNext();)
        {
            if (needsSeparator) buffer.append(" / ");
            StrutsAction action = (StrutsAction) iterator.next();
            buffer.append(action.getActionTrigger().getTriggerValue());
            needsSeparator = true;
        }

        if (buffer.length() == 0)
        {
            buffer.append(String.valueOf(tabIndex + 1));
        }
        return buffer.toString();
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

    public boolean handleIsDuplicateActionNamePresent()
    {
        boolean duplicatePresent = false;

        Collection actionNames = new HashSet();
        Collection actions = getActions();

        for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext() && !duplicatePresent;)
        {
            StrutsAction action = (StrutsAction) actionIterator.next();
            StrutsTrigger trigger = action.getActionTrigger();
            if (trigger != null)
            {
                // this name should never be null because of an OCL constraint
                String actionName = trigger.getName();
                if (actionNames.contains(actionName))
                {
                    duplicatePresent = true;
                }
                else
                {
                    actionNames.add(actionName);
                }
            }
        }
        return duplicatePresent;
    }
}
