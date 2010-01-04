package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.utils.StringUtilsHelper;



/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger
 */
public class StrutsTriggerLogicImpl
    extends StrutsTriggerLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsTriggerLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getTitleKey() + ".notallowed"
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getNotAllowedTitleKey()
     */
    protected String handleGetNotAllowedTitleKey()
    {
        return getTitleKey() + ".notallowed";
    }

    /**
     * @return getTriggerKey() + ".title"
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return getTriggerKey() + ".title";
    }

    /**
     * @return getTitleKey() + ".reset"
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetTitleKey()
     */
    protected String handleGetResetTitleKey()
    {
        return getTitleKey() + ".reset";
    }

    protected String handleGetResetMessageKey()
    {
        return getResetTitleKey() + ".message";
    }

    /**
     * @return getResetTitleKey() + ".not.allowed"
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetNotAllowedTitleKey()
     */
    protected String handleGetResetNotAllowedTitleKey()
    {
        return getResetTitleKey() + ".not.allowed";
    }

    /**
     * @return triggerKey
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTriggerKey()
     */
    protected String handleGetTriggerKey()
    {
        String triggerKey = StringUtilsHelper.toResourceMessageKey(getName());

        if (!normalizeMessages())
        {
            final StrutsAction action = getStrutsAction();
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

    protected String handleGetTriggerValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetTitleValue()
    {
        return getTriggerValue();
    }

    protected String handleGetResetTitleValue()
    {
        return "Reset";
    }

    protected String handleGetResetMessageValue()
    {
        return "Reset";
    }

    protected String handleGetResetNotAllowedTitleValue()
    {
        return "You are not allowed to reset";
    }

    protected String handleGetNotAllowedTitleValue()
    {
        return "You are not allowed to call this action";
    }

    protected boolean handleIsActionTrigger()
    {
        return this.getStrutsAction() != null;
    }

    protected Object handleGetStrutsAction()
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
