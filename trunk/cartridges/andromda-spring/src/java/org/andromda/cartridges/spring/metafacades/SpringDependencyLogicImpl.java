package org.andromda.cartridges.spring.metafacades;

import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringDependency.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringDependency
 */
public class SpringDependencyLogicImpl
    extends SpringDependencyLogic
    implements org.andromda.cartridges.spring.metafacades.SpringDependency
{
    // ---------------- constructor -------------------------------

    public SpringDependencyLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationConstantName()
     */
    protected String handleGetTransformationConstantName()
    {
        return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX
            + getName().toUpperCase();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationMethodName()
     */
    protected String handleGetTransformationMethodName()
    {
        return SpringGlobals.TRANSFORMATION_METHOD_PREFIX
            + StringUtils.capitalize(getName());
    }

    /**
     * The suffix for the transformation annonymous name.
     */
    private static final String TRANSFORMATION_ANONYMOUS_NAME_SUFFIX = "_TRANSFORMER";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationAnonymousName()
     */
    protected String handleGetTransformationAnonymousName()
    {
        return getName().toUpperCase() + TRANSFORMATION_ANONYMOUS_NAME_SUFFIX;
    }
}
