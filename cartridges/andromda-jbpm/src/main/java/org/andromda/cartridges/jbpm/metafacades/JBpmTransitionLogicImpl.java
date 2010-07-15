package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.GuardFacade;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmTransition.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmTransition
 */
public class JBpmTransitionLogicImpl
        extends JBpmTransitionLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public JBpmTransitionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmTransitionLogic#handleIsContainedInBusinessProcess()
     */
    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getSource().getStateMachine() instanceof ActivityGraphFacade &&
                ((ActivityGraphFacade)this.getSource().getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmTransitionLogic#handleGetCondition()
     */
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

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmTransitionLogic#handleIsTaskNode()
     */
    protected boolean handleIsTaskNode()
    {
        return hasStereotype(JBpmProfile.STEREOTYPE_TASK);
    }
}