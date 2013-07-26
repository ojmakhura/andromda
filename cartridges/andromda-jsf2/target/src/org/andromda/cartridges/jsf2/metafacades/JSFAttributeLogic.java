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
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.FrontEndParameter;
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
 * Represents an attribute on a classifier used by a JSF application.
 * MetafacadeLogic for JSFAttribute
 *
 * @see JSFAttribute
 */
public abstract class JSFAttributeLogic
    extends MetafacadeBase
    implements JSFAttribute
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
    protected JSFAttributeLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superAttributeFacade =
           (AttributeFacade)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.AttributeFacade",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFAttribute if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFAttribute";
        }
        return context;
    }

    private AttributeFacade superAttributeFacade;
    private boolean superAttributeFacadeInitialized = false;

    /**
     * Gets the AttributeFacade parent instance.
     * @return this.superAttributeFacade AttributeFacade
     */
    private AttributeFacade getSuperAttributeFacade()
    {
        if (!this.superAttributeFacadeInitialized)
        {
            ((MetafacadeBase)this.superAttributeFacade).setMetafacadeContext(this.getMetafacadeContext());
            this.superAttributeFacadeInitialized = true;
        }
        return this.superAttributeFacade;
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
            if (this.superAttributeFacadeInitialized)
            {
                ((MetafacadeBase)this.superAttributeFacade).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFAttribute
     */
    public boolean isJSFAttributeMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey1a;
    private boolean __messageKey1aSet = false;

    /**
     * The message key for this attribute.
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
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getMessageValue()
    * @return String
    */
    protected abstract String handleGetMessageValue();

    private String __messageValue2a;
    private boolean __messageValue2aSet = false;

    /**
     * The default value for the message key.
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
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getDummyValue()
    * @return String
    */
    protected abstract String handleGetDummyValue();

    private String __dummyValue3a;
    private boolean __dummyValue3aSet = false;

    /**
     * The dummy value to give the attribute when creating a dummy instance of this attribute's
     * owner.
     * @return (String)handleGetDummyValue()
     */
    public final String getDummyValue()
    {
        String dummyValue3a = this.__dummyValue3a;
        if (!this.__dummyValue3aSet)
        {
            // dummyValue has no pre constraints
            dummyValue3a = handleGetDummyValue();
            // dummyValue has no post constraints
            this.__dummyValue3a = dummyValue3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__dummyValue3aSet = true;
            }
        }
        return dummyValue3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getFormat()
    * @return String
    */
    protected abstract String handleGetFormat();

    private String __format4a;
    private boolean __format4aSet = false;

    /**
     * If this attributes represents a date or time this method will return the format in which it
     * must be represented. In the event this format has not been specified by the any tagged value
     * the default will be used.
     * @return (String)handleGetFormat()
     */
    public final String getFormat()
    {
        String format4a = this.__format4a;
        if (!this.__format4aSet)
        {
            // format has no pre constraints
            format4a = handleGetFormat();
            // format has no post constraints
            this.__format4a = format4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__format4aSet = true;
            }
        }
        return format4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputTextarea()
    * @return boolean
    */
    protected abstract boolean handleIsInputTextarea();

    private boolean __inputTextarea5a;
    private boolean __inputTextarea5aSet = false;

    /**
     * Indicates if this parameter represents as an input text area widget.
     * @return (boolean)handleIsInputTextarea()
     */
    public final boolean isInputTextarea()
    {
        boolean inputTextarea5a = this.__inputTextarea5a;
        if (!this.__inputTextarea5aSet)
        {
            // inputTextarea has no pre constraints
            inputTextarea5a = handleIsInputTextarea();
            // inputTextarea has no post constraints
            this.__inputTextarea5a = inputTextarea5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTextarea5aSet = true;
            }
        }
        return inputTextarea5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputText()
    * @return boolean
    */
    protected abstract boolean handleIsInputText();

    private boolean __inputText6a;
    private boolean __inputText6aSet = false;

    /**
     * Indicates whether or not this parameter should be rendered as a text input widget.
     * @return (boolean)handleIsInputText()
     */
    public final boolean isInputText()
    {
        boolean inputText6a = this.__inputText6a;
        if (!this.__inputText6aSet)
        {
            // inputText has no pre constraints
            inputText6a = handleIsInputText();
            // inputText has no post constraints
            this.__inputText6a = inputText6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputText6aSet = true;
            }
        }
        return inputText6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputSecret()
    * @return boolean
    */
    protected abstract boolean handleIsInputSecret();

    private boolean __inputSecret7a;
    private boolean __inputSecret7aSet = false;

    /**
     * Indicates whether or not this parameter represents an input "secret" widget (i.e. password).
     * @return (boolean)handleIsInputSecret()
     */
    public final boolean isInputSecret()
    {
        boolean inputSecret7a = this.__inputSecret7a;
        if (!this.__inputSecret7aSet)
        {
            // inputSecret has no pre constraints
            inputSecret7a = handleIsInputSecret();
            // inputSecret has no post constraints
            this.__inputSecret7a = inputSecret7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputSecret7aSet = true;
            }
        }
        return inputSecret7a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputSelect()
    * @return boolean
    */
    protected abstract boolean handleIsInputSelect();

    private boolean __inputSelect8a;
    private boolean __inputSelect8aSet = false;

    /**
     * Indicates whether or not this parameter represents an input select widget.
     * @return (boolean)handleIsInputSelect()
     */
    public final boolean isInputSelect()
    {
        boolean inputSelect8a = this.__inputSelect8a;
        if (!this.__inputSelect8aSet)
        {
            // inputSelect has no pre constraints
            inputSelect8a = handleIsInputSelect();
            // inputSelect has no post constraints
            this.__inputSelect8a = inputSelect8a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputSelect8aSet = true;
            }
        }
        return inputSelect8a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputRadio()
    * @return boolean
    */
    protected abstract boolean handleIsInputRadio();

    private boolean __inputRadio9a;
    private boolean __inputRadio9aSet = false;

    /**
     * Indicates whether or not this parameter should be rendered as an input radio widget.
     * @return (boolean)handleIsInputRadio()
     */
    public final boolean isInputRadio()
    {
        boolean inputRadio9a = this.__inputRadio9a;
        if (!this.__inputRadio9aSet)
        {
            // inputRadio has no pre constraints
            inputRadio9a = handleIsInputRadio();
            // inputRadio has no post constraints
            this.__inputRadio9a = inputRadio9a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputRadio9aSet = true;
            }
        }
        return inputRadio9a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputMultibox()
    * @return boolean
    */
    protected abstract boolean handleIsInputMultibox();

    private boolean __inputMultibox10a;
    private boolean __inputMultibox10aSet = false;

    /**
     * Indicates whether or not this type represents an input multibox.
     * @return (boolean)handleIsInputMultibox()
     */
    public final boolean isInputMultibox()
    {
        boolean inputMultibox10a = this.__inputMultibox10a;
        if (!this.__inputMultibox10aSet)
        {
            // inputMultibox has no pre constraints
            inputMultibox10a = handleIsInputMultibox();
            // inputMultibox has no post constraints
            this.__inputMultibox10a = inputMultibox10a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputMultibox10aSet = true;
            }
        }
        return inputMultibox10a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputHidden()
    * @return boolean
    */
    protected abstract boolean handleIsInputHidden();

    private boolean __inputHidden11a;
    private boolean __inputHidden11aSet = false;

    /**
     * Indicates whether or not this parameter represents a hidden input widget.
     * @return (boolean)handleIsInputHidden()
     */
    public final boolean isInputHidden()
    {
        boolean inputHidden11a = this.__inputHidden11a;
        if (!this.__inputHidden11aSet)
        {
            // inputHidden has no pre constraints
            inputHidden11a = handleIsInputHidden();
            // inputHidden has no post constraints
            this.__inputHidden11a = inputHidden11a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputHidden11aSet = true;
            }
        }
        return inputHidden11a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputFile()
    * @return boolean
    */
    protected abstract boolean handleIsInputFile();

    private boolean __inputFile12a;
    private boolean __inputFile12aSet = false;

    /**
     * Indicates whether or not this is a file input type.
     * @return (boolean)handleIsInputFile()
     */
    public final boolean isInputFile()
    {
        boolean inputFile12a = this.__inputFile12a;
        if (!this.__inputFile12aSet)
        {
            // inputFile has no pre constraints
            inputFile12a = handleIsInputFile();
            // inputFile has no post constraints
            this.__inputFile12a = inputFile12a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputFile12aSet = true;
            }
        }
        return inputFile12a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputCheckbox()
    * @return boolean
    */
    protected abstract boolean handleIsInputCheckbox();

    private boolean __inputCheckbox13a;
    private boolean __inputCheckbox13aSet = false;

    /**
     * Indicates if this parameter represents a checkbox widget.
     * @return (boolean)handleIsInputCheckbox()
     */
    public final boolean isInputCheckbox()
    {
        boolean inputCheckbox13a = this.__inputCheckbox13a;
        if (!this.__inputCheckbox13aSet)
        {
            // inputCheckbox has no pre constraints
            inputCheckbox13a = handleIsInputCheckbox();
            // inputCheckbox has no post constraints
            this.__inputCheckbox13a = inputCheckbox13a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputCheckbox13aSet = true;
            }
        }
        return inputCheckbox13a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getValueListDummyValue()
    * @return String
    */
    protected abstract String handleGetValueListDummyValue();

    private String __valueListDummyValue14a;
    private boolean __valueListDummyValue14aSet = false;

    /**
     * The dummy value for a value list.
     * @return (String)handleGetValueListDummyValue()
     */
    public final String getValueListDummyValue()
    {
        String valueListDummyValue14a = this.__valueListDummyValue14a;
        if (!this.__valueListDummyValue14aSet)
        {
            // valueListDummyValue has no pre constraints
            valueListDummyValue14a = handleGetValueListDummyValue();
            // valueListDummyValue has no post constraints
            this.__valueListDummyValue14a = valueListDummyValue14a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueListDummyValue14aSet = true;
            }
        }
        return valueListDummyValue14a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getValidWhen()
    * @return String
    */
    protected abstract String handleGetValidWhen();

    private String __validWhen15a;
    private boolean __validWhen15aSet = false;

    /**
     * The validator's 'validwhen' value, this is useful when the validation of a parameter depends
     * on the validation of others. See the apache commons-validator documentation for more
     * information.
     * @return (String)handleGetValidWhen()
     */
    public final String getValidWhen()
    {
        String validWhen15a = this.__validWhen15a;
        if (!this.__validWhen15aSet)
        {
            // validWhen has no pre constraints
            validWhen15a = handleGetValidWhen();
            // validWhen has no post constraints
            this.__validWhen15a = validWhen15a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validWhen15aSet = true;
            }
        }
        return validWhen15a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getValidatorTypes()
    * @return Collection
    */
    protected abstract Collection handleGetValidatorTypes();

    private Collection __validatorTypes16a;
    private boolean __validatorTypes16aSet = false;

    /**
     * All validator types for this attribute.
     * @return (Collection)handleGetValidatorTypes()
     */
    public final Collection getValidatorTypes()
    {
        Collection validatorTypes16a = this.__validatorTypes16a;
        if (!this.__validatorTypes16aSet)
        {
            // validatorTypes has no pre constraints
            validatorTypes16a = handleGetValidatorTypes();
            // validatorTypes has no post constraints
            this.__validatorTypes16a = validatorTypes16a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validatorTypes16aSet = true;
            }
        }
        return validatorTypes16a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isValidationRequired()
    * @return boolean
    */
    protected abstract boolean handleIsValidationRequired();

    private boolean __validationRequired17a;
    private boolean __validationRequired17aSet = false;

    /**
     * Indicates whether or not this attribute requires some kind of validation (the collection of
     * validator types is not empty).
     * @return (boolean)handleIsValidationRequired()
     */
    public final boolean isValidationRequired()
    {
        boolean validationRequired17a = this.__validationRequired17a;
        if (!this.__validationRequired17aSet)
        {
            // validationRequired has no pre constraints
            validationRequired17a = handleIsValidationRequired();
            // validationRequired has no post constraints
            this.__validationRequired17a = validationRequired17a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__validationRequired17aSet = true;
            }
        }
        return validationRequired17a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isStrictDateFormat()
    * @return boolean
    */
    protected abstract boolean handleIsStrictDateFormat();

    private boolean __strictDateFormat18a;
    private boolean __strictDateFormat18aSet = false;

    /**
     * Indicates where or not the date format is to be strictly respected. Otherwise the date
     * formatter used for the representation of this date is to be set to lenient.
     * @return (boolean)handleIsStrictDateFormat()
     */
    public final boolean isStrictDateFormat()
    {
        boolean strictDateFormat18a = this.__strictDateFormat18a;
        if (!this.__strictDateFormat18aSet)
        {
            // strictDateFormat has no pre constraints
            strictDateFormat18a = handleIsStrictDateFormat();
            // strictDateFormat has no post constraints
            this.__strictDateFormat18a = strictDateFormat18a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__strictDateFormat18aSet = true;
            }
        }
        return strictDateFormat18a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isEqualValidator()
    * @return boolean
    */
    protected abstract boolean handleIsEqualValidator();

    private boolean __equalValidator19a;
    private boolean __equalValidator19aSet = false;

    /**
     * Indicates whether or not this parameter uses the equal validator.
     * @return (boolean)handleIsEqualValidator()
     */
    public final boolean isEqualValidator()
    {
        boolean equalValidator19a = this.__equalValidator19a;
        if (!this.__equalValidator19aSet)
        {
            // equalValidator has no pre constraints
            equalValidator19a = handleIsEqualValidator();
            // equalValidator has no post constraints
            this.__equalValidator19a = equalValidator19a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__equalValidator19aSet = true;
            }
        }
        return equalValidator19a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputTable()
    * @return boolean
    */
    protected abstract boolean handleIsInputTable();

    private boolean __inputTable20a;
    private boolean __inputTable20aSet = false;

    /**
     * Indicates whether or not this is an table input type.
     * @return (boolean)handleIsInputTable()
     */
    public final boolean isInputTable()
    {
        boolean inputTable20a = this.__inputTable20a;
        if (!this.__inputTable20aSet)
        {
            // inputTable has no pre constraints
            inputTable20a = handleIsInputTable();
            // inputTable has no post constraints
            this.__inputTable20a = inputTable20a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTable20aSet = true;
            }
        }
        return inputTable20a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isInputTypePresent()
    * @return boolean
    */
    protected abstract boolean handleIsInputTypePresent();

    private boolean __inputTypePresent21a;
    private boolean __inputTypePresent21aSet = false;

    /**
     * Indicates whether or not there is an input type defined for this attribute.
     * @return (boolean)handleIsInputTypePresent()
     */
    public final boolean isInputTypePresent()
    {
        boolean inputTypePresent21a = this.__inputTypePresent21a;
        if (!this.__inputTypePresent21aSet)
        {
            // inputTypePresent has no pre constraints
            inputTypePresent21a = handleIsInputTypePresent();
            // inputTypePresent has no post constraints
            this.__inputTypePresent21a = inputTypePresent21a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTypePresent21aSet = true;
            }
        }
        return inputTypePresent21a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#isPlaintext()
    * @return boolean
    */
    protected abstract boolean handleIsPlaintext();

    private boolean __plaintext22a;
    private boolean __plaintext22aSet = false;

    /**
     * Indicates whether or not this attribute's value should be rendered as plain text (not as a
     * widget).
     * @return (boolean)handleIsPlaintext()
     */
    public final boolean isPlaintext()
    {
        boolean plaintext22a = this.__plaintext22a;
        if (!this.__plaintext22aSet)
        {
            // plaintext has no pre constraints
            plaintext22a = handleIsPlaintext();
            // plaintext has no post constraints
            this.__plaintext22a = plaintext22a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__plaintext22aSet = true;
            }
        }
        return plaintext22a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getInputTableIdentifierColumns()
    * @return String
    */
    protected abstract String handleGetInputTableIdentifierColumns();

    private String __inputTableIdentifierColumns23a;
    private boolean __inputTableIdentifierColumns23aSet = false;

    /**
     * A comma separated list of the input table identifier columns (these are the columns that
     * uniquely define a row in an input table).
     * @return (String)handleGetInputTableIdentifierColumns()
     */
    public final String getInputTableIdentifierColumns()
    {
        String inputTableIdentifierColumns23a = this.__inputTableIdentifierColumns23a;
        if (!this.__inputTableIdentifierColumns23aSet)
        {
            // inputTableIdentifierColumns has no pre constraints
            inputTableIdentifierColumns23a = handleGetInputTableIdentifierColumns();
            // inputTableIdentifierColumns has no post constraints
            this.__inputTableIdentifierColumns23a = inputTableIdentifierColumns23a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__inputTableIdentifierColumns23aSet = true;
            }
        }
        return inputTableIdentifierColumns23a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFAttribute#getMaxLength()
    * @return String
    */
    protected abstract String handleGetMaxLength();

    private String __maxLength24a;
    private boolean __maxLength24aSet = false;

    /**
     * The max length allowed in the input component
     * @return (String)handleGetMaxLength()
     */
    public final String getMaxLength()
    {
        String maxLength24a = this.__maxLength24a;
        if (!this.__maxLength24aSet)
        {
            // maxLength has no pre constraints
            maxLength24a = handleGetMaxLength();
            // maxLength has no post constraints
            this.__maxLength24a = maxLength24a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__maxLength24aSet = true;
            }
        }
        return maxLength24a;
    }

    // ---------------- business methods ----------------------

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
     * Gets backing list name for this attribute. This is useful if you want to be able to select
     * the attribute value from a list (i.e. a drop-down select input type).
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetBackingListName(ParameterFacade ownerParameter);

    /**
     * Gets backing list name for this attribute. This is useful if you want to be able to select
     * the attribute value from a list (i.e. a drop-down select input type).
     * @param ownerParameter ParameterFacade
     * The parameter that is the owner of this attribute.
     * @return handleGetBackingListName(ownerParameter)
     */
    public String getBackingListName(ParameterFacade ownerParameter)
    {
        // getBackingListName has no pre constraints
        String returnValue = handleGetBackingListName(ownerParameter);
        // getBackingListName has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Gets the name of the label list for this parameter. The label list name is the name of the
     * list storing the labels for the possible values of this attribute (typically used for the
     * labels of a drop-down select lists).
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetLabelListName(ParameterFacade ownerParameter);

    /**
     * Gets the name of the label list for this parameter. The label list name is the name of the
     * list storing the labels for the possible values of this attribute (typically used for the
     * labels of a drop-down select lists).
     * @param ownerParameter ParameterFacade
     * The parameter that is the owner of this attribute.
     * @return handleGetLabelListName(ownerParameter)
     */
    public String getLabelListName(ParameterFacade ownerParameter)
    {
        // getLabelListName has no pre constraints
        String returnValue = handleGetLabelListName(ownerParameter);
        // getLabelListName has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Gets the name of the value list for this parameter; this list stores the possible values that
     * this attribute may be (typically used for the values of a drop-down select list).
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetValueListName(ParameterFacade ownerParameter);

    /**
     * Gets the name of the value list for this parameter; this list stores the possible values that
     * this attribute may be (typically used for the values of a drop-down select list).
     * @param ownerParameter ParameterFacade
     * The parameter that is the owner of this attribute.
     * @return handleGetValueListName(ownerParameter)
     */
    public String getValueListName(ParameterFacade ownerParameter)
    {
        // getValueListName has no pre constraints
        String returnValue = handleGetValueListName(ownerParameter);
        // getValueListName has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Indicates whether or not this attribute is selectable according to its 'ownerParameter'.
     * @param ownerParameter
     * @return boolean
     */
    protected abstract boolean handleIsSelectable(FrontEndParameter ownerParameter);

    /**
     * Indicates whether or not this attribute is selectable according to its 'ownerParameter'.
     * @param ownerParameter FrontEndParameter
     * The parameter that 'owns' this attribute.
     * @return handleIsSelectable(ownerParameter)
     */
    public boolean isSelectable(FrontEndParameter ownerParameter)
    {
        // isSelectable has no pre constraints
        boolean returnValue = handleIsSelectable(ownerParameter);
        // isSelectable has no post constraints
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
     * The type of validator.
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
     * Gets the name of the date formatter for this attribute by constructing the name from the
     * 'ownerParameter' (if this attribute represents a date).
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetDateFormatter(JSFParameter ownerParameter);

    /**
     * Gets the name of the date formatter for this attribute by constructing the name from the
     * 'ownerParameter' (if this attribute represents a date).
     * @param ownerParameter JSFParameter
     * The parameter that is the 'owner' of this attribute.
     * @return handleGetDateFormatter(ownerParameter)
     */
    public String getDateFormatter(JSFParameter ownerParameter)
    {
        // getDateFormatter has no pre constraints
        String returnValue = handleGetDateFormatter(ownerParameter);
        // getDateFormatter has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Gets the name of the time formatter (if this parameter represents a time).
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetTimeFormatter(JSFParameter ownerParameter);

    /**
     * Gets the name of the time formatter (if this parameter represents a time).
     * @param ownerParameter JSFParameter
     * The parameter that is the 'owner' of this attribute.
     * @return handleGetTimeFormatter(ownerParameter)
     */
    public String getTimeFormatter(JSFParameter ownerParameter)
    {
        // getTimeFormatter has no pre constraints
        String returnValue = handleGetTimeFormatter(ownerParameter);
        // getTimeFormatter has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Constructs and returns the backing value name given the 'ownerParameter'.
     * @param ownerParameter
     * @return String
     */
    protected abstract String handleGetBackingValueName(ParameterFacade ownerParameter);

    /**
     * Constructs and returns the backing value name given the 'ownerParameter'.
     * @param ownerParameter ParameterFacade
     * The parameter that is the "owner" of this attribute (i.e. the parameter's type contains this
     * attribute).
     * @return handleGetBackingValueName(ownerParameter)
     */
    public String getBackingValueName(ParameterFacade ownerParameter)
    {
        // getBackingValueName has no pre constraints
        String returnValue = handleGetBackingValueName(ownerParameter);
        // getBackingValueName has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Indicates whether or not the backing value is required for this attribute (depending on the
     * 'ownerParameter').
     * @param ownerParameter
     * @return boolean
     */
    protected abstract boolean handleIsBackingValueRequired(FrontEndParameter ownerParameter);

    /**
     * Indicates whether or not the backing value is required for this attribute (depending on the
     * 'ownerParameter').
     * @param ownerParameter FrontEndParameter
     * The 'owner' of this attribute (i.e. the attrubte who's type has this attribute).
     * @return handleIsBackingValueRequired(ownerParameter)
     */
    public boolean isBackingValueRequired(FrontEndParameter ownerParameter)
    {
        // isBackingValueRequired has no pre constraints
        boolean returnValue = handleIsBackingValueRequired(ownerParameter);
        // isBackingValueRequired has no post constraints
        return returnValue;
    }

    /**
     * Method to be implemented in descendants
     * Gets the validator args for this attribute
     * @param ownerParameter
     * @return Collection
     */
    protected abstract Collection handleGetValidatorVars(JSFParameter ownerParameter);

    /**
     * Gets the validator args for this attribute
     * @param ownerParameter JSFParameter
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFAttribute.getValidatorVars(ownerParameter)
     * @return handleGetValidatorVars(ownerParameter)
     */
    public Collection getValidatorVars(JSFParameter ownerParameter)
    {
        // getValidatorVars has no pre constraints
        Collection returnValue = handleGetValidatorVars(ownerParameter);
        // getValidatorVars has no post constraints
        return returnValue;
    }

    /**
     * @return true
     * @see AttributeFacade
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

    // ----------- delegates to AttributeFacade ------------
    /**
     * Searches the given feature for the specified tag.
     * If the follow boolean is set to true then the search will continue from the class attribute
     * to the class itself and then up the class hierarchy.
     * @see AttributeFacade#findTaggedValue(String name, boolean follow)
     */
    public Object findTaggedValue(String name, boolean follow)
    {
        return this.getSuperAttributeFacade().findTaggedValue(name, follow);
    }

    /**
     * The default value of the attribute.  This is the value given if no value is defined.
     * @see AttributeFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        return this.getSuperAttributeFacade().getDefaultValue();
    }

    /**
     * If the attribute is an enumeration literal this represents the owning enumeration. Can be
     * empty.
     * @see AttributeFacade#getEnumeration()
     */
    public EnumerationFacade getEnumeration()
    {
        return this.getSuperAttributeFacade().getEnumeration();
    }

    /**
     * Returns the enumeration literal parameters defined by tagged value as a comma separated list.
     * @see AttributeFacade#getEnumerationLiteralParameters()
     */
    public String getEnumerationLiteralParameters()
    {
        return this.getSuperAttributeFacade().getEnumerationLiteralParameters();
    }

    /**
     * The value for this attribute if it is an enumeration literal, null otherwise. The default
     * value is returned as a String if it has been specified, if it's not specified this
     * attribute's name is assumed.
     * @see AttributeFacade#getEnumerationValue()
     */
    public String getEnumerationValue()
    {
        return this.getSuperAttributeFacade().getEnumerationValue();
    }

    /**
     * The name of the accessor operation that would retrieve this attribute's value.
     * @see AttributeFacade#getGetterName()
     */
    public String getGetterName()
    {
        return this.getSuperAttributeFacade().getGetterName();
    }

    /**
     * The name of the type that is returned on the accessor and mutator operations,  determined in
     * part by the multiplicity.
     * @see AttributeFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        return this.getSuperAttributeFacade().getGetterSetterTypeName();
    }

    /**
     * the lower value for the multiplicity
     * -only applicable for UML2
     * @see AttributeFacade#getLower()
     */
    public int getLower()
    {
        return this.getSuperAttributeFacade().getLower();
    }

    /**
     * Gets the classifier who is the owner of the attributes.
     * @see AttributeFacade#getOwner()
     */
    public ClassifierFacade getOwner()
    {
        return this.getSuperAttributeFacade().getOwner();
    }

    /**
     * The name of the mutator operation that would retrieve this attribute's value.
     * @see AttributeFacade#getSetterName()
     */
    public String getSetterName()
    {
        return this.getSuperAttributeFacade().getSetterName();
    }

    /**
     * The classifier owning this attribute.
     * @see AttributeFacade#getType()
     */
    public ClassifierFacade getType()
    {
        return this.getSuperAttributeFacade().getType();
    }

    /**
     * the upper value for the multiplicity (will be -1 for *)
     * -only applicable for UML2
     * @see AttributeFacade#getUpper()
     */
    public int getUpper()
    {
        return this.getSuperAttributeFacade().getUpper();
    }

    /**
     * True if this attribute can only be set.
     * @see AttributeFacade#isAddOnly()
     */
    public boolean isAddOnly()
    {
        return this.getSuperAttributeFacade().isAddOnly();
    }

    /**
     * True if this attribute can be modified.
     * @see AttributeFacade#isChangeable()
     */
    public boolean isChangeable()
    {
        return this.getSuperAttributeFacade().isChangeable();
    }

    /**
     * Indicates if the default value is present.
     * @see AttributeFacade#isDefaultValuePresent()
     */
    public boolean isDefaultValuePresent()
    {
        return this.getSuperAttributeFacade().isDefaultValuePresent();
    }

    /**
     * If the attribute is derived (its value is computed). UML2 only. UML14 always returns false.
     * Default=false.
     * @see AttributeFacade#isDerived()
     */
    public boolean isDerived()
    {
        return this.getSuperAttributeFacade().isDerived();
    }

    /**
     * True if this attribute is owned by an enumeration.
     * @see AttributeFacade#isEnumerationLiteral()
     */
    public boolean isEnumerationLiteral()
    {
        return this.getSuperAttributeFacade().isEnumerationLiteral();
    }

    /**
     * Returns true if enumeration literal parameters exist (defined by tagged value) for the
     * literal.
     * @see AttributeFacade#isEnumerationLiteralParametersExist()
     */
    public boolean isEnumerationLiteralParametersExist()
    {
        return this.getSuperAttributeFacade().isEnumerationLiteralParametersExist();
    }

    /**
     * True if this attribute is owned by an enumeration but is defined as a member variable (NOT a
     * literal).
     * @see AttributeFacade#isEnumerationMember()
     */
    public boolean isEnumerationMember()
    {
        return this.getSuperAttributeFacade().isEnumerationMember();
    }

    /**
     * IsLeaf property in the operation. If true, operation is final, cannot be extended or
     * implemented by a descendant.
     * @see AttributeFacade#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.getSuperAttributeFacade().isLeaf();
    }

    /**
     * Whether or not this attribute has a multiplicity greater than 1.
     * @see AttributeFacade#isMany()
     */
    public boolean isMany()
    {
        return this.getSuperAttributeFacade().isMany();
    }

    /**
     * Indicates whether or not the attributes are ordered (if multiplicity is greater than 1).
     * @see AttributeFacade#isOrdered()
     */
    public boolean isOrdered()
    {
        return this.getSuperAttributeFacade().isOrdered();
    }

    /**
     * Whether or not this attribute can be modified.
     * @see AttributeFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return this.getSuperAttributeFacade().isReadOnly();
    }

    /**
     * Whether or not the multiplicity of this attribute is 1.
     * @see AttributeFacade#isRequired()
     */
    public boolean isRequired()
    {
        return this.getSuperAttributeFacade().isRequired();
    }

    /**
     * Indicates if this attribute is 'static', meaning it has a classifier scope.
     * @see AttributeFacade#isStatic()
     */
    public boolean isStatic()
    {
        return this.getSuperAttributeFacade().isStatic();
    }

    /**
     * If the attribute is unique within the Collection type. UML2 only. UML14 always returns false.
     * Unique+Ordered determines the implementation Collection type. Default=false.
     * @see AttributeFacade#isUnique()
     */
    public boolean isUnique()
    {
        return this.getSuperAttributeFacade().isUnique();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperAttributeFacade().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperAttributeFacade().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperAttributeFacade().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperAttributeFacade().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperAttributeFacade().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperAttributeFacade().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperAttributeFacade().getDocumentation(indent);
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
        return this.getSuperAttributeFacade().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperAttributeFacade().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperAttributeFacade().getFullyQualifiedName();
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
        return this.getSuperAttributeFacade().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperAttributeFacade().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperAttributeFacade().getId();
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
        return this.getSuperAttributeFacade().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperAttributeFacade().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperAttributeFacade().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperAttributeFacade().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperAttributeFacade().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperAttributeFacade().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperAttributeFacade().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperAttributeFacade().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperAttributeFacade().getPackagePath();
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
        return this.getSuperAttributeFacade().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperAttributeFacade().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperAttributeFacade().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperAttributeFacade().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperAttributeFacade().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperAttributeFacade().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperAttributeFacade().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperAttributeFacade().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperAttributeFacade().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperAttributeFacade().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperAttributeFacade().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperAttributeFacade().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperAttributeFacade().hasKeyword(keywordName);
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
        return this.getSuperAttributeFacade().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperAttributeFacade().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperAttributeFacade().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperAttributeFacade().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperAttributeFacade().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperAttributeFacade().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperAttributeFacade().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperAttributeFacade().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperAttributeFacade().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperAttributeFacade().translateConstraints(kind, translation);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperAttributeFacade().initialize();
    }

    /**
     * @return Object getSuperAttributeFacade().getValidationOwner()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperAttributeFacade().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperAttributeFacade().getValidationName()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperAttributeFacade().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperAttributeFacade().validateInvariants(validationMessages);
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