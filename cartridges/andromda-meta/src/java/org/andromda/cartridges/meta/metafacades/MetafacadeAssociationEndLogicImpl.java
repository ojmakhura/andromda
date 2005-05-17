package org.andromda.cartridges.meta.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEnd.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEnd
 */
public class MetafacadeAssociationEndLogicImpl
    extends MetafacadeAssociationEndLogic
{
    public MetafacadeAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEnd#getImplementationOperationName()
     */
    protected java.lang.String handleGetImplementationOperationName()
    {
        return StringUtils.trimToEmpty(
            String.valueOf(
                this.getConfiguredProperty(
                    MetaGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll(
            "\\{0\\}",
            StringUtils.capitalize(this.getGetterName()));
    }
}