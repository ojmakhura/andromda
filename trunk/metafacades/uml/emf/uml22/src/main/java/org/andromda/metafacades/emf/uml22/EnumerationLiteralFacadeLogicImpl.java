package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.OpaqueExpression;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EnumerationLiteralFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 */
public class EnumerationLiteralFacadeLogicImpl
    extends EnumerationLiteralFacadeLogic
{
    private static final long serialVersionUID = 3244584657700009965L;

    /**
     * @param metaObject
     * @param context
     */
    public EnumerationLiteralFacadeLogicImpl(
        final EnumerationLiteral metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetName()
     */
    @Override
    protected String handleGetName()
    {
        return this.getName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getValue()
     */
    @Override
    protected String handleGetValue()
    {
        return this.getValue(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getName(boolean)
     */
    @Override
    protected String handleGetName(boolean modelName)
    {
        String name = super.handleGetName();
        final String mask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK));
        if (!modelName && StringUtils.isNotBlank(mask))
        {
            name = NameMasker.mask(name, mask);
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getValue(boolean)
     */
    @Override
    protected String handleGetValue(boolean modelValue)
    {
        String value = null;
        if (this.metaObject.getSpecification() != null && this.metaObject.getSpecification() instanceof OpaqueExpression)
        {
            OpaqueExpression expression = (OpaqueExpression)this.metaObject.getSpecification();
            if (expression.getBodies() != null && !expression.getBodies().isEmpty())
            {
                value = expression.getBodies().get(0);
            }
        }
        if (value == null)
        {
            value = this.getName(modelValue);
        }
        return StringUtils.trimToEmpty(value);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.EnumerationLiteralFacadeLogic#handleGetEnumerationValue()
     */
    @Override
    protected String handleGetEnumerationValue() {
        String value = this.getValue();
        if (StringUtils.isEmpty(value))
        {
            value = "\"\"";
        }
        if (value.indexOf('"')<0)
        {
            value = '\"' + value + '\"';
        }
        return value;
    }
}
