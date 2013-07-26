// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.ParameterFacade;

/**
 * TODO: Model Documentation for
 * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFManageableEntityAttribute
    extends ManageableEntityAttribute
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFManageableEntityAttributeMetaType();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.backingListName
     * @return String
     */
    public String getBackingListName();

    /**
     * The String format to use when referring to this date, only makes sense when the type is a
     * date type.
     * @return String
     */
    public String getDateFormat();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.dateFormatter
     * @return String
     */
    public String getDateFormatter();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.defaultDateFormat
     * @return String
     */
    public String getDefaultDateFormat();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.defaultTimeFormat
     * @return String
     */
    public String getDefaultTimeFormat();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.dummyValue
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
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.format
     * @return String
     */
    public String getFormat();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTableIdentifierColumns
     * @return String
     */
    public String getInputTableIdentifierColumns();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.labelListName
     * @return String
     */
    public String getLabelListName();

    /**
     * The max length allowed in the input component
     * @return String
     */
    public String getMaxLength();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.messageKey
     * @return String
     */
    public String getMessageKey();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.messageValue
     * @return String
     */
    public String getMessageValue();

    /**
     * The key to lookup the online help documentation. This documentation is gathered from the
     * documentation entered by the user, as well as analyzing the model.
     * @return String
     */
    public String getOnlineHelpKey();

    /**
     * The online help documentation. This documentation is gathered from the documentation entered
     * by the user, as well as analyzing the model. The format is HTML without any style.
     * @return String
     */
    public String getOnlineHelpValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.timeFormatter
     * @return String
     */
    public String getTimeFormatter();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.validWhen
     * @return String
     */
    public String getValidWhen();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.getValidatorArgs
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
     * The validator variables.
     * @return Collection
     */
    public Collection getValidatorVars();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.valueListDummyValue
     * @return String
     */
    public String getValueListDummyValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.valueListName
     * @return String
     */
    public String getValueListName();

    /**
     * The widget to use when rendering this attribute
     * @return String
     */
    public String getWidgetType();

    /**
     * Whether or not this attribute should be put in the view
     * @return boolean
     */
    public boolean isEditable();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.equalValidator
     * @return boolean
     */
    public boolean isEqualValidator();

    /**
     * Whether or not this attribute should be hidden from the view
     * @return boolean
     */
    public boolean isHidden();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputCheckbox
     * @return boolean
     */
    public boolean isInputCheckbox();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputFile
     * @return boolean
     */
    public boolean isInputFile();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputHidden
     * @return boolean
     */
    public boolean isInputHidden();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputMultibox
     * @return boolean
     */
    public boolean isInputMultibox();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputRadio
     * @return boolean
     */
    public boolean isInputRadio();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputSecret
     * @return boolean
     */
    public boolean isInputSecret();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputSelect
     * @return boolean
     */
    public boolean isInputSelect();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTable
     * @return boolean
     */
    public boolean isInputTable();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputText
     * @return boolean
     */
    public boolean isInputText();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTextarea
     * @return boolean
     */
    public boolean isInputTextarea();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.inputTypePresent
     * @return boolean
     */
    public boolean isInputTypePresent();

    /**
     * True if this attribute is of a type that cannot easily be represented as a textual string and
     * would be an ideal candidate for HTTP's support for file-upload.
     * @return boolean
     */
    public boolean isNeedsFileUpload();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAttribute.plaintext
     * @return boolean
     */
    public boolean isPlaintext();

    /**
     * True if this field is a date type and the date format is not be interpreted strictly.
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