package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EnumerationLiteralFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 * @author Bob Fields
 */
public class EnumerationLiteralFacadeLogicImpl
    extends EnumerationLiteralFacadeLogic
{
    public EnumerationLiteralFacadeLogicImpl(
        final org.eclipse.uml2.EnumerationLiteral metaObjectIn,
        final String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.ModelElementFacadeLogic#handleGetName()
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
        return StringUtils.trimToEmpty(this.getName(modelValue));
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.EnumerationLiteralFacade#getEnumerationValue()
     */
    @Override
    protected String handleGetEnumerationValue() 
    {
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