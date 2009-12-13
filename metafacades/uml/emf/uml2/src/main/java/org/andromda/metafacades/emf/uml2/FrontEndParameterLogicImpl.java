package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndParameter.
 *
 * @see org.andromda.metafacades.uml.FrontEndParameter
 */
public class FrontEndParameterLogicImpl
    extends FrontEndParameterLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndParameterLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getControllerOperation() != null
     * @see org.andromda.metafacades.uml.FrontEndParameter#isControllerOperationArgument()
     */
    protected boolean handleIsControllerOperationArgument()
    {
        return this.getControllerOperation() != null;
    }

    /**
     * @return getOperation()
     * @see org.andromda.metafacades.uml.FrontEndParameter#getControllerOperation()
     */
    protected Object handleGetControllerOperation()
    {
        return this.getOperation();
    }

    /**
     * @return getEvent() instanceof FrontEndEvent || getOperation() instanceof FrontEndControllerOperation
     * @see org.andromda.metafacades.uml.FrontEndParameter#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getEvent() instanceof FrontEndEvent || this.getOperation() instanceof FrontEndControllerOperation;
    }

    /**
     * @return getEvent().getTransition() instanceof FrontEndAction .getInput()
     * @see org.andromda.metafacades.uml.FrontEndParameter#getView()
     */
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
     * @return getAction().getParameters().contains(this)
     * @see org.andromda.metafacades.uml.FrontEndParameter#isActionParameter()
     */
    protected boolean handleIsActionParameter()
    {
        final FrontEndAction action = this.getAction();
        return action != null && action.getParameters().contains(this.THIS());
    }

    /**
     * @return getEvent().getTransition() instanceof FrontEndAction
     * @see org.andromda.metafacades.uml.FrontEndParameter#getAction()
     */
    protected Object handleGetAction()
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
        return actionObject;
    }

    /**
     * @return andromda_presentation_view_table for type
     * @see org.andromda.metafacades.uml.FrontEndParameter#isTable()
     */
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
     * @return andromda_presentation_view_table_columns
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumnNames()
     */
    protected Collection handleGetTableColumnNames()
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

        // - if we have no table column names explicitly defined, use the table
        // attribute names.
        if (tableColumnNames.isEmpty())
        {
            tableColumnNames.addAll(this.getTableAttributeNames());
        }
        return tableColumnNames;
    }

    /**
     * @return getNonArrayAttributes() with getTableColumnNames()
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumns()
     */
    protected Collection handleGetTableColumns()
    {
        final Collection tableColumns = new ArrayList(this.getNonArrayAttributes());
        final Collection tableColumnNames = this.getTableColumnNames();
        CollectionUtils.filter(
            tableColumns,
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
     *
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
     * @return getNonArrayAttributes().getNames()
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableAttributeNames()
     */
    protected Collection handleGetTableAttributeNames()
    {
        final Collection tableAttributeNames = new ArrayList(this.getNonArrayAttributes());
        CollectionUtils.transform(
            tableAttributeNames,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AttributeFacade)object).getName();
                }
            });
        return tableAttributeNames;
    }
}