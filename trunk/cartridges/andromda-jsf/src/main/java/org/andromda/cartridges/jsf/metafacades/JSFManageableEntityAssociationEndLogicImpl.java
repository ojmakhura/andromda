package org.andromda.cartridges.jsf.metafacades;

import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd
 */
public class JSFManageableEntityAssociationEndLogicImpl
    extends JSFManageableEntityAssociationEndLogic
{

    public JSFManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuilder messageKeyBuffer = new StringBuilder();

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
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getMessageValue()
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

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpKey()
     */
    protected java.lang.String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpValue()
     */
    protected java.lang.String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No field documentation has been specified" : value;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getBackingListName()
     */
    protected String handleGetBackingListName()
    {
        final String backingListName =
            StringUtils.replace(
                ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.BACKING_LIST_PATTERN)),
                "{0}",
                this.getName());
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getValueListName()
     */
    protected java.lang.String handleGetValueListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.VALUE_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getLabelListName()
     */
    protected java.lang.String handleGetLabelListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.LABEL_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

}