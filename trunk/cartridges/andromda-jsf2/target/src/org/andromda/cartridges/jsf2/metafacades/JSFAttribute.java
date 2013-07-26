// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.ParameterFacade;

/**
 * Represents an attribute on a classifier used by a JSF application.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFAttribute
    extends AttributeFacade
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFAttributeMetaType();

    /**
     * Gets backing list name for this attribute. This is useful if you want to be able to select
     * the attribute value from a list (i.e. a drop-down select input type).
     * @param ownerParameter ParameterFacade
     * @return String
     */
    public String getBackingListName(ParameterFacade ownerParameter);

    /**
     * Constructs and returns the backing value name given the 'ownerParameter'.
     * @param ownerParameter ParameterFacade
     * @return String
     */
    public String getBackingValueName(ParameterFacade ownerParameter);

    /**
     * Gets the name of the date formatter for this attribute by constructing the name from the
     * 'ownerParameter' (if this attribute represents a date).
     * @param ownerParameter JSFParameter
     * @return String
     */
    public String getDateFormatter(JSFParameter ownerParameter);

    /**
     * The dummy value to give the attribute when creating a dummy instance of this attribute's
     * owner.
     * @return String
     */
    public String getDummyValue();

    /**
     * Gets the unique id of this attribute on the form.
     * @param ownerParameter ParameterFacade
     * @return String
     */
    public String getFormPropertyId(ParameterFacade ownerParameter);

    /**
     * Retrieves the name of the form property for this attribute by taking the name of the owner
     * property.
     * @param ownerParameter ParameterFacade
     * @return String
     */
    public String getFormPropertyName(ParameterFacade ownerParameter);

    /**
     * If this attributes represents a date or time this method will return the format in which it
     * must be represented. In the event this format has not been specified by the any tagged value
     * the default will be used.
     * @return String
     */
    public String getFormat();

    /**
     * A comma separated list of the input table identifier columns (these are the columns that
     * uniquely define a row in an input table).
     * @return String
     */
    public String getInputTableIdentifierColumns();

    /**
     * Gets the name of the label list for this parameter. The label list name is the name of the
     * list storing the labels for the possible values of this attribute (typically used for the
     * labels of a drop-down select lists).
     * @param ownerParameter ParameterFacade
     * @return String
     */
    public String getLabelListName(ParameterFacade ownerParameter);

    /**
     * The max length allowed in the input component
     * @return String
     */
    public String getMaxLength();

    /**
     * The message key for this attribute.
     * @return String
     */
    public String getMessageKey();

    /**
     * The default value for the message key.
     * @return String
     */
    public String getMessageValue();

    /**
     * Gets the name of the time formatter (if this parameter represents a time).
     * @param ownerParameter JSFParameter
     * @return String
     */
    public String getTimeFormatter(JSFParameter ownerParameter);

    /**
     * The validator's 'validwhen' value, this is useful when the validation of a parameter depends
     * on the validation of others. See the apache commons-validator documentation for more
     * information.
     * @return String
     */
    public String getValidWhen();

    /**
     * Gets the arguments for this parameter's validators.
     * @param validatorType String
     * @return Collection
     */
    public Collection getValidatorArgs(String validatorType);

    /**
     * All validator types for this attribute.
     * @return Collection
     */
    public Collection getValidatorTypes();

    /**
     * Gets the validator args for this attribute
     * @param ownerParameter JSFParameter
     * @return Collection
     */
    public Collection getValidatorVars(JSFParameter ownerParameter);

    /**
     * The dummy value for a value list.
     * @return String
     */
    public String getValueListDummyValue();

    /**
     * Gets the name of the value list for this parameter; this list stores the possible values that
     * this attribute may be (typically used for the values of a drop-down select list).
     * @param ownerParameter ParameterFacade
     * @return String
     */
    public String getValueListName(ParameterFacade ownerParameter);

    /**
     * Indicates whether or not the backing value is required for this attribute (depending on the
     * 'ownerParameter').
     * @param ownerParameter FrontEndParameter
     * @return boolean
     */
    public boolean isBackingValueRequired(FrontEndParameter ownerParameter);

    /**
     * Indicates whether or not this parameter uses the equal validator.
     * @return boolean
     */
    public boolean isEqualValidator();

    /**
     * Indicates if this parameter represents a checkbox widget.
     * @return boolean
     */
    public boolean isInputCheckbox();

    /**
     * Indicates whether or not this is a file input type.
     * @return boolean
     */
    public boolean isInputFile();

    /**
     * Indicates whether or not this parameter represents a hidden input widget.
     * @return boolean
     */
    public boolean isInputHidden();

    /**
     * Indicates whether or not this type represents an input multibox.
     * @return boolean
     */
    public boolean isInputMultibox();

    /**
     * Indicates whether or not this parameter should be rendered as an input radio widget.
     * @return boolean
     */
    public boolean isInputRadio();

    /**
     * Indicates whether or not this parameter represents an input "secret" widget (i.e. password).
     * @return boolean
     */
    public boolean isInputSecret();

    /**
     * Indicates whether or not this parameter represents an input select widget.
     * @return boolean
     */
    public boolean isInputSelect();

    /**
     * Indicates whether or not this is an table input type.
     * @return boolean
     */
    public boolean isInputTable();

    /**
     * Indicates whether or not this parameter should be rendered as a text input widget.
     * @return boolean
     */
    public boolean isInputText();

    /**
     * Indicates if this parameter represents as an input text area widget.
     * @return boolean
     */
    public boolean isInputTextarea();

    /**
     * Indicates whether or not there is an input type defined for this attribute.
     * @return boolean
     */
    public boolean isInputTypePresent();

    /**
     * Indicates whether or not this attribute's value should be rendered as plain text (not as a
     * widget).
     * @return boolean
     */
    public boolean isPlaintext();

    /**
     * Indicates whether or not this attribute is selectable according to its 'ownerParameter'.
     * @param ownerParameter FrontEndParameter
     * @return boolean
     */
    public boolean isSelectable(FrontEndParameter ownerParameter);

    /**
     * Indicates where or not the date format is to be strictly respected. Otherwise the date
     * formatter used for the representation of this date is to be set to lenient.
     * @return boolean
     */
    public boolean isStrictDateFormat();

    /**
     * Indicates whether or not this attribute requires some kind of validation (the collection of
     * validator types is not empty).
     * @return boolean
     */
    public boolean isValidationRequired();
}