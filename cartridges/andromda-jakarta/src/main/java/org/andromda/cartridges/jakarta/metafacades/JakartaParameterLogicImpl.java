package org.andromda.cartridges.jakarta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.cartridges.jakarta.JakartaProfile;
import org.andromda.cartridges.jakarta.JakartaUtils;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaParameter.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter
 */
public class JakartaParameterLogicImpl
    extends JakartaParameterLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaParameterLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to make sure it's not an inputTable.
     *
     * @see org.andromda.metafacades.uml.FrontEndParameter#isTable()
     */
    public boolean isTable()
    {
        return (super.isTable() || this.isPageableTable()) && !this.isSelectable()
            && !this.isInputTable() && !this.isInputHidden();
    }

    /**
     * @return isPageableTable
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isPageableTable()
     */
    protected boolean handleIsPageableTable()
    {
        final Object value = this.findTaggedValue(JakartaProfile.TAGGEDVALUE_TABLE_PAGEABLE);
        return Boolean.valueOf(Objects.toString(value, "")).booleanValue();
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        final StringBuilder messageKey = new StringBuilder();

        if (!this.isNormalizeMessages())
        {
            if (this.isActionParameter())
            {
                final JakartaAction action = (JakartaAction)this.getAction();
                if (action != null)
                {
                    messageKey.append(action.getMessageKey());
                    messageKey.append('.');
                }
            }
            else
            {
                final JakartaView view = (JakartaView)this.getView();
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
     * @return getMessageKey() + '.' + JakartaGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + JakartaGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return documentationValue
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getDocumentationValue()
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
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(JakartaGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @return messageValue
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

    /**
     * @param columnName
     * @return tableColumnMessageKey
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableColumnMessageKey(String)
     */
    protected String handleGetTableColumnMessageKey(final String columnName)
    {
        StringBuilder messageKey = new StringBuilder();
        if (!this.isNormalizeMessages())
        {
            final JakartaView view = (JakartaView)this.getView();
            if (view != null)
            {
                messageKey.append(this.getMessageKey());
                messageKey.append('.');
            }
        }
        messageKey.append(StringUtilsHelper.toResourceMessageKey(columnName));
        return messageKey.toString();
    }

    /**
     * @param columnName
     * @return StringUtilsHelper.toPhrase(columnName)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableColumnMessageValue(String)
     */
    protected String handleGetTableColumnMessageValue(final String columnName)
    {
        return StringUtilsHelper.toPhrase(columnName);
    }

    /**
     * @return getTableActions(true)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableHyperlinkActions()
     */
    protected List<JakartaAction> handleGetTableHyperlinkActions()
    {
        return this.getTableActions(true);
    }

    private class ActionFilter implements Predicate
    {
        final private boolean hyperlink;
        public ActionFilter(boolean hyperlink)
        {
            this.hyperlink = hyperlink;
        }
        
        @Override
        public boolean evaluate(Object action) 
        {
            return ((JakartaAction)action).isHyperlink() == this.hyperlink;
        }
    }
    
    /**
     * If this is a table this method returns all those actions that are declared to work
     * on this table.
     *
     * @param hyperlink denotes on which type of actions to filter
     */
    private List<JakartaAction> getTableActions(boolean hyperlink)
    {
        final List<JakartaAction> actions = new ArrayList<JakartaAction>(super.getTableActions());
        CollectionUtils.filter(actions, new ActionFilter(hyperlink));
        return actions;
    }

    /**
     * @return getTableActions(false)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableFormActions()
     */
    protected List<JakartaAction> handleGetTableFormActions()
    {
        return this.getTableActions(false);
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableActions()
     */
    protected List<JakartaAction> handleGetTableActions() {
        final Set<JakartaAction> actions = new LinkedHashSet<JakartaAction>();
        final String name = StringUtils.trimToNull(getName());
        if (name != null && isTable())
        {
            final JakartaView view = (JakartaView)this.getView();

            final Collection<UseCaseFacade> allUseCases = getModel().getAllUseCases();
            for (final UseCaseFacade useCase : allUseCases)
            {
                if (useCase instanceof JakartaUseCase)
                {
                    final FrontEndActivityGraph graph = ((JakartaUseCase)useCase).getActivityGraph();
                    if (graph != null)
                    {
                        final Collection<TransitionFacade> transitions = graph.getTransitions();
                        for (final TransitionFacade transition : transitions)
                        {
                            if (transition.getSource().equals(view) && transition instanceof JakartaAction)
                            {
                                final JakartaAction action = (JakartaAction)transition;
                                if (action.isTableLink() && name.equals(action.getTableLinkName()))
                                {
                                    actions.add(action);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<JakartaAction>(actions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    // TODO tableColumns can be either String or JakartaParameter. Should use a single return type in Collection.
    public Collection getTableColumns()
    {
        final Collection tableColumns = super.getTableColumns();
        if (tableColumns.isEmpty())
        {
            // try to preserve the order of the elements encountered
            //final Map<String, JakartaParameter> tableColumnsMap = new LinkedHashMap<String, JakartaParameter>();
            final Map tableColumnsMap = new LinkedHashMap();

            // order is important
            final List<JakartaAction> actions = new ArrayList<JakartaAction>();

            // all table actions need the exact same parameters, just not always all of them
            actions.addAll(this.getTableFormActions());

            // if there are any actions that are hyperlinks then their parameters get priority
            // the user should not have modeled it that way (constraints will warn him/her)
            actions.addAll(this.getTableHyperlinkActions());

            for (final JakartaAction action : actions)
            {
                for (final FrontEndParameter actionParameter : action.getParameters())
                {
                    if (actionParameter instanceof JakartaParameter)
                    {
                        final JakartaParameter parameter = (JakartaParameter)actionParameter;
                        final String parameterName = parameter.getName();
                        if (parameterName != null)
                        {
                            // never overwrite column specific table links
                            // the hyperlink table links working on a real column get priority
                            final Object existingObject = tableColumnsMap.get(parameterName);
                            if (existingObject instanceof JakartaParameter)
                            {
                                if (action.isHyperlink() && parameterName.equals(action.getTableLinkColumnName()))
                                {
                                    tableColumnsMap.put(
                                        parameterName,
                                        parameter);
                                }
                            }
                        }
                    }
                }
            }

            // for any missing parameters we just add the name of the column
            for (final String columnName : this.getTableColumnNames())
            {
                if (!tableColumnsMap.containsKey(columnName))
                {
                    tableColumnsMap.put(
                        columnName,
                        columnName);
                }
            }

            // return everything in the same order as it has been modeled (using the table tagged value)
            for (final String columnObject : this.getTableColumnNames())
            {
                tableColumns.add(tableColumnsMap.get(columnObject));
            }
        }
        return tableColumns;
    }

    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(JakartaGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @return format
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getFormat()
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
     * @return JakartaUtils.isStrictDateFormat((ModelElementFacade)this.THIS())
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        return JakartaUtils.isStrictDateFormat((ModelElementFacade)this.THIS());
    }

    /**
     * @return dateFormatter
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getDateFormatter()
     */
    protected String handleGetDateFormatter()
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getName() + "DateFormatter" : null;
    }

    /**
     * @return timeFormatter
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTimeFormatter()
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
    private String getInputType()
    {
        return Objects.toString(this.findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_TYPE)).trim();
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
     * @return isInputType(JakartaGlobals.INPUT_TEXTAREA)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputTextarea()
     */
    protected boolean handleIsInputTextarea()
    {
        return this.isInputType(JakartaGlobals.INPUT_TEXTAREA);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_SELECT)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType(JakartaGlobals.INPUT_SELECT);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_PASSWORD)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType(JakartaGlobals.INPUT_PASSWORD);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_HIDDEN)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType(JakartaGlobals.INPUT_HIDDEN);
    }

    /**
     * @return isInputType(JakartaGlobals.PLAIN_TEXT)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType(JakartaGlobals.PLAIN_TEXT);
    }

    /**
     * @return isInputTable
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputTable()
     */
    protected boolean handleIsInputTable()
    {
        return this.getInputTableIdentifierColumns().length() > 0 || this.isInputType(JakartaGlobals.INPUT_TABLE);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_RADIO)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType(JakartaGlobals.INPUT_RADIO);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_TEXT)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType(JakartaGlobals.INPUT_TEXT);
    }

    /**
     * @return isInputType(JakartaGlobals.INPUT_MULTIBOX)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType(JakartaGlobals.INPUT_MULTIBOX);
    }

    /**
     * @return isInputCheckbox
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputCheckbox()
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
     * @return isInputFile
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isInputFile()
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
     * @return backingListName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getBackingListName()
     */
    protected String handleGetBackingListName()
    {
        return Objects.toString(this.getConfiguredProperty(JakartaGlobals.BACKING_LIST_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return backingValueName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getBackingValueName()
     */
    protected String handleGetBackingValueName()
    {
        return Objects.toString(this.getConfiguredProperty(JakartaGlobals.BACKING_VALUE_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return valueListName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getValueListName()
     */
    protected String handleGetValueListName()
    {
        return Objects.toString(this.getConfiguredProperty(JakartaGlobals.VALUE_LIST_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return labelListName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getLabelListName()
     */
    protected String handleGetLabelListName()
    {
        return Objects.toString(this.getConfiguredProperty(JakartaGlobals.LABEL_LIST_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return isSelectable
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isSelectable()
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
                final Collection<FrontEndView> views = this.getAction().getTargetViews();
                for (final Iterator<FrontEndView> iterator = views.iterator(); iterator.hasNext() && !selectable;)
                {
                    final Collection<FrontEndParameter> parameters = iterator.next().getAllActionParameters();
                    for (final Iterator<FrontEndParameter> parameterIterator = parameters.iterator();
                        parameterIterator.hasNext() && !selectable;)
                    {
                        final Object object = parameterIterator.next();
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
        else if (this.isControllerOperationArgument())
        {
            final String name = this.getName();
            final Collection actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                final JakartaAction action = (JakartaAction)actionIterator.next();
                final Collection<FrontEndParameter>  formFields = action.getFormFields();
                for (final Iterator<FrontEndParameter>  fieldIterator = formFields.iterator();
                    fieldIterator.hasNext() && !selectable;)
                {
                    final Object object = fieldIterator.next();
                    if (object instanceof JakartaParameter)
                    {
                        final JakartaParameter parameter = (JakartaParameter)object;
                        if (!parameter.equals(this))
                        {
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
     * Stores the initial value of each type.
     */
    private final Map<String, String> initialValues = new HashMap<String, String>();

    /**
     * @return constructDummyArray()
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getValueListDummyValue()
     */
    protected String handleGetValueListDummyValue()
    {
        return this.constructDummyArray();
    }

    /**
     * @return dummyValue
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getDummyValue()
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
                    "new java.util.LinkedHashSet(java.util.Arrays.asList(" + this.constructDummyArray() + "))";
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
                initialValue = this.initialValues.get(typeName);
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
    private String constructDummyArray()
    {
        return JakartaUtils.constructDummyArrayDeclaration(
            this.getName(),
            JakartaGlobals.DUMMY_ARRAY_COUNT);
    }

    /**
     * @return getName() + "SortColumn"
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableSortColumnProperty()
     */
    protected String handleGetTableSortColumnProperty()
    {
        return this.getName() + "SortColumn";
    }

    /**
     * @return getName() + "SortAscending"
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableSortAscendingProperty()
     */
    protected String handleGetTableSortAscendingProperty()
    {
        return this.getName() + "SortAscending";
    }

    /**
     * @return getName() + "Set"
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getFormAttributeSetProperty()
     */
    protected String handleGetFormAttributeSetProperty()
    {
        return this.getName() + "Set";
    }

    //TODO remove after 3.4 release
    /**
     * Hack to keep the compatibility with Andromda 3.4-SNAPSHOT
     */
    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getView()
     */
    public FrontEndView getView()
    {
        Object view = null;
        final EventFacade event = this.getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof JakartaActionLogicImpl)
            {
                final JakartaActionLogicImpl action = (JakartaActionLogicImpl)transition;
                view = action.getInput();
            }
            else if (transition instanceof FrontEndForward)
            {
                final FrontEndForward forward = (FrontEndForward)transition;
                if (forward.isEnteringView())
                {
                    view = forward.getTarget();
                }
            }
        }
        return (FrontEndView)view;
    }

    /**
     * @return validationRequired
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = !this.getValidatorTypes().isEmpty();
        if (!required)
        {
            // - look for any attributes
            for (final Iterator<JakartaAttribute> iterator = this.getAttributes().iterator(); iterator.hasNext();)
            {
                required = !iterator.next().getValidatorTypes().isEmpty();
                if (required)
                {
                    break;
                }
            }

            // - look for any table columns
            if (!required)
            {
                for (final Iterator iterator = this.getTableColumns().iterator(); iterator.hasNext();)
                {
                    final Object object = iterator.next();
                    if (object instanceof JakartaAttribute)
                    {
                        final JakartaAttribute attribute = (JakartaAttribute)object;
                        required = !attribute.getValidatorTypes().isEmpty();
                        if (required)
                        {
                            break;
                        }
                    }
                }
            }
        }
        return required;
    }

    /**
     * @return validatorTypes
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getValidatorTypes()
     */
    protected Collection handleGetValidatorTypes()
    {
        return JakartaUtils.getValidatorTypes(
            (ModelElementFacade)this.THIS(),
            this.getType());
    }

    /**
     * @return JakartaUtils.getValidWhen(this)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getValidWhen()
     */
    protected String handleGetValidWhen()
    {
        return JakartaUtils.getValidWhen(this);
    }

    /**
     * Overridden to have the same behavior as bpm4struts.
     *
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    public boolean isRequired()
    {
        if("org.omg.uml.foundation.core".equals(metaObject.getClass().getPackage().getName()))
        {
            //if uml 1.4, keep the old behavior (like bpm4struts)
            final Object value = this.findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_REQUIRED);
            return Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
        }
        else
        {
            //if >= uml 2, default behavior
            return super.isRequired();
        }
    }

    /**
     * @return JakartaUtils.isReadOnly(this)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        return JakartaUtils.isReadOnly(this);
    }

    /**
     * @param validatorType
     * @return validatorArgs
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getValidatorArgs(String)
     */
    protected Collection handleGetValidatorArgs(final String validatorType)
    {
        return JakartaUtils.getValidatorArgs(
            (ModelElementFacade)this.THIS(),
            validatorType);
    }

    /**
     * @return validatorVars
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getValidatorVars()
     */
    protected Collection handleGetValidatorVars()
    {
        return JakartaUtils.getValidatorVars(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            null);
    }

    /**
     * @return reset
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isReset()
     */
    protected boolean handleIsReset()
    {
        boolean reset =
            Boolean.valueOf(ObjectUtils.toString(this.findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_RESET)))
                   .booleanValue();
        if (!reset)
        {
            final JakartaAction action = (JakartaAction)this.getAction();
            reset = action != null && action.isFormReset();
        }
        return reset;
    }

    /**
     * @return complex
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isComplex()
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
     * @return attributes
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getAttributes()
     */
    protected Collection<AttributeFacade> handleGetAttributes()
    {
        Collection<AttributeFacade> attributes = null;
        ClassifierFacade type = this.getType();
        if (type != null)
        {
            if (type.isArrayType())
            {
                type = type.getNonArray();
            }
            if (type != null)
            {
                attributes = type.getAttributes(true);
            }
        }
        return attributes == null ? new ArrayList<AttributeFacade>() : attributes;
    }

    /**
     * @return navigableAssociationEnds
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getNavigableAssociationEnds()
     */
    @Override
    protected Collection<AssociationEndFacade> handleGetNavigableAssociationEnds()
    {
        Collection<AssociationEndFacade> associationEnds = null;
        ClassifierFacade type = this.getType();
        if (type != null)
        {
            if (type.isArrayType())
            {
                type = type.getNonArray();
            }
            if (type != null)
            {
                associationEnds = type.getNavigableConnectingEnds();
            }
        }
        return associationEnds == null ? new ArrayList<AssociationEndFacade>() : associationEnds;
    }

    /**
     * @return isEqualValidator
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isEqualValidator()
     */
    protected boolean handleIsEqualValidator()
    {
        final String equal = JakartaUtils.getEqual((ModelElementFacade)this.THIS());
        return equal != null && equal.trim().length() > 0;
    }

    /**
     * @return isBackingValueRequired
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#isEqualValidator()
     */
    protected boolean handleIsBackingValueRequired()
    {
        boolean required = false;
        if (this.isActionParameter())
        {
            required = this.isInputTable();
            final ClassifierFacade type = this.getType();

            if (!required && type != null)
            {
                final String name = this.getName();
                final String typeName = type.getFullyQualifiedName();

                // - if the backing value is not required for this parameter but on
                //   a targeting page it IS selectable we must allow the user to set the backing value as well
                final Collection<FrontEndView> views = this.getAction().getTargetViews();
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
        else if (this.isControllerOperationArgument())
        {
            final String name = this.getName();
            final Collection<FrontEndAction> actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator<FrontEndAction> actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                final JakartaAction action = (JakartaAction)actionIterator.next();
                final Collection<FrontEndParameter> formFields = action.getFormFields();
                for (final Iterator<FrontEndParameter> fieldIterator = formFields.iterator();
                    fieldIterator.hasNext() && !required;)
                {
                    final Object object = fieldIterator.next();
                    if (object instanceof JakartaParameter)
                    {
                        final JakartaParameter parameter = (JakartaParameter)object;
                        if (!parameter.equals(this))
                        {
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
     * @return findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getInputTableIdentifierColumns()
     */
    protected String handleGetInputTableIdentifierColumns()
    {
        return Objects.toString(this.findTaggedValue(JakartaProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS), "").trim();
    }

    /**
     * @param columnName
     * @return tableColumnActions
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getTableColumnActions(String)
     */
    protected List<JakartaAction> handleGetTableColumnActions(final String columnName)
    {
        final List<JakartaAction> columnActions = new ArrayList<JakartaAction>();

        if (columnName != null)
        {
            final Set<JakartaAction> actions = new LinkedHashSet<JakartaAction>(this.getTableHyperlinkActions());
            actions.addAll(this.getTableFormActions());
            for (final JakartaAction action : actions)
            {
                if (columnName.equals(action.getTableLinkColumnName()))
                {
                    columnActions.add(action);
                }
            }
        }

        return columnActions;
    }

    /**
     * @return maxLength
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getMaxLength()
     */
    protected String handleGetMaxLength()
    {
        final Collection<Collection> vars=getValidatorVars();
        if(vars == null)
        {
            return null;
        }
        for(Iterator<Collection> it=vars.iterator(); it.hasNext();)
        {
            final Object[] values=(it.next()).toArray();
            if("maxlength".equals(values[0]))
            {
                return values[1].toString();
            }
        }
        return null;
    }

    //to be used in the range validator: "range - 1000" or "range 20 -".
    /** - */
    static final String UNDEFINED_BOUND="-";
    /** javax.validation.constraints.NotNull */
    static final String AN_REQUIRED = "@javax.validation.constraints.NotNull";
    /** org.hibernate.validator.constraints.URL */
    static final String AN_URL = "@org.hibernate.validator.constraints.URL";
    /** org.apache.myfaces.extensions.validator.baseval.annotation.LongRange */
    static final String AN_LONG_RANGE = "@org.apache.myfaces.extensions.validator.baseval.annotation.LongRange";
    /** org.apache.myfaces.extensions.validator.baseval.annotation.DoubleRange */
    static final String AN_DOUBLE_RANGE = "@org.apache.myfaces.extensions.validator.baseval.annotation.DoubleRange";
    /** org.hibernate.validator.constraints.Email */
    static final String AN_EMAIL = "@org.hibernate.validator.constraints.Email";
    /** org.hibernate.validator.constraints.CreditCardNumber */
    static final String AN_CREDIT_CARD = "@org.hibernate.validator.constraints.CreditCardNumber";
    /** javax.validation.constraints.Size */
    static final String AN_LENGTH = "@javax.validation.constraints.Size";
    /** org.apache.myfaces.extensions.validator.baseval.annotation.Pattern */
    static final String AN_PATTERN = "@org.apache.myfaces.extensions.validator.baseval.annotation.Pattern";
    /** org.apache.myfaces.extensions.validator.crossval.annotation.Equals */
    static final String AN_EQUALS = "@org.apache.myfaces.extensions.validator.crossval.annotation.Equals";

    /**
     * @return the annotations
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaParameter#getMaxLength()
     */
    @Override
    protected Collection<String> handleGetAnnotations()
    {
        final Collection<String> result=new HashSet<String>();
        boolean requiredAdded=false;
        for(String vt: (Collection<String>)getValidatorTypes())
        {
            if(vt.startsWith("@")) //add the annotation
            {
                result.add(vt);
            }
            if(JakartaUtils.VT_REQUIRED.equals(vt))
            {
                requiredAdded=true;
                result.add(AN_REQUIRED);
            }
            else if(JakartaUtils.VT_URL.equals(vt))
            {
                result.add(AN_URL);
            }
            else if(JakartaUtils.VT_INT_RANGE.equals(vt))
            {
                final StringBuilder sb=new StringBuilder(AN_LONG_RANGE+"(");
                final String format = JakartaUtils.getInputFormat((ModelElementFacade)this.THIS());
                final String rangeStart = JakartaUtils.getRangeStart(format);
                boolean addComma=false;
                if(StringUtils.isNotBlank(rangeStart) && !rangeStart.equals(UNDEFINED_BOUND))
                {
                    sb.append("minimum="+rangeStart);
                    addComma=true;
                }
                final String rangeEnd = JakartaUtils.getRangeEnd(format);
                if(StringUtils.isNotBlank(rangeEnd) && !rangeEnd.equals(UNDEFINED_BOUND))
                {
                    if(addComma)
                    {
                        sb.append(",");
                    }
                    sb.append("maximum="+rangeEnd);
                }
                sb.append(")");
                result.add(sb.toString());
            }
            else if(JakartaUtils.VT_FLOAT_RANGE.equals(vt) || JakartaUtils.VT_DOUBLE_RANGE.equals(vt))
            {
                final StringBuilder sb=new StringBuilder(AN_DOUBLE_RANGE+"(");
                final String format = JakartaUtils.getInputFormat(((ModelElementFacade)this.THIS()));
                final String rangeStart = JakartaUtils.getRangeStart(format);
                boolean addComma=false;
                if(StringUtils.isNotBlank(rangeStart) && !rangeStart.equals(UNDEFINED_BOUND))
                {
                    sb.append("minimum="+rangeStart);
                    addComma=true;
                }
                final String rangeEnd = JakartaUtils.getRangeEnd(format);
                if(StringUtils.isNotBlank(rangeEnd) && !rangeEnd.equals(UNDEFINED_BOUND))
                {
                    if(addComma)
                    {
                        sb.append(",");
                    }
                    sb.append("maximum="+rangeEnd);
                }
                sb.append(")");
                result.add(sb.toString());
            }
            else if(JakartaUtils.VT_EMAIL.equals(vt))
            {
                result.add(AN_EMAIL);
            }
            else if(JakartaUtils.VT_CREDIT_CARD.equals(vt))
            {
                result.add(AN_CREDIT_CARD);
            }
            else if(JakartaUtils.VT_MIN_LENGTH.equals(vt) || JakartaUtils.VT_MAX_LENGTH.equals(vt))
            {
                final StringBuilder sb=new StringBuilder(AN_LENGTH+"(");
                final Collection formats = this.findTaggedValues(JakartaProfile.TAGGEDVALUE_INPUT_FORMAT);
                boolean addComma=false;
                for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                {
                    final String additionalFormat = String.valueOf(formatIterator.next());
                    if (JakartaUtils.isMinLengthFormat(additionalFormat))
                    {
                        if(addComma)
                        {
                            sb.append(",");
                        }
                        sb.append("min=");
                        sb.append(JakartaUtils.getMinLengthValue(additionalFormat));
                        addComma=true;
                    }
                    else if (JakartaUtils.isMaxLengthFormat(additionalFormat))
                    {
                        if(addComma)
                        {
                            sb.append(",");
                        }
                        sb.append("max=");
                        sb.append(JakartaUtils.getMinLengthValue(additionalFormat));
                        addComma=true;
                    }
                }
                sb.append(")");
                result.add(sb.toString());
            }
            else if(JakartaUtils.VT_MASK.equals(vt))
            {
                final Collection formats = this.findTaggedValues(JakartaProfile.TAGGEDVALUE_INPUT_FORMAT);
                for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                {
                    final String additionalFormat = String.valueOf(formatIterator.next());
                    if (JakartaUtils.isPatternFormat(additionalFormat))
                    {
                        result.add(AN_PATTERN+"(\""+JakartaUtils.getPatternValue(additionalFormat)+"\")");
                    }
                }
            }
            else if(JakartaUtils.VT_VALID_WHEN.equals(vt))
            {
                result.add("");
            }
            else if(JakartaUtils.VT_EQUAL.equals(vt))
            {
                result.add(AN_EQUALS+"(\""+JakartaUtils.getEqual((ModelElementFacade)this.THIS())+"\")");
            }
        }
        if(!requiredAdded && getLower() > 0)
        {
            result.add(AN_REQUIRED);
        }
        return result;
    }
}