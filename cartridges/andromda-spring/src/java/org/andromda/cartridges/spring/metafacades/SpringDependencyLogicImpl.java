package org.andromda.cartridges.spring.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringDependency.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringDependency
 */
public class SpringDependencyLogicImpl
       extends SpringDependencyLogic
       implements org.andromda.cartridges.spring.metafacades.SpringDependency
{
    // ---------------- constructor -------------------------------

    public SpringDependencyLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected String handleGetTransformationConstantName()
    {
        return "TRANSFORMATION_" + getName().toUpperCase();
    }

    protected String handleGetTransformationMethodName()
    {
        return "to" + StringUtils.capitalize(getName());
    }

    protected String handleGetTransformationAnonymousName()
    {
        return getName().toUpperCase() + "_TRANSFORMER";
    }
}
