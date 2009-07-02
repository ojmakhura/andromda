package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ParameterFacadeLogicImpl
    extends ParameterFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ParameterFacadeLogicImpl(
        Parameter metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.PARAMETER_NAME_MASK));
        String name = NameMasker.mask(
            super.handleGetName(),
            nameMask);
        // UML14 does not support multiplicity * on parameters
        /*final boolean templating = Boolean.parseBoolean(String.valueOf(
            this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)));
        final boolean arrayType = this.getType().isArrayType();
        if (this.handleIsMany() && this.isPluralizeParameterNames())
        {
            name = StringUtilsHelper.pluralize(name);
        }*/
        
        return name;
    }

    /*
     * Indicates whether or not we should pluralize association end names.
     *
     * @return true/false
    private boolean isPluralizeParameterNames()
    {
        final Object value = this.getConfiguredProperty(UMLMetafacadeProperties.PLURALIZE_PARAMETER_NAMES);
        return value != null && Boolean.valueOf(String.valueOf(value)).booleanValue();
    }
     */

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterName()
     */
    @Override
    protected String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getSetterName()
     */
    @Override
    protected String handleGetSetterName()
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
    @Override
    public String handleGetDefaultValue()
    {
        String defaultValue = null;
        final Expression expression = this.metaObject.getDefaultValue();
        //return expression == null ? "" : expression.getBody();
        if (expression != null)
        {
            defaultValue = expression.getBody();
        }
        // Put single or double quotes around default in case modeler forgot to do it. Most templates
        // declare Type parameter = $parameter.defaultValue, requiring quotes around the value
        if (StringUtils.isNotEmpty(defaultValue))
        {
            String typeName = this.metaObject.getType().getName();
            if (typeName.equals("String") && defaultValue.indexOf('"')<0)
            {
                defaultValue = '"' + defaultValue + '"';
            }
            else if ((typeName.equals("char") || typeName.equals("Character"))
                && defaultValue.indexOf("'")<0)
            {
                defaultValue = "'" + defaultValue.charAt(0) + "'";
            }
            //if (!defaultValue.equals("")) System.out.println("Attribute.handleGetDefaultValue " + this.getName() + " typeName=" + typeName + " defaultValue=" + defaultValue + " upper=" + this.metaObject.getUpper());
        }
        if (defaultValue==null) defaultValue="";
        return defaultValue;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isDefaultValuePresent()
     */
    @Override
    public boolean handleIsDefaultValuePresent()
    {
        return StringUtils.isNotBlank(this.getDefaultValue());
    }

    /**
     * @return this.getType().getFullyQualifiedName()
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    protected String handleGetGetterSetterTypeName()
    {
        return this.getType().getFullyQualifiedName();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    @Override
    protected Classifier handleGetType()
    {
        return this.metaObject.getType();
    }

    /**
     * UML2 Only: Returns "NONE" always.
     * @return "NONE"
     * @see org.andromda.metafacades.uml.ParameterFacade#isException()
     */
    @Override
    public String handleGetEffect()
    {
        return "NONE";
    }

    /**
     * UML2 Only: Returns false always.
     * @return false
     * @see org.andromda.metafacades.uml.ParameterFacade#isException()
     */
    @Override
    public boolean handleIsException()
    {
        return false;
    }

    /**
     * NOT IMPLEMENTED: UML2 only: returns false always
     * @return false
     * @see org.andromda.metafacades.uml.ParameterFacade#isOrdered()
     */
    @Override
    protected boolean handleIsOrdered()
    {
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReturn()
     */
    @Override
    public boolean handleIsReturn()
    {
        return ParameterDirectionKindEnum.PDK_RETURN.equals(this.metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    @Override
    protected boolean handleIsRequired()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_NULLABLE);
    }

    /**
     * @return !this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE)
     * @see org.andromda.metafacades.uml.ParameterFacade#isUnique()
     */
    @Override
    protected boolean handleIsUnique()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getOperation()
     */
    @Override
    protected Operation handleGetOperation()
    {
        Operation parameterOperation = null;
        final Collection<Operation> allOperations = UML14MetafacadeUtils.getModel().getCore().getOperation().refAllOfType();
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
    @Override
    protected Event handleGetEvent()
    {
        Event parameterEvent = null;
        final Collection<Event> allEvents = UML14MetafacadeUtils.getModel().getStateMachines().getEvent().refAllOfType();
        for (final Iterator<Event> iterator = allEvents.iterator(); iterator.hasNext() && parameterEvent == null;)
        {
            final Event event = iterator.next();
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
    @Override
    protected boolean handleIsReadable()
    {
        return this.isInParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isWritable()
     */
    @Override
    protected boolean handleIsWritable()
    {
        return this.isOutParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInParameter()
     */
    @Override
    protected boolean handleIsInParameter()
    {
        return ParameterDirectionKindEnum.PDK_IN.equals(this.metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isOutParameter()
     */
    @Override
    protected boolean handleIsOutParameter()
    {
        return ParameterDirectionKindEnum.PDK_OUT.equals(this.metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInoutParameter()
     */
    @Override
    protected boolean handleIsInoutParameter()
    {
        return ParameterDirectionKindEnum.PDK_INOUT.equals(this.metaObject.getKind());
    }

    /**
     * Get the UML upper multiplicity
     * Not implemented for UML1.4
     * @return false always
     * @see org.andromda.metafacades.uml.ParameterFacade#isMany()
     */
    //@Override
    protected boolean handleIsMany()
    {
        return false;
     }

    /**
     * Get the UML upper multiplicity
     * Not implemented for UML1.4
     * @return 1 always
     * @see org.andromda.metafacades.uml.ParameterFacade#getUpper()
     */
    protected int handleGetUpper()
    {
        //throw new UnsupportedOperationException("'upper' is not a UML1.4 feature");
        return 1;
     }

    /**
     * Get the UML lower multiplicity
     * Not implemented for UML1.4
     * @return 0 always
     * @see org.andromda.metafacades.uml.ParameterFacade#getLower()
     */
    protected int handleGetLower()
    {
        //throw new UnsupportedOperationException("'lower' is not a UML1.4 feature");
        return 0;
    }
}