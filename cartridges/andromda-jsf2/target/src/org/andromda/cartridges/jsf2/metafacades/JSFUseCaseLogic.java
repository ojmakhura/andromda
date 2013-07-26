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
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ExtendFacade;
import org.andromda.metafacades.uml.ExtensionPointFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;
import org.andromda.metafacades.uml.IncludeFacade;
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
 * Represents a JSF use case.
 * MetafacadeLogic for JSFUseCase
 *
 * @see JSFUseCase
 */
public abstract class JSFUseCaseLogic
    extends MetafacadeBase
    implements JSFUseCase
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
    protected JSFUseCaseLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superFrontEndUseCase =
           (FrontEndUseCase)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.FrontEndUseCase",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFUseCaseLogic.class);

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFUseCase if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFUseCase";
        }
        return context;
    }

    private FrontEndUseCase superFrontEndUseCase;
    private boolean superFrontEndUseCaseInitialized = false;

    /**
     * Gets the FrontEndUseCase parent instance.
     * @return this.superFrontEndUseCase FrontEndUseCase
     */
    private FrontEndUseCase getSuperFrontEndUseCase()
    {
        if (!this.superFrontEndUseCaseInitialized)
        {
            ((MetafacadeBase)this.superFrontEndUseCase).setMetafacadeContext(this.getMetafacadeContext());
            this.superFrontEndUseCaseInitialized = true;
        }
        return this.superFrontEndUseCase;
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
            if (this.superFrontEndUseCaseInitialized)
            {
                ((MetafacadeBase)this.superFrontEndUseCase).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFUseCase
     */
    public boolean isJSFUseCaseMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see JSFUseCase#getPath()
    * @return String
    */
    protected abstract String handleGetPath();

    private String __path1a;
    private boolean __path1aSet = false;

    /**
     * The path to which this use case points.
     * @return (String)handleGetPath()
     */
    public final String getPath()
    {
        String path1a = this.__path1a;
        if (!this.__path1aSet)
        {
            // path has no pre constraints
            path1a = handleGetPath();
            // path has no post constraints
            this.__path1a = path1a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__path1aSet = true;
            }
        }
        return path1a;
    }

   /**
    * @see JSFUseCase#getPathRoot()
    * @return String
    */
    protected abstract String handleGetPathRoot();

    private String __pathRoot2a;
    private boolean __pathRoot2aSet = false;

    /**
     * The root path for this use case (this is the path the directory containing the use case's
     * resources).
     * @return (String)handleGetPathRoot()
     */
    public final String getPathRoot()
    {
        String pathRoot2a = this.__pathRoot2a;
        if (!this.__pathRoot2aSet)
        {
            // pathRoot has no pre constraints
            pathRoot2a = handleGetPathRoot();
            // pathRoot has no post constraints
            this.__pathRoot2a = pathRoot2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__pathRoot2aSet = true;
            }
        }
        return pathRoot2a;
    }

   /**
    * @see JSFUseCase#getForwardName()
    * @return String
    */
    protected abstract String handleGetForwardName();

    private String __forwardName3a;
    private boolean __forwardName3aSet = false;

    /**
     * The name that will cause a forward to use case.
     * @return (String)handleGetForwardName()
     */
    public final String getForwardName()
    {
        String forwardName3a = this.__forwardName3a;
        if (!this.__forwardName3aSet)
        {
            // forwardName has no pre constraints
            forwardName3a = handleGetForwardName();
            // forwardName has no post constraints
            this.__forwardName3a = forwardName3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__forwardName3aSet = true;
            }
        }
        return forwardName3a;
    }

   /**
    * @see JSFUseCase#getAllMessages()
    * @return Map
    */
    protected abstract Map handleGetAllMessages();

    private Map __allMessages4a;
    private boolean __allMessages4aSet = false;

    /**
     * A map with keys sorted alphabetically, normalized across all different use-cases, views, etc.
     * @return (Map)handleGetAllMessages()
     */
    public final Map getAllMessages()
    {
        Map allMessages4a = this.__allMessages4a;
        if (!this.__allMessages4aSet)
        {
            // allMessages has no pre constraints
            allMessages4a = handleGetAllMessages();
            // allMessages has no post constraints
            this.__allMessages4a = allMessages4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__allMessages4aSet = true;
            }
        }
        return allMessages4a;
    }

   /**
    * @see JSFUseCase#getTitleKey()
    * @return String
    */
    protected abstract String handleGetTitleKey();

    private String __titleKey5a;
    private boolean __titleKey5aSet = false;

    /**
     * The title message key for this use-case.
     * @return (String)handleGetTitleKey()
     */
    public final String getTitleKey()
    {
        String titleKey5a = this.__titleKey5a;
        if (!this.__titleKey5aSet)
        {
            // titleKey has no pre constraints
            titleKey5a = handleGetTitleKey();
            // titleKey has no post constraints
            this.__titleKey5a = titleKey5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__titleKey5aSet = true;
            }
        }
        return titleKey5a;
    }

   /**
    * @see JSFUseCase#getTitleValue()
    * @return String
    */
    protected abstract String handleGetTitleValue();

    private String __titleValue6a;
    private boolean __titleValue6aSet = false;

    /**
     * The title message value for this use-case.
     * @return (String)handleGetTitleValue()
     */
    public final String getTitleValue()
    {
        String titleValue6a = this.__titleValue6a;
        if (!this.__titleValue6aSet)
        {
            // titleValue has no pre constraints
            titleValue6a = handleGetTitleValue();
            // titleValue has no post constraints
            this.__titleValue6a = titleValue6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__titleValue6aSet = true;
            }
        }
        return titleValue6a;
    }

   /**
    * @see JSFUseCase#getActionClassName()
    * @return String
    */
    protected abstract String handleGetActionClassName();

    private String __actionClassName7a;
    private boolean __actionClassName7aSet = false;

    /**
     * The name of the action class that forwards to this use case.
     * @return (String)handleGetActionClassName()
     */
    public final String getActionClassName()
    {
        String actionClassName7a = this.__actionClassName7a;
        if (!this.__actionClassName7aSet)
        {
            // actionClassName has no pre constraints
            actionClassName7a = handleGetActionClassName();
            // actionClassName has no post constraints
            this.__actionClassName7a = actionClassName7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__actionClassName7aSet = true;
            }
        }
        return actionClassName7a;
    }

   /**
    * @see JSFUseCase#getFullyQualifiedActionClassPath()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedActionClassPath();

    private String __fullyQualifiedActionClassPath8a;
    private boolean __fullyQualifiedActionClassPath8aSet = false;

    /**
     * The fully qualified path to the action class that forwards to this use case.
     * @return (String)handleGetFullyQualifiedActionClassPath()
     */
    public final String getFullyQualifiedActionClassPath()
    {
        String fullyQualifiedActionClassPath8a = this.__fullyQualifiedActionClassPath8a;
        if (!this.__fullyQualifiedActionClassPath8aSet)
        {
            // fullyQualifiedActionClassPath has no pre constraints
            fullyQualifiedActionClassPath8a = handleGetFullyQualifiedActionClassPath();
            // fullyQualifiedActionClassPath has no post constraints
            this.__fullyQualifiedActionClassPath8a = fullyQualifiedActionClassPath8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedActionClassPath8aSet = true;
            }
        }
        return fullyQualifiedActionClassPath8a;
    }

   /**
    * @see JSFUseCase#getControllerAction()
    * @return String
    */
    protected abstract String handleGetControllerAction();

    private String __controllerAction9a;
    private boolean __controllerAction9aSet = false;

    /**
     * The name of the action on the controller that executions this use case.
     * @return (String)handleGetControllerAction()
     */
    public final String getControllerAction()
    {
        String controllerAction9a = this.__controllerAction9a;
        if (!this.__controllerAction9aSet)
        {
            // controllerAction has no pre constraints
            controllerAction9a = handleGetControllerAction();
            // controllerAction has no post constraints
            this.__controllerAction9a = controllerAction9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__controllerAction9aSet = true;
            }
        }
        return controllerAction9a;
    }

   /**
    * @see JSFUseCase#getFullyQualifiedActionClassName()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedActionClassName();

    private String __fullyQualifiedActionClassName10a;
    private boolean __fullyQualifiedActionClassName10aSet = false;

    /**
     * The fully qualified name of the action class that forwards to this use case.
     * @return (String)handleGetFullyQualifiedActionClassName()
     */
    public final String getFullyQualifiedActionClassName()
    {
        String fullyQualifiedActionClassName10a = this.__fullyQualifiedActionClassName10a;
        if (!this.__fullyQualifiedActionClassName10aSet)
        {
            // fullyQualifiedActionClassName has no pre constraints
            fullyQualifiedActionClassName10a = handleGetFullyQualifiedActionClassName();
            // fullyQualifiedActionClassName has no post constraints
            this.__fullyQualifiedActionClassName10a = fullyQualifiedActionClassName10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedActionClassName10aSet = true;
            }
        }
        return fullyQualifiedActionClassName10a;
    }

   /**
    * @see JSFUseCase#getFormKey()
    * @return String
    */
    protected abstract String handleGetFormKey();

    private String __formKey11a;
    private boolean __formKey11aSet = false;

    /**
     * The key under which to store the action form passed along in this in this use-case.
     * @return (String)handleGetFormKey()
     */
    public final String getFormKey()
    {
        String formKey11a = this.__formKey11a;
        if (!this.__formKey11aSet)
        {
            // formKey has no pre constraints
            formKey11a = handleGetFormKey();
            // formKey has no post constraints
            this.__formKey11a = formKey11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formKey11aSet = true;
            }
        }
        return formKey11a;
    }

   /**
    * @see JSFUseCase#getInitialTargetPath()
    * @return String
    */
    protected abstract String handleGetInitialTargetPath();

    private String __initialTargetPath12a;
    private boolean __initialTargetPath12aSet = false;

    /**
     * The path of the initial target going into this use case.
     * @return (String)handleGetInitialTargetPath()
     */
    public final String getInitialTargetPath()
    {
        String initialTargetPath12a = this.__initialTargetPath12a;
        if (!this.__initialTargetPath12aSet)
        {
            // initialTargetPath has no pre constraints
            initialTargetPath12a = handleGetInitialTargetPath();
            // initialTargetPath has no post constraints
            this.__initialTargetPath12a = initialTargetPath12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__initialTargetPath12aSet = true;
            }
        }
        return initialTargetPath12a;
    }

   /**
    * @see JSFUseCase#isInitialTargetView()
    * @return boolean
    */
    protected abstract boolean handleIsInitialTargetView();

    private boolean __initialTargetView13a;
    private boolean __initialTargetView13aSet = false;

    /**
     * Indicates whether or not the initial target of this use case is a view or not.
     * @return (boolean)handleIsInitialTargetView()
     */
    public final boolean isInitialTargetView()
    {
        boolean initialTargetView13a = this.__initialTargetView13a;
        if (!this.__initialTargetView13aSet)
        {
            // initialTargetView has no pre constraints
            initialTargetView13a = handleIsInitialTargetView();
            // initialTargetView has no post constraints
            this.__initialTargetView13a = initialTargetView13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__initialTargetView13aSet = true;
            }
        }
        return initialTargetView13a;
    }

   /**
    * @see JSFUseCase#isApplicationValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsApplicationValidationRequired();

    private boolean __applicationValidationRequired14a;
    private boolean __applicationValidationRequired14aSet = false;

    /**
     * Indicates that at least one client/server parameter found in the collection of existing
     * use-cases requires validation.
     * @return (boolean)handleIsApplicationValidationRequired()
     */
    public final boolean isApplicationValidationRequired()
    {
        boolean applicationValidationRequired14a = this.__applicationValidationRequired14a;
        if (!this.__applicationValidationRequired14aSet)
        {
            // applicationValidationRequired has no pre constraints
            applicationValidationRequired14a = handleIsApplicationValidationRequired();
            // applicationValidationRequired has no post constraints
            this.__applicationValidationRequired14a = applicationValidationRequired14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__applicationValidationRequired14aSet = true;
            }
        }
        return applicationValidationRequired14a;
    }

   /**
    * @see JSFUseCase#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired15a;
    private boolean __validationRequired15aSet = false;

    /**
     * Indicates whether or not at least one parameter in this use-case require validation.
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired15a = this.__validationRequired15a;
        if (!this.__validationRequired15aSet)
        {
            // validationRequired has no pre constraints
            validationRequired15a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired15a = validationRequired15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired15aSet = true;
            }
        }
        return validationRequired15a;
    }

   /**
    * @see JSFUseCase#isViewHasNameOfUseCase()
    * @return boolean
    */
    protected abstract boolean handleIsViewHasNameOfUseCase();

    private boolean __viewHasNameOfUseCase16a;
    private boolean __viewHasNameOfUseCase16aSet = false;

    /**
     * Indicates whether or not at least one view in the use case has the same name as this use
     * case.
     * @return (boolean)handleIsViewHasNameOfUseCase()
     */
    public final boolean isViewHasNameOfUseCase()
    {
        boolean viewHasNameOfUseCase16a = this.__viewHasNameOfUseCase16a;
        if (!this.__viewHasNameOfUseCase16aSet)
        {
            // viewHasNameOfUseCase has no pre constraints
            viewHasNameOfUseCase16a = handleIsViewHasNameOfUseCase();
            // viewHasNameOfUseCase has no post constraints
            this.__viewHasNameOfUseCase16a = viewHasNameOfUseCase16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__viewHasNameOfUseCase16aSet = true;
            }
        }
        return viewHasNameOfUseCase16a;
    }

   /**
    * @see JSFUseCase#isRegistrationUseCase()
    * @return boolean
    */
    protected abstract boolean handleIsRegistrationUseCase();

    private boolean __registrationUseCase17a;
    private boolean __registrationUseCase17aSet = false;

    /**
     * Indicates whether or not this is a front-end registration use case.  Only one use case can be
     * labeled as a 'registration' use case.
     * @return (boolean)handleIsRegistrationUseCase()
     */
    public final boolean isRegistrationUseCase()
    {
        boolean registrationUseCase17a = this.__registrationUseCase17a;
        if (!this.__registrationUseCase17aSet)
        {
            // registrationUseCase has no pre constraints
            registrationUseCase17a = handleIsRegistrationUseCase();
            // registrationUseCase has no post constraints
            this.__registrationUseCase17a = registrationUseCase17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__registrationUseCase17aSet = true;
            }
        }
        return registrationUseCase17a;
    }

   /**
    * @see JSFUseCase#getForwardsClassName()
    * @return String
    */
    protected abstract String handleGetForwardsClassName();

    private String __forwardsClassName18a;
    private boolean __forwardsClassName18aSet = false;

    /**
     * The name of the class that stores all the forwards paths.
     * @return (String)handleGetForwardsClassName()
     */
    public final String getForwardsClassName()
    {
        String forwardsClassName18a = this.__forwardsClassName18a;
        if (!this.__forwardsClassName18aSet)
        {
            // forwardsClassName has no pre constraints
            forwardsClassName18a = handleGetForwardsClassName();
            // forwardsClassName has no post constraints
            this.__forwardsClassName18a = forwardsClassName18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__forwardsClassName18aSet = true;
            }
        }
        return forwardsClassName18a;
    }

   /**
    * @see JSFUseCase#getAllForwards()
    * @return List
    */
    protected abstract List handleGetAllForwards();

    private List __allForwards19a;
    private boolean __allForwards19aSet = false;

    /**
     * Constains all forwards includes regular FrontEndForwards and all actiion forwards.
     * @return (List)handleGetAllForwards()
     */
    public final List getAllForwards()
    {
        List allForwards19a = this.__allForwards19a;
        if (!this.__allForwards19aSet)
        {
            // allForwards has no pre constraints
            allForwards19a = handleGetAllForwards();
            // allForwards has no post constraints
            this.__allForwards19a = allForwards19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__allForwards19aSet = true;
            }
        }
        return allForwards19a;
    }

   /**
    * @see JSFUseCase#getPortletViewForwardName()
    * @return String
    */
    protected abstract String handleGetPortletViewForwardName();

    private String __portletViewForwardName20a;
    private boolean __portletViewForwardName20aSet = false;

    /**
     * The forward name for the portlet 'view' page.
     * @return (String)handleGetPortletViewForwardName()
     */
    public final String getPortletViewForwardName()
    {
        String portletViewForwardName20a = this.__portletViewForwardName20a;
        if (!this.__portletViewForwardName20aSet)
        {
            // portletViewForwardName has no pre constraints
            portletViewForwardName20a = handleGetPortletViewForwardName();
            // portletViewForwardName has no post constraints
            this.__portletViewForwardName20a = portletViewForwardName20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__portletViewForwardName20aSet = true;
            }
        }
        return portletViewForwardName20a;
    }

   /**
    * @see JSFUseCase#getPortletEditForwardName()
    * @return String
    */
    protected abstract String handleGetPortletEditForwardName();

    private String __portletEditForwardName21a;
    private boolean __portletEditForwardName21aSet = false;

    /**
     * The forward name for the portlet 'edit' page.
     * @return (String)handleGetPortletEditForwardName()
     */
    public final String getPortletEditForwardName()
    {
        String portletEditForwardName21a = this.__portletEditForwardName21a;
        if (!this.__portletEditForwardName21aSet)
        {
            // portletEditForwardName has no pre constraints
            portletEditForwardName21a = handleGetPortletEditForwardName();
            // portletEditForwardName has no post constraints
            this.__portletEditForwardName21a = portletEditForwardName21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__portletEditForwardName21aSet = true;
            }
        }
        return portletEditForwardName21a;
    }

   /**
    * @see JSFUseCase#getPortletHelpForwardName()
    * @return String
    */
    protected abstract String handleGetPortletHelpForwardName();

    private String __portletHelpForwardName22a;
    private boolean __portletHelpForwardName22aSet = false;

    /**
     * The forward name for the portlet 'help' page.
     * @return (String)handleGetPortletHelpForwardName()
     */
    public final String getPortletHelpForwardName()
    {
        String portletHelpForwardName22a = this.__portletHelpForwardName22a;
        if (!this.__portletHelpForwardName22aSet)
        {
            // portletHelpForwardName has no pre constraints
            portletHelpForwardName22a = handleGetPortletHelpForwardName();
            // portletHelpForwardName has no post constraints
            this.__portletHelpForwardName22a = portletHelpForwardName22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__portletHelpForwardName22aSet = true;
            }
        }
        return portletHelpForwardName22a;
    }

   /**
    * @see JSFUseCase#getPortletViewPath()
    * @return String
    */
    protected abstract String handleGetPortletViewPath();

    private String __portletViewPath23a;
    private boolean __portletViewPath23aSet = false;

    /**
     * The path to the portlet 'view' page.
     * @return (String)handleGetPortletViewPath()
     */
    public final String getPortletViewPath()
    {
        String portletViewPath23a = this.__portletViewPath23a;
        if (!this.__portletViewPath23aSet)
        {
            // portletViewPath has no pre constraints
            portletViewPath23a = handleGetPortletViewPath();
            // portletViewPath has no post constraints
            this.__portletViewPath23a = portletViewPath23a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__portletViewPath23aSet = true;
            }
        }
        return portletViewPath23a;
    }

   /**
    * @see JSFUseCase#getPortletEditPath()
    * @return String
    */
    protected abstract String handleGetPortletEditPath();

    private String __portletEditPath24a;
    private boolean __portletEditPath24aSet = false;

    /**
     * The path to the portlet 'edit' page.
     * @return (String)handleGetPortletEditPath()
     */
    public final String getPortletEditPath()
    {
        String portletEditPath24a = this.__portletEditPath24a;
        if (!this.__portletEditPath24aSet)
        {
            // portletEditPath has no pre constraints
            portletEditPath24a = handleGetPortletEditPath();
            // portletEditPath has no post constraints
            this.__portletEditPath24a = portletEditPath24a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__portletEditPath24aSet = true;
            }
        }
        return portletEditPath24a;
    }

   /**
    * @see JSFUseCase#getPortletHelpPath()
    * @return String
    */
    protected abstract String handleGetPortletHelpPath();

    private String __portletHelpPath25a;
    private boolean __portletHelpPath25aSet = false;

    /**
     * The path to the 'help' page of the portlet.
     * @return (String)handleGetPortletHelpPath()
     */
    public final String getPortletHelpPath()
    {
        String portletHelpPath25a = this.__portletHelpPath25a;
        if (!this.__portletHelpPath25aSet)
        {
            // portletHelpPath has no pre constraints
            portletHelpPath25a = handleGetPortletHelpPath();
            // portletHelpPath has no post constraints
            this.__portletHelpPath25a = portletHelpPath25a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__portletHelpPath25aSet = true;
            }
        }
        return portletHelpPath25a;
    }

    // ---------------- business methods ----------------------

    /**
     * Method to be implemented in descendants
     * Retrieves all navigation rules for the faces-config.xml
     * @return Collection
     */
    protected abstract Collection handleGetNavigationRules();

    /**
     * Retrieves all navigation rules for the faces-config.xml
     * @return handleGetNavigationRules()
     */
    public Collection getNavigationRules()
    {
        // getNavigationRules has no pre constraints
        Collection returnValue = handleGetNavigationRules();
        // getNavigationRules has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * JSFUseCase.getNavigationChildren
     * @return Collection
     */
    protected abstract Collection handleGetNavigationChildren();

    /**
     * TODO: Model Documentation for
     * JSFUseCase.getNavigationChildren
     * @return handleGetNavigationChildren()
     */
    public Collection getNavigationChildren()
    {
        // getNavigationChildren has no pre constraints
        Collection returnValue = handleGetNavigationChildren();
        // getNavigationChildren has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * JSFUseCase.getNavigationParents
     * @return Collection
     */
    protected abstract Collection handleGetNavigationParents();

    /**
     * TODO: Model Documentation for
     * JSFUseCase.getNavigationParents
     * @return handleGetNavigationParents()
     */
    public Collection getNavigationParents()
    {
        // getNavigationParents has no pre constraints
        Collection returnValue = handleGetNavigationParents();
        // getNavigationParents has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * JSFUseCase.getActionRoles
     * @return String
     */
    protected abstract String handleGetActionRoles();

    /**
     * TODO: Model Documentation for
     * JSFUseCase.getActionRoles
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
     * Represents a JSF use case.
     * @return (List<JSFForward>)handleGetForwards()
     */
    public final List<JSFForward> getForwards()
    {
        List<JSFForward> getForwards1r = null;
        // jSFUseCase has no pre constraints
        List result = handleGetForwards();
        List shieldedResult = this.shieldedElements(result);
        try
        {
            getForwards1r = (List<JSFForward>)shieldedResult;
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            JSFUseCaseLogic.logger.warn("incorrect metafacade cast for JSFUseCaseLogic.getForwards List<JSFForward> " + result + ": " + shieldedResult);
        }
        // jSFUseCase has no post constraints
        return getForwards1r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetForwards();

    private List<JSFAction> __getActionForwards2r;
    private boolean __getActionForwards2rSet = false;

    /**
     * Represents a JSF use case.
     * @return (List<JSFAction>)handleGetActionForwards()
     */
    public final List<JSFAction> getActionForwards()
    {
        List<JSFAction> getActionForwards2r = this.__getActionForwards2r;
        if (!this.__getActionForwards2rSet)
        {
            // jSFUseCase has no pre constraints
            List result = handleGetActionForwards();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getActionForwards2r = (List<JSFAction>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFUseCaseLogic.logger.warn("incorrect metafacade cast for JSFUseCaseLogic.getActionForwards List<JSFAction> " + result + ": " + shieldedResult);
            }
            // jSFUseCase has no post constraints
            this.__getActionForwards2r = getActionForwards2r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getActionForwards2rSet = true;
            }
        }
        return getActionForwards2r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetActionForwards();

    private Collection<JSFView> __getAllViews3r;
    private boolean __getAllViews3rSet = false;

    /**
     * Represents a JSF use case.
     * @return (Collection<JSFView>)handleGetAllViews()
     */
    public final Collection<JSFView> getAllViews()
    {
        Collection<JSFView> getAllViews3r = this.__getAllViews3r;
        if (!this.__getAllViews3rSet)
        {
            // jSFUseCase has no pre constraints
            Collection result = handleGetAllViews();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getAllViews3r = (Collection<JSFView>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFUseCaseLogic.logger.warn("incorrect metafacade cast for JSFUseCaseLogic.getAllViews Collection<JSFView> " + result + ": " + shieldedResult);
            }
            // jSFUseCase has no post constraints
            this.__getAllViews3r = getAllViews3r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getAllViews3rSet = true;
            }
        }
        return getAllViews3r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  Collection
     */
    protected abstract Collection handleGetAllViews();

    private JSFPortletPreferences __getPreferences4r;
    private boolean __getPreferences4rSet = false;

    /**
     * The use case to which the portlet preferences belongs.
     * @return (JSFPortletPreferences)handleGetPreferences()
     */
    public final JSFPortletPreferences getPreferences()
    {
        JSFPortletPreferences getPreferences4r = this.__getPreferences4r;
        if (!this.__getPreferences4rSet)
        {
            // useCase has no pre constraints
            Object result = handleGetPreferences();
            MetafacadeBase shieldedResult = this.shieldedElement(result);
            try
            {
                getPreferences4r = (JSFPortletPreferences)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFUseCaseLogic.logger.warn("incorrect metafacade cast for JSFUseCaseLogic.getPreferences JSFPortletPreferences " + result + ": " + shieldedResult);
            }
            // useCase has no post constraints
            this.__getPreferences4r = getPreferences4r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getPreferences4rSet = true;
            }
        }
        return getPreferences4r;
    }

    /**
     * UML Specific type is transformed by shieldedElements to AndroMDA Metafacade type
     * @return Object
     */
    protected abstract Object handleGetPreferences();

    private List<JSFUseCase> __getRegistrationUseCases5r;
    private boolean __getRegistrationUseCases5rSet = false;

    /**
     * Represents a JSF use case.
     * @return (List<JSFUseCase>)handleGetRegistrationUseCases()
     */
    public final List<JSFUseCase> getRegistrationUseCases()
    {
        List<JSFUseCase> getRegistrationUseCases5r = this.__getRegistrationUseCases5r;
        if (!this.__getRegistrationUseCases5rSet)
        {
            // jSFUseCase has no pre constraints
            List result = handleGetRegistrationUseCases();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getRegistrationUseCases5r = (List<JSFUseCase>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFUseCaseLogic.logger.warn("incorrect metafacade cast for JSFUseCaseLogic.getRegistrationUseCases List<JSFUseCase> " + result + ": " + shieldedResult);
            }
            // jSFUseCase has no post constraints
            this.__getRegistrationUseCases5r = getRegistrationUseCases5r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getRegistrationUseCases5rSet = true;
            }
        }
        return getRegistrationUseCases5r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetRegistrationUseCases();

    /**
     * @return true
     * @see FrontEndUseCase
     */
    public boolean isFrontEndUseCaseMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.UseCaseFacade
     */
    public boolean isUseCaseFacadeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.NamespaceFacade
     */
    public boolean isNamespaceFacadeMetaType()
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

    // ----------- delegates to FrontEndUseCase ------------
    /**
     * Return the attribute which name matches the parameter
     * @see ClassifierFacade#findAttribute(String name)
     */
    public AttributeFacade findAttribute(String name)
    {
        return this.getSuperFrontEndUseCase().findAttribute(name);
    }

    /**
     * Those abstraction dependencies for which this classifier is the client.
     * @see ClassifierFacade#getAbstractions()
     */
    public Collection<ClassifierFacade> getAbstractions()
    {
        return this.getSuperFrontEndUseCase().getAbstractions();
    }

    /**
     * Lists all classes associated to this one and any ancestor classes (through generalization).
     * There will be no duplicates. The order of the elements is predictable.
     * @see ClassifierFacade#getAllAssociatedClasses()
     */
    public Collection<ClassifierFacade> getAllAssociatedClasses()
    {
        return this.getSuperFrontEndUseCase().getAllAssociatedClasses();
    }

    /**
     * A collection containing all 'properties' of the classifier and its ancestors.  Properties are
     * any attributes and navigable connecting association ends.
     * @see ClassifierFacade#getAllProperties()
     */
    public Collection<ModelElementFacade> getAllProperties()
    {
        return this.getSuperFrontEndUseCase().getAllProperties();
    }

    /**
     * A collection containing all required and/or read-only 'properties' of the classifier and its
     * ancestors. Properties are any attributes and navigable connecting association ends.
     * @see ClassifierFacade#getAllRequiredConstructorParameters()
     */
    public Collection<ModelElementFacade> getAllRequiredConstructorParameters()
    {
        return this.getSuperFrontEndUseCase().getAllRequiredConstructorParameters();
    }

    /**
     * Gets the array type for this classifier.  If this classifier already represents an array, it
     * just returns itself.
     * @see ClassifierFacade#getArray()
     */
    public ClassifierFacade getArray()
    {
        return this.getSuperFrontEndUseCase().getArray();
    }

    /**
     * The name of the classifier as an array.
     * @see ClassifierFacade#getArrayName()
     */
    public String getArrayName()
    {
        return this.getSuperFrontEndUseCase().getArrayName();
    }

    /**
     * Lists the classes associated to this one, there is no repitition of classes. The order of the
     * elements is predictable.
     * @see ClassifierFacade#getAssociatedClasses()
     */
    public Collection<ClassifierFacade> getAssociatedClasses()
    {
        return this.getSuperFrontEndUseCase().getAssociatedClasses();
    }

    /**
     * Gets the association ends belonging to a classifier.
     * @see ClassifierFacade#getAssociationEnds()
     */
    public List<AssociationEndFacade> getAssociationEnds()
    {
        return this.getSuperFrontEndUseCase().getAssociationEnds();
    }

    /**
     * Gets the attributes that belong to the classifier.
     * @see ClassifierFacade#getAttributes()
     */
    public List<AttributeFacade> getAttributes()
    {
        return this.getSuperFrontEndUseCase().getAttributes();
    }

    /**
     * Gets all attributes for the classifier and if 'follow' is true goes up the inheritance
     * hierarchy and gets the attributes from the super classes as well.
     * @see ClassifierFacade#getAttributes(boolean follow)
     */
    public List<AttributeFacade> getAttributes(boolean follow)
    {
        return this.getSuperFrontEndUseCase().getAttributes(follow);
    }

    /**
     * The fully qualified name of the classifier as an array.
     * @see ClassifierFacade#getFullyQualifiedArrayName()
     */
    public String getFullyQualifiedArrayName()
    {
        return this.getSuperFrontEndUseCase().getFullyQualifiedArrayName();
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
        return this.getSuperFrontEndUseCase().getImplementationOperations();
    }

    /**
     * A comma separated list of the fully qualified names of all implemented interfaces.
     * @see ClassifierFacade#getImplementedInterfaceList()
     */
    public String getImplementedInterfaceList()
    {
        return this.getSuperFrontEndUseCase().getImplementedInterfaceList();
    }

    /**
     * Those attributes that are scoped to an instance of this class.
     * @see ClassifierFacade#getInstanceAttributes()
     */
    public Collection<AttributeFacade> getInstanceAttributes()
    {
        return this.getSuperFrontEndUseCase().getInstanceAttributes();
    }

    /**
     * Those operations that are scoped to an instance of this class.
     * @see ClassifierFacade#getInstanceOperations()
     */
    public List<OperationFacade> getInstanceOperations()
    {
        return this.getSuperFrontEndUseCase().getInstanceOperations();
    }

    /**
     * Those interfaces that are abstractions of this classifier, this basically means this
     * classifier realizes them.
     * @see ClassifierFacade#getInterfaceAbstractions()
     */
    public Collection<ClassifierFacade> getInterfaceAbstractions()
    {
        return this.getSuperFrontEndUseCase().getInterfaceAbstractions();
    }

    /**
     * A String representing a new Constructor declaration for this classifier type to be used in a
     * Java environment.
     * @see ClassifierFacade#getJavaNewString()
     */
    public String getJavaNewString()
    {
        return this.getSuperFrontEndUseCase().getJavaNewString();
    }

    /**
     * A String representing the null-value for this classifier type to be used in a Java
     * environment.
     * @see ClassifierFacade#getJavaNullString()
     */
    public String getJavaNullString()
    {
        return this.getSuperFrontEndUseCase().getJavaNullString();
    }

    /**
     * The other ends of this classifier's association ends which are navigable.
     * @see ClassifierFacade#getNavigableConnectingEnds()
     */
    public Collection<AssociationEndFacade> getNavigableConnectingEnds()
    {
        return this.getSuperFrontEndUseCase().getNavigableConnectingEnds();
    }

    /**
     * Get the other ends of this classifier's association ends which are navigable and if 'follow'
     * is true goes up the inheritance hierarchy and gets the super association ends as well.
     * @see ClassifierFacade#getNavigableConnectingEnds(boolean follow)
     */
    public List<AssociationEndFacade> getNavigableConnectingEnds(boolean follow)
    {
        return this.getSuperFrontEndUseCase().getNavigableConnectingEnds(follow);
    }

    /**
     * Assuming that the classifier is an array, this will return the non array type of the
     * classifier from
     * the model.  If the classifier is NOT an array, it will just return itself.
     * @see ClassifierFacade#getNonArray()
     */
    public ClassifierFacade getNonArray()
    {
        return this.getSuperFrontEndUseCase().getNonArray();
    }

    /**
     * The attributes from this classifier in the form of an operation call (this example would be
     * in Java): '(String attributeOne, String attributeTwo).  If there were no attributes on the
     * classifier, the result would be an empty '()'.
     * @see ClassifierFacade#getOperationCallFromAttributes()
     */
    public String getOperationCallFromAttributes()
    {
        return this.getSuperFrontEndUseCase().getOperationCallFromAttributes();
    }

    /**
     * The operations owned by this classifier.
     * @see ClassifierFacade#getOperations()
     */
    public List<OperationFacade> getOperations()
    {
        return this.getSuperFrontEndUseCase().getOperations();
    }

    /**
     * A collection containing all 'properties' of the classifier.  Properties are any attributes
     * and navigable connecting association ends.
     * @see ClassifierFacade#getProperties()
     */
    public List<ModelElementFacade> getProperties()
    {
        return this.getSuperFrontEndUseCase().getProperties();
    }

    /**
     * Gets all properties (attributes and navigable association ends) for the classifier and if
     * 'follow' is true goes up the inheritance hierarchy and gets the properties from the super
     * classes as well.
     * @see ClassifierFacade#getProperties(boolean follow)
     */
    public List getProperties(boolean follow)
    {
        return this.getSuperFrontEndUseCase().getProperties(follow);
    }

    /**
     * A collection containing all required and/or read-only 'properties' of the classifier. 
     * Properties are any attributes and navigable connecting association ends.
     * @see ClassifierFacade#getRequiredConstructorParameters()
     */
    public Collection<ModelElementFacade> getRequiredConstructorParameters()
    {
        return this.getSuperFrontEndUseCase().getRequiredConstructorParameters();
    }

    /**
     * Returns the serial version UID of the underlying model element.
     * @see ClassifierFacade#getSerialVersionUID()
     */
    public long getSerialVersionUID()
    {
        return this.getSuperFrontEndUseCase().getSerialVersionUID();
    }

    /**
     * Those attributes that are scoped to the definition of this class.
     * @see ClassifierFacade#getStaticAttributes()
     */
    public Collection<AttributeFacade> getStaticAttributes()
    {
        return this.getSuperFrontEndUseCase().getStaticAttributes();
    }

    /**
     * Those operations that are scoped to the definition of this class.
     * @see ClassifierFacade#getStaticOperations()
     */
    public List<OperationFacade> getStaticOperations()
    {
        return this.getSuperFrontEndUseCase().getStaticOperations();
    }

    /**
     * This class' superclass, returns the generalization if it is a ClassifierFacade, null
     * otherwise.
     * @see ClassifierFacade#getSuperClass()
     */
    public ClassifierFacade getSuperClass()
    {
        return this.getSuperFrontEndUseCase().getSuperClass();
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
        return this.getSuperFrontEndUseCase().getWrapperName();
    }

    /**
     * Indicates if this classifier is 'abstract'.
     * @see ClassifierFacade#isAbstract()
     */
    public boolean isAbstract()
    {
        return this.getSuperFrontEndUseCase().isAbstract();
    }

    /**
     * True if this classifier represents an array type. False otherwise.
     * @see ClassifierFacade#isArrayType()
     */
    public boolean isArrayType()
    {
        return this.getSuperFrontEndUseCase().isArrayType();
    }

    /**
     * True if the ClassifierFacade is an AssociationClass.
     * @see ClassifierFacade#isAssociationClass()
     */
    public boolean isAssociationClass()
    {
        return this.getSuperFrontEndUseCase().isAssociationClass();
    }

    /**
     * Returns true if this type represents a Blob type.
     * @see ClassifierFacade#isBlobType()
     */
    public boolean isBlobType()
    {
        return this.getSuperFrontEndUseCase().isBlobType();
    }

    /**
     * Indicates if this type represents a boolean type or not.
     * @see ClassifierFacade#isBooleanType()
     */
    public boolean isBooleanType()
    {
        return this.getSuperFrontEndUseCase().isBooleanType();
    }

    /**
     * Indicates if this type represents a char, Character, or java.lang.Character type or not.
     * @see ClassifierFacade#isCharacterType()
     */
    public boolean isCharacterType()
    {
        return this.getSuperFrontEndUseCase().isCharacterType();
    }

    /**
     * Returns true if this type represents a Clob type.
     * @see ClassifierFacade#isClobType()
     */
    public boolean isClobType()
    {
        return this.getSuperFrontEndUseCase().isClobType();
    }

    /**
     * True if this classifier represents a collection type. False otherwise.
     * @see ClassifierFacade#isCollectionType()
     */
    public boolean isCollectionType()
    {
        return this.getSuperFrontEndUseCase().isCollectionType();
    }

    /**
     * True/false depending on whether or not this classifier represents a datatype. A data type is
     * a type whose instances are identified only by their value. A data type may contain attributes
     * to support the modeling of structured data types.
     * @see ClassifierFacade#isDataType()
     */
    public boolean isDataType()
    {
        return this.getSuperFrontEndUseCase().isDataType();
    }

    /**
     * True when this classifier is a date type.
     * @see ClassifierFacade#isDateType()
     */
    public boolean isDateType()
    {
        return this.getSuperFrontEndUseCase().isDateType();
    }

    /**
     * Indicates if this type represents a Double type or not.
     * @see ClassifierFacade#isDoubleType()
     */
    public boolean isDoubleType()
    {
        return this.getSuperFrontEndUseCase().isDoubleType();
    }

    /**
     * Indicates whether or not this classifier represents an "EmbeddedValue'.
     * @see ClassifierFacade#isEmbeddedValue()
     */
    public boolean isEmbeddedValue()
    {
        return this.getSuperFrontEndUseCase().isEmbeddedValue();
    }

    /**
     * True if this classifier is in fact marked as an enumeration.
     * @see ClassifierFacade#isEnumeration()
     */
    public boolean isEnumeration()
    {
        return this.getSuperFrontEndUseCase().isEnumeration();
    }

    /**
     * Returns true if this type represents a 'file' type.
     * @see ClassifierFacade#isFileType()
     */
    public boolean isFileType()
    {
        return this.getSuperFrontEndUseCase().isFileType();
    }

    /**
     * Indicates if this type represents a Float type or not.
     * @see ClassifierFacade#isFloatType()
     */
    public boolean isFloatType()
    {
        return this.getSuperFrontEndUseCase().isFloatType();
    }

    /**
     * Indicates if this type represents an int or Integer or java.lang.Integer type or not.
     * @see ClassifierFacade#isIntegerType()
     */
    public boolean isIntegerType()
    {
        return this.getSuperFrontEndUseCase().isIntegerType();
    }

    /**
     * True/false depending on whether or not this Classifier represents an interface.
     * @see ClassifierFacade#isInterface()
     */
    public boolean isInterface()
    {
        return this.getSuperFrontEndUseCase().isInterface();
    }

    /**
     * True if this classifier cannot be extended and represent a leaf in the inheritance tree.
     * @see ClassifierFacade#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.getSuperFrontEndUseCase().isLeaf();
    }

    /**
     * True if this classifier represents a list type. False otherwise.
     * @see ClassifierFacade#isListType()
     */
    public boolean isListType()
    {
        return this.getSuperFrontEndUseCase().isListType();
    }

    /**
     * Indicates if this type represents a Long type or not.
     * @see ClassifierFacade#isLongType()
     */
    public boolean isLongType()
    {
        return this.getSuperFrontEndUseCase().isLongType();
    }

    /**
     * Indicates whether or not this classifier represents a Map type.
     * @see ClassifierFacade#isMapType()
     */
    public boolean isMapType()
    {
        return this.getSuperFrontEndUseCase().isMapType();
    }

    /**
     * Indicates whether or not this classifier represents a primitive type.
     * @see ClassifierFacade#isPrimitive()
     */
    public boolean isPrimitive()
    {
        return this.getSuperFrontEndUseCase().isPrimitive();
    }

    /**
     * True if this classifier represents a set type. False otherwise.
     * @see ClassifierFacade#isSetType()
     */
    public boolean isSetType()
    {
        return this.getSuperFrontEndUseCase().isSetType();
    }

    /**
     * Indicates whether or not this classifier represents a string type.
     * @see ClassifierFacade#isStringType()
     */
    public boolean isStringType()
    {
        return this.getSuperFrontEndUseCase().isStringType();
    }

    /**
     * Indicates whether or not this classifier represents a time type.
     * @see ClassifierFacade#isTimeType()
     */
    public boolean isTimeType()
    {
        return this.getSuperFrontEndUseCase().isTimeType();
    }

    /**
     * Returns true if this type is a wrapped primitive type.
     * @see ClassifierFacade#isWrappedPrimitive()
     */
    public boolean isWrappedPrimitive()
    {
        return this.getSuperFrontEndUseCase().isWrappedPrimitive();
    }

    /**
     * The actions for this use-case. This will include the initial action to start the use-case.
     * @see FrontEndUseCase#getActions()
     */
    public List<FrontEndAction> getActions()
    {
        return this.getSuperFrontEndUseCase().getActions();
    }

    /**
     * Returns the activity graph describing this use-case in more detail.
     * @see FrontEndUseCase#getActivityGraph()
     */
    public FrontEndActivityGraph getActivityGraph()
    {
        return this.getSuperFrontEndUseCase().getActivityGraph();
    }

    /**
     * All roles that directly or indirectly related to any "front-end" use cases.
     * @see FrontEndUseCase#getAllRoles()
     */
    public List<Role> getAllRoles()
    {
        return this.getSuperFrontEndUseCase().getAllRoles();
    }

    /**
     * Returns all struts use-cases in this "front end" application.
     * @see FrontEndUseCase#getAllUseCases()
     */
    public List<FrontEndUseCase> getAllUseCases()
    {
        return this.getSuperFrontEndUseCase().getAllUseCases();
    }

    /**
     * Returns the controller for this use-case.
     * @see FrontEndUseCase#getController()
     */
    public FrontEndController getController()
    {
        return this.getSuperFrontEndUseCase().getController();
    }

    /**
     * The first view of this use case, this may actually return a view of another use case if the
     * first is found by traveling through the final state.
     * @see FrontEndUseCase#getInitialView()
     */
    public FrontEndView getInitialView()
    {
        return this.getSuperFrontEndUseCase().getInitialView();
    }

    /**
     * The final states linking to this use case
     * @see FrontEndUseCase#getReferencingFinalStates()
     */
    public List<FrontEndFinalState> getReferencingFinalStates()
    {
        return this.getSuperFrontEndUseCase().getReferencingFinalStates();
    }

    /**
     * Returns all roles that are directly and indirectly associated to this use-case.
     * @see FrontEndUseCase#getRoles()
     */
    public List<Role> getRoles()
    {
        return this.getSuperFrontEndUseCase().getRoles();
    }

    /**
     * The variables for all views in this use-case. A parameter qualifies to be a variable when it
     * explicitely and directly receives it via an action.
     * @see FrontEndUseCase#getViewVariables()
     */
    public List<FrontEndParameter> getViewVariables()
    {
        return this.getSuperFrontEndUseCase().getViewVariables();
    }

    /**
     * All views that are part of this use case.
     * @see FrontEndUseCase#getViews()
     */
    public List<FrontEndView> getViews()
    {
        return this.getSuperFrontEndUseCase().getViews();
    }

    /**
     * True if this use-case is the entry point to the front end.
     * @see FrontEndUseCase#isEntryUseCase()
     */
    public boolean isEntryUseCase()
    {
        return this.getSuperFrontEndUseCase().isEntryUseCase();
    }

    /**
     * Indicates if this use case is "secured".  This is true when there is at least one role
     * associated to it.
     * @see FrontEndUseCase#isSecured()
     */
    public boolean isSecured()
    {
        return this.getSuperFrontEndUseCase().isSecured();
    }

    /**
     * Finds the tagged value optional searching the entire inheritance hierarchy if 'follow' is set
     * to true.
     * @see GeneralizableElementFacade#findTaggedValue(String tagName, boolean follow)
     */
    public Object findTaggedValue(String tagName, boolean follow)
    {
        return this.getSuperFrontEndUseCase().findTaggedValue(tagName, follow);
    }

    /**
     * All generalizations for this generalizable element, goes up the inheritance tree.
     * @see GeneralizableElementFacade#getAllGeneralizations()
     */
    public Collection<GeneralizableElementFacade> getAllGeneralizations()
    {
        return this.getSuperFrontEndUseCase().getAllGeneralizations();
    }

    /**
     * All specializations (travels down the inheritance hierarchy).
     * @see GeneralizableElementFacade#getAllSpecializations()
     */
    public Collection<GeneralizableElementFacade> getAllSpecializations()
    {
        return this.getSuperFrontEndUseCase().getAllSpecializations();
    }

    /**
     * Gets the direct generalization for this generalizable element.
     * @see GeneralizableElementFacade#getGeneralization()
     */
    public GeneralizableElementFacade getGeneralization()
    {
        return this.getSuperFrontEndUseCase().getGeneralization();
    }

    /**
     * Gets the actual links that this generalization element is part of (it plays either the
     * specialization or generalization).
     * @see GeneralizableElementFacade#getGeneralizationLinks()
     */
    public Collection<GeneralizationFacade> getGeneralizationLinks()
    {
        return this.getSuperFrontEndUseCase().getGeneralizationLinks();
    }

    /**
     * A comma separated list of the fully qualified names of all generalizations.
     * @see GeneralizableElementFacade#getGeneralizationList()
     */
    public String getGeneralizationList()
    {
        return this.getSuperFrontEndUseCase().getGeneralizationList();
    }

    /**
     * The element found when you recursively follow the generalization path up to the root. If an
     * element has no generalization itself will be considered the root.
     * @see GeneralizableElementFacade#getGeneralizationRoot()
     */
    public GeneralizableElementFacade getGeneralizationRoot()
    {
        return this.getSuperFrontEndUseCase().getGeneralizationRoot();
    }

    /**
     * Return all generalizations (ancestors) from this generalizable element.
     * @see GeneralizableElementFacade#getGeneralizations()
     */
    public Collection<GeneralizableElementFacade> getGeneralizations()
    {
        return this.getSuperFrontEndUseCase().getGeneralizations();
    }

    /**
     * Gets the direct specializations (i.e. sub elements) for this generalizatble element.
     * @see GeneralizableElementFacade#getSpecializations()
     */
    public Collection<GeneralizableElementFacade> getSpecializations()
    {
        return this.getSuperFrontEndUseCase().getSpecializations();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperFrontEndUseCase().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperFrontEndUseCase().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperFrontEndUseCase().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperFrontEndUseCase().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperFrontEndUseCase().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperFrontEndUseCase().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperFrontEndUseCase().getDocumentation(indent);
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
        return this.getSuperFrontEndUseCase().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperFrontEndUseCase().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperFrontEndUseCase().getFullyQualifiedName();
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
        return this.getSuperFrontEndUseCase().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperFrontEndUseCase().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperFrontEndUseCase().getId();
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
        return this.getSuperFrontEndUseCase().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperFrontEndUseCase().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperFrontEndUseCase().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperFrontEndUseCase().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperFrontEndUseCase().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperFrontEndUseCase().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperFrontEndUseCase().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperFrontEndUseCase().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperFrontEndUseCase().getPackagePath();
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
        return this.getSuperFrontEndUseCase().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperFrontEndUseCase().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperFrontEndUseCase().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperFrontEndUseCase().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperFrontEndUseCase().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperFrontEndUseCase().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperFrontEndUseCase().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperFrontEndUseCase().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperFrontEndUseCase().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperFrontEndUseCase().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperFrontEndUseCase().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperFrontEndUseCase().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperFrontEndUseCase().hasKeyword(keywordName);
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
        return this.getSuperFrontEndUseCase().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperFrontEndUseCase().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperFrontEndUseCase().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperFrontEndUseCase().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperFrontEndUseCase().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperFrontEndUseCase().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperFrontEndUseCase().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperFrontEndUseCase().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperFrontEndUseCase().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperFrontEndUseCase().translateConstraints(kind, translation);
    }

    /**
     * Gets the model elements which this namespace owns.
     * @see org.andromda.metafacades.uml.NamespaceFacade#getOwnedElements()
     */
    public Collection<ModelElementFacade> getOwnedElements()
    {
        return this.getSuperFrontEndUseCase().getOwnedElements();
    }

    /**
     * The extend instances related to this use-case.
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtends()
     */
    public Collection<ExtendFacade> getExtends()
    {
        return this.getSuperFrontEndUseCase().getExtends();
    }

    /**
     * The extension points related to this use-case.
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtensionPoints()
     */
    public Collection<ExtensionPointFacade> getExtensionPoints()
    {
        return this.getSuperFrontEndUseCase().getExtensionPoints();
    }

    /**
     * The first activity graph directly owned by this use-case.
     * @see org.andromda.metafacades.uml.UseCaseFacade#getFirstActivityGraph()
     */
    public ActivityGraphFacade getFirstActivityGraph()
    {
        return this.getSuperFrontEndUseCase().getFirstActivityGraph();
    }

    /**
     * The included instances related to this use-case.
     * @see org.andromda.metafacades.uml.UseCaseFacade#getIncludes()
     */
    public Collection<IncludeFacade> getIncludes()
    {
        return this.getSuperFrontEndUseCase().getIncludes();
    }

    /**
     * @see MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperFrontEndUseCase().initialize();
    }

    /**
     * @return Object getSuperFrontEndUseCase().getValidationOwner()
     * @see MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperFrontEndUseCase().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperFrontEndUseCase().getValidationName()
     * @see MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperFrontEndUseCase().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperFrontEndUseCase().validateInvariants(validationMessages);
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