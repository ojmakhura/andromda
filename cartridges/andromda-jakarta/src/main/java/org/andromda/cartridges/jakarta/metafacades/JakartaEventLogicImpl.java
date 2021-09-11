package org.andromda.cartridges.jakarta.metafacades;

import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaEvent.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaEvent
 */
public class JakartaEventLogicImpl
    extends JakartaEventLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaEventLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return triggerKey
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEvent#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        String triggerKey = StringUtilsHelper.toResourceMessageKey(getName());
        if (!this.isNormalizeMessages())
        {
            final JakartaAction action = (JakartaAction)this.getAction();
            if (action != null)
            {
                final JakartaView view = (JakartaView)action.getInput();
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
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEvent#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    /**
     * @return "Reset"
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEvent#getResetMessageValue()
     */
    protected String handleGetResetMessageValue()
    {
        return "Reset";
    }

    /**
     * @return getMessageKey() + ".reset.message"
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaEvent#getResetMessageKey()
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
        final String normalizeMessages = (String)getConfiguredProperty(JakartaGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
}
