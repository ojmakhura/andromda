package org.andromda.cartridges.thymeleaf.metafacades;

import org.andromda.cartridges.web.CartridgeWebGlobals;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEvent.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEvent
 */
public class ThymeleafEventLogicImpl
    extends ThymeleafEventLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafEventLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return triggerKey
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEvent#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        String triggerKey = StringUtilsHelper.toResourceMessageKey(getName());
        if (!this.isNormalizeMessages())
        {
            final ThymeleafAction action = (ThymeleafAction)this.getAction();
            if (action != null)
            {
                final ThymeleafView view = (ThymeleafView)action.getInput();
                if (view != null)
                {
                    triggerKey = view.getMessageKey() + '.' + triggerKey;
                }
            }
        }
        return triggerKey;
    }

    /**
     * @return StringUtilsHelper.toPhrase(this.getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEvent#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    /**
     * @return "Reset"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEvent#getResetMessageValue()
     */
    protected String handleGetResetMessageValue()
    {
        return "Reset";
    }

    /**
     * @return getMessageKey() + ".reset.message"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafEvent#getResetMessageKey()
     */
    protected String handleGetResetMessageKey()
    {
        return this.getMessageKey() + ".reset.message";
    }

    /**
     * Indicates whether or not we should normalize messages.
     * @return normalizeMessages true/false
     */
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(CartridgeWebGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
}
