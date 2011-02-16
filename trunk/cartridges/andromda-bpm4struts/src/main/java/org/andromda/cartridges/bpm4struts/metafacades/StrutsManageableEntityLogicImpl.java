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
    private static final long serialVersionUID = 34L;
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleIsMultipartFormData()
     */
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetFormBeanType()
     */
    protected String handleGetFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getFormBeanClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetFormBeanClassName()
     */
    protected String handleGetFormBeanClassName()
    {
        return this.getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetFormBeanFullPath()
     */
    protected String handleGetFormBeanFullPath()
    {
        return StringUtils.replace(this.getFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetPageTitleKey()
     */
    protected String handleGetPageTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".page.title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetPageTitleValue()
     */
    protected String handleGetPageTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetListName()
     */
    protected String handleGetListName()
    {
        return "manageableList";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetListGetterName()
     */
    protected String handleGetListGetterName()
    {
        return "getManageableList";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetListSetterName()
     */
    protected String handleGetListSetterName()
    {
        return "setManageableList";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetPageName()
     */
    protected String handleGetPageName()
    {
        return this.getName().toLowerCase() + "-crud.jsp";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetPageFullPath()
     */
    protected String handleGetPageFullPath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getPageName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetActionPath()
     */
    protected String handleGetActionPath()
    {
        return '/' + this.getName() + "/Manage";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetActionParameter()
     */
    protected String handleGetActionParameter()
    {
        return "crud";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetFormBeanName()
     */
    protected String handleGetFormBeanName()
    {
        return "manage" + this.getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetActionType()
     */
    protected String handleGetActionType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getActionClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetExceptionKey()
     */
    protected String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetExceptionPath()
     */
    protected String handleGetExceptionPath()
    {
        return this.getPageFullPath();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetActionFullPath()
     */
    protected String handleGetActionFullPath()
    {
        return '/' + StringUtils.replace(this.getActionType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return "Manage" + getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleIsPreload()
     */
    protected boolean handleIsPreload()
    {
        return this.isCreate() || this.isRead() || this.isUpdate() || this.isDelete();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        return (!this.isDocumentationPresent()) ? "No entity documentation has been specified" : 
            StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetOnlineHelpActionPath()
     */
    protected String handleGetOnlineHelpActionPath()
    {
        return this.getActionPath() + "Help";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetOnlineHelpPagePath()
     */
    protected String handleGetOnlineHelpPagePath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase() + "_help";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleIsTableExportable()
     */
    protected boolean handleIsTableExportable()
    {
        return this.getTableExportTypes().indexOf("none") == -1;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetTableExportTypes()
     */
    protected String handleGetTableExportTypes()
    {
        return Bpm4StrutsUtils.getDisplayTagExportTypes(
            this.findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_EXPORT),
            (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_TABLE_EXPORT_TYPES) );
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleIsTableSortable()
     */
    protected boolean handleIsTableSortable()
    {
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
            ? Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
            : Bpm4StrutsUtils.isTrue(String.valueOf(taggedValue));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityLogic#handleGetTableMaxRows()
     */
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