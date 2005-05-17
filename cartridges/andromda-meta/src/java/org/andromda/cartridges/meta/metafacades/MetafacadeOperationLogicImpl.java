package org.andromda.cartridges.meta.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeOperation.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeOperation
 */
public class MetafacadeOperationLogicImpl
    extends MetafacadeOperationLogic
{
    // ---------------- constructor -------------------------------
    public MetafacadeOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeOperation#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return StringUtils.trimToEmpty(
            String.valueOf(
                this.getConfiguredProperty(
                    MetaGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll(
            "\\{0\\}",
            StringUtils.capitalize(this.getName()));
    }
}