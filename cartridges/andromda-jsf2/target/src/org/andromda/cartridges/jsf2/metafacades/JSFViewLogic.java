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
import org.andromda.metafacades.uml.ActionFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndExceptionHandler;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.PartitionFacade;
import org.andromda.metafacades.uml.StateFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.translation.ocl.validation.OCLCollections;
import org.andromda.translation.ocl.validation.OCLIntrospector;
import org.andromda.translation.ocl.validation.OCLResultEnsurer;
import org.apache.commons.collections.Transformer;
import org.apache.log4j.Logger;

/**
 * Represents a JSF view for a front-end application.
 * MetafacadeLogic for JSFView
 *
 * @see JSFView
 */
public abstract class JSFViewLogic
    extends MetafacadeBase
    implements JSFView
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
    protected JSFViewLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superFrontEndView =
           (FrontEndView)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.FrontEndView",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFViewLogic.class);

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFView if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFView";
        }
        return context;
    }

    private FrontEndView superFrontEndView;
    private boolean superFrontEndViewInitialized = false;

    /**
     * Gets the FrontEndView parent instance.
     * @return this.superFrontEndView FrontEndView
     */
    private FrontEndView getSuperFrontEndView()
    {
        if (!this.superFrontEndViewInitialized)
        {
            ((MetafacadeBase)this.superFrontEndView).setMetafacadeContext(this.getMetafacadeContext());
            this.superFrontEndViewInitialized = true;
        }
        return this.superFrontEndView;
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
            if (this.superFrontEndViewInitialized)
            {
                ((MetafacadeBase)this.superFrontEndView).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFView
     */
    public boolean isJSFViewMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getPath()
    * @return String
    */
    protected abstract String handleGetPath();

    private String __path1a;
    private boolean __path1aSet = false;

    /**
     * The full path of the view resources (i.e. the JSP page).
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
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getTitleKey()
    * @return String
    */
    protected abstract String handleGetTitleKey();

    private String __titleKey2a;
    private boolean __titleKey2aSet = false;

    /**
     * A resource message key suited for the view's title.
     * @return (String)handleGetTitleKey()
     */
    public final String getTitleKey()
    {
        String titleKey2a = this.__titleKey2a;
        if (!this.__titleKey2aSet)
        {
            // titleKey has no pre constraints
            titleKey2a = handleGetTitleKey();
            // titleKey has no post constraints
            this.__titleKey2a = titleKey2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__titleKey2aSet = true;
            }
        }
        return titleKey2a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getTitleValue()
    * @return String
    */
    protected abstract String handleGetTitleValue();

    private String __titleValue3a;
    private boolean __titleValue3aSet = false;

    /**
     * A default resource message value suited for the page's title.
     * @return (String)handleGetTitleValue()
     */
    public final String getTitleValue()
    {
        String titleValue3a = this.__titleValue3a;
        if (!this.__titleValue3aSet)
        {
            // titleValue has no pre constraints
            titleValue3a = handleGetTitleValue();
            // titleValue has no post constraints
            this.__titleValue3a = titleValue3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__titleValue3aSet = true;
            }
        }
        return titleValue3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey4a;
    private boolean __messageKey4aSet = false;

    /**
     * The default resource message key for this view.
     * @return (String)handleGetMessageKey()
     */
    public final String getMessageKey()
    {
        String messageKey4a = this.__messageKey4a;
        if (!this.__messageKey4aSet)
        {
            // messageKey has no pre constraints
            messageKey4a = handleGetMessageKey();
            // messageKey has no post constraints
            this.__messageKey4a = messageKey4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageKey4aSet = true;
            }
        }
        return messageKey4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getDocumentationKey()
    * @return String
    */
    protected abstract String handleGetDocumentationKey();

    private String __documentationKey5a;
    private boolean __documentationKey5aSet = false;

    /**
     * A resource message key suited for the page's documentation.
     * @return (String)handleGetDocumentationKey()
     */
    public final String getDocumentationKey()
    {
        String documentationKey5a = this.__documentationKey5a;
        if (!this.__documentationKey5aSet)
        {
            // documentationKey has no pre constraints
            documentationKey5a = handleGetDocumentationKey();
            // documentationKey has no post constraints
            this.__documentationKey5a = documentationKey5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__documentationKey5aSet = true;
            }
        }
        return documentationKey5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getDocumentationValue()
    * @return String
    */
    protected abstract String handleGetDocumentationValue();

    private String __documentationValue6a;
    private boolean __documentationValue6aSet = false;

    /**
     * A resource message value suited for the view's documentation.
     * @return (String)handleGetDocumentationValue()
     */
    public final String getDocumentationValue()
    {
        String documentationValue6a = this.__documentationValue6a;
        if (!this.__documentationValue6aSet)
        {
            // documentationValue has no pre constraints
            documentationValue6a = handleGetDocumentationValue();
            // documentationValue has no post constraints
            this.__documentationValue6a = documentationValue6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__documentationValue6aSet = true;
            }
        }
        return documentationValue6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getMessageValue()
    * @return String
    */
    protected abstract String handleGetMessageValue();

    private String __messageValue7a;
    private boolean __messageValue7aSet = false;

    /**
     * A displayable version of this view's name.
     * @return (String)handleGetMessageValue()
     */
    public final String getMessageValue()
    {
        String messageValue7a = this.__messageValue7a;
        if (!this.__messageValue7aSet)
        {
            // messageValue has no pre constraints
            messageValue7a = handleGetMessageValue();
            // messageValue has no post constraints
            this.__messageValue7a = messageValue7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageValue7aSet = true;
            }
        }
        return messageValue7a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getFullyQualifiedPopulator()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedPopulator();

    private String __fullyQualifiedPopulator8a;
    private boolean __fullyQualifiedPopulator8aSet = false;

    /**
     * The fully qualified name of this view's form populator.
     * @return (String)handleGetFullyQualifiedPopulator()
     */
    public final String getFullyQualifiedPopulator()
    {
        String fullyQualifiedPopulator8a = this.__fullyQualifiedPopulator8a;
        if (!this.__fullyQualifiedPopulator8aSet)
        {
            // fullyQualifiedPopulator has no pre constraints
            fullyQualifiedPopulator8a = handleGetFullyQualifiedPopulator();
            // fullyQualifiedPopulator has no post constraints
            this.__fullyQualifiedPopulator8a = fullyQualifiedPopulator8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedPopulator8aSet = true;
            }
        }
        return fullyQualifiedPopulator8a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getPopulator()
    * @return String
    */
    protected abstract String handleGetPopulator();

    private String __populator9a;
    private boolean __populator9aSet = false;

    /**
     * The name of the form populator for this view.
     * @return (String)handleGetPopulator()
     */
    public final String getPopulator()
    {
        String populator9a = this.__populator9a;
        if (!this.__populator9aSet)
        {
            // populator has no pre constraints
            populator9a = handleGetPopulator();
            // populator has no post constraints
            this.__populator9a = populator9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__populator9aSet = true;
            }
        }
        return populator9a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getPopulatorPath()
    * @return String
    */
    protected abstract String handleGetPopulatorPath();

    private String __populatorPath10a;
    private boolean __populatorPath10aSet = false;

    /**
     * The path to the form populator.
     * @return (String)handleGetPopulatorPath()
     */
    public final String getPopulatorPath()
    {
        String populatorPath10a = this.__populatorPath10a;
        if (!this.__populatorPath10aSet)
        {
            // populatorPath has no pre constraints
            populatorPath10a = handleGetPopulatorPath();
            // populatorPath has no post constraints
            this.__populatorPath10a = populatorPath10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__populatorPath10aSet = true;
            }
        }
        return populatorPath10a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#isPopulatorRequired()
    * @return boolean
    */
    protected abstract boolean handleIsPopulatorRequired();

    private boolean __populatorRequired11a;
    private boolean __populatorRequired11aSet = false;

    /**
     * Indicates if a populator is required for this view.
     * @return (boolean)handleIsPopulatorRequired()
     */
    public final boolean isPopulatorRequired()
    {
        boolean populatorRequired11a = this.__populatorRequired11a;
        if (!this.__populatorRequired11aSet)
        {
            // populatorRequired has no pre constraints
            populatorRequired11a = handleIsPopulatorRequired();
            // populatorRequired has no post constraints
            this.__populatorRequired11a = populatorRequired11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__populatorRequired11aSet = true;
            }
        }
        return populatorRequired11a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired12a;
    private boolean __validationRequired12aSet = false;

    /**
     * Indicates whether or not at least one parameter of an outgoing action in this view requires
     * validation.
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired12a = this.__validationRequired12a;
        if (!this.__validationRequired12aSet)
        {
            // validationRequired has no pre constraints
            validationRequired12a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired12a = validationRequired12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired12aSet = true;
            }
        }
        return validationRequired12a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#isPopup()
    * @return boolean
    */
    protected abstract boolean handleIsPopup();

    private boolean __popup13a;
    private boolean __popup13aSet = false;

    /**
     * Indicates if this view represents a popup.
     * @return (boolean)handleIsPopup()
     */
    public final boolean isPopup()
    {
        boolean popup13a = this.__popup13a;
        if (!this.__popup13aSet)
        {
            // popup has no pre constraints
            popup13a = handleIsPopup();
            // popup has no post constraints
            this.__popup13a = popup13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__popup13aSet = true;
            }
        }
        return popup13a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#isNonTableVariablesPresent()
    * @return boolean
    */
    protected abstract boolean handleIsNonTableVariablesPresent();

    private boolean __nonTableVariablesPresent14a;
    private boolean __nonTableVariablesPresent14aSet = false;

    /**
     * Indicates whether or not any non-table view variables are present in this view.
     * @return (boolean)handleIsNonTableVariablesPresent()
     */
    public final boolean isNonTableVariablesPresent()
    {
        boolean nonTableVariablesPresent14a = this.__nonTableVariablesPresent14a;
        if (!this.__nonTableVariablesPresent14aSet)
        {
            // nonTableVariablesPresent has no pre constraints
            nonTableVariablesPresent14a = handleIsNonTableVariablesPresent();
            // nonTableVariablesPresent has no post constraints
            this.__nonTableVariablesPresent14a = nonTableVariablesPresent14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__nonTableVariablesPresent14aSet = true;
            }
        }
        return nonTableVariablesPresent14a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#isHasNameOfUseCase()
    * @return boolean
    */
    protected abstract boolean handleIsHasNameOfUseCase();

    private boolean __hasNameOfUseCase15a;
    private boolean __hasNameOfUseCase15aSet = false;

    /**
     * Indicates whether or not this view has the same name as the use case in which it is
     * contained.
     * @return (boolean)handleIsHasNameOfUseCase()
     */
    public final boolean isHasNameOfUseCase()
    {
        boolean hasNameOfUseCase15a = this.__hasNameOfUseCase15a;
        if (!this.__hasNameOfUseCase15aSet)
        {
            // hasNameOfUseCase has no pre constraints
            hasNameOfUseCase15a = handleIsHasNameOfUseCase();
            // hasNameOfUseCase has no post constraints
            this.__hasNameOfUseCase15a = hasNameOfUseCase15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__hasNameOfUseCase15aSet = true;
            }
        }
        return hasNameOfUseCase15a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getFormKey()
    * @return String
    */
    protected abstract String handleGetFormKey();

    private String __formKey16a;
    private boolean __formKey16aSet = false;

    /**
     * The key that stores the form in which information is passed from one action to another.
     * @return (String)handleGetFormKey()
     */
    public final String getFormKey()
    {
        String formKey16a = this.__formKey16a;
        if (!this.__formKey16aSet)
        {
            // formKey has no pre constraints
            formKey16a = handleGetFormKey();
            // formKey has no post constraints
            this.__formKey16a = formKey16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formKey16aSet = true;
            }
        }
        return formKey16a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getFromOutcome()
    * @return String
    */
    protected abstract String handleGetFromOutcome();

    private String __fromOutcome17a;
    private boolean __fromOutcome17aSet = false;

    /**
     * The name that corresponds to the from-outcome in an navigational rule.
     * @return (String)handleGetFromOutcome()
     */
    public final String getFromOutcome()
    {
        String fromOutcome17a = this.__fromOutcome17a;
        if (!this.__fromOutcome17aSet)
        {
            // fromOutcome has no pre constraints
            fromOutcome17a = handleGetFromOutcome();
            // fromOutcome has no post constraints
            this.__fromOutcome17a = fromOutcome17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fromOutcome17aSet = true;
            }
        }
        return fromOutcome17a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#isNeedsFileUpload()
    * @return boolean
    */
    protected abstract boolean handleIsNeedsFileUpload();

    private boolean __needsFileUpload18a;
    private boolean __needsFileUpload18aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.needsFileUpload
     * @return (boolean)handleIsNeedsFileUpload()
     */
    public final boolean isNeedsFileUpload()
    {
        boolean needsFileUpload18a = this.__needsFileUpload18a;
        if (!this.__needsFileUpload18aSet)
        {
            // needsFileUpload has no pre constraints
            needsFileUpload18a = handleIsNeedsFileUpload();
            // needsFileUpload has no post constraints
            this.__needsFileUpload18a = needsFileUpload18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__needsFileUpload18aSet = true;
            }
        }
        return needsFileUpload18a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getFullyQualifiedPageObjectClassPath()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedPageObjectClassPath();

    private String __fullyQualifiedPageObjectClassPath19a;
    private boolean __fullyQualifiedPageObjectClassPath19aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.fullyQualifiedPageObjectClassPath
     * @return (String)handleGetFullyQualifiedPageObjectClassPath()
     */
    public final String getFullyQualifiedPageObjectClassPath()
    {
        String fullyQualifiedPageObjectClassPath19a = this.__fullyQualifiedPageObjectClassPath19a;
        if (!this.__fullyQualifiedPageObjectClassPath19aSet)
        {
            // fullyQualifiedPageObjectClassPath has no pre constraints
            fullyQualifiedPageObjectClassPath19a = handleGetFullyQualifiedPageObjectClassPath();
            // fullyQualifiedPageObjectClassPath has no post constraints
            this.__fullyQualifiedPageObjectClassPath19a = fullyQualifiedPageObjectClassPath19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedPageObjectClassPath19aSet = true;
            }
        }
        return fullyQualifiedPageObjectClassPath19a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getPageObjectClassName()
    * @return String
    */
    protected abstract String handleGetPageObjectClassName();

    private String __pageObjectClassName20a;
    private boolean __pageObjectClassName20aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.pageObjectClassName
     * @return (String)handleGetPageObjectClassName()
     */
    public final String getPageObjectClassName()
    {
        String pageObjectClassName20a = this.__pageObjectClassName20a;
        if (!this.__pageObjectClassName20aSet)
        {
            // pageObjectClassName has no pre constraints
            pageObjectClassName20a = handleGetPageObjectClassName();
            // pageObjectClassName has no post constraints
            this.__pageObjectClassName20a = pageObjectClassName20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__pageObjectClassName20aSet = true;
            }
        }
        return pageObjectClassName20a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getPageObjectBeanName()
    * @return String
    */
    protected abstract String handleGetPageObjectBeanName();

    private String __pageObjectBeanName21a;
    private boolean __pageObjectBeanName21aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.pageObjectBeanName
     * @return (String)handleGetPageObjectBeanName()
     */
    public final String getPageObjectBeanName()
    {
        String pageObjectBeanName21a = this.__pageObjectBeanName21a;
        if (!this.__pageObjectBeanName21aSet)
        {
            // pageObjectBeanName has no pre constraints
            pageObjectBeanName21a = handleGetPageObjectBeanName();
            // pageObjectBeanName has no post constraints
            this.__pageObjectBeanName21a = pageObjectBeanName21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__pageObjectBeanName21aSet = true;
            }
        }
        return pageObjectBeanName21a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFView#getFullyQualifiedPageObjectClassName()
    * @return String
    */
    protected abstract String handleGetFullyQualifiedPageObjectClassName();

    private String __fullyQualifiedPageObjectClassName22a;
    private boolean __fullyQualifiedPageObjectClassName22aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.fullyQualifiedPageObjectClassName
     * @return (String)handleGetFullyQualifiedPageObjectClassName()
     */
    public final String getFullyQualifiedPageObjectClassName()
    {
        String fullyQualifiedPageObjectClassName22a = this.__fullyQualifiedPageObjectClassName22a;
        if (!this.__fullyQualifiedPageObjectClassName22aSet)
        {
            // fullyQualifiedPageObjectClassName has no pre constraints
            fullyQualifiedPageObjectClassName22a = handleGetFullyQualifiedPageObjectClassName();
            // fullyQualifiedPageObjectClassName has no post constraints
            this.__fullyQualifiedPageObjectClassName22a = fullyQualifiedPageObjectClassName22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fullyQualifiedPageObjectClassName22aSet = true;
            }
        }
        return fullyQualifiedPageObjectClassName22a;
    }

    // ------------- associations ------------------

    private List<JSFForward> __getForwards1r;
    private boolean __getForwards1rSet = false;

    /**
     * Represents a JSF view for a front-end application.
     * @return (List<JSFForward>)handleGetForwards()
     */
    public final List<JSFForward> getForwards()
    {
        List<JSFForward> getForwards1r = this.__getForwards1r;
        if (!this.__getForwards1rSet)
        {
            // jSFView has no pre constraints
            List result = handleGetForwards();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getForwards1r = (List<JSFForward>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFViewLogic.logger.warn("incorrect metafacade cast for JSFViewLogic.getForwards List<JSFForward> " + result + ": " + shieldedResult);
            }
            // jSFView has no post constraints
            this.__getForwards1r = getForwards1r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getForwards1rSet = true;
            }
        }
        return getForwards1r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetForwards();

    /**
     * Represents a JSF view for a front-end application.
     * @return (List<JSFParameter>)handleGetBackingValueVariables()
     */
    public final List<JSFParameter> getBackingValueVariables()
    {
        List<JSFParameter> getBackingValueVariables2r = null;
        // jSFView has no pre constraints
        List result = handleGetBackingValueVariables();
        List shieldedResult = this.shieldedElements(result);
        try
        {
            getBackingValueVariables2r = (List<JSFParameter>)shieldedResult;
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            JSFViewLogic.logger.warn("incorrect metafacade cast for JSFViewLogic.getBackingValueVariables List<JSFParameter> " + result + ": " + shieldedResult);
        }
        // jSFView has no post constraints
        return getBackingValueVariables2r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetBackingValueVariables();

    private List<JSFAction> __getFormActions3r;
    private boolean __getFormActions3rSet = false;

    /**
     * Represents a JSF view for a front-end application.
     * @return (List<JSFAction>)handleGetFormActions()
     */
    public final List<JSFAction> getFormActions()
    {
        List<JSFAction> getFormActions3r = this.__getFormActions3r;
        if (!this.__getFormActions3rSet)
        {
            // jSFView has no pre constraints
            List result = handleGetFormActions();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getFormActions3r = (List<JSFAction>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFViewLogic.logger.warn("incorrect metafacade cast for JSFViewLogic.getFormActions List<JSFAction> " + result + ": " + shieldedResult);
            }
            // jSFView has no post constraints
            this.__getFormActions3r = getFormActions3r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getFormActions3rSet = true;
            }
        }
        return getFormActions3r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetFormActions();

    private List<JSFAction> __getActionForwards4r;
    private boolean __getActionForwards4rSet = false;

    /**
     * Represents a JSF view for a front-end application.
     * @return (List<JSFAction>)handleGetActionForwards()
     */
    public final List<JSFAction> getActionForwards()
    {
        List<JSFAction> getActionForwards4r = this.__getActionForwards4r;
        if (!this.__getActionForwards4rSet)
        {
            // jSFView has no pre constraints
            List result = handleGetActionForwards();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getActionForwards4r = (List<JSFAction>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFViewLogic.logger.warn("incorrect metafacade cast for JSFViewLogic.getActionForwards List<JSFAction> " + result + ": " + shieldedResult);
            }
            // jSFView has no post constraints
            this.__getActionForwards4r = getActionForwards4r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getActionForwards4rSet = true;
            }
        }
        return getActionForwards4r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetActionForwards();

    /**
     * @return true
     * @see FrontEndView
     */
    public boolean isFrontEndViewMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.FrontEndActionState
     */
    public boolean isFrontEndActionStateMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.ActionStateFacade
     */
    public boolean isActionStateFacadeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see StateFacade
     */
    public boolean isStateFacadeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.StateVertexFacade
     */
    public boolean isStateVertexFacadeMetaType()
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

    // ----------- delegates to FrontEndView ------------
    /**
     * The entry action for this action state (if any).
     * @see org.andromda.metafacades.uml.ActionStateFacade#getEntry()
     */
    public ActionFacade getEntry()
    {
        return this.getSuperFrontEndView().getEntry();
    }

    /**
     * The method name representing this action state.
     * @see org.andromda.metafacades.uml.FrontEndActionState#getActionMethodName()
     */
    public String getActionMethodName()
    {
        return this.getSuperFrontEndView().getActionMethodName();
    }

    /**
     * The actions that pass through this action state.
     * @see org.andromda.metafacades.uml.FrontEndActionState#getContainerActions()
     */
    public List<FrontEndAction> getContainerActions()
    {
        return this.getSuperFrontEndView().getContainerActions();
    }

    /**
     * All calls deferred to the controller by this action state.
     * @see org.andromda.metafacades.uml.FrontEndActionState#getControllerCalls()
     */
    public List<OperationFacade> getControllerCalls()
    {
        return this.getSuperFrontEndView().getControllerCalls();
    }

    /**
     * All exceptions modelled on this action state.
     * @see org.andromda.metafacades.uml.FrontEndActionState#getExceptions()
     */
    public List<FrontEndExceptionHandler> getExceptions()
    {
        return this.getSuperFrontEndView().getExceptions();
    }

    /**
     * The next transition, there can be only one transition going out of an action state, otherwise
     * decision points should be used (triggers are not supported at the server-side).
     * @see org.andromda.metafacades.uml.FrontEndActionState#getForward()
     */
    public FrontEndForward getForward()
    {
        return this.getSuperFrontEndView().getForward();
    }

    /**
     * All calls deferred to the services by this action state.
     * @see org.andromda.metafacades.uml.FrontEndActionState#getServiceCalls()
     */
    public List<OperationFacade> getServiceCalls()
    {
        return this.getSuperFrontEndView().getServiceCalls();
    }

    /**
     * True if this element is contained in a FrontEndUseCase.
     * @see org.andromda.metafacades.uml.FrontEndActionState#isContainedInFrontEndUseCase()
     */
    public boolean isContainedInFrontEndUseCase()
    {
        return this.getSuperFrontEndView().isContainedInFrontEndUseCase();
    }

    /**
     * Indicates whether or not this front end action state is server side. Pages, for example, are
     * also action states but they return control to the client.
     * @see org.andromda.metafacades.uml.FrontEndActionState#isServerSide()
     */
    public boolean isServerSide()
    {
        return this.getSuperFrontEndView().isServerSide();
    }

    /**
     * All actions that can be triggered on this view.
     * @see FrontEndView#getActions()
     */
    public List<FrontEndAction> getActions()
    {
        return this.getSuperFrontEndView().getActions();
    }

    /**
     * All parameters for each action going out of this view.
     * @see FrontEndView#getAllActionParameters()
     */
    public List<FrontEndParameter> getAllActionParameters()
    {
        return this.getSuperFrontEndView().getAllActionParameters();
    }

    /**
     * All fields from all forms on the given view.
     * @see FrontEndView#getAllFormFields()
     */
    public List<FrontEndParameter> getAllFormFields()
    {
        return this.getSuperFrontEndView().getAllFormFields();
    }

    /**
     * All tables belonging to the front end view.
     * @see FrontEndView#getTables()
     */
    public List<FrontEndParameter> getTables()
    {
        return this.getSuperFrontEndView().getTables();
    }

    /**
     * The use-case of which this view is a member.
     * @see FrontEndView#getUseCase()
     */
    public FrontEndUseCase getUseCase()
    {
        return this.getSuperFrontEndView().getUseCase();
    }

    /**
     * All those variables that will be present as variables in the target view. These are the
     * trigger parameters on the incoming transitions.
     * @see FrontEndView#getVariables()
     */
    public List<FrontEndParameter> getVariables()
    {
        return this.getSuperFrontEndView().getVariables();
    }

    /**
     * True if this element carries the FrontEndView stereotype.
     * @see FrontEndView#isFrontEndView()
     */
    public boolean isFrontEndView()
    {
        return this.getSuperFrontEndView().isFrontEndView();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperFrontEndView().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperFrontEndView().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperFrontEndView().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperFrontEndView().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperFrontEndView().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperFrontEndView().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperFrontEndView().getDocumentation(indent);
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
        return this.getSuperFrontEndView().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperFrontEndView().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperFrontEndView().getFullyQualifiedName();
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
        return this.getSuperFrontEndView().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperFrontEndView().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperFrontEndView().getId();
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
        return this.getSuperFrontEndView().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperFrontEndView().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperFrontEndView().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperFrontEndView().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperFrontEndView().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperFrontEndView().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperFrontEndView().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperFrontEndView().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperFrontEndView().getPackagePath();
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
        return this.getSuperFrontEndView().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperFrontEndView().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperFrontEndView().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperFrontEndView().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperFrontEndView().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperFrontEndView().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperFrontEndView().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperFrontEndView().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperFrontEndView().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperFrontEndView().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperFrontEndView().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperFrontEndView().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperFrontEndView().hasKeyword(keywordName);
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
        return this.getSuperFrontEndView().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperFrontEndView().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperFrontEndView().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperFrontEndView().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperFrontEndView().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperFrontEndView().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperFrontEndView().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperFrontEndView().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperFrontEndView().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperFrontEndView().translateConstraints(kind, translation);
    }

    /**
     * Events to which is being deferred in this action state.
     * @see StateFacade#getDeferrableEvents()
     */
    public Collection<EventFacade> getDeferrableEvents()
    {
        return this.getSuperFrontEndView().getDeferrableEvents();
    }

    /**
     * Models a situation during which some (usually implicit) invariant condition holds. The states
     * of
     * protocol state machines are exposed to the users of their context classifiers. A protocol
     * state
     * represents an exposed stable situation of its context classifier: when an instance of the
     * classifier
     * is not processing any operation, users of this instance can always know its state
     * configuration.
     * @see org.andromda.metafacades.uml.StateVertexFacade#getContainer()
     */
    public StateFacade getContainer()
    {
        return this.getSuperFrontEndView().getContainer();
    }

    /**
     * A directed relationship between a source vertex and a target vertex. It may be part of a
     * compound
     * transition, which takes the state machine from one state configuration to another,
     * representing the
     * complete response of the state machine to an occurrence of an event of a particular type.
     * @see org.andromda.metafacades.uml.StateVertexFacade#getIncomings()
     */
    public Collection<TransitionFacade> getIncomings()
    {
        return this.getSuperFrontEndView().getIncomings();
    }

    /**
     * A directed relationship between a source vertex and a target vertex. It may be part of a
     * compound
     * transition, which takes the state machine from one state configuration to another,
     * representing the
     * complete response of the state machine to an occurrence of an event of a particular type.
     * @see org.andromda.metafacades.uml.StateVertexFacade#getOutgoings()
     */
    public Collection<TransitionFacade> getOutgoings()
    {
        return this.getSuperFrontEndView().getOutgoings();
    }

    /**
     * The partition (if any) to which this vertex belongs.
     * @see org.andromda.metafacades.uml.StateVertexFacade#getPartition()
     */
    public PartitionFacade getPartition()
    {
        return this.getSuperFrontEndView().getPartition();
    }

    /**
     * State machines can be used to express the behavior of part of a system. Behavior is modeled
     * as a
     * traversal of a graph of state nodes interconnected by one or more joined transition arcs that
     * are
     * triggered by the dispatching of series of (event) occurrences. During this traversal, the
     * state
     * machine executes a series of activities associated with various elements of the state
     * machine.
     * @see org.andromda.metafacades.uml.StateVertexFacade#getStateMachine()
     */
    public StateMachineFacade getStateMachine()
    {
        return this.getSuperFrontEndView().getStateMachine();
    }

    /**
     * @see MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperFrontEndView().initialize();
    }

    /**
     * @return Object getSuperFrontEndView().getValidationOwner()
     * @see MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperFrontEndView().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperFrontEndView().getValidationName()
     * @see MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperFrontEndView().getValidationName();
        return name;
    }

    /**
     * <p><b>Constraint:</b> org::andromda::cartridges::jsf2::metafacades::JSFView::duplicate view action names not allowed</p>
     * <p><b>Error:</b> Each view must contain actions which each have a unique name, this view has actions with duplicate names or names that cause an action name to be duplicated in faces-config.xml.</p>
     * <p><b>OCL:</b> context JSFView inv: actions->isUnique(name)</p>
     * @param validationMessages Collection<ModelValidationMessage>
     * @see MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperFrontEndView().validateInvariants(validationMessages);
        try
        {
            final Object contextElement = this.THIS();
            boolean constraintValid = OCLResultEnsurer.ensure(OCLCollections.isUnique(OCLIntrospector.invoke(contextElement,"actions"),new Transformer(){public Object transform(Object object){return OCLIntrospector.invoke(object,"name");}}));
            if (!constraintValid)
            {
                validationMessages.add(
                    new ModelValidationMessage(
                        (MetafacadeBase)contextElement ,
                        "org::andromda::cartridges::jsf2::metafacades::JSFView::duplicate view action names not allowed",
                        "Each view must contain actions which each have a unique name, this view has actions with duplicate names or names that cause an action name to be duplicated in faces-config.xml."));
            }
        }
        catch (Throwable th)
        {
            Throwable cause = th.getCause();
            int depth = 0; // Some throwables have infinite recursion
            while (cause != null && depth < 7)
            {
                th = cause;
                depth++;
            }
            logger.error("Error validating constraint 'org::andromda::cartridges::jsf2::metafacades::JSFView::duplicate view action names not allowed' ON "
                + this.THIS().toString() + ": " + th.getMessage(), th);
        }
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