package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
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
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TransitionFacade;
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
     * Overridden to provide quotes around string types.
     * 
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        if (StringUtils.isNotBlank(defaultValue))
        {
            final ClassifierFacade type = this.getType();
            if (type != null && type.isStringType())
            {
                defaultValue = "\"" + defaultValue + "\"";
            }
        }
        return defaultValue;
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
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    public Collection getTableColumns()
    {
        final Collection tableColumns = super.getTableColumns();
        if (tableColumns.isEmpty())
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
            for (final Iterator columnNameIterator = columnNames.iterator(); columnNameIterator.hasNext();)
            {
                final Object columnObject = columnNameIterator.next();
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
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getFormat()
     */
    protected String handleGetFormat()
    {
        return JSFUtils.getFormat(
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
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isStrictDateFormat()
     */
    protected boolean handleIsStrictDateFormat()
    {
        return JSFUtils.isStrictDateFormat((ModelElementFacade)this.THIS());
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
        return this.isInputType(JSFGlobals.INPUT_TEXTAREA);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputSelect()
     */
    protected boolean handleIsInputSelect()
    {
        return this.isInputType(JSFGlobals.INPUT_SELECT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputSecret()
     */
    protected boolean handleIsInputSecret()
    {
        return this.isInputType(JSFGlobals.INPUT_PASSWORD);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputHidden()
     */
    protected boolean handleIsInputHidden()
    {
        return this.isInputType(JSFGlobals.INPUT_HIDDEN);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isPlaintext()
     */
    protected boolean handleIsPlaintext()
    {
        return this.isInputType(JSFGlobals.PLAIN_TEXT);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputTable()
     */
    protected boolean handleIsInputTable()
    {
        return this.isInputType(JSFGlobals.INPUT_TABLE);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputRadio()
     */
    protected boolean handleIsInputRadio()
    {
        return this.isInputType(JSFGlobals.INPUT_RADIO);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputText()
     */
    protected boolean handleIsInputText()
    {
        return this.isInputType(JSFGlobals.INPUT_TEXT);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputMultibox()
     */
    protected boolean handleIsInputMultibox()
    {
        return this.isInputType(JSFGlobals.INPUT_MULTIBOX);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isInputCheckbox()
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getBackingValueName()
     */
    protected String handleGetBackingValueName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.BACKING_VALUE_PATTERN)).replaceAll(
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
        return JSFUtils.constructDummyArrayDeclaration(
            this.getName(),
            JSFGlobals.DUMMY_ARRAY_COUNT);
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
        boolean required = !this.getValidatorTypes().isEmpty();
        if (!required)
        {
            for (final Iterator iterator = this.getAttributes().iterator(); iterator.hasNext();)
            {
                final JSFAttribute attribute = (JSFAttribute)iterator.next();
                required = !attribute.getValidatorTypes().isEmpty();
                if (required)
                {
                    break;
                }
            }
        }
        return required;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidatorTypes()
     */
    protected java.util.Collection handleGetValidatorTypes()
    {
        return JSFUtils.getValidatorTypes((ModelElementFacade)this.THIS(), this.getType());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidWhen()
     */
    protected java.lang.String handleGetValidWhen()
    {
        return JSFUtils.getValidWhen(this);
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
        return JSFUtils.isReadOnly(this);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidatorArgs(java.lang.String)
     */
    protected java.util.Collection handleGetValidatorArgs(final java.lang.String validatorType)
    {
        return JSFUtils.getValidatorArgs((ModelElementFacade)this.THIS(), validatorType);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#getValidatorVars()
     */
    protected java.util.Collection handleGetValidatorVars()
    {
        return JSFUtils.getValidatorVars(((ModelElementFacade)this.THIS()), this.getType());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isReset()
     */
    protected boolean handleIsReset()
    {
        boolean reset =
            Boolean.valueOf(ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_RESET)))
                   .booleanValue();
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
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isEqualValidator()
     */
    protected boolean handleIsEqualValidator()
    {
        final String equal = JSFUtils.getEqual((ModelElementFacade)this.THIS());
        return equal != null && equal.trim().length() > 0;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFParameter#isEqualValidator()
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
                //   a targetting page it IS selectable we must allow the user to set the backing value as well                 
                final Collection views = this.getAction().getTargetViews();
                for (final Iterator iterator = views.iterator(); iterator.hasNext() && !required;)
                {
                    final FrontEndView view = (FrontEndView)iterator.next();
                    final Collection parameters = view.getAllActionParameters();
                    for (final Iterator parameterIterator = parameters.iterator();
                        parameterIterator.hasNext() && !required;)
                    {
                        final JSFParameter parameter = (JSFParameter)parameterIterator.next();
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
        else if (this.isControllerOperationArgument())
        {
            final String name = this.getName();
            final Collection actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                final JSFAction action = (JSFAction)actionIterator.next();
                final Collection formFields = action.getFormFields();
                for (final Iterator fieldIterator = formFields.iterator(); fieldIterator.hasNext() && !required;)
                {
                    final JSFParameter parameter = (JSFParameter)fieldIterator.next();
                    if (name.equals(parameter.getName()))
                    {
                        required = parameter.isBackingValueRequired();
                    }
                }
            }
        }
        return required;
    }
}