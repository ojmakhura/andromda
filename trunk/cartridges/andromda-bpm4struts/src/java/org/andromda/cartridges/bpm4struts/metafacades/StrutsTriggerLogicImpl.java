package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.TransitionFacade;

import java.lang.reflect.Method;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger
 */
public class StrutsTriggerLogicImpl
    extends StrutsTriggerLogic
{
    // ---------------- constructor -------------------------------

    public StrutsTriggerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTrigger ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getNotAllowedTitleKey()()
     */
    protected java.lang.String handleGetNotAllowedTitleKey()
    {
        return getTitleKey() + ".notallowed";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTitleKey()()
     */
    protected java.lang.String handleGetTitleKey()
    {
        return getTriggerKey() + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetTitleKey()()
     */
    protected java.lang.String handleGetResetTitleKey()
    {
        return getTitleKey() + ".reset";
    }

    protected java.lang.String handleGetResetMessageKey()
    {
        return getResetTitleKey() + ".message";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetNotAllowedTitleKey()()
     */
    protected java.lang.String handleGetResetNotAllowedTitleKey()
    {
        return getResetTitleKey() + ".not.allowed";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTriggerKey()()
     */
    protected java.lang.String handleGetTriggerKey()
    {
        String triggerKey = null;

        StrutsAction action = getAction();
        if (action != null)
        {
            triggerKey = action.getMessageKey() + '.' + StringUtilsHelper.toResourceMessageKey(getName());
        }

        return triggerKey;
    }

    protected java.lang.String handleGetTriggerValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetTitleValue()
    {
        return getTriggerValue();
    }

    protected java.lang.String handleGetResetTitleValue()
    {
        return "Reset";
    }

    protected java.lang.String handleGetResetMessageValue()
    {
        return "Reset";
    }

    protected java.lang.String handleGetResetNotAllowedTitleValue()
    {
        return "You are not allowed to reset";
    }

    protected java.lang.String handleGetNotAllowedTitleValue()
    {
        return "You are not allowed to call this action";
    }

    protected boolean handleIsActionTrigger()
    {
        return getAction() != null;
    }

    // ------------- relations ------------------

    protected Object handleGetControllerCall()
    {
        /*
         * hack until I find a solution to workaround the JMI multiple inheritance (through interfaces)
         * @todo: find the solution
         */
        try
        {
            Method method = metaObject.getClass().getMethod("getOperation", (Class[])null);
            return method.invoke(metaObject, (Object[])null);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    protected Object handleGetAction()
    {
        StrutsAction triggerAction = null;

        TransitionFacade transition = getTransition();
        if (transition instanceof StrutsAction)
        {
            triggerAction = (StrutsAction)transition;
        }
        return triggerAction;
    }
}
