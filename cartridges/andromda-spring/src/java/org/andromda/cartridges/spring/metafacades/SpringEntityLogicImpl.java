package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ValueObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringEntity.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringEntity
 */
public class SpringEntityLogicImpl
        extends SpringEntityLogic
{
    
    public SpringEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Value for one Table per root class
     */
    private static final String INHERITANCE_STRATEGY_CLASS = "class";

    /**
     * Value for joined-subclass
     */
    private static final String INHERITANCE_STRATEGY_SUBCLASS = "subclass";

    /**
     * Value for one Table per concrete class
     */
    private static final String INHERITANCE_STRATEGY_CONCRETE = "concrete";

    /**
     * Value make Entity an interface, delegate attributes to subclasses.
     */
    private static final String INHERITANCE_STRATEGY_INTERFACE = "interface";

    /**
     * Stores the valid inheritance strategies.
     */
    private static final Collection inheritanceStrategies = new ArrayList();

    static
    {
        inheritanceStrategies.add(INHERITANCE_STRATEGY_CLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_SUBCLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_CONCRETE);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoName()
     */
    protected java.lang.String handleGetDaoName()
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
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoName()
     */
    protected java.lang.String handleGetFullyQualifiedDaoName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getDaoName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoImplementationName()
     */
    protected java.lang.String handleGetDaoImplementationName()
    {
        return this.getDaoImplementationNamePattern().replaceAll("\\{0\\}", this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#PROPERTY_DAO_IMPLEMENTATION_PATTERN}
     *
     * @return the DAO implementation name pattern.
     */
    private String getDaoImplementationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.PROPERTY_DAO_IMPLEMENTATION_PATTERN));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedDaoImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getDaoImplementationName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBaseName()
     */
    protected java.lang.String handleGetDaoBaseName()
    {
        return this.getDaoBaseNamePattern().replaceAll("\\{0\\}", this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#PROPERTY_DAO_BASE_PATTERN}
     *
     * @return the DAO base name pattern.
     */
    private String getDaoBaseNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.PROPERTY_DAO_BASE_PATTERN));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoBaseName()
     */
    protected java.lang.String handleGetFullyQualifiedDaoBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getDaoBaseName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getImplementationName()
     */
    protected java.lang.String handleGetEntityImplementationName()
    {
        return this.getEntityName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedEntityImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getEntityName(),
                SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getBeanName(boolean)
     */
    protected java.lang.String handleGetBeanName(boolean targetSuffix)
    {
        final String beanName = StringUtils.uncapitalize(StringUtils.trimToEmpty(this.getName()));
        final StringBuffer beanNameBuffer = new StringBuffer(this.getDaoNamePattern().replaceAll("\\{0\\}", beanName));
        if (targetSuffix)
        {
            beanNameBuffer.append(SpringGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanNameBuffer.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        final String entityNamePattern = (String)this.getConfiguredProperty("entityNamePattern");
        return MessageFormat.format(entityNamePattern, new Object[]{StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this.getPackageName(), this.getEntityName(), null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getRoot()
     */
    protected Object handleGetRoot()
    {
        GeneralizableElementFacade generalization = this;
        for (;
             generalization.getGeneralization() != null && generalization instanceof SpringEntity;
             generalization = generalization.getGeneralization())
            ;
        return generalization;
    }

    /**
     * The namespace property storing the hibernate default-cascade value for an entity.
     */
    private static final String HIBERNATE_DEFAULT_CASCADE = "hibernateDefaultCascade";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateDefaultCascade()
     */
    protected String handleGetHibernateDefaultCascade()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(HIBERNATE_DEFAULT_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoBusinessOperationsPresent()
     */
    protected boolean handleIsDaoBusinessOperationsPresent()
    {
        return this.getDaoBusinessOperations() != null && !this.getDaoBusinessOperations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBusinessOperations()
     */
    protected Collection handleGetDaoBusinessOperations()
    {
        // operations that are not finders and static
        Collection finders = this.getQueryOperations();
        Collection operations = this.getOperations();

        Collection nonFinders = CollectionUtils.subtract(operations, finders);
        return new FilteredCollection(nonFinders)
        {
            public boolean evaluate(Object object)
            {
                return ((OperationFacade)object).isStatic();
            }
        };
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getValueObjectReferences()
     */
    protected Collection handleGetValueObjectReferences()
    {
        return this.getValueObjectReferences(false);
    }
    
    /**
     * Retrieves the values object references for this entity.  If
     * <code>follow</code> is true, then all value object references
     * (including those that were inherited) will be retrieved.
     */
    protected Collection getValueObjectReferences(boolean follow)
    {
        final Collection sourceDependencies = new ArrayList(this.getSourceDependencies());
        if (follow)
        {
            for (GeneralizableElementFacade entity = this.getGeneralization(); entity != null; entity = entity.getGeneralization())
            {
                sourceDependencies.addAll(entity.getSourceDependencies());
            }
        }
        return new FilteredCollection(sourceDependencies)
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                Object targetElement = ((DependencyFacade)object).getTargetElement();
                if (targetElement instanceof ClassifierFacade)
                {
                    ClassifierFacade element = (ClassifierFacade)targetElement;
                    valid = element.isDataType() || element instanceof ValueObject;
                }
                return valid;
            }
        };
    }
    
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getAllValueObjectReferences()
     */
    protected Collection handleGetAllValueObjectReferences()
    {
        return this.getValueObjectReferences(true);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoImplementationRequired()
     */
    protected boolean handleIsDaoImplementationRequired()
    {
        return !this.getValueObjectReferences().isEmpty() || !this.getDaoBusinessOperations().isEmpty() || !this.getQueryOperations(
                true).isEmpty();
    }

    /**
     * The suffix given to the no transformation constant.
     */
    private static final String NO_TRANSFORMATION_CONSTANT_SUFFIX = "NONE";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoNoTransformationConstantName()
     */
    protected String handleGetDaoNoTransformationConstantName()
    {
        return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX + NO_TRANSFORMATION_CONSTANT_SUFFIX;
    }

    /**
     * Common routine to check inheritance.
     */
    protected boolean checkHibInheritance(String inheritance)
    {
        return inheritance.equals(getHibernateInheritanceStrategy());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceClass()
     */
    protected boolean handleIsHibernateInheritanceClass()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_CLASS);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceInterface()
     */
    protected boolean handleIsHibernateInheritanceInterface()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceSubclass()
     */
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_SUBCLASS);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceConcrete()
     */
    protected boolean handleIsHibernateInheritanceConcrete()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_CONCRETE);
    }

    /**
     * Stores the default hibernate inheritance strategy.
     */
    private static final String INHERITANCE_STRATEGY = "hibernateInheritanceStrategy";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateInheritanceStrategy()
     */
    protected String handleGetHibernateInheritanceStrategy()
    {
        String inheritance = this.getInheritance(this);
        for (SpringEntity superEntity = this.getSuperEntity();
             superEntity != null && StringUtils.isBlank(inheritance);)
        {
            inheritance = superEntity.getHibernateInheritanceStrategy();
        }
        if (StringUtils.isBlank(inheritance) || !inheritanceStrategies.contains(inheritance))
        {
            inheritance = this.getDefaultInheritanceStrategy();
        }
        return inheritance;
    }

    /**
     * Gets the default hibernate inhertance strategy.
     *
     * @return the default hibernate inheritance strategy.
     */
    private String getDefaultInheritanceStrategy()
    {
        return String.valueOf(this.getConfiguredProperty(INHERITANCE_STRATEGY));
    }

    /**
     * Return the inheritance tagged value for for given <code>entity</code>.
     *
     * @param the SpringEntity from which to retrieve the inheritance tagged value.
     * @return String inheritance tagged value.
     */
    private String getInheritance(SpringEntity entity)
    {
        String inheritance = null;
        if (entity != null)
        {
            Object value = entity.findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_INHERITANCE);
            if (value != null)
            {
                inheritance = String.valueOf(value);
            }
        }
        return inheritance;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.SpringEntity#isRequiresHibernateMapping()
     */
    protected boolean handleIsRequiresHibernateMapping()
    {
        final SpringEntity superEntity = this.getSuperEntity();
        return this.isRoot() &&
                (!this.isHibernateInheritanceInterface() ||
                (superEntity != null && superEntity.isHibernateInheritanceInterface()));
    }

    /**
     * Indicates if this entity as a <code>root</code> entity (meaning it doesn't specialize anything).
     */
    private boolean isRoot()
    {
        final SpringEntity superEntity = this.getSuperEntity();
        boolean abstractConcreteEntity = (this.isHibernateInheritanceConcrete() ||
                this.isHibernateInheritanceInterface()) &&
                this.isAbstract();
        return (this.getSuperEntity() == null ||
                (superEntity.isHibernateInheritanceInterface() || superEntity.isHibernateInheritanceConcrete())) &&
                !abstractConcreteEntity;
    }

    /**
     * Gets the super entity for this entity (if one exists). If a generalization does not exist OR if it's not an
     * instance of SpringEntity then return null.
     *
     * @return the super entity or null if one doesn't exist.
     */
    private SpringEntity getSuperEntity()
    {
        SpringEntity superEntity = null;
        if (this.getGeneralization() != null && this.getGeneralization() instanceof SpringEntity)
        {
            superEntity = (SpringEntity)this.getGeneralization();
        }
        return superEntity;
    }

}