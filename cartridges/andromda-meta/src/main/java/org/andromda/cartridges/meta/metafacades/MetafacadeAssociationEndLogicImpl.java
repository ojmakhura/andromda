package org.andromda.cartridges.meta.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEnd.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEnd
 * @author Bob Fields
 */
public class MetafacadeAssociationEndLogicImpl
    extends MetafacadeAssociationEndLogic
{
    /**
     * @param metaObjectIn
     * @param context
     */
    public MetafacadeAssociationEndLogicImpl(
        Object metaObjectIn,
        String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEnd#getImplementationOperationName()
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