package org.andromda.cartridges.jakarta.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.cartridges.jakarta.JakartaProfile;
import org.andromda.cartridges.jakarta.JakartaUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaAttribute.
 *
 * @see JakartaAttribute
 */
public class JakartaAttributeLogicImpl
    extends JakartaAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return messageKey
     * @see JakartaAttribute#getMessageKey()
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
            ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.NORMALIZE_MESSAGES));
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @return StringUtilsHelper.toPhrase(super.getName())
     * @see JakartaAttribute#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName());
    }

    /**
     * @return format
     * @see JakartaAttribute#getFormat()
     */
    protected String handleGetFormat()
    {
        return JakartaUtils.getFormat(
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
        return (String)this.getConfiguredProperty(JakartaGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(JakartaGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @return dummyValue
     * @see JakartaAttribute#getDummyValue()
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
        return JakartaUtils.constructDummyArrayDeclaration(
            this.getName(),
            JakartaGlobals.DUMMY_ARRAY_COUNT);
    }

    /**
     * @param ownerParameter
     * @return propertyName
     * @see JakartaAttribute#getFormPropertyName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetFormPropertyName(final ParameterFacade ownerParameter)
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
     * @see JakartaAttribute#getBackingListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetBackingListName(final ParameterFacade ownerParameter)
    {
        final String backingListName =
            StringUtils.replace(
                ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.BACKING_LIST_PATTERN)),
                "{0}",
                this.getFormPropertyId(ownerParameter));
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @param ownerParameter
     * @return backingValueName
     * @see JakartaAttribute#getBackingValueName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetBackingValueName(final ParameterFacade ownerParameter)
    {
        final String backingListName =
            StringUtils.replace(
                ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.BACKING_VALUE_PATTERN)),
                "{0}",
                this.getFormPropertyId(ownerParameter));
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @param ownerParameter
     * @return labelListName
     * @see JakartaAttribute#getLabelListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetLabelListName(final ParameterFacade ownerParameter)
    {
        return StringUtils.replace(
            ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.LABEL_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
    }

    /**
     * @param ownerParameter
     * @return valueListName
     * @see JakartaAttribute#getValueListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetValueListName(final ParameterFacade ownerParameter)
    {
        return StringUtils.replace(
            ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.VALUE_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
    }

    /**
     * @param ownerParameter
     * @return formPropertyId
     * @see JakartaAttribute#getFormPropertyId(ParameterFacade)
     */
    protected String handleGetFormPropertyId(final ParameterFacade ownerParameter)
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getFormPropertyName(ownerParameter));
    }

    /**
     * @param ownerParameter
     * @return isSelectable
     * @see JakartaAttribute#isSelectable(org.andromda.metafacades.uml.FrontEndParameter)
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
                            if (object instanceof JakartaParameter)
                            {
                                final JakartaParameter parameter = (JakartaParameter)object;
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
                        if (object instanceof JakartaParameter)
                        {
                            final JakartaParameter parameter = (JakartaParameter)object;
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
     * @see JakartaAttribute#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        return !this.getValidatorTypes().isEmpty();
    }

    /**
     * @return validatorTypes
     * @see JakartaAttribute#getValidatorTypes()
     */
    protected Collection<String> handleGetValidatorTypes()
    {
        return JakartaUtils.getValidatorTypes(
            (ModelElementFacade)this.THIS(),
            this.getType());
    }

    /**
     * @param ownerParameter
     * @return validatorVars
     * @see JakartaAttribute#getValidatorVars(JakartaParameter)
     */
    protected Collection<List<String>> handleGetValidatorVars(JakartaParameter ownerParameter)
    {
        return JakartaUtils.getValidatorVars(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            ownerParameter);
    }

    /**
     * @return JakartaUtils.getValidWhen(this)
     * @see JakartaAttribute#getValidWhen()
     */
    protected String handleGetValidWhen()
    {
        return JakartaUtils.getValidWhen(this);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_TEXTAREA)
     * @see JakartaAttribute#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType(JakartaGlobals.INPUT_TEXTAREA);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_SELECT)
     * @see JakartaAttribute#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType(JakartaGlobals.INPUT_SELECT);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_PASSWORD)
     * @see JakartaAttribute#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType(JakartaGlobals.INPUT_PASSWORD);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_HIDDEN)
     * @see JakartaAttribute#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType(JakartaGlobals.INPUT_HIDDEN);
    }

    /**
     * @return isInputType(JakartaGlobals.PLAIN_TEXT)
     * @see JakartaAttribute#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType(JakartaGlobals.PLAIN_TEXT);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_RADIO)
     * @see JakartaAttribute#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType(JakartaGlobals.INPUT_RADIO);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_TEXT)
     * @see JakartaAttribute#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType(JakartaGlobals.INPUT_TEXT);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_MULTIBOX)
     * @see JakartaAttribute#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType(JakartaGlobals.INPUT_MULTIBOX);
    }

    /**
     * @return inputTable
     * @see JakartaAttribute#isInputTable()
     */
    protected boolean handleIsInputTable()
    {
        return this.getInputTableIdentifierColumns().length() > 0 || this.isInputType(JakartaGlobals.INPUT_TABLE);
    }

    /**
     * @return inputCheckbox
     * @see JakartaAttribute#isInputCheckbox()
     */
    protected boolean handleIsInputCheckbox()
    {
        boolean checkbox = this.isInputType(JakartaGlobals.INPUT_CHECKBOX);
        if (!checkbox && this.getInputType().length() == 0)
        {
            final ClassifierFacade type = this.getType();
            checkbox = type != null ? type.isBooleanType() : false;
        }
        return checkbox;
    }

    /**
     * Gets the current value of the specified input type (or an empty string
     * if one isn't specified).
     *
     * @return the input type name.
     */
    private String getInputType()
    {
        return ObjectUtils.toString(this.findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_TYPE)).trim();
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
     * @see JakartaAttribute#isInputFile()
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
     * Overridden to provide consistent behavior with {@link JakartaParameter#isReadOnly()}.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return JakartaUtils.isReadOnly(this);
    }

    /**
     * @return constructDummyArray()
     * @see JakartaAttribute#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray();
    }

    /**
     * @param validatorType
     * @return getValidatorArgs
     * @see JakartaAttribute#getValidatorArgs(String)
     */
    protected Collection handleGetValidatorArgs(final String validatorType)
    {
        return JakartaUtils.getValidatorArgs(
            (ModelElementFacade)this.THIS(),
            validatorType);
    }

    /**
     * @return isStrictDateFormat
     * @see JakartaAttribute#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        return JakartaUtils.isStrictDateFormat((ModelElementFacade)this.THIS());
    }

    /**
     * @param ownerParameter
     * @return dateFormatter
     * @see JakartaAttribute#getDateFormatter(org.andromda.cartridges.jakarta.metafacades.JakartaParameter)
     */
    protected String handleGetDateFormatter(final JakartaParameter ownerParameter)
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getFormPropertyId(ownerParameter) + "DateFormatter" : null;
    }

    /**
     * @param ownerParameter
     * @return timeFormatter
     * @see JakartaAttribute#getTimeFormatter(org.andromda.cartridges.jakarta.metafacades.JakartaParameter)
     */
    protected String handleGetTimeFormatter(final JakartaParameter ownerParameter)
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
            if (type != null && type.isStringType() && defaultValue.startsWith("\""))
            {
                defaultValue = "\"" + defaultValue + "\"";
            }
        }
        return defaultValue;
    }

    /**
     * @return isEqualValidator
     * @see JakartaAttribute#isEqualValidator()
     */
    protected boolean handleIsEqualValidator()
    {
        final String equal = JakartaUtils.getEqual((ModelElementFacade)this.THIS());
        return equal != null && equal.trim().length() > 0;
    }

    /**
     * @param ownerParameter
     * @return isBackingValueRequired
     * @see JakartaAttribute#isBackingValueRequired(org.andromda.metafacades.uml.FrontEndParameter)
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
                            if (object instanceof JakartaParameter)
                            {
                                final JakartaParameter parameter = (JakartaParameter)object;
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
                    final JakartaAction action = (JakartaAction)actionIterator.next();
                    final Collection<FrontEndParameter> formFields = action.getFormFields();
                    for (final Iterator<FrontEndParameter> fieldIterator = formFields.iterator();
                        fieldIterator.hasNext() && !required;)
                    {
                        final FrontEndParameter object = fieldIterator.next();
                        if (object instanceof JakartaParameter)
                        {
                            final JakartaParameter parameter = (JakartaParameter)object;
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
     * @see JakartaAttribute#isInputTypePresent()
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
     * @return findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS)
     * @see JakartaAttribute#getInputTableIdentifierColumns()
     */
    protected String handleGetInputTableIdentifierColumns()
    {
        return Objects.toString(this.findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS), "").trim();
    }

    /**
     * @return maxlength
     * @see JakartaAttribute#getMaxLength()
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
