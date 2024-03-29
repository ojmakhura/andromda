package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.andromda.cartridges.web.CartridgeWebGlobals;
import org.andromda.cartridges.web.CartridgeWebProfile;
import org.andromda.cartridges.web.CartridgeWebUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute
 */
public class ThymeleafManageableEntityAttributeLogicImpl
    extends ThymeleafManageableEntityAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafManageableEntityAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        String titleKey = "";

        final ClassifierFacade owner = getOwner();
        if (owner != null)
        {
            titleKey += owner.getName() + '.';
        }

        return StringUtilsHelper.toResourceMessageKey(titleKey + getName());
    }

    /**
     * @return StringUtilsHelper.toPhrase(getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return dateFormat
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getDateFormat()
     */
    protected String handleGetDateFormat()
    {
        String dateFormat = this.internalGetDateFormat();

        if (dateFormat != null)
        {
            final String[] tokens = dateFormat.split("[\\s]+");
            int tokenIndex = 0;
            if (tokenIndex < tokens.length && "strict".equals(tokens[tokenIndex].trim()))
            {
                tokenIndex++;
            }
            if (tokenIndex < tokens.length)
            {
                dateFormat = tokens[tokenIndex].trim();
            }
        }

        return dateFormat;
    }

    /**
     * @return getType().isBlobType()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isNeedsFileUpload()
     */
    protected boolean handleIsNeedsFileUpload()
    {
        return this.getType() != null && this.getType().isBlobType();
    }

    @Override
    public boolean isDisplay()
    {
        return super.isDisplay() && isEditable() && !CartridgeWebProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN.equals(this.getWidgetType());
    }
    
    /**
     * @return isHidden
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isHidden()
     */
    protected boolean handleIsHidden()
    {
        return !this.isDisplay();
    }

    /**
     * @return widgetType
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getWidgetType()
     */
    protected String handleGetWidgetType()
    {
        final Object widgetTag = findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_TYPE);
        return (widgetTag == null) ? CartridgeWebProfile.TAGGEDVALUE_INPUT_TYPE_TEXT : widgetTag.toString();
    }

    /**
     * @return isStrictDateFormat
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        final String dateFormat = this.internalGetDateFormat();
        return (dateFormat != null && dateFormat.trim().startsWith("strict"));
    }

    /**
     * @return getMessageKey() + ".online.help"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @return getDocumentation
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No field documentation has been specified" : value;
    }

    /**
     * @return format
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getFormat()
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
     * @return getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_DATEFORMAT)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getDefaultDateFormat()
     */
    protected String handleGetDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @return getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_TIMEFORMAT)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getDefaultTimeFormat()
     */
    protected String handleGetDefaultTimeFormat()
    {
        return (String)this.getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    /**
     * @return dateFormatter
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getDateFormatter()
     */
    protected String handleGetDateFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getName() + "DateFormatter" : null;
    }

    /**
     * @return timeFormatter
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getTimeFormatter()
     */
    protected String handleGetTimeFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isTimeType() ? this.getName() + "TimeFormatter" : null;
    }

    /**
     * @return backingListName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getBackingListName()
     */
    protected String handleGetBackingListName()
    {
        final String backingListName =
            StringUtils.replace(
                Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.BACKING_LIST_PATTERN)),
                "{0}",
                this.getName());
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(backingListName);
    }

    /**
     * @return valueListName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.VALUE_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return labelListName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return Objects.toString(this.getConfiguredProperty(CartridgeWebGlobals.LABEL_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return validatorTypes
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getValidatorTypes()
     */
    protected Collection handleGetValidatorTypes()
    {
        return CartridgeWebUtils.getValidatorTypes(
            (ModelElementFacade)this.THIS(),
            this.getType());
    }

    /**
     * @return !getValidatorTypes().isEmpty()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        return !this.getValidatorTypes().isEmpty();
    }

    /**
     * @return validatorVars
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getValidatorVars()
     */
    protected Collection handleGetValidatorVars()
    {
        return ThymeleafUtils.getValidatorVars(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            null);
    }

    /**
     * @return CartridgeWebUtils.getValidWhen(this)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getValidWhen()
     */
    protected String handleGetValidWhen()
    {
        return CartridgeWebUtils.getValidWhen(this);
    }

    /**
     * @return checkbox
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputCheckbox()
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
     * @return file
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputFile()
     */
    protected boolean handleIsInputFile()
    {
        boolean file = false;
        ClassifierFacade type = getType();
        if (type != null)
        {
            file = type.isFileType() || type.isBlobType();
        }
        return file;
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_HIDDEN)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_HIDDEN);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_MULTIBOX)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_MULTIBOX);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_RADIO)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_RADIO);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_PASSWORD)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_PASSWORD);
    }

    /**
     * @return isInputType(CartridgeWebGlobals.INPUT_SELECT)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_SELECT);
    }

    /**
     * @return isInputTable
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputTable()
     */
    protected boolean handleIsInputTable()
    {
        return this.getInputTableIdentifierColumns().length() > 0 || this.isInputType(CartridgeWebGlobals.INPUT_TABLE);
    }

    /**
     * @return inputTableIdentifierColumns
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getInputTableIdentifierColumns()
     */
    protected String handleGetInputTableIdentifierColumns()
    {
        return Objects.toString(this.findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS)).trim();
    }

    /**
     * @return isInputText
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_TEXT);
    }

    /**
     * @return isInputTextarea
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType(CartridgeWebGlobals.INPUT_TEXTAREA);
    }

    /**
     * @return isInputTypePresent
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isInputTypePresent()
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
     * @return dummyValue
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getDummyValue()
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
     * @return isEqualValidator
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isEqualValidator()
     */
    protected boolean handleIsEqualValidator()
    {
        final String equal = CartridgeWebUtils.getEqual((ModelElementFacade)this.THIS());
        return equal != null && equal.trim().length() > 0;
    }

    /**
     * @return isInputType(CartridgeWebGlobals.PLAIN_TEXT)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType(CartridgeWebGlobals.PLAIN_TEXT);
    }

    /**
     * @return constructDummyArray()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray();
    }

    /**
     * @param validatorType
     * @return getValidatorArgs
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getValidatorArgs(String)
     */
    protected Collection handleGetValidatorArgs(String validatorType)
    {
        return CartridgeWebUtils.getValidatorArgs(
            (ModelElementFacade)this.THIS(),
            validatorType);
    }

    /**
     * Gets the current value of the specified input type (or an empty string
     * if one isn't specified).
     *
     * @return the input type name.
     */
    private String getInputType()
    {
        return Objects.toString(this.findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_TYPE)).trim();
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
     * Overridden to provide consistent behavior with {@link ThymeleafParameter#isReadOnly()}.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    @Override
    public boolean isReadOnly()
    {
        return CartridgeWebUtils.isReadOnly(this);
    }

    /**
     * Overridden to have the same behavior as bpm4struts.
     *
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    @Override
    public boolean isRequired()
    {
        if("org.omg.uml.foundation.core".equals(metaObject.getClass().getPackage().getName()))
        {
            //if uml 1.4, keep the old behavior (like bpm4struts)
            final Object value = this.findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_REQUIRED);
            return Boolean.valueOf(Objects.toString(value)).booleanValue();
        }
        else
        {
            //if >= uml 2, default behavior
            return super.isRequired();
        }
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

    private String internalGetDateFormat()
    {
        String dateFormat = null;

        if (this.getType() != null && this.getType().isDateType())
        {
            final Object taggedValueObject = this.findTaggedValue(CartridgeWebProfile.TAGGEDVALUE_INPUT_FORMAT);
            if (taggedValueObject == null)
            {
                dateFormat = (String)this.getConfiguredProperty(CartridgeWebGlobals.PROPERTY_DEFAULT_DATEFORMAT);
            }
            else
            {
                dateFormat = taggedValueObject.toString();
            }
        }

        return dateFormat;
    }

    /**
     * @param ownerParameter
     * @return propertyName
     * @see ThymeleafAttribute#getFormPropertyName(org.andromda.metafacades.uml.ParameterFacade)
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
     * @return StringUtilsHelper.lowerCamelCaseName(this.getFormPropertyName(ownerParameter))
     * @see ThymeleafAttribute#getFormPropertyId(ParameterFacade)
     */
    protected String handleGetFormPropertyId(final ParameterFacade ownerParameter)
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getFormPropertyName(ownerParameter));
    }

    //TODO remove after 3.4 release
    /**
     * Hack to keep the compatibility with Andromda 3.4-SNAPSHOT
     * @return defaultValue
     */
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        // Put single or double quotes around default in case modeler forgot to do it. Most templates
        // declare Type attribute = $attribute.defaultValue, requiring quotes around the value
        if (defaultValue!=null && defaultValue.length()>0 && !super.isMany())
        {
            String typeName = getType().getName();
            if ("String".equals(typeName) && defaultValue.indexOf('"')<0)
            {
                defaultValue = '"' + defaultValue + '"';
            }
            else if (("char".equals(typeName) || "Character".equals(typeName))
                && !defaultValue.contains("'"))
            {
                defaultValue = "'" + defaultValue.charAt(0) + "'";
            }
        }
        if (defaultValue==null)
        {
            defaultValue="";
        }
        return defaultValue;
    }

    /**
     * @return getColumnLength()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#getMaxLength()
     */
    protected String handleGetMaxLength()
    {
        final Collection vars=getValidatorVars();
        if(vars == null)
        {
            return getColumnLength();
        }
        for(Iterator<Collection> it=vars.iterator(); it.hasNext(); )
        {
            final Object[] values=(it.next()).toArray();
            if("maxlength".equals(values[0]))
            {
                return values[1].toString();
            }
        }
        return getColumnLength();
    }

    /**
     * @return isEditable
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityAttribute#isEditable()
     */
    @Override
    protected boolean handleIsEditable() {
        Object ignore=this.findTaggedValue(CartridgeWebProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_IGNORE);
        return ignore==null || !BooleanUtils.toBoolean(Objects.toString(ignore));
    }
}
