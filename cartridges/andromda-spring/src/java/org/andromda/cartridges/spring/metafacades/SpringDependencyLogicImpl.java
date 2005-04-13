package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringDependency.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringDependency
 */
public class SpringDependencyLogicImpl
        extends SpringDependencyLogic
{
    // ---------------- constructor -------------------------------

    public SpringDependencyLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationConstantName()
     */
    protected String handleGetTransformationConstantName()
    {
        return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX + getName().toUpperCase();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationMethodName()
     */
    protected String handleGetTransformationMethodName()
    {
        return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(getName());
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
        final Collection sourceDependencies = targetElement.getSourceDependencies();
        if (sourceDependencies != null && !sourceDependencies.isEmpty())
        {
            circularReference = CollectionUtils.find(sourceDependencies, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null && dependency.getTargetElement().equals(sourceElement);
                }
            }) != null;
        }
        return circularReference;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationMethodName()
     */
    protected String handleGetTransformationToCollectionMethodName()
    {
        return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName()) +
                SpringGlobals.TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getDaoName()
     */
    protected String handleGetDaoName()
    {
        return this.getDaoNamePattern().replaceAll("\\{0\\}", this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#PROPERTY_DAO_PATTERN}
     *
     * @return the DAO name pattern.
     */
    private String getDaoNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.PROPERTY_DAO_PATTERN));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getDaoGetterName()
     */
    protected String handleGetDaoGetterName()
    {
        return "get" + StringUtils.capitalize(this.getDaoName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getDaoSetterName()
     */
    protected String handleGetDaoSetterName()
    {
        return "set" + StringUtils.capitalize(this.getDaoName());
    }
}
