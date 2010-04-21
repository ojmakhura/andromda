package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntity.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntity
 */
public class StrutsManageableEntityLogicImpl
    extends StrutsManageableEntityLogic
{
    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String getNamespaceProperty()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

    /**
     * @param metaObject
     * @param context
     */
    public StrutsManageableEntityLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsMultipartFormData()
    {
        boolean multipartFormPost = false;

        final Collection<ManageableEntityAttribute> formFields = this.getManageableAttributes();
        for (final Iterator fieldIterator = formFields.iterator(); !multipartFormPost && fieldIterator.hasNext();)
        {
            final AttributeFacade field = (AttributeFacade)fieldIterator.next();
            if (field.getType().isFileType())
            {
                multipartFormPost = true;
            }
        }

        return multipartFormPost;
    }

    protected String handleGetFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getFormBeanClassName();
    }

    protected String handleGetFormBeanClassName()
    {
        return this.getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    protected String handleGetFormBeanFullPath()
    {
        return StringUtils.replace(this.getFormBeanType(), this.getNamespaceProperty(), "/");
    }

    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName());
    }

    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    protected String handleGetPageTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".page.title";
    }

    protected String handleGetPageTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetListName()
    {
        return "manageableList";
    }

    protected String handleGetListGetterName()
    {
        return "getManageableList";
    }

    protected String handleGetListSetterName()
    {
        return "setManageableList";
    }

    protected String handleGetPageName()
    {
        return this.getName().toLowerCase() + "-crud.jsp";
    }

    protected String handleGetPageFullPath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getPageName();
    }

    protected String handleGetActionPath()
    {
        return '/' + this.getName() + "/Manage";
    }

    protected String handleGetActionParameter()
    {
        return "crud";
    }

    protected String handleGetFormBeanName()
    {
        return "manage" + this.getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    protected String handleGetActionType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getActionClassName();
    }

    protected String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception";
    }

    protected String handleGetExceptionPath()
    {
        return this.getPageFullPath();
    }

    protected String handleGetActionFullPath()
    {
        return '/' + StringUtils.replace(this.getActionType(), this.getNamespaceProperty(), "/");
    }

    protected String handleGetActionClassName()
    {
        return "Manage" + getName();
    }

    protected boolean handleIsPreload()
    {
        return this.isCreate() || this.isRead() || this.isUpdate() || this.isDelete();
    }

    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No entity documentation has been specified" : value;
    }

    protected String handleGetOnlineHelpActionPath()
    {
        return this.getActionPath() + "Help";
    }

    protected String handleGetOnlineHelpPagePath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase() + "_help";
    }

    protected boolean handleIsTableExportable()
    {
        return this.getTableExportTypes().indexOf("none") == -1;
    }

    protected String handleGetTableExportTypes()
    {
        return Bpm4StrutsUtils.getDisplayTagExportTypes(
            this.findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_EXPORT),
            (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_TABLE_EXPORT_TYPES) );
    }

    protected boolean handleIsTableSortable()
    {
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
            ? Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
            : Bpm4StrutsUtils.isTrue(String.valueOf(taggedValue));
    }

    protected int handleGetTableMaxRows()
    {
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_MAXROWS);
        int pageSize;

        try
        {
            pageSize = Integer.parseInt(String.valueOf(taggedValue));
        }
        catch (Exception e)
        {
            pageSize = Bpm4StrutsProfile.TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT;
        }

        return pageSize;
    }
}
