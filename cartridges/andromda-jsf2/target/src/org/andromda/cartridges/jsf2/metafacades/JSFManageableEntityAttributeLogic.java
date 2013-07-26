// license-header java merge-point
//
// Attention: generated code (by MetafacadeLogic.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;

/**
 * TODO: Model Documentation for
 * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute
 * MetafacadeLogic for JSFManageableEntityAttribute
 *
 * @see JSFManageableEntityAttribute
 */
public abstract class JSFManageableEntityAttributeLogic
    extends MetafacadeBase
    implements JSFManageableEntityAttribute
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
    protected JSFManageableEntityAttributeLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superManageableEntityAttribute =
           (ManageableEntityAttribute)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.ManageableEntityAttribute",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFManageableEntityAttribute if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute";
        }
        return context;
    }

    private ManageableEntityAttribute superManageableEntityAttribute;
    private boolean superManageableEntityAttributeInitialized = false;

    /**
     * Gets the ManageableEntityAttribute parent instance.
     * @return this.superManageableEntityAttribute ManageableEntityAttribute
     */
    private ManageableEntityAttribute getSuperManageableEntityAttribute()
    {
        if (!this.superManageableEntityAttributeInitialized)
        {
            ((MetafacadeBase)this.superManageableEntityAttribute).setMetafacadeContext(this.getMetafacadeContext());
            this.superManageableEntityAttributeInitialized = true;
        }
        return this.superManageableEntityAttribute;
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
            if (this.superManageableEntityAttributeInitialized)
            {
                ((MetafacadeBase)this.superManageableEntityAttribute).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFManageableEntityAttribute
     */
    public boolean isJSFManageableEntityAttributeMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey1a;
    private boolean __messageKey1aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.messageKey
     * @return (String)handleGetMessageKey()
     */
    public final String getMessageKey()
    {
        String messageKey1a = this.__messageKey1a;
        if (!this.__messageKey1aSet)
        {
            // messageKey has no pre constraints
            messageKey1a = handleGetMessageKey();
            // messageKey has no post constraints
            this.__messageKey1a = messageKey1a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageKey1aSet = true;
            }
        }
        return messageKey1a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getMessageValue()
    * @return String
    */
    protected abstract String handleGetMessageValue();

    private String __messageValue2a;
    private boolean __messageValue2aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.messageValue
     * @return (String)handleGetMessageValue()
     */
    public final String getMessageValue()
    {
        String messageValue2a = this.__messageValue2a;
        if (!this.__messageValue2aSet)
        {
            // messageValue has no pre constraints
            messageValue2a = handleGetMessageValue();
            // messageValue has no post constraints
            this.__messageValue2a = messageValue2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageValue2aSet = true;
            }
        }
        return messageValue2a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getDateFormat()
    * @return String
    */
    protected abstract String handleGetDateFormat();

    private String __dateFormat3a;
    private boolean __dateFormat3aSet = false;

    /**
     * The String format to use when referring to this date, only makes sense when the type is a
     * date type.
     * @return (String)handleGetDateFormat()
     */
    public final String getDateFormat()
    {
        String dateFormat3a = this.__dateFormat3a;
        if (!this.__dateFormat3aSet)
        {
            // dateFormat has no pre constraints
            dateFormat3a = handleGetDateFormat();
            // dateFormat has no post constraints
            this.__dateFormat3a = dateFormat3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dateFormat3aSet = true;
            }
        }
        return dateFormat3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isNeedsFileUpload()
    * @return boolean
    */
    protected abstract boolean handleIsNeedsFileUpload();

    private boolean __needsFileUpload4a;
    private boolean __needsFileUpload4aSet = false;

    /**
     * True if this attribute is of a type that cannot easily be represented as a textual string and
     * would be an ideal candidate for HTTP's support for file-upload.
     * @return (boolean)handleIsNeedsFileUpload()
     */
    public final boolean isNeedsFileUpload()
    {
        boolean needsFileUpload4a = this.__needsFileUpload4a;
        if (!this.__needsFileUpload4aSet)
        {
            // needsFileUpload has no pre constraints
            needsFileUpload4a = handleIsNeedsFileUpload();
            // needsFileUpload has no post constraints
            this.__needsFileUpload4a = needsFileUpload4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__needsFileUpload4aSet = true;
            }
        }
        return needsFileUpload4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isHidden()
    * @return boolean
    */
    protected abstract boolean handleIsHidden();

    private boolean __hidden5a;
    private boolean __hidden5aSet = false;

    /**
     * Whether or not this attribute should be hidden from the view
     * @return (boolean)handleIsHidden()
     */
    public final boolean isHidden()
    {
        boolean hidden5a = this.__hidden5a;
        if (!this.__hidden5aSet)
        {
            // hidden has no pre constraints
            hidden5a = handleIsHidden();
            // hidden has no post constraints
            this.__hidden5a = hidden5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__hidden5aSet = true;
            }
        }
        return hidden5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getWidgetType()
    * @return String
    */
    protected abstract String handleGetWidgetType();

    private String __widgetType6a;
    private boolean __widgetType6aSet = false;

    /**
     * The widget to use when rendering this attribute
     * @return (String)handleGetWidgetType()
     */
    public final String getWidgetType()
    {
        String widgetType6a = this.__widgetType6a;
        if (!this.__widgetType6aSet)
        {
            // widgetType has no pre constraints
            widgetType6a = handleGetWidgetType();
            // widgetType has no post constraints
            this.__widgetType6a = widgetType6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__widgetType6aSet = true;
            }
        }
        return widgetType6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isStrictDateFormat()
    * @return boolean
    */
    protected abstract boolean handleIsStrictDateFormat();

    private boolean __strictDateFormat7a;
    private boolean __strictDateFormat7aSet = false;

    /**
     * True if this field is a date type and the date format is not be interpreted strictly.
     * @return (boolean)handleIsStrictDateFormat()
     */
    public final boolean isStrictDateFormat()
    {
        boolean strictDateFormat7a = this.__strictDateFormat7a;
        if (!this.__strictDateFormat7aSet)
        {
            // strictDateFormat has no pre constraints
            strictDateFormat7a = handleIsStrictDateFormat();
            // strictDateFormat has no post constraints
            this.__strictDateFormat7a = strictDateFormat7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__strictDateFormat7aSet = true;
            }
        }
        return strictDateFormat7a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getOnlineHelpKey()
    * @return String
    */
    protected abstract String handleGetOnlineHelpKey();

    private String __onlineHelpKey8a;
    private boolean __onlineHelpKey8aSet = false;

    /**
     * The key to lookup the online help documentation. This documentation is gathered from the
     * documentation entered by the user, as well as analyzing the model.
     * @return (String)handleGetOnlineHelpKey()
     */
    public final String getOnlineHelpKey()
    {
        String onlineHelpKey8a = this.__onlineHelpKey8a;
        if (!this.__onlineHelpKey8aSet)
        {
            // onlineHelpKey has no pre constraints
            onlineHelpKey8a = handleGetOnlineHelpKey();
            // onlineHelpKey has no post constraints
            this.__onlineHelpKey8a = onlineHelpKey8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpKey8aSet = true;
            }
        }
        return onlineHelpKey8a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getOnlineHelpValue()
    * @return String
    */
    protected abstract String handleGetOnlineHelpValue();

    private String __onlineHelpValue9a;
    private boolean __onlineHelpValue9aSet = false;

    /**
     * The online help documentation. This documentation is gathered from the documentation entered
     * by the user, as well as analyzing the model. The format is HTML without any style.
     * @return (String)handleGetOnlineHelpValue()
     */
    public final String getOnlineHelpValue()
    {
        String onlineHelpValue9a = this.__onlineHelpValue9a;
        if (!this.__onlineHelpValue9aSet)
        {
            // onlineHelpValue has no pre constraints
            onlineHelpValue9a = handleGetOnlineHelpValue();
            // onlineHelpValue has no post constraints
            this.__onlineHelpValue9a = onlineHelpValue9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpValue9aSet = true;
            }
        }
        return onlineHelpValue9a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getFormat()
    * @return String
    */
    protected abstract String handleGetFormat();

    private String __format10a;
    private boolean __format10aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.format
     * @return (String)handleGetFormat()
     */
    public final String getFormat()
    {
        String format10a = this.__format10a;
        if (!this.__format10aSet)
        {
            // format has no pre constraints
            format10a = handleGetFormat();
            // format has no post constraints
            this.__format10a = format10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__format10aSet = true;
            }
        }
        return format10a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getDefaultDateFormat()
    * @return String
    */
    protected abstract String handleGetDefaultDateFormat();

    private String __defaultDateFormat11a;
    private boolean __defaultDateFormat11aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.defaultDateFormat
     * @return (String)handleGetDefaultDateFormat()
     */
    public final String getDefaultDateFormat()
    {
        String defaultDateFormat11a = this.__defaultDateFormat11a;
        if (!this.__defaultDateFormat11aSet)
        {
            // defaultDateFormat has no pre constraints
            defaultDateFormat11a = handleGetDefaultDateFormat();
            // defaultDateFormat has no post constraints
            this.__defaultDateFormat11a = defaultDateFormat11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__defaultDateFormat11aSet = true;
            }
        }
        return defaultDateFormat11a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getDefaultTimeFormat()
    * @return String
    */
    protected abstract String handleGetDefaultTimeFormat();

    private String __defaultTimeFormat12a;
    private boolean __defaultTimeFormat12aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.defaultTimeFormat
     * @return (String)handleGetDefaultTimeFormat()
     */
    public final String getDefaultTimeFormat()
    {
        String defaultTimeFormat12a = this.__defaultTimeFormat12a;
        if (!this.__defaultTimeFormat12aSet)
        {
            // defaultTimeFormat has no pre constraints
            defaultTimeFormat12a = handleGetDefaultTimeFormat();
            // defaultTimeFormat has no post constraints
            this.__defaultTimeFormat12a = defaultTimeFormat12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__defaultTimeFormat12aSet = true;
            }
        }
        return defaultTimeFormat12a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getDateFormatter()
    * @return String
    */
    protected abstract String handleGetDateFormatter();

    private String __dateFormatter13a;
    private boolean __dateFormatter13aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.dateFormatter
     * @return (String)handleGetDateFormatter()
     */
    public final String getDateFormatter()
    {
        String dateFormatter13a = this.__dateFormatter13a;
        if (!this.__dateFormatter13aSet)
        {
            // dateFormatter has no pre constraints
            dateFormatter13a = handleGetDateFormatter();
            // dateFormatter has no post constraints
            this.__dateFormatter13a = dateFormatter13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dateFormatter13aSet = true;
            }
        }
        return dateFormatter13a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getTimeFormatter()
    * @return String
    */
    protected abstract String handleGetTimeFormatter();

    private String __timeFormatter14a;
    private boolean __timeFormatter14aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.timeFormatter
     * @return (String)handleGetTimeFormatter()
     */
    public final String getTimeFormatter()
    {
        String timeFormatter14a = this.__timeFormatter14a;
        if (!this.__timeFormatter14aSet)
        {
            // timeFormatter has no pre constraints
            timeFormatter14a = handleGetTimeFormatter();
            // timeFormatter has no post constraints
            this.__timeFormatter14a = timeFormatter14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__timeFormatter14aSet = true;
            }
        }
        return timeFormatter14a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getBackingListName()
    * @return String
    */
    protected abstract String handleGetBackingListName();

    private String __backingListName15a;
    private boolean __backingListName15aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.backingListName
     * @return (String)handleGetBackingListName()
     */
    public final String getBackingListName()
    {
        String backingListName15a = this.__backingListName15a;
        if (!this.__backingListName15aSet)
        {
            // backingListName has no pre constraints
            backingListName15a = handleGetBackingListName();
            // backingListName has no post constraints
            this.__backingListName15a = backingListName15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__backingListName15aSet = true;
            }
        }
        return backingListName15a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getValueListName()
    * @return String
    */
    protected abstract String handleGetValueListName();

    private String __valueListName16a;
    private boolean __valueListName16aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.valueListName
     * @return (String)handleGetValueListName()
     */
    public final String getValueListName()
    {
        String valueListName16a = this.__valueListName16a;
        if (!this.__valueListName16aSet)
        {
            // valueListName has no pre constraints
            valueListName16a = handleGetValueListName();
            // valueListName has no post constraints
            this.__valueListName16a = valueListName16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueListName16aSet = true;
            }
        }
        return valueListName16a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getLabelListName()
    * @return String
    */
    protected abstract String handleGetLabelListName();

    private String __labelListName17a;
    private boolean __labelListName17aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.labelListName
     * @return (String)handleGetLabelListName()
     */
    public final String getLabelListName()
    {
        String labelListName17a = this.__labelListName17a;
        if (!this.__labelListName17aSet)
        {
            // labelListName has no pre constraints
            labelListName17a = handleGetLabelListName();
            // labelListName has no post constraints
            this.__labelListName17a = labelListName17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__labelListName17aSet = true;
            }
        }
        return labelListName17a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getValidatorTypes()
    * @return Collection
    */
    protected abstract Collection handleGetValidatorTypes();

    private Collection __validatorTypes18a;
    private boolean __validatorTypes18aSet = false;

    /**
     * All validator types for this attribute.
     * @return (Collection)handleGetValidatorTypes()
     */
    public final Collection getValidatorTypes()
    {
        Collection validatorTypes18a = this.__validatorTypes18a;
        if (!this.__validatorTypes18aSet)
        {
            // validatorTypes has no pre constraints
            validatorTypes18a = handleGetValidatorTypes();
            // validatorTypes has no post constraints
            this.__validatorTypes18a = validatorTypes18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validatorTypes18aSet = true;
            }
        }
        return validatorTypes18a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired19a;
    private boolean __validationRequired19aSet = false;

    /**
     * Indicates whether or not this attribute requires some kind of validation (the collection of
     * validator types is not empty).
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired19a = this.__validationRequired19a;
        if (!this.__validationRequired19aSet)
        {
            // validationRequired has no pre constraints
            validationRequired19a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired19a = validationRequired19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired19aSet = true;
            }
        }
        return validationRequired19a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getValidWhen()
    * @return String
    */
    protected abstract String handleGetValidWhen();

    private String __validWhen20a;
    private boolean __validWhen20aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.validWhen
     * @return (String)handleGetValidWhen()
     */
    public final String getValidWhen()
    {
        String validWhen20a = this.__validWhen20a;
        if (!this.__validWhen20aSet)
        {
            // validWhen has no pre constraints
            validWhen20a = handleGetValidWhen();
            // validWhen has no post constraints
            this.__validWhen20a = validWhen20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validWhen20aSet = true;
            }
        }
        return validWhen20a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputCheckbox()
    * @return boolean
    */
    protected abstract boolean handleIsInputCheckbox();

    private boolean __inputCheckbox21a;
    private boolean __inputCheckbox21aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputCheckbox
     * @return (boolean)handleIsInputCheckbox()
     */
    public final boolean isInputCheckbox()
    {
        boolean inputCheckbox21a = this.__inputCheckbox21a;
        if (!this.__inputCheckbox21aSet)
        {
            // inputCheckbox has no pre constraints
            inputCheckbox21a = handleIsInputCheckbox();
            // inputCheckbox has no post constraints
            this.__inputCheckbox21a = inputCheckbox21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputCheckbox21aSet = true;
            }
        }
        return inputCheckbox21a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputFile()
    * @return boolean
    */
    protected abstract boolean handleIsInputFile();

    private boolean __inputFile22a;
    private boolean __inputFile22aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputFile
     * @return (boolean)handleIsInputFile()
     */
    public final boolean isInputFile()
    {
        boolean inputFile22a = this.__inputFile22a;
        if (!this.__inputFile22aSet)
        {
            // inputFile has no pre constraints
            inputFile22a = handleIsInputFile();
            // inputFile has no post constraints
            this.__inputFile22a = inputFile22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputFile22aSet = true;
            }
        }
        return inputFile22a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputHidden()
    * @return boolean
    */
    protected abstract boolean handleIsInputHidden();

    private boolean __inputHidden23a;
    private boolean __inputHidden23aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputHidden
     * @return (boolean)handleIsInputHidden()
     */
    public final boolean isInputHidden()
    {
        boolean inputHidden23a = this.__inputHidden23a;
        if (!this.__inputHidden23aSet)
        {
            // inputHidden has no pre constraints
            inputHidden23a = handleIsInputHidden();
            // inputHidden has no post constraints
            this.__inputHidden23a = inputHidden23a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputHidden23aSet = true;
            }
        }
        return inputHidden23a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputMultibox()
    * @return boolean
    */
    protected abstract boolean handleIsInputMultibox();

    private boolean __inputMultibox24a;
    private boolean __inputMultibox24aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputMultibox
     * @return (boolean)handleIsInputMultibox()
     */
    public final boolean isInputMultibox()
    {
        boolean inputMultibox24a = this.__inputMultibox24a;
        if (!this.__inputMultibox24aSet)
        {
            // inputMultibox has no pre constraints
            inputMultibox24a = handleIsInputMultibox();
            // inputMultibox has no post constraints
            this.__inputMultibox24a = inputMultibox24a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputMultibox24aSet = true;
            }
        }
        return inputMultibox24a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputRadio()
    * @return boolean
    */
    protected abstract boolean handleIsInputRadio();

    private boolean __inputRadio25a;
    private boolean __inputRadio25aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputRadio
     * @return (boolean)handleIsInputRadio()
     */
    public final boolean isInputRadio()
    {
        boolean inputRadio25a = this.__inputRadio25a;
        if (!this.__inputRadio25aSet)
        {
            // inputRadio has no pre constraints
            inputRadio25a = handleIsInputRadio();
            // inputRadio has no post constraints
            this.__inputRadio25a = inputRadio25a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputRadio25aSet = true;
            }
        }
        return inputRadio25a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputSecret()
    * @return boolean
    */
    protected abstract boolean handleIsInputSecret();

    private boolean __inputSecret26a;
    private boolean __inputSecret26aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputSecret
     * @return (boolean)handleIsInputSecret()
     */
    public final boolean isInputSecret()
    {
        boolean inputSecret26a = this.__inputSecret26a;
        if (!this.__inputSecret26aSet)
        {
            // inputSecret has no pre constraints
            inputSecret26a = handleIsInputSecret();
            // inputSecret has no post constraints
            this.__inputSecret26a = inputSecret26a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputSecret26aSet = true;
            }
        }
        return inputSecret26a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputSelect()
    * @return boolean
    */
    protected abstract boolean handleIsInputSelect();

    private boolean __inputSelect27a;
    private boolean __inputSelect27aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputSelect
     * @return (boolean)handleIsInputSelect()
     */
    public final boolean isInputSelect()
    {
        boolean inputSelect27a = this.__inputSelect27a;
        if (!this.__inputSelect27aSet)
        {
            // inputSelect has no pre constraints
            inputSelect27a = handleIsInputSelect();
            // inputSelect has no post constraints
            this.__inputSelect27a = inputSelect27a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputSelect27aSet = true;
            }
        }
        return inputSelect27a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputTable()
    * @return boolean
    */
    protected abstract boolean handleIsInputTable();

    private boolean __inputTable28a;
    private boolean __inputTable28aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTable
     * @return (boolean)handleIsInputTable()
     */
    public final boolean isInputTable()
    {
        boolean inputTable28a = this.__inputTable28a;
        if (!this.__inputTable28aSet)
        {
            // inputTable has no pre constraints
            inputTable28a = handleIsInputTable();
            // inputTable has no post constraints
            this.__inputTable28a = inputTable28a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTable28aSet = true;
            }
        }
        return inputTable28a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getInputTableIdentifierColumns()
    * @return String
    */
    protected abstract String handleGetInputTableIdentifierColumns();

    private String __inputTableIdentifierColumns29a;
    private boolean __inputTableIdentifierColumns29aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTableIdentifierColumns
     * @return (String)handleGetInputTableIdentifierColumns()
     */
    public final String getInputTableIdentifierColumns()
    {
        String inputTableIdentifierColumns29a = this.__inputTableIdentifierColumns29a;
        if (!this.__inputTableIdentifierColumns29aSet)
        {
            // inputTableIdentifierColumns has no pre constraints
            inputTableIdentifierColumns29a = handleGetInputTableIdentifierColumns();
            // inputTableIdentifierColumns has no post constraints
            this.__inputTableIdentifierColumns29a = inputTableIdentifierColumns29a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTableIdentifierColumns29aSet = true;
            }
        }
        return inputTableIdentifierColumns29a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputText()
    * @return boolean
    */
    protected abstract boolean handleIsInputText();

    private boolean __inputText30a;
    private boolean __inputText30aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputText
     * @return (boolean)handleIsInputText()
     */
    public final boolean isInputText()
    {
        boolean inputText30a = this.__inputText30a;
        if (!this.__inputText30aSet)
        {
            // inputText has no pre constraints
            inputText30a = handleIsInputText();
            // inputText has no post constraints
            this.__inputText30a = inputText30a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputText30aSet = true;
            }
        }
        return inputText30a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputTextarea()
    * @return boolean
    */
    protected abstract boolean handleIsInputTextarea();

    private boolean __inputTextarea31a;
    private boolean __inputTextarea31aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTextarea
     * @return (boolean)handleIsInputTextarea()
     */
    public final boolean isInputTextarea()
    {
        boolean inputTextarea31a = this.__inputTextarea31a;
        if (!this.__inputTextarea31aSet)
        {
            // inputTextarea has no pre constraints
            inputTextarea31a = handleIsInputTextarea();
            // inputTextarea has no post constraints
            this.__inputTextarea31a = inputTextarea31a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTextarea31aSet = true;
            }
        }
        return inputTextarea31a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isInputTypePresent()
    * @return boolean
    */
    protected abstract boolean handleIsInputTypePresent();

    private boolean __inputTypePresent32a;
    private boolean __inputTypePresent32aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTypePresent
     * @return (boolean)handleIsInputTypePresent()
     */
    public final boolean isInputTypePresent()
    {
        boolean inputTypePresent32a = this.__inputTypePresent32a;
        if (!this.__inputTypePresent32aSet)
        {
            // inputTypePresent has no pre constraints
            inputTypePresent32a = handleIsInputTypePresent();
            // inputTypePresent has no post constraints
            this.__inputTypePresent32a = inputTypePresent32a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTypePresent32aSet = true;
            }
        }
        return inputTypePresent32a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getDummyValue()
    * @return String
    */
    protected abstract String handleGetDummyValue();

    private String __dummyValue33a;
    private boolean __dummyValue33aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.dummyValue
     * @return (String)handleGetDummyValue()
     */
    public final String getDummyValue()
    {
        String dummyValue33a = this.__dummyValue33a;
        if (!this.__dummyValue33aSet)
        {
            // dummyValue has no pre constraints
            dummyValue33a = handleGetDummyValue();
            // dummyValue has no post constraints
            this.__dummyValue33a = dummyValue33a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dummyValue33aSet = true;
            }
        }
        return dummyValue33a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isEqualValidator()
    * @return boolean
    */
    protected abstract boolean handleIsEqualValidator();

    private boolean __equalValidator34a;
    private boolean __equalValidator34aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.equalValidator
     * @return (boolean)handleIsEqualValidator()
     */
    public final boolean isEqualValidator()
    {
        boolean equalValidator34a = this.__equalValidator34a;
        if (!this.__equalValidator34aSet)
        {
            // equalValidator has no pre constraints
            equalValidator34a = handleIsEqualValidator();
            // equalValidator has no post constraints
            this.__equalValidator34a = equalValidator34a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__equalValidator34aSet = true;
            }
        }
        return equalValidator34a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isPlaintext()
    * @return boolean
    */
    protected abstract boolean handleIsPlaintext();

    private boolean __plaintext35a;
    private boolean __plaintext35aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.plaintext
     * @return (boolean)handleIsPlaintext()
     */
    public final boolean isPlaintext()
    {
        boolean plaintext35a = this.__plaintext35a;
        if (!this.__plaintext35aSet)
        {
            // plaintext has no pre constraints
            plaintext35a = handleIsPlaintext();
            // plaintext has no post constraints
            this.__plaintext35a = plaintext35a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__plaintext35aSet = true;
            }
        }
        return plaintext35a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getValueListDummyValue()
    * @return String
    */
    protected abstract String handleGetValueListDummyValue();

    private String __valueListDummyValue36a;
    private boolean __valueListDummyValue36aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.valueListDummyValue
     * @return (String)handleGetValueListDummyValue()
     */
    public final String getValueListDummyValue()
    {
        String valueListDummyValue36a = this.__valueListDummyValue36a;
        if (!this.__valueListDummyValue36aSet)
        {
            // valueListDummyValue has no pre constraints
            valueListDummyValue36a = handleGetValueListDummyValue();
            // valueListDummyValue has no post constraints
            this.__valueListDummyValue36a = valueListDummyValue36a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueListDummyValue36aSet = true;
            }
        }
        return valueListDummyValue36a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getValidatorVars()
    * @return Collection
    */
    protected abstract Collection handleGetValidatorVars();

    private Collection __validatorVars37a;
    private boolean __validatorVars37aSet = false;

    /**
     * The validator variables.
     * @return (Collection)handleGetValidatorVars()
     */
    public final Collection getValidatorVars()
    {
        Collection validatorVars37a = this.__validatorVars37a;
        if (!this.__validatorVars37aSet)
        {
            // validatorVars has no pre constraints
            validatorVars37a = handleGetValidatorVars();
            // validatorVars has no post constraints
            this.__validatorVars37a = validatorVars37a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validatorVars37aSet = true;
            }
        }
        return validatorVars37a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#getMaxLength()
    * @return String
    */
    protected abstract String handleGetMaxLength();

    private String __maxLength38a;
    private boolean __maxLength38aSet = false;

    /**
     * The max length allowed in the input component
     * @return (String)handleGetMaxLength()
     */
    public final String getMaxLength()
    {
        String maxLength38a = this.__maxLength38a;
        if (!this.__maxLength38aSet)
        {
            // maxLength has no pre constraints
            maxLength38a = handleGetMaxLength();
            // maxLength has no post constraints
            this.__maxLength38a = maxLength38a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__maxLength38aSet = true;
            }
        }
        return maxLength38a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute#isEditable()
    * @return boolean
    */
    protected abstract boolean handleIsEditable();

    private boolean __editable39a;
    private boolean __editable39aSet = false;

    /**
     * Whether or not this attribute should be put in the view
     * @return (boolean)handleIsEditable()
     */
    public final boolean isEditable()
    {
        boolean editable39a = this.__editable39a;
        if (!this.__editable39aSet)
        {
            // editable has no pre constraints
            editable39a = handleIsEditable();
            // editable has no post constraints
            this.__editable39a = editable39a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__editable39aSet = true;
            }
        }
        return editable39a;
    }

    // ---------------- business methods ----------------------

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.getValidatorArgs
     * @param validatorType
     * @return Collection
     */
    protected abstract Collection handleGetValidatorArgs(String validatorType);

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.getValidatorArgs
     * @param validatorType String
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.getValidatorArgs(validatorType)
     * @return handleGetValidatorArgs(validatorType)
     */
    public Collection getValidatorArgs(String validatorType)
    {
        // getValidatorArgs has no pre constraints
        Collection returnValue = handleGetValidatorArgs(validatorType);
        // getValidatorArgs has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Gets the unique id of this attribute on the form.
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetFormPropertyId(ParameterFacade ownerParameter);

    /**
     * Gets the unique id of this attribute on the form.
     * @param ownerParameter ParameterFacade
     * The parameter that is the owner of this attribute.
     * @return handleGetFormPropertyId(ownerParameter)
     */
    public String getFormPropertyId(ParameterFacade ownerParameter)
    {
        // getFormPropertyId has no pre constraints
        String returnValue = handleGetFormPropertyId(ownerParameter);
        // getFormPropertyId has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Retrieves the name of the form property for this attribute by taking the name of the owner
     * property.
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetFormPropertyName(ParameterFacade ownerParameter);

    /**
     * Retrieves the name of the form property for this attribute by taking the name of the owner
     * property.
     * @param ownerParameter ParameterFacade
     * The parent that is the owner of this parameter.
     * @return handleGetFormPropertyName(ownerParameter)
     */
    public String getFormPropertyName(ParameterFacade ownerParameter)
    {
        // getFormPropertyName has no pre constraints
        String returnValue = handleGetFormPropertyName(ownerParameter);
        // getFormPropertyName has no post constraints
        return returnValue;
    }

    /**
     * @return true
     * @see ManageableEntityAttribute
     */
    public boolean isManageableEntityAttributeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.EntityAttribute
     */
    public boolean isEntityAttributeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.AttributeFacade
     */
    public boolean isAttributeFacadeMetaType()
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

    // ----------- delegates to ManageableEntityAttribute ------------
    /**
     * Searches the given feature for the specified tag.
     * If the follow boolean is set to true then the search will continue from the class attribute
     * to the class itself and then up the class hierarchy.
     * @see org.andromda.metafacades.uml.AttributeFacade#findTaggedValue(String name, boolean follow)
     */
    public Object findTaggedValue(String name, boolean follow)
    {
        return this.getSuperManageableEntityAttribute().findTaggedValue(name, follow);
    }

    /**
     * The default value of the attribute.  This is the value given if no value is defined.
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        return this.getSuperManageableEntityAttribute().getDefaultValue();
    }

    /**
     * If the attribute is an enumeration literal this represents the owning enumeration. Can be
     * empty.
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumeration()
     */
    public EnumerationFacade getEnumeration()
    {
        return this.getSuperManageableEntityAttribute().getEnumeration();
    }

    /**
     * Returns the enumeration literal parameters defined by tagged value as a comma separated list.
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationLiteralParameters()
     */
    public String getEnumerationLiteralParameters()
    {
        return this.getSuperManageableEntityAttribute().getEnumerationLiteralParameters();
    }

    /**
     * The value for this attribute if it is an enumeration literal, null otherwise. The default
     * value is returned as a String if it has been specified, if it's not specified this
     * attribute's name is assumed.
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationValue()
     */
    public String getEnumerationValue()
    {
        return this.getSuperManageableEntityAttribute().getEnumerationValue();
    }

    /**
     * The name of the accessor operation that would retrieve this attribute's value.
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    public String getGetterName()
    {
        return this.getSuperManageableEntityAttribute().getGetterName();
    }

    /**
     * The name of the type that is returned on the accessor and mutator operations,  determined in
     * part by the multiplicity.
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        return this.getSuperManageableEntityAttribute().getGetterSetterTypeName();
    }

    /**
     * the lower value for the multiplicity
     * -only applicable for UML2
     * @see org.andromda.metafacades.uml.AttributeFacade#getLower()
     */
    public int getLower()
    {
        return this.getSuperManageableEntityAttribute().getLower();
    }

    /**
     * Gets the classifier who is the owner of the attributes.
     * @see org.andromda.metafacades.uml.AttributeFacade#getOwner()
     */
    public ClassifierFacade getOwner()
    {
        return this.getSuperManageableEntityAttribute().getOwner();
    }

    /**
     * The name of the mutator operation that would retrieve this attribute's value.
     * @see org.andromda.metafacades.uml.AttributeFacade#getSetterName()
     */
    public String getSetterName()
    {
        return this.getSuperManageableEntityAttribute().getSetterName();
    }

    /**
     * The classifier owning this attribute.
     * @see org.andromda.metafacades.uml.AttributeFacade#getType()
     */
    public ClassifierFacade getType()
    {
        return this.getSuperManageableEntityAttribute().getType();
    }

    /**
     * the upper value for the multiplicity (will be -1 for *)
     * -only applicable for UML2
     * @see org.andromda.metafacades.uml.AttributeFacade#getUpper()
     */
    public int getUpper()
    {
        return this.getSuperManageableEntityAttribute().getUpper();
    }

    /**
     * True if this attribute can only be set.
     * @see org.andromda.metafacades.uml.AttributeFacade#isAddOnly()
     */
    public boolean isAddOnly()
    {
        return this.getSuperManageableEntityAttribute().isAddOnly();
    }

    /**
     * True if this attribute can be modified.
     * @see org.andromda.metafacades.uml.AttributeFacade#isChangeable()
     */
    public boolean isChangeable()
    {
        return this.getSuperManageableEntityAttribute().isChangeable();
    }

    /**
     * Indicates if the default value is present.
     * @see org.andromda.metafacades.uml.AttributeFacade#isDefaultValuePresent()
     */
    public boolean isDefaultValuePresent()
    {
        return this.getSuperManageableEntityAttribute().isDefaultValuePresent();
    }

    /**
     * If the attribute is derived (its value is computed). UML2 only. UML14 always returns false.
     * Default=false.
     * @see org.andromda.metafacades.uml.AttributeFacade#isDerived()
     */
    public boolean isDerived()
    {
        return this.getSuperManageableEntityAttribute().isDerived();
    }

    /**
     * True if this attribute is owned by an enumeration.
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteral()
     */
    public boolean isEnumerationLiteral()
    {
        return this.getSuperManageableEntityAttribute().isEnumerationLiteral();
    }

    /**
     * Returns true if enumeration literal parameters exist (defined by tagged value) for the
     * literal.
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteralParametersExist()
     */
    public boolean isEnumerationLiteralParametersExist()
    {
        return this.getSuperManageableEntityAttribute().isEnumerationLiteralParametersExist();
    }

    /**
     * True if this attribute is owned by an enumeration but is defined as a member variable (NOT a
     * literal).
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationMember()
     */
    public boolean isEnumerationMember()
    {
        return this.getSuperManageableEntityAttribute().isEnumerationMember();
    }

    /**
     * IsLeaf property in the operation. If true, operation is final, cannot be extended or
     * implemented by a descendant.
     * @see org.andromda.metafacades.uml.AttributeFacade#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.getSuperManageableEntityAttribute().isLeaf();
    }

    /**
     * Whether or not this attribute has a multiplicity greater than 1.
     * @see org.andromda.metafacades.uml.AttributeFacade#isMany()
     */
    public boolean isMany()
    {
        return this.getSuperManageableEntityAttribute().isMany();
    }

    /**
     * Indicates whether or not the attributes are ordered (if multiplicity is greater than 1).
     * @see org.andromda.metafacades.uml.AttributeFacade#isOrdered()
     */
    public boolean isOrdered()
    {
        return this.getSuperManageableEntityAttribute().isOrdered();
    }

    /**
     * Whether or not this attribute can be modified.
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return this.getSuperManageableEntityAttribute().isReadOnly();
    }

    /**
     * Whether or not the multiplicity of this attribute is 1.
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    public boolean isRequired()
    {
        return this.getSuperManageableEntityAttribute().isRequired();
    }

    /**
     * Indicates if this attribute is 'static', meaning it has a classifier scope.
     * @see org.andromda.metafacades.uml.AttributeFacade#isStatic()
     */
    public boolean isStatic()
    {
        return this.getSuperManageableEntityAttribute().isStatic();
    }

    /**
     * If the attribute is unique within the Collection type. UML2 only. UML14 always returns false.
     * Unique+Ordered determines the implementation Collection type. Default=false.
     * @see org.andromda.metafacades.uml.AttributeFacade#isUnique()
     */
    public boolean isUnique()
    {
        return this.getSuperManageableEntityAttribute().isUnique();
    }

    /**
     * The name of the index to create on a column that persists the entity attribute.
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnIndex()
     */
    public String getColumnIndex()
    {
        return this.getSuperManageableEntityAttribute().getColumnIndex();
    }

    /**
     * The length of the column that persists this entity attribute.
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnLength()
     */
    public String getColumnLength()
    {
        return this.getSuperManageableEntityAttribute().getColumnLength();
    }

    /**
     * The name of the table column to which this entity is mapped.
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName()
     */
    public String getColumnName()
    {
        return this.getSuperManageableEntityAttribute().getColumnName();
    }

    /**
     * The PIM to language specific mappings for JDBC.
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcMappings()
     */
    public TypeMappings getJdbcMappings()
    {
        return this.getSuperManageableEntityAttribute().getJdbcMappings();
    }

    /**
     * The JDBC type for this entity attribute.
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcType()
     */
    public String getJdbcType()
    {
        return this.getSuperManageableEntityAttribute().getJdbcType();
    }

    /**
     * The SQL mappings (i.e. the mappings which provide PIM to SQL mappings).
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlMappings()
     */
    public TypeMappings getSqlMappings()
    {
        return this.getSuperManageableEntityAttribute().getSqlMappings();
    }

    /**
     * The SQL type for this attribute.
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlType()
     */
    public String getSqlType()
    {
        return this.getSuperManageableEntityAttribute().getSqlType();
    }

    /**
     * The name of the unique-key that this unique attribute belongs
     * @see org.andromda.metafacades.uml.EntityAttribute#getUniqueGroup()
     */
    public String getUniqueGroup()
    {
        return this.getSuperManageableEntityAttribute().getUniqueGroup();
    }

    /**
     * True if this attribute is an identifier for its entity.
     * @see org.andromda.metafacades.uml.EntityAttribute#isIdentifier()
     */
    public boolean isIdentifier()
    {
        return this.getSuperManageableEntityAttribute().isIdentifier();
    }

    /**
     * Indicates this attribute should be ignored by the persistence layer.
     * @see org.andromda.metafacades.uml.EntityAttribute#isTransient()
     */
    public boolean isTransient()
    {
        return this.getSuperManageableEntityAttribute().isTransient();
    }

    /**
     * Whether or not this attribute should be displayed.
     * @see ManageableEntityAttribute#isDisplay()
     */
    public boolean isDisplay()
    {
        return this.getSuperManageableEntityAttribute().isDisplay();
    }

    /**
     * Whether or not this attribute can be read in a call isolated from the rest (for example when
     * downloading binary fields).
     * @see ManageableEntityAttribute#isManageableGetterAvailable()
     */
    public boolean isManageableGetterAvailable()
    {
        return this.getSuperManageableEntityAttribute().isManageableGetterAvailable();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperManageableEntityAttribute().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperManageableEntityAttribute().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperManageableEntityAttribute().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperManageableEntityAttribute().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperManageableEntityAttribute().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperManageableEntityAttribute().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperManageableEntityAttribute().getDocumentation(indent);
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
        return this.getSuperManageableEntityAttribute().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperManageableEntityAttribute().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperManageableEntityAttribute().getFullyQualifiedName();
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
        return this.getSuperManageableEntityAttribute().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperManageableEntityAttribute().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperManageableEntityAttribute().getId();
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
        return this.getSuperManageableEntityAttribute().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperManageableEntityAttribute().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperManageableEntityAttribute().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperManageableEntityAttribute().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperManageableEntityAttribute().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperManageableEntityAttribute().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperManageableEntityAttribute().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperManageableEntityAttribute().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperManageableEntityAttribute().getPackagePath();
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
        return this.getSuperManageableEntityAttribute().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperManageableEntityAttribute().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperManageableEntityAttribute().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperManageableEntityAttribute().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperManageableEntityAttribute().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperManageableEntityAttribute().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperManageableEntityAttribute().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperManageableEntityAttribute().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperManageableEntityAttribute().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperManageableEntityAttribute().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperManageableEntityAttribute().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperManageableEntityAttribute().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperManageableEntityAttribute().hasKeyword(keywordName);
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
        return this.getSuperManageableEntityAttribute().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperManageableEntityAttribute().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperManageableEntityAttribute().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperManageableEntityAttribute().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperManageableEntityAttribute().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperManageableEntityAttribute().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperManageableEntityAttribute().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperManageableEntityAttribute().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperManageableEntityAttribute().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperManageableEntityAttribute().translateConstraints(kind, translation);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperManageableEntityAttribute().initialize();
    }

    /**
     * @return Object getSuperManageableEntityAttribute().getValidationOwner()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperManageableEntityAttribute().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperManageableEntityAttribute().getValidationName()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperManageableEntityAttribute().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperManageableEntityAttribute().validateInvariants(validationMessages);
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