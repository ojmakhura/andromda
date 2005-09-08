package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndParameter.
 *
 * @see org.andromda.metafacades.uml.FrontEndParameter
 */
public class FrontEndParameterLogicImpl
    extends FrontEndParameterLogic
{
    public FrontEndParameterLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isControllerOperationArgument()
     */
    protected boolean handleIsControllerOperationArgument()
    {
        return this.getControllerOperation() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getControllerOperation()
     */
    protected Object handleGetControllerOperation()
    {
        return this.getOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getEvent() instanceof FrontEndEvent || this.getOperation() instanceof FrontEndControllerOperation;
    }

    /**
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
     * @see org.andromda.metafacades.uml.FrontEndParameter#isActionParameter()
     */
    protected boolean handleIsActionParameter()
    {
        final FrontEndAction action = this.getAction();
        return action != null && action.getParameters().contains(this.THIS());
    }

    /**
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
     * @see org.andromda.metafacades.uml.FrontEndParameter#isTable()
     */
    protected boolean handleIsTable()
    {
        boolean isTable = false;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            isTable = (type.isCollectionType() || type.isArrayType()) && !this.getTableColumnNames().isEmpty();
        }
        return isTable;
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndParameter#getTableColumnNames()
     */
    protected Collection handleGetTableColumnNames()
    {
        final Collection tableColumnNames = new HashSet();
        if (!this.isControllerOperationArgument())
        {
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
        }
        return tableColumnNames;
    }
}