package org.andromda.cartridges.hibernate.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.cartridges.hibernate.HibernateUtils;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.ValueObject;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> Provides support for the hibernate inheritance strategies of class
 * (table per hierarchy), subclass (table per subclass in hierarchy) and
 * concrete (table per class). With concrete the strategy can be changed lower
 * down. Also provides for the root class being defined as an interface and the
 * attributes remapped to the subclasses. This is useful in the concrete case
 * because it has limitations in the associations.
 * </p>
 * <p> Also provides support for not generating the entity factory which is
 * useful when using subclass mode.
 * </p>
 *
 * @author Chad Brandon
 * @author Martin West
 * @author Carlos Cuenca
 * @author Peter Friese
 * @author Wouter Zoons
 * @author Bob Fields
 */
public class HibernateEntityLogicImpl
    extends HibernateEntityLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public HibernateEntityLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Value for one table per root class
     */
    private static final String INHERITANCE_STRATEGY_CLASS = "class";

    /**
     * Value for joined-subclass
     */
    private static final String INHERITANCE_STRATEGY_SUBCLASS = "subclass";

    /**
     * Value for one table per concrete class
     */
    private static final String INHERITANCE_STRATEGY_CONCRETE = "concrete";

    /**
     * Value make entity an interface, delegate attributes to subclasses.
     */
    private static final String INHERITANCE_STRATEGY_INTERFACE = "interface";

    /**
     * Value for one table per concrete class, (with union-subclass)
     */
    private static final String INHERITANCE_STRATEGY_UNION_SUBCLASS = "union-subclass";

    /**
     * Stores the valid inheritance strategies.
     */
    private static final Collection<String> inheritanceStrategies = new ArrayList<String>();

    static
    {
        inheritanceStrategies.add(INHERITANCE_STRATEGY_CLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_SUBCLASS);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_CONCRETE);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_INTERFACE);
        inheritanceStrategies.add(INHERITANCE_STRATEGY_UNION_SUBCLASS);
    }

    /**
     * Stores the default hibernate inheritance strategy.
     */
    private static final String INHERITANCE_STRATEGY = "hibernateInheritanceStrategy";

    /**
     * Stores the hibernate entity cache value.
     */
    private static final String HIBERNATE_ENTITY_CACHE = "hibernateEntityCache";

    /**
     * The namespace property storing the hibernate default-cascade value for an
     * entity.
     */
    private static final String HIBERNATE_DEFAULT_CASCADE = "hibernateDefaultCascade";

    /**
     * Namespace property storing the default hibernate generator class.
     */
    private static final String DEFAULT_HIBERNATE_GENERATOR_CLASS = "defaultHibernateGeneratorClass";

    /**
     * Represents a <em>foreign</em> Hibernate generator class.
     */
    private static final String HIBERNATE_GENERATOR_CLASS_FOREIGN = "foreign";

    /**
     * Represents an <em>assigned</em> Hibernate generator class.
     */
    private static final String HIBERNATE_GENERATOR_CLASS_ASSIGNED = "assigned";
    private static final String HIBERNATE_GENERATOR_CLASS_SEQUENCE = "sequence";

    /**
     * The namespace property for specifying a hibernate proxy for this entity.
     */
    private static final String HIBERNATE_PROXY = "hibernateProxy";

    /**
     * The "class" mapping name.
     */
    private static final String CLASS_MAPPING_NAME = "class";

    /**
     * The "joined-subclass" mapping name.
     */
    private static final String JOINED_SUBCLASS_MAPPING_NAME = "joined-subclass";

    /**
     * The "subclass" mapping name.
     */
    private static final String SUBCLASS_MAPPING_NAME = "subclass";

    /**
     * The "union-subclass" mapping name.
     */
    private static final String UNION_SUBCLASS_MAPPING_NAME = "union-subclass";

    /**
     * The pattern used to construct the DAO implementation name.
     */
    private static final String DAO_IMPLEMENTATION_PATTERN = "daoImplementationNamePattern";

    /**
     * The pattern used to construct the DAO base name.
     */
    private static final String DAO_BASE_PATTERN = "daoBaseNamePattern";

    /**
     * The property which stores the pattern defining the DAO default exception name.
     */
    private static final String DAO_DEFAULT_EXCEPTION_NAME_PATTERN = "daoDefaultExceptionNamePattern";

    /**
     * Return all the business operations (ones that are inherited as well as
     * directly on the entity).
     *
     * @return all business operations
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getAllBusinessOperations()
     */
    protected Collection<OperationFacade> handleGetAllBusinessOperations()
    {
        Collection<OperationFacade> result = this.getBusinessOperations();
        GeneralizableElementFacade general = this.getGeneralization();
        // Allow for Entities that inherit from a non-Entity ancestor
        if (general != null && general instanceof Entity)
        {
            Entity superElement = (Entity)general;
            result.addAll(superElement.getBusinessOperations());
            general = this.getGeneralization();
        }

        return result;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateInheritanceStrategy()
     */
    @Override
    protected String handleGetHibernateInheritanceStrategy()
    {
        String inheritance = HibernateEntityLogicImpl.getInheritance(this);

        for (HibernateEntity superEntity = this.getSuperEntity();
            (superEntity != null) && StringUtils.isBlank(inheritance);)
        {
            inheritance = superEntity.getHibernateInheritanceStrategy();
        }

        inheritance = inheritance != null ? inheritance.toLowerCase() : null;

        if (StringUtils.isBlank(inheritance) || !inheritanceStrategies.contains(inheritance))
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
     * @param entity the HibernateEntity from which to retrieve the inheritance tagged
     *        value.
     * @return String inheritance tagged value.
     */
    private static String getInheritance(HibernateEntity entity)
    {
        String inheritance = null;

        if (entity != null)
        {
            final Object value = entity.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INHERITANCE);
            if (value != null)
            {
                inheritance = String.valueOf(value);
            }
        }
        return inheritance;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    public List<ModelElementFacade> getProperties()
    {
        List<ModelElementFacade> properties = new ArrayList<ModelElementFacade>();
        properties.addAll(this.getAttributes());
        for (final AssociationEndFacade end : this.getAssociationEnds())
        {
            final AssociationEndFacade otherEnd = end.getOtherEnd();
            if (otherEnd.isNavigable() ||
                (end.isChild() && isForeignHibernateGeneratorClass()))
            {
                properties.add(otherEnd);
            }
        }
        return properties;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceClass()
     */
    @Override
    protected boolean handleIsHibernateInheritanceClass()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_CLASS);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceInterface()
     */
    @Override
    protected boolean handleIsHibernateInheritanceInterface()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceSubclass()
     */
    @Override
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_SUBCLASS);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceConcrete()
     */
    @Override
    protected boolean handleIsHibernateInheritanceConcrete()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_CONCRETE);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceUnionSubClass()
     */
    @Override
    protected boolean handleIsHibernateInheritanceUnionSubClass()
    {
        String version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION);
        return (version.startsWith(HibernateGlobals.HIBERNATE_VERSION_3) || version.startsWith(HibernateGlobals.HIBERNATE_VERSION_4))
            && this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_UNION_SUBCLASS);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isLazy()
     */
    @Override
    protected boolean handleIsLazy()
    {
        String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        if (StringUtils.isBlank(value))
        {
            String version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION);
            value = version.startsWith(HibernateGlobals.HIBERNATE_VERSION_2) ? "false" : "true";
        }
        return Boolean.valueOf(value).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateCacheType()
     */
    @Override
    protected String handleGetHibernateCacheType()
    {
        String cacheType = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITY_CACHE);
        if (StringUtils.isBlank(cacheType))
        {
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ENTITY_CACHE));
        }
        return cacheType;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getFullyQualifiedEntityName()
     */
    @Override
    protected String handleGetFullyQualifiedEntityName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            null);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getFullyQualifiedEntityImplementationName()
     */
    @Override
    protected String handleGetFullyQualifiedEntityImplementationName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityImplementationName(),
            null);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDefaultCascade()
     */
    @Override
    protected String handleGetHibernateDefaultCascade()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(HIBERNATE_DEFAULT_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateGeneratorClass()
     */
    @Override
    protected String handleGetHibernateGeneratorClass()
    {
        String hibernateGeneratorClass;

        // if the entity is using a foreign identifier, then
        // we automatically set the identifier generator
        // class to be foreign
        if (this.isUsingForeignIdentifier())
        {
            hibernateGeneratorClass = HIBERNATE_GENERATOR_CLASS_FOREIGN;
        }
        else if (this.isUsingAssignedIdentifier())
        {
            hibernateGeneratorClass = HIBERNATE_GENERATOR_CLASS_ASSIGNED;
        }
        else
        {
            hibernateGeneratorClass =
                (String)this.findTaggedValue(
                    HibernateProfile.TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS,
                    false);

            if (StringUtils.isBlank(hibernateGeneratorClass))
            {
                hibernateGeneratorClass = (String)this.getConfiguredProperty(DEFAULT_HIBERNATE_GENERATOR_CLASS);
            }
        }
        return StringUtils.trimToEmpty(hibernateGeneratorClass);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isForeignHibernateGeneratorClass()
     */
    @Override
    protected boolean handleIsForeignHibernateGeneratorClass()
    {
        // check to see if the entity is using a foreign identifier
        // OR if the actual hibernate generator class is set to foreign
        return this.isUsingForeignIdentifier() ||
        this.getHibernateGeneratorClass().equalsIgnoreCase(HIBERNATE_GENERATOR_CLASS_FOREIGN);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isSequenceHibernateGeneratorClass()
     */
    @Override
    protected boolean handleIsSequenceHibernateGeneratorClass()
    {
        return this.getHibernateGeneratorClass().equalsIgnoreCase(HIBERNATE_GENERATOR_CLASS_SEQUENCE);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityName()
     */
    @Override
    protected String handleGetEntityName()
    {
        String entityNamePattern = (String)this.getConfiguredProperty(HibernateGlobals.ENTITY_NAME_PATTERN);

        return MessageFormat.format(
            entityNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityImplementationName()
     */
    @Override
    protected String handleGetEntityImplementationName()
    {
        String implNamePattern =
            String.valueOf(this.getConfiguredProperty(HibernateGlobals.ENTITY_IMPLEMENTATION_NAME_PATTERN));

        return MessageFormat.format(
            implNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorColumn()
     */
    @Override
    protected String handleGetHibernateDiscriminatorColumn()
    {
        String column = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_ENTITY_DISCRIMINATOR_COLUMN);

        if (StringUtils.isBlank(column))
        {
            column = String.valueOf(this.getConfiguredProperty(HibernateGlobals.ENTITY_DISCRIMINATOR_COLUMN));
        }

        return column;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorType()
     */
    @Override
    protected String handleGetHibernateDiscriminatorType()
    {
        String type = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_ENTITY_DISCRIMINATOR_TYPE);

        if (StringUtils.isBlank(type))
        {
            type = String.valueOf(this.getConfiguredProperty(HibernateGlobals.ENTITY_DISCRIMINATOR_TYPE));
        }

        return type;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorLength()
     */
    @Override
    protected int handleGetHibernateDiscriminatorLength()
    {
        return 1;
    }

    /**
     * Override so that we retrieve only the operations that are classifier
     * scope (i.e. static).
     *
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityLogic#getBusinessOperations()
     */
    @Override
    public Collection<OperationFacade> getBusinessOperations()
    {
        return HibernateMetafacadeUtils.filterBusinessOperations(super.getBusinessOperations());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isBusinessOperationsPresent()
     */
    @Override
    protected boolean handleIsBusinessOperationsPresent()
    {
        final Collection<OperationFacade> allBusinessOperations = this.getAllBusinessOperations();

        return (allBusinessOperations != null) && !allBusinessOperations.isEmpty();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateProxy()
     */
    @Override
    protected boolean handleIsHibernateProxy()
    {
        String hibernateProxy = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROXY);
        if (StringUtils.isBlank(hibernateProxy))
        {
            hibernateProxy = (String)this.getConfiguredProperty(HIBERNATE_PROXY);
        }
        return Boolean.valueOf(hibernateProxy).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheMaxElementsInMemory()
     */
    @Override
    protected int handleGetEhCacheMaxElementsInMemory()
    {
        String maxElements = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS);
        if (StringUtils.isBlank(maxElements))
        {
            maxElements = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_MAX_ELEMENTS);
        }
        return Integer.parseInt(StringUtils.trimToEmpty(maxElements));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEhCacheEternal()
     */
    @Override
    protected boolean handleIsEhCacheEternal()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL);
        if (StringUtils.isBlank(eternal))
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_ETERNAL);
        }
        return Boolean.valueOf(StringUtils.trimToEmpty(eternal)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheTimeToIdleSeconds()
     */
    @Override
    protected int handleGetEhCacheTimeToIdleSeconds()
    {
        String timeToIdle =
           (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE);
        if (StringUtils.isBlank(timeToIdle))
        {
            timeToIdle = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_IDLE);
        }
        return Integer.parseInt(StringUtils.trimToEmpty(timeToIdle));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheTimeToLiveSeconds()
     */
    @Override
    protected int handleGetEhCacheTimeToLiveSeconds()
    {
        String timeToLive =
           (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE);
        if (StringUtils.isBlank(timeToLive))
        {
            timeToLive = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_LIVE);
        }
        return Integer.parseInt(StringUtils.trimToEmpty(timeToLive));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEhCacheOverflowToDisk()
     */
    @Override
    protected boolean handleIsEhCacheOverflowToDisk()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        if (StringUtils.isBlank(eternal))
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        }
        return Boolean.valueOf(StringUtils.trimToEmpty(eternal)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateCacheDistributed()
     */
    @Override
    protected boolean handleIsHibernateCacheDistributed()
    {
        String distributed = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_ENTITYCACHE_DISTRIBUTED);
        boolean distributedCachingEnabled = Boolean.valueOf(StringUtils.trimToEmpty(distributed)).booleanValue();

        if (distributedCachingEnabled)
        {
            String entityCacheDistributed =
                (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITYCACHE_DISTRIBUTED);
            return Boolean.valueOf(StringUtils.trimToEmpty(entityCacheDistributed)).booleanValue();
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isTableRequired()
     */
    @Override
    protected boolean handleIsTableRequired()
    {
        return !this.isHibernateInheritanceClass() ||
        (this.isHibernateInheritanceClass() && (this.getGeneralization() == null));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getMappingClassName()
     */
    @Override
    protected String handleGetMappingClassName()
    {
        String mappingClassName = CLASS_MAPPING_NAME;
        final HibernateEntity superEntity = this.getSuperEntity();

        if ((superEntity != null) && !superEntity.isHibernateInheritanceInterface() &&
            !superEntity.isHibernateInheritanceConcrete())
        {
            mappingClassName = JOINED_SUBCLASS_MAPPING_NAME;

            if (this.isHibernateInheritanceClass())
            {
                mappingClassName = SUBCLASS_MAPPING_NAME;
            }
            else if (this.isHibernateInheritanceUnionSubClass())
            {
                mappingClassName = UNION_SUBCLASS_MAPPING_NAME;
            }
        }

        return mappingClassName;
    }

    /**
     * Gets the super entity for this entity (if one exists). If a
     * generalization does not exist OR if it's not an instance of
     * HibernateEntity then return null.
     *
     * @return the super entity or null if one doesn't exist.
     */
    private HibernateEntity getSuperEntity()
    {
        HibernateEntity superEntity = null;

        if ((this.getGeneralization() != null) && this.getGeneralization() instanceof HibernateEntity)
        {
            superEntity = (HibernateEntity)this.getGeneralization();
        }

        return superEntity;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getSubclassKeyColumn()
     */
    @Override
    protected String handleGetSubclassKeyColumn()
    {
        String column = null;
        final HibernateEntity superEntity = this.getSuperEntity();

        if ((superEntity != null) && superEntity.isHibernateInheritanceSubclass())
        {
            ModelElementFacade facade = this.getIdentifiers().iterator().next();
            if (facade instanceof EntityAttribute)
            {
                column = ((EntityAttribute)facade).getColumnName();
            }
        }

        return column;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRequiresMapping()
     */
    @Override
    protected boolean handleIsRequiresMapping()
    {
        final HibernateEntity superEntity = this.getSuperEntity();
        return HibernateUtils.mapSubclassesInSeparateFile(
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_MAPPING_STRATEGY)) ||
            this.isRoot() &&
            (
                !this.isHibernateInheritanceInterface() || this.getSpecializations().isEmpty() ||
                (superEntity != null && superEntity.isHibernateInheritanceInterface())
            );
    }

    /**
     * Indicates if this entity is a <code>root</code> entity (meaning it
     * doesn't specialize anything).
     */
    private boolean isRoot()
    {
        final HibernateEntity superEntity = this.getSuperEntity();
        boolean abstractConcreteEntity =
            (this.isHibernateInheritanceConcrete() || this.isHibernateInheritanceInterface()) && this.isAbstract();

        return (
            this.getSuperEntity() == null ||
            (superEntity.isHibernateInheritanceInterface() || superEntity.isHibernateInheritanceConcrete())
        ) && !abstractConcreteEntity;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRequiresSpecializationMapping()
     */
    @Override
    protected boolean handleIsRequiresSpecializationMapping()
    {
        return !HibernateUtils.mapSubclassesInSeparateFile(
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_MAPPING_STRATEGY)) && this.isRoot() &&
        (
            this.isHibernateInheritanceSubclass() || this.isHibernateInheritanceClass() ||
            this.isHibernateInheritanceUnionSubClass()
        );
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isDynamicInsert()
     */
    @Override
    protected boolean handleIsDynamicInsert()
    {
        String dynamicInsert =
            (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_INSERT);
        if (StringUtils.isBlank(dynamicInsert))
        {
            dynamicInsert = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_ENTITY_DYNAMIC_INSERT);
        }
        return Boolean.valueOf(dynamicInsert).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isDynamicUpdate()
     */
    @Override
    protected boolean handleIsDynamicUpdate()
    {
        String dynamicUpdate =
            (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_UPDATE);
        if (StringUtils.isBlank(dynamicUpdate))
        {
            dynamicUpdate = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_ENTITY_DYNAMIC_UPDATE);
        }
        return Boolean.valueOf(dynamicUpdate).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isMappingRequiresSuperProperties()
     */
    @Override
    protected boolean handleIsMappingRequiresSuperProperties()
    {
        return this.isHibernateInheritanceInterface() || (this.isHibernateInheritanceConcrete() && this.isAbstract());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateVersionProperty()
     */
    @Override
    protected String handleGetHibernateVersionProperty()
    {
        String version = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_VERSION_PROPERTY);
        if (StringUtils.isBlank(version))
        {
            version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION_PROPERTY);
        }
        return version;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateVersionPropertySqlName()
     */
    @Override
    protected String handleGetHibernateVersionPropertySqlName()
    {
        return EntityMetafacadeUtils.toSqlName(this.getHibernateVersionProperty(), this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getVersion()
     */
    @Override
    protected int handleGetVersion()
    {
        String version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION);
        if (version==null)
        {
            version = HibernateGlobals.HIBERNATE_VERSION_3;
        }
        else
        {
            version = version.substring(0, 1);
        }
        return Integer.parseInt(version);
    }

    private boolean isXmlPersistenceActive()
    {
        return HibernateUtils.isXmlPersistenceActive(
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION),
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityLogic#handleGetXmlTagName()
     */
    protected String handleGetXmlTagName()
    {
        String tagName = null;
        if (isXmlPersistenceActive())
        {
            tagName = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_XML_TAG_NAME);

            if (StringUtils.isBlank(tagName))
            {
                tagName = this.getName();
            }
        }
        return (StringUtils.isBlank(tagName)) ? null : tagName;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorValue()
     */
    @Override
    protected String handleGetHibernateDiscriminatorValue()
    {
        String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_ENTITY_DISCRIMINATOR_VALUE);
        if (StringUtils.isBlank(value))
        {
            value = getEntityImplementationName();
        }
        return value;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getSequenceName()
     */
    @Override
    protected String handleGetSequenceName()
    {
        String sequenceName = this.getTableName();
        final String sequenceSuffix = this.getSequenceSuffix();
        // Implicit conversion from short to int
        final int maxLength = this.getMaxSqlNameLength() - this.getSequenceSuffix().length();
        if (maxLength > 0)
        {
            final Object method = this.getConfiguredProperty(UMLMetafacadeProperties.SHORTEN_SQL_NAMES_METHOD);
            sequenceName = EntityMetafacadeUtils.ensureMaximumNameLength(sequenceName, Integer.valueOf(maxLength).shortValue(), (String)method) + sequenceSuffix;
        }
        return sequenceName;
    }

    private static final String SEQUENCE_IDENTIFIER_SUFFIX = "sequenceIdentifierSuffix";

    private String getSequenceSuffix()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(SEQUENCE_IDENTIFIER_SUFFIX));
    }
    
    //keeps fk index unique
    int lastIndexCounter=1;
    
    protected String nextIndexSuffix(){
        lastIndexCounter++;
        return String.valueOf(lastIndexCounter);
    }
 
    /**
     * @see EJB3EntityFacadeLogic#handleGetDaoBaseName()
     */
    @Override
    protected String handleGetDaoBaseName()
    {
        return MessageFormat.format(
                getDaoBaseNamePattern(),
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * Gets the value of the {@link #DAO_BASE_PATTERN}
     *
     * @return the DAO base name pattern.
     */
    private String getDaoBaseNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(DAO_BASE_PATTERN));
    }

    @Override
    protected boolean handleIsDaoBusinessOperationsPresent() {
        return false;
    }

    @Override
    protected boolean handleIsDaoImplementationRequired() {
        
        return !this.getValueObjectReferences().isEmpty() || !this.getDaoBusinessOperations().isEmpty() ||
                !this.getQueryOperations(true).isEmpty();
    }

    @Override
    protected String handleGetDaoNoTransformationConstantName() {
        
        return HibernateGlobals.TRANSFORMATION_CONSTANT_PREFIX + HibernateGlobals.NO_TRANSFORMATION_CONSTANT_SUFFIX;
    }

    @Override
    protected String handleGetDaoImplementationName() {
        
        return MessageFormat.format(
                getDaoImplementationNamePattern(),
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * Gets the value of the {@link #DAO_IMPLEMENTATION_PATTERN}
     *
     * @return the DAO implementation name pattern.
     */
    private String getDaoImplementationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(DAO_IMPLEMENTATION_PATTERN));
    }

    @Override
    protected String handleGetDaoName() {
        
        return MessageFormat.format(
            getDaoNamePattern(),
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * Gets the value of the {@link EJB3Globals#DAO_PATTERN}
     *
     * @return the DAO name pattern.
     */
    private String getDaoNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(HibernateGlobals.DAO_PATTERN));
    }

    @Override
    protected String handleGetFullyQualifiedDaoBaseName() {
        
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getDaoBaseName(),
            null);
    }

    @Override
    protected Collection<DependencyFacade> handleGetValueObjectReferences() {
        
        return this.getValueObjectReferences(false);
    }

    @Override
    protected Collection<DependencyFacade> handleGetAllValueObjectReferences() {
        
        return this.getValueObjectReferences(true);
    }

    @Override
    protected Collection<HibernateOperation> handleGetDaoBusinessOperations() {
        
        // operations that are not finders and static
        Collection finders = this.getQueryOperations();
        Collection<OperationFacade> operations = this.getOperations();

        Collection<HibernateOperation> nonFinders = CollectionUtils.subtract(operations, finders);
        CollectionUtils.filter(
            nonFinders,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((HibernateOperation)object).isStatic();
                }
            }
        );
        return nonFinders;
    }

    @Override
    protected Collection handleGetValueObjectReferences(boolean follow) {
        
        final Collection<DependencyFacade> sourceDependencies = new ArrayList<DependencyFacade>(this.getSourceDependencies());
        if (follow)
        {
            for (GeneralizableElementFacade entity = this.getGeneralization(); entity != null;
                entity = entity.getGeneralization())
            {
                sourceDependencies.addAll(entity.getSourceDependencies());
            }
        }
        Collection<DependencyFacade> valueDependencies = new ArrayList<DependencyFacade>();
        for (DependencyFacade dependency : sourceDependencies)
        {
            Object targetElement = dependency.getTargetElement();
            if (targetElement instanceof ClassifierFacade)
            {
                ClassifierFacade element = (ClassifierFacade)targetElement;
                if (element.isDataType() || element instanceof ValueObject ||
                            element instanceof EnumerationFacade)
                {
                    valueDependencies.add(dependency);
                }
            }
        }
        return valueDependencies;
    }

    @Override
    protected boolean handleIsOperationPresent(String op) {
        
        Collection collOps = this.getOperations();
        for (final Iterator it = collOps.iterator(); it.hasNext();)
        {
            final OperationFacade operation = (OperationFacade)it.next();
            if (operation.getName().equalsIgnoreCase(op))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the value of the {@link #DAO_DEFAULT_EXCEPTION_NAME_PATTERN}
     *
     * @return the DAO default exception name pattern.
     */
    private String getDaoDefaultExceptionNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(DAO_DEFAULT_EXCEPTION_NAME_PATTERN));
    }

    @Override
    protected String handleGetFullyQualifiedDaoDefaultExceptionName() {
        
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getDaoDefaultExceptionName(),
            null);
    }

    @Override
    protected String handleGetDaoDefaultExceptionName() {
        return MessageFormat.format(
                getDaoDefaultExceptionNamePattern(),
                StringUtils.trimToEmpty(this.getName()));
    }

    @Override
    protected HibernateEntity handleGetRoot() {
        HibernateEntity generalization = this;
        for (
            ; generalization.getGeneralization() != null && generalization instanceof HibernateEntity;
            generalization = (HibernateEntity) generalization.getGeneralization())
            ;
        return generalization;
    }

    @Override
    protected Collection handleGetInstanceAttributes(boolean follow, boolean withIdentifiers) {
        final Collection<AttributeFacade> attributes = this.getAttributes(follow, withIdentifiers);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = true;
                    if (object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isStatic();
                    }
                    return valid;
                }
            });
        // If a 1:1 owned identifier relationship, the dependent identifier attributes should be included in addition to the association
        // and the generator="foreign" and @PrimaryKeyJoinColumn annotations are used.
        final List<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        /*MetafacadeUtils.filterByStereotype(
            associationEnds,
            UMLProfile.STEREOTYPE_IDENTIFIER);*/
        //System.out.println("GetInstanceAttributes " + this.getFullyQualifiedName() + " associationEnds=" + this.getAssociationEnds().size() + " identifiers=" + associationEnds.size());
        for(AssociationEndFacade associationEnd : associationEnds)
        {
            //System.out.println("GetInstanceAttributes " + this.getFullyQualifiedName() + " " + associationEnd.getOtherEnd().getFullyQualifiedName() + " Identifier=" + associationEnd.getOtherEnd().hasStereotype("Identifier") + " associationEnd=" + associationEnd + " One2One=" + associationEnd.getOtherEnd().isOne2One() + " Type=" + associationEnd.getOtherEnd().getType());
            if (associationEnd.getOtherEnd().hasStereotype("Identifier") && associationEnd.getOtherEnd().isOne2One() && !associationEnd.getOtherEnd().hasStereotype(UMLProfile.STEREOTYPE_TRANSIENT) && associationEnd.getOtherEnd() instanceof HibernateAssociationEnd)
            {
                HibernateAssociationEnd ejb3AssociationEnd = (HibernateAssociationEnd) associationEnd;
                //System.out.println("GetInstanceAttributes " + this.getFullyQualifiedName() + " " + ejb3AssociationEnd + " Owning=" + ejb3AssociationEnd.isOwning() + " Aggregation=" + ejb3AssociationEnd.isAggregation() + " Composition=" + ejb3AssociationEnd.isComposition() + " OAggregation=" + ejb3AssociationEnd.getOtherEnd().isAggregation() + " OComposition=" + ejb3AssociationEnd.getOtherEnd().isComposition());
                if (ejb3AssociationEnd.isOwning())
                {
                    Entity entity = (Entity)ejb3AssociationEnd.getType();
                    Collection<ModelElementFacade> identifierAttributes = EntityMetafacadeUtils.getIdentifierAttributes(entity, follow);
                    //System.out.println("GetInstanceAttributes "  + this.getFullyQualifiedName() + " entity=" + entity + " Attributes=" + identifierAttributes);
                    for(ModelElementFacade identifier : identifierAttributes)
                    {
                        //System.out.println(identifier);
                        if (identifier instanceof AttributeFacade)
                        {
                            attributes.add((AttributeFacade)identifier);
                            //System.out.println("Added "  + identifier + " to entity=" + entity);
                        }
                    }
                }
            }
        }
        return attributes;
    }

    @Override
    protected String handleGetAttributesAsList(Collection attributes, boolean includeTypes, boolean includeNames,
            boolean includeAutoIdentifiers) {
                if ((!includeNames && !includeTypes) || attributes == null)
                {
                    return "";
                }
        
                StringBuilder sb = new StringBuilder();
                String separator = "";
        
                boolean isCompositePKPresent = this.isCompositePrimaryKeyPresent();
                if (isCompositePKPresent)
                {
                    if (includeTypes)
                    {
                        sb.append(this.getFullyQualifiedName()).append("PK");
                    }
                    sb.append(" pk");
                    separator = ", ";
                }
                for (final Object obj : attributes)
                {
                    if (obj instanceof HibernateEntityAttribute)
                    {
                        HibernateEntityAttribute attr = (HibernateEntityAttribute)obj;
                        // Do not include attributes that are assigned for optimistic lock value as a version
                        if (!attr.isVersion())
                        {
                            /* Do not include identifier attributes for entities with a composite primary key
                             or if includeAutoIdentifiers is false, do not include identifiers with auto generated values. */
                            if (!attr.isIdentifier() ||
                               (!isCompositePKPresent && (includeAutoIdentifiers || attr.isGeneratorTypeNone())))
                            {
                                sb.append(separator);
                                separator = ", ";
                                if (includeTypes)
                                {
                                    /*
                                     * If attribute is a LOB and lob type is overridden, then use
                                     * overriding lob type.
                                     */
                                    if (attr.isLob() && StringUtils.isNotBlank(attr.getLobType()))
                                    {
                                        sb.append(attr.getLobType());
                                    }
                                    else
                                    {
                                        sb.append(attr.getGetterSetterTypeName());
                                    }
                                    sb.append(" ");
                                }
                                if (includeNames)
                                {
                                    sb.append(attr.getName());
                                }
                            }
                        }
                    }
                    if (obj instanceof HibernateAssociationEnd)
                    {
                        HibernateAssociationEnd assoc = (HibernateAssociationEnd)obj;
                        /* Do not include identifier attributes for entities with a composite primary key
                         or if includeAutoIdentifiers is false, do not include identifiers with auto generated values.*/
                        //System.out.println(this.getName() + "." + assoc.getName() + " Identifier:" + assoc.isIdentifier() + " isCompositePKPresent:" + isCompositePKPresent + " includeAutoIdentifiers:" + includeAutoIdentifiers);
                        if (!assoc.isIdentifier() || !isCompositePKPresent)
                        {
                            sb.append(separator);
                            separator = ", ";
                            if (includeTypes)
                            {
                                sb.append(assoc.getGetterSetterTypeName()).append(" ");
                            }
                            if (includeNames)
                            {
                                sb.append(assoc.getName());
                            }
                        }
                    }
                }
                return sb.toString();
    }

    @Override
    protected String handleGetInstanceAttributeNameList(boolean follow, boolean withIdentifiers) {
        return this.getNameList(this.getInstanceAttributes(follow, withIdentifiers));
    }

    @Override
    protected String handleGetInstanceAttributeTypeList(boolean follow, boolean withIdentifiers) {
        return this.getTypeList(this.getInstanceAttributes(follow, withIdentifiers));
    }

    @Override
    protected boolean handleIsCompositePrimaryKeyPresent() {
        boolean isCompositePK = false;
        if (this.getIdentifiers().size() > 1)
        {
            isCompositePK = true;
        }
        return isCompositePK;
    }

    /**
     * Constructs a comma separated list of attribute type names from the passed in collection of
     * <code>attributes</code>.
     *
     * @param attributes the attributes to construct the list from.
     * @return the comma separated list of attribute types.
     */
    private String getTypeList(final Collection attributes)
    {
        final StringBuilder list = new StringBuilder();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            attributes,
            new Closure()
            {
                public void execute(final Object object)
                {
                    if (object instanceof AttributeFacade)
                    {
                        final AttributeFacade attribute = (AttributeFacade)object;
                        if (attribute.getType() != null)
                        {
                            list.append(attribute.getGetterSetterTypeName());
                            list.append(comma);
                        }
                    }
                    if (object instanceof AssociationEndFacade)
                    {
                        final AssociationEndFacade associationEnd = (AssociationEndFacade)object;
                        if (associationEnd.getType() != null)
                        {
                            list.append(associationEnd.getGetterSetterTypeName());
                            list.append(comma);
                        }
                    }
                }
            });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * Constructs a comma separated list of attribute names from the passed in collection of <code>attributes</code>.
     *
     * @param properties the properties to construct the list from.
     * @return the comma separated list of attribute names.
     */
    private String getNameList(final Collection properties)
    {
        final StringBuilder list = new StringBuilder();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            properties,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object instanceof EntityAttribute)
                    {
                        list.append(((AttributeFacade)object).getName());
                        list.append(comma);
                    }
                    if (object instanceof EntityAssociationEnd)
                    {
                        list.append(((AssociationEndFacade)object).getName());
                        list.append(comma);
                    }
                }
            });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    @Override
    protected AttributeFacade handleGetManageableDisplayAttribute() {
        AttributeFacade displayAttribute = null;

        final Object taggedValueObject = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME);
        if (taggedValueObject != null)
        {
            displayAttribute = this.findAttribute(StringUtils.trimToEmpty(taggedValueObject.toString()));
        }

        final Collection<AttributeFacade> attributes = this.getAttributes(true);
        for (final Iterator<AttributeFacade> attributeIterator = attributes.iterator();
            attributeIterator.hasNext() && displayAttribute == null;)
        {
            final AttributeFacade attribute = attributeIterator.next();
            if (attribute.isUnique())
            {
                displayAttribute = attribute;
            }
        }

        if (displayAttribute == null)
        {
            if (!this.getIdentifiers().isEmpty())
            {
                ModelElementFacade facade = this.getIdentifiers().iterator().next();
                if (facade instanceof AttributeFacade)
                {
                    displayAttribute = (AttributeFacade)facade;
                }
                else if (!attributes.isEmpty())
                {
                    displayAttribute = (EntityAttribute)attributes.iterator().next();
                }
            }
            else if (!attributes.isEmpty())
            {
                displayAttribute = (EntityAttribute)attributes.iterator().next();
            }
        }

        return displayAttribute;
    }

    @Override
    protected Object handleGetIdentifier() {
        ModelElementFacade identifier = null;
        final Collection<ModelElementFacade> identifiers = this.getIdentifiers();
        if (identifiers != null && !identifiers.isEmpty())
        {
            identifier = this.getIdentifiers().iterator().next();
        }
        return identifier;
    }

    @Override
    protected String handleGetFullyQualifiedDaoName() {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getDaoName(),
                null);
    }

    @Override
    protected String handleGetFullyQualifiedDaoImplementationName() {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getDaoImplementationName(),
                null);
    }
}