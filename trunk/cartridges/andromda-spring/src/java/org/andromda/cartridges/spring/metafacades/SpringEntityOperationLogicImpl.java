package org.andromda.cartridges.spring.metafacades;

import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringEntityOperation.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation
 */
public class SpringEntityOperationLogicImpl
    extends SpringEntityOperationLogic
    implements org.andromda.cartridges.spring.metafacades.SpringEntityOperation
{
    // ---------------- constructor -------------------------------

    public SpringEntityOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationName()
     */
    public java.lang.String handleGetImplementationName()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationCall()
     */
    public java.lang.String handleGetImplementationCall()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getCall());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationSignature()
     */
    public java.lang.String handleGetImplementationSignature()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getSignature());
    }

    /**
     * Retrieves the implementationNamePrefix property from the namespace.
     * 
     * @return the implementation name prefix
     */
    private String getImplementationNamePrefix()
    {
        return StringUtils
            .trimToEmpty(String
                .valueOf(this
                    .getConfiguredProperty(SpringGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX)));
    }

}