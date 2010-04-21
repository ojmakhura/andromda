package org.andromda.cartridges.spring.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringEntityOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation
 */
public class SpringEntityOperationLogicImpl
        extends SpringEntityOperationLogic
{
    
    /**
     * Public constructor for SpringEntityOperationLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation
     */
    public SpringEntityOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getImplementationOperationName(StringUtils.capitalize(this.getName()))
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @return getImplementationOperationName(StringUtils.capitalize(this.getCall()))
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * @return getImplementationOperationName(StringUtils.capitalize(this.getSignature()))
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationSignature()
     */
    protected String handleGetImplementationSignature()
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

    /**
     * @return getArguments().getType()
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationCall()
     */
    protected Collection<SpringCriteriaAttributeLogic> handleGetArguments()
    {
        Collection<SpringCriteriaAttributeLogic> arguments = new ArrayList<SpringCriteriaAttributeLogic>();
        for (ParameterFacade parameter : this.getArguments())
        {
            arguments.add((SpringCriteriaAttributeLogic)parameter.getType());
        }
        return arguments;
    }

}