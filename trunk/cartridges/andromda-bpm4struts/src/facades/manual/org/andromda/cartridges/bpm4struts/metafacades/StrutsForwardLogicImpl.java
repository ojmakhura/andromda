package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;

import java.util.Collections;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
 */
public class StrutsForwardLogicImpl
        extends StrutsForwardLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
{
    // ---------------- constructor -------------------------------
    
    public StrutsForwardLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsForward ...

    public String getGuardName()
    {
        final GuardFacade guard = getGuard();
        return (guard == null) ? null : guard.getName();
    }

    public boolean isTargettingActionState()
    {
        return getTarget() instanceof StrutsActionState;
    }

    public boolean isTargettingFinalState()
    {
        return getTarget() instanceof StrutsFinalState;
    }

    public boolean isTargettingDecisionPoint()
    {
        final StateVertexFacade target = getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint();
    }

    public boolean isTargettingPage()
    {
        return getTarget() instanceof StrutsJsp;
    }

    public java.lang.String getForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(resolveName());
    }

    public java.lang.String getForwardPath()
    {
        final StateVertexFacade target = getTarget();
        if (target instanceof StrutsJsp)
        {
            return ((StrutsJsp)target).getFullPath() + ".jsp";
        }
        else if (target instanceof StrutsFinalState)
        {
            return ((StrutsFinalState)target).getFullPath() + ".do";
        }
        else
            return null;
    }

    public String getActionMethodName()
    {
        return StringUtilsHelper.toJavaMethodName(resolveName());
    }

    private String resolveName()
    {
        String forwardName = null;
        //trigger
        final EventFacade trigger = getTrigger();
        if (trigger != null) forwardName = trigger.getName();
        //name
        if (forwardName == null) forwardName = getName();
        //target
        if (forwardName == null) forwardName = getTarget().getName();
        // else
        if (forwardName == null) forwardName = "unknown";
        // return
        return forwardName;
    }

    // ------------- relations ------------------

    protected java.util.Collection handleGetForwardParameters()
    {
        final EventFacade trigger = getTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    protected Object handleGetDecisionTrigger()
    {
        return getTrigger();
    }
}
