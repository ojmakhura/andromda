package org.andromda.cartridges.jbpm.metafacades;

import java.util.ArrayList;
import java.util.List;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.PartitionFacade;
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
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JBpmProcessDefinitionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetStates()
     */
    protected List<JBpmState> handleGetStates()
    {
        final List<JBpmState> states = new ArrayList<JBpmState>();
        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            for (final StateFacade state : graph.getStates())
            {
                if (state instanceof JBpmState)
                {
                    states.add((JBpmState)state);
                }
            }
        }
        return states;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetNodes()
     */
    protected List<JBpmNode> handleGetNodes()
    {
        final List<JBpmNode> states = new ArrayList<JBpmNode>();
        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            for (final ActionStateFacade state : graph.getActionStates())
            {
                if (state instanceof JBpmNode && !((JBpmNode)state).isTaskNode())
                {
                    states.add((JBpmNode)state);
                }
            }
        }
        return states;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetTaskNodes()
     */
    protected List<JBpmNode> handleGetTaskNodes()
    {
        final List<JBpmNode> taskNodes = new ArrayList<JBpmNode>();
        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            for (final ActionStateFacade state : graph.getActionStates())
            {
                if (state instanceof JBpmNode && ((JBpmNode)state).isTaskNode())
                {
                    taskNodes.add((JBpmNode)state);
                }
            }
        }
        return taskNodes;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmProcessDefinitionLogic#handleGetTasks()
     */
    protected List<JBpmAction> handleGetTasks()
    {
        final List<JBpmAction> tasks = new ArrayList<JBpmAction>();
        for (JBpmNode node : getTaskNodes())
        {
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
    protected List<PartitionFacade> handleGetSwimlanes()
    {
        final List<PartitionFacade> swimlanes = new ArrayList<PartitionFacade>();
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
    protected PseudostateFacade  handleGetStartState()
    {
        PseudostateFacade  startState = null;
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
    protected List<FinalStateFacade> handleGetEndStates()
    {
        final List<FinalStateFacade> endStates = new ArrayList<FinalStateFacade>();
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
    protected List<PseudostateFacade> handleGetDecisions()
    {
        final List<PseudostateFacade> decisions = new ArrayList<PseudostateFacade>();
        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            for (final PseudostateFacade pseudostate : graph.getPseudostates())
            {
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
    protected List<PseudostateFacade> handleGetForks()
    {
        final List<PseudostateFacade> forks = new ArrayList<PseudostateFacade>();
        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            for (final PseudostateFacade pseudostate : graph.getPseudostates())
            {
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
    protected List<PseudostateFacade> handleGetJoins()
    {
        final List<PseudostateFacade> joins = new ArrayList<PseudostateFacade>();
        final ActivityGraphFacade graph = this.getFirstActivityGraph();
        if (graph != null)
        {
            for (final PseudostateFacade pseudostate : graph.getPseudostates())
            {
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
        final StringBuilder pathBuffer = new StringBuilder();

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
