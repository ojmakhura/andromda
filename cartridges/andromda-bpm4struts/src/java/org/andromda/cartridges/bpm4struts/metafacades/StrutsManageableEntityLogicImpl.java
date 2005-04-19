package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntity.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntity
 */
public class StrutsManageableEntityLogicImpl
    extends StrutsManageableEntityLogic
{
    // ---------------- constructor -------------------------------

    public StrutsManageableEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected String handleGetFormBeanType()
    {
        return getCrudPackageName() + '.' + getFormBeanClassName();
    }

    protected String handleGetFormBeanClassName()
    {
        return getName() + "Form";
    }

    protected String handleGetFormBeanFullPath()
    {
        return getFormBeanType().replace('.', '/');
    }

    protected java.lang.String handleGetActionInput()
    {
        return getPreloadActionPath() + ".do";
    }

    protected java.lang.String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetPageTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".page.title";
    }

    protected java.lang.String handleGetPageTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetListName()
    {
        return "crudList";
    }

    protected java.lang.String handleGetListGetterName()
    {
        return "getCrudList";
    }

    protected java.lang.String handleGetListSetterName()
    {
        return "setCrudList";
    }

    protected String handleGetPageName()
    {
        return getName().toLowerCase() + "-crud.jsp";
    }

    protected String handleGetPageFullPath()
    {
        return '/' + getCrudPackagePath() + '/' + getPageName();
    }

    protected java.lang.String handleGetCreateActionPath()
    {
        return '/' + getName() + "/Create";
    }

    protected java.lang.String handleGetCreateFormBeanName()
    {
        return "create" + getName() + "Form";
    }

    protected java.lang.String handleGetCreateActionType()
    {
        return getCrudPackageName() + '.' + getCreateActionClassName();
    }

    protected java.lang.String handleGetCreateExceptionKey()
    {
        return getName().toLowerCase() + '.' + "create.exception";
    }

    protected java.lang.String handleGetCreateExceptionPath()
    {
        return getActionInput();
    }

    protected java.lang.String handleGetCreateActionFullPath()
    {
        return '/' + getCreateActionType().replace('.', '/');
    }

    protected java.lang.String handleGetCreateActionClassName()
    {
        return "Create" + getName();
    }

    protected java.lang.String handleGetReadActionPath()
    {
        return '/' + getName() + "/Read";
    }

    protected java.lang.String handleGetReadFormBeanName()
    {
        return "read" + getName() + "Form";
    }

    protected java.lang.String handleGetReadActionType()
    {
        return getCrudPackageName() + '.' + getReadActionClassName();
    }

    protected java.lang.String handleGetReadExceptionKey()
    {
        return getName().toLowerCase() + '.' + "read.exception";
    }

    protected java.lang.String handleGetReadExceptionPath()
    {
        return getActionInput();
    }

    protected java.lang.String handleGetReadActionFullPath()
    {
        return '/' + getReadActionType().replace('.', '/');
    }

    protected java.lang.String handleGetReadActionClassName()
    {
        return "Read" + getName();
    }

    protected java.lang.String handleGetUpdateActionPath()
    {
        return '/' + getName() + "/Update";
    }

    protected java.lang.String handleGetUpdateFormBeanName()
    {
        return "update" + getName() + "Form";
    }

    protected java.lang.String handleGetUpdateActionType()
    {
        return getCrudPackageName() + '.' + getUpdateActionClassName();
    }

    protected java.lang.String handleGetUpdateExceptionKey()
    {
        return getName().toLowerCase() + '.' + "update.exception";
    }

    protected java.lang.String handleGetUpdateExceptionPath()
    {
        return getActionInput();
    }

    protected java.lang.String handleGetUpdateActionFullPath()
    {
        return '/' + getUpdateActionType().replace('.', '/');
    }

    protected java.lang.String handleGetUpdateActionClassName()
    {
        return "Update" + getName();
    }

    protected java.lang.String handleGetDeleteActionPath()
    {
        return '/' + getName() + "/Delete";
    }

    protected java.lang.String handleGetDeleteFormBeanName()
    {
        return "delete" + getName() + "Form";
    }

    protected java.lang.String handleGetDeleteActionType()
    {
        return getCrudPackageName() + '.' + getDeleteActionClassName();
    }

    protected java.lang.String handleGetDeleteExceptionKey()
    {
        return getName().toLowerCase() + '.' + "delete.exception";
    }

    protected java.lang.String handleGetDeleteExceptionPath()
    {
        return getActionInput();
    }

    protected java.lang.String handleGetDeleteActionFullPath()
    {
        return '/' + getDeleteActionType().replace('.', '/');
    }

    protected java.lang.String handleGetDeleteActionClassName()
    {
        return "Delete" + getName();
    }

    protected String handleGetPreloadActionPath()
    {
        return '/' + getName() + "/Preload";
    }

    protected String handleGetPreloadActionType()
    {
        return getCrudPackageName() + '.' + getPreloadActionClassName();
    }

    protected String handleGetPreloadFormBeanName()
    {
        return "preload" + getName() + "Form";
    }

    protected String handleGetPreloadActionFullPath()
    {
        return getCrudPackagePath() + '/' + getPreloadActionClassName();
    }

    protected String handleGetPreloadActionClassName()
    {
        return "Preload" + getName();
    }

    protected String handleGetPreloadForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    protected boolean handleIsPreload()
    {
        return isCreate() || isRead() || isUpdate() || isDelete();
    }

    protected String handleGetPreloadForwardPath()
    {
        return getPreloadActionPath();
    }
}