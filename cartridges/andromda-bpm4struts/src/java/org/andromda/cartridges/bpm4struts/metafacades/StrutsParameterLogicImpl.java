package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
 */
public class StrutsParameterLogicImpl
        extends StrutsParameterLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
{
    private String widgetType = null;
    private String resetValue = null;
    private Collection validatorTypes = null;
    private Collection validatorVars = null;
    private Collection validatorArgs = null;

    // ---------------- constructor -------------------------------

    public StrutsParameterLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsParameter ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getGetterName()()
     */
    public java.lang.String getGetterName()
    {
        return "get" + StringUtilsHelper.upperCaseFirstLetter(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getSetterName()()
     */
    public java.lang.String getSetterName()
    {
        return "set" + StringUtilsHelper.upperCaseFirstLetter(getName());
    }

    /**
     * @todo we need null or zero length when returning ?
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetValue()()
     */
    public java.lang.String getNullValue()
    {
        final String type = getFullyQualifiedName();
        if ("boolean".equals(type))
            return "false";
        else if (getType().isPrimitiveType())
            return "0";
//        else if (isArray()) return "new Object[0]";   //
//        else if (isCollection()) return "java.util.Collections.EMPTY_LIST";   // same for collection
        else
            return "null";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#mustReset()()
     */
    public boolean mustReset()
    {
        final String type = getFullyQualifiedName();
        return Boolean.class.getName().equals(type) || "boolean".equals(type) || getType().isArrayType();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageKey()()
     */
    public java.lang.String getMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageValue()()
     */
    public java.lang.String getMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleKey()()
     */
    public java.lang.String getTitleKey()
    {
        return getMessageKey() + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleValue()()
     */
    public java.lang.String getTitleValue()
    {
        return getMessageValue();
    }

    public String getWidgetType()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && widgetType != null) return widgetType;

        final String fieldType = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE).toString();

        if (fieldType == null)
        {
            final String parameterType = getType().getFullyQualifiedName();
            if (isValidatorBoolean(parameterType)) return "checkbox";
            if (getType().isCollectionType() || getType().isArrayType()) return "select";
            widgetType = "text";
        } else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_TEXTAREA.equalsIgnoreCase(fieldType))
        {
            widgetType = "textarea";
        } else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_HIDDEN.equalsIgnoreCase(fieldType))
        {
            widgetType = "hidden";
        } else if (fieldType.toLowerCase().startsWith(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_RADIO))
        {
            widgetType = "radio";
        } else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_CHECKBOX.equalsIgnoreCase(fieldType))
        {
            widgetType = "checkbox";
        } else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_SELECT.equalsIgnoreCase(fieldType))
        {
            widgetType = "select";
        } else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_PASSWORD.equalsIgnoreCase(fieldType))
        {
            widgetType = "password";
        } else
        {
            widgetType = (getType().isCollectionType() || getType().isArrayType()) ? "select" : "text";
        }
        return widgetType;
    }

    public boolean isMultiple()
    {
        return getType().isCollectionType() || getType().isArrayType();
    }

    public boolean hasBackingList()
    {
        return "select".equals(getWidgetType());
    }

    public String getBackingListName()
    {
        return getName() + "BackingList";
    }

    public String getBackingListType()
    {
        return "Object[]";
    }

    public String getBackingListResetValue()
    {
        return constructArray();
    }

    private String constructArray()
    {
        final String name = getName();
        return "new " + getBackingListType() + "{\"" + name + "-1\", \"" + name + "-2\", \"" + name + "-3\", \"" + name + "-4\", \"" + name + "-5\"}";
    }

    public boolean isRequired()
    {
        return isTrue(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_REQUIRED).toString());
    }

    public boolean isReadOnly()
    {
        return isTrue(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_READONLY).toString());
    }

    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) ||
                "on".equalsIgnoreCase(string) || "1".equalsIgnoreCase(string);
    }

    public String getResetValue()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && resetValue != null) return resetValue;

        final String name = getName();
        final String type = getType().getFullyQualifiedName();
        if (String.class.getName().equals(type)) return "\"" + name + "-test" + "\"";

        if ("boolean".equals(type)) resetValue = "false";
        if ("float".equals(type)) resetValue = "(float)" + name.hashCode() / hashCode();
        if ("double".equals(type)) resetValue = "(double)" + name.hashCode() / hashCode();
        if ("short".equals(type)) resetValue = "(short)" + name.hashCode();
        if ("long".equals(type)) resetValue = "(long)" + name.hashCode();
        if ("byte".equals(type)) resetValue = "(byte)" + name.hashCode();
        if ("char".equals(type)) resetValue = "(char)" + name.hashCode();
        if ("int".equals(type)) resetValue = "(int)" + name.hashCode();

        final String array = constructArray();
        if (getType().isArrayType()) resetValue = array;
        if (getType().isCollectionType()) resetValue = "java.util.Arrays.asList(" + array + ")";

        if (resetValue == null) resetValue = "\"" + name + "-test" + "\"";
        return resetValue;
    }

    protected String getValidatorFormat()
    {
        final String format = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_FORMAT).toString();
        return (format == null) ? null : format.trim();
    }

    public java.util.Collection getValidatorTypes()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && validatorTypes != null) return validatorTypes;

        final String type = getType().getFullyQualifiedName();
        final String format = getValidatorFormat();
        final boolean isRangeFormat = (format == null) ? false : isRangeFormat(format);

        final Collection validatorTypesList = new LinkedList();

        if (isRequired()) validatorTypesList.add("required");

        if (isValidatorByte(type)) validatorTypesList.add("byte");
        else if (isValidatorShort(type)) validatorTypesList.add("short");
        else if (isValidatorLong(type)) validatorTypesList.add("long");
        else if (isValidatorDate(type)) validatorTypesList.add("date");

        if (isRangeFormat)
        {
            if (isValidatorInteger(type)) validatorTypesList.add("intRange");
            if (isValidatorFloat(type)) validatorTypesList.add("floatRange");
            if (isValidatorDouble(type)) validatorTypesList.add("doubleRange");
        }
        else
        {
            if (isValidatorInteger(type)) validatorTypesList.add("integer");
            else if (isValidatorFloat(type)) validatorTypesList.add("float");
            else if (isValidatorDouble(type)) validatorTypesList.add("double");
        }

        if (format != null && isValidatorString(type))
        {
            if (isEmailFormat(format)) validatorTypesList.add("email");
            else if (isCreditCardFormat(format)) validatorTypesList.add("creditCard");
            else if (isMinLengthFormat(format)) validatorTypesList.add("minlength");
            else if (isMaxLengthFormat(format)) validatorTypesList.add("maxlength");
            else if (isPatternFormat(format)) validatorTypesList.add("mask");
        }
        return validatorTypes = validatorTypesList;
    }

    public String getValidatorMsgKey()
    {
        return getMessageKey();
    }

    public java.util.Collection getValidatorArgs(java.lang.String validatorType)
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && validatorArgs != null) return validatorArgs;

        final Collection args = new LinkedList();
        if ("intRange".equals(validatorType) || "floatRange".equals(validatorType) || "doubleRange".equals(validatorType))
        {
            args.add("${var:min}");
            args.add("${var:max}");
        } else if ("minlength".equals(validatorType))
        {
            args.add("${var:minlength}");
        } else if ("maxlength".equals(validatorType))
        {
            args.add("${var:maxlength}");
        }
        return validatorArgs = args;
    }


    public java.util.Collection getValidatorVars()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && validatorVars != null) return validatorVars;

        final Collection vars = new LinkedList();

        final String type = getType().getFullyQualifiedName();
        final String format = getValidatorFormat();
        if (format != null)
        {
            final boolean isRangeFormat = isRangeFormat(format);

            if (isRangeFormat && (isValidatorInteger(type) || isValidatorFloat(type) || isValidatorDouble(type)))
            {
                vars.add(Arrays.asList(new Object[]{"min", getRangeStart(format)}));
                vars.add(Arrays.asList(new Object[]{"max", getRangeEnd(format)}));
            } else if (isValidatorString(type))
            {
                if (isMinLengthFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"minlength", getMinLengthValue(format)}));
                } else if (isMaxLengthFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"maxlength", getMaxLengthValue(format)}));
                } else if (isPatternFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"mask", getPatternValue(format)}));
                }
            } else if (isValidatorDate(type))
            {
                if (isStrictDateFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"datePatternStrict", getDateFormat(format)}));
                } else
                {
                    vars.add(Arrays.asList(new Object[]{"datePattern", getDateFormat(format)}));
                }
            }
        }
        return validatorVars = vars;
    }

    public java.lang.String getValidWhen()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_VALIDWHEN).toString();
    }

    public Collection getOptionKeys()
    {
        final String key = getMessageKey() + '.';
        final Collection optionKeys = new LinkedList();
        final int optionCount = getOptionCount() + 1;
        for (int i = 1; i < optionCount; i++) optionKeys.add(key + i);
        return optionKeys;
    }

    private int getOptionCount()
    {
        if ("radio".equals(getWidgetType()))
        {
            String fieldType = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE).toString();
            if (fieldType.length() > Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_CHECKBOX.length())
            {
                try
                {
                    return Integer.parseInt(fieldType.substring(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_CHECKBOX.length()).trim());
                } catch (Exception exception)
                {
                    // let the next return statement handle this
                }
            }
            return 3;
        }
        return 0;
    }

    // ------------------------------------------
    private boolean isValidatorBoolean(String type)
    {
        return "boolean".equals(type) || Boolean.class.getName().equals(type);
    }

    private boolean isValidatorByte(String type)
    {
        return "byte".equals(type) || Byte.class.getName().equals(type);
    }

    private boolean isValidatorShort(String type)
    {
        return "short".equals(type) || Short.class.getName().equals(type);
    }

    private boolean isValidatorInteger(String type)
    {
        return "int".equals(type) || Integer.class.getName().equals(type);
    }

    private boolean isValidatorLong(String type)
    {
        return "long".equals(type) || Long.class.getName().equals(type);
    }

    private boolean isValidatorFloat(String type)
    {
        return "float".equals(type) || Float.class.getName().equals(type);
    }

    private boolean isValidatorDouble(String type)
    {
        return "double".equals(type) || Double.class.getName().equals(type);
    }

    private boolean isValidatorDate(String type)
    {
        return java.util.Date.class.getName().equals(type) || java.sql.Date.class.getName().equals(type);
    }

    private boolean isValidatorString(String type)
    {
        return String.class.getName().equals(type);
    }

    private boolean isEmailFormat(String format)
    {
        return "email".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private boolean isCreditCardFormat(String format)
    {
        return "creditcard".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private boolean isRangeFormat(String format)
    {
        return "range".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private boolean isPatternFormat(String format)
    {
        return "pattern".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private boolean isStrictDateFormat(String format)
    {
        return "strict".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private boolean isMinLengthFormat(String format)
    {
        return "minlength".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(getToken(format, 0, 2));
    }

    private String getRangeStart(String format)
    {
        return getToken(format, 1, 3);
    }

    private String getRangeEnd(String format)
    {
        return getToken(format, 2, 4);
    }

    private String getDateFormat(String format)
    {
        return (isStrictDateFormat(format)) ? getToken(format, 1, 3) : getToken(format, 0, 2);
    }

    private String getMinLengthValue(String format)
    {
        return getToken(format, 1, 3);
    }

    private String getMaxLengthValue(String format)
    {
        return getToken(format, 1, 3);
    }

    private String getPatternValue(String format)
    {
        return getToken(format, 1, 3);
    }

    private String getToken(String string, int index, int limit)
    {
        String[] tokens = string.split("[\\s]", limit);
        return (index >= tokens.length) ? null : tokens[index];
    }

}
