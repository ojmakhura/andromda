package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
 */
public class StrutsParameterLogicImpl
        extends StrutsParameterLogic
{
    // ---------------- constructor -------------------------------

    public StrutsParameterLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- relations ----------------------

    protected Object handleGetAction()
    {
        Object actionObject = null;

        EventFacade event = getEvent();
        if (event != null)
        {
            TransitionFacade transition = event.getTransition();
            if (transition instanceof StrutsAction)
            {
                actionObject = transition;
            }
        }
        return actionObject;
    }

    protected Object handleGetJsp()
    {
        Object jspObject = null;

        EventFacade event = getEvent();
        if (event != null)
        {
            TransitionFacade transition = event.getTransition();
            if (transition instanceof StrutsAction)
            {
                StrutsAction action = (StrutsAction) transition;
                jspObject = action.getInput();
            }
            else if (transition instanceof StrutsForward)
            {
                StrutsForward forward = (StrutsForward) transition;
                if (forward.isEnteringPage())
                {
                    jspObject = forward.getTarget();
                }
            }
        }

        return jspObject;
    }

    protected Object handleGetControllerOperation()
    {
        return getOperation();
    }

    protected Collection handleGetFormFields()
    {
        Collection formFields = null;
        if (isControllerOperationArgument() && getName() != null)
        {
            final String name = getName();
            formFields = new ArrayList();
            Collection actions = getControllerOperation().getDeferringActions();
            for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                StrutsAction action = (StrutsAction) actionIterator.next();
                Collection actionFormFields = action.getActionFormFields();
                for (Iterator fieldIterator = actionFormFields.iterator(); fieldIterator.hasNext();)
                {
                    StrutsParameter parameter = (StrutsParameter) fieldIterator.next();
                    if (name.equals(parameter.getName()))
                    {
                        formFields.add(parameter);
                    }
                }
            }
        }
        else
        {
            formFields = Collections.EMPTY_LIST;
        }
        return formFields;
    }

    // -------------------- business methods ----------------------

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getGetterName()()
     */
    protected java.lang.String handleGetGetterName()
    {
        String prefix = isValidatorBoolean(getFullyQualifiedName(true)) ? "is" : "get";
        return StringUtils.trimToEmpty(prefix) + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getSetterName()()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetValue()()
     */
    protected java.lang.String handleGetNullValue()
    {
        String nullValue = null;

        ClassifierFacade type = getType();
        if (type != null)
        {
            nullValue = type.getJavaNullString();
        }
        return nullValue;
    }

    /**
     * @see StrutsParameter#isResetRequired()
     */
    protected boolean handleIsResetRequired()
    {
        boolean resetRequired = false;

        if (isSelectable())
        {
            resetRequired = true;
        }
        else
        {
            final ClassifierFacade type = getType();
            if (type != null)
            {
                if (type.isArrayType() || type.isFileType())
                {
                    resetRequired = true;
                }
                else
                {
                    final String typeName = type.getFullyQualifiedName(true);
                    resetRequired = isValidatorBoolean(typeName);
                }
            }
        }
        return resetRequired;
    }

    protected boolean handleIsControllerOperationArgument()
    {
        return getControllerOperation() != null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageKey()()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer buffer = new StringBuffer();

        final StrutsAction action = getAction();
        if (action != null)
        {
            buffer.append(action.getMessageKey());
            buffer.append('.');
        }

        buffer.append("param.");
        buffer.append(StringUtilsHelper.toResourceMessageKey(getName()));

        return buffer.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageValue()()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleKey()()
     */
    protected java.lang.String handleGetTitleKey()
    {
        return getMessageKey() + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTitleValue()()
     */
    protected java.lang.String handleGetTitleValue()
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

    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + ".documentation";
    }

    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    protected String handleGetOnlineHelpKey()
    {
        return getMessageKey() + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
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

        final String validWhen = getValidWhen();
        if (validWhen != null)
        {
            buffer.append("This field is only valid under specific conditions, more concretely the following " +
                    "expression must evaluate true: " + validWhen);
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

    protected boolean handleIsCalendarRequired()
    {
        return isDate() && String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_CALENDAR)).equals("true");
    }

    protected boolean handleIsActionParameter()
    {
        StrutsAction action = getAction();
        return (action == null) ? false : action.getActionParameters().contains(this);
    }

    protected String handleGetCollectionImplementationType()
    {
        String typeName = null;

        ClassifierFacade type = getType();

        if (type != null)
        {
            if (type.isCollectionType() || type.isListType())
            {
                typeName = "java.util.ArrayList";
            }
            else if (type.isSetType())
            {
                typeName = "java.util.HashSet";
            }
            else
            {
                typeName = type.getFullyQualifiedName();
            }
        }
        return typeName;
    }

    protected boolean handleIsTable()
    {
        boolean isTable = false;

        ClassifierFacade type = getType();
        if (type != null)
        {
            isTable = (type.isCollectionType() ||
                    type.isArrayType()) &&
                    (!getTableColumnNames().isEmpty());
        }
        return isTable;
    }

    protected boolean handleIsTableDecoratorRequired()
    {
        boolean required = false;

        if (isTable())
        {
            Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_DECORATOR);
            if (taggedValue != null)
            {
                String taggedValueString = String.valueOf(taggedValue);
                required = Boolean.valueOf(taggedValueString).booleanValue();
            }
            else
            {
                Object property = getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_GENERATE_TABLE_DECORATORS);
                String propertyString = String.valueOf(property);
                required = Boolean.valueOf(propertyString).booleanValue();
            }
        }

        return required;
    }

    protected boolean handleIsTableLink()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TABLELINK) != null;
    }

    // @todo: review this method, since it calls 'getAllActionStates()'
    protected Object handleGetTableAction()
    {
        final String name = getName();
        Object tableAction = null;

        Collection allActionStates = getModel().getAllActionStates();
        for (Iterator actionstateIterator = allActionStates.iterator(); actionstateIterator.hasNext();)
        {
            Object actionState = actionstateIterator.next();
            if (actionState instanceof StrutsJsp)
            {
                StrutsJsp jsp = (StrutsJsp) actionState;
                if (jsp.getPageVariables().contains(this))
                {
                    // find the first action referencing this table
                    Collection actions = jsp.getActions();
                    for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext() && tableAction == null;)
                    {
                        StrutsAction pageAction = (StrutsAction) actionIterator.next();
                        Collection actionParameters = pageAction.getActionParameters();
                        for (Iterator parameterIterator = actionParameters.iterator(); parameterIterator.hasNext() && tableAction == null;)
                        {
                            StrutsParameter parameter = (StrutsParameter) parameterIterator.next();
                            if (parameter.isTableLink() && name.equals(parameter.getTableLinkTableName()))
                            {
                                tableAction = pageAction;
                            }
                        }
                    }
                }
            }
        }
        return tableAction;
    }

    protected String handleGetTableLinkTableName()
    {
        if (isTableLink())
        {
            final String link = String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TABLELINK));

            int dotOffset = link.indexOf('.');
            return (dotOffset == -1) ? link : link.substring(0, link.indexOf('.'));
        }
        return null;
    }

    protected String handleGetTableLinkColumnName()
    {
        if (isTableLink())
        {
            final String link = String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TABLELINK));

            int dotOffset = link.indexOf('.');
            return (dotOffset == -1 || dotOffset >= link.length() - 1) ? super.getName() : link.substring(link.indexOf('.') + 1);
        }
        return null;
    }

    protected Collection handleGetTargettedTableColumnNames()
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

    protected Collection handleGetTableLinks()
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

    protected boolean handleIsTableLinksPresent()
    {
        return getTableLinks().isEmpty() == false;
    }

    protected String handleGetTableDecoratorFullyQualifiedName()
    {
        String name = getTableDecoratorPackageName();
        name = (StringUtils.trimToEmpty(name) == null) ? "" :  name + '.';
        return name + getTableDecoratorClassName();
    }

    protected String handleGetTableDecoratorPackageName()
    {
        final StrutsJsp jsp = getJsp();
        return (jsp == null) ? null : jsp.getPackageName();
    }

    protected String handleGetTableDecoratorClassName()
    {
        String tableDecoratorClassName = null;

        final StrutsJsp jsp = getJsp();
        if (jsp != null)
        {
            String suffix = String.valueOf(getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_TABLE_DECORATOR_SUFFIX));
            tableDecoratorClassName = StringUtilsHelper.upperCamelCaseName(getName()) + suffix;
        }

        return tableDecoratorClassName;
    }

    protected String handleGetTableDecoratorFullPath()
    {
        return getTableDecoratorFullyQualifiedName().replace('.', '/');
    }

    protected String handleGetTableExportTypes()
    {
        String exportTypes = null;

        Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_EXPORT);
        if (taggedValues.isEmpty())
        {
            exportTypes = "xml csv excel pdf";
        }
        else
        {
            StringBuffer buffer = new StringBuffer();
            for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                String exportType = StringUtils.trimToNull(String.valueOf(iterator.next()));
                if (exportType != null)
                {
                    buffer.append(exportType);
                    buffer.append(' ');
                }
            }
            exportTypes = buffer.toString().trim();
        }

        return exportTypes;
    }

    protected boolean handleIsTableExportable()
    {
        return StringUtils.isNotBlank(getTableExportTypes());
    }

    protected boolean handleIsTableSortable()
    {
        Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
                ? Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
                : isTrue(String.valueOf(taggedValue));
    }

    protected Collection handleGetTableColumnNames()
    {
        Collection tableColumnNames = new ArrayList();

        if (!isActionParameter() && !isControllerOperationArgument())
        {
            Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_COLUMNS);
            if (taggedValues.isEmpty() == false)
            {
                for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
                {
                    String taggedValue = StringUtils.trimToNull(String.valueOf(iterator.next()));
                    if (taggedValue != null)
                    {
                        String[] properties = taggedValue.split("[,\\s]+");
                        for (int i = 0; i < properties.length; i++)
                        {
                            String property = properties[i];
                            tableColumnNames.add(property);
                        }
                    }
                }
            }
        }
        else if (isControllerOperationArgument())
        {
            tableColumnNames = new HashSet();
            final String name = StringUtils.trimToEmpty(getName());
            Collection actions = getControllerOperation().getDeferringActions();
            for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                StrutsAction action = (StrutsAction) actionIterator.next();
                Collection formFields = action.getActionFormFields();
                for (Iterator fieldIterator = formFields.iterator(); fieldIterator.hasNext();)
                {
                    StrutsParameter parameter = (StrutsParameter) fieldIterator.next();
                    if (name.equals(parameter.getName()))
                    {
                        tableColumnNames.addAll(parameter.getTableColumnNames());
                    }
                }
            }
        }
        else
        {
            tableColumnNames = Collections.EMPTY_LIST;
        }
        return tableColumnNames;
    }

    protected String handleGetTableColumnMessageKey(String columnName)
    {
        return (isTable()) ? getMessageKey() + '.' + columnName.toLowerCase() : null;
    }

    protected String handleGetTableColumnMessageValue(String columnName)
    {
        return (isTable()) ? StringUtilsHelper.toPhrase(columnName) : null;
    }

    protected int handleGetTableMaxRows()
    {
        Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_MAXROWS);
        int pageSize = 0;

        try
        {
            pageSize = Integer.parseInt(String.valueOf(taggedValue));
        }
        catch (Exception e)
        {
            pageSize = Bpm4StrutsProfile.TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT;
        }

        return pageSize;
    }

    protected String handleGetWidgetType()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE);
        final String fieldType = value == null ? null : value.toString();

        String widgetType = null;

        if (isActionParameter())
        {
            if (fieldType == null)
            {
                ClassifierFacade type = getType();
                if (type != null)
                {
                    final String parameterType = type.getFullyQualifiedName(true);

                    if (!isTableLink())
                    {
                        if (type.isFileType())
                            widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_FILE;
                        else if (isValidatorBoolean(parameterType))
                            widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX;
                        else if (isMultiple())
                            widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT;
                        else
                            widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT;
                    }
                }
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT.equalsIgnoreCase(fieldType))
            {
                widgetType = "text";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXTAREA.equalsIgnoreCase(fieldType))
            {
                widgetType = "textarea";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN.equalsIgnoreCase(fieldType))
            {
                widgetType = "hidden";
            }
            else if (fieldType.toLowerCase().startsWith(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_RADIO))
            {
                widgetType = "radio";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX.equalsIgnoreCase(fieldType))
            {
                widgetType = "checkbox";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT.equalsIgnoreCase(fieldType))
            {
                widgetType = "select";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PASSWORD.equalsIgnoreCase(fieldType))
            {
                widgetType = "password";
            }
            else if (isTableLink())
            {
                if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_LINK.equalsIgnoreCase(fieldType))
                {
                    widgetType = "link";
                }
                else if (getMultiboxPropertyName() != null)
                {
                    widgetType = "multibox";
                }
            }
            else
            {
                widgetType = (isMultiple()) ? "select" : "text";
            }
        }
        return widgetType;
    }

    protected boolean handleIsFile()
    {
        boolean file = false;

        ClassifierFacade type = getType();
        if (type != null)
        {
            file = type.isFileType();
        }
        return file;
    }

    protected boolean handleIsMultiple()
    {
        boolean multiple = false;

        ClassifierFacade type = getType();
        if (type != null)
        {
            multiple = type.isCollectionType() || type.isArrayType();
        }
        return multiple;
    }

    protected String handleGetBackingListName()
    {
        return getName() + "BackingList";
    }

    protected String handleGetBackingListType()
    {
        return "Object[]";
    }

    protected String handleGetValueListResetValue()
    {
        return constructArray();
    }

    protected boolean handleIsSelectable()
    {
        boolean selectable = false;

        if (isActionParameter())
        {
            selectable = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT.equals(getWidgetType());
            ClassifierFacade type = getType();

            if (!selectable && type != null)
            {
                String name = getName();
                String typeName = type.getFullyQualifiedName();

                /**
                 * if the parameter is not selectable but on a targetting page it _is_ selectable we must
                 * allow the user to set the backing list too
                 */
                Collection pages = getAction().getTargetPages();
                for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext() && !selectable;)
                {
                    StrutsJsp page = (StrutsJsp) pageIterator.next();
                    Collection parameters = page.getAllActionParameters();
                    for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext() && !selectable;)
                    {
                        StrutsParameter parameter = (StrutsParameter) parameterIterator.next();
                        String parameterName = parameter.getName();
                        ClassifierFacade parameterType = parameter.getType();
                        if (parameterType != null)
                        {
                            String parameterTypeName = parameterType.getFullyQualifiedName();
                            if (name.equals(parameterName) && typeName.equals(parameterTypeName))
                            {
                                selectable = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT.equals(parameter.getWidgetType());
                            }
                        }
                    }
                }
            }
        }
        else if (isControllerOperationArgument())
        {
            final String name = getName();
            Collection actions = getControllerOperation().getDeferringActions();
            for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                StrutsAction action = (StrutsAction) actionIterator.next();
                Collection formFields = action.getActionFormFields();
                for (Iterator fieldIterator = formFields.iterator(); fieldIterator.hasNext() && !selectable;)
                {
                    StrutsParameter parameter = (StrutsParameter) fieldIterator.next();
                    if (name.equals(parameter.getName()))
                    {
                        selectable = parameter.isSelectable();
                    }
                }
            }
        }
        return selectable;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return getName() + "ValueList";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return getName() + "LabelList";
    }

    private String constructArray()
    {
        final String name = getName();
        return "new Object[] {\"" + name + "-1\", \"" + name + "-2\", \"" + name + "-3\", \"" + name + "-4\", \"" + name + "-5\"}";
    }

    /**
     * Override normal parameter facade required implementation.
     *
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    public boolean isRequired()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_REQUIRED);
        return isTrue(value == null ? null : String.valueOf(value));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_READONLY);
        return isTrue(value == null ? null : String.valueOf(value));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isDate()
     */
    protected boolean handleIsDate()
    {
        boolean dateType = false;

        ClassifierFacade type = getType();
        if (type != null)
        {
            dateType = type.isDateType();
        }
        return dateType;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getDateFormat()
     */
    protected String handleGetDateFormat()
    {
        final String format = getValidatorFormat();
        return (format == null) ? getDefaultDateFormat() : getDateFormat(format);
    }

    private String getDefaultDateFormat()
    {
        String defaultDateFormat = null;
        try
        {
            defaultDateFormat = (String) getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_DATEFORMAT);
        }
        catch (Exception e)
        {
            defaultDateFormat = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_DEFAULT_DATEFORMAT;
        }
        return defaultDateFormat;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        final String format = getValidatorFormat();
        return (format == null) ? false : isStrictDateFormat(format);
    }

    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) ||
                "on".equalsIgnoreCase(string) || "1".equalsIgnoreCase(string);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetValue()
     */
    protected String handleGetResetValue()
    {
        final ClassifierFacade type = getType();
        if (type != null)
        {
            final String name = getName();
            final String typeName = type.getFullyQualifiedName(true);

            if (isValidatorString(typeName)) return "\"" + name + "-test" + "\"";
            if (isValidatorDate(typeName)) return "new java.util.Date()";

            if (type.isPrimitive())
            {
                if (isValidatorInteger(typeName)) return "(int)" + name.hashCode();
                if (isValidatorBoolean(typeName)) return "false";
                if (isValidatorLong(typeName)) return "(long)" + name.hashCode();
                if (isValidatorChar(typeName)) return "(char)" + name.hashCode();
                if (isValidatorFloat(typeName)) return "(float)" + name.hashCode() / hashCode();
                if (isValidatorDouble(typeName)) return "(double)" + name.hashCode() / hashCode();
                if (isValidatorShort(typeName)) return "(short)" + name.hashCode();
                if (isValidatorByte(typeName)) return "(byte)" + name.hashCode();
            }
            else
            {
                if (isValidatorInteger(typeName)) return "new Integer((int)" + name.hashCode() + ")";
                if (isValidatorBoolean(typeName)) return "Boolean.FALSE";
                if (isValidatorLong(typeName)) return "new Long((long)" + name.hashCode() + ")";
                if (isValidatorChar(typeName)) return "new Character(char)" + name.hashCode() + ")";
                if (isValidatorFloat(typeName)) return "new Float((float)" + name.hashCode() / hashCode() + ")";
                if (isValidatorDouble(typeName)) return "new Double((double)" + name.hashCode() / hashCode() + ")";
                if (isValidatorShort(typeName)) return "new Short((short)" + name.hashCode() + ")";
                if (isValidatorByte(typeName)) return "new Byte((byte)" + name.hashCode() + ")";
            }

            if (type.isArrayType()) return constructArray();
            if (type.isSetType()) return "new java.util.HashSet(java.util.Arrays.asList(" + constructArray() + "))";
            if (type.isCollectionType()) return "java.util.Arrays.asList(" + constructArray() + ")";

            // maps and others types will simply not be treated
        }
        return "null";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        return getValidatorTypes().isEmpty() == false;
    }

    protected String getValidatorFormat()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return (format == null) ? null : format.trim();
    }

    protected java.util.Collection handleGetValidatorTypes()
    {
        final Collection validatorTypesList = new ArrayList();

        ClassifierFacade type = getType();
        if (type != null)
        {
            final String typeName = type.getFullyQualifiedName(true);
            final String format = getValidatorFormat();
            final boolean isRangeFormat = (format == null) ? false : isRangeFormat(format);

            if (isRequired()) validatorTypesList.add("required");

            if (isValidatorByte(typeName))
                validatorTypesList.add("byte");
            else if (isValidatorShort(typeName))
                validatorTypesList.add("short");
            else if (isValidatorInteger(typeName))
                validatorTypesList.add("integer");
            else if (isValidatorLong(typeName))
                validatorTypesList.add("long");
            else if (isValidatorFloat(typeName))
                validatorTypesList.add("float");
            else if (isValidatorDouble(typeName))
                validatorTypesList.add("double");
            else if (isValidatorDate(typeName))
                validatorTypesList.add("date");
            else if (isValidatorUrl(typeName))
                validatorTypesList.add("url");

            if (isRangeFormat)
            {
                if (isValidatorInteger(typeName)) validatorTypesList.add("intRange");
                if (isValidatorFloat(typeName)) validatorTypesList.add("floatRange");
                if (isValidatorDouble(typeName)) validatorTypesList.add("doubleRange");
            }

            if (format != null && isValidatorString(typeName))
            {
                if (isEmailFormat(format))
                    validatorTypesList.add("email");
                else if (isCreditCardFormat(format))
                    validatorTypesList.add("creditCard");
                else
                {
                    Collection formats = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (isMinLengthFormat(additionalFormat))
                            validatorTypesList.add("minlength");
                        else if (isMaxLengthFormat(additionalFormat))
                            validatorTypesList.add("maxlength");
                        else if (isPatternFormat(additionalFormat))
                            validatorTypesList.add("mask");
                    }
                }
            }

            if (getValidWhen() != null)
            {
                validatorTypesList.add("validwhen");
            }
        }

        // custom (paramterized) validators are allowed here
        Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());
            validatorTypesList.add(Bpm4StrutsUtils.parseValidatorName(validator));
        }

        return validatorTypesList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getValidatorMsgKey()
     */
    protected String handleGetValidatorMsgKey()
    {
        return getMessageKey();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getValidatorArgs(java.lang.String)
     */
    protected java.util.Collection handleGetValidatorArgs(java.lang.String validatorType)
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

        // custom (paramterized) validators are allowed here
        Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());
            if (validatorType.equals(Bpm4StrutsUtils.parseValidatorName(validator)))
            {
                args.addAll(Bpm4StrutsUtils.parseValidatorArgs(validator));
            }
        }

        return args;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getValidatorVars()
     */
    protected java.util.Collection handleGetValidatorVars()
    {
        final Map vars = new HashMap();

        ClassifierFacade type = getType();
        if (type != null)
        {
            final String typeName = type.getFullyQualifiedName(true);
            final String format = getValidatorFormat();
            if (format != null)
            {
                final boolean isRangeFormat = isRangeFormat(format);

                if (isRangeFormat && (isValidatorInteger(typeName) || isValidatorFloat(typeName) || isValidatorDouble(typeName)))
                {
                    vars.put("min",Arrays.asList(new Object[]{"min", getRangeStart(format)}));
                    vars.put("max",Arrays.asList(new Object[]{"max", getRangeEnd(format)}));
                }
                else if (isValidatorString(typeName))
                {
                    Collection formats = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (isMinLengthFormat(additionalFormat))
                            vars.put("minlength",Arrays.asList(new Object[]{"minlength", getMinLengthValue(additionalFormat)}));
                        else if (isMaxLengthFormat(additionalFormat))
                            vars.put("maxlenght",Arrays.asList(new Object[]{"maxlength", getMaxLengthValue(additionalFormat)}));
                        else if (isPatternFormat(additionalFormat))
                            vars.put("mask",Arrays.asList(new Object[]{"mask", getPatternValue(additionalFormat)}));
                    }
                }
            }
            if (isValidatorDate(typeName))
            {
                if (format != null && isStrictDateFormat(format))
                {
                    vars.put("datePatternStrict", Arrays.asList(new Object[]{"datePatternStrict", getDateFormat()}));
                }
                else
                {
                    vars.put("datePattern",Arrays.asList(new Object[]{"datePattern", getDateFormat()}));
                }
            }

            final String validWhen = getValidWhen();
            if (validWhen != null)
            {
                vars.put("test", Arrays.asList(new Object[]{"test", validWhen}));
            }
        }

        // custom (paramterized) validators are allowed here
        // in this case we will reuse the validator arg values
        Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());

            // guaranteed to be of the same length
            List validatorVars = Bpm4StrutsUtils.parseValidatorVars(validator);
            List validatorArgs = Bpm4StrutsUtils.parseValidatorArgs(validator);

            for (int i = 0; i < validatorVars.size(); i++)
            {
                String validatorVar = (String) validatorVars.get(i);
                String validatorArg = (String) validatorArgs.get(i);

                vars.put(validatorVar, Arrays.asList(new Object[]{validatorVar,validatorArg}));
            }
        }

        return vars.values();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getValidWhen()
     */
    protected java.lang.String handleGetValidWhen()
    {
        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_VALIDWHEN);
        return value == null ? null : '('+value.toString()+')';
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMultiboxPropertyName()
     */
    protected String handleGetMultiboxPropertyName()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_MULTIBOX);
        return (value == null) ? null : StringUtils.trimToNull(value.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getOptionKeys()
     */
    protected Collection handleGetOptionKeys()
    {
        final String key = getMessageKey() + '.';
        final Collection optionKeys = new ArrayList();
        final int optionCount = getOptionCount() + 1;
        for (int i = 1; i < optionCount; i++) optionKeys.add(key + i);
        return optionKeys;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getOptionValues()
     */
    protected Collection handleGetOptionValues()
    {
        Collection optionValues = new ArrayList();
        Object taggedValueObject = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_RADIO);

        if (taggedValueObject != null)
        {
            String taggedValue = String.valueOf(taggedValueObject).trim();

            int optionCount = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_OPTION_DEFAULT_COUNT;
            try
            {
                optionCount = Integer.parseInt(taggedValue);
                String name = getName();
                for (int i = 1; i <= optionCount; i++)
                {
                    optionValues.add(name + '-' + i);
                }
            }
            catch (Exception exception)
            {
                String[] options = taggedValue.replaceAll("[\\s]+", "").split("[,]");
                for (int i = 0; i < options.length; i++)
                {
                    optionValues.add(options[i].trim());
                }
                return optionValues;
            }
        }
        return optionValues;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getOptionCount()
     */
    protected int handleGetOptionCount()
    {
        return getOptionValues().size();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isShouldReset()
     */
    protected boolean handleIsShouldReset()
    {
        boolean shouldReset = false;
        Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_RESET);
        if (value != null)
        {
            shouldReset = Boolean.valueOf(StringUtils.trimToEmpty((String) value)).booleanValue();
        }
        return shouldReset;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetName()
     */
    protected String handleGetResetName()
    {
        return "reset" + StringUtils.capitalize(StringUtils.trimToEmpty(this.getName()));
    }

    protected boolean handleIsPassword()
    {
        return Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PASSWORD.equals(getWidgetType());
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
