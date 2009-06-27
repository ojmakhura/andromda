package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter
 */
public class StrutsParameterLogicImpl
    extends StrutsParameterLogic
{
    public StrutsParameterLogicImpl(java.lang.Object metaObject,
                                    java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetStrutsAction()
    {
        Object actionObject = null;

        final EventFacade event = getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof StrutsAction)
            {
                actionObject = transition;
            }
        }
        return actionObject;
    }

    protected String handleGetStyleId()
    {
        String styleId = null;

        final StrutsAction action = this.getStrutsAction();
        if (action != null)
        {
            // if this parameter's action links to a table we use a specific styleId
            if (action.isTableLink())
            {
                styleId = action.getTableLinkName() + StringUtilsHelper.upperCamelCaseName(getName());
            }
            else
            {
                final EventFacade trigger = action.getTrigger();
                if (trigger != null)
                {
                    styleId = StringUtilsHelper.lowerCamelCaseName(trigger.getName()) +
                        StringUtilsHelper.upperCamelCaseName(getName());
                }
            }
        }

        return styleId;
    }

    protected Object handleGetJsp()
    {
        Object jspObject = null;

        final EventFacade event = getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof StrutsAction)
            {
                final StrutsAction action = (StrutsAction)transition;
                jspObject = action.getInput();
            }
            else if (transition instanceof StrutsForward)
            {
                final StrutsForward forward = (StrutsForward)transition;
                if (forward.isEnteringPage())
                {
                    jspObject = forward.getTarget();
                }
            }
        }

        return jspObject;
    }

    protected List handleGetFormFields()
    {
        final List formFields;
        if (isControllerOperationArgument() && getName() != null)
        {
            final String name = getName();
            formFields = new ArrayList();
            Collection actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                StrutsAction action = (StrutsAction)actionIterator.next();
                Collection actionFormFields = action.getActionFormFields();
                for (final Iterator fieldIterator = actionFormFields.iterator(); fieldIterator.hasNext();)
                {
                    StrutsParameter parameter = (StrutsParameter)fieldIterator.next();
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getResetValue()()
     */
    protected java.lang.String handleGetNullValue()
    {
        String nullValue = null;

        final ClassifierFacade type = getType();
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
        final boolean resetRequired;

        if (isSelectable())
        {
            resetRequired = true;
        }
        else
        {
            final ClassifierFacade type = getType();
            if (type == null)
            {
                resetRequired = false;
            }
            else
            {
                resetRequired = (type.isArrayType() || type.isFileType()) ? true : this.isValidatorBoolean();
            }
        }
        return resetRequired;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getMessageKey()()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();

        if (!normalizeMessages())
        {
            if (isActionParameter())
            {
                final StrutsAction action = this.getStrutsAction();
                if (action != null)
                {
                    messageKey.append(action.getMessageKey());
                    messageKey.append('.');
                }
            }
            else
            {
                final StrutsJsp page = getJsp();
                if (page != null)
                {
                    messageKey.append(page.getMessageKey());
                    messageKey.append('.');
                }
            }
            messageKey.append("param.");
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(super.getName()));
        return messageKey.toString();
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
            dateSuffix = (isStrictDateFormat())
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
        return this.getMessageKey() + ".documentation";
    }

    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final String format = getValidatorFormat();
        final StringBuffer buffer = new StringBuffer();

        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        buffer.append((value == null) ? "No field documentation has been specified" : value);
        buffer.append(crlf);
        buffer.append(crlf);

        buffer.append(isRequired() ? "This field is required" : "This field is optional");
        buffer.append(crlf);

        if ("password".equals(getWidgetType()))
        {
            buffer.append("This is a password field, it will not show the data you enter, ")
                .append("each character will be masked using an asterisk");
            buffer.append(crlf);
        }

        if (isCreditCardFormat(format))
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://www.beachnet.com/~hstiles/cardtype.html\" target=\"_blank\">creditcard</a> ");
            buffer.append(crlf);
        }

        if (isDate())
        {
            String dateFormat = getDateFormat();
            buffer.append("This field represents a date and should be formatted in the matter described here")
                .append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html\" ")
                .append("target=\"_jdk\">");
            buffer.append(dateFormat).append("</a> ");

            if (isStrictDateFormat()) buffer
                .append("This format is strict in the sense that the parser will not use any heuristics in ")
                .append("order to guess the intended date in case the input would not perfectly match the format");
            else
            {
                buffer.append("This format is lenient in the sense that the parser will attempt to use heuristics in ")
                    .append("order to guess the intended date in case the input would not perfectly match the format");
            }
            buffer.append(crlf);
            buffer.append("A calendar has been foreseen to select a date from, it will automatically convert the date ")
                .append("to the appropriate format.");
            buffer.append(crlf);
        }

        if (this.isValidatorTime())
        {
            String dateFormat = getDateFormat();
            buffer
                .append("This field represents a time and should be formatted in the manner described here (for time) ")
                .append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html\" ")
                .append("target=\"_jdk\">");
            buffer.append(dateFormat).append("</a> ");
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
            buffer.append(
                "<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html\" target=\"_jdk\">");
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
            buffer.append("This field is only valid under specific conditions, more concretely the following ")
                .append("expression must evaluate true: ").append(validWhen);
            buffer.append(crlf);
        }

        if (isReadOnly())
        {
            buffer.append("The value of this field cannot be changed, it is read-only");
            buffer.append(crlf);
        }

        if (isValidatorBoolean())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">boolean</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorByte())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">byte</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorChar())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">character</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorDouble())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">double precision integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorFloat())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">floating point</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorInteger())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorLong())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">long integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorShort())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
                .append("target=\"_jdk\">short integer</a> value");
            buffer.append(crlf);
        }
        else if (isValidatorUrl())
        {
            buffer.append("The value of this field should reflect a ")
                .append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/net/URL.html\" ")
                .append("target=\"_jdk\">URL</a> value");
            buffer.append(crlf);
        }

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    protected boolean handleIsCalendarRequired()
    {
        return isDate() && String.valueOf(findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_CALENDAR)).equals("true");
    }

    /**
     * Overridden since StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndParameter#isActionParameter()
     */
    public boolean isActionParameter()
    {
        final StrutsAction action = getStrutsAction();
        return (action != null) && action.getActionParameters().contains(this);
    }

    protected String handleGetCollectionImplementationType()
    {
        String typeName = null;

        final ClassifierFacade type = this.getType();
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

    protected boolean handleIsTableDecoratorRequired()
    {
        boolean required = false;

        if (isTable())
        {
            final Object taggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_DECORATOR);
            if (taggedValue != null)
            {
                final String taggedValueString = String.valueOf(taggedValue);
                required = Boolean.valueOf(taggedValueString).booleanValue();
            }
            else
            {
                final Object property = getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_GENERATE_TABLE_DECORATORS);
                final String propertyString = String.valueOf(property);
                required = Boolean.valueOf(propertyString).booleanValue();
            }
        }

        return required;
    }
    
    /**
     * Override to not allow selectable parameters to be considered tables.
     * 
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameterLogic#isTable()
     */
    public boolean isTable()
    {
        return super.isTable() && !this.isSelectable() && !this.isHiddenField();
    }

    protected boolean handleIsAllGlobalTableActionsHaveSameParameter()
    {
        boolean sameParameter = true;

        String name = null;
        String type = null;

        final Collection actions = this.getTableGlobalActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext() && sameParameter;)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            final List parameters = action.getActionParameters();
            if (!parameters.isEmpty())
            {
                final StrutsParameter parameter = (StrutsParameter)parameters.iterator().next();
                if (name == null || type == null)
                {
                    name = parameter.getName();
                    type = parameter.getType().getFullyQualifiedName();
                }
                else
                {
                    sameParameter = name.equals(parameter.getName()) &&
                        type.equals(parameter.getType().getFullyQualifiedName());
                }
            }
        }

        return sameParameter;
    }

    protected List handleGetTableFormActions()
    {
        return this.internalGetTableActions(false, true, false);
    }

    protected List handleGetTableHyperlinkActions()
    {
        return this.internalGetTableActions(true, false, false);
    }

    protected Collection handleGetTableGlobalActions()
    {
        return this.internalGetTableActions(false, false, true);
    }

    protected Object handleGetTableGlobalActionParameter()
    {
        Object parameter = null;

        final Collection actions = this.getTableGlobalActions();
        if (!actions.isEmpty())
        {
            final List actionParameters = ((StrutsAction)actions.iterator().next()).getActionParameters();
            if (!actionParameters.isEmpty())
            {
                parameter = actionParameters.iterator().next();
            }
        }

        return parameter;
    }

    protected boolean handleIsTableFormActionSharingWidgets()
    {
        // @todo (wouter)
        return true;
    }

    /**
     * If this is a table this method returns all those actions that are declared to work
     * on this table.
     */
    private List internalGetTableActions(boolean hyperlink,
                                         boolean formPost,
                                         boolean tableAction)
    {
        final String name = StringUtils.trimToNull(getName());
        if (name == null || !isTable())
        {
            return Collections.EMPTY_LIST;
        }

        final StrutsJsp page = this.getJsp();

        final Collection tableActions = new LinkedHashSet();

        final Collection allUseCases = getModel().getAllUseCases();
        for (final Iterator useCaseIterator = allUseCases.iterator(); useCaseIterator.hasNext();)
        {
            final UseCaseFacade useCase = (UseCaseFacade)useCaseIterator.next();
            if (useCase instanceof StrutsUseCase)
            {
                final FrontEndActivityGraph graph = ((StrutsUseCase)useCase).getActivityGraph();
                if (graph != null)
                {
                    final Collection transitions = graph.getTransitions();
                    for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
                    {
                        final TransitionFacade transition = (TransitionFacade)transitionIterator.next();
                        if (transition.getSource().equals(page) && transition instanceof StrutsAction)
                        {
                            final StrutsAction action = (StrutsAction)transition;
                            if (action.isTableLink() && name.equals(action.getTableLinkName()))
                            {
                                if ((hyperlink && action.isHyperlink()) ||
                                    (formPost && action.isFormPost()) ||
                                    (tableAction && action.isTableAction()))
                                {
                                    tableActions.add(action);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList(tableActions);
    }

    protected String handleGetTableDecoratorFullyQualifiedName()
    {
        String name = getTableDecoratorPackageName();
        name = (StringUtils.trimToEmpty(name) == null) ? "" : name + '.';
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
        return Bpm4StrutsUtils.getDisplayTagExportTypes(
            this.findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_EXPORT),
            (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_TABLE_EXPORT_TYPES) );
    }

    protected boolean handleIsTableExportable()
    {
        return this.getTableExportTypes().indexOf("none") == -1;
    }

    protected boolean handleIsTableSortable()
    {
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
            ? Bpm4StrutsProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
            : Bpm4StrutsUtils.isTrue(String.valueOf(taggedValue));
    }

    protected boolean handleIsTableHyperlinkColumn()
    {
        boolean tableHyperlinkColumn = false;

        final String name = this.getName();
        if (name != null)
        {
            // this parameter's action must be a table hyperlink
            final StrutsAction action = this.getStrutsAction();
            if (action.isHyperlink() && action.isTableLink())
            {
                // get the table and check whether this parameter is part of that table's columns
                final StrutsParameter table = action.getTableLinkParameter();
                if (table != null)
                {
                    final Collection tableColumns = table.getTableColumns();
                    // if this parameter's name matches that targetted column name then we have found our column
                    tableHyperlinkColumn = tableColumns.contains(this) && name.equals(action.getTableLinkColumnName());
                }
            }
        }

        return tableHyperlinkColumn;
    }

    protected List handleGetTableColumnActions(final String columnName)
    {
        final List columnActions = new ArrayList();

        if (columnName != null)
        {
            // only hyperlinks can target table columns
            final List hyperlinkActions = this.getTableHyperlinkActions();
            for (int i = 0; i < hyperlinkActions.size(); i++)
            {
                final StrutsAction action = (StrutsAction)hyperlinkActions.get(i);
                if (columnName.equals(action.getTableLinkColumnName()))
                {
                    columnActions.add(action);
                }
            }
        }

        return columnActions;
    }

    /**
     * @return true if this parameter represents a table and is an array of custom types (no datatype)
     */
    private boolean isCustomArrayTable()
    {
        final ClassifierFacade type = this.getType();
        return type != null && this.isTable() && type.isArrayType() && !type.isDataType();
    }

    /**
     * Overridden since StrutsAction doesn't extend FrontEndAction.
     * 
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    public Collection getTableColumns()
    {
        // in this method we collect the elements that represent the columns of a table
        // if no specific element (parameter, attribute) can be found a simple String instance
        // is used
        // the event parameters have priority to be included in the collection because
        // they contain additional information such as validation constraint and widget type, ...

        // try to preserve the order of the elements encountered
        final Map tableColumnsMap = new LinkedHashMap();

        // order is important
        final List actions = new ArrayList();

        // all table actions need the exact same parameters, just not always all of them
        actions.addAll(this.getTableFormActions());
        // if there are any actions that are hyperlinks then their parameters get priority
        // the user should not have modeled it that way (constraints will warn him/her)
        actions.addAll(this.getTableHyperlinkActions());

        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final StrutsAction action = (StrutsAction)actionIterator.next();
            final Collection actionParameters = action.getActionParameters();
            for (final Iterator parameterIterator = actionParameters.iterator(); parameterIterator.hasNext();)
            {
                final StrutsParameter parameter = (StrutsParameter)parameterIterator.next();
                final String parameterName = parameter.getName();
                if (parameterName != null)
                {
                    // never overwrite column specific table links
                    // the hyperlink table links working on a real column get priority
                    final StrutsParameter existingParameter = (StrutsParameter)tableColumnsMap.get(parameterName);
                    if (existingParameter == null ||
                        (action.isHyperlink() && parameterName.equals(action.getTableLinkColumnName())))
                    {
                        tableColumnsMap.put(parameterName, parameter);
                    }
                }
            }
        }

        final Collection columnNames = this.getTableColumnNames();

        // in case of a custom array just add the attributes
        if (this.isCustomArrayTable())
        {
            final Collection attributes = this.getType().getNonArray().getAttributes(true);
            for (final Iterator attributeIterator = attributes.iterator(); attributeIterator.hasNext();)
            {
                final ModelElementFacade attribute = (ModelElementFacade)attributeIterator.next();
                // don't override
                if (!tableColumnsMap.containsKey(attribute.getName()))
                {
                    tableColumnsMap.put(attribute.getName(), attribute);
                }
            }
        }
        else
        {
            for (final Iterator columnNameIterator = columnNames.iterator(); columnNameIterator.hasNext();)
            {
                final String columnName = (String)columnNameIterator.next();
                // don't override
                if (!tableColumnsMap.containsKey(columnName))
                {
                    tableColumnsMap.put(columnName, columnName);
                }
            }
        }

        // return everything in the same order as it has been modeled (using the table tagged value)
        // note that only those columns mentioned in the tagged value are returned
        final Collection tableColumns = new ArrayList();
        for (final Iterator columnNameIterator = columnNames.iterator(); columnNameIterator.hasNext();)
        {
            final Object columnObject = columnNameIterator.next();
            tableColumns.add(tableColumnsMap.get(columnObject));
        }
        return tableColumns;
    }

    protected String handleGetTableColumnMessageKey(String columnName)
    {
        StringBuffer messageKey = null;

        if (isTable())
        {
            messageKey = new StringBuffer();

            if (!normalizeMessages())
            {
                final StrutsJsp page = getJsp();
                if (page != null)
                {
                    messageKey.append(getMessageKey());
                    messageKey.append('.');
                }
            }

            messageKey.append(StringUtilsHelper.toResourceMessageKey(columnName));
        }

        return (messageKey == null) ? null : messageKey.toString();
    }

    protected String handleGetTableColumnMessageValue(String columnName)
    {
        return (isTable()) ? StringUtilsHelper.toPhrase(columnName) : null;
    }

    protected int handleGetTableMaxRows()
    {
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_TABLE_MAXROWS);
        int pageSize;

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
                // no widget type has been specified
                final ClassifierFacade type = getType();
                if (type != null)
                {
                    if (type.isFileType()) widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_FILE;
                    else if (isValidatorBoolean()) widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX;
                    else if (isMultiple()) widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT;
                    else widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT;
                }
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT.equalsIgnoreCase(fieldType))
            {
                widgetType = "select";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PASSWORD.equalsIgnoreCase(fieldType))
            {
                widgetType = "password";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXTAREA.equalsIgnoreCase(fieldType))
            {
                widgetType = "textarea";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN.equalsIgnoreCase(fieldType))
            {
                widgetType = HIDDEN_INPUT_TYPE;
            }
            else if (fieldType.toLowerCase().startsWith(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_RADIO))
            {
                widgetType = "radio";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX.equalsIgnoreCase(fieldType))
            {
                widgetType = "checkbox";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PLAINTEXT.equalsIgnoreCase(fieldType))
            {
                widgetType = "plaintext";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT.equalsIgnoreCase(fieldType))
            {
                widgetType = "text";
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_MULTIBOX.equalsIgnoreCase(fieldType))
            {
                if (getMultiboxPropertyName() != null)
                {
                    widgetType = "multibox";
                }
            }
            else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_LINK.equalsIgnoreCase(fieldType))
            {
                final StrutsAction action = getStrutsAction();
                if (action != null)
                {
                    if (action.isTableLink())
                    {
                        widgetType = "link";
                    }
                }
            }
            else
            {
                widgetType = (isMultiple()) ? "select" : "text";
            }
        }
        return widgetType;
    }

    /**
     * The input type representing a 'hidden' parameter.
     */
    static final String HIDDEN_INPUT_TYPE = "hidden";

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
            final ClassifierFacade type = getType();

            if (!selectable && type != null)
            {
                final String name = getName();
                final String typeName = type.getFullyQualifiedName();

                /**
                 * if the parameter is not selectable but on a targetting page it _is_ selectable we must
                 * allow the user to set the backing list too
                 */
                final Collection pages = getStrutsAction().getTargetPages();
                for (final Iterator pageIterator = pages.iterator(); pageIterator.hasNext() && !selectable;)
                {
                    final StrutsJsp page = (StrutsJsp)pageIterator.next();
                    final Collection parameters = page.getAllActionParameters();
                    for (final Iterator parameterIterator = parameters.iterator();
                         parameterIterator.hasNext() && !selectable;)
                    {
                        final StrutsParameter parameter = (StrutsParameter)parameterIterator.next();
                        final String parameterName = parameter.getName();
                        final ClassifierFacade parameterType = parameter.getType();
                        if (parameterType != null)
                        {
                            final String parameterTypeName = parameterType.getFullyQualifiedName();
                            if (name.equals(parameterName) && typeName.equals(parameterTypeName))
                            {
                                selectable = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT
                                    .equals(parameter.getWidgetType());
                            }
                        }
                    }
                }
            }
        }
        else if (isControllerOperationArgument())
        {
            final String name = this.getName();
            final Collection actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                final StrutsAction action = (StrutsAction)actionIterator.next();
                final Collection formFields = action.getActionFormFields();
                for (final Iterator fieldIterator = formFields.iterator(); fieldIterator.hasNext() && !selectable;)
                {
                    final StrutsParameter parameter = (StrutsParameter)fieldIterator.next();
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

    /**
     * @return A String representing Java code for the initialization of an array using 5 elements.
     */
    private String constructArray()
    {
        final String name = getName();
        return "new Object[] {\"" +
            name +
            "-1\", \"" +
            name +
            "-2\", \"" +
            name +
            "-3\", \"" +
            name +
            "-4\", \"" +
            name +
            "-5\"}";
    }

    /**
     * Override normal parameter facade required implementation.
     *
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    public boolean isRequired()
    {
        final Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_REQUIRED);
        return Bpm4StrutsUtils.isTrue(value == null ? null : String.valueOf(value));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        final Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_READONLY);
        return Bpm4StrutsUtils.isTrue(value == null ? null : String.valueOf(value));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isDate()
     */
    protected boolean handleIsDate()
    {
        return this.isValidatorDate();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getDateFormat()
     */
    protected String handleGetDateFormat()
    {
        final String format = this.getValidatorFormat();
        return format == null ? this.getDefaultDateFormat() : this.getDateFormat(format);
    }

    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getTimeFormat()
     */
    protected String handleGetTimeFormat()
    {
        final String format = this.getValidatorFormat();
        return format == null ? this.getDefaultTimeFormat() : format;
    }

    /**
     * @return the default time format pattern as defined using the configured property
     */
    private String getDefaultTimeFormat()
    {
        return (String)this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        final String format = this.getValidatorFormat();
        return format == null 
            ? Bpm4StrutsUtils.isTrue((String)this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_STRICT_DATETIMEFORMAT))
            : this.isStrictDateFormat(format);
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

            if (isValidatorString()) return "\"" + name + "-test" + "\"";
            if (isValidatorDate()) return "new java.util.Date()";

            if (type.isPrimitive())
            {
                if (isValidatorInteger()) return "(int)" + name.hashCode();
                if (isValidatorBoolean()) return "false";
                if (isValidatorLong()) return "(long)" + name.hashCode();
                if (isValidatorChar()) return "(char)" + name.hashCode();
                if (isValidatorFloat()) return "(float)" + name.hashCode();
                if (isValidatorDouble()) return "(double)" + name.hashCode();
                if (isValidatorShort()) return "(short)" + name.hashCode();
                if (isValidatorByte()) return "(byte)" + name.hashCode();
            }
            else
            {
                if (isValidatorInteger()) return "new Integer((int)" + name.hashCode() + ")";
                if (isValidatorBoolean()) return "Boolean.FALSE";
                if (isValidatorLong()) return "new Long((long)" + name.hashCode() + ")";
                if (isValidatorChar()) return "new Character(char)" + name.hashCode() + ")";
                if (isValidatorFloat()) return "new Float((float)" + name.hashCode() + ")";
                if (isValidatorDouble()) return "new Double((double)" + name.hashCode() + ")";
                if (isValidatorShort()) return "new Short((short)" + name.hashCode() + ")";
                if (isValidatorByte()) return "new Byte((byte)" + name.hashCode() + ")";
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
        final String disableValidationForHiddenFormFields = (String)getConfiguredProperty(Bpm4StrutsGlobals.DISABLE_VALIDATION_FOR_HIDDEN_FORM_FIELDS);
        return !("true".equals(disableValidationForHiddenFormFields) && "hidden".equals(getWidgetType())) &&
            !getValidatorTypes().isEmpty();
    }

    private String getValidatorFormat()
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
            final String format = getValidatorFormat();
            final boolean isRangeFormat = (format != null) && isRangeFormat(format);

            if (isRequired()) validatorTypesList.add("required");

            if (isValidatorByte()) validatorTypesList.add("byte");
            else if (isValidatorShort()) validatorTypesList.add("short");
            else if (isValidatorInteger()) validatorTypesList.add("integer");
            else if (isValidatorLong()) validatorTypesList.add("long");
            else if (isValidatorFloat()) validatorTypesList.add("float");
            else if (isValidatorDouble()) validatorTypesList.add("double");
            else if (isValidatorDate()) validatorTypesList.add("date");
            else if (isValidatorTime()) validatorTypesList.add("time");
            else if (isValidatorUrl()) validatorTypesList.add("url");

            if (isRangeFormat)
            {
                if (isValidatorInteger() || isValidatorShort() || isValidatorLong()) validatorTypesList.add("intRange");
                if (isValidatorFloat()) validatorTypesList.add("floatRange");
                if (isValidatorDouble()) validatorTypesList.add("doubleRange");
            }

            if (format != null)
            {
                if (isValidatorString() && isEmailFormat(format)) validatorTypesList.add("email");
                else if (isValidatorString() && isCreditCardFormat(format)) validatorTypesList.add("creditCard");
                else
                {
                    Collection formats = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (isMinLengthFormat(additionalFormat)) validatorTypesList.add("minlength");
                        else if (isMaxLengthFormat(additionalFormat)) validatorTypesList.add("maxlength");
                        else if (isPatternFormat(additionalFormat)) validatorTypesList.add("mask");
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
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
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
        if ("intRange".equals(validatorType) ||
            "floatRange".equals(validatorType) ||
            "doubleRange".equals(validatorType))
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
        else if ("date".equals(validatorType))
        {
            final String validatorFormat = getValidatorFormat();
            if (validatorFormat != null && isStrictDateFormat(validatorFormat))
            {
                args.add("${var:datePatternStrict}");
            }
            else
            {
                args.add("${var:datePattern}");
            }
        }
        else if ("time".equals(validatorType))
        {
            args.add("${var:timePattern}");
        }

        // custom (paramterized) validators are allowed here
        Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
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
        final Map vars = new LinkedHashMap();

        final ClassifierFacade type = getType();
        if (type != null)
        {
            final String format = getValidatorFormat();
            if (format != null)
            {
                final boolean isRangeFormat = isRangeFormat(format);

                if (isRangeFormat)
                {
                    vars.put("min", Arrays.asList(new Object[]{"min", getRangeStart(format)}));
                    vars.put("max", Arrays.asList(new Object[]{"max", getRangeEnd(format)}));
                }
                else
                {
                    final Collection formats = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        final String additionalFormat = String.valueOf(formatIterator.next());
                        if (isMinLengthFormat(additionalFormat)) vars.put("minlength",
                            Arrays.asList(new Object[]{"minlength", this.getMinLengthValue(additionalFormat)}));
                        else if (isMaxLengthFormat(additionalFormat)) vars.put("maxlength",
                            Arrays.asList(new Object[]{"maxlength", this.getMaxLengthValue(additionalFormat)}));
                        else if (isPatternFormat(additionalFormat)) vars
                            .put("mask", Arrays.asList(new Object[]{"mask", this.getPatternValue(additionalFormat)}));
                    }
                }
            }
            if (isValidatorDate())
            {
                if (format != null && isStrictDateFormat(format))
                {
                    vars.put("datePatternStrict",
                        Arrays.asList(new Object[]{"datePatternStrict", this.getDateFormat()}));
                }
                else
                {
                    vars.put("datePattern", Arrays.asList(new Object[]{"datePattern", this.getDateFormat()}));
                }
            }
            if (this.isValidatorTime())
            {
                vars.put("timePattern", Arrays.asList(new Object[]{"timePattern", this.getTimeFormat()}));
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
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());

            // guaranteed to be of the same length
            List validatorVars = Bpm4StrutsUtils.parseValidatorVars(validator);
            List validatorArgs = Bpm4StrutsUtils.parseValidatorArgs(validator);

            for (int i = 0; i < validatorVars.size(); i++)
            {
                String validatorVar = (String)validatorVars.get(i);
                String validatorArg = (String)validatorArgs.get(i);

                vars.put(validatorVar, Arrays.asList(new Object[]{validatorVar, validatorArg}));
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
        return value == null ? null : '(' + value.toString() + ')';
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
    protected List handleGetOptionKeys()
    {
        final String key = getMessageKey() + '.';
        final List optionKeys = new ArrayList();
        final int optionCount = getOptionCount();
        for (int i = 0; i < optionCount; i++)
            optionKeys.add(key + i);
        return optionKeys;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getOptionValues()
     */
    protected List handleGetOptionValues()
    {
        final List optionValues = new ArrayList();
        final Object taggedValueObject = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_RADIO);

        if (taggedValueObject == null)
        {
            // we resort to the default values
            optionValues.add("0");
            optionValues.add("1");
            optionValues.add("2");
        }
        else
        {
            final String taggedValue = String.valueOf(taggedValueObject).trim();

            int optionCount;
            try
            {
                optionCount = Integer.parseInt(taggedValue);
                for (int i = 0; i < optionCount; i++)
                {
                    optionValues.add(String.valueOf(i));
                }
            }
            catch (Exception exception)
            {
                // this means the value wasn't a valid integer, we'll interpret it is a comma-separated
                // list of option-values
                final String[] options = taggedValue.split("[,]");
                for (int i = 0; i < options.length; i++)
                {
                    optionValues.add(options[i].trim());
                }
            }
        }
        return optionValues;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getOptionCount()
     */
    protected int handleGetOptionCount()
    {
        return this.getOptionValues().size();
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
            shouldReset = Boolean.valueOf(StringUtils.trimToEmpty((String)value)).booleanValue();
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
        return Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PASSWORD.equals(this.getWidgetType());
    }

    protected boolean handleIsCombobox()
    {
        return "select".equals(this.getWidgetType());
    }

    protected boolean handleIsCheckbox()
    {
        return "checkbox".equals(this.getWidgetType());
    }

    protected boolean handleIsTextField()
    {
        return "text".equals(this.getWidgetType());
    }

    protected boolean handleIsTextArea()
    {
        return "textarea".equals(this.getWidgetType());
    }

    protected boolean handleIsMultibox()
    {
        return "multibox".equals(this.getWidgetType());
    }

    protected boolean handleIsRadioButton()
    {
        return "radio".equals(this.getWidgetType());
    }

    protected boolean handleIsHiddenField()
    {
        return "hidden".equals(this.getWidgetType());
    }

    protected boolean handleIsHyperlink()
    {
        return "link".equals(this.getWidgetType());
    }

    /**
     * @return <code>true</code> if the type of this field is a boolean, <code>false</code> otherwise
     */
    private boolean isValidatorBoolean()
    {
        return UMLMetafacadeUtils.isType(this.getType(), UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a character, <code>false</code> otherwise
     */
    private boolean isValidatorChar()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.CHARACTER_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    private boolean isValidatorByte()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.BYTE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    private boolean isValidatorShort()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.SHORT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    private boolean isValidatorInteger()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.INTEGER_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    private boolean isValidatorLong()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.LONG_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    private boolean isValidatorFloat()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.FLOAT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a double precision floating point, <code>false</code> otherwise
     */
    private boolean isValidatorDouble()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.DOUBLE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a date, <code>false</code> otherwise
     */
    private boolean isValidatorDate()
    {
        return this.getType() != null && this.getType().isDateType();
    }

    /**
     * @return <code>true</code> if the type of this field is a time, <code>false</code> otherwise
     */
    private boolean isValidatorTime()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.TIME_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    private boolean isValidatorUrl()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.URL_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a String, <code>false</code> otherwise
     */
    private boolean isValidatorString()
    {
        return this.getType() != null && this.getType().isStringType();
    }

    /**
     * @return <code>true</code> if this field is to be formatted as an email address, <code>false</code> otherwise
     */
    private boolean isEmailFormat(String format)
    {
        return "email".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field is to be formatted as a credit card, <code>false</code> otherwise
     */
    private boolean isCreditCardFormat(String format)
    {
        return "creditcard".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    private boolean isRangeFormat(String format)
    {
        return "range".equalsIgnoreCase(getToken(format, 0, 2)) &&
            (isValidatorInteger() ||
                isValidatorLong() ||
                isValidatorShort() ||
                isValidatorFloat() ||
                isValidatorDouble());

    }

    /**
     * @return <code>true</code> if this field's value needs to respect a certain pattern, <code>false</code> otherwise
     */
    private boolean isPatternFormat(String format)
    {
        return "pattern".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to conform to a strict date format, <code>false</code> otherwise
     */
    private boolean isStrictDateFormat(String format)
    {
        return "strict".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to consist of at least a certain number of characters, <code>false</code> otherwise
     */
    private boolean isMinLengthFormat(String format)
    {
        return "minlength".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to consist of at maximum a certain number of characters, <code>false</code> otherwise
     */
    private boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return the lower limit for this field's value's range
     */
    private String getRangeStart(String format)
    {
        return getToken(format, 1, 3);
    }

    /**
     * @return the upper limit for this field's value's range
     */
    private String getRangeEnd(String format)
    {
        return getToken(format, 2, 3);
    }

    /**
     * @return this field's date format
     */
    private String getDateFormat(String format)
    {
        return (isStrictDateFormat(format)) ? getToken(format, 1, 2) : getToken(format, 0, 1);
    }

    /**
     * @return the minimum number of characters this field's value must consist of
     */
    private String getMinLengthValue(String format)
    {
        return getToken(format, 1, 2);
    }

    /**
     * @return the maximum number of characters this field's value must consist of
     */
    private String getMaxLengthValue(String format)
    {
        return getToken(format, 1, 2);
    }

    /**
     * @return the pattern this field's value must respect
     */
    private String getPatternValue(String format)
    {
        return '^' + getToken(format, 1, 2) + '$';
    }

    /**
     * @return the i-th space delimited token read from the argument String, where i does not exceed the specified limit
     */
    private String getToken(String string,
                            int index,
                            int limit)
    {
        if (string == null) return null;

        final String[] tokens = string.split("[\\s]+", limit);
        return (index >= tokens.length) ? null : tokens[index];
    }

    public Object getValidationOwner()
    {
        return (this.isTable() && this.getJsp() != null) ? this.getJsp() : super.getValidationOwner();
    }

    protected boolean handleIsSortableBy()
    {
        boolean sortableBy = true;

        final Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE);
        if (value != null)
        {
            final String fieldType = value.toString();
            sortableBy = !(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PASSWORD.equalsIgnoreCase(fieldType) ||
                Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN.equalsIgnoreCase(fieldType) ||
                Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_RADIO.equalsIgnoreCase(fieldType) ||
                Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX.equalsIgnoreCase(fieldType) ||
                Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_MULTIBOX.equalsIgnoreCase(fieldType));
        }

        return sortableBy;
    }

    private boolean normalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameterLogic#handleIsTime()
     */
    protected boolean handleIsTime()
    {
        return this.isValidatorTime();
    }

    protected Integer handleGetFieldColumnCount()
    {
        Integer columnCount = null;

        Object columnCountObject = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_COLUMN_COUNT);
        if (columnCountObject == null)
        {
            columnCountObject = this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_INPUT_COLUMN_COUNT);
        }

        if (columnCountObject != null)
        {
            try
            {
                columnCount = Integer.valueOf(columnCountObject.toString());
            }
            catch (NumberFormatException e)
            {
                // do nothing, we want columnCount to be null in case of an invalid value
            }
        }

        return columnCount;
    }

    protected Integer handleGetFieldRowCount()
    {
        Integer rowCount = null;

        Object rowCountObject = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_ROW_COUNT);
        if (rowCountObject == null)
        {
            rowCountObject = this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_INPUT_ROW_COUNT);
        }

        if (rowCountObject != null)
        {
            try
            {
                rowCount = Integer.valueOf(rowCountObject.toString());
            }
            catch (NumberFormatException e)
            {
                // do nothing, we want rowCount to be null in case of an invalid value
            }
        }

        return rowCount;
    }

    protected boolean handleIsSafeNamePresent()
    {
        return Bpm4StrutsUtils.isSafeName(this.getName());
    }
}
