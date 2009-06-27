package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.AttributeFacade;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Iterator;


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

    public StrutsManageableEntityLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsMultipartFormData()
    {
        boolean multipartFormPost = false;

        final Collection formFields = this.getManageableAttributes();
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

    protected java.lang.String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName());
    }

    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    protected java.lang.String handleGetPageTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".page.title";
    }

    protected java.lang.String handleGetPageTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetListName()
    {
        return "manageableList";
    }

    protected java.lang.String handleGetListGetterName()
    {
        return "getManageableList";
    }

    protected java.lang.String handleGetListSetterName()
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

    protected java.lang.String handleGetActionPath()
    {
        return '/' + this.getName() + "/Manage";
    }

    protected java.lang.String handleGetActionParameter()
    {
        return "crud";
    }

    protected java.lang.String handleGetFormBeanName()
    {
        return "manage" + this.getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    protected java.lang.String handleGetActionType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getActionClassName();
    }

    protected java.lang.String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception";
    }

    protected java.lang.String handleGetExceptionPath()
    {
        return this.getPageFullPath();
    }

    protected java.lang.String handleGetActionFullPath()
    {
        return '/' + StringUtils.replace(this.getActionType(), this.getNamespaceProperty(), "/");
    }

    protected java.lang.String handleGetActionClassName()
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