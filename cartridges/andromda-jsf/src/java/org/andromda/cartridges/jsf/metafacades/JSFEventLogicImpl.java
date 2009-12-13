package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFEvent.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFEvent
 */
public class JSFEventLogicImpl
    extends JSFEventLogic
{

    /**
     * Public constructor for JSFEventLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.jsf.metafacades.JSFEvent
     */
    public JSFEventLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return MessageKey
     * @see org.andromda.cartridges.jsf.metafacades.JSFEvent#getMessageKey()
     */
    protected String handleGetMessageKey()
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
     * @return StringUtilsHelper.toPhrase(this.getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFEvent#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }
    
    /**
     * @return "Reset"
     * @see org.andromda.cartridges.jsf.metafacades.JSFEvent#getResetMessageValue()
     */
    protected String handleGetResetMessageValue()
    {
        return "Reset";
    }
    
    /**
     * @return getMessageKey() + ".reset.message"
     * @see org.andromda.cartridges.jsf.metafacades.JSFEvent#getResetMessageKey()
     */
    protected String handleGetResetMessageKey()
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
        final String normalizeMessages = (String)getConfiguredProperty(JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages);
    }

}