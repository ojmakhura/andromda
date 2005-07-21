package org.andromda.metafacades.uml14;

import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.ParameterDirectionKind;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

import java.util.Collection;
import java.util.Iterator;


/**
 * Metaclass facade implementation.
 */
public class ParameterFacadeLogicImpl
        extends ParameterFacadeLogic
{
    public ParameterFacadeLogicImpl(
            org.omg.uml.foundation.core.Parameter metaObject,
            String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.PARAMETER_NAME_MASK));
        return NameMasker.mask(
                super.handleGetName(),
                nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        Object owner = getOperation();

        if (owner == null)
        {
            owner = getEvent();
        }

        return owner;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    public String handleGetDefaultValue()
    {
        final Expression expression = metaObject.getDefaultValue();
        return (expression == null) ? "" : expression.getBody();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#sReturn()
     */
    public boolean handleIsReturn()
    {
        final ParameterDirectionKind kind = metaObject.getKind();
        return kind == null ? false : kind.equals(ParameterDirectionKindEnum.PDK_RETURN);
    }

    /**
     * @see org.andromda.metafacades.uml14.ParameterFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_NULLABLE);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getOperation()
     */
    protected Object handleGetOperation()
    {
        Operation parameterOperation = null;
        final Collection allOperations = UML14MetafacadeUtils.getModel().getCore().getOperation().refAllOfType();
        for (final Iterator iterator = allOperations.iterator(); iterator.hasNext() && parameterOperation == null;)
        {
            final Operation operation = (Operation)iterator.next();
            if (operation.getParameter().contains(metaObject))
            {
                parameterOperation = operation;
            }
        }
        return parameterOperation;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getEvent()
     */
    protected Object handleGetEvent()
    {
        Event parameterEvent = null;
        final Collection allEvents = UML14MetafacadeUtils.getModel().getStateMachines().getEvent().refAllOfType();
        for (final Iterator iterator = allEvents.iterator(); iterator.hasNext() && parameterEvent == null;)
        {
            final Event event = (Event)iterator.next();
            if (event.getParameter().contains(metaObject))
            {
                parameterEvent = event;
            }
        }
        return parameterEvent;
    }

    protected boolean handleIsReadable()
    {
        final ParameterDirectionKind kind = metaObject.getKind();
        return kind == null ? true :
                kind.equals(ParameterDirectionKindEnum.PDK_IN) || kind.equals(ParameterDirectionKindEnum.PDK_INOUT);
    }

    protected boolean handleIsWritable()
    {
        final ParameterDirectionKind kind = metaObject.getKind();
        return kind == null ? true :
                kind.equals(ParameterDirectionKindEnum.PDK_OUT) || kind.equals(ParameterDirectionKindEnum.PDK_INOUT);
    }
}