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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetResetMessageKey()
     */
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetTriggerValue()
     */
    protected String handleGetTriggerValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return getTriggerValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetResetTitleValue()
     */
    protected String handleGetResetTitleValue()
    {
        return "Reset";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetResetMessageValue()
     */
    protected String handleGetResetMessageValue()
    {
        return "Reset";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetResetNotAllowedTitleValue()
     */
    protected String handleGetResetNotAllowedTitleValue()
    {
        return "You are not allowed to reset";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetNotAllowedTitleValue()
     */
    protected String handleGetNotAllowedTitleValue()
    {
        return "You are not allowed to call this action";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleIsActionTrigger()
     */
    protected boolean handleIsActionTrigger()
    {
        return this.getStrutsAction() != null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTriggerLogic#handleGetStrutsAction()
     */
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
