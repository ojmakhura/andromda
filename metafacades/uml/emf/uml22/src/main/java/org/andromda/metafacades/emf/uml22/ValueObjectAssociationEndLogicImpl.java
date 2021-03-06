package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.ValueObject;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ValueObjectAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.ValueObjectAssociationEnd
 */
public class ValueObjectAssociationEndLogicImpl
    extends ValueObjectAssociationEndLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ValueObjectAssociationEndLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of array names within many type
     * multiplicities.
     *
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    @Override
    protected String handleGetGetterSetterTypeName()
    {
        String name = super.handleGetGetterSetterTypeName();
        if (this.isMany())
        {
            final boolean useArrays =
                Boolean.valueOf(
                    String.valueOf(
                        this.getConfiguredProperty(UMLMetafacadeProperties.USE_ARRAYS_FOR_MULTIPLICITIES_OF_TYPE_MANY)))
                       .booleanValue();
            if (useArrays)
            {
                if (this.handleGetType() != null)
                {
                    name = this.getType().getFullyQualifiedArrayName();
                }
            }
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.ValueObjectAssociationEnd#isValueObjectType()
     */
    @Override
    protected boolean handleIsValueObjectType()
    {
        return this.getType() instanceof ValueObject;
    }
}
