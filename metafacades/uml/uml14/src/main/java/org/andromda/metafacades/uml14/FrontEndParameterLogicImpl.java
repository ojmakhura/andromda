package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndParameter.
 *
 * @see org.andromda.metafacades.uml.FrontEndParameter
 * @author Bob Fields
 */
public class FrontEndParameterLogicImpl
    extends FrontEndParameterLogic
{
    private static final long serialVersionUID = 2219144832310011722L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndParameterLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isControllerOperationArgument()
     */
    @Override
    protected boolean handleIsControllerOperationArgument()
    {
        return this.getControllerOperation() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getControllerOperation()
     */
    @Override
    protected OperationFacade handleGetControllerOperation()
    {
        return this.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getEvent() instanceof FrontEndEvent || this.getOperation() instanceof FrontEndControllerOperation;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getView()
     */
    @Override
    protected Object handleGetView()
    {
        Object view = null;
        final EventFacade event = this.getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction)
            {
                final FrontEndAction action = (FrontEndAction)transition;
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
        return view;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isActionParameter()
     */
    @Override
    protected boolean handleIsActionParameter()
    {
        final FrontEndAction action = this.getAction();
        return action != null && action.getParameters().contains(this.THIS());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getAction()
     */
    @Override
    protected FrontEndAction handleGetAction()
    {
        Object actionObject = null;
        final EventFacade event = this.getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof FrontEndAction)
            {
                actionObject = transition;
            }
        }
        return (FrontEndAction)actionObject;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isTable()
     */
    @Override
    protected boolean handleIsTable()
    {
        boolean isTable = false;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            isTable = type.isCollectionType() || type.isArrayType();
            if (isTable)
            {
                final String tableTaggedValue = ObjectUtils.toString(this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_IS_TABLE));
                isTable =
                    StringUtils.isNotBlank(tableTaggedValue) ? Boolean.valueOf(tableTaggedValue.trim()) : true;
                if (!isTable)
                {
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
    protected Collection<String> handleGetTableColumnNames()
    {
        final Collection tableColumnNames = new LinkedHashSet();
        final Collection taggedValues = this.findTaggedValues(UMLProfile.TAGGEDVALUE_PRESENTATION_TABLE_COLUMNS);
        if (!taggedValues.isEmpty())
        {
            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                final String taggedValue = StringUtils.trimToNull(String.valueOf(iterator.next()));
                if (taggedValue != null)
                {
                    final String[] properties = taggedValue.split("[,\\s]+");
                    for (int ctr = 0; ctr < properties.length; ctr++)
                    {
                        final String property = properties[ctr];
                        tableColumnNames.add(property);
                    }
                }
            }
        }
        // - if we have no table column names explicitly defined, use the table attribute names.
        if (tableColumnNames.isEmpty())
        {
            tableColumnNames.addAll(this.getTableAttributeNames());
        }
        return tableColumnNames;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    @Override
    protected Collection<String> handleGetTableColumns()
    {
        final Collection tableColumns = new ArrayList(this.getNonArrayAttributes());
        final Collection tableColumnNames = this.getTableColumnNames();
        CollectionUtils.filter(tableColumns,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final ModelElementFacade attribute = (ModelElementFacade)object;
                    final String attributeName = attribute.getName();
                    return attributeName != null && tableColumnNames.contains(attributeName);
                }
            });
        return tableColumns;
    }

    /**
     * Gets all attributes for an array type that has a corresponding non-array
     * type.
     * @return the collection of attributes.
     */
    private Collection getNonArrayAttributes()
    {
        final Collection nonArrayAttributes = new ArrayList();
        final ClassifierFacade type = this.getType();
        if (type != null && type.isArrayType())
        {
            final ClassifierFacade nonArrayType = type.getNonArray();
            if (nonArrayType != null)
            {
                nonArrayAttributes.addAll(nonArrayType.getAttributes(true));
            }
        }
        return nonArrayAttributes;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableAttributeNames()
     */
    @Override
    protected Collection<String> handleGetTableAttributeNames()
    {
        final Collection tableAttributeNames = new ArrayList(this.getNonArrayAttributes());
        CollectionUtils.transform(tableAttributeNames,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AttributeFacade)object).getName();
                }
            });
        return tableAttributeNames;
    }

    @Override
    protected String handleGetDateFormatter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsStrictDateFormat() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTimeFormatter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsInputCheckbox() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputTextarea() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputSelect() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputSecret() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputHidden() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputFile() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsPlaintext() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputRadio() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputText() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetBackingListName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetLabelListName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetValueListName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsSelectable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetDummyValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetValueListDummyValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTableSortColumnProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTableSortAscendingProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFormAttributeSetProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsValidationRequired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Collection handleGetValidatorTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetValidWhen() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsInputMultibox() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsComplex() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Collection handleGetAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection<AssociationEndFacade> handleGetNavigableAssociationEnds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetBackingValueName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsInputTable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsBackingValueRequired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetInputTableIdentifierColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsPageableTable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetMaxLength() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAnnotations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<FrontEndAction> handleGetTableHyperlinkActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<FrontEndAction> handleGetTableFormActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<FrontEndAction> handleGetTableActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsEqualValidator() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsReset() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Collection handleGetValidatorVars() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsInputButton() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputColor() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputDate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputDatetimeLocal() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputEmail() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputImage() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputMonth() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputNumber() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputRange() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputReset() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputSearch() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputSubmit() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputTel() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputTime() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputUrl() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsInputWeek() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetInputType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetMessageValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetDocumentationKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetMessageKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetDocumentationValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTableColumnMessageKey(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTableColumnMessageValue(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetValidatorArgs(String validatorType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List handleGetTableColumnActions(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetInputAction() {
        // TODO Auto-generated method stub
        return null;
    }
}