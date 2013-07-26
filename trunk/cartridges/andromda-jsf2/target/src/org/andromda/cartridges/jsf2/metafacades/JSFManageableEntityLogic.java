// license-header java merge-point
//
// Attention: generated code (by MetafacadeLogic.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import java.util.List;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.EntityQueryOperation;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.log4j.Logger;

/**
 * TODO: Model Documentation for org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity
 * MetafacadeLogic for JSFManageableEntity
 *
 * @see JSFManageableEntity
 */
public abstract class JSFManageableEntityLogic
    extends MetafacadeBase
    implements JSFManageableEntity
{
    /**
     * The underlying UML object
     * @see Object
     */
    protected Object metaObject;

    /** Create Metafacade implementation instance using the MetafacadeFactory from the context
     * @param metaObjectIn
     * @param context
     */
    protected JSFManageableEntityLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superManageableEntity =
           (ManageableEntity)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.ManageableEntity",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFManageableEntityLogic.class);

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFManageableEntity if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity";
        }
        return context;
    }

    private ManageableEntity superManageableEntity;
    private boolean superManageableEntityInitialized = false;

    /**
     * Gets the ManageableEntity parent instance.
     * @return this.superManageableEntity ManageableEntity
     */
    private ManageableEntity getSuperManageableEntity()
    {
        if (!this.superManageableEntityInitialized)
        {
            ((MetafacadeBase)this.superManageableEntity).setMetafacadeContext(this.getMetafacadeContext());
            this.superManageableEntityInitialized = true;
        }
        return this.superManageableEntity;
    }

    /** Reset context only for non-root metafacades
     * @param context
     * @see org.andromda.core.metafacade.MetafacadeBase#resetMetafacadeContext(String context)
     */
    @Override
    public void resetMetafacadeContext(String context)
    {
        if (!this.contextRoot) // reset context only for non-root metafacades
        {
            context = getContext(context);  // to have same value as in original constructor call
            setMetafacadeContext (context);
            if (this.superManageableEntityInitialized)
            {
                ((MetafacadeBase)this.superManageableEntity).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFManageableEntity
     */
    public boolean isJSFManageableEntityMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getViewName()
    * @return String
    */
    protected abstract String handleGetViewName();

    private String __viewName1a;
    private boolean __viewName1aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewName
     * @return (String)handleGetViewName()
     */
    public final String getViewName()
    {
        String viewName1a = this.__viewName1a;
        if (!this.__viewName1aSet)
        {
            // viewName has no pre constraints
            viewName1a = handleGetViewName();
            // viewName has no post constraints
            this.__viewName1a = viewName1a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__viewName1aSet = true;
            }
        }
        return viewName1a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getViewTitleKey()
    * @return String
    */
    protected abstract String handleGetViewTitleKey();

    private String __viewTitleKey2a;
    private boolean __viewTitleKey2aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewTitleKey
     * @return (String)handleGetViewTitleKey()
     */
    public final String getViewTitleKey()
    {
        String viewTitleKey2a = this.__viewTitleKey2a;
        if (!this.__viewTitleKey2aSet)
        {
            // viewTitleKey has no pre constraints
            viewTitleKey2a = handleGetViewTitleKey();
            // viewTitleKey has no post constraints
            this.__viewTitleKey2a = viewTitleKey2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__viewTitleKey2aSet = true;
            }
        }
        return viewTitleKey2a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getViewTitleValue()
    * @return String
    */
    protected abstract String handleGetViewTitleValue();

    private String __viewTitleValue3a;
    private boolean __viewTitleValue3aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewTitleValue
     * @return (String)handleGetViewTitleValue()
     */
    public final String getViewTitleValue()
    {
        String viewTitleValue3a = this.__viewTitleValue3a;
        if (!this.__viewTitleValue3aSet)
        {
            // viewTitleValue has no pre constraints
            viewTitleValue3a = handleGetViewTitleValue();
            // viewTitleValue has no post constraints
            this.__viewTitleValue3a = viewTitleValue3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__viewTitleValue3aSet = true;
            }
        }
        return viewTitleValue3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getListName()
    * @return String
    */
    protected abstract String handleGetListName();

    private String __listName4a;
    private boolean __listName4aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.listName
     * @return (String)handleGetListName()
     */
    public final String getListName()
    {
        String listName4a = this.__listName4a;
        if (!this.__listName4aSet)
        {
            // listName has no pre constraints
            listName4a = handleGetListName();
            // listName has no post constraints
            this.__listName4a = listName4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__listName4aSet = true;
            }
        }
        return listName4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getFormBeanType()
    * @return String
    */
    protected abstract String handleGetFormBeanType();

    private String __formBeanType5a;
    private boolean __formBeanType5aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanType
     * @return (String)handleGetFormBeanType()
     */
    public final String getFormBeanType()
    {
        String formBeanType5a = this.__formBeanType5a;
        if (!this.__formBeanType5aSet)
        {
            // formBeanType has no pre constraints
            formBeanType5a = handleGetFormBeanType();
            // formBeanType has no post constraints
            this.__formBeanType5a = formBeanType5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formBeanType5aSet = true;
            }
        }
        return formBeanType5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getFormBeanName()
    * @return String
    */
    protected abstract String handleGetFormBeanName();

    private String __formBeanName6a;
    private boolean __formBeanName6aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanName
     * @return (String)handleGetFormBeanName()
     */
    public final String getFormBeanName()
    {
        String formBeanName6a = this.__formBeanName6a;
        if (!this.__formBeanName6aSet)
        {
            // formBeanName has no pre constraints
            formBeanName6a = handleGetFormBeanName();
            // formBeanName has no post constraints
            this.__formBeanName6a = formBeanName6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formBeanName6aSet = true;
            }
        }
        return formBeanName6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getExceptionKey()
    * @return String
    */
    protected abstract String handleGetExceptionKey();

    private String __exceptionKey7a;
    private boolean __exceptionKey7aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.exceptionKey
     * @return (String)handleGetExceptionKey()
     */
    public final String getExceptionKey()
    {
        String exceptionKey7a = this.__exceptionKey7a;
        if (!this.__exceptionKey7aSet)
        {
            // exceptionKey has no pre constraints
            exceptionKey7a = handleGetExceptionKey();
            // exceptionKey has no post constraints
            this.__exceptionKey7a = exceptionKey7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__exceptionKey7aSet = true;
            }
        }
        return exceptionKey7a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getActionType()
    * @return String
    */
    protected abstract String handleGetActionType();

    private String __actionType8a;
    private boolean __actionType8aSet = false;

    /**
     * The fully qualified name of the action class that execute the manageable actions.
     * @return (String)handleGetActionType()
     */
    public final String getActionType()
    {
        String actionType8a = this.__actionType8a;
        if (!this.__actionType8aSet)
        {
            // actionType has no pre constraints
            actionType8a = handleGetActionType();
            // actionType has no post constraints
            this.__actionType8a = actionType8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionType8aSet = true;
            }
        }
        return actionType8a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getActionFullPath()
    * @return String
    */
    protected abstract String handleGetActionFullPath();

    private String __actionFullPath9a;
    private boolean __actionFullPath9aSet = false;

    /**
     * The fully qualified path to the action class that execute the manageable actions.
     * @return (String)handleGetActionFullPath()
     */
    public final String getActionFullPath()
    {
        String actionFullPath9a = this.__actionFullPath9a;
        if (!this.__actionFullPath9aSet)
        {
            // actionFullPath has no pre constraints
            actionFullPath9a = handleGetActionFullPath();
            // actionFullPath has no post constraints
            this.__actionFullPath9a = actionFullPath9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionFullPath9aSet = true;
            }
        }
        return actionFullPath9a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getActionPath()
    * @return String
    */
    protected abstract String handleGetActionPath();

    private String __actionPath10a;
    private boolean __actionPath10aSet = false;

    /**
     * The path to the action class that execute the manageable actions.
     * @return (String)handleGetActionPath()
     */
    public final String getActionPath()
    {
        String actionPath10a = this.__actionPath10a;
        if (!this.__actionPath10aSet)
        {
            // actionPath has no pre constraints
            actionPath10a = handleGetActionPath();
            // actionPath has no post constraints
            this.__actionPath10a = actionPath10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionPath10aSet = true;
            }
        }
        return actionPath10a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getActionClassName()
    * @return String
    */
    protected abstract String handleGetActionClassName();

    private String __actionClassName11a;
    private boolean __actionClassName11aSet = false;

    /**
     * The name of the action class that executes the manageable actions.
     * @return (String)handleGetActionClassName()
     */
    public final String getActionClassName()
    {
        String actionClassName11a = this.__actionClassName11a;
        if (!this.__actionClassName11aSet)
        {
            // actionClassName has no pre constraints
            actionClassName11a = handleGetActionClassName();
            // actionClassName has no post constraints
            this.__actionClassName11a = actionClassName11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionClassName11aSet = true;
            }
        }
        return actionClassName11a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getExceptionPath()
    * @return String
    */
    protected abstract String handleGetExceptionPath();

    private String __exceptionPath12a;
    private boolean __exceptionPath12aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.exceptionPath
     * @return (String)handleGetExceptionPath()
     */
    public final String getExceptionPath()
    {
        String exceptionPath12a = this.__exceptionPath12a;
        if (!this.__exceptionPath12aSet)
        {
            // exceptionPath has no pre constraints
            exceptionPath12a = handleGetExceptionPath();
            // exceptionPath has no post constraints
            this.__exceptionPath12a = exceptionPath12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__exceptionPath12aSet = true;
            }
        }
        return exceptionPath12a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isPreload()
    * @return boolean
    */
    protected abstract boolean handleIsPreload();

    private boolean __preload13a;
    private boolean __preload13aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.preload
     * @return (boolean)handleIsPreload()
     */
    public final boolean isPreload()
    {
        boolean preload13a = this.__preload13a;
        if (!this.__preload13aSet)
        {
            // preload has no pre constraints
            preload13a = handleIsPreload();
            // preload has no post constraints
            this.__preload13a = preload13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__preload13aSet = true;
            }
        }
        return preload13a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getFormBeanClassName()
    * @return String
    */
    protected abstract String handleGetFormBeanClassName();

    private String __formBeanClassName14a;
    private boolean __formBeanClassName14aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanClassName
     * @return (String)handleGetFormBeanClassName()
     */
    public final String getFormBeanClassName()
    {
        String formBeanClassName14a = this.__formBeanClassName14a;
        if (!this.__formBeanClassName14aSet)
        {
            // formBeanClassName has no pre constraints
            formBeanClassName14a = handleGetFormBeanClassName();
            // formBeanClassName has no post constraints
            this.__formBeanClassName14a = formBeanClassName14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formBeanClassName14aSet = true;
            }
        }
        return formBeanClassName14a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getFormBeanFullPath()
    * @return String
    */
    protected abstract String handleGetFormBeanFullPath();

    private String __formBeanFullPath15a;
    private boolean __formBeanFullPath15aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanFullPath
     * @return (String)handleGetFormBeanFullPath()
     */
    public final String getFormBeanFullPath()
    {
        String formBeanFullPath15a = this.__formBeanFullPath15a;
        if (!this.__formBeanFullPath15aSet)
        {
            // formBeanFullPath has no pre constraints
            formBeanFullPath15a = handleGetFormBeanFullPath();
            // formBeanFullPath has no post constraints
            this.__formBeanFullPath15a = formBeanFullPath15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formBeanFullPath15aSet = true;
            }
        }
        return formBeanFullPath15a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getListGetterName()
    * @return String
    */
    protected abstract String handleGetListGetterName();

    private String __listGetterName16a;
    private boolean __listGetterName16aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.listGetterName
     * @return (String)handleGetListGetterName()
     */
    public final String getListGetterName()
    {
        String listGetterName16a = this.__listGetterName16a;
        if (!this.__listGetterName16aSet)
        {
            // listGetterName has no pre constraints
            listGetterName16a = handleGetListGetterName();
            // listGetterName has no post constraints
            this.__listGetterName16a = listGetterName16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__listGetterName16aSet = true;
            }
        }
        return listGetterName16a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getListSetterName()
    * @return String
    */
    protected abstract String handleGetListSetterName();

    private String __listSetterName17a;
    private boolean __listSetterName17aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.listSetterName
     * @return (String)handleGetListSetterName()
     */
    public final String getListSetterName()
    {
        String listSetterName17a = this.__listSetterName17a;
        if (!this.__listSetterName17aSet)
        {
            // listSetterName has no pre constraints
            listSetterName17a = handleGetListSetterName();
            // listSetterName has no post constraints
            this.__listSetterName17a = listSetterName17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__listSetterName17aSet = true;
            }
        }
        return listSetterName17a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey18a;
    private boolean __messageKey18aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.messageKey
     * @return (String)handleGetMessageKey()
     */
    public final String getMessageKey()
    {
        String messageKey18a = this.__messageKey18a;
        if (!this.__messageKey18aSet)
        {
            // messageKey has no pre constraints
            messageKey18a = handleGetMessageKey();
            // messageKey has no post constraints
            this.__messageKey18a = messageKey18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageKey18aSet = true;
            }
        }
        return messageKey18a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getMessageValue()
    * @return String
    */
    protected abstract String handleGetMessageValue();

    private String __messageValue19a;
    private boolean __messageValue19aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.messageValue
     * @return (String)handleGetMessageValue()
     */
    public final String getMessageValue()
    {
        String messageValue19a = this.__messageValue19a;
        if (!this.__messageValue19aSet)
        {
            // messageValue has no pre constraints
            messageValue19a = handleGetMessageValue();
            // messageValue has no post constraints
            this.__messageValue19a = messageValue19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageValue19aSet = true;
            }
        }
        return messageValue19a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getOnlineHelpKey()
    * @return String
    */
    protected abstract String handleGetOnlineHelpKey();

    private String __onlineHelpKey20a;
    private boolean __onlineHelpKey20aSet = false;

    /**
     * The key to lookup the online help documentation.
     * @return (String)handleGetOnlineHelpKey()
     */
    public final String getOnlineHelpKey()
    {
        String onlineHelpKey20a = this.__onlineHelpKey20a;
        if (!this.__onlineHelpKey20aSet)
        {
            // onlineHelpKey has no pre constraints
            onlineHelpKey20a = handleGetOnlineHelpKey();
            // onlineHelpKey has no post constraints
            this.__onlineHelpKey20a = onlineHelpKey20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpKey20aSet = true;
            }
        }
        return onlineHelpKey20a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getOnlineHelpValue()
    * @return String
    */
    protected abstract String handleGetOnlineHelpValue();

    private String __onlineHelpValue21a;
    private boolean __onlineHelpValue21aSet = false;

    /**
     * The online help documentation. The format is HTML without any style.
     * @return (String)handleGetOnlineHelpValue()
     */
    public final String getOnlineHelpValue()
    {
        String onlineHelpValue21a = this.__onlineHelpValue21a;
        if (!this.__onlineHelpValue21aSet)
        {
            // onlineHelpValue has no pre constraints
            onlineHelpValue21a = handleGetOnlineHelpValue();
            // onlineHelpValue has no post constraints
            this.__onlineHelpValue21a = onlineHelpValue21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpValue21aSet = true;
            }
        }
        return onlineHelpValue21a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getOnlineHelpActionPath()
    * @return String
    */
    protected abstract String handleGetOnlineHelpActionPath();

    private String __onlineHelpActionPath22a;
    private boolean __onlineHelpActionPath22aSet = false;

    /**
     * The full path to this entity's online help action. The returned String does not have a suffix
     * such as '.do'.
     * @return (String)handleGetOnlineHelpActionPath()
     */
    public final String getOnlineHelpActionPath()
    {
        String onlineHelpActionPath22a = this.__onlineHelpActionPath22a;
        if (!this.__onlineHelpActionPath22aSet)
        {
            // onlineHelpActionPath has no pre constraints
            onlineHelpActionPath22a = handleGetOnlineHelpActionPath();
            // onlineHelpActionPath has no post constraints
            this.__onlineHelpActionPath22a = onlineHelpActionPath22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpActionPath22aSet = true;
            }
        }
        return onlineHelpActionPath22a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getOnlineHelpPagePath()
    * @return String
    */
    protected abstract String handleGetOnlineHelpPagePath();

    private String __onlineHelpPagePath23a;
    private boolean __onlineHelpPagePath23aSet = false;

    /**
     * The full path to this entitiy's online help page. The returned String does not have a suffix
     * such as '.jsp'.
     * @return (String)handleGetOnlineHelpPagePath()
     */
    public final String getOnlineHelpPagePath()
    {
        String onlineHelpPagePath23a = this.__onlineHelpPagePath23a;
        if (!this.__onlineHelpPagePath23aSet)
        {
            // onlineHelpPagePath has no pre constraints
            onlineHelpPagePath23a = handleGetOnlineHelpPagePath();
            // onlineHelpPagePath has no post constraints
            this.__onlineHelpPagePath23a = onlineHelpPagePath23a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpPagePath23aSet = true;
            }
        }
        return onlineHelpPagePath23a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isTableExportable()
    * @return boolean
    */
    protected abstract boolean handleIsTableExportable();

    private boolean __tableExportable24a;
    private boolean __tableExportable24aSet = false;

    /**
     * True if it is possible to export the table data to XML, CSV, PDF or Excel format.
     * @return (boolean)handleIsTableExportable()
     */
    public final boolean isTableExportable()
    {
        boolean tableExportable24a = this.__tableExportable24a;
        if (!this.__tableExportable24aSet)
        {
            // tableExportable has no pre constraints
            tableExportable24a = handleIsTableExportable();
            // tableExportable has no post constraints
            this.__tableExportable24a = tableExportable24a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableExportable24aSet = true;
            }
        }
        return tableExportable24a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getTableExportTypes()
    * @return String
    */
    protected abstract String handleGetTableExportTypes();

    private String __tableExportTypes25a;
    private boolean __tableExportTypes25aSet = false;

    /**
     * Tthe available types of export in a single String instance.
     * @return (String)handleGetTableExportTypes()
     */
    public final String getTableExportTypes()
    {
        String tableExportTypes25a = this.__tableExportTypes25a;
        if (!this.__tableExportTypes25aSet)
        {
            // tableExportTypes has no pre constraints
            tableExportTypes25a = handleGetTableExportTypes();
            // tableExportTypes has no post constraints
            this.__tableExportTypes25a = tableExportTypes25a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableExportTypes25aSet = true;
            }
        }
        return tableExportTypes25a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getTableMaxRows()
    * @return int
    */
    protected abstract int handleGetTableMaxRows();

    private int __tableMaxRows26a;
    private boolean __tableMaxRows26aSet = false;

    /**
     * The maximum number of rows to be displayed in the table at the same time. This is also known
     * as the page size. A value of zero or less will display all data in the same table (therefore
     * also on the same page).
     * @return (int)handleGetTableMaxRows()
     */
    public final int getTableMaxRows()
    {
        int tableMaxRows26a = this.__tableMaxRows26a;
        if (!this.__tableMaxRows26aSet)
        {
            // tableMaxRows has no pre constraints
            tableMaxRows26a = handleGetTableMaxRows();
            // tableMaxRows has no post constraints
            this.__tableMaxRows26a = tableMaxRows26a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableMaxRows26aSet = true;
            }
        }
        return tableMaxRows26a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isTableSortable()
    * @return boolean
    */
    protected abstract boolean handleIsTableSortable();

    private boolean __tableSortable27a;
    private boolean __tableSortable27aSet = false;

    /**
     * True if it is possible to sort the columns of the table.
     * @return (boolean)handleIsTableSortable()
     */
    public final boolean isTableSortable()
    {
        boolean tableSortable27a = this.__tableSortable27a;
        if (!this.__tableSortable27aSet)
        {
            // tableSortable has no pre constraints
            tableSortable27a = handleIsTableSortable();
            // tableSortable has no post constraints
            this.__tableSortable27a = tableSortable27a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableSortable27aSet = true;
            }
        }
        return tableSortable27a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getControllerType()
    * @return String
    */
    protected abstract String handleGetControllerType();

    private String __controllerType28a;
    private boolean __controllerType28aSet = false;

    /**
     * Fully qualified name of this manageable controller.
     * @return (String)handleGetControllerType()
     */
    public final String getControllerType()
    {
        String controllerType28a = this.__controllerType28a;
        if (!this.__controllerType28aSet)
        {
            // controllerType has no pre constraints
            controllerType28a = handleGetControllerType();
            // controllerType has no post constraints
            this.__controllerType28a = controllerType28a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__controllerType28aSet = true;
            }
        }
        return controllerType28a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getControllerBeanName()
    * @return String
    */
    protected abstract String handleGetControllerBeanName();

    private String __controllerBeanName29a;
    private boolean __controllerBeanName29aSet = false;

    /**
     * The bean name of this manageable controller (this is what is stored in the JSF configuration
     * file).
     * @return (String)handleGetControllerBeanName()
     */
    public final String getControllerBeanName()
    {
        String controllerBeanName29a = this.__controllerBeanName29a;
        if (!this.__controllerBeanName29aSet)
        {
            // controllerBeanName has no pre constraints
            controllerBeanName29a = handleGetControllerBeanName();
            // controllerBeanName has no post constraints
            this.__controllerBeanName29a = controllerBeanName29a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__controllerBeanName29aSet = true;
            }
        }
        return controllerBeanName29a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getControllerFullPath()
    * @return String
    */
    protected abstract String handleGetControllerFullPath();

    private String __controllerFullPath30a;
    private boolean __controllerFullPath30aSet = false;

    /**
     * Full path of this manageable controller.
     * @return (String)handleGetControllerFullPath()
     */
    public final String getControllerFullPath()
    {
        String controllerFullPath30a = this.__controllerFullPath30a;
        if (!this.__controllerFullPath30aSet)
        {
            // controllerFullPath has no pre constraints
            controllerFullPath30a = handleGetControllerFullPath();
            // controllerFullPath has no post constraints
            this.__controllerFullPath30a = controllerFullPath30a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__controllerFullPath30aSet = true;
            }
        }
        return controllerFullPath30a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getControllerName()
    * @return String
    */
    protected abstract String handleGetControllerName();

    private String __controllerName31a;
    private boolean __controllerName31aSet = false;

    /**
     * Manageable controller class name.
     * @return (String)handleGetControllerName()
     */
    public final String getControllerName()
    {
        String controllerName31a = this.__controllerName31a;
        if (!this.__controllerName31aSet)
        {
            // controllerName has no pre constraints
            controllerName31a = handleGetControllerName();
            // controllerName has no post constraints
            this.__controllerName31a = controllerName31a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__controllerName31aSet = true;
            }
        }
        return controllerName31a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getValueObjectClassName()
    * @return String
    */
    protected abstract String handleGetValueObjectClassName();

    private String __valueObjectClassName32a;
    private boolean __valueObjectClassName32aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.valueObjectClassName
     * @return (String)handleGetValueObjectClassName()
     */
    public final String getValueObjectClassName()
    {
        String valueObjectClassName32a = this.__valueObjectClassName32a;
        if (!this.__valueObjectClassName32aSet)
        {
            // valueObjectClassName has no pre constraints
            valueObjectClassName32a = handleGetValueObjectClassName();
            // valueObjectClassName has no post constraints
            this.__valueObjectClassName32a = valueObjectClassName32a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueObjectClassName32aSet = true;
            }
        }
        return valueObjectClassName32a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getFormSerialVersionUID()
    * @return String
    */
    protected abstract String handleGetFormSerialVersionUID();

    private String __formSerialVersionUID33a;
    private boolean __formSerialVersionUID33aSet = false;

    /**
     * The calcuated serial version UID for this action's form.
     * @return (String)handleGetFormSerialVersionUID()
     */
    public final String getFormSerialVersionUID()
    {
        String formSerialVersionUID33a = this.__formSerialVersionUID33a;
        if (!this.__formSerialVersionUID33aSet)
        {
            // formSerialVersionUID has no pre constraints
            formSerialVersionUID33a = handleGetFormSerialVersionUID();
            // formSerialVersionUID has no post constraints
            this.__formSerialVersionUID33a = formSerialVersionUID33a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formSerialVersionUID33aSet = true;
            }
        }
        return formSerialVersionUID33a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getActionSerialVersionUID()
    * @return String
    */
    protected abstract String handleGetActionSerialVersionUID();

    private String __actionSerialVersionUID34a;
    private boolean __actionSerialVersionUID34aSet = false;

    /**
     * The calcuated serial version UID for this manageable actions.
     * @return (String)handleGetActionSerialVersionUID()
     */
    public final String getActionSerialVersionUID()
    {
        String actionSerialVersionUID34a = this.__actionSerialVersionUID34a;
        if (!this.__actionSerialVersionUID34aSet)
        {
            // actionSerialVersionUID has no pre constraints
            actionSerialVersionUID34a = handleGetActionSerialVersionUID();
            // actionSerialVersionUID has no post constraints
            this.__actionSerialVersionUID34a = actionSerialVersionUID34a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionSerialVersionUID34aSet = true;
            }
        }
        return actionSerialVersionUID34a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getPopulatorName()
    * @return String
    */
    protected abstract String handleGetPopulatorName();

    private String __populatorName35a;
    private boolean __populatorName35aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.populatorName
     * @return (String)handleGetPopulatorName()
     */
    public final String getPopulatorName()
    {
        String populatorName35a = this.__populatorName35a;
        if (!this.__populatorName35aSet)
        {
            // populatorName has no pre constraints
            populatorName35a = handleGetPopulatorName();
            // populatorName has no post constraints
            this.__populatorName35a = populatorName35a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__populatorName35aSet = true;
            }
        }
        return populatorName35a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getPopulatorFullPath()
    * @return String
    */
    protected abstract String handleGetPopulatorFullPath();

    private String __populatorFullPath36a;
    private boolean __populatorFullPath36aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.populatorFullPath
     * @return (String)handleGetPopulatorFullPath()
     */
    public final String getPopulatorFullPath()
    {
        String populatorFullPath36a = this.__populatorFullPath36a;
        if (!this.__populatorFullPath36aSet)
        {
            // populatorFullPath has no pre constraints
            populatorFullPath36a = handleGetPopulatorFullPath();
            // populatorFullPath has no post constraints
            this.__populatorFullPath36a = populatorFullPath36a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__populatorFullPath36aSet = true;
            }
        }
        return populatorFullPath36a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getPopulatorType()
    * @return String
    */
    protected abstract String handleGetPopulatorType();

    private String __populatorType37a;
    private boolean __populatorType37aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.populatorType
     * @return (String)handleGetPopulatorType()
     */
    public final String getPopulatorType()
    {
        String populatorType37a = this.__populatorType37a;
        if (!this.__populatorType37aSet)
        {
            // populatorType has no pre constraints
            populatorType37a = handleGetPopulatorType();
            // populatorType has no post constraints
            this.__populatorType37a = populatorType37a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__populatorType37aSet = true;
            }
        }
        return populatorType37a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getViewFullPath()
    * @return String
    */
    protected abstract String handleGetViewFullPath();

    private String __viewFullPath38a;
    private boolean __viewFullPath38aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewFullPath
     * @return (String)handleGetViewFullPath()
     */
    public final String getViewFullPath()
    {
        String viewFullPath38a = this.__viewFullPath38a;
        if (!this.__viewFullPath38aSet)
        {
            // viewFullPath has no pre constraints
            viewFullPath38a = handleGetViewFullPath();
            // viewFullPath has no post constraints
            this.__viewFullPath38a = viewFullPath38a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__viewFullPath38aSet = true;
            }
        }
        return viewFullPath38a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired39a;
    private boolean __validationRequired39aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.validationRequired
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired39a = this.__validationRequired39a;
        if (!this.__validationRequired39aSet)
        {
            // validationRequired has no pre constraints
            validationRequired39a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired39a = validationRequired39a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired39aSet = true;
            }
        }
        return validationRequired39a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFormBeanName()
    * @return String
    */
    protected abstract String handleGetSearchFormBeanName();

    private String __searchFormBeanName40a;
    private boolean __searchFormBeanName40aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanName
     * @return (String)handleGetSearchFormBeanName()
     */
    public final String getSearchFormBeanName()
    {
        String searchFormBeanName40a = this.__searchFormBeanName40a;
        if (!this.__searchFormBeanName40aSet)
        {
            // searchFormBeanName has no pre constraints
            searchFormBeanName40a = handleGetSearchFormBeanName();
            // searchFormBeanName has no post constraints
            this.__searchFormBeanName40a = searchFormBeanName40a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFormBeanName40aSet = true;
            }
        }
        return searchFormBeanName40a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFormBeanType()
    * @return String
    */
    protected abstract String handleGetSearchFormBeanType();

    private String __searchFormBeanType41a;
    private boolean __searchFormBeanType41aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanType
     * @return (String)handleGetSearchFormBeanType()
     */
    public final String getSearchFormBeanType()
    {
        String searchFormBeanType41a = this.__searchFormBeanType41a;
        if (!this.__searchFormBeanType41aSet)
        {
            // searchFormBeanType has no pre constraints
            searchFormBeanType41a = handleGetSearchFormBeanType();
            // searchFormBeanType has no post constraints
            this.__searchFormBeanType41a = searchFormBeanType41a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFormBeanType41aSet = true;
            }
        }
        return searchFormBeanType41a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFormBeanFullPath()
    * @return String
    */
    protected abstract String handleGetSearchFormBeanFullPath();

    private String __searchFormBeanFullPath42a;
    private boolean __searchFormBeanFullPath42aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanFullPath
     * @return (String)handleGetSearchFormBeanFullPath()
     */
    public final String getSearchFormBeanFullPath()
    {
        String searchFormBeanFullPath42a = this.__searchFormBeanFullPath42a;
        if (!this.__searchFormBeanFullPath42aSet)
        {
            // searchFormBeanFullPath has no pre constraints
            searchFormBeanFullPath42a = handleGetSearchFormBeanFullPath();
            // searchFormBeanFullPath has no post constraints
            this.__searchFormBeanFullPath42a = searchFormBeanFullPath42a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFormBeanFullPath42aSet = true;
            }
        }
        return searchFormBeanFullPath42a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFormBeanClassName()
    * @return String
    */
    protected abstract String handleGetSearchFormBeanClassName();

    private String __searchFormBeanClassName43a;
    private boolean __searchFormBeanClassName43aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanClassName
     * @return (String)handleGetSearchFormBeanClassName()
     */
    public final String getSearchFormBeanClassName()
    {
        String searchFormBeanClassName43a = this.__searchFormBeanClassName43a;
        if (!this.__searchFormBeanClassName43aSet)
        {
            // searchFormBeanClassName has no pre constraints
            searchFormBeanClassName43a = handleGetSearchFormBeanClassName();
            // searchFormBeanClassName has no post constraints
            this.__searchFormBeanClassName43a = searchFormBeanClassName43a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFormBeanClassName43aSet = true;
            }
        }
        return searchFormBeanClassName43a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getManageableSearchAttributes()
    * @return Collection
    */
    protected abstract Collection handleGetManageableSearchAttributes();

    private Collection __manageableSearchAttributes44a;
    private boolean __manageableSearchAttributes44aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.manageableSearchAttributes
     * @return (Collection)handleGetManageableSearchAttributes()
     */
    public final Collection getManageableSearchAttributes()
    {
        Collection manageableSearchAttributes44a = this.__manageableSearchAttributes44a;
        if (!this.__manageableSearchAttributes44aSet)
        {
            // manageableSearchAttributes has no pre constraints
            manageableSearchAttributes44a = handleGetManageableSearchAttributes();
            // manageableSearchAttributes has no post constraints
            this.__manageableSearchAttributes44a = manageableSearchAttributes44a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__manageableSearchAttributes44aSet = true;
            }
        }
        return manageableSearchAttributes44a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getManageableSearchAssociationEnds()
    * @return Collection
    */
    protected abstract Collection handleGetManageableSearchAssociationEnds();

    private Collection __manageableSearchAssociationEnds45a;
    private boolean __manageableSearchAssociationEnds45aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.manageableSearchAssociationEnds
     * @return (Collection)handleGetManageableSearchAssociationEnds()
     */
    public final Collection getManageableSearchAssociationEnds()
    {
        Collection manageableSearchAssociationEnds45a = this.__manageableSearchAssociationEnds45a;
        if (!this.__manageableSearchAssociationEnds45aSet)
        {
            // manageableSearchAssociationEnds has no pre constraints
            manageableSearchAssociationEnds45a = handleGetManageableSearchAssociationEnds();
            // manageableSearchAssociationEnds has no post constraints
            this.__manageableSearchAssociationEnds45a = manageableSearchAssociationEnds45a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__manageableSearchAssociationEnds45aSet = true;
            }
        }
        return manageableSearchAssociationEnds45a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isNeedsFileUpload()
    * @return boolean
    */
    protected abstract boolean handleIsNeedsFileUpload();

    private boolean __needsFileUpload46a;
    private boolean __needsFileUpload46aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.needsFileUpload
     * @return (boolean)handleIsNeedsFileUpload()
     */
    public final boolean isNeedsFileUpload()
    {
        boolean needsFileUpload46a = this.__needsFileUpload46a;
        if (!this.__needsFileUpload46aSet)
        {
            // needsFileUpload has no pre constraints
            needsFileUpload46a = handleIsNeedsFileUpload();
            // needsFileUpload has no post constraints
            this.__needsFileUpload46a = needsFileUpload46a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__needsFileUpload46aSet = true;
            }
        }
        return needsFileUpload46a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getConverterFullPath()
    * @return String
    */
    protected abstract String handleGetConverterFullPath();

    private String __converterFullPath47a;
    private boolean __converterFullPath47aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.converterFullPath
     * @return (String)handleGetConverterFullPath()
     */
    public final String getConverterFullPath()
    {
        String converterFullPath47a = this.__converterFullPath47a;
        if (!this.__converterFullPath47aSet)
        {
            // converterFullPath has no pre constraints
            converterFullPath47a = handleGetConverterFullPath();
            // converterFullPath has no post constraints
            this.__converterFullPath47a = converterFullPath47a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__converterFullPath47aSet = true;
            }
        }
        return converterFullPath47a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getConverterType()
    * @return String
    */
    protected abstract String handleGetConverterType();

    private String __converterType48a;
    private boolean __converterType48aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.converterType
     * @return (String)handleGetConverterType()
     */
    public final String getConverterType()
    {
        String converterType48a = this.__converterType48a;
        if (!this.__converterType48aSet)
        {
            // converterType has no pre constraints
            converterType48a = handleGetConverterType();
            // converterType has no post constraints
            this.__converterType48a = converterType48a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__converterType48aSet = true;
            }
        }
        return converterType48a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getConverterClassName()
    * @return String
    */
    protected abstract String handleGetConverterClassName();

    private String __converterClassName49a;
    private boolean __converterClassName49aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.converterClassName
     * @return (String)handleGetConverterClassName()
     */
    public final String getConverterClassName()
    {
        String converterClassName49a = this.__converterClassName49a;
        if (!this.__converterClassName49aSet)
        {
            // converterClassName has no pre constraints
            converterClassName49a = handleGetConverterClassName();
            // converterClassName has no post constraints
            this.__converterClassName49a = converterClassName49a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__converterClassName49aSet = true;
            }
        }
        return converterClassName49a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getOdsExportFullPath()
    * @return String
    */
    protected abstract String handleGetOdsExportFullPath();

    private String __odsExportFullPath50a;
    private boolean __odsExportFullPath50aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.odsExportFullPath
     * @return (String)handleGetOdsExportFullPath()
     */
    public final String getOdsExportFullPath()
    {
        String odsExportFullPath50a = this.__odsExportFullPath50a;
        if (!this.__odsExportFullPath50aSet)
        {
            // odsExportFullPath has no pre constraints
            odsExportFullPath50a = handleGetOdsExportFullPath();
            // odsExportFullPath has no post constraints
            this.__odsExportFullPath50a = odsExportFullPath50a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__odsExportFullPath50aSet = true;
            }
        }
        return odsExportFullPath50a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isNeedsUserInterface()
    * @return boolean
    */
    protected abstract boolean handleIsNeedsUserInterface();

    private boolean __needsUserInterface51a;
    private boolean __needsUserInterface51aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.needsUserInterface
     * @return (boolean)handleIsNeedsUserInterface()
     */
    public final boolean isNeedsUserInterface()
    {
        boolean needsUserInterface51a = this.__needsUserInterface51a;
        if (!this.__needsUserInterface51aSet)
        {
            // needsUserInterface has no pre constraints
            needsUserInterface51a = handleIsNeedsUserInterface();
            // needsUserInterface has no post constraints
            this.__needsUserInterface51a = needsUserInterface51a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__needsUserInterface51aSet = true;
            }
        }
        return needsUserInterface51a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#isNeedsImplementation()
    * @return boolean
    */
    protected abstract boolean handleIsNeedsImplementation();

    private boolean __needsImplementation52a;
    private boolean __needsImplementation52aSet = false;

    /**
     * Returns true if the user needs to modify the standard behavior and a Impl.java file will be
     * created in web/src/main/java.
     * @return (boolean)handleIsNeedsImplementation()
     */
    public final boolean isNeedsImplementation()
    {
        boolean needsImplementation52a = this.__needsImplementation52a;
        if (!this.__needsImplementation52aSet)
        {
            // needsImplementation has no pre constraints
            needsImplementation52a = handleIsNeedsImplementation();
            // needsImplementation has no post constraints
            this.__needsImplementation52a = needsImplementation52a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__needsImplementation52aSet = true;
            }
        }
        return needsImplementation52a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFilterFullPath()
    * @return String
    */
    protected abstract String handleGetSearchFilterFullPath();

    private String __searchFilterFullPath53a;
    private boolean __searchFilterFullPath53aSet = false;

    /**
     * Full path of this manageable search filter.
     * @return (String)handleGetSearchFilterFullPath()
     */
    public final String getSearchFilterFullPath()
    {
        String searchFilterFullPath53a = this.__searchFilterFullPath53a;
        if (!this.__searchFilterFullPath53aSet)
        {
            // searchFilterFullPath has no pre constraints
            searchFilterFullPath53a = handleGetSearchFilterFullPath();
            // searchFilterFullPath has no post constraints
            this.__searchFilterFullPath53a = searchFilterFullPath53a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFilterFullPath53aSet = true;
            }
        }
        return searchFilterFullPath53a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFilterName()
    * @return String
    */
    protected abstract String handleGetSearchFilterName();

    private String __searchFilterName54a;
    private boolean __searchFilterName54aSet = false;

    /**
     * Search filter class name.
     * @return (String)handleGetSearchFilterName()
     */
    public final String getSearchFilterName()
    {
        String searchFilterName54a = this.__searchFilterName54a;
        if (!this.__searchFilterName54aSet)
        {
            // searchFilterName has no pre constraints
            searchFilterName54a = handleGetSearchFilterName();
            // searchFilterName has no post constraints
            this.__searchFilterName54a = searchFilterName54a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFilterName54aSet = true;
            }
        }
        return searchFilterName54a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getSearchFilterSerialVersionUID()
    * @return String
    */
    protected abstract String handleGetSearchFilterSerialVersionUID();

    private String __searchFilterSerialVersionUID55a;
    private boolean __searchFilterSerialVersionUID55aSet = false;

    /**
     * The calculated serial version UID for this controller.
     * @return (String)handleGetSearchFilterSerialVersionUID()
     */
    public final String getSearchFilterSerialVersionUID()
    {
        String searchFilterSerialVersionUID55a = this.__searchFilterSerialVersionUID55a;
        if (!this.__searchFilterSerialVersionUID55aSet)
        {
            // searchFilterSerialVersionUID has no pre constraints
            searchFilterSerialVersionUID55a = handleGetSearchFilterSerialVersionUID();
            // searchFilterSerialVersionUID has no post constraints
            this.__searchFilterSerialVersionUID55a = searchFilterSerialVersionUID55a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__searchFilterSerialVersionUID55aSet = true;
            }
        }
        return searchFilterSerialVersionUID55a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity#getManageableEditAttributes()
    * @return Collection
    */
    protected abstract Collection handleGetManageableEditAttributes();

    private Collection __manageableEditAttributes56a;
    private boolean __manageableEditAttributes56aSet = false;

    /**
     * returns all editable attributes
     * @return (Collection)handleGetManageableEditAttributes()
     */
    public final Collection getManageableEditAttributes()
    {
        Collection manageableEditAttributes56a = this.__manageableEditAttributes56a;
        if (!this.__manageableEditAttributes56aSet)
        {
            // manageableEditAttributes has no pre constraints
            manageableEditAttributes56a = handleGetManageableEditAttributes();
            // manageableEditAttributes has no post constraints
            this.__manageableEditAttributes56a = manageableEditAttributes56a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__manageableEditAttributes56aSet = true;
            }
        }
        return manageableEditAttributes56a;
    }

    // ---------------- business methods ----------------------

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.isSearchable
     * @param element
     * @return boolean
     */
    protected abstract boolean handleIsSearchable(Object element);

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.isSearchable
     * @param element Object
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.isSearchable(element)
     * @return handleIsSearchable(element)
     */
    public boolean isSearchable(Object element)
    {
        // isSearchable has no pre constraints
        boolean returnValue = handleIsSearchable(element);
        // isSearchable has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.getActionRoles
     * @return String
     */
    protected abstract String handleGetActionRoles();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.getActionRoles
     * @return handleGetActionRoles()
     */
    public String getActionRoles()
    {
        // getActionRoles has no pre constraints
        String returnValue = handleGetActionRoles();
        // getActionRoles has no post constraints
        return returnValue;
    }

    // ------------- associations ------------------

    /**
     * TODO: Model Documentation for org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity
     * @return (Collection<Role>)handleGetRoles()
     */
    public final Collection<Role> getRoles()
    {
        Collection<Role> getRoles1r = null;
        // jSFManageableEntity has no pre constraints
        Collection result = handleGetRoles();
        List shieldedResult = this.shieldedElements(result);
        try
        {
            getRoles1r = (Collection<Role>)shieldedResult;
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            JSFManageableEntityLogic.logger.warn("incorrect metafacade cast for JSFManageableEntityLogic.getRoles Collection<Role> " + result + ": " + shieldedResult);
        }
        // jSFManageableEntity has no post constraints
        return getRoles1r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  Collection
     */
    protected abstract Collection handleGetRoles();

    /**
     * @return true
     * @see ManageableEntity
     */
    public boolean isManageableEntityMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.Entity
     */
    public boolean isEntityMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see ClassifierFacade
     */
    public boolean isClassifierFacadeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see GeneralizableElementFacade
     */
    public boolean isGeneralizableElementFacadeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see ModelElementFacade
     */
    public boolean isModelElementFacadeMetaType()
    {
        return true;
    }

    // ----------- delegates to ManageableEntity ------------
    /**
     * Return the attribute which name matches the parameter
     * @see ClassifierFacade#findAttribute(String name)
     */
    public AttributeFacade findAttribute(String name)
    {
        return this.getSuperManageableEntity().findAttribute(name);
    }

    /**
     * Those abstraction dependencies for which this classifier is the client.
     * @see ClassifierFacade#getAbstractions()
     */
    public Collection<ClassifierFacade> getAbstractions()
    {
        return this.getSuperManageableEntity().getAbstractions();
    }

    /**
     * Lists all classes associated to this one and any ancestor classes (through generalization).
     * There will be no duplicates. The order of the elements is predictable.
     * @see ClassifierFacade#getAllAssociatedClasses()
     */
    public Collection<ClassifierFacade> getAllAssociatedClasses()
    {
        return this.getSuperManageableEntity().getAllAssociatedClasses();
    }

    /**
     * A collection containing all 'properties' of the classifier and its ancestors.  Properties are
     * any attributes and navigable connecting association ends.
     * @see ClassifierFacade#getAllProperties()
     */
    public Collection<ModelElementFacade> getAllProperties()
    {
        return this.getSuperManageableEntity().getAllProperties();
    }

    /**
     * A collection containing all required and/or read-only 'properties' of the classifier and its
     * ancestors. Properties are any attributes and navigable connecting association ends.
     * @see ClassifierFacade#getAllRequiredConstructorParameters()
     */
    public Collection<ModelElementFacade> getAllRequiredConstructorParameters()
    {
        return this.getSuperManageableEntity().getAllRequiredConstructorParameters();
    }

    /**
     * Gets the array type for this classifier.  If this classifier already represents an array, it
     * just returns itself.
     * @see ClassifierFacade#getArray()
     */
    public ClassifierFacade getArray()
    {
        return this.getSuperManageableEntity().getArray();
    }

    /**
     * The name of the classifier as an array.
     * @see ClassifierFacade#getArrayName()
     */
    public String getArrayName()
    {
        return this.getSuperManageableEntity().getArrayName();
    }

    /**
     * Lists the classes associated to this one, there is no repitition of classes. The order of the
     * elements is predictable.
     * @see ClassifierFacade#getAssociatedClasses()
     */
    public Collection<ClassifierFacade> getAssociatedClasses()
    {
        return this.getSuperManageableEntity().getAssociatedClasses();
    }

    /**
     * Gets the association ends belonging to a classifier.
     * @see ClassifierFacade#getAssociationEnds()
     */
    public List<AssociationEndFacade> getAssociationEnds()
    {
        return this.getSuperManageableEntity().getAssociationEnds();
    }

    /**
     * Gets the attributes that belong to the classifier.
     * @see ClassifierFacade#getAttributes()
     */
    public List<AttributeFacade> getAttributes()
    {
        return this.getSuperManageableEntity().getAttributes();
    }

    /**
     * Gets all attributes for the classifier and if 'follow' is true goes up the inheritance
     * hierarchy and gets the attributes from the super classes as well.
     * @see ClassifierFacade#getAttributes(boolean follow)
     */
    public List<AttributeFacade> getAttributes(boolean follow)
    {
        return this.getSuperManageableEntity().getAttributes(follow);
    }

    /**
     * The fully qualified name of the classifier as an array.
     * @see ClassifierFacade#getFullyQualifiedArrayName()
     */
    public String getFullyQualifiedArrayName()
    {
        return this.getSuperManageableEntity().getFullyQualifiedArrayName();
    }

    /**
     * Returns all those operations that could be implemented at this classifier's level. This means
     * the operations owned by this classifier as well as any realized interface's operations
     * (recursively) in case this classifier itself is not already an interface, or generalized when
     * this classifier is an interface.
     * @see ClassifierFacade#getImplementationOperations()
     */
    public Collection<OperationFacade> getImplementationOperations()
    {
        return this.getSuperManageableEntity().getImplementationOperations();
    }

    /**
     * A comma separated list of the fully qualified names of all implemented interfaces.
     * @see ClassifierFacade#getImplementedInterfaceList()
     */
    public String getImplementedInterfaceList()
    {
        return this.getSuperManageableEntity().getImplementedInterfaceList();
    }

    /**
     * Those attributes that are scoped to an instance of this class.
     * @see ClassifierFacade#getInstanceAttributes()
     */
    public Collection<AttributeFacade> getInstanceAttributes()
    {
        return this.getSuperManageableEntity().getInstanceAttributes();
    }

    /**
     * Those operations that are scoped to an instance of this class.
     * @see ClassifierFacade#getInstanceOperations()
     */
    public List<OperationFacade> getInstanceOperations()
    {
        return this.getSuperManageableEntity().getInstanceOperations();
    }

    /**
     * Those interfaces that are abstractions of this classifier, this basically means this
     * classifier realizes them.
     * @see ClassifierFacade#getInterfaceAbstractions()
     */
    public Collection<ClassifierFacade> getInterfaceAbstractions()
    {
        return this.getSuperManageableEntity().getInterfaceAbstractions();
    }

    /**
     * A String representing a new Constructor declaration for this classifier type to be used in a
     * Java environment.
     * @see ClassifierFacade#getJavaNewString()
     */
    public String getJavaNewString()
    {
        return this.getSuperManageableEntity().getJavaNewString();
    }

    /**
     * A String representing the null-value for this classifier type to be used in a Java
     * environment.
     * @see ClassifierFacade#getJavaNullString()
     */
    public String getJavaNullString()
    {
        return this.getSuperManageableEntity().getJavaNullString();
    }

    /**
     * The other ends of this classifier's association ends which are navigable.
     * @see ClassifierFacade#getNavigableConnectingEnds()
     */
    public Collection<AssociationEndFacade> getNavigableConnectingEnds()
    {
        return this.getSuperManageableEntity().getNavigableConnectingEnds();
    }

    /**
     * Get the other ends of this classifier's association ends which are navigable and if 'follow'
     * is true goes up the inheritance hierarchy and gets the super association ends as well.
     * @see ClassifierFacade#getNavigableConnectingEnds(boolean follow)
     */
    public List<AssociationEndFacade> getNavigableConnectingEnds(boolean follow)
    {
        return this.getSuperManageableEntity().getNavigableConnectingEnds(follow);
    }

    /**
     * Assuming that the classifier is an array, this will return the non array type of the
     * classifier from
     * the model.  If the classifier is NOT an array, it will just return itself.
     * @see ClassifierFacade#getNonArray()
     */
    public ClassifierFacade getNonArray()
    {
        return this.getSuperManageableEntity().getNonArray();
    }

    /**
     * The attributes from this classifier in the form of an operation call (this example would be
     * in Java): '(String attributeOne, String attributeTwo).  If there were no attributes on the
     * classifier, the result would be an empty '()'.
     * @see ClassifierFacade#getOperationCallFromAttributes()
     */
    public String getOperationCallFromAttributes()
    {
        return this.getSuperManageableEntity().getOperationCallFromAttributes();
    }

    /**
     * The operations owned by this classifier.
     * @see ClassifierFacade#getOperations()
     */
    public List<OperationFacade> getOperations()
    {
        return this.getSuperManageableEntity().getOperations();
    }

    /**
     * A collection containing all 'properties' of the classifier.  Properties are any attributes
     * and navigable connecting association ends.
     * @see ClassifierFacade#getProperties()
     */
    public List<ModelElementFacade> getProperties()
    {
        return this.getSuperManageableEntity().getProperties();
    }

    /**
     * Gets all properties (attributes and navigable association ends) for the classifier and if
     * 'follow' is true goes up the inheritance hierarchy and gets the properties from the super
     * classes as well.
     * @see ClassifierFacade#getProperties(boolean follow)
     */
    public List getProperties(boolean follow)
    {
        return this.getSuperManageableEntity().getProperties(follow);
    }

    /**
     * A collection containing all required and/or read-only 'properties' of the classifier. 
     * Properties are any attributes and navigable connecting association ends.
     * @see ClassifierFacade#getRequiredConstructorParameters()
     */
    public Collection<ModelElementFacade> getRequiredConstructorParameters()
    {
        return this.getSuperManageableEntity().getRequiredConstructorParameters();
    }

    /**
     * Returns the serial version UID of the underlying model element.
     * @see ClassifierFacade#getSerialVersionUID()
     */
    public long getSerialVersionUID()
    {
        return this.getSuperManageableEntity().getSerialVersionUID();
    }

    /**
     * Those attributes that are scoped to the definition of this class.
     * @see ClassifierFacade#getStaticAttributes()
     */
    public Collection<AttributeFacade> getStaticAttributes()
    {
        return this.getSuperManageableEntity().getStaticAttributes();
    }

    /**
     * Those operations that are scoped to the definition of this class.
     * @see ClassifierFacade#getStaticOperations()
     */
    public List<OperationFacade> getStaticOperations()
    {
        return this.getSuperManageableEntity().getStaticOperations();
    }

    /**
     * This class' superclass, returns the generalization if it is a ClassifierFacade, null
     * otherwise.
     * @see ClassifierFacade#getSuperClass()
     */
    public ClassifierFacade getSuperClass()
    {
        return this.getSuperManageableEntity().getSuperClass();
    }

    /**
     * The wrapper name for this classifier if a mapped type has a defined wrapper class (ie. 'long'
     * maps to 'Long').  If the classifier doesn't have a wrapper defined for it, this method will
     * return a null.  Note that wrapper mappings must be defined for the namespace by defining the
     * 'wrapperMappingsUri', this property must point to the location of the mappings file which
     * maps the primitives to wrapper types.
     * @see ClassifierFacade#getWrapperName()
     */
    public String getWrapperName()
    {
        return this.getSuperManageableEntity().getWrapperName();
    }

    /**
     * Indicates if this classifier is 'abstract'.
     * @see ClassifierFacade#isAbstract()
     */
    public boolean isAbstract()
    {
        return this.getSuperManageableEntity().isAbstract();
    }

    /**
     * True if this classifier represents an array type. False otherwise.
     * @see ClassifierFacade#isArrayType()
     */
    public boolean isArrayType()
    {
        return this.getSuperManageableEntity().isArrayType();
    }

    /**
     * True if the ClassifierFacade is an AssociationClass.
     * @see ClassifierFacade#isAssociationClass()
     */
    public boolean isAssociationClass()
    {
        return this.getSuperManageableEntity().isAssociationClass();
    }

    /**
     * Returns true if this type represents a Blob type.
     * @see ClassifierFacade#isBlobType()
     */
    public boolean isBlobType()
    {
        return this.getSuperManageableEntity().isBlobType();
    }

    /**
     * Indicates if this type represents a boolean type or not.
     * @see ClassifierFacade#isBooleanType()
     */
    public boolean isBooleanType()
    {
        return this.getSuperManageableEntity().isBooleanType();
    }

    /**
     * Indicates if this type represents a char, Character, or java.lang.Character type or not.
     * @see ClassifierFacade#isCharacterType()
     */
    public boolean isCharacterType()
    {
        return this.getSuperManageableEntity().isCharacterType();
    }

    /**
     * Returns true if this type represents a Clob type.
     * @see ClassifierFacade#isClobType()
     */
    public boolean isClobType()
    {
        return this.getSuperManageableEntity().isClobType();
    }

    /**
     * True if this classifier represents a collection type. False otherwise.
     * @see ClassifierFacade#isCollectionType()
     */
    public boolean isCollectionType()
    {
        return this.getSuperManageableEntity().isCollectionType();
    }

    /**
     * True/false depending on whether or not this classifier represents a datatype. A data type is
     * a type whose instances are identified only by their value. A data type may contain attributes
     * to support the modeling of structured data types.
     * @see ClassifierFacade#isDataType()
     */
    public boolean isDataType()
    {
        return this.getSuperManageableEntity().isDataType();
    }

    /**
     * True when this classifier is a date type.
     * @see ClassifierFacade#isDateType()
     */
    public boolean isDateType()
    {
        return this.getSuperManageableEntity().isDateType();
    }

    /**
     * Indicates if this type represents a Double type or not.
     * @see ClassifierFacade#isDoubleType()
     */
    public boolean isDoubleType()
    {
        return this.getSuperManageableEntity().isDoubleType();
    }

    /**
     * Indicates whether or not this classifier represents an "EmbeddedValue'.
     * @see ClassifierFacade#isEmbeddedValue()
     */
    public boolean isEmbeddedValue()
    {
        return this.getSuperManageableEntity().isEmbeddedValue();
    }

    /**
     * True if this classifier is in fact marked as an enumeration.
     * @see ClassifierFacade#isEnumeration()
     */
    public boolean isEnumeration()
    {
        return this.getSuperManageableEntity().isEnumeration();
    }

    /**
     * Returns true if this type represents a 'file' type.
     * @see ClassifierFacade#isFileType()
     */
    public boolean isFileType()
    {
        return this.getSuperManageableEntity().isFileType();
    }

    /**
     * Indicates if this type represents a Float type or not.
     * @see ClassifierFacade#isFloatType()
     */
    public boolean isFloatType()
    {
        return this.getSuperManageableEntity().isFloatType();
    }

    /**
     * Indicates if this type represents an int or Integer or java.lang.Integer type or not.
     * @see ClassifierFacade#isIntegerType()
     */
    public boolean isIntegerType()
    {
        return this.getSuperManageableEntity().isIntegerType();
    }

    /**
     * True/false depending on whether or not this Classifier represents an interface.
     * @see ClassifierFacade#isInterface()
     */
    public boolean isInterface()
    {
        return this.getSuperManageableEntity().isInterface();
    }

    /**
     * True if this classifier cannot be extended and represent a leaf in the inheritance tree.
     * @see ClassifierFacade#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.getSuperManageableEntity().isLeaf();
    }

    /**
     * True if this classifier represents a list type. False otherwise.
     * @see ClassifierFacade#isListType()
     */
    public boolean isListType()
    {
        return this.getSuperManageableEntity().isListType();
    }

    /**
     * Indicates if this type represents a Long type or not.
     * @see ClassifierFacade#isLongType()
     */
    public boolean isLongType()
    {
        return this.getSuperManageableEntity().isLongType();
    }

    /**
     * Indicates whether or not this classifier represents a Map type.
     * @see ClassifierFacade#isMapType()
     */
    public boolean isMapType()
    {
        return this.getSuperManageableEntity().isMapType();
    }

    /**
     * Indicates whether or not this classifier represents a primitive type.
     * @see ClassifierFacade#isPrimitive()
     */
    public boolean isPrimitive()
    {
        return this.getSuperManageableEntity().isPrimitive();
    }

    /**
     * True if this classifier represents a set type. False otherwise.
     * @see ClassifierFacade#isSetType()
     */
    public boolean isSetType()
    {
        return this.getSuperManageableEntity().isSetType();
    }

    /**
     * Indicates whether or not this classifier represents a string type.
     * @see ClassifierFacade#isStringType()
     */
    public boolean isStringType()
    {
        return this.getSuperManageableEntity().isStringType();
    }

    /**
     * Indicates whether or not this classifier represents a time type.
     * @see ClassifierFacade#isTimeType()
     */
    public boolean isTimeType()
    {
        return this.getSuperManageableEntity().isTimeType();
    }

    /**
     * Returns true if this type is a wrapped primitive type.
     * @see ClassifierFacade#isWrappedPrimitive()
     */
    public boolean isWrappedPrimitive()
    {
        return this.getSuperManageableEntity().isWrappedPrimitive();
    }

    /**
     * Returns a collection of all entities this entity and its ancestors have a relation to.
     * @see org.andromda.metafacades.uml.Entity#getAllEntityReferences()
     */
    public Collection<DependencyFacade> getAllEntityReferences()
    {
        return this.getSuperManageableEntity().getAllEntityReferences();
    }

    /**
     * Gets a comma separated list of attribute names.  If 'follow' is true, will travel up the
     * inheritance hiearchy to include attributes in parent entities as well.  If 'withIdentifiers'
     * is true, will include identifiers.
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean follow, boolean withIdentifiers)
     */
    public String getAttributeNameList(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getAttributeNameList(follow, withIdentifiers);
    }

    /**
     * Gets a comma separated list of attribute names.  If 'follow' is true, will travel up the
     * inheritance hiearchy to include attributes in parent entities as well.  If 'withIdentifiers'
     * is true, will include identifiers  and  if 'withDerived' is set to true, will include derived
     * attributes.
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean follow, boolean withIdentifiers, boolean withDerived)
     */
    public String getAttributeNameList(boolean follow, boolean withIdentifiers, boolean withDerived)
    {
        return this.getSuperManageableEntity().getAttributeNameList(follow, withIdentifiers, withDerived);
    }

    /**
     * Gets a comma separated list of attribute types.  If 'follow' is true, will travel up the
     * inheritance hierarchy to include attributes in parent entities as well.  If 'withIdentifiers'
     * is true, will include identifiers.
     * @see org.andromda.metafacades.uml.Entity#getAttributeTypeList(boolean follow, boolean withIdentifiers)
     */
    public String getAttributeTypeList(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getAttributeTypeList(follow, withIdentifiers);
    }

    /**
     * Gets all attributes of the entity, and optionally retieves the super entities attributes as
     * well as excludes the entity's identifiers if 'withIdentifiers' is set to false.
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean follow, boolean withIdentifiers)
     */
    public Collection<AttributeFacade> getAttributes(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getAttributes(follow, withIdentifiers);
    }

    /**
     * Gets all attributes of the entity, and optionally retieves the super entities attributes as
     * well as excludes the entity's identifiers if 'withIdentifiers' is set to false and exclude
     * derived attributes if 'withDerived' is set to false.
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean follow, boolean withIdentifiers, boolean withDerived)
     */
    public Collection<AttributeFacade> getAttributes(boolean follow, boolean withIdentifiers, boolean withDerived)
    {
        return this.getSuperManageableEntity().getAttributes(follow, withIdentifiers, withDerived);
    }

    /**
     * All business operations of the entity, these include any operations that aren't queries.
     * @see org.andromda.metafacades.uml.Entity#getBusinessOperations()
     */
    public Collection<OperationFacade> getBusinessOperations()
    {
        return this.getSuperManageableEntity().getBusinessOperations();
    }

    /**
     * Gets any children association ends (i.e. entity association ends that are participants in an
     * association with this entity and this entity has composite aggregation defined for those
     * associations).
     * @see org.andromda.metafacades.uml.Entity#getChildEnds()
     */
    public Collection<EntityAssociationEnd> getChildEnds()
    {
        return this.getSuperManageableEntity().getChildEnds();
    }

    /**
     * The embedded values belonging to this entity.
     * @see org.andromda.metafacades.uml.Entity#getEmbeddedValues()
     */
    public Collection<AttributeFacade> getEmbeddedValues()
    {
        return this.getSuperManageableEntity().getEmbeddedValues();
    }

    /**
     * All entities referenced by this entity.
     * @see org.andromda.metafacades.uml.Entity#getEntityReferences()
     */
    public Collection<DependencyFacade> getEntityReferences()
    {
        return this.getSuperManageableEntity().getEntityReferences();
    }

    /**
     * The full name of the type of the identifier. If composite identifier add the PK sufix to the
     * class name. If not, retorns the fully qualified name of the identifier.
     * @see org.andromda.metafacades.uml.Entity#getFullyQualifiedIdentifierTypeName()
     */
    public String getFullyQualifiedIdentifierTypeName()
    {
        return this.getSuperManageableEntity().getFullyQualifiedIdentifierTypeName();
    }

    /**
     * Gets all the associationEnds of this entity marked with the identifiers stereotype.
     * @see org.andromda.metafacades.uml.Entity#getIdentifierAssociationEnds()
     */
    public Collection<AssociationEndFacade> getIdentifierAssociationEnds()
    {
        return this.getSuperManageableEntity().getIdentifierAssociationEnds();
    }

    /**
     * The getter name of the identifier.
     * @see org.andromda.metafacades.uml.Entity#getIdentifierGetterName()
     */
    public String getIdentifierGetterName()
    {
        return this.getSuperManageableEntity().getIdentifierGetterName();
    }

    /**
     * The name of the identifier. If composite identifier add the Pk suffix. If not composite
     * returns the attribute name of the identifier.
     * @see org.andromda.metafacades.uml.Entity#getIdentifierName()
     */
    public String getIdentifierName()
    {
        return this.getSuperManageableEntity().getIdentifierName();
    }

    /**
     * The setter name of the identifier.
     * @see org.andromda.metafacades.uml.Entity#getIdentifierSetterName()
     */
    public String getIdentifierSetterName()
    {
        return this.getSuperManageableEntity().getIdentifierSetterName();
    }

    /**
     * The name of the type of the identifier. If composite identifier add the PK suffix to the
     * class name. If not, returns the name of the identifier.
     * @see org.andromda.metafacades.uml.Entity#getIdentifierTypeName()
     */
    public String getIdentifierTypeName()
    {
        return this.getSuperManageableEntity().getIdentifierTypeName();
    }

    /**
     * All the attributes of the entity which make up its identifier (primary key).  Will search any
     * super classes as well.  If no identifiers exist, a default identifier will be created if the
     * allowDefaultIdentifiers property is set to true.
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers()
     */
    public Collection<ModelElementFacade> getIdentifiers()
    {
        return this.getSuperManageableEntity().getIdentifiers();
    }

    /**
     * Gets all identifiers for an entity. If 'follow' is true, and if no identifiers can be found
     * on the entity, a search up the inheritance chain will be performed, and the identifiers from
     * the first super class having them will be used.   If no identifiers exist, a default
     * identifier will be created if the allowDefaultIdentifiers property is set to true.
     * Identifiers can be on attributes or associations (composite primary key).
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers(boolean follow)
     */
    public Collection<ModelElementFacade> getIdentifiers(boolean follow)
    {
        return this.getSuperManageableEntity().getIdentifiers(follow);
    }

    /**
     * The maximum length a SQL name may be.
     * @see org.andromda.metafacades.uml.Entity#getMaxSqlNameLength()
     */
    public short getMaxSqlNameLength()
    {
        return this.getSuperManageableEntity().getMaxSqlNameLength();
    }

    /**
     * Gets the attributes as a list within an operation call, optionally including the type names
     * and the identifier attributes.
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean withIdentifiers)
     */
    public String getOperationCallFromAttributes(boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getOperationCallFromAttributes(withIdentifiers);
    }

    /**
     * Gets the attributes as a list within an operation call.  If 'withTypeNames' is true, it will
     * include the type names, if 'withIdentifiers' is true it will include the identifiers.  If
     * 'follow' is true it will follow the inheritance hierarchy and get the attributes of the super
     * class as well.
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean withIdentifiers, boolean follow)
     */
    public String getOperationCallFromAttributes(boolean withIdentifiers, boolean follow)
    {
        return this.getSuperManageableEntity().getOperationCallFromAttributes(withIdentifiers, follow);
    }

    /**
     * Returns the parent association end of this entity if its a child entity.  The parent is the
     * entity that is the participant the association that has composite aggregation defined.  Will
     * return null if the entity has no parent.
     * @see org.andromda.metafacades.uml.Entity#getParentEnd()
     */
    public EntityAssociationEnd getParentEnd()
    {
        return this.getSuperManageableEntity().getParentEnd();
    }

    /**
     * Gets all properties of this entity, this includes the attributes and navigable association
     * ends of the entity.  The 'follow' flag indcates whether or not the inheritance hierarchy
     * should be followed when getting all the properties.  The 'withIdentifiers' flag indicates
     * whether or not identifiers should be included in the collection of properties.
     * @see org.andromda.metafacades.uml.Entity#getProperties(boolean follow, boolean withIdentifiers)
     */
    public Collection<ModelElementFacade> getProperties(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getProperties(follow, withIdentifiers);
    }

    /**
     * Returns all the operations that can perform queries on the entity.
     * @see org.andromda.metafacades.uml.Entity#getQueryOperations()
     */
    public Collection<EntityQueryOperation> getQueryOperations()
    {
        return this.getSuperManageableEntity().getQueryOperations();
    }

    /**
     * Gets all query operations for an entity. If 'follow' is true, and if no query operations can
     * be found on the entity, a search up the inheritance chain will be performed, and the
     * identifiers from the first super class having them will be used.   If no identifiers exist, a
     * default identifier will be created if the allowDefaultIdentifiers property is set to true.
     * @see org.andromda.metafacades.uml.Entity#getQueryOperations(boolean follow)
     */
    public Collection<OperationFacade> getQueryOperations(boolean follow)
    {
        return this.getSuperManageableEntity().getQueryOperations(follow);
    }

    /**
     * Gets a comma separated list of required attribute names.  If 'follow' is true, will travel up
     * the inheritance hierarchy to include attributes in parent entities as well.  If
     * 'withIdentifiers' is true, will include identifiers.
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeNameList(boolean follow, boolean withIdentifiers)
     */
    public String getRequiredAttributeNameList(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getRequiredAttributeNameList(follow, withIdentifiers);
    }

    /**
     * Gets a comma separated list of attribute types with are required.  If 'follow' is true, will
     * travel up the inheritance hierarchy to include attributes in parent entities as well.  If
     * 'withIdentifiers' is true, will include identifiers.
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeTypeList(boolean follow, boolean withIdentifiers)
     */
    public String getRequiredAttributeTypeList(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getRequiredAttributeTypeList(follow, withIdentifiers);
    }

    /**
     * Returns all attributes that are specified as 'required' in the model.  If 'follow' is true,
     * then required attributes in super classes will also be returned, if false, just the ones
     * directly on the entity will be returned.  If 'withIdentifiers' is true, the identifiers will
     * be include, if false, no identifiers will be included.
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributes(boolean follow, boolean withIdentifiers)
     */
    public Collection<AttributeFacade> getRequiredAttributes(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getRequiredAttributes(follow, withIdentifiers);
    }

    /**
     * Gets all required properties for this entity.  These consist of any required attributes as
     * well as navigable associations that are marked as 'required'.  If 'follow' is true, then the
     * inheritance hierchy will be followed and all required properties from super classes will be
     * included as well.
     * If 'withIdentifiers' is true, the identifiers will be include, if false, no identifiers will
     * be included.
     * @see org.andromda.metafacades.uml.Entity#getRequiredProperties(boolean follow, boolean withIdentifiers)
     */
    public Collection<ModelElementFacade> getRequiredProperties(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getRequiredProperties(follow, withIdentifiers);
    }

    /**
     * Creates a comma separated list of the required property names.
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyNameList(boolean follow, boolean withIdentifiers)
     */
    public String getRequiredPropertyNameList(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getRequiredPropertyNameList(follow, withIdentifiers);
    }

    /**
     * A comma separated list of the required property types.
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyTypeList(boolean follow, boolean withIdentifiers)
     */
    public String getRequiredPropertyTypeList(boolean follow, boolean withIdentifiers)
    {
        return this.getSuperManageableEntity().getRequiredPropertyTypeList(follow, withIdentifiers);
    }

    /**
     * The name of the schema that contains the database table
     * @see org.andromda.metafacades.uml.Entity#getSchema()
     */
    public String getSchema()
    {
        return this.getSuperManageableEntity().getSchema();
    }

    /**
     * The name of the database table to which this entity is persisted.
     * @see org.andromda.metafacades.uml.Entity#getTableName()
     */
    public String getTableName()
    {
        return this.getSuperManageableEntity().getTableName();
    }

    /**
     * Returns true/false depending on whether or not this entity represetns a child in an
     * association (this occurs when this entity is on the opposite end of an assocation end defined
     * as composite).
     * @see org.andromda.metafacades.uml.Entity#isChild()
     */
    public boolean isChild()
    {
        return this.getSuperManageableEntity().isChild();
    }

    /**
     * True if this entity identifier is a composite (consists of multiple key columns, typically
     * abstracted into an external composite identifier class)
     * @see org.andromda.metafacades.uml.Entity#isCompositeIdentifier()
     */
    public boolean isCompositeIdentifier()
    {
        return this.getSuperManageableEntity().isCompositeIdentifier();
    }

    /**
     * True if the entity has its identifiers dynamically added, false otherwise.
     * @see org.andromda.metafacades.uml.Entity#isDynamicIdentifiersPresent()
     */
    public boolean isDynamicIdentifiersPresent()
    {
        return this.getSuperManageableEntity().isDynamicIdentifiersPresent();
    }

    /**
     * True if the entity has any identifiers defined, false otherwise.
     * @see org.andromda.metafacades.uml.Entity#isIdentifiersPresent()
     */
    public boolean isIdentifiersPresent()
    {
        return this.getSuperManageableEntity().isIdentifiersPresent();
    }

    /**
     * Indiciates if this entity is using an assigned identifier or not.
     * @see org.andromda.metafacades.uml.Entity#isUsingAssignedIdentifier()
     */
    public boolean isUsingAssignedIdentifier()
    {
        return this.getSuperManageableEntity().isUsingAssignedIdentifier();
    }

    /**
     * Indicates whether or not this entity is using a foreign identifier as its identifiers.  That
     * is: the foreignIdentifier flag was set on an incoming association end and the entity is
     * therefore using the related foreign parent entity's identifier.
     * @see org.andromda.metafacades.uml.Entity#isUsingForeignIdentifier()
     */
    public boolean isUsingForeignIdentifier()
    {
        return this.getSuperManageableEntity().isUsingForeignIdentifier();
    }

    /**
     * Finds the tagged value optional searching the entire inheritance hierarchy if 'follow' is set
     * to true.
     * @see GeneralizableElementFacade#findTaggedValue(String tagName, boolean follow)
     */
    public Object findTaggedValue(String tagName, boolean follow)
    {
        return this.getSuperManageableEntity().findTaggedValue(tagName, follow);
    }

    /**
     * All generalizations for this generalizable element, goes up the inheritance tree.
     * @see GeneralizableElementFacade#getAllGeneralizations()
     */
    public Collection<GeneralizableElementFacade> getAllGeneralizations()
    {
        return this.getSuperManageableEntity().getAllGeneralizations();
    }

    /**
     * All specializations (travels down the inheritance hierarchy).
     * @see GeneralizableElementFacade#getAllSpecializations()
     */
    public Collection<GeneralizableElementFacade> getAllSpecializations()
    {
        return this.getSuperManageableEntity().getAllSpecializations();
    }

    /**
     * Gets the direct generalization for this generalizable element.
     * @see GeneralizableElementFacade#getGeneralization()
     */
    public GeneralizableElementFacade getGeneralization()
    {
        return this.getSuperManageableEntity().getGeneralization();
    }

    /**
     * Gets the actual links that this generalization element is part of (it plays either the
     * specialization or generalization).
     * @see GeneralizableElementFacade#getGeneralizationLinks()
     */
    public Collection<GeneralizationFacade> getGeneralizationLinks()
    {
        return this.getSuperManageableEntity().getGeneralizationLinks();
    }

    /**
     * A comma separated list of the fully qualified names of all generalizations.
     * @see GeneralizableElementFacade#getGeneralizationList()
     */
    public String getGeneralizationList()
    {
        return this.getSuperManageableEntity().getGeneralizationList();
    }

    /**
     * The element found when you recursively follow the generalization path up to the root. If an
     * element has no generalization itself will be considered the root.
     * @see GeneralizableElementFacade#getGeneralizationRoot()
     */
    public GeneralizableElementFacade getGeneralizationRoot()
    {
        return this.getSuperManageableEntity().getGeneralizationRoot();
    }

    /**
     * Return all generalizations (ancestors) from this generalizable element.
     * @see GeneralizableElementFacade#getGeneralizations()
     */
    public Collection<GeneralizableElementFacade> getGeneralizations()
    {
        return this.getSuperManageableEntity().getGeneralizations();
    }

    /**
     * Gets the direct specializations (i.e. sub elements) for this generalizatble element.
     * @see GeneralizableElementFacade#getSpecializations()
     */
    public Collection<GeneralizableElementFacade> getSpecializations()
    {
        return this.getSuperManageableEntity().getSpecializations();
    }

    /**
     * All entities to which can be browsed from this entity. Currently this property will simple
     * hold all entities, so the value is the same for any arbitrary entity. Hiding entities can be
     * done in the presentation tier, for example depending on runtime security information.
     * @see ManageableEntity#getAllManageables()
     */
    public List<ManageableEntity> getAllManageables()
    {
        return this.getSuperManageableEntity().getAllManageables();
    }

    /**
     * The attribute used as a key link to display values for this entity.
     * @see ManageableEntity#getDisplayAttribute()
     */
    public ManageableEntityAttribute getDisplayAttribute()
    {
        return this.getSuperManageableEntity().getDisplayAttribute();
    }

    /**
     * The fully qualified service name of the entity.
     * @see ManageableEntity#getFullyQualifiedManageableServiceName()
     */
    public String getFullyQualifiedManageableServiceName()
    {
        return this.getSuperManageableEntity().getFullyQualifiedManageableServiceName();
    }

    /**
     * The associations to other entities from this entity.
     * @see ManageableEntity#getManageableAssociationEnds()
     */
    public List<ManageableEntityAssociationEnd> getManageableAssociationEnds()
    {
        return this.getSuperManageableEntity().getManageableAssociationEnds();
    }

    /**
     * Lists the attributes that can be managed for this entity. This feature is particularly
     * important when resolving inherited attributes and ids.
     * @see ManageableEntity#getManageableAttributes()
     */
    public List<ManageableEntityAttribute> getManageableAttributes()
    {
        return this.getSuperManageableEntity().getManageableAttributes();
    }

    /**
     * The identifier used when managing this entity.
     * @see ManageableEntity#getManageableIdentifier()
     */
    public ManageableEntityAttribute getManageableIdentifier()
    {
        return this.getSuperManageableEntity().getManageableIdentifier();
    }

    /**
     * ManageableAttributes and ManageableAssociationEnds
     * @see ManageableEntity#getManageableMembers()
     */
    public List<ModelElementFacade> getManageableMembers()
    {
        return this.getSuperManageableEntity().getManageableMembers();
    }

    /**
     * The entity package name.
     * @see ManageableEntity#getManageablePackageName()
     */
    public String getManageablePackageName()
    {
        return this.getSuperManageableEntity().getManageablePackageName();
    }

    /**
     * The Package path of the Entity
     * @see ManageableEntity#getManageablePackagePath()
     */
    public String getManageablePackagePath()
    {
        return this.getSuperManageableEntity().getManageablePackagePath();
    }

    /**
     * The entity accessor (getter) call.
     * @see ManageableEntity#getManageableServiceAccessorCall()
     */
    public String getManageableServiceAccessorCall()
    {
        return this.getSuperManageableEntity().getManageableServiceAccessorCall();
    }

    /**
     * The service full path of the entity.
     * @see ManageableEntity#getManageableServiceFullPath()
     */
    public String getManageableServiceFullPath()
    {
        return this.getSuperManageableEntity().getManageableServiceFullPath();
    }

    /**
     * The service name of the entity.
     * @see ManageableEntity#getManageableServiceName()
     */
    public String getManageableServiceName()
    {
        return this.getSuperManageableEntity().getManageableServiceName();
    }

    /**
     * The maximum number of rows to load from the database.
     * @see ManageableEntity#getMaximumListSize()
     */
    public int getMaximumListSize()
    {
        return this.getSuperManageableEntity().getMaximumListSize();
    }

    /**
     * The maximum number of rows to load from the database.
     * @see ManageableEntity#getPageSize()
     */
    public int getPageSize()
    {
        return this.getSuperManageableEntity().getPageSize();
    }

    /**
     * Other Manageable Entities which reference this entity.
     * @see ManageableEntity#getReferencingManageables()
     */
    public List<ManageableEntity> getReferencingManageables()
    {
        return this.getSuperManageableEntity().getReferencingManageables();
    }

    /**
     * The Actors (Roles) which can manage the Entity.
     * @see ManageableEntity#getUsers()
     */
    public List<ActorFacade> getUsers()
    {
        return this.getSuperManageableEntity().getUsers();
    }

    /**
     * Create a create operation on the entity manager?
     * @see ManageableEntity#isCreate()
     */
    public boolean isCreate()
    {
        return this.getSuperManageableEntity().isCreate();
    }

    /**
     * Create a delete operation on the entity manager?
     * @see ManageableEntity#isDelete()
     */
    public boolean isDelete()
    {
        return this.getSuperManageableEntity().isDelete();
    }

    /**
     * True: Entity is manageable.
     * @see ManageableEntity#isManageable()
     */
    public boolean isManageable()
    {
        return this.getSuperManageableEntity().isManageable();
    }

    /**
     * Create a read operation on the entity manager?
     * @see ManageableEntity#isRead()
     */
    public boolean isRead()
    {
        return this.getSuperManageableEntity().isRead();
    }

    /**
     * The maximum number of rows to load from the database.
     * @see ManageableEntity#isResolveable()
     */
    public boolean isResolveable()
    {
        return this.getSuperManageableEntity().isResolveable();
    }

    /**
     * Create an update operation on the entity manager?
     * @see ManageableEntity#isUpdate()
     */
    public boolean isUpdate()
    {
        return this.getSuperManageableEntity().isUpdate();
    }

    /**
     * Returns a string with the attributes without wrapper types.
     * @see ManageableEntity#listManageableMembers(boolean withTypes)
     */
    public String listManageableMembers(boolean withTypes)
    {
        return this.getSuperManageableEntity().listManageableMembers(withTypes);
    }

    /**
     * Returns a string with the attributes and wrapper types.
     * @see ManageableEntity#listManageableMembersWithWrapperTypes()
     */
    public String listManageableMembersWithWrapperTypes()
    {
        return this.getSuperManageableEntity().listManageableMembersWithWrapperTypes();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperManageableEntity().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperManageableEntity().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperManageableEntity().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperManageableEntity().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperManageableEntity().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperManageableEntity().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperManageableEntity().getDocumentation(indent);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. By default paragraphs are returned as HTML.
     * This method is equivalent to <code>getDocumentation(indent, lineLength, true)</code>.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength)
     */
    public String getDocumentation(String indent, int lineLength)
    {
        return this.getSuperManageableEntity().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperManageableEntity().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperManageableEntity().getFullyQualifiedName();
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element.  If modelName is true, then
     * the original name of the model element (the name contained within the model) will be the name
     * returned, otherwise a name from a language mapping will be returned.
     * @see ModelElementFacade#getFullyQualifiedName(boolean modelName)
     */
    public String getFullyQualifiedName(boolean modelName)
    {
        return this.getSuperManageableEntity().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperManageableEntity().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperManageableEntity().getId();
    }

    /**
     * UML2: Retrieves the keywords for this element. Used to modify implementation properties which
     * are not represented by other properties, i.e. native, transient, volatile, synchronized,
     * (added annotations) override, deprecated. Can also be used to suppress compiler warnings:
     * (added annotations) unchecked, fallthrough, path, serial, finally, all. Annotations require
     * JDK5 compiler level.
     * @see ModelElementFacade#getKeywords()
     */
    public Collection<String> getKeywords()
    {
        return this.getSuperManageableEntity().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperManageableEntity().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperManageableEntity().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperManageableEntity().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperManageableEntity().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperManageableEntity().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperManageableEntity().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperManageableEntity().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperManageableEntity().getPackagePath();
    }

    /**
     * UML2: Returns the value of the 'Qualified Name' attribute. A name which allows the
     * NamedElement to be identified within a hierarchy of nested Namespaces. It is constructed from
     * the names of the containing namespaces starting at the root of the hierarchy and ending with
     * the name of the NamedElement itself.
     * @see ModelElementFacade#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return this.getSuperManageableEntity().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperManageableEntity().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperManageableEntity().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperManageableEntity().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperManageableEntity().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperManageableEntity().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperManageableEntity().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperManageableEntity().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperManageableEntity().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperManageableEntity().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperManageableEntity().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperManageableEntity().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperManageableEntity().hasKeyword(keywordName);
    }

    /**
     * Returns true if the model element has the specified stereotype.  If the stereotype itself
     * does not match, then a search will be made up the stereotype inheritance hierarchy, and if
     * one of the stereotype's ancestors has a matching name this method will return true, false
     * otherwise.
     * For example, if we have a certain stereotype called <<exception>> and a model element has a
     * stereotype called <<applicationException>> which extends <<exception>>, when calling this
     * method with 'stereotypeName' defined as 'exception' the method would return true since
     * <<applicationException>> inherits from <<exception>>.  If you want to check if the model
     * element has the exact stereotype, then use the method 'hasExactStereotype' instead.
     * @see ModelElementFacade#hasStereotype(String stereotypeName)
     */
    public boolean hasStereotype(String stereotypeName)
    {
        return this.getSuperManageableEntity().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperManageableEntity().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperManageableEntity().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperManageableEntity().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperManageableEntity().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperManageableEntity().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperManageableEntity().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperManageableEntity().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperManageableEntity().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperManageableEntity().translateConstraints(kind, translation);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperManageableEntity().initialize();
    }

    /**
     * @return Object getSuperManageableEntity().getValidationOwner()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperManageableEntity().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperManageableEntity().getValidationName()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperManageableEntity().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperManageableEntity().validateInvariants(validationMessages);
    }

    /**
     * The property that stores the name of the metafacade.
     */
    private static final String NAME_PROPERTY = "name";
    private static final String FQNAME_PROPERTY = "fullyQualifiedName";

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder toString = new StringBuilder(this.getClass().getName());
        toString.append("[");
        try
        {
            toString.append(Introspector.instance().getProperty(this, FQNAME_PROPERTY));
        }
        catch (final Throwable tryAgain)
        {
            try
            {
                toString.append(Introspector.instance().getProperty(this, NAME_PROPERTY));
            }
            catch (final Throwable ignore)
            {
                // - just ignore when the metafacade doesn't have a name or fullyQualifiedName property
            }
        }
        toString.append("]");
        return toString.toString();
    }
}