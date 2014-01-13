package org.andromda.cartridges.spring.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;

import org.andromda.cartridges.spring.SpringProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringEntityOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation
 */
public class SpringEntityOperationLogicImpl
        extends SpringEntityOperationLogic
{
    private static final long serialVersionUID = 34L;
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

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#isPrePersist()
     */
    @Override
    protected boolean handleIsPrePersist()
    {
        return this.hasStereotype(SpringProfile.STEREOTYPE_PRE_PERSIST);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#isPostPersist()
     */
    @Override
    protected boolean handleIsPostPersist()
    {
        return this.hasStereotype(SpringProfile.STEREOTYPE_POST_PERSIST);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#isPreRemove()
     */
    @Override
    protected boolean handleIsPreRemove()
    {
        return this.hasStereotype(SpringProfile.STEREOTYPE_PRE_REMOVE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#isPostRemove()
     */
    @Override
    protected boolean handleIsPostRemove()
    {
        return this.hasStereotype(SpringProfile.STEREOTYPE_POST_REMOVE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#isPreUpdate()
     */
    @Override
    protected boolean handleIsPreUpdate()
    {
        return this.hasStereotype(SpringProfile.STEREOTYPE_PRE_UPDATE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#isPostUpdate()
     */
    @Override
    protected boolean handleIsPostUpdate()
    {
        return this.hasStereotype(SpringProfile.STEREOTYPE_POST_UPDATE);
    }
}