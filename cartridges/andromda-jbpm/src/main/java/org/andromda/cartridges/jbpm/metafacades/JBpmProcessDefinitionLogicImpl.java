package org.andromda.cartridges.jbpm.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinition.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinition
 */
public class JBpmProcessDefinitionLogicImpl
    extends JBpmProcessDefinitionLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public JBpmProcessDefinitionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetStates()
     */
    protected List handleGetStates()
    {
        final List states = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection graphStates = graph.getStates();
            for (final Iterator stateIterator = graphStates.iterator(); stateIterator.hasNext();)
            {
                final StateFacade state = (StateFacade)stateIterator.next();
                if (state instanceof JBpmState)
                {
                    states.add(state);
                }
            }
        }

        return states;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetNodes()
     */
    protected List handleGetNodes()
    {
        final List states = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (final Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final ActionStateFacade state = (ActionStateFacade)actionStateIterator.next();
                if (state instanceof JBpmNode && !((JBpmNode)state).isTaskNode())
                {
                    states.add(state);
                }
            }
        }

        return states;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetTaskNodes()
     */
    protected List handleGetTaskNodes()
    {
        final List taskNodes = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (final Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final ActionStateFacade state = (ActionStateFacade)actionStateIterator.next();
                if (state instanceof JBpmNode && ((JBpmNode)state).isTaskNode())
                {
                    taskNodes.add(state);
                }
            }
        }

        return taskNodes;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetTasks()
     */
    protected List handleGetTasks()
    {
        final List tasks = new ArrayList();

        final List taskNodes = getTaskNodes();
        for (int i = 0; i < taskNodes.size(); i++)
        {
            final JBpmNode node = (JBpmNode)taskNodes.get(i);
            tasks.addAll(node.getTasks());
        }

        return tasks;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleIsBusinessProcess()
     */
    protected boolean handleIsBusinessProcess()
    {
        return true;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetSwimlanes()
     */
    protected List handleGetSwimlanes()
    {
        final List swimlanes = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            swimlanes.addAll(graph.getPartitions());
        }

        return swimlanes;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetStartState()
     */
    protected Object handleGetStartState()
    {
        Object startState = null;

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            startState = graph.getInitialState();
        }

        return startState;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetEndStates()
     */
    protected List handleGetEndStates()
    {
        final List endStates = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            endStates.addAll(graph.getFinalStates());
        }

        return endStates;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetDecisions()
     */
    protected List handleGetDecisions()
    {
        final List decisions = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection pseudostates = graph.getPseudostates();
            for (final Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
            {
                final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
                if (pseudostate.isDecisionPoint())
                {
                    decisions.add(pseudostate);
                }
            }
        }

        return decisions;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetForks()
     */
    protected List handleGetForks()
    {
        final List forks = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection pseudostates = graph.getPseudostates();
            for (final Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
            {
                final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
                if (pseudostate.isSplit())
                {
                    forks.add(pseudostate);
                }
            }
        }

        return forks;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetJoins()
     */
    protected List handleGetJoins()
    {
        final List joins = new ArrayList();

        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            final Collection pseudostates = graph.getPseudostates();
            for (final Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
            {
                final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
                if (pseudostate.isCollect())
                {
                    joins.add(pseudostate);
                }
            }
        }

        return joins;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetDescriptorFullPath()
     */
    protected String handleGetDescriptorFullPath()
    {
        final StringBuffer pathBuffer = new StringBuffer();

        if (StringUtils.isNotBlank(this.getPackagePath()))
        {
            pathBuffer.append(this.getPackagePath());
            pathBuffer.append('/');
        }
        pathBuffer.append(StringUtilsHelper.separate(this.getName(), "-").toLowerCase());
        pathBuffer.append(".pdl.xml");

        return pathBuffer.toString();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetNodeInterfaceName()
     */
    protected String handleGetNodeInterfaceName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + "ProcessNode";
    }

    /**
     * Overwritten because we want to be able to make use of the AndroMDA tagged value for use-case
     * to activity graph linking.
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#getFirstActivityGraph()
     */
    public ActivityGraphFacade getFirstActivityGraph()
    {
        ActivityGraphFacade activityGraph;

        final Object activity = this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY);
        if (activity == null)
        {
            activityGraph = super.getFirstActivityGraph();
        }
        else
        {
            final String activityName = String.valueOf(activity.toString());
            activityGraph = this.getModel().findActivityGraphByName(activityName);
        }

        return activityGraph;
    }
}