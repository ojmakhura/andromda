package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
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

    public StrutsManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected String handleGetFormBeanType()
    {
        return getManageablePackageName() + '.' + getFormBeanClassName();
    }

    protected String handleGetFormBeanClassName()
    {
        return getName() + "Form";
    }

    protected String handleGetFormBeanFullPath()
    {
        return getFormBeanType().replace('.', '/');
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
        return getName().toLowerCase() + "-manageable.jsp";
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
        return "manage" + getName() + "Form";
    }

    protected java.lang.String handleGetActionType()
    {
        return getManageablePackageName() + '.' + getActionClassName();
    }

    protected java.lang.String handleGetExceptionKey()
    {
        return getName().toLowerCase() + ".exception";
    }

    protected java.lang.String handleGetExceptionPath()
    {
        return getActionPath() + ".do";
    }

    protected java.lang.String handleGetActionFullPath()
    {
        return '/' + getActionType().replace('.', '/');
    }

    protected java.lang.String handleGetActionClassName()
    {
        return "Manage" + getName();
    }

    protected boolean handleIsPreload()
    {
        return isCreate() || isRead() || isUpdate() || isDelete();
    }

    protected int handleGetMaximumListSize()
    {
        int maximumListSize = -1;

        final Object taggedValueObject = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_MANAGEABLE_MAXIMUM_LIST_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                maximumListSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                maximumListSize = internalDefaultMaximumListSize();
            }
        }
        else
        {
            maximumListSize = internalDefaultMaximumListSize();
        }

        return maximumListSize;
    }

    private int internalDefaultMaximumListSize()
    {
        int maximumListSize = -1;

        try
        {
            maximumListSize =
                    Integer.parseInt((String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_MAX_LIST_SIZE));
        }
        catch (NumberFormatException e1)
        {
            maximumListSize = -1;
        }

        return maximumListSize;
    }

    protected int handleGetPageSize()
    {
        int pageSize = 20;

        final Object taggedValueObject = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_MANAGEABLE_PAGE_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                pageSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                pageSize = internalDefaultPageSize();
            }
        }
        else
        {
            pageSize = internalDefaultPageSize();
        }

        return pageSize;
    }

    private int internalDefaultPageSize()
    {
        int pageSize = 20;

        try
        {
            pageSize = Integer.parseInt((String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_PAGE_SIZE));
        }
        catch (NumberFormatException e1)
        {
            pageSize = 20;
        }

        return pageSize;
    }

    protected boolean handleIsResolveable()
    {
        boolean resolveable = true;

        final Object taggedValueObject = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_MANAGEABLE_RESOLVEABLE);
        if (taggedValueObject != null)
        {
            try
            {
                resolveable = Boolean.valueOf(taggedValueObject.toString()).booleanValue();
            }
            catch (NumberFormatException e)
            {
                resolveable = internalDefaultResolveable();
            }
        }
        else
        {
            resolveable = internalDefaultResolveable();
        }

        return resolveable;
    }

    private boolean internalDefaultResolveable()
    {
        boolean resolveable = true;

        try
        {
            resolveable =
                    Boolean.valueOf((String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_RESOLVEABLE))
                    .booleanValue();
        }
        catch (NumberFormatException e1)
        {
            resolveable = true;
        }

        return resolveable;
    }
}