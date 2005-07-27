package org.andromda.cartridges.bpm4jsf.metafacades;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter
 */
public class JSFParameterLogicImpl
    extends JSFParameterLogic
{

    public JSFParameterLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();

        if (!this.isNormalizeMessages())
        {
            if (isActionParameter())
            {
                final JSFAction action = (JSFAction)this.getAction();
                if (action != null)
                {
                    messageKey.append(action.getMessageKey());
                    messageKey.append('.');
                }
            }
            else
            {
                final JSFView view = (JSFView)this.getView();
                if (view != null)
                {
                    messageKey.append(view.getMessageKey());
                    messageKey.append('.');
                }
            }
            messageKey.append("param.");
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(super.getName()));
        return messageKey.toString();
    }
 
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + BPM4JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDocumentationValue(()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return value == null ? "" : value;
    }
    
    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(BPM4JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

}