package org.andromda.cartridges.meta.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeAttribute.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeAttribute
 */
public class MetafacadeAttributeLogicImpl
    extends MetafacadeAttributeLogic
{
    public MetafacadeAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeAttribute#getImplementationOperationName()
     */
    protected String handleGetImplementationOperationName()
    {
        return StringUtils.trimToEmpty(
            String.valueOf(
                this.getConfiguredProperty(
                    MetaGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll(
            "\\{0\\}",
            StringUtils.capitalize(this.getGetterName()));
    }
}