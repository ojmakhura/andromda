package org.andromda.cartridges.spring.metafacades;

import java.util.Collection;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#isCircularReference()
     */
    protected boolean handleIsCircularReference()
    {
        boolean circularReference = false;
        final ModelElementFacade sourceElement = this.getSourceElement();
        final ModelElementFacade targetElement = this.getTargetElement();
        final Collection sourceDependencies = targetElement
            .getSourceDependencies();
        if (sourceDependencies != null && !sourceDependencies.isEmpty())
        {
            circularReference = CollectionUtils.find(
                sourceDependencies,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        return dependency != null
                            && dependency.getTargetElement().equals(
                                sourceElement);
                    }
                }) != null;
        }
        return circularReference;
    }
}
