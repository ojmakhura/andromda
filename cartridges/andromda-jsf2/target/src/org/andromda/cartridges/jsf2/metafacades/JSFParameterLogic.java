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
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.log4j.Logger;

/**
 * Represents a parameter in a JSF front-end.
 * MetafacadeLogic for JSFParameter
 *
 * @see JSFParameter
 */
public abstract class JSFParameterLogic
    extends MetafacadeBase
    implements JSFParameter
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
    protected JSFParameterLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superFrontEndParameter =
           (FrontEndParameter)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.FrontEndParameter",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFParameterLogic.class);

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFParameter if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFParameter";
        }
        return context;
    }

    private FrontEndParameter superFrontEndParameter;
    private boolean superFrontEndParameterInitialized = false;

    /**
     * Gets the FrontEndParameter parent instance.
     * @return this.superFrontEndParameter FrontEndParameter
     */
    private FrontEndParameter getSuperFrontEndParameter()
    {
        if (!this.superFrontEndParameterInitialized)
        {
            ((MetafacadeBase)this.superFrontEndParameter).setMetafacadeContext(this.getMetafacadeContext());
            this.superFrontEndParameterInitialized = true;
        }
        return this.superFrontEndParameter;
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
            if (this.superFrontEndParameterInitialized)
            {
                ((MetafacadeBase)this.superFrontEndParameter).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFParameter
     */
    public boolean isJSFParameterMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey1a;
    private boolean __messageKey1aSet = false;

    /**
     * The default message key for this parameter.
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
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getMessageValue()
    * @return String
    */
    protected abstract String handleGetMessageValue();

    private String __messageValue2a;
    private boolean __messageValue2aSet = false;

    /**
     * The default message value for this parameter.
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
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getDocumentationKey()
    * @return String
    */
    protected abstract String handleGetDocumentationKey();

    private String __documentationKey3a;
    private boolean __documentationKey3aSet = false;

    /**
     * A resource message key suited for the parameter's documentation.
     * @return (String)handleGetDocumentationKey()
     */
    public final String getDocumentationKey()
    {
        String documentationKey3a = this.__documentationKey3a;
        if (!this.__documentationKey3aSet)
        {
            // documentationKey has no pre constraints
            documentationKey3a = handleGetDocumentationKey();
            // documentationKey has no post constraints
            this.__documentationKey3a = documentationKey3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__documentationKey3aSet = true;
            }
        }
        return documentationKey3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getDocumentationValue()
    * @return String
    */
    protected abstract String handleGetDocumentationValue();

    private String __documentationValue4a;
    private boolean __documentationValue4aSet = false;

    /**
     * A resource message value suited for the parameter's documentation.
     * @return (String)handleGetDocumentationValue()
     */
    public final String getDocumentationValue()
    {
        String documentationValue4a = this.__documentationValue4a;
        if (!this.__documentationValue4aSet)
        {
            // documentationValue has no pre constraints
            documentationValue4a = handleGetDocumentationValue();
            // documentationValue has no post constraints
            this.__documentationValue4a = documentationValue4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__documentationValue4aSet = true;
            }
        }
        return documentationValue4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getFormat()
    * @return String
    */
    protected abstract String handleGetFormat();

    private String __format5a;
    private boolean __format5aSet = false;

    /**
     * If this parameter represents a date or time this method will return the format in which it
     * must be represented. In the event this format has not been specified by the any tagged value
     * the default will be used.
     * @return (String)handleGetFormat()
     */
    public final String getFormat()
    {
        String format5a = this.__format5a;
        if (!this.__format5aSet)
        {
            // format has no pre constraints
            format5a = handleGetFormat();
            // format has no post constraints
            this.__format5a = format5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__format5aSet = true;
            }
        }
        return format5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isStrictDateFormat()
    * @return boolean
    */
    protected abstract boolean handleIsStrictDateFormat();

    private boolean __strictDateFormat6a;
    private boolean __strictDateFormat6aSet = false;

    /**
     * Indicates where or not the date format is to be strictly respected. Otherwise the date
     * formatter used for the representation of this date is to be set to lenient.
     * @return (boolean)handleIsStrictDateFormat()
     */
    public final boolean isStrictDateFormat()
    {
        boolean strictDateFormat6a = this.__strictDateFormat6a;
        if (!this.__strictDateFormat6aSet)
        {
            // strictDateFormat has no pre constraints
            strictDateFormat6a = handleIsStrictDateFormat();
            // strictDateFormat has no post constraints
            this.__strictDateFormat6a = strictDateFormat6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__strictDateFormat6aSet = true;
            }
        }
        return strictDateFormat6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getDateFormatter()
    * @return String
    */
    protected abstract String handleGetDateFormatter();

    private String __dateFormatter7a;
    private boolean __dateFormatter7aSet = false;

    /**
     * The name of the date formatter for this parameter (if this parameter represents a date).
     * @return (String)handleGetDateFormatter()
     */
    public final String getDateFormatter()
    {
        String dateFormatter7a = this.__dateFormatter7a;
        if (!this.__dateFormatter7aSet)
        {
            // dateFormatter has no pre constraints
            dateFormatter7a = handleGetDateFormatter();
            // dateFormatter has no post constraints
            this.__dateFormatter7a = dateFormatter7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dateFormatter7aSet = true;
            }
        }
        return dateFormatter7a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getTimeFormatter()
    * @return String
    */
    protected abstract String handleGetTimeFormatter();

    private String __timeFormatter8a;
    private boolean __timeFormatter8aSet = false;

    /**
     * The name of the time formatter (if this parameter represents a time).
     * @return (String)handleGetTimeFormatter()
     */
    public final String getTimeFormatter()
    {
        String timeFormatter8a = this.__timeFormatter8a;
        if (!this.__timeFormatter8aSet)
        {
            // timeFormatter has no pre constraints
            timeFormatter8a = handleGetTimeFormatter();
            // timeFormatter has no post constraints
            this.__timeFormatter8a = timeFormatter8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__timeFormatter8aSet = true;
            }
        }
        return timeFormatter8a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputCheckbox()
    * @return boolean
    */
    protected abstract boolean handleIsInputCheckbox();

    private boolean __inputCheckbox9a;
    private boolean __inputCheckbox9aSet = false;

    /**
     * Indicates if this parameter represents a checkbox widget.
     * @return (boolean)handleIsInputCheckbox()
     */
    public final boolean isInputCheckbox()
    {
        boolean inputCheckbox9a = this.__inputCheckbox9a;
        if (!this.__inputCheckbox9aSet)
        {
            // inputCheckbox has no pre constraints
            inputCheckbox9a = handleIsInputCheckbox();
            // inputCheckbox has no post constraints
            this.__inputCheckbox9a = inputCheckbox9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputCheckbox9aSet = true;
            }
        }
        return inputCheckbox9a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputTextarea()
    * @return boolean
    */
    protected abstract boolean handleIsInputTextarea();

    private boolean __inputTextarea10a;
    private boolean __inputTextarea10aSet = false;

    /**
     * Indicates if this parameter represents as an input text area widget.
     * @return (boolean)handleIsInputTextarea()
     */
    public final boolean isInputTextarea()
    {
        boolean inputTextarea10a = this.__inputTextarea10a;
        if (!this.__inputTextarea10aSet)
        {
            // inputTextarea has no pre constraints
            inputTextarea10a = handleIsInputTextarea();
            // inputTextarea has no post constraints
            this.__inputTextarea10a = inputTextarea10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTextarea10aSet = true;
            }
        }
        return inputTextarea10a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputSelect()
    * @return boolean
    */
    protected abstract boolean handleIsInputSelect();

    private boolean __inputSelect11a;
    private boolean __inputSelect11aSet = false;

    /**
     * Indicates whether or not this parameter represents an input select widget.
     * @return (boolean)handleIsInputSelect()
     */
    public final boolean isInputSelect()
    {
        boolean inputSelect11a = this.__inputSelect11a;
        if (!this.__inputSelect11aSet)
        {
            // inputSelect has no pre constraints
            inputSelect11a = handleIsInputSelect();
            // inputSelect has no post constraints
            this.__inputSelect11a = inputSelect11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputSelect11aSet = true;
            }
        }
        return inputSelect11a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputSecret()
    * @return boolean
    */
    protected abstract boolean handleIsInputSecret();

    private boolean __inputSecret12a;
    private boolean __inputSecret12aSet = false;

    /**
     * Indicates whether or not this parameter represents an input "secret" widget (i.e. password).
     * @return (boolean)handleIsInputSecret()
     */
    public final boolean isInputSecret()
    {
        boolean inputSecret12a = this.__inputSecret12a;
        if (!this.__inputSecret12aSet)
        {
            // inputSecret has no pre constraints
            inputSecret12a = handleIsInputSecret();
            // inputSecret has no post constraints
            this.__inputSecret12a = inputSecret12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputSecret12aSet = true;
            }
        }
        return inputSecret12a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputHidden()
    * @return boolean
    */
    protected abstract boolean handleIsInputHidden();

    private boolean __inputHidden13a;
    private boolean __inputHidden13aSet = false;

    /**
     * Indicates whether or not this parameter represents a hidden input widget.
     * @return (boolean)handleIsInputHidden()
     */
    public final boolean isInputHidden()
    {
        boolean inputHidden13a = this.__inputHidden13a;
        if (!this.__inputHidden13aSet)
        {
            // inputHidden has no pre constraints
            inputHidden13a = handleIsInputHidden();
            // inputHidden has no post constraints
            this.__inputHidden13a = inputHidden13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputHidden13aSet = true;
            }
        }
        return inputHidden13a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isPlaintext()
    * @return boolean
    */
    protected abstract boolean handleIsPlaintext();

    private boolean __plaintext14a;
    private boolean __plaintext14aSet = false;

    /**
     * Indicates whether or not this field should be rendered as plain text (not as a widget).
     * @return (boolean)handleIsPlaintext()
     */
    public final boolean isPlaintext()
    {
        boolean plaintext14a = this.__plaintext14a;
        if (!this.__plaintext14aSet)
        {
            // plaintext has no pre constraints
            plaintext14a = handleIsPlaintext();
            // plaintext has no post constraints
            this.__plaintext14a = plaintext14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__plaintext14aSet = true;
            }
        }
        return plaintext14a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputRadio()
    * @return boolean
    */
    protected abstract boolean handleIsInputRadio();

    private boolean __inputRadio15a;
    private boolean __inputRadio15aSet = false;

    /**
     * Indicates whether or not this parameter should be rendered as an input radio widget.
     * @return (boolean)handleIsInputRadio()
     */
    public final boolean isInputRadio()
    {
        boolean inputRadio15a = this.__inputRadio15a;
        if (!this.__inputRadio15aSet)
        {
            // inputRadio has no pre constraints
            inputRadio15a = handleIsInputRadio();
            // inputRadio has no post constraints
            this.__inputRadio15a = inputRadio15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputRadio15aSet = true;
            }
        }
        return inputRadio15a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputText()
    * @return boolean
    */
    protected abstract boolean handleIsInputText();

    private boolean __inputText16a;
    private boolean __inputText16aSet = false;

    /**
     * Indicates whether or not this parameter should be rendered as a text input widget.
     * @return (boolean)handleIsInputText()
     */
    public final boolean isInputText()
    {
        boolean inputText16a = this.__inputText16a;
        if (!this.__inputText16aSet)
        {
            // inputText has no pre constraints
            inputText16a = handleIsInputText();
            // inputText has no post constraints
            this.__inputText16a = inputText16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputText16aSet = true;
            }
        }
        return inputText16a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getBackingListName()
    * @return String
    */
    protected abstract String handleGetBackingListName();

    private String __backingListName17a;
    private boolean __backingListName17aSet = false;

    /**
     * The backing list name for this parameter. This is useful if you want to be able to select the
     * parameter value from a list (i.e. a drop-down select input type).
     * @return (String)handleGetBackingListName()
     */
    public final String getBackingListName()
    {
        String backingListName17a = this.__backingListName17a;
        if (!this.__backingListName17aSet)
        {
            // backingListName has no pre constraints
            backingListName17a = handleGetBackingListName();
            // backingListName has no post constraints
            this.__backingListName17a = backingListName17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__backingListName17aSet = true;
            }
        }
        return backingListName17a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getLabelListName()
    * @return String
    */
    protected abstract String handleGetLabelListName();

    private String __labelListName18a;
    private boolean __labelListName18aSet = false;

    /**
     * The name of the label list for this parameter. The label list name is the name of the list
     * storing the labels for the possible values of this parameter (typically used for the labels
     * of a drop-down select lists).
     * @return (String)handleGetLabelListName()
     */
    public final String getLabelListName()
    {
        String labelListName18a = this.__labelListName18a;
        if (!this.__labelListName18aSet)
        {
            // labelListName has no pre constraints
            labelListName18a = handleGetLabelListName();
            // labelListName has no post constraints
            this.__labelListName18a = labelListName18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__labelListName18aSet = true;
            }
        }
        return labelListName18a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getValueListName()
    * @return String
    */
    protected abstract String handleGetValueListName();

    private String __valueListName19a;
    private boolean __valueListName19aSet = false;

    /**
     * Stores the name of the value list for this parameter; this list stores the possible values
     * that this parameter may be (typically used for the values of a drop-down select list).
     * @return (String)handleGetValueListName()
     */
    public final String getValueListName()
    {
        String valueListName19a = this.__valueListName19a;
        if (!this.__valueListName19aSet)
        {
            // valueListName has no pre constraints
            valueListName19a = handleGetValueListName();
            // valueListName has no post constraints
            this.__valueListName19a = valueListName19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueListName19aSet = true;
            }
        }
        return valueListName19a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isSelectable()
    * @return boolean
    */
    protected abstract boolean handleIsSelectable();

    private boolean __selectable20a;
    private boolean __selectable20aSet = false;

    /**
     * Indicates whether or not this parameter is selectable or not (that is: it can be selected
     * from a list of values).
     * @return (boolean)handleIsSelectable()
     */
    public final boolean isSelectable()
    {
        boolean selectable20a = this.__selectable20a;
        if (!this.__selectable20aSet)
        {
            // selectable has no pre constraints
            selectable20a = handleIsSelectable();
            // selectable has no post constraints
            this.__selectable20a = selectable20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__selectable20aSet = true;
            }
        }
        return selectable20a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getDummyValue()
    * @return String
    */
    protected abstract String handleGetDummyValue();

    private String __dummyValue21a;
    private boolean __dummyValue21aSet = false;

    /**
     * The dummy value for this parameter. The dummy value is used for setting the dummy information
     * when dummyData is enabled.
     * @return (String)handleGetDummyValue()
     */
    public final String getDummyValue()
    {
        String dummyValue21a = this.__dummyValue21a;
        if (!this.__dummyValue21aSet)
        {
            // dummyValue has no pre constraints
            dummyValue21a = handleGetDummyValue();
            // dummyValue has no post constraints
            this.__dummyValue21a = dummyValue21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dummyValue21aSet = true;
            }
        }
        return dummyValue21a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getValueListDummyValue()
    * @return String
    */
    protected abstract String handleGetValueListDummyValue();

    private String __valueListDummyValue22a;
    private boolean __valueListDummyValue22aSet = false;

    /**
     * The dummy value for a value list.
     * @return (String)handleGetValueListDummyValue()
     */
    public final String getValueListDummyValue()
    {
        String valueListDummyValue22a = this.__valueListDummyValue22a;
        if (!this.__valueListDummyValue22aSet)
        {
            // valueListDummyValue has no pre constraints
            valueListDummyValue22a = handleGetValueListDummyValue();
            // valueListDummyValue has no post constraints
            this.__valueListDummyValue22a = valueListDummyValue22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueListDummyValue22aSet = true;
            }
        }
        return valueListDummyValue22a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getTableSortColumnProperty()
    * @return String
    */
    protected abstract String handleGetTableSortColumnProperty();

    private String __tableSortColumnProperty23a;
    private boolean __tableSortColumnProperty23aSet = false;

    /**
     * The name of the property storing the column to sort by if this parameter represents a table.
     * @return (String)handleGetTableSortColumnProperty()
     */
    public final String getTableSortColumnProperty()
    {
        String tableSortColumnProperty23a = this.__tableSortColumnProperty23a;
        if (!this.__tableSortColumnProperty23aSet)
        {
            // tableSortColumnProperty has no pre constraints
            tableSortColumnProperty23a = handleGetTableSortColumnProperty();
            // tableSortColumnProperty has no post constraints
            this.__tableSortColumnProperty23a = tableSortColumnProperty23a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableSortColumnProperty23aSet = true;
            }
        }
        return tableSortColumnProperty23a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getTableSortAscendingProperty()
    * @return String
    */
    protected abstract String handleGetTableSortAscendingProperty();

    private String __tableSortAscendingProperty24a;
    private boolean __tableSortAscendingProperty24aSet = false;

    /**
     * The name of the property that Indicates whether or not the table should be sorted ascending
     * (if this parameter represents a table).
     * @return (String)handleGetTableSortAscendingProperty()
     */
    public final String getTableSortAscendingProperty()
    {
        String tableSortAscendingProperty24a = this.__tableSortAscendingProperty24a;
        if (!this.__tableSortAscendingProperty24aSet)
        {
            // tableSortAscendingProperty has no pre constraints
            tableSortAscendingProperty24a = handleGetTableSortAscendingProperty();
            // tableSortAscendingProperty has no post constraints
            this.__tableSortAscendingProperty24a = tableSortAscendingProperty24a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__tableSortAscendingProperty24aSet = true;
            }
        }
        return tableSortAscendingProperty24a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getFormAttributeSetProperty()
    * @return String
    */
    protected abstract String handleGetFormAttributeSetProperty();

    private String __formAttributeSetProperty25a;
    private boolean __formAttributeSetProperty25aSet = false;

    /**
     * The name of the property used for indicating whether or not a form attribute has been set at
     * least once.
     * @return (String)handleGetFormAttributeSetProperty()
     */
    public final String getFormAttributeSetProperty()
    {
        String formAttributeSetProperty25a = this.__formAttributeSetProperty25a;
        if (!this.__formAttributeSetProperty25aSet)
        {
            // formAttributeSetProperty has no pre constraints
            formAttributeSetProperty25a = handleGetFormAttributeSetProperty();
            // formAttributeSetProperty has no post constraints
            this.__formAttributeSetProperty25a = formAttributeSetProperty25a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__formAttributeSetProperty25aSet = true;
            }
        }
        return formAttributeSetProperty25a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isReadOnly()
    * @return boolean
    */
    protected abstract boolean handleIsReadOnly();

    private boolean __readOnly26a;
    private boolean __readOnly26aSet = false;

    /**
     * Indicates if this parameter can only be read and not modified.
     * @return (boolean)handleIsReadOnly()
     */
    public final boolean isReadOnly()
    {
        boolean readOnly26a = this.__readOnly26a;
        if (!this.__readOnly26aSet)
        {
            // readOnly has no pre constraints
            readOnly26a = handleIsReadOnly();
            // readOnly has no post constraints
            this.__readOnly26a = readOnly26a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__readOnly26aSet = true;
            }
        }
        return readOnly26a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired27a;
    private boolean __validationRequired27aSet = false;

    /**
     * Indicates whether or not this parameter requires some kind of validation (the collection of
     * validator types is not empty).
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired27a = this.__validationRequired27a;
        if (!this.__validationRequired27aSet)
        {
            // validationRequired has no pre constraints
            validationRequired27a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired27a = validationRequired27a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired27aSet = true;
            }
        }
        return validationRequired27a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getValidatorTypes()
    * @return Collection
    */
    protected abstract Collection handleGetValidatorTypes();

    private Collection __validatorTypes28a;
    private boolean __validatorTypes28aSet = false;

    /**
     * All the validator types for this parameter.
     * @return (Collection)handleGetValidatorTypes()
     */
    public final Collection getValidatorTypes()
    {
        Collection validatorTypes28a = this.__validatorTypes28a;
        if (!this.__validatorTypes28aSet)
        {
            // validatorTypes has no pre constraints
            validatorTypes28a = handleGetValidatorTypes();
            // validatorTypes has no post constraints
            this.__validatorTypes28a = validatorTypes28a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validatorTypes28aSet = true;
            }
        }
        return validatorTypes28a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getValidWhen()
    * @return String
    */
    protected abstract String handleGetValidWhen();

    private String __validWhen29a;
    private boolean __validWhen29aSet = false;

    /**
     * The validator's 'validwhen' value, this is useful when the validation of a parameter depends
     * on the validation of others. See the apache commons-validator documentation for more
     * information.
     * @return (String)handleGetValidWhen()
     */
    public final String getValidWhen()
    {
        String validWhen29a = this.__validWhen29a;
        if (!this.__validWhen29aSet)
        {
            // validWhen has no pre constraints
            validWhen29a = handleGetValidWhen();
            // validWhen has no post constraints
            this.__validWhen29a = validWhen29a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validWhen29aSet = true;
            }
        }
        return validWhen29a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputFile()
    * @return boolean
    */
    protected abstract boolean handleIsInputFile();

    private boolean __inputFile30a;
    private boolean __inputFile30aSet = false;

    /**
     * Indicates whether or not this is a file input type.
     * @return (boolean)handleIsInputFile()
     */
    public final boolean isInputFile()
    {
        boolean inputFile30a = this.__inputFile30a;
        if (!this.__inputFile30aSet)
        {
            // inputFile has no pre constraints
            inputFile30a = handleIsInputFile();
            // inputFile has no post constraints
            this.__inputFile30a = inputFile30a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputFile30aSet = true;
            }
        }
        return inputFile30a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getValidatorVars()
    * @return Collection
    */
    protected abstract Collection handleGetValidatorVars();

    private Collection __validatorVars31a;
    private boolean __validatorVars31aSet = false;

    /**
     * The validator variables.
     * @return (Collection)handleGetValidatorVars()
     */
    public final Collection getValidatorVars()
    {
        Collection validatorVars31a = this.__validatorVars31a;
        if (!this.__validatorVars31aSet)
        {
            // validatorVars has no pre constraints
            validatorVars31a = handleGetValidatorVars();
            // validatorVars has no post constraints
            this.__validatorVars31a = validatorVars31a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validatorVars31aSet = true;
            }
        }
        return validatorVars31a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputMultibox()
    * @return boolean
    */
    protected abstract boolean handleIsInputMultibox();

    private boolean __inputMultibox32a;
    private boolean __inputMultibox32aSet = false;

    /**
     * Indicates whether or not this type represents an input multibox.
     * @return (boolean)handleIsInputMultibox()
     */
    public final boolean isInputMultibox()
    {
        boolean inputMultibox32a = this.__inputMultibox32a;
        if (!this.__inputMultibox32aSet)
        {
            // inputMultibox has no pre constraints
            inputMultibox32a = handleIsInputMultibox();
            // inputMultibox has no post constraints
            this.__inputMultibox32a = inputMultibox32a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputMultibox32aSet = true;
            }
        }
        return inputMultibox32a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isReset()
    * @return boolean
    */
    protected abstract boolean handleIsReset();

    private boolean __reset33a;
    private boolean __reset33aSet = false;

    /**
     * Indicates if this parameter's value should be reset or not after an action has been performed
     * with this parameter.
     * @return (boolean)handleIsReset()
     */
    public final boolean isReset()
    {
        boolean reset33a = this.__reset33a;
        if (!this.__reset33aSet)
        {
            // reset has no pre constraints
            reset33a = handleIsReset();
            // reset has no post constraints
            this.__reset33a = reset33a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__reset33aSet = true;
            }
        }
        return reset33a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isComplex()
    * @return boolean
    */
    protected abstract boolean handleIsComplex();

    private boolean __complex34a;
    private boolean __complex34aSet = false;

    /**
     * Indicates if this parameter is 'complex', that is: its of a complex type (has at least one
     * attribute or association).
     * @return (boolean)handleIsComplex()
     */
    public final boolean isComplex()
    {
        boolean complex34a = this.__complex34a;
        if (!this.__complex34aSet)
        {
            // complex has no pre constraints
            complex34a = handleIsComplex();
            // complex has no post constraints
            this.__complex34a = complex34a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__complex34aSet = true;
            }
        }
        return complex34a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getAttributes()
    * @return Collection
    */
    protected abstract Collection handleGetAttributes();

    private Collection __attributes35a;
    private boolean __attributes35aSet = false;

    /**
     * All attributes belonging to this parameter's type.
     * @return (Collection)handleGetAttributes()
     */
    public final Collection getAttributes()
    {
        Collection attributes35a = this.__attributes35a;
        if (!this.__attributes35aSet)
        {
            // attributes has no pre constraints
            attributes35a = handleGetAttributes();
            // attributes has no post constraints
            this.__attributes35a = attributes35a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__attributes35aSet = true;
            }
        }
        return attributes35a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getNavigableAssociationEnds()
    * @return Collection<AssociationEndFacade>
    */
    protected abstract Collection<AssociationEndFacade> handleGetNavigableAssociationEnds();

    private Collection<AssociationEndFacade> __navigableAssociationEnds36a;
    private boolean __navigableAssociationEnds36aSet = false;

    /**
     * All navigation association ends belonging to this parameter's type.
     * @return (Collection<AssociationEndFacade>)handleGetNavigableAssociationEnds()
     */
    public final Collection<AssociationEndFacade> getNavigableAssociationEnds()
    {
        Collection<AssociationEndFacade> navigableAssociationEnds36a = this.__navigableAssociationEnds36a;
        if (!this.__navigableAssociationEnds36aSet)
        {
            // navigableAssociationEnds has no pre constraints
            navigableAssociationEnds36a = handleGetNavigableAssociationEnds();
            // navigableAssociationEnds has no post constraints
            this.__navigableAssociationEnds36a = navigableAssociationEnds36a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__navigableAssociationEnds36aSet = true;
            }
        }
        return navigableAssociationEnds36a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isEqualValidator()
    * @return boolean
    */
    protected abstract boolean handleIsEqualValidator();

    private boolean __equalValidator37a;
    private boolean __equalValidator37aSet = false;

    /**
     * Indicates whether or not this parameter uses the equal validator.
     * @return (boolean)handleIsEqualValidator()
     */
    public final boolean isEqualValidator()
    {
        boolean equalValidator37a = this.__equalValidator37a;
        if (!this.__equalValidator37aSet)
        {
            // equalValidator has no pre constraints
            equalValidator37a = handleIsEqualValidator();
            // equalValidator has no post constraints
            this.__equalValidator37a = equalValidator37a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__equalValidator37aSet = true;
            }
        }
        return equalValidator37a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getBackingValueName()
    * @return String
    */
    protected abstract String handleGetBackingValueName();

    private String __backingValueName38a;
    private boolean __backingValueName38aSet = false;

    /**
     * The name of the backing value for this parameter (only used with collections and arrays that
     * are input type table).
     * @return (String)handleGetBackingValueName()
     */
    public final String getBackingValueName()
    {
        String backingValueName38a = this.__backingValueName38a;
        if (!this.__backingValueName38aSet)
        {
            // backingValueName has no pre constraints
            backingValueName38a = handleGetBackingValueName();
            // backingValueName has no post constraints
            this.__backingValueName38a = backingValueName38a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__backingValueName38aSet = true;
            }
        }
        return backingValueName38a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isInputTable()
    * @return boolean
    */
    protected abstract boolean handleIsInputTable();

    private boolean __inputTable39a;
    private boolean __inputTable39aSet = false;

    /**
     * Indicates whether or not this is an table input type.
     * @return (boolean)handleIsInputTable()
     */
    public final boolean isInputTable()
    {
        boolean inputTable39a = this.__inputTable39a;
        if (!this.__inputTable39aSet)
        {
            // inputTable has no pre constraints
            inputTable39a = handleIsInputTable();
            // inputTable has no post constraints
            this.__inputTable39a = inputTable39a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTable39aSet = true;
            }
        }
        return inputTable39a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isBackingValueRequired()
    * @return boolean
    */
    protected abstract boolean handleIsBackingValueRequired();

    private boolean __backingValueRequired40a;
    private boolean __backingValueRequired40aSet = false;

    /**
     * Indicates if a backing value is required for this parameter.
     * @return (boolean)handleIsBackingValueRequired()
     */
    public final boolean isBackingValueRequired()
    {
        boolean backingValueRequired40a = this.__backingValueRequired40a;
        if (!this.__backingValueRequired40aSet)
        {
            // backingValueRequired has no pre constraints
            backingValueRequired40a = handleIsBackingValueRequired();
            // backingValueRequired has no post constraints
            this.__backingValueRequired40a = backingValueRequired40a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__backingValueRequired40aSet = true;
            }
        }
        return backingValueRequired40a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getInputTableIdentifierColumns()
    * @return String
    */
    protected abstract String handleGetInputTableIdentifierColumns();

    private String __inputTableIdentifierColumns41a;
    private boolean __inputTableIdentifierColumns41aSet = false;

    /**
     * A comma separated list of the input table identifier columns (these are the columns that
     * uniquely define a row in an input table).
     * @return (String)handleGetInputTableIdentifierColumns()
     */
    public final String getInputTableIdentifierColumns()
    {
        String inputTableIdentifierColumns41a = this.__inputTableIdentifierColumns41a;
        if (!this.__inputTableIdentifierColumns41aSet)
        {
            // inputTableIdentifierColumns has no pre constraints
            inputTableIdentifierColumns41a = handleGetInputTableIdentifierColumns();
            // inputTableIdentifierColumns has no post constraints
            this.__inputTableIdentifierColumns41a = inputTableIdentifierColumns41a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTableIdentifierColumns41aSet = true;
            }
        }
        return inputTableIdentifierColumns41a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#isPageableTable()
    * @return boolean
    */
    protected abstract boolean handleIsPageableTable();

    private boolean __pageableTable42a;
    private boolean __pageableTable42aSet = false;

    /**
     * Whether or not the parameter is a "pageable table", that is a table that supports paging
     * (i.e. DB paging).
     * @return (boolean)handleIsPageableTable()
     */
    public final boolean isPageableTable()
    {
        boolean pageableTable42a = this.__pageableTable42a;
        if (!this.__pageableTable42aSet)
        {
            // pageableTable has no pre constraints
            pageableTable42a = handleIsPageableTable();
            // pageableTable has no post constraints
            this.__pageableTable42a = pageableTable42a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__pageableTable42aSet = true;
            }
        }
        return pageableTable42a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getMaxLength()
    * @return String
    */
    protected abstract String handleGetMaxLength();

    private String __maxLength43a;
    private boolean __maxLength43aSet = false;

    /**
     * The max length allowed in the input component
     * @return (String)handleGetMaxLength()
     */
    public final String getMaxLength()
    {
        String maxLength43a = this.__maxLength43a;
        if (!this.__maxLength43aSet)
        {
            // maxLength has no pre constraints
            maxLength43a = handleGetMaxLength();
            // maxLength has no post constraints
            this.__maxLength43a = maxLength43a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__maxLength43aSet = true;
            }
        }
        return maxLength43a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFParameter#getAnnotations()
    * @return Collection
    */
    protected abstract Collection handleGetAnnotations();

    private Collection __annotations44a;
    private boolean __annotations44aSet = false;

    /**
     * All the annotations for this parameter.
     * @return (Collection)handleGetAnnotations()
     */
    public final Collection getAnnotations()
    {
        Collection annotations44a = this.__annotations44a;
        if (!this.__annotations44aSet)
        {
            // annotations has no pre constraints
            annotations44a = handleGetAnnotations();
            // annotations has no post constraints
            this.__annotations44a = annotations44a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__annotations44aSet = true;
            }
        }
        return annotations44a;
    }

    // ---------------- business methods ----------------------

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFParameter.getTableColumnMessageKey
     * @param columnName
     * @return String
     */
    protected abstract String handleGetTableColumnMessageKey(String columnName);

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFParameter.getTableColumnMessageKey
     * @param columnName String
     * The name of the table column.
     * @return handleGetTableColumnMessageKey(columnName)
     */
    public String getTableColumnMessageKey(String columnName)
    {
        // getTableColumnMessageKey has no pre constraints
        String returnValue = handleGetTableColumnMessageKey(columnName);
        // getTableColumnMessageKey has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFParameter.getTableColumnMessageValue
     * @param columnName
     * @return String
     */
    protected abstract String handleGetTableColumnMessageValue(String columnName);

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFParameter.getTableColumnMessageValue
     * @param columnName String
     * Returns the resource bundle value for this table's column, only returns a value when this
     * parameter is a table.
     * @return handleGetTableColumnMessageValue(columnName)
     */
    public String getTableColumnMessageValue(String columnName)
    {
        // getTableColumnMessageValue has no pre constraints
        String returnValue = handleGetTableColumnMessageValue(columnName);
        // getTableColumnMessageValue has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Gets the arguments for this parameter's validators.
     * @param validatorType
     * @return Collection
     */
    protected abstract Collection handleGetValidatorArgs(String validatorType);

    /**
     * Gets the arguments for this parameter's validators.
     * @param validatorType String
     * The type of the validator.
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
     * Those actions that are targetting the given column, only makes sense when this parameter
     * represents a table view-variable.
     * @param columnName
     * @return List
     */
    protected abstract List handleGetTableColumnActions(String columnName);

    /**
     * Those actions that are targetting the given column, only makes sense when this parameter
     * represents a table view-variable.
     * @param columnName String
     * The column name of the column that the retrieved actions target.
     * @return handleGetTableColumnActions(columnName)
     */
    public List getTableColumnActions(String columnName)
    {
        // getTableColumnActions has no pre constraints
        List returnValue = handleGetTableColumnActions(columnName);
        // getTableColumnActions has no post constraints
        return returnValue;
    }

    // ------------- associations ------------------

    private List<JSFAction> __getTableHyperlinkActions1r;
    private boolean __getTableHyperlinkActions1rSet = false;

    /**
     * Represents a parameter in a JSF front-end.
     * @return (List<JSFAction>)handleGetTableHyperlinkActions()
     */
    public final List<JSFAction> getTableHyperlinkActions()
    {
        List<JSFAction> getTableHyperlinkActions1r = this.__getTableHyperlinkActions1r;
        if (!this.__getTableHyperlinkActions1rSet)
        {
            // jSFParameter has no pre constraints
            List result = handleGetTableHyperlinkActions();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getTableHyperlinkActions1r = (List<JSFAction>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFParameterLogic.logger.warn("incorrect metafacade cast for JSFParameterLogic.getTableHyperlinkActions List<JSFAction> " + result + ": " + shieldedResult);
            }
            // jSFParameter has no post constraints
            this.__getTableHyperlinkActions1r = getTableHyperlinkActions1r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getTableHyperlinkActions1rSet = true;
            }
        }
        return getTableHyperlinkActions1r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetTableHyperlinkActions();

    private List<JSFAction> __getTableFormActions2r;
    private boolean __getTableFormActions2rSet = false;

    /**
     * Represents a parameter in a JSF front-end.
     * @return (List<JSFAction>)handleGetTableFormActions()
     */
    public final List<JSFAction> getTableFormActions()
    {
        List<JSFAction> getTableFormActions2r = this.__getTableFormActions2r;
        if (!this.__getTableFormActions2rSet)
        {
            // jSFParameter has no pre constraints
            List result = handleGetTableFormActions();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getTableFormActions2r = (List<JSFAction>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFParameterLogic.logger.warn("incorrect metafacade cast for JSFParameterLogic.getTableFormActions List<JSFAction> " + result + ": " + shieldedResult);
            }
            // jSFParameter has no post constraints
            this.__getTableFormActions2r = getTableFormActions2r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getTableFormActions2rSet = true;
            }
        }
        return getTableFormActions2r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetTableFormActions();

    private List<JSFAction> __getTableActions3r;
    private boolean __getTableActions3rSet = false;

    /**
     * Represents a parameter in a JSF front-end.
     * @return (List<JSFAction>)handleGetTableActions()
     */
    public final List<JSFAction> getTableActions()
    {
        List<JSFAction> getTableActions3r = this.__getTableActions3r;
        if (!this.__getTableActions3rSet)
        {
            // jSFParameter has no pre constraints
            List result = handleGetTableActions();
            List shieldedResult = this.shieldedElements(result);
            try
            {
                getTableActions3r = (List<JSFAction>)shieldedResult;
            }
            catch (ClassCastException ex)
            {
                // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
                JSFParameterLogic.logger.warn("incorrect metafacade cast for JSFParameterLogic.getTableActions List<JSFAction> " + result + ": " + shieldedResult);
            }
            // jSFParameter has no post constraints
            this.__getTableActions3r = getTableActions3r;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__getTableActions3rSet = true;
            }
        }
        return getTableActions3r;
    }

    /**
     * UML Specific type is returned in Collection, transformed by shieldedElements to AndroMDA Metafacade type
     * @return  List
     */
    protected abstract List handleGetTableActions();

    /**
     * @return true
     * @see FrontEndParameter
     */
    public boolean isFrontEndParameterMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.ParameterFacade
     */
    public boolean isParameterFacadeMetaType()
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

    // ----------- delegates to FrontEndParameter ------------
    /**
     * The action to which this parameter belongs (if it belongs to an action), otherwise it returns
     * null.
     * @see FrontEndParameter#getAction()
     */
    public FrontEndAction getAction()
    {
        return this.getSuperFrontEndParameter().getAction();
    }

    /**
     * Gets the controller operation to which this parameter belongs.
     * @see FrontEndParameter#getControllerOperation()
     */
    public FrontEndControllerOperation getControllerOperation()
    {
        return this.getSuperFrontEndParameter().getControllerOperation();
    }

    /**
     * A collection of all possible attribute names of a table (this will only work when your table
     * is modeled as an array..not a collection).
     * @see FrontEndParameter#getTableAttributeNames()
     */
    public Collection<String> getTableAttributeNames()
    {
        return this.getSuperFrontEndParameter().getTableAttributeNames();
    }

    /**
     * All the columns for this parameter if it represents a table variable. If a column is linked
     * by an event (action) a FrontEndParameter instance is included in the return value, otherwise
     * a plain String representing the column name.
     * @see FrontEndParameter#getTableColumnNames()
     */
    public Collection<String> getTableColumnNames()
    {
        return this.getSuperFrontEndParameter().getTableColumnNames();
    }

    /**
     * A list of all attributes which make up the table columns of this table (this only contains
     * attributes when the table is represented by an array).
     * @see FrontEndParameter#getTableColumns()
     */
    public Collection<String> getTableColumns()
    {
        return this.getSuperFrontEndParameter().getTableColumns();
    }

    /**
     * Represents the view in which this parameter will be used.
     * @see FrontEndParameter#getView()
     */
    public FrontEndView getView()
    {
        return this.getSuperFrontEndParameter().getView();
    }

    /**
     * Indicates whether or not this is an action parameter or not.
     * @see FrontEndParameter#isActionParameter()
     */
    public boolean isActionParameter()
    {
        return this.getSuperFrontEndParameter().isActionParameter();
    }

    /**
     * Indicates if this parameter is contained in a "front-end" use case.
     * @see FrontEndParameter#isContainedInFrontEndUseCase()
     */
    public boolean isContainedInFrontEndUseCase()
    {
        return this.getSuperFrontEndParameter().isContainedInFrontEndUseCase();
    }

    /**
     * Indicates whether or not this parameter is an argument of a controller operation.
     * @see FrontEndParameter#isControllerOperationArgument()
     */
    public boolean isControllerOperationArgument()
    {
        return this.getSuperFrontEndParameter().isControllerOperationArgument();
    }

    /**
     * Indicates whether or not this parameter represents a table.
     * @see FrontEndParameter#isTable()
     */
    public boolean isTable()
    {
        return this.getSuperFrontEndParameter().isTable();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperFrontEndParameter().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperFrontEndParameter().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperFrontEndParameter().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperFrontEndParameter().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperFrontEndParameter().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperFrontEndParameter().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperFrontEndParameter().getDocumentation(indent);
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
        return this.getSuperFrontEndParameter().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperFrontEndParameter().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperFrontEndParameter().getFullyQualifiedName();
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
        return this.getSuperFrontEndParameter().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperFrontEndParameter().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperFrontEndParameter().getId();
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
        return this.getSuperFrontEndParameter().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperFrontEndParameter().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperFrontEndParameter().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperFrontEndParameter().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperFrontEndParameter().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperFrontEndParameter().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperFrontEndParameter().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperFrontEndParameter().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperFrontEndParameter().getPackagePath();
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
        return this.getSuperFrontEndParameter().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperFrontEndParameter().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperFrontEndParameter().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperFrontEndParameter().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperFrontEndParameter().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperFrontEndParameter().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperFrontEndParameter().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperFrontEndParameter().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperFrontEndParameter().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperFrontEndParameter().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperFrontEndParameter().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperFrontEndParameter().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperFrontEndParameter().hasKeyword(keywordName);
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
        return this.getSuperFrontEndParameter().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperFrontEndParameter().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperFrontEndParameter().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperFrontEndParameter().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperFrontEndParameter().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperFrontEndParameter().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperFrontEndParameter().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperFrontEndParameter().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperFrontEndParameter().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperFrontEndParameter().translateConstraints(kind, translation);
    }

    /**
     * TODO: Model Documentation for org.andromda.metafacades.uml.ParameterFacade.defaultValue
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        return this.getSuperFrontEndParameter().getDefaultValue();
    }

    /**
     * UML2: A representation of the literals of the enumeration 'Parameter Effect Kind': CREATE,
     * READ, UPDATE, DELETE. The datatype ParameterEffectKind is an enumeration that indicates the
     * effect of a behavior on values passed in or out of its parameters.
     * @see org.andromda.metafacades.uml.ParameterFacade#getEffect()
     */
    public String getEffect()
    {
        return this.getSuperFrontEndParameter().getEffect();
    }

    /**
     * If this parameter is located on an event, this will represent that event.
     * @see org.andromda.metafacades.uml.ParameterFacade#getEvent()
     */
    public EventFacade getEvent()
    {
        return this.getSuperFrontEndParameter().getEvent();
    }

    /**
     * The name to use for accessors getting this parameter from a bean.
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterName()
     */
    public String getGetterName()
    {
        return this.getSuperFrontEndParameter().getGetterName();
    }

    /**
     * Fully Qualified TypeName, determined in part by multiplicity (for UML2). For UML14, same as
     * getterName.
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        return this.getSuperFrontEndParameter().getGetterSetterTypeName();
    }

    /**
     * Fully Qualified implementation class of TypeName, determined in part by multiplicity (for
     * UML2). If upper multiplicity =1, same as getterSetterTypeName.
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterSetterTypeNameImpl()
     */
    public String getGetterSetterTypeNameImpl()
    {
        return this.getSuperFrontEndParameter().getGetterSetterTypeNameImpl();
    }

    /**
     * the lower value for the multiplicity
     * -only applicable for UML2
     * @see org.andromda.metafacades.uml.ParameterFacade#getLower()
     */
    public int getLower()
    {
        return this.getSuperFrontEndParameter().getLower();
    }

    /**
     * If this parameter is located on an operation, this will represent that operation.
     * @see org.andromda.metafacades.uml.ParameterFacade#getOperation()
     */
    public OperationFacade getOperation()
    {
        return this.getSuperFrontEndParameter().getOperation();
    }

    /**
     * The name to use for accessors getting this parameter in a bean.
     * @see org.andromda.metafacades.uml.ParameterFacade#getSetterName()
     */
    public String getSetterName()
    {
        return this.getSuperFrontEndParameter().getSetterName();
    }

    /**
     * A Classifier is a classification of instances - it describes a set of instances that have
     * features
     * in common. Can specify a generalization hierarchy by referencing its general classifiers. It
     * may be
     * a Class, DataType, PrimitiveType, Association, Collaboration, UseCase, etc. Can specify a
     * generalization hierarchy by referencing its general classifiers. Has the capability to own
     * collaboration uses. These collaboration uses link a collaboration with the classifier to give
     * a
     * description of the workings of the classifier. Classifier is defined to be a kind of
     * templateable
     * element so that a classifier can be parameterized. It is also defined to be a kind of
     * parameterable
     * element so that a classifier can be a formal template parameter.
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    public ClassifierFacade getType()
    {
        return this.getSuperFrontEndParameter().getType();
    }

    /**
     * the upper value of the multiplicity (will be -1 for *)
     * -only applicable for UML2
     * @see org.andromda.metafacades.uml.ParameterFacade#getUpper()
     */
    public int getUpper()
    {
        return this.getSuperFrontEndParameter().getUpper();
    }

    /**
     * Indicates if the default value is present.
     * @see org.andromda.metafacades.uml.ParameterFacade#isDefaultValuePresent()
     */
    public boolean isDefaultValuePresent()
    {
        return this.getSuperFrontEndParameter().isDefaultValuePresent();
    }

    /**
     * UML2: Returns the value of the 'Is Exception' attribute. The default value is "false". Tells
     * whether an output parameter may emit a value to the exclusion of the other outputs.
     * @see org.andromda.metafacades.uml.ParameterFacade#isException()
     */
    public boolean isException()
    {
        return this.getSuperFrontEndParameter().isException();
    }

    /**
     * True if this parameter is an 'in' parameter.
     * @see org.andromda.metafacades.uml.ParameterFacade#isInParameter()
     */
    public boolean isInParameter()
    {
        return this.getSuperFrontEndParameter().isInParameter();
    }

    /**
     * True if this parameter is an inout parameter.
     * @see org.andromda.metafacades.uml.ParameterFacade#isInoutParameter()
     */
    public boolean isInoutParameter()
    {
        return this.getSuperFrontEndParameter().isInoutParameter();
    }

    /**
     * If upper>1 or upper==unlimited. Only applies to UML2. For UML14, always false.
     * @see org.andromda.metafacades.uml.ParameterFacade#isMany()
     */
    public boolean isMany()
    {
        return this.getSuperFrontEndParameter().isMany();
    }

    /**
     * UML2 Only: Is parameter ordered within the Collection type. Ordered+Unique determines the
     * implementation Collection Type. For UML14, always false.
     * @see org.andromda.metafacades.uml.ParameterFacade#isOrdered()
     */
    public boolean isOrdered()
    {
        return this.getSuperFrontEndParameter().isOrdered();
    }

    /**
     * True if this parameter is an 'out' parameter.
     * @see org.andromda.metafacades.uml.ParameterFacade#isOutParameter()
     */
    public boolean isOutParameter()
    {
        return this.getSuperFrontEndParameter().isOutParameter();
    }

    /**
     * True if this parameter is readable, aka an in-parameter, or this feature is unspecified.
     * @see org.andromda.metafacades.uml.ParameterFacade#isReadable()
     */
    public boolean isReadable()
    {
        return this.getSuperFrontEndParameter().isReadable();
    }

    /**
     * Whether or not this parameter is considered required (i.e must a non-empty value).
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    public boolean isRequired()
    {
        return this.getSuperFrontEndParameter().isRequired();
    }

    /**
     * Whether or not this parameter represents a return parameter.
     * @see org.andromda.metafacades.uml.ParameterFacade#isReturn()
     */
    public boolean isReturn()
    {
        return this.getSuperFrontEndParameter().isReturn();
    }

    /**
     * If Parameter type isMany (UML2), is the parameter unique within the Collection. Unique+Sorted
     * determines pareter implementation type. For UML14, always false.
     * @see org.andromda.metafacades.uml.ParameterFacade#isUnique()
     */
    public boolean isUnique()
    {
        return this.getSuperFrontEndParameter().isUnique();
    }

    /**
     * True if this parameter is writable, aka an out-parameter, or this feature is unspecified.
     * @see org.andromda.metafacades.uml.ParameterFacade#isWritable()
     */
    public boolean isWritable()
    {
        return this.getSuperFrontEndParameter().isWritable();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperFrontEndParameter().initialize();
    }

    /**
     * @return Object getSuperFrontEndParameter().getValidationOwner()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperFrontEndParameter().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperFrontEndParameter().getValidationName()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperFrontEndParameter().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperFrontEndParameter().validateInvariants(validationMessages);
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