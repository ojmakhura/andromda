package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;

import java.util.Collection;
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
    private String actionMethodName = null;
    private String forwardName = null;
    private String forwardPath = null;
    private String guardName = null;

    private Object decisionTrigger = null;
    private Collection forwardParameters = null;

    private Boolean isTargettingActionState = null;
    private Boolean isTargettingDecisionPoint = null;
    private Boolean isTargettingFinalState = null;
    private Boolean isTargettingPage = null;

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
        if (Bpm4StrutsProfile.ENABLE_CACHE && guardName != null) return guardName;

        final GuardFacade guard = getGuard();
        return guardName = (guard == null) ? null : guard.getName();
    }

    public boolean isTargettingActionState()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isTargettingActionState != null) return isTargettingActionState.booleanValue();
        return (isTargettingActionState = (getTarget() instanceof StrutsActionState) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public boolean isTargettingFinalState()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isTargettingFinalState != null) return isTargettingFinalState.booleanValue();
        return (isTargettingFinalState = (getTarget() instanceof StrutsFinalState) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public boolean isTargettingDecisionPoint()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isTargettingDecisionPoint != null) return isTargettingDecisionPoint.booleanValue();
        final StateVertexFacade target = getTarget();
        return (isTargettingDecisionPoint = (target instanceof PseudostateFacade && ((PseudostateFacade) target).isDecisionPoint()) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public boolean isTargettingPage()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isTargettingPage != null) return isTargettingPage.booleanValue();
        return (isTargettingPage = (getTarget() instanceof StrutsJsp) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public java.lang.String getForwardName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && forwardName != null) return forwardName;
        return forwardName = StringUtilsHelper.toResourceMessageKey(resolveName());
    }

    public java.lang.String getForwardPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && forwardPath != null) return forwardPath;

        final StateVertexFacade target = getTarget();
        if (target instanceof StrutsJsp)
        {
            return forwardPath = ((StrutsJsp) target).getFullPath() + ".jsp";
        } else if (target instanceof StrutsFinalState)
        {
            return forwardPath = ((StrutsFinalState) target).getFullPath() + ".do";
        } else
            return null;
    }

    public String getActionMethodName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionMethodName != null) return actionMethodName;
        return actionMethodName = StringUtilsHelper.toJavaMethodName(resolveName());
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
        if (Bpm4StrutsProfile.ENABLE_CACHE && forwardParameters != null) return forwardParameters;

        final EventFacade trigger = getTrigger();
        return forwardParameters = (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    protected Object handleGetDecisionTrigger()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && decisionTrigger != null) return decisionTrigger;
        return decisionTrigger = getTrigger();
    }
}
