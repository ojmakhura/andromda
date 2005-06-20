package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.cartridges.jbpm.JBpmProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmTransition.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmTransition
 */
public class JBpmTransitionLogicImpl
    extends JBpmTransitionLogic
{

    public JBpmTransitionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
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
}