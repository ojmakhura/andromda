package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
 */
public class StrutsParameterLogicImpl
        extends StrutsParameterLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
{
    // ---------------- constructor -------------------------------

    public StrutsParameterLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- relations ----------------------

    private boolean isActionCached = false;
    private Object cachedAction = null;

    protected Object handleGetAction()
    {
        if (isActionCached == false)
        {
            isActionCached = true;
            final Collection transitions = getModel().getAllTransitions();
            for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
            {
                Object transitionObject = iterator.next();
                if (transitionObject instanceof StrutsAction)
                {
                    StrutsAction action = (StrutsAction) transitionObject;
                    Collection parameters = action.getActionParameters();
                    if (parameters != null && parameters.contains(this))
                    {
                        cachedAction = action;
                        break;
                    }
                }
            }
        }
        return cachedAction;
    }

    private boolean isJspCached = false;
    private Object cachedJsp = null;

    protected Object handleGetJsp()
    {
        if (isJspCached == false)
        {
            isJspCached = true;
            StrutsAction action = getAction();
            if (action == null)
            {
                Collection actionStates = getModel().getAllActionStates();
                for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
                {
                    Object stateObject = iterator.next();
                    if (stateObject instanceof StrutsJsp)
                    {
                        StrutsJsp jsp = (StrutsJsp) stateObject;
                        if (jsp.getPageVariables().contains(this))
                        {
                            cachedJsp = jsp;
                            break;
                        }
                    }
                }
            }
            else
            {
                cachedJsp = action.getInput();
            }
        }
        return cachedJsp;
    }

    // -------------------- business methods ----------------------

    public String getName()
    {
        StrutsAction action = getAction();
        StrutsTrigger trigger = (action == null) ? null : action.getActionTrigger();

        return (trigger == null)
                ? StringUtilsHelper.lowerCamelCaseName(super.getName())
                : StringUtilsHelper.lowerCamelCaseName(trigger.getName()) + StringUtilsHelper.upperCamelCaseName(super.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getGetterName()()
     */
    public java.lang.String handleGetGetterName()
    {
        return "get" + StringUtilsHelper.upperCaseFirstLetter(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getSetterName()()
     */
    public java.lang.String handleGetSetterName()
    {
        return "set" + StringUtilsHelper.upperCaseFirstLetter(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetValue()()
     */
    public java.lang.String handleGetNullValue()
    {
        final ClassifierFacade type = getType();
        final String typeName = type.getFullyQualifiedName(true);
        if (isValidatorBoolean(typeName) && type.isPrimitiveType())
            return "false";
        else if (type.isPrimitiveType())
            return "0";
        else
            return "null";
    }

    /**
     * @see StrutsParameter#isResetRequired()
     */
    public boolean handleIsResetRequired()
    {
        final ClassifierFacade type = getType();
        final String typeName = type.getFullyQualifiedName(true);
        return isValidatorBoolean(typeName) || type.isArrayType();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageKey()()
     */
    public java.lang.String handleGetMessageKey()
    {
        String messageKey = StringUtilsHelper.toResourceMessageKey(getName());
        StrutsAction action = getAction();
        return (action == null) ? messageKey : action.getMessageKey() + '.' + messageKey;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageValue()()
     */
    public java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleKey()()
     */
    public java.lang.String handleGetTitleKey()
    {
        return getMessageKey() + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleValue()()
     */
    public java.lang.String handleGetTitleValue()
    {
        String requiredSuffix = "";
        if (isRequired())
        {
            requiredSuffix = " is required";
        }

        String dateSuffix = "";
        if (isDate())
        {
            dateSuffix =
                    (isStrictDateFormat())
                    ? " (use this strict format: " + getDateFormat() + ")"
                    : " (use this lenient format: " + getDateFormat() + ")";
        }

        String documentation = getDocumentation("", 64, false);
        return StringUtilsHelper.toResourceMessage((StringUtils.isBlank(documentation))
                ? super.getName() + requiredSuffix + dateSuffix
                : documentation.trim().replaceAll("\n", "<br/>"));
    }

    public String handleGetDocumentationKey()
    {
        return getMessageKey() + ".documentation";
    }

    public String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    public String handleGetOnlineHelpKey()
    {
        return getMessageKey() + ".online.help";
    }

    public String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final String format = getValidatorFormat();
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No field documentation has been specified" : value);
        buffer.append(crlf);
        buffer.append(crlf);

        buffer.append(isRequired() ? "This field is required" : "This field is optional");
        buffer.append(crlf);

        if ("password".equals(getWidgetType()))
        {
            buffer.append("This is a password field, it will not show the data you enter, " +
                    "each character will be masked using an asterisk");
            buffer.append(crlf);
        }

        if (isCreditCardFormat(format))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://www.beachnet.com/~hstiles/cardtype.html\" target=\"_blank\">creditcard</a> ");
            buffer.append(crlf);
        }

        if (isDate())
        {
            String dateFormat = getDateFormat();
            buffer.append("This field represents a date and should be formatted like this " +
                    "<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html\" " +
                    "target=\"_jdk\">");
            buffer.append(dateFormat + "</a> ");

            if (isStrictDateFormat())
            {
                buffer.append("This format is strict in the sense that the parser will not use any heuristics in " +
                        "order to guess the intended date in case the input would not perfectly match the format");
            }
            else
            {
                buffer.append("This format is lenient in the sense that the parser will attempt to use heuristics in " +
                        "order to guess the intended date in case the input would not perfectly match the format");
            }
            buffer.append(crlf);
            buffer.append("A calendar has been foreseen to select a date from, it will automatically convert the date " +
                    "to the appropriate format.");
            buffer.append(crlf);
        }

        if (isEmailFormat(format))
        {
            buffer.append("The value of this field should reflect an email address");
            buffer.append(crlf);
        }

        if (isMaxLengthFormat(format))
        {
            buffer.append("This field should not contain more than ");
            buffer.append(getMaxLengthValue(format));
            buffer.append(" characters");
            buffer.append(crlf);
        }

        if (isMinLengthFormat(format))
        {
            buffer.append("This field should contain at least ");
            buffer.append(getMinLengthValue(format));
            buffer.append(" characters");
            buffer.append(crlf);
        }

        if (isPatternFormat(format))
        {
            buffer.append("The value should match this ");
            buffer.append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html\" target=\"_jdk\">");
            buffer.append("regular expression</a>: ");
            buffer.append(getPatternValue(format));
            buffer.append(crlf);
        }

        if (isRangeFormat(format))
        {
            buffer.append("The value of this field should be in the range of ");
            buffer.append(getRangeStart(format));
            buffer.append(" to ");
            buffer.append(getRangeEnd(format));
            buffer.append(crlf);
        }

        if (isReadOnly())
        {
            buffer.append("The value of this field cannot be changed, it is read-only");
            buffer.append(crlf);
        }

        String datatype = getFullyQualifiedName(true);
        if (isValidatorBoolean(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">boolean</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorByte(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">byte</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorChar(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">character</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorDouble(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">double precision integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorFloat(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">floating point</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorInteger(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorLong(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">long integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorShort(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" " +
                    "target=\"_jdk\">short integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorUrl(datatype))
        {
            buffer.append("The value of this field should reflect a " +
                    "<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/net/URL.html\" " +
                    "target=\"_jdk\">URL</a> value");
            buffer.append(crlf);
        }


        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    public boolean handleIsCalendarRequired()
    {
        return isDate() && String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_CALENDAR)).equals("true");
    }

    public boolean handleIsTable()
    {
        boolean isTable = this.getType() != null;
        if (isTable)
        {
            isTable = (getType().isCollectionType() ||
                    getType().isArrayType()) &&
                    (!getTableColumnNames().isEmpty());
        }
        return isTable;
    }

    public boolean handleIsTableLink()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TABLELINK) != null;
    }

    public String handleGetTableLinkTableName()
    {
        if (isTableLink())
        {
            final String link = String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TABLELINK));

            int dotOffset = link.indexOf('.');
            return (dotOffset == -1) ? link : link.substring(0, link.indexOf('.'));
        }
        return null;
    }

    public String handleGetTableLinkColumnName()
    {
        if (isTableLink())
        {
            final String link = String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TABLELINK));

            int dotOffset = link.indexOf('.');
            return (dotOffset == -1 || dotOffset >= link.length() - 1) ? super.getName() : link.substring(link.indexOf('.') + 1);
        }
        return null;
    }

    public Collection handleGetTargettedTableColumnNames()
    {
        final Collection columnNames = new HashSet();

        Collection tableLinks = getTableLinks();
        for (Iterator iterator = tableLinks.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            columnNames.add(parameter.getTableLinkColumnName());
        }
        return columnNames;
    }

    public Collection handleGetTableLinks()
    {
        if (isTable())
        {
            String thisTableName = super.getName();
            Collection tableLinks = new ArrayList();

            StrutsJsp page = getJsp();
            Collection parameters = page.getAllActionParameters();
            for (Iterator iterator = parameters.iterator(); iterator.hasNext();)
            {
                StrutsParameter parameter = (StrutsParameter) iterator.next();
                if (thisTableName.equals(parameter.getTableLinkTableName()))
                {
                    tableLinks.add(parameter);
                }
            }
            return tableLinks;
        }
        return Collections.EMPTY_LIST;
    }

    public boolean handleIsTableLinksPresent()
    {
        return getTableLinks().isEmpty() == false;
    }

    public String handleGetDecoratorPackageName()
    {
        return getJsp().getPackageName();
    }

    public String handleGetDecoratorClassName()
    {
        StrutsJsp jsp = getJsp();
        return StringUtilsHelper.upperCamelCaseName(jsp.getName()) +
                StringUtilsHelper.upperCamelCaseName(getName()) + "TableDecorator";
    }

    public String handleGetDecoratorFullPath()
    {
        return (getDecoratorPackageName() + '/' + getDecoratorClassName()).replace('.', '/');
    }

    public String handleGetTableExportTypes()
    {
        Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_TABLE_EXPORT);
        if (taggedValue == null)
        {
            return "all";
        }
        final String formats = String.valueOf(taggedValue).toLowerCase();
        byte types = 0x00;

        final byte XML = 0x01;
        final byte CSV = 0x02;
        final byte HTML = 0x04;
        final byte EXCEL = 0x08;

        if (formats.indexOf("xml") > -1) types |= XML;
        if (formats.indexOf("csv") > -1) types |= CSV;
        if (formats.indexOf("html") > -1) types |= HTML;
        if (formats.indexOf("excel") > -1) types |= EXCEL;

        if (types == 0x0F) return "all";

        final StringBuffer buffer = new StringBuffer();
        if (XML == (types & XML)) buffer.append(" xml");
        if (CSV == (types & CSV)) buffer.append(" csv");
        if (HTML == (types & HTML)) buffer.append(" html");
        if (EXCEL == (types & EXCEL)) buffer.append(" excel");

        return buffer.toString().trim();
    }

    public boolean handleIsTableExportable()
    {
        return StringUtils.isNotBlank(getTableExportTypes());
    }

    public boolean handleIsTableSortable()
    {
        Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_TABLE_SORTABLE);
        return (taggedValue == null)
                ? Bpm4StrutsProfile.TAGGED_VALUE_TABLE_SORTABLE_DEFAULT_VALUE
                : isTrue(String.valueOf(taggedValue));
    }

    public Collection handleGetTableColumnNames()
    {
        Collection columnNamesCollection = Collections.EMPTY_LIST;
        Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_TABLE_COLUMNS);
        if ((taggedValue == null) || (String.valueOf(taggedValue).matches(",")))
        {
            columnNamesCollection = Collections.EMPTY_LIST;
        }
        else
        {
            columnNamesCollection = new ArrayList();
            String columnNames = String.valueOf(taggedValue);
            String[] properties = columnNames.split(",");
            for (int i = 0; i < properties.length; i++)
            {
                String property = properties[i];
                columnNamesCollection.add(StringUtils.trimToEmpty(property));
            }
        }
        return columnNamesCollection;
    }

    public int handleGetTableMaxRows()
    {
        Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_TABLE_MAXROWS);
        int pageSize = 0;

        try
        {
            pageSize = Integer.parseInt(String.valueOf(taggedValue));
        }
        catch (Exception e)
        {
            pageSize = Bpm4StrutsProfile.TAGGED_VALUE_TABLE_MAXROWS_DEFAULT_COUNT;
        }

        return pageSize;
    }

    public int handleGetTabIndex()
    {
        final String tabIndex = String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TABINDEX));

        if (tabIndex == null)
        {
            return -1;
        }
        try
        {
            return Integer.parseInt(tabIndex);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    public String handleGetWidgetType()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE);
        final String fieldType = value == null ? null : value.toString();

        if (fieldType == null)
        {
            final String parameterType = getType().getFullyQualifiedName(true);
            if (isValidatorBoolean(parameterType)) return "checkbox";
            if (getType().isCollectionType() || getType().isArrayType()) return "select";
            return "text";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_TEXTAREA.equalsIgnoreCase(fieldType))
        {
            return "textarea";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_HIDDEN.equalsIgnoreCase(fieldType))
        {
            return "hidden";
        }
        else if (fieldType.toLowerCase().startsWith(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_RADIO))
        {
            return "radio";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_CHECKBOX.equalsIgnoreCase(fieldType))
        {
            return "checkbox";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_SELECT.equalsIgnoreCase(fieldType))
        {
            return "select";
        }
        else if (Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_PASSWORD.equalsIgnoreCase(fieldType))
        {
            return "password";
        }
        else
        {
            return (getType().isCollectionType() || getType().isArrayType()) ? "select" : "text";
        }
    }

    public boolean handleIsMultiple()
    {
        return getType().isCollectionType() || getType().isArrayType();
    }

    public String handleGetBackingListName()
    {
        return getName() + "BackingList";
    }

    public String handleGetBackingListType()
    {
        return "Object[]";
    }

    public String handleGetValueListResetValue()
    {
        return constructArray();
    }

    public boolean handleIsSelectable()
    {
        return "select".equals(getWidgetType()) && getAction() != null;   // only on action parameters
    }

    public String handleGetValueListName()
    {
        return getName() + "ValueList";
    }

    public String handleGetLabelListName()
    {
        return getName() + "LabelList";
    }

    private String constructArray()
    {
        final String name = getName();
        return "new Object[] {\"" + name + "-1\", \"" + name + "-2\", \"" + name + "-3\", \"" + name + "-4\", \"" + name + "-5\"}";
    }

    public boolean handleIsRequired()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_REQUIRED);
        return isTrue(value == null ? null : String.valueOf(value));
    }

    public boolean handleIsReadOnly()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_READONLY);
        return isTrue(value == null ? null : String.valueOf(value));
    }

    public boolean handleIsDate()
    {
        return getType().isDateType();
    }

    public String handleGetDateFormat()
    {
        final String format = getValidatorFormat();
        return (format == null) ? Bpm4StrutsProfile.TAGGED_VALUE_INPUT_DEFAULT_DATEFORMAT : getDateFormat(format);
    }

    public boolean handleIsStrictDateFormat()
    {
        final String format = getValidatorFormat();
        return (format == null) ? false : isStrictDateFormat(format);
    }

    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) ||
                "on".equalsIgnoreCase(string) || "1".equalsIgnoreCase(string);
    }

    public String handleGetResetValue()
    {
        final String name = getName();
        final String type = getType().getFullyQualifiedName(true);

        if (isValidatorString(type)) return "\"" + name + "-test" + "\"";
        if (isValidatorDate(type)) return "new java.util.Date()";

        if (getType().isPrimitiveType())
        {
            if (isValidatorBoolean(type)) return "false";
            if (isValidatorFloat(type)) return "(float)" + name.hashCode() / hashCode();
            if (isValidatorDouble(type)) return "(double)" + name.hashCode() / hashCode();
            if (isValidatorShort(type)) return "(short)" + name.hashCode();
            if (isValidatorLong(type)) return "(long)" + name.hashCode();
            if (isValidatorByte(type)) return "(byte)" + name.hashCode();
            if (isValidatorChar(type)) return "(char)" + name.hashCode();
            if (isValidatorInteger(type)) return "(int)" + name.hashCode();
        }

        final String array = constructArray();
        if (getType().isArrayType()) return array;
        if (getType().isCollectionType()) return "java.util.Arrays.asList(" + array + ")";

        return "\"" + name + "-test" + "\"";
    }

    public boolean handleIsValidationRequired()
    {
        return getValidatorTypes().isEmpty() == false;
    }

    protected String getValidatorFormat()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return (format == null) ? null : format.trim();
    }

    public java.util.Collection handleGetValidatorTypes()
    {
        final String type = getType().getFullyQualifiedName(true);
        final String format = getValidatorFormat();
        final boolean isRangeFormat = (format == null) ? false : isRangeFormat(format);

        final Collection validatorTypesList = new ArrayList();

        if (isRequired()) validatorTypesList.add("required");

        if (isValidatorByte(type))
            validatorTypesList.add("byte");
        else if (isValidatorShort(type))
            validatorTypesList.add("short");
        else if (isValidatorInteger(type))
            validatorTypesList.add("integer");
        else if (isValidatorLong(type))
            validatorTypesList.add("long");
        else if (isValidatorFloat(type))
            validatorTypesList.add("float");
        else if (isValidatorDouble(type))
            validatorTypesList.add("double");
        else if (isValidatorDate(type))
            validatorTypesList.add("date");
        else if (isValidatorUrl(type))
            validatorTypesList.add("url");

        if (isRangeFormat)
        {
            if (isValidatorInteger(type)) validatorTypesList.add("intRange");
            if (isValidatorFloat(type)) validatorTypesList.add("floatRange");
            if (isValidatorDouble(type)) validatorTypesList.add("doubleRange");
        }

        if (format != null && isValidatorString(type))
        {
            if (isEmailFormat(format))
                validatorTypesList.add("email");
            else if (isCreditCardFormat(format))
                validatorTypesList.add("creditCard");
            else if (isMinLengthFormat(format))
                validatorTypesList.add("minlength");
            else if (isMaxLengthFormat(format))
                validatorTypesList.add("maxlength");
            else if (isPatternFormat(format)) validatorTypesList.add("mask");
        }
        return validatorTypesList;
    }

    public String handleGetValidatorMsgKey()
    {
        return getMessageKey();
    }

    public java.util.Collection handleGetValidatorArgs(java.lang.String validatorType)
    {
        final Collection args = new ArrayList();
        if ("intRange".equals(validatorType) || "floatRange".equals(validatorType) || "doubleRange".equals(validatorType))
        {
            args.add("${var:min}");
            args.add("${var:max}");
        }
        else if ("minlength".equals(validatorType))
        {
            args.add("${var:minlength}");
        }
        else if ("maxlength".equals(validatorType))
        {
            args.add("${var:maxlength}");
        }
        return args;
    }


    public java.util.Collection handleGetValidatorVars()
    {
        final Collection vars = new ArrayList();

        final String type = getType().getFullyQualifiedName(true);
        final String format = getValidatorFormat();
        if (format != null)
        {
            final boolean isRangeFormat = isRangeFormat(format);

            if (isRangeFormat && (isValidatorInteger(type) || isValidatorFloat(type) || isValidatorDouble(type)))
            {
                vars.add(Arrays.asList(new Object[]{"min", getRangeStart(format)}));
                vars.add(Arrays.asList(new Object[]{"max", getRangeEnd(format)}));
            }
            else if (isValidatorString(type))
            {
                if (isMinLengthFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"minlength", getMinLengthValue(format)}));
                }
                else if (isMaxLengthFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"maxlength", getMaxLengthValue(format)}));
                }
                else if (isPatternFormat(format))
                {
                    vars.add(Arrays.asList(new Object[]{"mask", getPatternValue(format)}));
                }
            }
        }
        if (isValidatorDate(type))
        {
            if (format != null && isStrictDateFormat(format))
            {
                vars.add(Arrays.asList(new Object[]{"datePatternStrict", getDateFormat()}));
            }
            else
            {
                vars.add(Arrays.asList(new Object[]{"datePattern", getDateFormat()}));
            }
        }
        return vars;
    }

    public java.lang.String handleGetValidWhen()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_VALIDWHEN);
        return value == null ? null : value.toString();
    }

    public Collection handleGetOptionKeys()
    {
        final String key = getMessageKey() + '.';
        final Collection optionKeys = new ArrayList();
        final int optionCount = getOptionCount() + 1;
        for (int i = 1; i < optionCount; i++) optionKeys.add(key + i);
        return optionKeys;
    }

    public Collection handleGetOptionValues()
    {
        Collection optionValues = new ArrayList();
        if ("radio".equals(getWidgetType()))
        {
            Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE);

            if (value != null)
            {
                String valueString = String.valueOf(value).trim();
                int optionCount = Bpm4StrutsProfile.TAGGED_VALUE_INPUT_TYPE_OPTION_DEFAULT_COUNT;
                if (valueString.length() > 5)
                {
                    try
                    {
                        optionCount = Integer.parseInt(valueString.substring(5).trim());
                    }
                    catch (Exception exception)
                    {
                        String[] options = valueString.substring(5).replaceAll("[\\s]+", "").split("[,]");
                        for (int i = 0; i < options.length; i++)
                        {
                            optionValues.add(options[i].trim());
                        }
                        return optionValues;
                    }
                }

                String name = getName();
                for (int i = 1; i <= optionCount; i++)
                {
                    optionValues.add(name + '-' + optionCount);
                }
            }
        }
        return optionValues;
    }

    public int handleGetOptionCount()
    {
        return getOptionValues().size();
    }

    // ------------------------------------------
    private boolean isValidatorBoolean(String type)
    {
        return "datatype.boolean".equals(type) || "datatype.Boolean".equals(type);
    }

    private boolean isValidatorChar(String type)
    {
        return "datatype.char".equals(type) || "datatype.Char".equals(type);
    }

    private boolean isValidatorByte(String type)
    {
        return "datatype.byte".equals(type) || "datatype.Byte".equals(type);
    }

    private boolean isValidatorShort(String type)
    {
        return "datatype.short".equals(type) || "datatype.Short".equals(type);
    }

    private boolean isValidatorInteger(String type)
    {
        return "datatype.int".equals(type) || "datatype.Integer".equals(type);
    }

    private boolean isValidatorLong(String type)
    {
        return "datatype.long".equals(type) || "datatype.Long".equals(type);
    }

    private boolean isValidatorFloat(String type)
    {
        return "datatype.float".equals(type) || "datatype.Float".equals(type);
    }

    private boolean isValidatorDouble(String type)
    {
        return "datatype.double".equals(type) || "datatype.Double".equals(type);
    }

    private boolean isValidatorDate(String type)
    {
        return "datatype.Date".equals(type);
    }

    private boolean isValidatorUrl(String type)
    {
        return "datatype.URL".equals(type);
    }

    private boolean isValidatorString(String type)
    {
        return "datatype.String".equals(type);
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
        return getToken(format, 2, 3);
    }

    private String getDateFormat(String format)
    {
        return (isStrictDateFormat(format)) ? getToken(format, 1, 2) : getToken(format, 0, 1);
    }

    private String getMinLengthValue(String format)
    {
        return getToken(format, 1, 2);
    }

    private String getMaxLengthValue(String format)
    {
        return getToken(format, 1, 2);
    }

    private String getPatternValue(String format)
    {
        return '^' + getToken(format, 1, 2) + '$';
    }

    private String getToken(String string, int index, int limit)
    {
        if (string == null) return null;

        String[] tokens = string.split("[\\s]+", limit);
        return (index >= tokens.length) ? null : tokens[index];
    }

}
