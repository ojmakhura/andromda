package org.andromda.cartridges.spring.metafacades;

import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringEntityOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation
 */
public class SpringEntityOperationLogicImpl
        extends SpringEntityOperationLogic
{
    
    public SpringEntityOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationName()
     */
    protected java.lang.String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationCall()
     */
    protected java.lang.String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationSignature()
     */
    protected java.lang.String handleGetImplementationSignature()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getSignature()));
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * SpringGlobals#IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(
                SpringGlobals.IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll("\\{0\\}", replacement);
    }

}