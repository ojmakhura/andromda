package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFParameter.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFParameter
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();

        if (!this.isNormalizeMessages())
        {
            if (this.isActionParameter())
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getDocumentationValue(()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation(
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
        final String normalizeMessages = (String)getConfiguredProperty(JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableColumnMessageKey(String)
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableColumnMessageValue(String)
     */
    protected String handleGetTableColumnMessageValue(final String columnName)
    {
        return this.isTable() ? StringUtilsHelper.toPhrase(columnName) : null;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableHyperlinkActions()
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableFormActions()
     */
    protected List handleGetTableFormActions()
    {
        return this.getTableActions(false);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableColumns()
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
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getFormat()
     */
    protected String handleGetFormat()
    {
        String format = null;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            format = this.getInputFormat();
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
     * Retrieves the input format defined by the {@link JSFProfile#TAGGEDVALUE_INPUT_FORMAT}.
     *
     * @return the input format.
     */
    private final String getInputFormat()
    {
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        final String format = this.getInputFormat();
        return format != null && JSFUtils.isStrictDateFormat(format);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getDateFormatter()
     */
    protected String handleGetDateFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getName() + "DateFormatter" : null;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTimeFormatter()
     */
    protected String handleGetTimeFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isTimeType() ? this.getName() + "TimeFormatter" : null;
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType("textarea");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType("select");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType("password");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType("hidden");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType("plaintext");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType("radio");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType("text");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType("multibox");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputCheckbox()
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputFile()
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getBackingListName()
     */
    protected String handleGetBackingListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.BACKING_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.VALUE_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.LABEL_LIST_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isSelectable()
     */
    protected boolean handleIsSelectable()
    {
        boolean selectable = false;
        if (this.isActionParameter())
        {
            selectable = this.isInputMultibox() || this.isInputSelect() || this.isInputRadio();
            final ClassifierFacade type = this.getType();

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
                                selectable =
                                    parameter.isInputMultibox() || parameter.isInputSelect() ||
                                    parameter.isInputRadio();
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

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getDummyValue()
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
                    "new java.util.LinkedHashSet(java.util.Arrays.asList(" + this.constructDummyArray() +
                    "))";
            }
            else if (type.isCollectionType())
            {
                initialValue = "java.util.Arrays.asList(" + this.constructDummyArray() + ")";
            }
            else if (type.isArrayType())
            {
                initialValue = this.constructDummyArray();
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
     * @return A String representing Java code for the initialization of an array.
     */
    private final String constructDummyArray()
    {
        return JSFUtils.constructDummyArrayDeclaration(this.getName(), JSFGlobals.DUMMY_ARRAY_COUNT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableSortColumnProperty()
     */
    protected String handleGetTableSortColumnProperty()
    {
        return this.getName() + "SortColumn";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getTableSortAscendingProperty()
     */
    protected String handleGetTableSortAscendingProperty()
    {
        return this.getName() + "SortAscending";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getFormAttributeSetProperty()
     */
    protected String handleGetFormAttributeSetProperty()
    {
        return this.getName() + "Set";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        return !this.getValidatorTypes().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidatorTypes()
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
                    Collection formats = findTaggedValues(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
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
     * @see org.andromda.metafacades.uml.ParameterFacade#getValidWhen()
     */
    protected java.lang.String handleGetValidWhen()
    {
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_VALIDWHEN);
        return value == null ? null : '(' + value.toString() + ')';
    }

    /**
     * Overridden to have the same behavior as bpm4struts.
     *
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    public boolean isRequired()
    {
        final Object value = this.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_REQUIRED);
        return Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        final Object value = this.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_READONLY);
        return Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
    }

    /**
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    private boolean isByte()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.BYTE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    private boolean isShort()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.SHORT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    private boolean isInteger()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.INTEGER_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    private boolean isLong()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.LONG_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    private boolean isFloat()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.FLOAT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a double precision floating point, <code>false</code> otherwise
     */
    private boolean isDouble()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.DOUBLE_TYPE_NAME);
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
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.TIME_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    private boolean isUrl()
    {
        return UMLMetafacadeUtils.isType(
            this.getType(),
            JSFProfile.URL_TYPE_NAME);
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
        return "email".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @return <code>true</code> if this field is to be formatted as a credit card, <code>false</code> otherwise
     */
    private boolean isCreditCardFormat(final String format)
    {
        return "creditcard".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    private boolean isRangeFormat(final String format)
    {
        return "range".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2)) && (this.isInteger() || this.isLong() || this.isShort() || this.isFloat() || this.isDouble());
    }

    /**
     * @return <code>true</code> if this field's value needs to respect a certain pattern, <code>false</code> otherwise
     */
    private boolean isPatternFormat(final String format)
    {
        return "pattern".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @return <code>true</code> if this field's value needs to consist of at least a certain number of characters, <code>false</code> otherwise
     */
    private boolean isMinLengthFormat(final String format)
    {
        return "minlength".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @return <code>true</code> if this field's value needs to consist of at maximum a certain number of characters, <code>false</code> otherwise
     */
    private boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @return the lower limit for this field's value's range
     */
    private String getRangeStart(final String format)
    {
        return JSFUtils.getToken(
            format,
            1,
            3);
    }

    /**
     * @return the upper limit for this field's value's range
     */
    private String getRangeEnd(final String format)
    {
        return JSFUtils.getToken(
            format,
            2,
            3);
    }

    /**
     * @return the minimum number of characters this field's value must consist of
     */
    private String getMinLengthValue(final String format)
    {
        return JSFUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @return the maximum number of characters this field's value must consist of
     */
    private String getMaxLengthValue(final String format)
    {
        return JSFUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @return the pattern this field's value must respect
     */
    private String getPatternValue(final String format)
    {
        return '^' + JSFUtils.getToken(
            format,
            1,
            2) + '$';
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidatorArgs(java.lang.String)
     */
    protected java.util.Collection handleGetValidatorArgs(final java.lang.String validatorType)
    {
        final Collection args = new ArrayList();
        if ("intRange".equals(validatorType) || "floatRange".equals(validatorType) ||
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
            final String validatorFormat = this.getInputFormat();
            if (validatorFormat != null && JSFUtils.isStrictDateFormat(validatorFormat))
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
        Collection taggedValues = findTaggedValues(JSFProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String validator = String.valueOf(iterator.next());
            if (validatorType.equals(JSFUtils.parseValidatorName(validator)))
            {
                args.addAll(JSFUtils.parseValidatorArgs(validator));
            }
        }
        return args;
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
                final boolean isRangeFormat = this.isRangeFormat(format);

                if (isRangeFormat)
                {
                    final String min = "min";
                    final String max = "max";
                    vars.put(
                        min,
                        Arrays.asList(new Object[] {min, getRangeStart(format)}));
                    vars.put(
                        max,
                        Arrays.asList(new Object[] {max, getRangeEnd(format)}));
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
                        if (isMinLengthFormat(additionalFormat))
                        {
                            vars.put(
                                minlength,
                                Arrays.asList(new Object[] {minlength, this.getMinLengthValue(additionalFormat)}));
                        }
                        else if (isMaxLengthFormat(additionalFormat))
                        {
                            vars.put(
                                maxlength,
                                Arrays.asList(new Object[] {maxlength, this.getMaxLengthValue(additionalFormat)}));
                        }
                        else if (isPatternFormat(additionalFormat))
                        {
                            vars.put(
                                mask,
                                Arrays.asList(new Object[] {mask, this.getPatternValue(additionalFormat)}));
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isReset()
     */
    protected boolean handleIsReset()
    {
        boolean reset = Boolean.valueOf(
            ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_RESET))).booleanValue();
        if (!reset)
        {
            final JSFAction action = (JSFAction)this.getAction();
            reset = action != null && action.isFormReset();
        }
        return reset;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isComplex()
     */
    protected boolean handleIsComplex()
    {
        boolean complex = false;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            complex = !type.getAttributes().isEmpty();
            if (!complex)
            {
                complex = !type.getAssociationEnds().isEmpty();
            }
        }
        return complex;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getAttributes()
     */
    protected Collection handleGetAttributes()
    {
        Collection attributes = null;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            attributes = type.getAttributes();
        }
        return attributes == null ? Collections.EMPTY_LIST : attributes;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getNavigableAssociationEnds()
     */
    protected Collection handleGetNavigableAssociationEnds()
    {
        Collection associationEnds = null;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            associationEnds = type.getNavigableConnectingEnds();
        }
        return associationEnds == null ? Collections.EMPTY_LIST : associationEnds;
    }
}