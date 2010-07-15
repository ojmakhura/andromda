package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.utils.StringUtilsHelper;
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

    /**
     * @param metaObject
     * @param context
     */
    public JSFManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getMessageKey()
     */
    protected String handleGetMessageKey()
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
     * @return messageValue
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getMessageValue()
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

    /**
     * @return getMessageKey() + ".online.help"
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @return onlineHelpValue
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No field documentation has been specified" : value;
    }

    /**
     * @return backingListName
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
     * @return valueListName
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.VALUE_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return labelListName
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntityAssociationEnd#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.LABEL_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

}