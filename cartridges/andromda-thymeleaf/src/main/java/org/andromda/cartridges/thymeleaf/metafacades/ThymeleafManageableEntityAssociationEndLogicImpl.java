package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.Objects;

import org.andromda.cartridges.thymeleaf.ThymeleafGlobals;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd
 */
public class ThymeleafManageableEntityAssociationEndLogicImpl
    extends ThymeleafManageableEntityAssociationEndLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafManageableEntityAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getMessageKey()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getMessageValue()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @return onlineHelpValue
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No field documentation has been specified" : value;
    }

    /**
     * @return backingListName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getBackingListName()
     */
    protected String handleGetBackingListName()
    {
        final String backingListName =
            StringUtils.replace(
                Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.BACKING_LIST_PATTERN), ""),
                "{0}",
                this.getName());
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @return valueListName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.VALUE_LIST_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return labelListName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAssociationEnd#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.LABEL_LIST_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }
    
    //TODO should go to ancestor
    @Override
    public boolean isDisplay()
    {
        return super.isDisplay() && (getType() instanceof ManageableEntity);
    }
}
