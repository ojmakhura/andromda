package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndAttribute;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.uml2.uml.NamedElement;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndParameter.
 *
 * @see org.andromda.metafacades.uml.FrontEndParameter
 */
public class FrontEndParameterLogicImpl
        extends FrontEndParameterLogic {
    private static final long serialVersionUID = -5932754222510758357L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndParameterLogicImpl(
            final Object metaObject,
            final String context) {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isControllerOperationArgument()
     */
    @Override
    protected boolean handleIsControllerOperationArgument() {
        return this.getControllerOperation() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getControllerOperation()
     */
    @Override
    protected OperationFacade handleGetControllerOperation() {
        return this.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase() {
        return this.getEvent() instanceof FrontEndEvent || this.getOperation() instanceof FrontEndControllerOperation;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getView()
     */
    @Override
    protected Object handleGetView() {
        Object view = null;
        final EventFacade event = this.getEvent();
        if (event != null) {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction) {
                final FrontEndAction action = (FrontEndAction) transition;
                view = action.getInput();
            } else if (transition instanceof FrontEndForward) {
                final FrontEndForward forward = (FrontEndForward) transition;
                if (forward.isEnteringView()) {
                    view = forward.getTarget();
                }
            }
        }
        return view;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isActionParameter()
     */
    @Override
    protected boolean handleIsActionParameter() {
        final FrontEndAction action = this.getAction();
        return action != null && action.getParameters().contains(this.THIS());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getAction()
     */
    @Override
    protected FrontEndAction handleGetAction() {
        FrontEndAction actionObject = null;
        final EventFacade event = this.getEvent();
        if (event != null) {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction) {
                actionObject = (FrontEndAction) transition;
            }
        }
        return actionObject;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isTable()
     */
    @Override
    protected boolean handleIsTable() {
        boolean isTable = false;
        final ClassifierFacade type = this.getType();
        if (type != null) {
            isTable = isMany() || type.isCollectionType() || type.isArrayType();
            if (isTable) {
                final String tableTaggedValue = ObjectUtils
                        .toString(this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_IS_TABLE));
                isTable = StringUtils.isNotBlank(tableTaggedValue) ? Boolean.valueOf(tableTaggedValue.trim()) : true;
                if (!isTable) {
                    isTable = !this.getTableColumnNames().isEmpty();
                }
            }
        }
        return isTable && this.getOperation() == null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumnNames()
     */
    @Override
    protected Collection<String> handleGetTableColumnNames() {
        final Collection<String> tableColumnNames = new LinkedHashSet<String>();
        final Collection<Object> taggedValues = this
                .findTaggedValues(UMLProfile.TAGGEDVALUE_PRESENTATION_TABLE_COLUMNS);
        if (!taggedValues.isEmpty()) {
            for (final Object value : taggedValues) {
                final String taggedValue = StringUtils.trimToNull(String.valueOf(value));
                if (taggedValue != null) {
                    final String[] properties = taggedValue.split("[,\\s]+");
                    for (int ctr = 0; ctr < properties.length; ctr++) {
                        final String property = properties[ctr];
                        tableColumnNames.add(property);
                    }
                }
            }
        }

        // - if we have no table column names explicitly defined, use the table
        // attribute names.
        if (tableColumnNames.isEmpty()) {
            tableColumnNames.addAll(this.getTableAttributeNames());
        }
        return tableColumnNames;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    @Override
    protected Collection<String> handleGetTableColumns() {
        final Collection<String> tableColumns = new ArrayList(this.getNonArrayAttributes());
        final Collection<String> tableColumnNames = this.getTableColumnNames();
        CollectionUtils.filter(
                tableColumns,
                new Predicate() {
                    public boolean evaluate(final Object object) {
                        final ModelElementFacade attribute = (ModelElementFacade) object;
                        final String attributeName = attribute.getName();
                        return attributeName != null && tableColumnNames.contains(attributeName);
                    }
                });
        return tableColumns;
    }

    /**
     * Gets all attributes for an array type that has a corresponding non-array
     * type.
     *
     * @return the collection of attributes.
     */
    private Collection<AttributeFacade> getNonArrayAttributes() {
        final Collection<AttributeFacade> nonArrayAttributes = new ArrayList<AttributeFacade>();
        final ClassifierFacade type = this.getType();
        if (type != null && (type.isArrayType() || isMany())) {
            final ClassifierFacade nonArrayType = type.getNonArray();
            if (nonArrayType != null) {
                nonArrayAttributes.addAll(nonArrayType.getAttributes(true));
            }
        }
        return nonArrayAttributes;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableAttributeNames()
     */
    @Override
    protected Collection<String> handleGetTableAttributeNames() {
        final Collection<String> tableAttributeNames = new ArrayList<String>();
        for (AttributeFacade attribute : this.getNonArrayAttributes()) {
            tableAttributeNames.add(attribute.getName());
        }
        return tableAttributeNames;
    }

    /**
     * UML2 v5: FrontEndParameter returns NULL for namespace. Need another way to
     * get package name
     * 
     * @see org.andromda.metafacades.uml.FrontEndEvent#getPackageName()
     */
    @Override
    protected String handleGetPackageName() {
        String packageName = UmlUtilities.getPackageName((NamedElement) this.metaObject, ".", false);
        if (StringUtils.isBlank(packageName)) {
            packageName = this.getPackage().getFullyQualifiedName();
        }
        return packageName;
    }

    @Override
    protected String handleGetDateFormatter() {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getName() + "DateFormatter" : null;
    }

    @Override
    protected boolean handleIsStrictDateFormat() {
        return MetafacadeWebUtils.isStrictDateFormat((ModelElementFacade) this.THIS());
    }

    /**
     * @return the default date format pattern as defined using the configured
     *         property
     */
    private String getDefaultDateFormat() {
        return (String) this.getConfiguredProperty(MetafacadeWebGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }

    /**
     * @return the default time format pattern as defined using the configured
     *         property
     */
    private String getDefaultTimeFormat() {
        return (String) this.getConfiguredProperty(MetafacadeWebGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }

    @Override
    protected String handleGetFormat() {
        return MetafacadeWebUtils.getFormat(
                (ModelElementFacade) this.THIS(),
                this.getType(),
                this.getDefaultDateFormat(),
                this.getDefaultTimeFormat());
    }

    @Override
    protected String handleGetTimeFormatter() {
        final ClassifierFacade type = this.getType();
        return type != null && type.isTimeType() ? this.getName() + "TimeFormatter" : null;
    }

    /**
     * Gets the current value of the specified input type (or an empty string
     * if one isn't specified).
     *
     * @return the input type name.
     */
    @Override
    protected String handleGetInputType() {
        return Objects.toString(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_TYPE)).trim();
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

    @Override
    protected boolean handleIsInputCheckbox() {
        boolean checkbox = this.isInputType(MetafacadeWebGlobals.INPUT_CHECKBOX);
        if (!checkbox && this.getInputType().length() == 0) {
            final ClassifierFacade type = this.getType();
            checkbox = type != null ? type.isBooleanType() : false;
        }
        return checkbox;
    }

    @Override
    protected boolean handleIsInputTextarea() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_TEXTAREA);
    }

    @Override
    protected boolean handleIsInputSelect() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_SELECT);
    }

    @Override
    protected boolean handleIsInputSecret() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_PASSWORD);
    }

    @Override
    protected boolean handleIsInputHidden() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_HIDDEN);
    }

    @Override
    protected boolean handleIsInputFile() {
        boolean file = this.isInputType(MetafacadeWebGlobals.INPUT_FILE);

        if (file) {
            return file;
        }

        ClassifierFacade type = getType();
        if (type != null) {
            file = type.isFileType();
        }
        return file;
    }

    @Override
    protected boolean handleIsPlaintext() {
        return this.isInputType(MetafacadeWebGlobals.PLAIN_TEXT);
    }

    @Override
    protected boolean handleIsInputRadio() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_RADIO);
    }

    @Override
    protected boolean handleIsInputText() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_TEXT);
    }

    @Override
    protected String handleGetBackingListName() {
        return Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.BACKING_LIST_PATTERN), "").replaceAll(
                "\\{0\\}",
                this.getName());
    }

    @Override
    protected String handleGetLabelListName() {
        return Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.LABEL_LIST_PATTERN), "").replaceAll(
                "\\{0\\}",
                this.getName());
    }

    @Override
    protected String handleGetValueListName() {
        return Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.VALUE_LIST_PATTERN), "").replaceAll(
                "\\{0\\}",
                this.getName());
    }

    @Override
    protected boolean handleIsSelectable() {
        boolean selectable = false;
        if (this.isActionParameter()) {
            selectable = this.isInputMultibox() || this.isInputSelect() || this.isInputRadio();
            final ClassifierFacade type = this.getType();

            if (!selectable && type != null) {
                final String name = this.getName();
                final String typeName = type.getFullyQualifiedName();

                // - if the parameter is not selectable but on a targetting page it IS
                // selectable we must
                // allow the user to set the backing list too
                final Collection<FrontEndView> views = this.getAction().getTargetViews();
                for (final Iterator<FrontEndView> iterator = views.iterator(); iterator.hasNext() && !selectable;) {
                    final Collection<FrontEndParameter> parameters = iterator.next().getAllActionParameters();
                    for (final Iterator<FrontEndParameter> parameterIterator = parameters.iterator(); parameterIterator
                            .hasNext() && !selectable;) {
                        final FrontEndParameter parameter = parameterIterator.next();
                        final String parameterName = parameter.getName();
                        final ClassifierFacade parameterType = parameter.getType();
                        if (parameterType != null) {
                            final String parameterTypeName = parameterType.getFullyQualifiedName();
                            if (name.equals(parameterName) && typeName.equals(parameterTypeName)) {
                                selectable = parameter.isInputMultibox() || parameter.isInputSelect() ||
                                        parameter.isInputRadio();
                            }
                        }
                    }
                }
            }
        } else if (this.isControllerOperationArgument()) {
            final String name = this.getName();
            final Collection actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();) {
                final FrontEndAction action = (FrontEndAction) actionIterator.next();
                final Collection<FrontEndParameter> formFields = action.getFormFields();
                for (final Iterator<FrontEndParameter> fieldIterator = formFields.iterator(); fieldIterator.hasNext()
                        && !selectable;) {
                    final Object object = fieldIterator.next();
                    final FrontEndParameter parameter = (FrontEndParameter) object;
                    if (!parameter.equals(this)) {
                        if (name.equals(parameter.getName())) {
                            selectable = parameter.isSelectable();
                        }
                    }
                }
            }
        }
        return selectable;
    }
    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @return A String representing Java code for the initialization of an array.
     */
    private String constructDummyArray()
    {
        return MetafacadeWebUtils.constructDummyArrayDeclaration(
            this.getName(),
            MetafacadeWebGlobals.DUMMY_ARRAY_COUNT);
    }

    /**
     * Stores the initial value of each type.
     */
    private final Map<String, String> initialValues = new HashMap<String, String>();

    @Override
    protected String handleGetDummyValue() {
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

    @Override
    protected String handleGetValueListDummyValue() {
        return this.constructDummyArray();
    }

    @Override
    protected String handleGetTableSortColumnProperty() {
        return this.getName() + "SortColumn";
    }

    @Override
    protected String handleGetTableSortAscendingProperty() {
        return this.getName() + "SortAscending";
    }

    @Override
    protected String handleGetFormAttributeSetProperty() {
        return this.getName() + "Set";
    }

    @Override
    protected boolean handleIsReadOnly() {
        return MetafacadeWebUtils.isReadOnly(this);
    }

    @Override
    protected boolean handleIsValidationRequired() {
        boolean required = !this.getValidatorTypes().isEmpty();
        if (!required)
        {
            // - look for any attributes
            for (final Iterator<FrontEndAttribute> iterator = this.getAttributes().iterator(); iterator.hasNext();)
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
                    if (object instanceof FrontEndAttribute)
                    {
                        final FrontEndAttribute attribute = (FrontEndAttribute)object;
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

    @Override
    protected Collection handleGetValidatorTypes() {
        return MetafacadeWebUtils.getValidatorTypes(
            (ModelElementFacade)this.THIS(),
            this.getType());
    }

    @Override
    protected String handleGetValidWhen() {
        return MetafacadeWebUtils.getValidWhen(this);
    }

    @Override
    protected boolean handleIsInputMultibox() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_MULTIBOX);
    }

    @Override
    protected boolean handleIsComplex() {
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

    @Override
    protected Collection handleGetAttributes() {
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

    @Override
    protected Collection<AssociationEndFacade> handleGetNavigableAssociationEnds() {
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

    @Override
    protected String handleGetBackingValueName() {
        return Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.BACKING_VALUE_PATTERN), "").replaceAll(
            "\\{0\\}",
            this.getName());
    }

    @Override
    protected boolean handleIsInputTable() {
        return this.getInputTableIdentifierColumns().length() > 0 || this.isInputType(MetafacadeWebGlobals.INPUT_TABLE);
    }

    @Override
    protected boolean handleIsBackingValueRequired() {
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
                        final FrontEndParameter parameter = parameterIterator.next();
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
            final Collection<FrontEndAction> actions = this.getControllerOperation().getDeferringActions();
            for (final Iterator<FrontEndAction> actionIterator = actions.iterator(); actionIterator.hasNext();)
            {
                final FrontEndAction action = (FrontEndAction)actionIterator.next();
                final Collection<FrontEndParameter> formFields = action.getFormFields();
                for (final Iterator<FrontEndParameter> fieldIterator = formFields.iterator();
                    fieldIterator.hasNext() && !required;)
                {
                    final FrontEndParameter parameter = fieldIterator.next();
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
        return required;
    }

    @Override
    protected String handleGetInputTableIdentifierColumns() {
        return Objects.toString(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_TABLE_IDENTIFIER_COLUMNS), "").trim();
    }

    @Override
    protected boolean handleIsPageableTable() {
        final Object value = this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_TABLE_PAGEABLE);
        return Boolean.valueOf(Objects.toString(value, "")).booleanValue();
    }

    @Override
    protected String handleGetMaxLength() {
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

    @Override
    protected Collection handleGetAnnotations() {
        final Collection<String> result=new HashSet<String>();
        boolean requiredAdded=false;
        for(String vt: (Collection<String>)getValidatorTypes())
        {
            if(vt.startsWith("@")) //add the annotation
            {
                result.add(vt);
            }
            if(MetafacadeWebUtils.VT_REQUIRED.equals(vt))
            {
                requiredAdded=true;
                result.add(AN_REQUIRED);
            }
            else if(MetafacadeWebUtils.VT_URL.equals(vt))
            {
                result.add(AN_URL);
            }
            else if(MetafacadeWebUtils.VT_INT_RANGE.equals(vt))
            {
                final StringBuilder sb=new StringBuilder(AN_LONG_RANGE+"(");
                final String format = MetafacadeWebUtils.getInputFormat((ModelElementFacade)this.THIS());
                final String rangeStart = MetafacadeWebUtils.getRangeStart(format);
                boolean addComma=false;
                if(StringUtils.isNotBlank(rangeStart) && !rangeStart.equals(UNDEFINED_BOUND))
                {
                    sb.append("minimum="+rangeStart);
                    addComma=true;
                }
                final String rangeEnd = MetafacadeWebUtils.getRangeEnd(format);
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
            else if(MetafacadeWebUtils.VT_FLOAT_RANGE.equals(vt) || MetafacadeWebUtils.VT_DOUBLE_RANGE.equals(vt))
            {
                final StringBuilder sb=new StringBuilder(AN_DOUBLE_RANGE+"(");
                final String format = MetafacadeWebUtils.getInputFormat(((ModelElementFacade)this.THIS()));
                final String rangeStart = MetafacadeWebUtils.getRangeStart(format);
                boolean addComma=false;
                if(StringUtils.isNotBlank(rangeStart) && !rangeStart.equals(UNDEFINED_BOUND))
                {
                    sb.append("minimum="+rangeStart);
                    addComma=true;
                }
                final String rangeEnd = MetafacadeWebUtils.getRangeEnd(format);
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
            else if(MetafacadeWebUtils.VT_EMAIL.equals(vt))
            {
                result.add(AN_EMAIL);
            }
            else if(MetafacadeWebUtils.VT_CREDIT_CARD.equals(vt))
            {
                result.add(AN_CREDIT_CARD);
            }
            else if(MetafacadeWebUtils.VT_MIN_LENGTH.equals(vt) || MetafacadeWebUtils.VT_MAX_LENGTH.equals(vt))
            {
                final StringBuilder sb=new StringBuilder(AN_LENGTH+"(");
                final Collection formats = this.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_FORMAT);
                boolean addComma=false;
                for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                {
                    final String additionalFormat = String.valueOf(formatIterator.next());
                    if (MetafacadeWebUtils.isMinLengthFormat(additionalFormat))
                    {
                        if(addComma)
                        {
                            sb.append(",");
                        }
                        sb.append("min=");
                        sb.append(MetafacadeWebUtils.getMinLengthValue(additionalFormat));
                        addComma=true;
                    }
                    else if (MetafacadeWebUtils.isMaxLengthFormat(additionalFormat))
                    {
                        if(addComma)
                        {
                            sb.append(",");
                        }
                        sb.append("max=");
                        sb.append(MetafacadeWebUtils.getMinLengthValue(additionalFormat));
                        addComma=true;
                    }
                }
                sb.append(")");
                result.add(sb.toString());
            }
            else if(MetafacadeWebUtils.VT_MASK.equals(vt))
            {
                final Collection formats = this.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_FORMAT);
                for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                {
                    final String additionalFormat = String.valueOf(formatIterator.next());
                    if (MetafacadeWebUtils.isPatternFormat(additionalFormat))
                    {
                        result.add(AN_PATTERN+"(\""+MetafacadeWebUtils.getPatternValue(additionalFormat)+"\")");
                    }
                }
            }
            else if(MetafacadeWebUtils.VT_VALID_WHEN.equals(vt))
            {
                result.add("");
            }
            else if(MetafacadeWebUtils.VT_EQUAL.equals(vt))
            {
                result.add(AN_EQUALS+"(\""+MetafacadeWebUtils.getEqual((ModelElementFacade)this.THIS())+"\")");
            }
        }
        if(!requiredAdded && getLower() > 0)
        {
            result.add(AN_REQUIRED);
        }
        return result;
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
            return ((FrontEndAction)action).isHyperlink() == this.hyperlink;
        }
    }
    
    /**
     * If this is a table this method returns all those actions that are declared to work
     * on this table.
     *
     * @param hyperlink denotes on which type of actions to filter
     */
    private List<FrontEndAction> getTableActions(boolean hyperlink)
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>(super.getTableActions());
        CollectionUtils.filter(actions, new ActionFilter(hyperlink));
        return actions;
    }

    @Override
    protected List<FrontEndAction> handleGetTableHyperlinkActions() {
        return this.getTableActions(true);
    }

    @Override
    protected List<FrontEndAction> handleGetTableFormActions() {
        return this.getTableActions(false);
    }

    @Override
    protected List<FrontEndAction> handleGetTableActions() {
        final Set<FrontEndAction> actions = new LinkedHashSet<FrontEndAction>();
        final String name = StringUtils.trimToNull(getName());
        if (name != null && isTable())
        {
            final FrontEndView view = this.getView();

            final Collection<UseCaseFacade> allUseCases = getModel().getAllUseCases();
            for (final UseCaseFacade useCase : allUseCases)
            {
                if (useCase instanceof FrontEndUseCase)
                {
                    final FrontEndActivityGraph graph = ((FrontEndUseCase)useCase).getActivityGraph();
                    if (graph != null)
                    {
                        final Collection<TransitionFacade> transitions = graph.getTransitions();
                        for (final TransitionFacade transition : transitions)
                        {
                            if (transition.getSource().equals(view) && transition instanceof FrontEndAction)
                            {
                                final FrontEndAction action = (FrontEndAction)transition;
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
        return new ArrayList<FrontEndAction>(actions);
    }

    @Override
    protected boolean handleIsEqualValidator() {
        final String equal = MetafacadeWebUtils.getEqual((ModelElementFacade)this.THIS());
        return equal != null && equal.trim().length() > 0;
    }

    @Override
    protected boolean handleIsReset() {
        boolean reset =
            Boolean.valueOf(Objects.toString(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_RESET), ""))
                   .booleanValue();
        if (!reset)
        {
            final FrontEndAction action = (FrontEndAction)this.getAction();
            reset = action != null && action.isFormReset();
        }
        return reset;
    }

    @Override
    protected Collection handleGetValidatorVars() {
        return MetafacadeWebUtils.getValidatorVars(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            null);
    }

    @Override
    protected boolean handleIsInputButton() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_BUTTON);
    }

    @Override
    protected boolean handleIsInputColor() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_COLOR);
    }

    @Override
    protected boolean handleIsInputDate() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_DATE);
    }

    @Override
    protected boolean handleIsInputDatetimeLocal() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_DATETIME_LOCAL);
    }

    @Override
    protected boolean handleIsInputEmail() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_EMAIL);
    }

    @Override
    protected boolean handleIsInputImage() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_IMAGE);
    }

    @Override
    protected boolean handleIsInputMonth() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_MONTH);
    }

    @Override
    protected boolean handleIsInputNumber() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_NUMBER);
    }

    @Override
    protected boolean handleIsInputRange() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_RANGE);
    }

    @Override
    protected boolean handleIsInputReset() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_RESET);
    }

    @Override
    protected boolean handleIsInputSearch() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_SEARCH);
    }

    @Override
    protected boolean handleIsInputSubmit() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_SUBMIT);
    }

    @Override
    protected boolean handleIsInputTel() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_TEL);
    }

    @Override
    protected boolean handleIsInputTime() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_TIME);
    }

    @Override
    protected boolean handleIsInputUrl() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_URL);
    }

    @Override
    protected boolean handleIsInputWeek() {
        return this.isInputType(MetafacadeWebGlobals.INPUT_WEEK);
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(MetafacadeWebGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    @Override
    protected String handleGetTableColumnMessageKey(String columnName) {
        StringBuilder messageKey = new StringBuilder();
        if (!this.isNormalizeMessages())
        {
            final FrontEndView view = (FrontEndView)this.getView();
            if (view != null)
            {
                messageKey.append(this.getMessageKey());
                messageKey.append('.');
            }
        }
        messageKey.append(StringUtilsHelper.toResourceMessageKey(columnName));
        return messageKey.toString();
    }

    @Override
    protected String handleGetTableColumnMessageValue(String columnName) {
        return StringUtilsHelper.toPhrase(columnName);
    }

    @Override
    protected Collection handleGetValidatorArgs(String validatorType) {
        return MetafacadeWebUtils.getValidatorArgs(
            (ModelElementFacade)this.THIS(),
            validatorType);
    }

    @Override
    protected List handleGetTableColumnActions(String columnName) {
        final List<FrontEndAction> columnActions = new ArrayList<FrontEndAction>();

        if (columnName != null)
        {
            final Set<FrontEndAction> actions = new LinkedHashSet<FrontEndAction>(this.getTableHyperlinkActions());
            actions.addAll(this.getTableFormActions());
            for (final FrontEndAction action : actions)
            {
                if (columnName.equals(action.getTableLinkColumnName()))
                {
                    columnActions.add(action);
                }
            }
        }

        return columnActions;
    }

    @Override
    protected String handleGetMessageValue() {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }

    @Override
    protected String handleGetDocumentationKey() {
        return getMessageKey() + '.' + MetafacadeWebGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    @Override
    protected String handleGetMessageKey() {
        final StringBuilder messageKey = new StringBuilder();

        if (!this.isNormalizeMessages())
        {
            if (this.isActionParameter())
            {
                final FrontEndAction action = this.getAction();
                if (action != null)
                {
                    messageKey.append(action.getMessageKey());
                    messageKey.append('.');
                }
            }
            else
            {
                final FrontEndView view = this.getView();
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

    @Override
    protected String handleGetDocumentationValue() {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation(
                    "",
                    64,
                    false));
        return value == null ? "" : value;
    }
}
