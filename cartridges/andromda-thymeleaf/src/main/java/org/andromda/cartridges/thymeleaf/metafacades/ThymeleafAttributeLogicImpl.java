package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.andromda.cartridges.web.CartridgeWebGlobals;
import org.andromda.cartridges.web.CartridgeWebProfile;
import org.andromda.cartridges.web.CartridgeWebUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAttribute.
 *
 * @see ThymeleafAttribute
 */
public class ThymeleafAttributeLogicImpl
    extends ThymeleafAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return messageKey
     * @see ThymeleafAttribute#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        final StringBuilder messageKey = new StringBuilder();
        if (!this.isNormalizeMessages())
        {
            final ClassifierFacade owner = this.getOwner();
            if (owner != null)
            {
                messageKey.append(StringUtilsHelper.toResourceMessageKey(owner.getName()));
                messageKey.append('.');
            }
        }
        final String name = this.getName();
        if (name != null && name.trim().length() > 0)
        {
            messageKey.append(StringUtilsHelper.toResourceMessageKey(name));
        }
        return messageKey.toString();
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages =
            Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.NORMALIZE_MESSAGES));
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @return StringUtilsHelper.toPhrase(super.getName())
     * @see ThymeleafAttribute#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName());
    }

    /**
     * @return format
     * @see ThymeleafAttribute#getFormat()
     */
    protected String handleGetFormat()
    {
        return CartridgeWebUtils.getFormat(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            this.getDefaultDateFormat(),
            this.getDefaultTimeFormat());
    }

    /**
     * @return the default time format pattern as defined using the configured property
     */
    private String getDefaultTimeFormat()
    {
        return (String)this.getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @return dummyValue
     * @see ThymeleafAttribute#getDummyValue()
     */
    protected String handleGetDummyValue()
    {
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String typeName = type.getFullyQualifiedName();
            final String name = this.getName();
            if ("String".equals(typeName))
            {
                return "\"" + name + "-test" + "\"";
            }
            if ("java.util.Date".equals(typeName))
            {
                return "new java.util.Date()";
            }
            if ("java.sql.Date".equals(typeName))
            {
                return "new java.sql.Date(new java.util.Date().getTime())";
            }
            if ("java.sql.Timestamp".equals(typeName))
            {
                return "new java.sql.Timestamp(new Date().getTime())";
            }
            if ("java.util.Calendar".equals(typeName))
            {
                return "java.util.Calendar.getInstance()";
            }
            if ("int".equals(typeName))
            {
                return "(int)" + name.hashCode();
            }
            if ("boolean".equals(typeName))
            {
                return "false";
            }
            if ("long".equals(typeName))
            {
                return "(long)" + name.hashCode();
            }
            if ("char".equals(typeName))
            {
                return "(char)" + name.hashCode();
            }
            if ("float".equals(typeName))
            {
                return "(float)" + name.hashCode() / hashCode();
            }
            if ("double".equals(typeName))
            {
                return "(double)" + name.hashCode() / hashCode();
            }
            if ("short".equals(typeName))
            {
                return "(short)" + name.hashCode();
            }
            if ("byte".equals(typeName))
            {
                return "(byte)" + name.hashCode();
            }
            if ("java.lang.Integer".equals(typeName) || "Integer".equals(typeName))
            {
                return "new Integer((int)" + name.hashCode() + ")";
            }
            if ("java.lang.Boolean".equals(typeName) || "Boolean".equals(typeName))
            {
                return "Boolean.FALSE";
            }
            if ("java.lang.Long".equals(typeName) || "Long".equals(typeName))
            {
                return "new Long((long)" + name.hashCode() + ")";
            }
            if ("java.lang.Character".equals(typeName) || "Character".equals(typeName))
            {
                return "new Character(char)" + name.hashCode() + ")";
            }
            if ("java.lang.Float".equals(typeName) || "Float".equals(typeName))
            {
                return "new Float((float)" + name.hashCode() / hashCode() + ")";
            }
            if ("java.lang.Double".equals(typeName) || "Double".equals(typeName))
            {
                return "new Double((double)" + name.hashCode() / hashCode() + ")";
            }
            if ("java.lang.Short".equals(typeName) || "Short".equals(typeName))
            {
                return "new Short((short)" + name.hashCode() + ")";
            }
            if ("java.lang.Byte".equals(typeName) || "Byte".equals(typeName))
            {
                return "new Byte((byte)" + name.hashCode() + ")";
            }

            //if (type.isArrayType()) return constructDummyArray();
            if (type.isSetType())
            {
                return "new java.util.HashSet(java.util.Arrays.asList(" + constructDummyArray() + "))";
            }
            if (type.isCollectionType())
            {
                return "java.util.Arrays.asList(" + constructDummyArray() + ")";
            }

            // maps and others types will simply not be treated
        }
        return "null";
    }

    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @return A String representing Java code for the initialization of an array.
     */
    private String constructDummyArray()
    {
        return CartridgeWebUtils.constructDummyArrayDeclaration(
            this.getName(),
            CartridgeWebGlobals.DUMMY_ARRAY_COUNT);
    }

    /**
     * @param ownerParameter
     * @return propertyName
     * @see ThymeleafAttribute#getFormPropertyName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetFormPropertyName(final ModelElementFacade ownerParameter)
    {
        final StringBuilder propertyName = new StringBuilder();
        if (ownerParameter != null)
        {
            propertyName.append(ownerParameter.getName());
            propertyName.append('.');
        }
        final String name = this.getName();
        if (name != null && name.trim().length() > 0)
        {
            propertyName.append(name);
        }
        return propertyName.toString();
    }

    /**
     * @param ownerParameter
     * @return backingListName
     * @see ThymeleafAttribute#getBackingListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetBackingListName(final ParameterFacade ownerParameter)
    {
        final String backingListName =
            StringUtils.replace(
                Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.BACKING_LIST_PATTERN)),
                "{0}",
                this.getFormPropertyId(ownerParameter));
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @param ownerParameter
     * @return backingValueName
     * @see ThymeleafAttribute#getBackingValueName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetBackingValueName(final ParameterFacade ownerParameter)
    {
        final String backingListName =
            StringUtils.replace(
                Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.BACKING_VALUE_PATTERN)),
                "{0}",
                this.getFormPropertyId(ownerParameter));
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @param ownerParameter
     * @return labelListName
     * @see ThymeleafAttribute#getLabelListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetLabelListName(final ParameterFacade ownerParameter)
    {
        return StringUtils.replace(
            Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.LABEL_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
    }

    /**
     * @param ownerParameter
     * @return valueListName
     * @see ThymeleafAttribute#getValueListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetValueListName(final ParameterFacade ownerParameter)
    {
        return StringUtils.replace(
            Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.VALUE_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
    }

    /**
     * @param ownerParameter
     * @return formPropertyId
     * @see ThymeleafAttribute#getFormPropertyId(ParameterFacade)
     */
    protected String handleGetFormPropertyId(final ModelElementFacade ownerParameter)
    {
        System.out.println("***************************************************");
        return StringUtilsHelper.lowerCamelCaseName(this.getFormPropertyName(ownerParameter));
    }

    /**
     * @param ownerParameter
     * @return isSelectable
     * @see ThymeleafAttribute#isSelectable(org.andromda.metafacades.uml.FrontEndParameter)
     */
    protected boolean handleIsSelectable(final FrontEndParameter ownerParameter)
    {
        boolean selectable = false;
        if (ownerParameter != null)
        {
            if (ownerParameter.isActionParameter())
            {
                selectable = this.isInputMultibox() || this.isInputSelect() || this.isInputRadio();
                final ClassifierFacade type = this.getType();

                if (!selectable && type != null)
                {
                    final String name = this.getName();
                    final String typeName = type.getFullyQualifiedName();

                    // - if the parameter is not selectable but on a targeting page it IS selectable we must
                    //   allow the user to set the backing list too
                    final Collection<FrontEndView> views = ownerParameter.getAction().getTargetViews();
                    for (final Iterator<FrontEndView> iterator = views.iterator(); iterator.hasNext() && !selectable;)
                    {
                        final Collection<FrontEndParameter> parameters = iterator.next().getAllActionParameters();
                        for (final Iterator<FrontEndParameter> parameterIterator = parameters.iterator();
                            parameterIterator.hasNext() && !selectable;)
                        {
                            final FrontEndParameter object = parameterIterator.next();
                            if (object instanceof ThymeleafParameter)
                            {
                                final ThymeleafParameter parameter = (ThymeleafParameter)object;
                                final String parameterName = parameter.getName();
                                final ClassifierFacade parameterType = parameter.getType();
                                if (parameterType != null)
                                {
                                    final String parameterTypeName = parameterType.getFullyQualifiedName();
                                    if (name.equals(parameterName) && typeName.equals(parameterTypeName))
                                    {
                                        selectable =
                                            parameter.isInputMultibox() || parameter.isInputSelect() ||
                                            parameter.isInputRadio();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (ownerParameter.isControllerOperationArgument())
            {
                final String name = this.getName();
                for (final FrontEndAction action : ownerParameter.getControllerOperation().getDeferringActions())
                {
                    final Collection<FrontEndParameter> formFields = action.getFormFields();
                    for (final Iterator<FrontEndParameter> fieldIterator = formFields.iterator(); fieldIterator.hasNext() && !selectable;)
                    {
                        final FrontEndParameter object = fieldIterator.next();
                        if (object instanceof ThymeleafParameter)
                        {
                            final ThymeleafParameter parameter = (ThymeleafParameter)object;
                            if (name.equals(parameter.getName()))
                            {
                                selectable = parameter.isSelectable();
                            }
                        }
                    }
                }
            }
        }
        return selectable;
    }

    /**
     * @return !this.getValidatorTypes().isEmpty()
     * @see ThymeleafAttribute#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        return !this.getValidatorTypes().isEmpty();
    }

    /**
     * @return validatorTypes
     * @see ThymeleafAttribute#getValidatorTypes()
     */
    protected Collection<String> handleGetValidatorTypes()
    {
        return CartridgeWebUtils.getValidatorTypes(
            (ModelElementFacade)this.THIS(),
            this.getType());
    }

    /**
     * @param ownerParameter
     * @return validatorVars
     * @see ThymeleafAttribute#getValidatorVars(ThymeleafParameter)
     */
    protected Collection<List<String>> handleGetValidatorVars(ThymeleafParameter ownerParameter)
    {
        return ThymeleafUtils.getValidatorVars(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            ownerParameter);
    }

    /**
     * @return CartridgeWebUtils.getValidWhen(this)
     * @see ThymeleafAttribute#getValidWhen()
     */
    protected String handleGetValidWhen()
    {
        return CartridgeWebUtils.getValidWhen(this);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_TEXTAREA)
     * @see ThymeleafAttribute#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_TEXTAREA);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_SELECT)
     * @see ThymeleafAttribute#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_SELECT);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_PASSWORD)
     * @see ThymeleafAttribute#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_PASSWORD);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_HIDDEN)
     * @see ThymeleafAttribute#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_HIDDEN);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.PLAIN_TEXT)
     * @see ThymeleafAttribute#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType(CartridgeWebGlobals.PLAIN_TEXT);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_RADIO)
     * @see ThymeleafAttribute#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_RADIO);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_TEXT)
     * @see ThymeleafAttribute#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_TEXT);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_MULTIBOX)
     * @see ThymeleafAttribute#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_MULTIBOX);
    }

    /**
     * @return inputTable
     * @see ThymeleafAttribute#isInputTable()
     */
    protected boolean handleIsInputTable()
    {
        return this.getInputTableIdentifierColumns().length() > 0 || this.isInputType(CartridgeWebGlobals.INPUT_TABLE);
    }

    /**
     * @return inputCheckbox
     * @see ThymeleafAttribute#isInputCheckbox()
     */
    protected boolean handleIsInputCheckbox()
    {
        boolean checkbox = this.isInputType(CartridgeWebGlobals.INPUT_CHECKBOX);
        if (!checkbox && this.getInputType().length() == 0)
        {
            final ClassifierFacade type = this.getType();
            checkbox = type != null ? type.isBooleanType() : false;
        }
        return checkbox;
    }

    /**
     * Indicates whether or not this parameter is of the given input type.
     *
     * @param inputType the name of the input type to check for.
     * @return true/false
     */
    private boolean isInputType(final String inputType)
    {
        return inputType.equalsIgnoreCase(this.getInputType());
    }

    /**
     * @return inputFile
     * @see ThymeleafAttribute#isInputFile()
     */
    protected boolean handleIsInputFile()
    {
        boolean file = false;
        ClassifierFacade type = getType();
        if (type != null)
        {
            file = type.isFileType();
        }
        return file;
    }

    /**
     * Overridden to provide consistent behavior with {@link ThymeleafParameter#isReadOnly()}.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return CartridgeWebUtils.isReadOnly((ModelElementFacade) this);
    }

    /**
     * @return constructDummyArray()
     * @see ThymeleafAttribute#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray();
    }

    /**
     * @param validatorType
     * @return getValidatorArgs
     * @see ThymeleafAttribute#getValidatorArgs(String)
     */
    protected Collection handleGetValidatorArgs(final String validatorType)
    {
        return CartridgeWebUtils.getValidatorArgs(
            (ModelElementFacade)this.THIS(),
            validatorType);
    }

    /**
     * @return isStrictDateFormat
     * @see ThymeleafAttribute#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        return CartridgeWebUtils.isStrictDateFormat((ModelElementFacade)this.THIS());
    }

    /**
     * @param ownerParameter
     * @return dateFormatter
     * @see ThymeleafAttribute#getDateFormatter(org.andromda.cartridges.thymeleaf.metafacades.ThymeleafParameter)
     */
    protected String handleGetDateFormatter(final ThymeleafParameter ownerParameter)
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getFormPropertyId(ownerParameter) + "DateFormatter" : null;
    }

    /**
     * @param ownerParameter
     * @return timeFormatter
     * @see ThymeleafAttribute#getTimeFormatter(org.andromda.cartridges.thymeleaf.metafacades.ThymeleafParameter)
     */
    protected String handleGetTimeFormatter(final ThymeleafParameter ownerParameter)
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isTimeType() ? this.getFormPropertyId(ownerParameter) + "TimeFormatter" : null;
    }

    /**
     * Overridden to provide quotes around string types.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        if (StringUtils.isNotBlank(defaultValue))
        {
            final ClassifierFacade type = this.getType();
            if (type != null && type.isStringType())
            {
                defaultValue = "\"" + defaultValue + "\"";
            }
        }
        return defaultValue;
    }

    /**
     * @return isEqualValidator
     * @see ThymeleafAttribute#isEqualValidator()
     */
    protected boolean handleIsEqualValidator()
    {
        final String equal = CartridgeWebUtils.getEqual((ModelElementFacade)this.THIS());
        return equal != null && equal.trim().length() > 0;
    }

    /**
     * @param ownerParameter
     * @return isBackingValueRequired
     * @see ThymeleafAttribute#isBackingValueRequired(org.andromda.metafacades.uml.FrontEndParameter)
     */
    protected boolean handleIsBackingValueRequired(final FrontEndParameter ownerParameter)
    {
        boolean required = false;
        if (ownerParameter != null)
        {
            if (ownerParameter.isActionParameter())
            {
                required = this.isInputTable();
                final ClassifierFacade type = this.getType();

                if (!required && type != null)
                {
                    final String name = this.getName();
                    final String typeName = type.getFullyQualifiedName();

                    // - if the parameter is not selectable but on a targetting page it IS selectable we must
                    //   allow the user to set the backing list too
                    final Collection<FrontEndView> views = ownerParameter.getAction().getTargetViews();
                    for (final Iterator<FrontEndView> iterator = views.iterator(); iterator.hasNext() && !required;)
                    {
                        final Collection<FrontEndParameter> parameters = iterator.next().getAllActionParameters();
                        for (final Iterator<FrontEndParameter> parameterIterator = parameters.iterator();
                            parameterIterator.hasNext() && !required;)
                        {
                            final FrontEndParameter object = parameterIterator.next();
                            if (object instanceof ThymeleafParameter)
                            {
                                final ThymeleafParameter parameter = (ThymeleafParameter)object;
                                final String parameterName = parameter.getName();
                                final ClassifierFacade parameterType = parameter.getType();
                                if (parameterType != null)
                                {
                                    final String parameterTypeName = parameterType.getFullyQualifiedName();
                                    if (name.equals(parameterName) && typeName.equals(parameterTypeName))
                                    {
                                        required = parameter.isInputTable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (ownerParameter.isControllerOperationArgument())
            {
                final String name = this.getName();
                final Collection<FrontEndAction> actions = ownerParameter.getControllerOperation().getDeferringActions();
                for (final Iterator<FrontEndAction> actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    final ThymeleafAction action = (ThymeleafAction)actionIterator.next();
                    final Collection<FrontEndParameter> formFields = action.getFormFields();
                    for (final Iterator<FrontEndParameter> fieldIterator = formFields.iterator();
                        fieldIterator.hasNext() && !required;)
                    {
                        final FrontEndParameter object = fieldIterator.next();
                        if (object instanceof ThymeleafParameter)
                        {
                            final ThymeleafParameter parameter = (ThymeleafParameter)object;
                            if (name.equals(parameter.getName()))
                            {
                                required = parameter.isBackingValueRequired();
                            }
                        }
                    }
                }
            }
        }
        return required;
    }

    /**
     * @return present
     * @see ThymeleafAttribute#isInputTypePresent()
     */
    protected boolean handleIsInputTypePresent()
    {
        boolean present = false;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            present =
                (StringUtils.isNotBlank(this.getInputType()) || type.isDateType() || type.isBooleanType()) &&
                !this.isPlaintext();
        }
        return present;
    }

    /**
     * @return findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS)
     * @see ThymeleafAttribute#getInputTableIdentifierColumns()
     */
    protected String handleGetInputTableIdentifierColumns()
    {
        return Objects.toString(this.findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS), "").trim();
    }

    /**
     * @return maxlength
     * @see ThymeleafAttribute#getMaxLength()
     */
    protected String handleGetMaxLength()
    {
        final Collection<List<String>> vars = this.getValidatorVars(null);
        if(vars == null)
        {
            return null;
        }
        for(final List<String> values : vars)
        {
            if("maxlength".equals(values.get(0)))
            {
                return values.get(1);
            }
        }
        return null;
    }
}
