package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmTransition.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmTransition
 */
public class JBpmTransitionLogicImpl
        extends JBpmTransitionLogic
{
    public JBpmTransitionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getSource().getStateMachine() instanceof ActivityGraphFacade &&
                ((ActivityGraphFacade)this.getSource().getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    protected String handleGetCondition()
    {
        String decision = null;

        final GuardFacade guard = this.getGuard();
        if (guard != null)
        {
            decision = guard.getBody();
        }

        return decision;
    }

    protected boolean handleIsTaskNode()
    {
        return hasStereotype(JBpmProfile.STEREOTYPE_TASK);
    }

    protected java.util.Collection handleGetPossibleTargetNodes()
    {
        final Set targetNodes = new HashSet();
        this.collectTargetNodes(targetNodes, this);
        return targetNodes;
    }

    /**
     * Recursively collects all nodes that might be a direct or indirect runtime target for this transition.
     * This method steps over pseudostates and analyzes the outgoig transitions.
     */
    private void collectTargetNodes(Collection nodes, TransitionFacade transition)
    {
        final StateVertexFacade target = transition.getTarget();

        if (target instanceof PseudostateFacade)
        {
            final Collection outgoingTransitions = target.getOutgoing();
            for (final Iterator outgoingTransitionIterator = outgoingTransitions.iterator();
                    outgoingTransitionIterator.hasNext();)
            {
                final TransitionFacade outgoingTransition = (TransitionFacade)outgoingTransitionIterator.next();
                this.collectTargetNodes(nodes, outgoingTransition);
            }
        }
        else
        {
            nodes.add(target);
        }
    }
}