package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.datatypes.Expression;
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
        return NameMasker.mask(super.handleGetName(), nameMask);
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
        Object owner = this.getOperation();

        if (owner == null)
        {
            owner = this.getEvent();
        }

        return owner;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    public String handleGetDefaultValue()
    {
        final Expression expression = this.metaObject.getDefaultValue();
        return expression == null ? "" : expression.getBody();
    }
    
    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isDefaultValuePresent()
     */
    public boolean handleIsDefaultValuePresent()
    {
        return StringUtils.isNotBlank(this.getDefaultValue());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    protected Object handleGetType()
    {
        return this.metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReturn()
     */
    public boolean handleIsReturn()
    {
        return ParameterDirectionKindEnum.PDK_RETURN.equals(this.metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
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
            if (operation.getParameter().contains(this.metaObject))
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
            if (event.getParameter().contains(this.metaObject))
            {
                parameterEvent = event;
            }
        }
        return parameterEvent;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReadable()
     */
    protected boolean handleIsReadable()
    {
        return this.isInParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isWritable()
     */
    protected boolean handleIsWritable()
    {
        return this.isOutParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInParameter()
     */
    protected boolean handleIsInParameter()
    {
        return ParameterDirectionKindEnum.PDK_IN.equals(this.metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isOutParameter()
     */
    protected boolean handleIsOutParameter()
    {
        return ParameterDirectionKindEnum.PDK_OUT.equals(this.metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInoutParameter()
     */
    protected boolean handleIsInoutParameter()
    {
        return ParameterDirectionKindEnum.PDK_INOUT.equals(this.metaObject.getKind());
    }
}