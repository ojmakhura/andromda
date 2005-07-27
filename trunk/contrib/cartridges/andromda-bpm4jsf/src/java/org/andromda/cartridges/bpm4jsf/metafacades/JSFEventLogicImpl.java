package org.andromda.cartridges.bpm4jsf.metafacades;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFEvent.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFEvent
 */
public class JSFEventLogicImpl
    extends JSFEventLogic
{

    public JSFEventLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFEvent#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        String triggerKey = StringUtilsHelper.toResourceMessageKey(getName());
        if (!this.isNormalizeMessages())
        {
            final JSFAction action = (JSFAction)this.getAction();
            if (action != null)
            {
                final JSFView view = (JSFView)action.getInput();
                if (view != null)
                {
                    triggerKey = view.getMessageKey() + '.' + triggerKey;
                }
            }
        }
        return triggerKey;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFEvent#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFEvent#getResetMessageValue()
     */
    protected java.lang.String handleGetResetMessageValue()
    {
        return "Reset";
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFEvent#getResetMessageKey()
     */
    protected java.lang.String handleGetResetMessageKey()
    {
        return this.getMessageKey() + ".reset.message";
    }
    
    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(BPM4JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

}