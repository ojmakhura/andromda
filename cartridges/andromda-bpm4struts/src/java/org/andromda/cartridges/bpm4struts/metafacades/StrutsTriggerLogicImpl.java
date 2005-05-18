package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;

import java.lang.reflect.Method;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger
 */
public class StrutsTriggerLogicImpl
        extends StrutsTriggerLogic
{
    public StrutsTriggerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

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
        String triggerKey = StringUtilsHelper.toResourceMessageKey(getName());

        if (!normalizeMessages())
        {
            final StrutsAction action = getAction();
            if (action != null)
            {
                final StrutsJsp page = action.getInput();
                if (page != null)
                {
                    triggerKey = page.getMessageKey() + '.' + triggerKey;
                }
            }
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

    private boolean normalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
}
