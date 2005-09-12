package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFAttribute.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute
 */
public class JSFAttributeLogicImpl
    extends JSFAttributeLogic
{

    public JSFAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();
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
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.NORMALIZE_MESSAGES));
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName());
    }
    
    /**
     * Retrieves the input format defined by the {@link JSFProfile#TAGGEDVALUE_INPUT_FORMAT}.
     *
     * @return the input format.
     */
    private final String getDisplayFormat()
    {
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getFormat()
     */
    protected String handleGetFormat()
    {
        String format = null;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            format = this.getDisplayFormat();
            if (format == null)
            {
                if (type.isTimeType())
                {
                    format = this.getDefaultTimeFormat();
                }
                else if (type.isDateType())
                {
                    format = this.getDefaultDateFormat();
                }
            }
            else if (type.isDateType())
            {
                format = JSFUtils.getDateFormat(format);
            }
        }
        return format;
    }
    
    /**
     * @return the default time format pattern as defined using the configured property
     */
    private String getDefaultTimeFormat()
    {
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }
    
    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getDummyValue()
     */
    protected String handleGetDummyValue()
    {
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String typeName = type.getFullyQualifiedName();
            final String name = this.getName();
            if ("java.lang.String".equals(typeName)) return "\"" + name + "-test" + "\"";
            if ("java.util.Date".equals(typeName)) return "new java.util.Date()";
            if ("java.sql.Date".equals(typeName)) return "new java.sql.Date(new java.util.Date().getTime())";
            if ("java.sql.Timestamp".equals(typeName)) return "new java.sql.Timestamp(new Date().getTime())";
            if ("java.util.Calendar".equals(typeName)) return "java.util.Calendar.getInstance()";
            if ("int".equals(typeName)) return "(int)" + name.hashCode();
            if ("boolean".equals(typeName)) return "false";
            if ("long".equals(typeName)) return "(long)" + name.hashCode();
            if ("char".equals(typeName)) return "(char)" + name.hashCode();
            if ("float".equals(typeName)) return "(float)" + name.hashCode() / hashCode();
            if ("double".equals(typeName)) return "(double)" + name.hashCode() / hashCode();
            if ("short".equals(typeName)) return "(short)" + name.hashCode();
            if ("byte".equals(typeName)) return "(byte)" + name.hashCode();
            if ("java.lang.Integer".equals(typeName)) return "new Integer((int)" + name.hashCode() + ")";
            if ("java.lang.Boolean".equals(typeName)) return "Boolean.FALSE";
            if ("java.lang.Long".equals(typeName)) return "new Long((long)" + name.hashCode() + ")";
            if ("java.lang.Character".equals(typeName)) return "new Character(char)" + name.hashCode() + ")";
            if ("java.lang.Float".equals(typeName)) return "new Float((float)" + name.hashCode() / hashCode() + ")";
            if ("java.lang.Double".equals(typeName)) return "new Double((double)" + name.hashCode() / hashCode() + ")";
            if ("java.lang.Short".equals(typeName)) return "new Short((short)" + name.hashCode() + ")";
            if ("java.lang.Byte".equals(typeName)) return "new Byte((byte)" + name.hashCode() + ")";
            //if (type.isArrayType()) return constructDummyArray();
            if (type.isSetType()) return "new java.util.HashSet(java.util.Arrays.asList(" + constructDummyArray() + "))";
            if (type.isCollectionType()) return "java.util.Arrays.asList(" + constructDummyArray() + ")";

            // maps and others types will simply not be treated
        }
        return "null";
    }
    
    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @return A String representing Java code for the initialization of an array.
     */
    private final String constructDummyArray()
    {
        return JSFUtils.constructDummyArrayDeclaration(this.getName(), JSFGlobals.DUMMY_ARRAY_COUNT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getFormPropertyName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetFormPropertyName(final ParameterFacade ownerParameter)
    {
        final StringBuffer propertyName = new StringBuffer();
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getBackingListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetBackingListName(final ParameterFacade ownerParameter)
    {
        final String backingListName = StringUtils.replace(ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.BACKING_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getLabelListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetLabelListName(final ParameterFacade ownerParameter)
    {
        return StringUtils.replace(ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.LABEL_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getValueListName(org.andromda.metafacades.uml.ParameterFacade)
     */
    protected String handleGetValueListName(final ParameterFacade ownerParameter)
    {
        return StringUtils.replace(ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.VALUE_LIST_PATTERN)),
            "{0}",
            this.getFormPropertyId(ownerParameter));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getFormPropertyId(java.lang.String)
     */
    protected String handleGetFormPropertyId(final ParameterFacade ownerParameter)
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getFormPropertyName(ownerParameter));
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isSelectable(org.andromda.metafacades.uml.FrontEndParameter)
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
    
                    // - if the parameter is not selectable but on a targetting page it IS selectable we must
                    //   allow the user to set the backing list too                 
                    final Collection views = ownerParameter.getAction().getTargetViews();
                    for (final Iterator iterator = views.iterator(); iterator.hasNext() && !selectable;)
                    {
                        final FrontEndView view = (FrontEndView)iterator.next();
                        final Collection parameters = view.getAllActionParameters();
                        for (final Iterator parameterIterator = parameters.iterator();
                            parameterIterator.hasNext() && !selectable;)
                        {
                            final JSFParameter parameter = (JSFParameter)parameterIterator.next();
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
            else if (ownerParameter.isControllerOperationArgument())
            {
                final String name = this.getName();
                final Collection actions = ownerParameter.getControllerOperation().getDeferringActions();
                for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    final JSFAction action = (JSFAction)actionIterator.next();
                    final Collection formFields = action.getFormFields();
                    for (final Iterator fieldIterator = formFields.iterator(); fieldIterator.hasNext() && !selectable;)
                    {
                        final JSFParameter parameter = (JSFParameter)fieldIterator.next();
                        if (name.equals(parameter.getName()))
                        {
                            selectable = parameter.isSelectable();
                        }
                    }
                }
            }
        }
        return selectable;
    }
    
    /**
     * Retrieves the input format defined by the {@link JSFProfile#TAGGEDVALUE_INPUT_FORMAT}.
     *
     * @return the input format.
     */
    private final String getInputFormat()
    {
        return JSFUtils.getInputFormat(this);
    }
    
    /**
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    private boolean isRangeFormat(final String format)
    {
        return JSFUtils.isRangeFormat(format);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getValidatorTypes()
     */
    protected java.util.Collection handleGetValidatorTypes()
    {
        final Collection validatorTypesList = new ArrayList();
        final ClassifierFacade type = getType();
        if (type != null)
        {
            final String format = this.getInputFormat();
            final boolean isRangeFormat = format != null && isRangeFormat(format);
            if (this.isRequired())
            {
                validatorTypesList.add("required");
            }
            if (this.isByte())
            {
                validatorTypesList.add("byte");
            }
            else if (this.isShort())
            {
                validatorTypesList.add("short");
            }
            else if (this.isInteger())
            {
                validatorTypesList.add("integer");
            }
            else if (this.isLong())
            {
                validatorTypesList.add("long");
            }
            else if (this.isFloat())
            {
                validatorTypesList.add("float");
            }
            else if (this.isDouble())
            {
                validatorTypesList.add("double");
            }
            else if (this.isDate())
            {
                validatorTypesList.add("date");
            }
            else if (this.isTime())
            {
                validatorTypesList.add("time");
            }
            else if (this.isUrl())
            {
                validatorTypesList.add("url");
            }

            if (isRangeFormat)
            {
                if (this.isInteger() || this.isShort() || this.isLong())
                {
                    validatorTypesList.add("intRange");
                }
                if (this.isFloat())
                {
                    validatorTypesList.add("floatRange");
                }
                if (this.isDouble())
                {
                    validatorTypesList.add("doubleRange");
                }
            }

            if (format != null)
            {
                if (this.isString() && JSFUtils.isEmailFormat(format))
                {
                    validatorTypesList.add("email");
                }
                else if (this.isString() && JSFUtils.isCreditCardFormat(format))
                {
                    validatorTypesList.add("creditCard");
                }
                else
                {
                    Collection formats = findTaggedValues(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (JSFUtils.isMinLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add("minlength");
                        }
                        else if (JSFUtils.isMaxLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add("maxlength");
                        }
                        else if (JSFUtils.isPatternFormat(additionalFormat))
                        {
                            validatorTypesList.add("mask");
                        }
                    }
                }
            }

            if (this.getValidWhen() != null)
            {
                validatorTypesList.add("validwhen");
            }
        }

        // - custom (paramterized) validators are allowed here
        final Collection taggedValues = findTaggedValues(JSFProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());
            validatorTypesList.add(JSFUtils.parseValidatorName(validator));
        }
        return validatorTypesList;
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidatorVars()
     */
    protected java.util.Collection handleGetValidatorVars()
    {
        final Map vars = new HashMap();

        final ClassifierFacade type = getType();
        if (type != null)
        {
            final String format = this.getInputFormat();
            if (format != null)
            {
                final boolean isRangeFormat = JSFUtils.isRangeFormat(format);

                if (isRangeFormat)
                {
                    final String min = "min";
                    final String max = "max";
                    vars.put(
                        min,
                        Arrays.asList(new Object[] {min, JSFUtils.getRangeStart(format)}));
                    vars.put(
                        max,
                        Arrays.asList(new Object[] {max, JSFUtils.getRangeEnd(format)}));
                }
                else
                {
                    final Collection formats = findTaggedValues(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        final String additionalFormat = String.valueOf(formatIterator.next());
                        final String minlength = "minlength";
                        final String maxlength = "maxlength";
                        final String mask = "mask";
                        if (JSFUtils.isMinLengthFormat(additionalFormat))
                        {
                            vars.put(
                                minlength,
                                Arrays.asList(new Object[] {minlength, JSFUtils.getMinLengthValue(additionalFormat)}));
                        }
                        else if (JSFUtils.isMaxLengthFormat(additionalFormat))
                        {
                            vars.put(
                                maxlength,
                                Arrays.asList(new Object[] {maxlength, JSFUtils.getMaxLengthValue(additionalFormat)}));
                        }
                        else if (JSFUtils.isPatternFormat(additionalFormat))
                        {
                            vars.put(
                                mask,
                                Arrays.asList(new Object[] {mask, JSFUtils.getPatternValue(additionalFormat)}));
                        }
                    }
                }
            }
            if (this.isDate())
            {
                final String datePatternStrict = "datePatternStrict";
                if (format != null && JSFUtils.isStrictDateFormat(format))
                {
                    vars.put(
                        datePatternStrict,
                        Arrays.asList(new Object[] {datePatternStrict, this.getFormat()}));
                }
                else
                {
                    final String datePattern = "datePattern";
                    vars.put(
                        datePattern,
                        Arrays.asList(new Object[] {datePattern, this.getFormat()}));
                }
            }
            if (this.isTime())
            {
                final String timePattern = "timePattern";
                vars.put(
                    timePattern,
                    Arrays.asList(new Object[] {timePattern, this.getFormat()}));
            }

            final String validWhen = getValidWhen();
            if (validWhen != null)
            {
                final String test = "test";
                vars.put(
                    test,
                    Arrays.asList(new Object[] {test, validWhen}));
            }
        }

        // - custom (parameterized) validators are allowed here
        //   in this case we will reuse the validator arg values
        final Collection taggedValues = findTaggedValues(JSFProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            final String validator = String.valueOf(iterator.next());

            // - guaranteed to be of the same length
            final List validatorVars = JSFUtils.parseValidatorVars(validator);
            final List validatorArgs = JSFUtils.parseValidatorArgs(validator);

            for (int ctr = 0; ctr < validatorVars.size(); ctr++)
            {
                final String validatorVar = (String)validatorVars.get(ctr);
                final String validatorArg = (String)validatorArgs.get(ctr);
                vars.put(
                    validatorVar,
                    Arrays.asList(new Object[] {validatorVar, validatorArg}));
            }
        }

        return vars.values();
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getValidWhen()
     */
    protected java.lang.String handleGetValidWhen()
    {
        return JSFUtils.getValidWhen(this);
    }
    
    /**
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    private boolean isByte()
    {
        return JSFUtils.isByte(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    private boolean isShort()
    {
        return JSFUtils.isShort(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    private boolean isInteger()
    {
        return JSFUtils.isInteger(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    private boolean isLong()
    {
        return JSFUtils.isLong(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    private boolean isFloat()
    {
        return JSFUtils.isFloat(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a double precision floating point, <code>false</code> otherwise
     */
    private boolean isDouble()
    {
        return JSFUtils.isDouble(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a date, <code>false</code> otherwise
     */
    private boolean isDate()
    {
        return JSFUtils.isDate(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a time, <code>false</code> otherwise
     */
    private boolean isTime()
    {
        return JSFUtils.isTime(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    private boolean isUrl()
    {
        return JSFUtils.isUrl(this.getType());
    }

    /**
     * @return <code>true</code> if the type of this field is a String, <code>false</code> otherwise
     */
    private boolean isString()
    {
        return JSFUtils.isString(this.getType());
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType(JSFGlobals.INPUT_TEXTAREA);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType(JSFGlobals.INPUT_SELECT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType(JSFGlobals.INPUT_PASSWORD);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType(JSFGlobals.INPUT_HIDDEN);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType(JSFGlobals.PLAIN_TEXT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType(JSFGlobals.INPUT_RADIO);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType(JSFGlobals.INPUT_TEXT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType(JSFGlobals.INPUT_MULTIBOX);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputCheckbox()
     */
    protected boolean handleIsInputCheckbox()
    {
        boolean checkbox = this.isInputType(JSFGlobals.INPUT_CHECKBOX);
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
    private final String getInputType()
    {
        return ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_TYPE)).trim();
    }
    
    /**
     * Indicates whether or not this parameter is of the given input type.
     *
     * @param inputType the name of the input type to check for.
     * @return true/false
     */
    private final boolean isInputType(final String inputType)
    {
        return inputType.equalsIgnoreCase(this.getInputType());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#isInputFile()
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
     * Overridden to provide consistent behavior with {@link JSFParameter#isReadOnly()}.
     * 
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return JSFUtils.isReadOnly(this);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray();
    }

}