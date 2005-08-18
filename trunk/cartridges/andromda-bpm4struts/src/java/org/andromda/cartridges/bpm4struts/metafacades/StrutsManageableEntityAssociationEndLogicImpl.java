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

        final ClassifierFacade ownerType = getOtherEnd().getType();
        if (ownerType instanceof ManageableEntity)
        {
            messageKeyBuffer.append(((ManageableEntity)ownerType).getName());
        }
        else
        {
            messageKeyBuffer.append(ownerType.getName());
        }

        messageKeyBuffer.append('.');
        messageKeyBuffer.append(getName());

        return StringUtilsHelper.toResourceMessageKey(messageKeyBuffer.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        String messageValue = null;

        final ClassifierFacade type = getType();
        if (type instanceof Entity)
        {
            messageValue = getName();
        }

        return StringUtilsHelper.toPhrase(messageValue);
    }

    protected boolean handleIsSafeNamePresent()
    {
        return Bpm4StrutsUtils.isSafeName(this.getName());
    }
}