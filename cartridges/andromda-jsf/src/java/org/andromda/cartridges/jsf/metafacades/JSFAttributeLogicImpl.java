package org.andromda.cartridges.jsf.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFAttribute.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute
 */
public class JSFAttributeLogicImpl
    extends JSFAttributeLogic
{

    public JSFAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();
        final ClassifierFacade owner = this.getOwner();
        if (owner != null)
        {
            messageKey.append(StringUtilsHelper.toResourceMessageKey(owner.getName()));
            messageKey.append('.');
        }
        final String name = this.getName();
        if (name != null && name.trim().length() > 0)
        {
            messageKey.append(StringUtilsHelper.toResourceMessageKey(name));
        }
        return messageKey.toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName());
    }
}