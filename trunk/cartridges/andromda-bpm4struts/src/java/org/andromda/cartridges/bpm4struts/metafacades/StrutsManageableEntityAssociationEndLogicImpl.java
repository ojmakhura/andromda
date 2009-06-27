package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd
 */
public class StrutsManageableEntityAssociationEndLogicImpl
    extends StrutsManageableEntityAssociationEndLogic
{
    public StrutsManageableEntityAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKeyBuffer = new StringBuffer();

        final ClassifierFacade ownerType = this.getOtherEnd().getType();
        if (ownerType instanceof ManageableEntity)
        {
            messageKeyBuffer.append(ownerType.getName());
        }
        else
        {
            messageKeyBuffer.append(ownerType.getName());
        }

        messageKeyBuffer.append('.');
        messageKeyBuffer.append(this.getName());

        return StringUtilsHelper.toResourceMessageKey(messageKeyBuffer.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
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