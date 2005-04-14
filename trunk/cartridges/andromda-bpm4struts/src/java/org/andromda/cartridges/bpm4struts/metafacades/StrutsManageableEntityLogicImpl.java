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

    protected java.lang.String handleGetActionInput()
    {
        return '/' + getCrudPackagePath() + '/' + getPageName();
    }

    protected java.lang.String handleGetActionForwardPath()
    {
        return getActionInput();
    }

    protected java.lang.String handleGetPageTitleKey()
    {
        return getName().toLowerCase() + ".page.title";
    }

    protected java.lang.String handleGetPageTitleValue()
    {
        return getName();
    }

    protected java.lang.String handleGetListName()
    {
        return "crudList";
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

    protected java.lang.String handleGetCreateFormBeanType()
    {
        return getCrudPackageName() + '.' + getCreateFormBeanClassName();
    }

    protected java.lang.String handleGetCreateForwardName()
    {
        return "create" + '.' + getName().toLowerCase();
    }

    protected java.lang.String handleGetCreateForwardPath()
    {
        return getCreateActionPath() + ".do";
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

    protected java.lang.String handleGetCreateActionForwardName()
    {
        return "crud";
    }

    protected java.lang.String handleGetCreateActionFullPath()
    {
        return '/' + getCreateActionType().replace('.', '/');
    }

    protected java.lang.String handleGetCreateActionClassName()
    {
        return StringUtilsHelper.upperCamelCaseName("create") + getName();
    }

    protected java.lang.String handleGetCreateFormBeanFullPath()
    {
        return getCrudPackagePath() + '/' + getCreateFormBeanClassName();
    }

    protected java.lang.String handleGetCreateFormBeanClassName()
    {
        return getCreateActionClassName() + "Form";
    }

    protected java.lang.String handleGetReadActionPath()
    {
        return '/' + getName() + "/Read";
    }

    protected java.lang.String handleGetReadFormBeanName()
    {
        return "read" + getName() + "Form";
    }

    protected java.lang.String handleGetReadFormBeanType()
    {
        return getCrudPackageName() + '.' + getReadFormBeanClassName();
    }

    protected java.lang.String handleGetReadForwardName()
    {
        return "read" + '.' + getName().toLowerCase();
    }

    protected java.lang.String handleGetReadForwardPath()
    {
        return getReadActionPath() + ".do";
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
        return StringUtilsHelper.upperCamelCaseName("read") + getName();
    }

    protected java.lang.String handleGetReadFormBeanFullPath()
    {
        return getCrudPackagePath() + '/' + getReadFormBeanClassName();
    }

    protected java.lang.String handleGetReadFormBeanClassName()
    {
        return getReadActionClassName() + "Form";
    }

    protected java.lang.String handleGetUpdateActionPath()
    {
        return '/' + getName() + "/Update";
    }

    protected java.lang.String handleGetUpdateFormBeanName()
    {
        return "update" + getName() + "Form";
    }

    protected java.lang.String handleGetUpdateFormBeanType()
    {
        return getCrudPackageName() + '.' + getUpdateFormBeanClassName();
    }

    protected java.lang.String handleGetUpdateForwardName()
    {
        return "update" + '.' + getName().toLowerCase();
    }

    protected java.lang.String handleGetUpdateForwardPath()
    {
        return getUpdateActionPath() + ".do";
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

    protected java.lang.String handleGetUpdateFormBeanFullPath()
    {
        return getCrudPackagePath() + '/' + getUpdateFormBeanClassName();
    }

    protected java.lang.String handleGetUpdateFormBeanClassName()
    {
        return getUpdateActionClassName() + "Form";
    }

    protected java.lang.String handleGetDeleteActionPath()
    {
        return '/' + getName() + "/Delete";
    }

    protected java.lang.String handleGetDeleteFormBeanName()
    {
        return "delete" + getName() + "Form";
    }

    protected java.lang.String handleGetDeleteFormBeanType()
    {
        return getCrudPackageName() + '.' + getDeleteFormBeanClassName();
    }

    protected java.lang.String handleGetDeleteForwardName()
    {
        return "delete" + '.' + getName().toLowerCase();
    }

    protected java.lang.String handleGetDeleteForwardPath()
    {
        return getDeleteActionPath() + ".do";
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

    protected java.lang.String handleGetDeleteFormBeanFullPath()
    {
        return getCrudPackagePath() + '/' + getDeleteFormBeanClassName();
    }

    protected java.lang.String handleGetDeleteFormBeanClassName()
    {
        return getDeleteActionClassName() + "Form";
    }
}