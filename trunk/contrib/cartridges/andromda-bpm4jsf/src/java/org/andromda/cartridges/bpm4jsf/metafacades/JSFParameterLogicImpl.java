package org.andromda.cartridges.bpm4jsf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.cartridges.bpm4jsf.BPM4JSFProfile;
import org.andromda.cartridges.bpm4jsf.BPM4JSFUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter
 */
public class JSFParameterLogicImpl
    extends JSFParameterLogic
{
    public JSFParameterLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();

        if (!this.isNormalizeMessages())
        {
            if (isActionParameter())
            {
                final JSFAction action = (JSFAction)this.getAction();
                if (action != null)
                {
                    messageKey.append(action.getMessageKey());
                    messageKey.append('.');
                }
            }
            else
            {
                final JSFView view = (JSFView)this.getView();
                if (view != null)
                {
                    messageKey.append(view.getMessageKey());
                    messageKey.append('.');
                }
            }
            messageKey.append("param.");
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(super.getName()));
        return messageKey.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + BPM4JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDocumentationValue(()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(
                    "",
                    64,
                    false));
        return value == null ? "" : value;
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(BPM4JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableColumnMessageKey(String)
     */
    protected String handleGetTableColumnMessageKey(final String columnName)
    {
        StringBuffer messageKey = null;
        if (this.isTable())
        {
            messageKey = new StringBuffer();
            if (!this.isNormalizeMessages())
            {
                final JSFView view = (JSFView)this.getView();
                if (view != null)
                {
                    messageKey.append(this.getMessageKey());
                    messageKey.append('.');
                }
            }
            messageKey.append(StringUtilsHelper.toResourceMessageKey(columnName));
        }
        return messageKey == null ? null : messageKey.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableColumnMessageValue(String)
     */
    protected String handleGetTableColumnMessageValue(final String columnName)
    {
        return this.isTable() ? StringUtilsHelper.toPhrase(columnName) : null;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableHyperlinkActions()
     */
    protected List handleGetTableHyperlinkActions()
    {
        return this.getTableActions(true);
    }

    /**
     * If this is a table this method returns all those actions that are declared to work
     * on this table.
     *
     * @param hyperlink denotes on which type of actions to filter
     */
    private final List getTableActions(boolean hyperlink)
    {
        final Set actions = new LinkedHashSet();
        final String name = StringUtils.trimToNull(getName());
        if (name != null && isTable())
        {
            final JSFView view = (JSFView)this.getView();

            final Collection allUseCases = getModel().getAllUseCases();
            for (final Iterator useCaseIterator = allUseCases.iterator(); useCaseIterator.hasNext();)
            {
                final UseCaseFacade useCase = (UseCaseFacade)useCaseIterator.next();
                if (useCase instanceof JSFUseCase)
                {
                    final FrontEndActivityGraph graph = ((JSFUseCase)useCase).getActivityGraph();
                    if (graph != null)
                    {
                        final Collection transitions = graph.getTransitions();
                        for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
                        {
                            final TransitionFacade transition = (TransitionFacade)transitionIterator.next();
                            if (transition.getSource().equals(view) && transition instanceof JSFAction)
                            {
                                final JSFAction action = (JSFAction)transition;
                                if (action.isTableLink() && name.equals(action.getTableLinkName()))
                                {
                                    if (hyperlink == action.isHyperlink())
                                    {
                                        actions.add(action);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList(actions);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableFormActions()
     */
    protected List handleGetTableFormActions()
    {
        return this.getTableActions(false);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableColumns()
     */
    protected Collection handleGetTableColumns()
    {
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
            final JSFAction action = (JSFAction)actionIterator.next();
            final Collection actionParameters = action.getParameters();
            for (final Iterator parameterIterator = actionParameters.iterator(); parameterIterator.hasNext();)
            {
                final JSFParameter parameter = (JSFParameter)parameterIterator.next();
                final String parameterName = parameter.getName();
                if (parameterName != null)
                {
                    // never overwrite column specific table links
                    // the hyperlink table links working on a real column get priority
                    final JSFParameter existingParameter = (JSFParameter)tableColumnsMap.get(parameterName);
                    if (existingParameter == null ||
                        (action.isHyperlink() && parameterName.equals(action.getTableLinkColumnName())))
                    {
                        tableColumnsMap.put(
                            parameterName,
                            parameter);
                    }
                }
            }
        }

        // for any missing parameters we just add the name of the column
        final Collection columnNames = getTableColumnNames();
        for (final Iterator columnNameIterator = columnNames.iterator(); columnNameIterator.hasNext();)
        {
            final String columnName = (String)columnNameIterator.next();
            if (!tableColumnsMap.containsKey(columnName))
            {
                tableColumnsMap.put(
                    columnName,
                    columnName);
            }
        }

        // return everything in the same order as it has been modeled (using the table tagged value)
        final Collection tableColumns = new ArrayList();
        for (final Iterator columnNameIterator = columnNames.iterator(); columnNameIterator.hasNext();)
        {
            final Object columnObject = columnNameIterator.next();
            tableColumns.add(tableColumnsMap.get(columnObject));
        }

        return tableColumns;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDateFormat()
     */
    protected String handleGetDateFormat()
    {
        final String format = this.getInputFormat();
        return format == null ? this.getDefaultDateFormat() : this.getDateFormat(format);
    }

    /**
     * @return this field's date format
     */
    private String getDateFormat(String format)
    {
        return this.isStrictDateFormat(format) ? getToken(
            format,
            1,
            2) : this.getToken(
            format,
            0,
            1);
    }

    /**
     * @return <code>true</code> if this field's value needs to conform to a strict date format, <code>false</code> otherwise
     */
    private boolean isStrictDateFormat(String format)
    {
        return "strict".equalsIgnoreCase(getToken(
                format,
                0,
                2));
    }

    /**
     * @return the i-th space delimited token read from the argument String, where i does not exceed the specified limit
     */
    private String getToken(
        String string,
        int index,
        int limit)
    {
        String token = null;
        if (string != null && string.length() > 0)
        {
            final String[] tokens = string.split(
                    "[\\s]+",
                    limit);
            token = index >= tokens.length ? null : tokens[index];
        }
        return token;
    }

    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(BPM4JSFGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTimeFormat()
     */
    protected String handleGetTimeFormat()
    {
        final String format = this.getInputFormat();
        return format == null ? this.getDefaultTimeFormat() : format;
    }

    /**
     * @return the default time format pattern as defined using the configured property
     */
    private String getDefaultTimeFormat()
    {
        return (String)this.getConfiguredProperty(BPM4JSFGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    /**
     * Retrieves the input format defined by the {@link BPM4JSFProfile#TAGGEDVALUE_INPUT_FORMAT}.
     *
     * @return the input format.
     */
    private final String getInputFormat()
    {
        final Object value = findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        final String format = this.getInputFormat();
        return format != null && this.isStrictDateFormat(format);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDateFormatter()
     */
    protected String handleGetDateFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getName() + "DateFormatter" : null;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTimeFormatter()
     */
    protected String handleGetTimeFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isTimeType() ? this.getName() + "TimeFormatter" : null;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputCheckbox()
     */
    protected boolean handleIsInputCheckbox()
    {
        boolean checkbox = this.isInputType("checkbox");
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
        return ObjectUtils.toString(this.findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_INPUT_TYPE)).trim();
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
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType("textarea");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType("select");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType("password");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType("hidden");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType("plaintext");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType("radio");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType("text");
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getBackingListName()
     */
    protected String handleGetBackingListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.BACKING_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.VALUE_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.LABEL_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isSelectable()
     */
    protected boolean handleIsSelectable()
    {
        boolean selectable = false;

        if (this.isActionParameter())
        {
            selectable = this.isInputSelect();
            final ClassifierFacade type = getType();

            if (!selectable && type != null)
            {
                final String name = this.getName();
                final String typeName = type.getFullyQualifiedName();

                // - if the parameter is not selectable but on a targetting page it IS selectable we must
                //   allow the user to set the backing list too                 
                final Collection views = this.getAction().getTargetViews();
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
                                selectable = parameter.isInputSelect();
                            }
                        }
                    }
                }
            }
        }
        else if (this.isControllerOperationArgument())
        {
            final String name = this.getName();
            final Collection actions = this.getControllerOperation().getDeferringActions();
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
        return selectable;
    }

    /**
     * Stores the initial value of each type.
     */
    private final Map initialValues = new HashMap();
    private final int arrayCount = 5;

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray(arrayCount);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDummyValue()
     */
    protected String handleGetDummyValue()
    {
        final ClassifierFacade type = this.getType();
        final String typeName = type != null ? type.getFullyQualifiedName() : "";
        String initialValue = null;
        if (type != null)
        {
            if (type.isSetType())
            {
                initialValue =
                    "new java.util.LinkedHashSet(java.util.Arrays.asList(" + this.constructDummyArray(arrayCount) +
                    "))";
            }
            else if (type.isCollectionType())
            {
                initialValue = "java.util.Arrays.asList(" + this.constructDummyArray(arrayCount) + ")";
            }
            else if (type.isArrayType())
            {
                initialValue = this.constructDummyArray(arrayCount);
            }
            final String name = this.getName() != null ? this.getName() : "";
            if (this.initialValues.isEmpty())
            {
                initialValues.put(
                    boolean.class.getName(),
                    "false");
                initialValues.put(
                    int.class.getName(),
                    "(int)" + name.hashCode());
                initialValues.put(
                    long.class.getName(),
                    "(long)" + name.hashCode());
                initialValues.put(
                    short.class.getName(),
                    "(short)" + name.hashCode());
                initialValues.put(
                    byte.class.getName(),
                    "(byte)" + name.hashCode());
                initialValues.put(
                    float.class.getName(),
                    "(float)" + name.hashCode());
                initialValues.put(
                    double.class.getName(),
                    "(double)" + name.hashCode());
                initialValues.put(
                    char.class.getName(),
                    "(char)" + name.hashCode());

                initialValues.put(
                    String.class.getName(),
                    "\"" + name + "-test" + "\"");
                initialValues.put(
                    java.util.Date.class.getName(),
                    "new java.util.Date()");
                initialValues.put(
                    java.sql.Date.class.getName(),
                    "new java.util.Date()");
                initialValues.put(
                    java.sql.Timestamp.class.getName(),
                    "new java.util.Date()");

                initialValues.put(
                    Integer.class.getName(),
                    "new Integer((int)" + name.hashCode() + ")");
                initialValues.put(
                    Boolean.class.getName(),
                    "Boolean.FALSE");
                initialValues.put(
                    Long.class.getName(),
                    "new Long((long)" + name.hashCode() + ")");
                initialValues.put(
                    Character.class.getName(),
                    "new Character(char)" + name.hashCode() + ")");
                initialValues.put(
                    Float.class.getName(),
                    "new Float((float)" + name.hashCode() / hashCode() + ")");
                initialValues.put(
                    Double.class.getName(),
                    "new Double((double)" + name.hashCode() / hashCode() + ")");
                initialValues.put(
                    Short.class.getName(),
                    "new Short((short)" + name.hashCode() + ")");
                initialValues.put(
                    Byte.class.getName(),
                    "new Byte((byte)" + name.hashCode() + ")");
            }
            if (initialValue == null)
            {
                initialValue = (String)this.initialValues.get(typeName);
            }
        }
        if (initialValue == null)
        {
            initialValue = "null";
        }
        return initialValue;
    }

    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @return A String representing Java code for the initialization of an array using 5 elements.
     */
    private final String constructDummyArray(final int count)
    {
        final StringBuffer array = new StringBuffer("new Object[] {");
        final String name = this.getName();
        for (int ctr = 1; ctr <= count; ctr++)
        {
            array.append("\"" + name + "-" + ctr + "\"");
            if (ctr != count)
            {
                array.append(", ");
            }
        }
        array.append("}");
        return array.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableSortColumnProperty()
     */
    protected String handleGetTableSortColumnProperty()
    {
        return this.getName() + "SortColumn";
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableSortAscendingProperty()
     */
    protected String handleGetTableSortAscendingProperty()
    {
        return this.getName() + "SortAscending";
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getFormAttributeSetProperty()
     */
    protected String handleGetFormAttributeSetProperty()
    {
        return this.getName() + "Set";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        return !this.getValidatorTypes().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsParameter#getValidatorTypes()
     */
    protected java.util.Collection handleGetValidatorTypes()
    {
        final Collection validatorTypesList = new ArrayList();
        final ClassifierFacade type = getType();
        if (type != null)
        {
            final String format = this.getInputFormat();
            final boolean isRangeFormat = format != null && isRangeFormat(format);
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
                if (this.isString() && this.isEmailFormat(format))
                {
                    validatorTypesList.add("email");
                }
                else if (this.isString() && this.isCreditCardFormat(format))
                {
                    validatorTypesList.add("creditCard");
                }
                else
                {
                    Collection formats = findTaggedValues(BPM4JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (isMinLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add("minlength");
                        }
                        else if (isMaxLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add("maxlength");
                        }
                        else if (isPatternFormat(additionalFormat))
                        {
                            validatorTypesList.add("mask");
                        }
                    }
                }
            }

            if (getValidWhen() != null)
            {
                validatorTypesList.add("validwhen");
            }
        }

        // - custom (paramterized) validators are allowed here
        final Collection taggedValues = findTaggedValues(BPM4JSFProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());
            validatorTypesList.add(BPM4JSFUtils.parseValidatorName(validator));
        }
        return validatorTypesList;
    }
    
    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getValidWhen()
     */
    protected java.lang.String handleGetValidWhen()
    {
        final Object value = findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_INPUT_VALIDWHEN);
        return value == null ? null : '(' + value.toString() + ')';
    }

    /**
     * Overridden to have the same behavior as bpm4struts.
     *
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    public boolean isRequired()
    {
        final Object value = this.findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_INPUT_REQUIRED);
        return Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        final Object value = this.findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_INPUT_READONLY);
        return Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
    }
    
    /**
     * @return <code>true</code> if the type of this field is a boolean, <code>false</code> otherwise
     */
    private boolean isBoolean()
    {
        return UMLMetafacadeUtils.isType(this.getType(), UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a character, <code>false</code> otherwise
     */
    private boolean isChar()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.CHARACTER_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    private boolean isByte()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.BYTE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    private boolean isShort()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.SHORT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    private boolean isInteger()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.INTEGER_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    private boolean isLong()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.LONG_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    private boolean isFloat()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.FLOAT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a double precision floating point, <code>false</code> otherwise
     */
    private boolean isDouble()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.DOUBLE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a date, <code>false</code> otherwise
     */
    private boolean isDate()
    {
        return this.getType() != null && this.getType().isDateType();
    }

    /**
     * @return <code>true</code> if the type of this field is a time, <code>false</code> otherwise
     */
    private boolean isTime()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.TIME_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    private boolean isUrl()
    {
        return UMLMetafacadeUtils.isType(this.getType(), BPM4JSFProfile.URL_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a String, <code>false</code> otherwise
     */
    private boolean isString()
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
    private boolean isCreditCardFormat(final String format)
    {
        return "creditcard".equalsIgnoreCase(getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    private boolean isRangeFormat(final String format)
    {
        return "range".equalsIgnoreCase(this.getToken(format, 0, 2)) &&
            (this.isInteger() ||
                this.isLong() ||
                this.isShort() ||
                this.isFloat() ||
                this.isDouble());

    }

    /**
     * @return <code>true</code> if this field's value needs to respect a certain pattern, <code>false</code> otherwise
     */
    private boolean isPatternFormat(final String format)
    {
        return "pattern".equalsIgnoreCase(this.getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to consist of at least a certain number of characters, <code>false</code> otherwise
     */
    private boolean isMinLengthFormat(final String format)
    {
        return "minlength".equalsIgnoreCase(this.getToken(format, 0, 2));
    }

    /**
     * @return <code>true</code> if this field's value needs to consist of at maximum a certain number of characters, <code>false</code> otherwise
     */
    private boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(this.getToken(format, 0, 2));
    }

    /**
     * @return the lower limit for this field's value's range
     */
    private String getRangeStart(final String format)
    {
        return this.getToken(format, 1, 3);
    }

    /**
     * @return the upper limit for this field's value's range
     */
    private String getRangeEnd(final String format)
    {
        return this.getToken(format, 2, 3);
    }

    /**
     * @return the minimum number of characters this field's value must consist of
     */
    private String getMinLengthValue(final String format)
    {
        return this.getToken(format, 1, 2);
    }

    /**
     * @return the maximum number of characters this field's value must consist of
     */
    private String getMaxLengthValue(final String format)
    {
        return this.getToken(format, 1, 2);
    }

    /**
     * @return the pattern this field's value must respect
     */
    private String getPatternValue(final String format)
    {
        return '^' + this.getToken(format, 1, 2) + '$';
    }
}