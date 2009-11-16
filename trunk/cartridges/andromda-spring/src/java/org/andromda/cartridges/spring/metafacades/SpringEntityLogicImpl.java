package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.cartridges.spring.SpringHibernateUtils;
import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityQueryOperation;
import org.andromda.metafacades.uml.EnumerationFacade;
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
    /**
     * Public constructor for SpringEntityLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity
     */
    public SpringEntityLogicImpl(
        Object metaObject,
        String context)
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
    private static final Collection<String> INHERITANCE_STRATEGIES = new ArrayList<String>();

    static
    {
        INHERITANCE_STRATEGIES.add(INHERITANCE_STRATEGY_CLASS);
        INHERITANCE_STRATEGIES.add(INHERITANCE_STRATEGY_SUBCLASS);
        INHERITANCE_STRATEGIES.add(INHERITANCE_STRATEGY_CONCRETE);
        INHERITANCE_STRATEGIES.add(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @return getDaoNamePattern().replaceAll("\\{0\\}", getName())
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoName()
     */
    protected String handleGetDaoName()
    {
        return this.getDaoNamePattern().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#DAO_PATTERN}
     *
     * @return the DAO name pattern.
     */
    private String getDaoNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.DAO_PATTERN));
    }

    /**
     * @return fullyQualifiedName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoName()
     */
    protected String handleGetFullyQualifiedDaoName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getDaoName());
    }

    /**
     * @return daoImplementationName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoImplementationName()
     */
    protected String handleGetDaoImplementationName()
    {
        return this.getDaoImplementationNamePattern().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#DAO_IMPLEMENTATION_PATTERN}
     *
     * @return the DAO implementation name pattern.
     */
    private String getDaoImplementationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.DAO_IMPLEMENTATION_PATTERN));
    }

    /**
     * @return fullyQualifiedDaoImplementationName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoImplementationName()
     */
    protected String handleGetFullyQualifiedDaoImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getDaoImplementationName());
    }

    /**
     * @return DaoBaseName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBaseName()
     */
    protected String handleGetDaoBaseName()
    {
        return this.getDaoBaseNamePattern().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Gets the value of the {@link SpringGlobals#DAO_BASE_PATTERN}
     *
     * @return the DAO base name pattern.
     */
    private String getDaoBaseNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.DAO_BASE_PATTERN));
    }

    /**
     * @return FullyQualifiedDaoBaseName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoBaseName()
     */
    protected String handleGetFullyQualifiedDaoBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getDaoBaseName());
    }

    /**
     * @return EntityImplementationName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getEntityImplementationName()
     */
    protected String handleGetEntityImplementationName()
    {
        return this.getEntityName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @return FullyQualifiedEntityImplementationName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityImplementationName()
     */
    protected String handleGetFullyQualifiedEntityImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @param targetSuffix 
     * @return BeanName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getBeanName(boolean)
     */
    protected String handleGetBeanName(boolean targetSuffix)
    {
        final String beanName = StringUtils.uncapitalize(StringUtils.trimToEmpty(this.getName()));
        StringBuilder beanNameBuffer = new StringBuilder(String.valueOf(this.getConfiguredProperty(SpringGlobals.BEAN_NAME_PREFIX))); 
        beanNameBuffer.append(this.getDaoNamePattern().replaceAll("\\{0\\}", beanName));
        if (targetSuffix)
        {
            beanNameBuffer.append(SpringGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanNameBuffer.toString();
    }

    /**
     * @return EntityName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        final String entityNamePattern = (String)this.getConfiguredProperty("entityNamePattern");
        return MessageFormat.format(
            entityNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @return FullyQualifiedEntityName
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            null);
    }

    /**
     * @return Object Root
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getRoot()
     */
    protected Object handleGetRoot()
    {
        GeneralizableElementFacade generalization = this;
        for (
            ; generalization.getGeneralization() != null && generalization instanceof SpringEntity;
            generalization = generalization.getGeneralization())
        {
            ;
        }
        return generalization;
    }

    /**
     * @return IsDaoBusinessOperationsPresent
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoBusinessOperationsPresent()
     */
    protected boolean handleIsDaoBusinessOperationsPresent()
    {
        return this.getDaoBusinessOperations() != null && !this.getDaoBusinessOperations().isEmpty();
    }

    /**
     * @return DaoBusinessOperations
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBusinessOperations()
     */
    protected Collection<OperationFacade> handleGetDaoBusinessOperations()
    {
        // operations that are not finders and static
        Collection<EntityQueryOperation> finders = this.getQueryOperations();
        Collection<OperationFacade> operations = this.getOperations();

        Collection<OperationFacade> nonFinders = CollectionUtils.subtract(operations, finders);
        return new FilteredCollection(nonFinders)
            {
                public boolean evaluate(Object object)
                {
                    return ((OperationFacade)object).isStatic();
                }
            };
    }

    /**
     * @return getValueObjectReferences(false)
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getValueObjectReferences()
     */
    protected Collection<DependencyFacade> handleGetValueObjectReferences()
    {
        return this.getValueObjectReferences(false);
    }

    /**
     * Retrieves the values object references for this entity.  If
     * <code>follow</code> is true, then all value object references
     * (including those that were inherited) will be retrieved.
     * @param follow 
     * @return ValueObject references
     */
    protected Collection<DependencyFacade> getValueObjectReferences(boolean follow)
    {
        final Collection<DependencyFacade> sourceDependencies = new ArrayList<DependencyFacade>(this.getSourceDependencies());
        if (follow)
        {
            for (
                GeneralizableElementFacade entity = this.getGeneralization(); entity != null;
                entity = entity.getGeneralization())
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
                        valid = element.isDataType() || element instanceof ValueObject || element instanceof EnumerationFacade;
                    }
                    return valid;
                }
            };
    }

    /**
     * @return getValueObjectReferences(true)
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getAllValueObjectReferences()
     */
    protected Collection<DependencyFacade> handleGetAllValueObjectReferences()
    {
        return this.getValueObjectReferences(true);
    }

    /**
     * @return IsDaoImplementationRequired
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoImplementationRequired()
     */
    protected boolean handleIsDaoImplementationRequired()
    {
        return !this.getValueObjectReferences().isEmpty() || !this.getDaoBusinessOperations().isEmpty() ||
        !this.getQueryOperations(true).isEmpty();
    }

    /**
     * The suffix given to the no transformation constant. "NONE"
     */
    private static final String NO_TRANSFORMATION_CONSTANT_SUFFIX = "NONE";

    /**
     * @return TRANSFORMATION_CONSTANT_PREFIX + NO_TRANSFORMATION_CONSTANT_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoNoTransformationConstantName()
     */
    protected String handleGetDaoNoTransformationConstantName()
    {
        return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX + NO_TRANSFORMATION_CONSTANT_SUFFIX;
    }

    /**
     * Common routine to check inheritance.
     * @param inheritance 
     * @return inheritance.equals(getHibernateInheritanceStrategy())
     */
    protected boolean checkHibInheritance(String inheritance)
    {
        return inheritance.equals(getHibernateInheritanceStrategy());
    }

    /**
     * @return checkHibInheritance(INHERITANCE_STRATEGY_CLASS)
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceClass()
     */
    protected boolean handleIsHibernateInheritanceClass()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_CLASS);
    }

    /**
     * @return checkHibInheritance(INHERITANCE_STRATEGY_INTERFACE)
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceInterface()
     */
    protected boolean handleIsHibernateInheritanceInterface()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @return checkHibInheritance(INHERITANCE_STRATEGY_SUBCLASS)
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceSubclass()
     */
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_SUBCLASS);
    }

    /**
     * @return checkHibInheritance(INHERITANCE_STRATEGY_CONCRETE)
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
     * @return superEntity.getHibernateInheritanceStrategy()
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateInheritanceStrategy()
     */
    protected String handleGetHibernateInheritanceStrategy()
    {
        String inheritance = this.getInheritance(this);
        for (SpringEntity superEntity = this.getSpringSuperEntity(); superEntity != null && StringUtils.isBlank(inheritance);)
        {
            inheritance = superEntity.getHibernateInheritanceStrategy();
        }
        inheritance = inheritance != null ? inheritance.toLowerCase() : null;
        if (StringUtils.isBlank(inheritance) || !INHERITANCE_STRATEGIES.contains(inheritance))
        {
            inheritance = this.getDefaultInheritanceStrategy();
        }
        return inheritance;
    }

    /**
     * Gets the default hibernate inheritance strategy.
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
     * @return IsRequiresHibernateMapping
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isRequiresHibernateMapping()
     */
    protected boolean handleIsRequiresHibernateMapping()
    {
        final SpringEntity superEntity = this.getSpringSuperEntity();
        return
            SpringHibernateUtils.mapSubclassesInSeparateFile(
                (String)this.getConfiguredProperty(SpringGlobals.HIBERNATE_MAPPING_STRATEGY)) ||
            this.isRoot() &&
            (
                !this.isHibernateInheritanceInterface() || this.getSpecializations().isEmpty() ||
                (superEntity != null && superEntity.isHibernateInheritanceInterface())
            );
    }

    /**
     * Indicates if this entity as a <code>root</code> entity (meaning it doesn't specialize anything).
     */
    private boolean isRoot()
    {
        final SpringEntity superEntity = this.getSpringSuperEntity();
        boolean abstractConcreteEntity =
            (this.isHibernateInheritanceConcrete() || this.isHibernateInheritanceInterface()) && this.isAbstract();
        return (
            this.getSpringSuperEntity() == null ||
            (superEntity.isHibernateInheritanceInterface() || superEntity.isHibernateInheritanceConcrete())
        ) && !abstractConcreteEntity;
    }

    /**
     * Gets the super entity for this entity (if one exists). If a generalization does not exist OR if it's not an
     * instance of SpringEntity then return null.
     *
     * @return the super entity or null if one doesn't exist.
     */
    private SpringEntity getSpringSuperEntity()
    {
        SpringEntity superEntity = null;
        if (this.getGeneralization() != null && this.getGeneralization() instanceof SpringEntity)
        {
            superEntity = (SpringEntity)this.getGeneralization();
        }
        return superEntity;
    }
    
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getAttributeEmbeddedValueList()
     */
    @Override
    protected String handleGetAttributeEmbeddedValueList()
    {
        final StringBuilder buffer = new StringBuilder();
        for (final Iterator<AttributeFacade> iterator = this.getEmbeddedValues().iterator(); iterator.hasNext();)
        {
            final AttributeFacade attribute = iterator.next();
            final String name = attribute.getName();
            if (StringUtils.isNotBlank(name))
            {
                buffer.append('\"').append(name).append('\"');
                if (iterator.hasNext())
                {
                    buffer.append(", ");
                }
            }
        }
        return buffer.toString();
    }
    
    /**
     * @return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty("richClient"))).equalsIgnoreCase("true")
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isRichClient()
     */
    protected boolean handleIsRichClient() 
    {
        String richClient =
            StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty("richClient")));

        return "true".equalsIgnoreCase(richClient);
    }
    
}