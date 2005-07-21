package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateFacade;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinition.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinition
 */
public class JBpmProcessDefinitionLogicImpl
    extends JBpmProcessDefinitionLogic
{

    public JBpmProcessDefinitionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

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

    protected boolean handleIsBusinessProcess()
    {
        return true;
    }

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

    protected String handleGetNodeInterfaceName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + "Node";
    }
}