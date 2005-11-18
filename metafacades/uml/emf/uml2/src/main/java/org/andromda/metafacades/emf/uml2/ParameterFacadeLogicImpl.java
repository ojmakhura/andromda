package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.ParameterDirectionKind;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ParameterFacade.
 *
 * @see org.andromda.metafacades.uml.ParameterFacade
 */
public class ParameterFacadeLogicImpl
    extends ParameterFacadeLogic
{
    public ParameterFacadeLogicImpl(
        org.eclipse.uml2.Parameter metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    protected java.lang.String handleGetDefaultValue()
    {
        return metaObject.getDefault();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReturn()
     */
    protected boolean handleIsReturn()
    {
        return metaObject.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return false;
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
     * @see org.andromda.metafacades.uml.ParameterFacade#isDefaultValuePresent()
     */
    protected boolean handleIsDefaultValuePresent()
    {
        return StringUtils.isNotBlank(this.getDefaultValue());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInParameter()
     */
    protected boolean handleIsInParameter()
    {
        return metaObject.getDirection().equals(ParameterDirectionKind.IN_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isOutParameter()
     */
    protected boolean handleIsOutParameter()
    {
        return metaObject.getDirection().equals(ParameterDirectionKind.OUT_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInoutParameter()
     */
    protected boolean handleIsInoutParameter()
    {
        return metaObject.getDirection().equals(ParameterDirectionKind.INOUT_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getOperation()
     */
    protected java.lang.Object handleGetOperation()
    {
        return metaObject.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getEvent()
     */
    protected java.lang.Object handleGetEvent()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        return this.metaObject.getType();
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
}