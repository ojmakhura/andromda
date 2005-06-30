package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
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

    public StrutsManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected String handleGetFormBeanType()
    {
        return getManageablePackageName() + this.getNamespaceProperty() + getFormBeanClassName();
    }

    protected String handleGetFormBeanClassName()
    {
        return getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    protected String handleGetFormBeanFullPath()
    {
        return StringUtils.replace(this.getFormBeanType(), this.getNamespaceProperty(), "/");
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
        return getName().toLowerCase() + "-crud.jsp";
    }

    protected String handleGetPageFullPath()
    {
        return '/' + getManageablePackagePath() + '/' + getPageName();
    }

    protected java.lang.String handleGetActionPath()
    {
        return '/' + getName() + "/Manage";
    }

    protected java.lang.String handleGetActionParameter()
    {
        return "crud";
    }

    protected java.lang.String handleGetFormBeanName()
    {
        return "manage" + getName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    protected java.lang.String handleGetActionType()
    {
        return getManageablePackageName() + this.getNamespaceProperty() + getActionClassName();
    }

    protected java.lang.String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".exception";
    }

    protected java.lang.String handleGetExceptionPath()
    {
        return getPageFullPath();
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
        return isCreate() || isRead() || isUpdate() || isDelete();
    }
}