package org.andromda.cartridges.jsf2.metafacades;

import java.util.Objects;

import org.andromda.cartridges.jsf2.JSFGlobals;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd
 */
public class JSFManageableEntityAssociationEndLogicImpl
    extends JSFManageableEntityAssociationEndLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFManageableEntityAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // /**
    //  * @return messageKey
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getMessageKey()
    //  */
    // protected String handleGetMessageKey()
    // {
    //     final StringBuilder messageKeyBuffer = new StringBuilder();

    //     final ClassifierFacade ownerType = this.getOtherEnd().getType();
    //     if (ownerType instanceof ManageableEntity)
    //     {
    //         messageKeyBuffer.append(ownerType.getName());
    //     }
    //     else
    //     {
    //         messageKeyBuffer.append(ownerType.getName());
    //     }

    //     messageKeyBuffer.append('.');
    //     messageKeyBuffer.append(this.getName());

    //     return StringUtilsHelper.toResourceMessageKey(messageKeyBuffer.toString());
    // }

    // /**
    //  * @return messageValue
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getMessageValue()
    //  */
    // protected String handleGetMessageValue()
    // {
    //     String messageValue = null;

    //     final ClassifierFacade type = this.getType();
    //     if (type instanceof Entity)
    //     {
    //         messageValue = this.getName();
    //     }

    //     return StringUtilsHelper.toPhrase(messageValue);
    // }

    // /**
    //  * @return getMessageKey() + ".online.help"
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpKey()
    //  */
    // protected String handleGetOnlineHelpKey()
    // {
    //     return this.getMessageKey() + ".online.help";
    // }

    // /**
    //  * @return onlineHelpValue
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpValue()
    //  */
    // protected String handleGetOnlineHelpValue()
    // {
    //     final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
    //     return (value == null) ? "No field documentation has been specified" : value;
    // }

    // /**
    //  * @return backingListName
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getBackingListName()
    //  */
    // protected String handleGetBackingListName()
    // {
    //     final String backingListName =
    //         StringUtils.replace(
    //             Objects.toString(this.getConfiguredProperty(JSFGlobals.BACKING_LIST_PATTERN), ""),
    //             "{0}",
    //             this.getName());
    //     return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    // }

    // /**
    //  * @return valueListName
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getValueListName()
    //  */
    // protected String handleGetValueListName()
    // {
    //     return Objects.toString(this.getConfiguredProperty(JSFGlobals.VALUE_LIST_PATTERN), "").replaceAll(
    //         "\\{0\\}",
    //         this.getName());
    // }

    // /**
    //  * @return labelListName
    //  * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getLabelListName()
    //  */
    // protected String handleGetLabelListName()
    // {
    //     return Objects.toString(this.getConfiguredProperty(JSFGlobals.LABEL_LIST_PATTERN), "").replaceAll(
    //         "\\{0\\}",
    //         this.getName());
    // }
    
    // //TODO should go to ancestor
    // @Override
    // public boolean isDisplay()
    // {
    //     return super.isDisplay() && (getType() instanceof ManageableEntity);
    // }
}
