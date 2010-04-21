package org.andromda.cartridges.meta.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeAttribute.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeAttribute
 * @author Bob Fields
 */
public class MetafacadeAttributeLogicImpl
    extends MetafacadeAttributeLogic
{
    /**
     * @param metaObjectIn
     * @param context
     */
    public MetafacadeAttributeLogicImpl(
        Object metaObjectIn,
        String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeAttribute#getImplementationOperationName()
     */
    @Override
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