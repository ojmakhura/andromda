package org.andromda.cartridges.spring.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.cartridges.spring.CartridgeSpringHibernateUtils;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringDependency.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringDependency
 */
public class SpringDependencyLogicImpl
    extends SpringDependencyLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for SpringDependencyLogicImpl
     * @param metaObject
     * @param context
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency
     */
    public SpringDependencyLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX + this.getName().toUpperCase()
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationConstantName()
     */
    protected String handleGetTransformationConstantName()
    {
        return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX + this.getName().toUpperCase();
    }

    /**
     * @return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName())
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationMethodName()
     */
    protected String handleGetTransformationMethodName()
    {
        return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName());
    }

    /**
     * The suffix for the transformation annonymous name.
     */
    private static final String TRANSFORMATION_ANONYMOUS_NAME_SUFFIX = "_TRANSFORMER";

    /**
     * @return getName().toUpperCase() + TRANSFORMATION_ANONYMOUS_NAME_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationAnonymousName()
     */
    protected String handleGetTransformationAnonymousName()
    {
        return this.getName().toUpperCase() + TRANSFORMATION_ANONYMOUS_NAME_SUFFIX;
    }

    /**
     * @return dependency.getTargetElement().equals(sourceElement)
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#isCircularReference()
     */
    protected boolean handleIsCircularReference()
    {
        boolean circularReference = false;
        final ModelElementFacade sourceElement = this.getSourceElement();
        final ModelElementFacade targetElement = this.getTargetElement();
        final Collection<DependencyFacade> sourceDependencies = targetElement.getSourceDependencies();
        if (sourceDependencies != null && !sourceDependencies.isEmpty())
        {
            circularReference =
                CollectionUtils.find(
                    sourceDependencies,
                    new Predicate()
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

    private boolean isXmlPersistenceActive()
    {
        return CartridgeSpringHibernateUtils.isXmlPersistenceActive(
            (String)this.getConfiguredProperty(SpringGlobals.HIBERNATE_VERSION),
            (String)this.getConfiguredProperty(SpringGlobals.HIBERNATE_XML_PERSISTENCE));
    }

    /**
     * @return incoming ValueObject references
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationConstantValue()
     */
    protected int handleGetTransformationConstantValue()
    {
        int value = 0;
        ModelElementFacade element = this.getSourceElement();
        if (element instanceof SpringEntity)
        {
            final List<SpringEntity> hierarchy = new ArrayList<SpringEntity>();
            for (SpringEntity entity = (SpringEntity)element; entity != null;
                entity = (SpringEntity)entity.getGeneralization())
            {
                hierarchy.add(entity);
            }
            boolean breakOut = false;
            for (int ctr = hierarchy.size() - 1; ctr >= 0; ctr--)
            {
                final SpringEntity generalization = hierarchy.get(ctr);
                //TODO Bug? generalization.getValueObjectReferences() can contain not only ClassifierFacades
                for (final Iterator referenceIterator = generalization.getValueObjectReferences().iterator();
                    referenceIterator.hasNext();)
                {
                    final Object reference = referenceIterator.next();
                    value++;
                    if (reference.equals(this))
                    {
                        breakOut = true;
                        break;
                    }
                }
                if (breakOut)
                {
                    break;
                }
            }
        }

        if (isXmlPersistenceActive())
        {
            value++;
        }

        return value;
    }

    /**
     * @return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + getName() + SpringGlobals.TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationToCollectionMethodName()
     */
    protected String handleGetTransformationToCollectionMethodName()
    {
        return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName()) +
        SpringGlobals.TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX;
    }

    /**
     * @return TRANSFORMATION_METHOD_PREFIX + capitalize(getName()) + TRANSFORMATION_TO_ARRAY_METHOD_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationToArrayMethodName()
     */
    protected String handleGetTransformationToArrayMethodName()
    {
        return SpringGlobals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName()) +
            SpringGlobals.TRANSFORMATION_TO_ARRAY_METHOD_SUFFIX;
    }

    /**
     * @return getDaoNamePattern().replaceAll("\\{0\\}", getName())
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getDaoName()
     */
    protected String handleGetDaoName()
    {
        return this.getDaoNamePattern().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#DAO_PATTERN}.
     *
     * @return the DAO name pattern.
     */
    private String getDaoNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.DAO_PATTERN));
    }

    /**
     * @return "get" + StringUtils.capitalize(this.getDaoName())
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getDaoGetterName()
     */
    protected String handleGetDaoGetterName()
    {
        return "get" + StringUtils.capitalize(this.getDaoName());
    }

    /**
     * @return "set" + StringUtils.capitalize(this.getDaoName())
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getDaoSetterName()
     */
    protected String handleGetDaoSetterName()
    {
        return "set" + StringUtils.capitalize(this.getDaoName());
    }

    /**
     * @return getTransformationToEntityMethodName() + TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationToEntityCollectionMethodName()
     */
    protected String handleGetTransformationToEntityCollectionMethodName()
    {
        return this.getTransformationToEntityMethodName() + SpringGlobals.TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX;
    }

    /**
     * The suffix for the transformation to entity method name.
     */
    private static final String TRANSFORMATION_TO_ENTITY_METHOD_NAME_SUFFIX = "ToEntity";

    /**
     * @return getName() + TRANSFORMATION_TO_ENTITY_METHOD_NAME_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getTransformationToEntityMethodName()
     */
    protected String handleGetTransformationToEntityMethodName()
    {
        return this.getName() + TRANSFORMATION_TO_ENTITY_METHOD_NAME_SUFFIX;
    }

    /**
     * The suffix for the value object to entity transformer. "Transformer"
     */
    private static final String VALUE_OBJECT_TO_ENTITY_TRANSFORMER_SUFFIX = "Transformer";

    /**
     * @return capitalize(this.getTransformationToEntityMethodName()) + VALUE_OBJECT_TO_ENTITY_TRANSFORMER_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringDependency#getValueObjectToEntityTransformerName()
     */
    protected String handleGetValueObjectToEntityTransformerName()
    {
        return StringUtils.capitalize(this.getTransformationToEntityMethodName()) +
        VALUE_OBJECT_TO_ENTITY_TRANSFORMER_SUFFIX;
    }
}