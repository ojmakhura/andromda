// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import java.util.List;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.FrontEndParameter;

/**
 * Represents a parameter in a JSF front-end.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFParameter
    extends FrontEndParameter
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFParameterMetaType();

    /**
     * All the annotations for this parameter.
     * @return Collection
     */
    public Collection getAnnotations();

    /**
     * All attributes belonging to this parameter's type.
     * @return Collection
     */
    public Collection getAttributes();

    /**
     * The backing list name for this parameter. This is useful if you want to be able to select the
     * parameter value from a list (i.e. a drop-down select input type).
     * @return String
     */
    public String getBackingListName();

    /**
     * The name of the backing value for this parameter (only used with collections and arrays that
     * are input type table).
     * @return String
     */
    public String getBackingValueName();

    /**
     * The name of the date formatter for this parameter (if this parameter represents a date).
     * @return String
     */
    public String getDateFormatter();

    /**
     * A resource message key suited for the parameter's documentation.
     * @return String
     */
    public String getDocumentationKey();

    /**
     * A resource message value suited for the parameter's documentation.
     * @return String
     */
    public String getDocumentationValue();

    /**
     * The dummy value for this parameter. The dummy value is used for setting the dummy information
     * when dummyData is enabled.
     * @return String
     */
    public String getDummyValue();

    /**
     * The name of the property used for indicating whether or not a form attribute has been set at
     * least once.
     * @return String
     */
    public String getFormAttributeSetProperty();

    /**
     * If this parameter represents a date or time this method will return the format in which it
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
     * The name of the label list for this parameter. The label list name is the name of the list
     * storing the labels for the possible values of this parameter (typically used for the labels
     * of a drop-down select lists).
     * @return String
     */
    public String getLabelListName();

    /**
     * The max length allowed in the input component
     * @return String
     */
    public String getMaxLength();

    /**
     * The default message key for this parameter.
     * @return String
     */
    public String getMessageKey();

    /**
     * The default message value for this parameter.
     * @return String
     */
    public String getMessageValue();

    /**
     * All navigation association ends belonging to this parameter's type.
     * @return Collection<AssociationEndFacade>
     */
    public Collection<AssociationEndFacade> getNavigableAssociationEnds();

    /**
     * Actions used when submitting forms for this table. It only makes sense to call this property
     * on parameters that represent a table page-variable.
     * @return List<JSFAction>
     */
    public List<JSFAction> getTableActions();

    /**
     * Those actions that are targetting the given column, only makes sense when this parameter
     * represents a table view-variable.
     * @param columnName String
     * @return List
     */
    public List getTableColumnActions(String columnName);

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFParameter.getTableColumnMessageKey
     * @param columnName String
     * @return String
     */
    public String getTableColumnMessageKey(String columnName);

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFParameter.getTableColumnMessageValue
     * @param columnName String
     * @return String
     */
    public String getTableColumnMessageValue(String columnName);

    /**
     * Actions used when submitting forms for this table. Table actions that are hyperlinks are not
     * included. It only makes sense to call this property on parameters that represent a table
     * page-variable.
     * @return List<JSFAction>
     */
    public List<JSFAction> getTableFormActions();

    /**
     * Actions that are working with this table and are to be represented as hyperlinks. This only
     * makes sense on a parameter that represents a table page variable.
     * @return List<JSFAction>
     */
    public List<JSFAction> getTableHyperlinkActions();

    /**
     * The name of the property that Indicates whether or not the table should be sorted ascending
     * (if this parameter represents a table).
     * @return String
     */
    public String getTableSortAscendingProperty();

    /**
     * The name of the property storing the column to sort by if this parameter represents a table.
     * @return String
     */
    public String getTableSortColumnProperty();

    /**
     * The name of the time formatter (if this parameter represents a time).
     * @return String
     */
    public String getTimeFormatter();

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
     * All the validator types for this parameter.
     * @return Collection
     */
    public Collection getValidatorTypes();

    /**
     * The validator variables.
     * @return Collection
     */
    public Collection getValidatorVars();

    /**
     * The dummy value for a value list.
     * @return String
     */
    public String getValueListDummyValue();

    /**
     * Stores the name of the value list for this parameter; this list stores the possible values
     * that this parameter may be (typically used for the values of a drop-down select list).
     * @return String
     */
    public String getValueListName();

    /**
     * Indicates if a backing value is required for this parameter.
     * @return boolean
     */
    public boolean isBackingValueRequired();

    /**
     * Indicates if this parameter is 'complex', that is: its of a complex type (has at least one
     * attribute or association).
     * @return boolean
     */
    public boolean isComplex();

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
     * Whether or not the parameter is a "pageable table", that is a table that supports paging
     * (i.e. DB paging).
     * @return boolean
     */
    public boolean isPageableTable();

    /**
     * Indicates whether or not this field should be rendered as plain text (not as a widget).
     * @return boolean
     */
    public boolean isPlaintext();

    /**
     * Indicates if this parameter can only be read and not modified.
     * @return boolean
     */
    public boolean isReadOnly();

    /**
     * Indicates if this parameter's value should be reset or not after an action has been performed
     * with this parameter.
     * @return boolean
     */
    public boolean isReset();

    /**
     * Indicates whether or not this parameter is selectable or not (that is: it can be selected
     * from a list of values).
     * @return boolean
     */
    public boolean isSelectable();

    /**
     * Indicates where or not the date format is to be strictly respected. Otherwise the date
     * formatter used for the representation of this date is to be set to lenient.
     * @return boolean
     */
    public boolean isStrictDateFormat();

    /**
     * Indicates whether or not this parameter requires some kind of validation (the collection of
     * validator types is not empty).
     * @return boolean
     */
    public boolean isValidationRequired();
}