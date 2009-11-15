package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.utils.StringUtilsHelper;



/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd
 */
public class StrutsManageableEntityAssociationEndLogicImpl
    extends StrutsManageableEntityAssociationEndLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsManageableEntityAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        final StringBuilder messageKeyBuffer = new StringBuilder();

        final ClassifierFacade ownerType = this.getOtherEnd().getType();
        messageKeyBuffer.append(ownerType.getName());
        messageKeyBuffer.append('.');
        messageKeyBuffer.append(this.getName());

        return StringUtilsHelper.toResourceMessageKey(messageKeyBuffer.toString());
    }

    /**
     * @return messageValue
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        String messageValue = null;

        final ClassifierFacade type = this.getType();
        if (type instanceof Entity)
        {
            messageValue = this.getName();
        }

        return StringUtilsHelper.toPhrase(messageValue);
    }

    protected boolean handleIsSafeNamePresent()
    {
        return Bpm4StrutsUtils.isSafeName(this.getName());
    }

    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No field documentation has been specified" : value;
    }
}
