// license-header java merge-point
//
// Attention: generated code (by MetafacadeLogic.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.metafacades.uml.ActionFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.log4j.Logger;

/**
 * Represents an action taken during a "front-end" event execution on a JSF application.
 * MetafacadeLogic for JSFAction
 *
 * @see JSFAction
 */
public abstract class JSFActionLogic
    extends MetafacadeBase
    implements JSFAction
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
    protected JSFActionLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superFrontEndAction =
           (FrontEndAction)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.FrontEndAction",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFActionLogic.class);

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFAction if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFAction";
        }
        return context;
    }

    private FrontEndAction superFrontEndAction;
    private boolean superFrontEndActionInitialized = false;

    /**
     * Gets the FrontEndAction parent instance.
     * @return this.superFrontEndAction FrontEndAction
     */
    private FrontEndAction getSuperFrontEndAction()
    {
        if (!this.superFrontEndActionInitialized)
        {
            ((MetafacadeBase)this.superFrontEndAction).setMetafacadeContext(this.getMetafacadeContext());
            this.superFrontEndActionInitialized = true;
        }
        return this.superFrontEndAction;
    }

    /** Reset context only for non-root metafacades
     * @param context
     * @see MetafacadeBase#resetMetafacadeContext(String context)
     */
    @Override
    public void resetMetafacadeContext(String context)
    {
        if (!this.contextRoot) // reset context only for non-root metafacades
        {
            context = getContext(context);  // to have same value as in original constructor call
            setMetafacadeContext (context);
            if (this.superFrontEndActionInitialized)
            {
                ((MetafacadeBase)this.superFrontEndAction).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFAction
     */
    public boolean isJSFActionMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormImplementationName()
    * @return String
    */
    protected abstract String handleGetFormImplementationName();

    private String __formImplementationName1a;
    private boolean __formImplementationName1aSet = false;

    /**
     * The name of the form implementation.
     * @return (String)handleGetFormImplementationName()
     */
    public final String getFormImplementationName()
    {
        String formImplementationName1a = this.__formImplementationName1a;
        if (!this.__formImplementationName1aSet)
        {
            // formImplementationName has no pre constraints
            formImplementationName1a = handleGetFormImplementationName();
            // formImplementationName has no post constraints
            this.__formImplementationName1a = formImplementationName1a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formImplementationName1aSet = true;
            }
        }
        return formImplementationName1a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormBeanName()
    * @return String
    */
    protected abstract String handleGetFormBeanName();

    private String __formBeanName2a;
    private boolean __formBeanName2aSet = false;

    /**
     * The name of the bean under which the form is stored.
     * @return (String)handleGetFormBeanName()
     */
    public final String getFormBeanName()
    {
        String formBeanName2a = this.__formBeanName2a;
        if (!this.__formBeanName2aSet)
        {
            // formBeanName has no pre constraints
            formBeanName2a = handleGetFormBeanName();
            // formBeanName has no post constraints
            this.__formBeanName2a = formBeanName2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formBeanName2aSet = true;
            }
        }
        return formBeanName2a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFullyQualifiedFormImplementationName()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedFormImplementationName();

    private String __fullyQualifiedFormImplementationName3a;
    private boolean __fullyQualifiedFormImplementationName3aSet = false;

    /**
     * The fully qualified name of the form implementation.
     * @return (String)handleGetFullyQualifiedFormImplementationName()
     */
    public final String getFullyQualifiedFormImplementationName()
    {
        String fullyQualifiedFormImplementationName3a = this.__fullyQualifiedFormImplementationName3a;
        if (!this.__fullyQualifiedFormImplementationName3aSet)
        {
            // fullyQualifiedFormImplementationName has no pre constraints
            fullyQualifiedFormImplementationName3a = handleGetFullyQualifiedFormImplementationName();
            // fullyQualifiedFormImplementationName has no post constraints
            this.__fullyQualifiedFormImplementationName3a = fullyQualifiedFormImplementationName3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedFormImplementationName3aSet = true;
            }
        }
        return fullyQualifiedFormImplementationName3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFullyQualifiedFormImplementationPath()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedFormImplementationPath();

    private String __fullyQualifiedFormImplementationPath4a;
    private boolean __fullyQualifiedFormImplementationPath4aSet = false;

    /**
     * The fully qualified path of the form implementation.
     * @return (String)handleGetFullyQualifiedFormImplementationPath()
     */
    public final String getFullyQualifiedFormImplementationPath()
    {
        String fullyQualifiedFormImplementationPath4a = this.__fullyQualifiedFormImplementationPath4a;
        if (!this.__fullyQualifiedFormImplementationPath4aSet)
        {
            // fullyQualifiedFormImplementationPath has no pre constraints
            fullyQualifiedFormImplementationPath4a = handleGetFullyQualifiedFormImplementationPath();
            // fullyQualifiedFormImplementationPath has no post constraints
            this.__fullyQualifiedFormImplementationPath4a = fullyQualifiedFormImplementationPath4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedFormImplementationPath4aSet = true;
            }
        }
        return fullyQualifiedFormImplementationPath4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormScope()
    * @return String
    */
    protected abstract String handleGetFormScope();

    private String __formScope5a;
    private boolean __formScope5aSet = false;

    /**
     * The scope of the JSF form (request, session,application,etc).
     * @return (String)handleGetFormScope()
     */
    public final String getFormScope()
    {
        String formScope5a = this.__formScope5a;
        if (!this.__formScope5aSet)
        {
            // formScope has no pre constraints
            formScope5a = handleGetFormScope();
            // formScope has no post constraints
            this.__formScope5a = formScope5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formScope5aSet = true;
            }
        }
        return formScope5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormImplementationInterfaceList()
    * @return String
    */
    protected abstract String handleGetFormImplementationInterfaceList();

    private String __formImplementationInterfaceList6a;
    private boolean __formImplementationInterfaceList6aSet = false;

    /**
     * A comma separated list of all the form interfaces which the form implementation implements.
     * @return (String)handleGetFormImplementationInterfaceList()
     */
    public final String getFormImplementationInterfaceList()
    {
        String formImplementationInterfaceList6a = this.__formImplementationInterfaceList6a;
        if (!this.__formImplementationInterfaceList6aSet)
        {
            // formImplementationInterfaceList has no pre constraints
            formImplementationInterfaceList6a = handleGetFormImplementationInterfaceList();
            // formImplementationInterfaceList has no post constraints
            this.__formImplementationInterfaceList6a = formImplementationInterfaceList6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formImplementationInterfaceList6aSet = true;
            }
        }
        return formImplementationInterfaceList6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getPath()
    * @return String
    */
    protected abstract String handleGetPath();

    private String __path7a;
    private boolean __path7aSet = false;

    /**
     * The path to this action.
     * @return (String)handleGetPath()
     */
    public final String getPath()
    {
        String path7a = this.__path7a;
        if (!this.__path7aSet)
        {
            // path has no pre constraints
            path7a = handleGetPath();
            // path has no post constraints
            this.__path7a = path7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__path7aSet = true;
            }
        }
        return path7a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getPathRoot()
    * @return String
    */
    protected abstract String handleGetPathRoot();

    private String __pathRoot8a;
    private boolean __pathRoot8aSet = false;

    /**
     * The path's root.
     * @return (String)handleGetPathRoot()
     */
    public final String getPathRoot()
    {
        String pathRoot8a = this.__pathRoot8a;
        if (!this.__pathRoot8aSet)
        {
            // pathRoot has no pre constraints
            pathRoot8a = handleGetPathRoot();
            // pathRoot has no post constraints
            this.__pathRoot8a = pathRoot8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__pathRoot8aSet = true;
            }
        }
        return pathRoot8a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey9a;
    private boolean __messageKey9aSet = false;

    /**
     * The default resource message key for this action.
     * @return (String)handleGetMessageKey()
     */
    public final String getMessageKey()
    {
        String messageKey9a = this.__messageKey9a;
        if (!this.__messageKey9aSet)
        {
            // messageKey has no pre constraints
            messageKey9a = handleGetMessageKey();
            // messageKey has no post constraints
            this.__messageKey9a = messageKey9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageKey9aSet = true;
            }
        }
        return messageKey9a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getDocumentationKey()
    * @return String
    */
    protected abstract String handleGetDocumentationKey();

    private String __documentationKey10a;
    private boolean __documentationKey10aSet = false;

    /**
     * A resource message key suited for the action''s documentation.
     * @return (String)handleGetDocumentationKey()
     */
    public final String getDocumentationKey()
    {
        String documentationKey10a = this.__documentationKey10a;
        if (!this.__documentationKey10aSet)
        {
            // documentationKey has no pre constraints
            documentationKey10a = handleGetDocumentationKey();
            // documentationKey has no post constraints
            this.__documentationKey10a = documentationKey10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__documentationKey10aSet = true;
            }
        }
        return documentationKey10a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getDocumentationValue()
    * @return String
    */
    protected abstract String handleGetDocumentationValue();

    private String __documentationValue11a;
    private boolean __documentationValue11aSet = false;

    /**
     * The resource messsage value suited for the action''s documentation.
     * @return (String)handleGetDocumentationValue()
     */
    public final String getDocumentationValue()
    {
        String documentationValue11a = this.__documentationValue11a;
        if (!this.__documentationValue11aSet)
        {
            // documentationValue has no pre constraints
            documentationValue11a = handleGetDocumentationValue();
            // documentationValue has no post constraints
            this.__documentationValue11a = documentationValue11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__documentationValue11aSet = true;
            }
        }
        return documentationValue11a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getTriggerName()
    * @return String
    */
    protected abstract String handleGetTriggerName();

    private String __triggerName12a;
    private boolean __triggerName12aSet = false;

    /**
     * The name of the trigger that triggers that action.
     * @return (String)handleGetTriggerName()
     */
    public final String getTriggerName()
    {
        String triggerName12a = this.__triggerName12a;
        if (!this.__triggerName12aSet)
        {
            // triggerName has no pre constraints
            triggerName12a = handleGetTriggerName();
            // triggerName has no post constraints
            this.__triggerName12a = triggerName12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__triggerName12aSet = true;
            }
        }
        return triggerName12a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getViewFragmentPath()
    * @return String
    */
    protected abstract String handleGetViewFragmentPath();

    private String __viewFragmentPath13a;
    private boolean __viewFragmentPath13aSet = false;

    /**
     * The path to the view fragment corresponding to this action
     * @return (String)handleGetViewFragmentPath()
     */
    public final String getViewFragmentPath()
    {
        String viewFragmentPath13a = this.__viewFragmentPath13a;
        if (!this.__viewFragmentPath13aSet)
        {
            // viewFragmentPath has no pre constraints
            viewFragmentPath13a = handleGetViewFragmentPath();
            // viewFragmentPath has no post constraints
            this.__viewFragmentPath13a = viewFragmentPath13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__viewFragmentPath13aSet = true;
            }
        }
        return viewFragmentPath13a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getTableLinkName()
    * @return String
    */
    protected abstract String handleGetTableLinkName();

    private String __tableLinkName14a;
    private boolean __tableLinkName14aSet = false;

    /**
     * The name of the table link specified for this action.
     * @return (String)handleGetTableLinkName()
     */
    public final String getTableLinkName()
    {
        String tableLinkName14a = this.__tableLinkName14a;
        if (!this.__tableLinkName14aSet)
        {
            // tableLinkName has no pre constraints
            tableLinkName14a = handleGetTableLinkName();
            // tableLinkName has no post constraints
            this.__tableLinkName14a = tableLinkName14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableLinkName14aSet = true;
            }
        }
        return tableLinkName14a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getTableLinkColumnName()
    * @return String
    */
    protected abstract String handleGetTableLinkColumnName();

    private String __tableLinkColumnName15a;
    private boolean __tableLinkColumnName15aSet = false;

    /**
     * The name of the column targetted by this action.
     * @return (String)handleGetTableLinkColumnName()
     */
    public final String getTableLinkColumnName()
    {
        String tableLinkColumnName15a = this.__tableLinkColumnName15a;
        if (!this.__tableLinkColumnName15aSet)
        {
            // tableLinkColumnName has no pre constraints
            tableLinkColumnName15a = handleGetTableLinkColumnName();
            // tableLinkColumnName has no post constraints
            this.__tableLinkColumnName15a = tableLinkColumnName15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableLinkColumnName15aSet = true;
            }
        }
        return tableLinkColumnName15a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isTableLink()
    * @return boolean
    */
    protected abstract boolean handleIsTableLink();

    private boolean __tableLink16a;
    private boolean __tableLink16aSet = false;

    /**
     * Indicates if a table link name has been specified and it properly targets a table
     * page-variable from the input page.
     * @return (boolean)handleIsTableLink()
     */
    public final boolean isTableLink()
    {
        boolean tableLink16a = this.__tableLink16a;
        if (!this.__tableLink16aSet)
        {
            // tableLink has no pre constraints
            tableLink16a = handleIsTableLink();
            // tableLink has no post constraints
            this.__tableLink16a = tableLink16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableLink16aSet = true;
            }
        }
        return tableLink16a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isHyperlink()
    * @return boolean
    */
    protected abstract boolean handleIsHyperlink();

    private boolean __hyperlink17a;
    private boolean __hyperlink17aSet = false;

    /**
     * Indicates whether or not this action is represented by clicking on a hyperlink.
     * @return (boolean)handleIsHyperlink()
     */
    public final boolean isHyperlink()
    {
        boolean hyperlink17a = this.__hyperlink17a;
        if (!this.__hyperlink17aSet)
        {
            // hyperlink has no pre constraints
            hyperlink17a = handleIsHyperlink();
            // hyperlink has no post constraints
            this.__hyperlink17a = hyperlink17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__hyperlink17aSet = true;
            }
        }
        return hyperlink17a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getActionClassName()
    * @return String
    */
    protected abstract String handleGetActionClassName();

    private String __actionClassName18a;
    private boolean __actionClassName18aSet = false;

    /**
     * The name of the action class that executes this action.
     * @return (String)handleGetActionClassName()
     */
    public final String getActionClassName()
    {
        String actionClassName18a = this.__actionClassName18a;
        if (!this.__actionClassName18aSet)
        {
            // actionClassName has no pre constraints
            actionClassName18a = handleGetActionClassName();
            // actionClassName has no post constraints
            this.__actionClassName18a = actionClassName18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionClassName18aSet = true;
            }
        }
        return actionClassName18a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFullyQualifiedActionClassPath()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedActionClassPath();

    private String __fullyQualifiedActionClassPath19a;
    private boolean __fullyQualifiedActionClassPath19aSet = false;

    /**
     * The fully qualified path to the action class that execute this action.
     * @return (String)handleGetFullyQualifiedActionClassPath()
     */
    public final String getFullyQualifiedActionClassPath()
    {
        String fullyQualifiedActionClassPath19a = this.__fullyQualifiedActionClassPath19a;
        if (!this.__fullyQualifiedActionClassPath19aSet)
        {
            // fullyQualifiedActionClassPath has no pre constraints
            fullyQualifiedActionClassPath19a = handleGetFullyQualifiedActionClassPath();
            // fullyQualifiedActionClassPath has no post constraints
            this.__fullyQualifiedActionClassPath19a = fullyQualifiedActionClassPath19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedActionClassPath19aSet = true;
            }
        }
        return fullyQualifiedActionClassPath19a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getControllerAction()
    * @return String
    */
    protected abstract String handleGetControllerAction();

    private String __controllerAction20a;
    private boolean __controllerAction20aSet = false;

    /**
     * The name of the action on the controller that executions this action.
     * @return (String)handleGetControllerAction()
     */
    public final String getControllerAction()
    {
        String controllerAction20a = this.__controllerAction20a;
        if (!this.__controllerAction20aSet)
        {
            // controllerAction has no pre constraints
            controllerAction20a = handleGetControllerAction();
            // controllerAction has no post constraints
            this.__controllerAction20a = controllerAction20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__controllerAction20aSet = true;
            }
        }
        return controllerAction20a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFullyQualifiedActionClassName()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedActionClassName();

    private String __fullyQualifiedActionClassName21a;
    private boolean __fullyQualifiedActionClassName21aSet = false;

    /**
     * The fully qualified name of the action class that execute this action.
     * @return (String)handleGetFullyQualifiedActionClassName()
     */
    public final String getFullyQualifiedActionClassName()
    {
        String fullyQualifiedActionClassName21a = this.__fullyQualifiedActionClassName21a;
        if (!this.__fullyQualifiedActionClassName21aSet)
        {
            // fullyQualifiedActionClassName has no pre constraints
            fullyQualifiedActionClassName21a = handleGetFullyQualifiedActionClassName();
            // fullyQualifiedActionClassName has no post constraints
            this.__fullyQualifiedActionClassName21a = fullyQualifiedActionClassName21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedActionClassName21aSet = true;
            }
        }
        return fullyQualifiedActionClassName21a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isResettable()
    * @return boolean
    */
    protected abstract boolean handleIsResettable();

    private boolean __resettable22a;
    private boolean __resettable22aSet = false;

    /**
     * Indicates whether or not the values passed along with this action can be reset or not.
     * @return (boolean)handleIsResettable()
     */
    public final boolean isResettable()
    {
        boolean resettable22a = this.__resettable22a;
        if (!this.__resettable22aSet)
        {
            // resettable has no pre constraints
            resettable22a = handleIsResettable();
            // resettable has no post constraints
            this.__resettable22a = resettable22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__resettable22aSet = true;
            }
        }
        return resettable22a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormKey()
    * @return String
    */
    protected abstract String handleGetFormKey();

    private String __formKey23a;
    private boolean __formKey23aSet = false;

    /**
     * The key that stores the form in which information is passed from one action to another.
     * @return (String)handleGetFormKey()
     */
    public final String getFormKey()
    {
        String formKey23a = this.__formKey23a;
        if (!this.__formKey23aSet)
        {
            // formKey has no pre constraints
            formKey23a = handleGetFormKey();
            // formKey has no post constraints
            this.__formKey23a = formKey23a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formKey23aSet = true;
            }
        }
        return formKey23a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isTableAction()
    * @return boolean
    */
    protected abstract boolean handleIsTableAction();

    private boolean __tableAction24a;
    private boolean __tableAction24aSet = false;

    /**
     * Indicates that this action works on all rows of the table from the table link relation.
     * @return (boolean)handleIsTableAction()
     */
    public final boolean isTableAction()
    {
        boolean tableAction24a = this.__tableAction24a;
        if (!this.__tableAction24aSet)
        {
            // tableAction has no pre constraints
            tableAction24a = handleIsTableAction();
            // tableAction has no post constraints
            this.__tableAction24a = tableAction24a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableAction24aSet = true;
            }
        }
        return tableAction24a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired25a;
    private boolean __validationRequired25aSet = false;

    /**
     * Indicates whether or not at least one parameter on this action requires validation.
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired25a = this.__validationRequired25a;
        if (!this.__validationRequired25aSet)
        {
            // validationRequired has no pre constraints
            validationRequired25a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired25a = validationRequired25a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired25aSet = true;
            }
        }
        return validationRequired25a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isPopup()
    * @return boolean
    */
    protected abstract boolean handleIsPopup();

    private boolean __popup26a;
    private boolean __popup26aSet = false;

    /**
     * Indicates if this action forwards to a popup. Only applied when the target is a final state
     * pointing to another use case.
     * @return (boolean)handleIsPopup()
     */
    public final boolean isPopup()
    {
        boolean popup26a = this.__popup26a;
        if (!this.__popup26aSet)
        {
            // popup has no pre constraints
            popup26a = handleIsPopup();
            // popup has no post constraints
            this.__popup26a = popup26a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__popup26aSet = true;
            }
        }
        return popup26a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isFormResetRequired()
    * @return boolean
    */
    protected abstract boolean handleIsFormResetRequired();

    private boolean __formResetRequired27a;
    private boolean __formResetRequired27aSet = false;

    /**
     * Indicates if at least one parameter on the form requires being reset.
     * @return (boolean)handleIsFormResetRequired()
     */
    public final boolean isFormResetRequired()
    {
        boolean formResetRequired27a = this.__formResetRequired27a;
        if (!this.__formResetRequired27aSet)
        {
            // formResetRequired has no pre constraints
            formResetRequired27a = handleIsFormResetRequired();
            // formResetRequired has no post constraints
            this.__formResetRequired27a = formResetRequired27a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formResetRequired27aSet = true;
            }
        }
        return formResetRequired27a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isFormReset()
    * @return boolean
    */
    protected abstract boolean handleIsFormReset();

    private boolean __formReset28a;
    private boolean __formReset28aSet = false;

    /**
     * Whether or not the entire form should be reset (all action parameters on the form).
     * @return (boolean)handleIsFormReset()
     */
    public final boolean isFormReset()
    {
        boolean formReset28a = this.__formReset28a;
        if (!this.__formReset28aSet)
        {
            // formReset has no pre constraints
            formReset28a = handleIsFormReset();
            // formReset has no post constraints
            this.__formReset28a = formReset28a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formReset28aSet = true;
            }
        }
        return formReset28a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormImplementationGetter()
    * @return String
    */
    protected abstract String handleGetFormImplementationGetter();

    private String __formImplementationGetter29a;
    private boolean __formImplementationGetter29aSet = false;

    /**
     * The signature of the accessor method that returns the form implementation instance.
     * @return (String)handleGetFormImplementationGetter()
     */
    public final String getFormImplementationGetter()
    {
        String formImplementationGetter29a = this.__formImplementationGetter29a;
        if (!this.__formImplementationGetter29aSet)
        {
            // formImplementationGetter has no pre constraints
            formImplementationGetter29a = handleGetFormImplementationGetter();
            // formImplementationGetter has no post constraints
            this.__formImplementationGetter29a = formImplementationGetter29a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formImplementationGetter29aSet = true;
            }
        }
        return formImplementationGetter29a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFormSerialVersionUID()
    * @return String
    */
    protected abstract String handleGetFormSerialVersionUID();

    private String __formSerialVersionUID30a;
    private boolean __formSerialVersionUID30aSet = false;

    /**
     * The calcuated serial version UID for this action's form.
     * @return (String)handleGetFormSerialVersionUID()
     */
    public final String getFormSerialVersionUID()
    {
        String formSerialVersionUID30a = this.__formSerialVersionUID30a;
        if (!this.__formSerialVersionUID30aSet)
        {
            // formSerialVersionUID has no pre constraints
            formSerialVersionUID30a = handleGetFormSerialVersionUID();
            // formSerialVersionUID has no post constraints
            this.__formSerialVersionUID30a = formSerialVersionUID30a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formSerialVersionUID30aSet = true;
            }
        }
        return formSerialVersionUID30a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isFinalStateTarget()
    * @return boolean
    */
    protected abstract boolean handleIsFinalStateTarget();

    private boolean __finalStateTarget31a;
    private boolean __finalStateTarget31aSet = false;

    /**
     * Indicates whether or not a final state is the target of this action.
     * @return (boolean)handleIsFinalStateTarget()
     */
    public final boolean isFinalStateTarget()
    {
        boolean finalStateTarget31a = this.__finalStateTarget31a;
        if (!this.__finalStateTarget31aSet)
        {
            // finalStateTarget has no pre constraints
            finalStateTarget31a = handleIsFinalStateTarget();
            // finalStateTarget has no post constraints
            this.__finalStateTarget31a = finalStateTarget31a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__finalStateTarget31aSet = true;
            }
        }
        return finalStateTarget31a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getFromOutcome()
    * @return String
    */
    protected abstract String handleGetFromOutcome();

    private String __fromOutcome32a;
    private boolean __fromOutcome32aSet = false;

    /**
     * The name that corresponds to the from-outcome in an navigational rule.
     * @return (String)handleGetFromOutcome()
     */
    public final String getFromOutcome()
    {
        String fromOutcome32a = this.__fromOutcome32a;
        if (!this.__fromOutcome32aSet)
        {
            // fromOutcome has no pre constraints
            fromOutcome32a = handleGetFromOutcome();
            // fromOutcome has no post constraints
            this.__fromOutcome32a = fromOutcome32a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fromOutcome32aSet = true;
            }
        }
        return fromOutcome32a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isWarningMessagesPresent()
    * @return boolean
    */
    protected abstract boolean handleIsWarningMessagesPresent();

    private boolean __warningMessagesPresent33a;
    private boolean __warningMessagesPresent33aSet = false;

    /**
     * Whether or not any warning messages are present.
     * @return (boolean)handleIsWarningMessagesPresent()
     */
    public final boolean isWarningMessagesPresent()
    {
        boolean warningMessagesPresent33a = this.__warningMessagesPresent33a;
        if (!this.__warningMessagesPresent33aSet)
        {
            // warningMessagesPresent has no pre constraints
            warningMessagesPresent33a = handleIsWarningMessagesPresent();
            // warningMessagesPresent has no post constraints
            this.__warningMessagesPresent33a = warningMessagesPresent33a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__warningMessagesPresent33aSet = true;
            }
        }
        return warningMessagesPresent33a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getWarningMessages()
    * @return Map
    */
    protected abstract Map handleGetWarningMessages();

    private Map __warningMessages34a;
    private boolean __warningMessages34aSet = false;

    /**
     * Any messages used to indicate a warning.
     * @return (Map)handleGetWarningMessages()
     */
    public final Map getWarningMessages()
    {
        Map warningMessages34a = this.__warningMessages34a;
        if (!this.__warningMessages34aSet)
        {
            // warningMessages has no pre constraints
            warningMessages34a = handleGetWarningMessages();
            // warningMessages has no post constraints
            this.__warningMessages34a = warningMessages34a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__warningMessages34aSet = true;
            }
        }
        return warningMessages34a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isSuccessMessagesPresent()
    * @return boolean
    */
    protected abstract boolean handleIsSuccessMessagesPresent();

    private boolean __successMessagesPresent35a;
    private boolean __successMessagesPresent35aSet = false;

    /**
     * Indicates whether or not any success messags are present.
     * @return (boolean)handleIsSuccessMessagesPresent()
     */
    public final boolean isSuccessMessagesPresent()
    {
        boolean successMessagesPresent35a = this.__successMessagesPresent35a;
        if (!this.__successMessagesPresent35aSet)
        {
            // successMessagesPresent has no pre constraints
            successMessagesPresent35a = handleIsSuccessMessagesPresent();
            // successMessagesPresent has no post constraints
            this.__successMessagesPresent35a = successMessagesPresent35a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__successMessagesPresent35aSet = true;
            }
        }
        return successMessagesPresent35a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getSuccessMessages()
    * @return Map
    */
    protected abstract Map handleGetSuccessMessages();

    private Map __successMessages36a;
    private boolean __successMessages36aSet = false;

    /**
     * Messages used to indicate successful execution.
     * @return (Map)handleGetSuccessMessages()
     */
    public final Map getSuccessMessages()
    {
        Map successMessages36a = this.__successMessages36a;
        if (!this.__successMessages36aSet)
        {
            // successMessages has no pre constraints
            successMessages36a = handleGetSuccessMessages();
            // successMessages has no post constraints
            this.__successMessages36a = successMessages36a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__successMessages36aSet = true;
            }
        }
        return successMessages36a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isNeedsFileUpload()
    * @return boolean
    */
    protected abstract boolean handleIsNeedsFileUpload();

    private boolean __needsFileUpload37a;
    private boolean __needsFileUpload37aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFAction.needsFileUpload
     * @return (boolean)handleIsNeedsFileUpload()
     */
    public final boolean isNeedsFileUpload()
    {
        boolean needsFileUpload37a = this.__needsFileUpload37a;
        if (!this.__needsFileUpload37aSet)
        {
            // needsFileUpload has no pre constraints
            needsFileUpload37a = handleIsNeedsFileUpload();
            // needsFileUpload has no post constraints
            this.__needsFileUpload37a = needsFileUpload37a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__needsFileUpload37aSet = true;
            }
        }
        return needsFileUpload37a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#getTriggerMethodName()
    * @return String
    */
    protected abstract String handleGetTriggerMethodName();

    private String __triggerMethodName38a;
    private boolean __triggerMethodName38aSet = false;

    /**
     * The name of the method to be executed when this action is triggered.
     * @return (String)handleGetTriggerMethodName()
     */
    public final String getTriggerMethodName()
    {
        String triggerMethodName38a = this.__triggerMethodName38a;
        if (!this.__triggerMethodName38aSet)
        {
            // triggerMethodName has no pre constraints
            triggerMethodName38a = handleGetTriggerMethodName();
            // triggerMethodName has no post constraints
            this.__triggerMethodName38a = triggerMethodName38a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__triggerMethodName38aSet = true;
            }
        }
        return triggerMethodName38a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAction#isDialog()
    * @return boolean
    */
    protected abstract boolean handleIsDialog();

    private boolean __dialog39a;
    private boolean __dialog39aSet = false;

    /**
     * Indicates if this action forwards to a dialog (use case runs in different conversation
     * scope). Only applied when the target is a final state pointing to another use case.
     * @return (boolean)handleIsDialog()
     */
    public final boolean isDialog()
    {
        boolean dialog39a = this.__dialog39a;
        if (!this.__dialog39aSet)
        {
            // dialog has no pre constraints
            dialog39a = handleIsDialog();
            // dialog has no post constraints
            this.__dialog39a = dialog39a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dialog39aSet = true;
            }
        }
        return dialog39a;
    }

    // ------------- associations ------------------

    private JSFParameter __getTableLinkParameter1r;
    private boolean __getTableLinkParameter1rSet = false;

    /**
     * Represents an action taken during a "front-end" event execution on a JSF application.
     * @return (JSFParameter)handleGetTableLinkParameter()
     */
    public final JSFParameter getTableLinkParameter()
    {
        JSFParameter getTableLinkParameter1r = this.__getTableLinkParameter1r;
        if (!this.__getTableLinkParameter1rSet)
        {
            // jSFAction has no pre constraints
            Object result = handleGetTableLinkParameter();
            MetafacadeBase shieldedResult = this.shieldedElement(result);
            try
            {
                getTableLinkParameter1r = (JSFParameter)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFActionLogic.logger.warn("incorrect metafacade cast for JSFActionLogic.getTableLinkParameter JSFParameter " + result + ": " + shieldedResult);
            }
            // jSFAction has no post constraints
            this.__getTableLinkParameter1r = getTableLinkParameter1r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getTableLinkParameter1rSet = true;
            }
        }
        return getTableLinkParameter1r;
    }

    /**
     * UML Specific type is transformed by shieldedElements to AndroMDA Metafacade type
     * @return Object
     */
    protected abstract Object handleGetTableLinkParameter();

    private List<JSFParameter> __getHiddenParameters2r;
    private boolean __getHiddenParameters2rSet = false;

    /**
     * Represents an action taken during a "front-end" event execution on a JSF application.
     * @return (List<JSFParameter>)handleGetHiddenParameters()
     */
    public final List<JSFParameter> getHiddenParameters()
    {
        List<JSFParameter> getHiddenParameters2r = this.__getHiddenParameters2r;
        if (!this.__getHiddenParameters2rSet)
        {
            // jSFAction has no pre constraints
            List result = handleGetHiddenParameters();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getHiddenParameters2r = (List<JSFParameter>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFActionLogic.logger.warn("incorrect metafacade cast for JSFActionLogic.getHiddenParameters List<JSFParameter> " + result + ": " + shieldedResult);
            }
            // jSFAction has no post constraints
            this.__getHiddenParameters2r = getHiddenParameters2r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getHiddenParameters2rSet = true;
            }
        }
        return getHiddenParameters2r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetHiddenParameters();

    /**
     * @return true
     * @see FrontEndAction
     */
    public boolean isFrontEndActionMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see FrontEndForward
     */
    public boolean isFrontEndForwardMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.TransitionFacade
     */
    public boolean isTransitionFacadeMetaType()
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

    // ----------- delegates to FrontEndAction ------------
    /**
     * Finds the parameter on this action having the given name, if no parameter is found, null is
     * returned instead.
     * @see FrontEndAction#findParameter(String name)
     */
    public ParameterFacade findParameter(String name)
    {
        return this.getSuperFrontEndAction().findParameter(name);
    }

    /**
     * All action forwards for this foward. Each action forward either calls a view or another
     * use-case (which is essentially an action).
     * @see FrontEndAction#getActionForwards()
     */
    public List<FrontEndForward> getActionForwards()
    {
        return this.getSuperFrontEndAction().getActionForwards();
    }

    /**
     * All action states visited by this action.
     * @see FrontEndAction#getActionStates()
     */
    public List<FrontEndActionState> getActionStates()
    {
        return this.getSuperFrontEndAction().getActionStates();
    }

    /**
     * The controller that will handle the execution of this front-end action. This controller is
     * set as the context of the activity graph (and therefore also of the use-case).
     * @see FrontEndAction#getController()
     */
    public FrontEndController getController()
    {
        return this.getSuperFrontEndAction().getController();
    }

    /**
     * All the transitions coming out of decision points. These transitions should all carry guards.
     * @see FrontEndAction#getDecisionTransitions()
     */
    public List<FrontEndForward> getDecisionTransitions()
    {
        return this.getSuperFrontEndAction().getDecisionTransitions();
    }

    /**
     * The controller operations to which this action defers, the order is preserved.
     * @see FrontEndAction#getDeferredOperations()
     */
    public List<FrontEndControllerOperation> getDeferredOperations()
    {
        return this.getSuperFrontEndAction().getDeferredOperations();
    }

    /**
     * Form fields for this action. Form fields are those parameters that can be altered by the user
     * on a corresponding view at runtime.
     * @see FrontEndAction#getFormFields()
     */
    public List<FrontEndParameter> getFormFields()
    {
        return this.getSuperFrontEndAction().getFormFields();
    }

    /**
     * The StateVertex (FrontEndView or PseudostateFacade) on which this action can be triggered.
     * @see FrontEndAction#getInput()
     */
    public StateVertexFacade getInput()
    {
        return this.getSuperFrontEndAction().getInput();
    }

    /**
     * All parameters sent by this "front-end" action.
     * @see FrontEndAction#getParameters()
     */
    public List<FrontEndParameter> getParameters()
    {
        return this.getSuperFrontEndAction().getParameters();
    }

    /**
     * All views that can be possibly targetted by triggering this action.
     * @see FrontEndAction#getTargetViews()
     */
    public List<FrontEndView> getTargetViews()
    {
        return this.getSuperFrontEndAction().getTargetViews();
    }

    /**
     * All the transitions that make up this action, this directly maps onto the forwards.
     * @see FrontEndAction#getTransitions()
     */
    public List<FrontEndForward> getTransitions()
    {
        return this.getSuperFrontEndAction().getTransitions();
    }

    /**
     * Indicates if this action represents the beginning of the front-end use case or not.
     * @see FrontEndAction#isUseCaseStart()
     */
    public boolean isUseCaseStart()
    {
        return this.getSuperFrontEndAction().isUseCaseStart();
    }

    /**
     * The method name used to delegate to this forward.
     * @see FrontEndForward#getActionMethodName()
     */
    public String getActionMethodName()
    {
        return this.getSuperFrontEndAction().getActionMethodName();
    }

    /**
     * The front-end actions directly containing this front-end forward.
     * @see FrontEndForward#getActions()
     */
    public List<FrontEndAction> getActions()
    {
        return this.getSuperFrontEndAction().getActions();
    }

    /**
     * The trigger for this front-end forward.
     * @see FrontEndForward#getDecisionTrigger()
     */
    public FrontEndEvent getDecisionTrigger()
    {
        return this.getSuperFrontEndAction().getDecisionTrigger();
    }

    /**
     * Tthe set of parameter used during transport in this forward.
     * @see FrontEndForward#getForwardParameters()
     */
    public List<FrontEndParameter> getForwardParameters()
    {
        return this.getSuperFrontEndAction().getForwardParameters();
    }

    /**
     * The activity graph which holds this forward if the graph is contained in a FrontEndUseCase.
     * @see FrontEndForward#getFrontEndActivityGraph()
     */
    public FrontEndActivityGraph getFrontEndActivityGraph()
    {
        return this.getSuperFrontEndAction().getFrontEndActivityGraph();
    }

    /**
     * The operation to which is called during execution of this front-end forward.
     * @see FrontEndForward#getOperationCall()
     */
    public FrontEndControllerOperation getOperationCall()
    {
        return this.getSuperFrontEndAction().getOperationCall();
    }

    /**
     * The use case in which this forward is contained.
     * @see FrontEndForward#getUseCase()
     */
    public FrontEndUseCase getUseCase()
    {
        return this.getSuperFrontEndAction().getUseCase();
    }

    /**
     * Indicates if this forward is contained in a FrontEndUseCase.
     * @see FrontEndForward#isContainedInFrontEndUseCase()
     */
    public boolean isContainedInFrontEndUseCase()
    {
        return this.getSuperFrontEndAction().isContainedInFrontEndUseCase();
    }

    /**
     * Indicates if this action directly targets a "front-end" view, false otherwise.
     * @see FrontEndForward#isEnteringView()
     */
    public boolean isEnteringView()
    {
        return this.getSuperFrontEndAction().isEnteringView();
    }

    /**
     * Indicates if this forward (transition) is coming out of a front-end view.
     * @see FrontEndForward#isExitingView()
     */
    public boolean isExitingView()
    {
        return this.getSuperFrontEndAction().isExitingView();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperFrontEndAction().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperFrontEndAction().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperFrontEndAction().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperFrontEndAction().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperFrontEndAction().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperFrontEndAction().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperFrontEndAction().getDocumentation(indent);
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
        return this.getSuperFrontEndAction().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperFrontEndAction().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperFrontEndAction().getFullyQualifiedName();
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
        return this.getSuperFrontEndAction().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperFrontEndAction().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperFrontEndAction().getId();
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
        return this.getSuperFrontEndAction().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperFrontEndAction().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperFrontEndAction().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperFrontEndAction().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperFrontEndAction().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperFrontEndAction().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperFrontEndAction().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperFrontEndAction().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperFrontEndAction().getPackagePath();
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
        return this.getSuperFrontEndAction().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperFrontEndAction().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperFrontEndAction().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperFrontEndAction().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperFrontEndAction().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperFrontEndAction().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperFrontEndAction().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperFrontEndAction().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperFrontEndAction().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperFrontEndAction().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperFrontEndAction().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperFrontEndAction().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperFrontEndAction().hasKeyword(keywordName);
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
        return this.getSuperFrontEndAction().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperFrontEndAction().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperFrontEndAction().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperFrontEndAction().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperFrontEndAction().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperFrontEndAction().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperFrontEndAction().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperFrontEndAction().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperFrontEndAction().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperFrontEndAction().translateConstraints(kind, translation);
    }

    /**
     * An action is a named element that is the fundamental unit of executable functionality. The
     * execution
     * of an action represents some transformation or processing in the modeled system, be it a
     * computer
     * system or otherwise. An action represents a single step within an activity, that is, one that
     * is not
     * further decomposed within the activity. An action has pre- and post-conditions.
     * @see org.andromda.metafacades.uml.TransitionFacade#getEffect()
     */
    public ActionFacade getEffect()
    {
        return this.getSuperFrontEndAction().getEffect();
    }

    /**
     * A representation of the model object 'Constraint'. A condition or restriction expressed in
     * natural
     * language text or in a machine readable language for the purpose of declaring some of the
     * semantics
     * of an element.
     * @see org.andromda.metafacades.uml.TransitionFacade#getGuard()
     */
    public GuardFacade getGuard()
    {
        return this.getSuperFrontEndAction().getGuard();
    }

    /**
     * A representation of the model object 'Vertex'. An abstraction of a node in a state machine
     * graph. In
     * general, it can be the source or destination of any number of transitions.
     * @see org.andromda.metafacades.uml.TransitionFacade#getSource()
     */
    public StateVertexFacade getSource()
    {
        return this.getSuperFrontEndAction().getSource();
    }

    /**
     * A representation of the model object 'Vertex'. An abstraction of a node in a state machine
     * graph. In
     * general, it can be the source or destination of any number of transitions.
     * @see org.andromda.metafacades.uml.TransitionFacade#getTarget()
     */
    public StateVertexFacade getTarget()
    {
        return this.getSuperFrontEndAction().getTarget();
    }

    /**
     * If a trigger is present on this transition, this event represents that trigger.
     * @see org.andromda.metafacades.uml.TransitionFacade#getTrigger()
     */
    public EventFacade getTrigger()
    {
        return this.getSuperFrontEndAction().getTrigger();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.enteringActionState
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringActionState()
     */
    public boolean isEnteringActionState()
    {
        return this.getSuperFrontEndAction().isEnteringActionState();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.enteringDecisionPoint
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringDecisionPoint()
     */
    public boolean isEnteringDecisionPoint()
    {
        return this.getSuperFrontEndAction().isEnteringDecisionPoint();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.enteringFinalState
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringFinalState()
     */
    public boolean isEnteringFinalState()
    {
        return this.getSuperFrontEndAction().isEnteringFinalState();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.exitingActionState
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingActionState()
     */
    public boolean isExitingActionState()
    {
        return this.getSuperFrontEndAction().isExitingActionState();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.exitingDecisionPoint
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingDecisionPoint()
     */
    public boolean isExitingDecisionPoint()
    {
        return this.getSuperFrontEndAction().isExitingDecisionPoint();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.exitingInitialState
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingInitialState()
     */
    public boolean isExitingInitialState()
    {
        return this.getSuperFrontEndAction().isExitingInitialState();
    }

    /**
     * TODO: Model Documentation for org.andromda.metafacades.uml.TransitionFacade.triggerPresent
     * @see org.andromda.metafacades.uml.TransitionFacade#isTriggerPresent()
     */
    public boolean isTriggerPresent()
    {
        return this.getSuperFrontEndAction().isTriggerPresent();
    }

    /**
     * @see MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperFrontEndAction().initialize();
    }

    /**
     * @return Object getSuperFrontEndAction().getValidationOwner()
     * @see MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperFrontEndAction().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperFrontEndAction().getValidationName()
     * @see MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperFrontEndAction().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperFrontEndAction().validateInvariants(validationMessages);
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