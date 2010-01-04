package org.andromda.cartridges.hibernate.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.cartridges.hibernate.HibernateUtils;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p> Provides support for the hibernate inheritance strategies of class
 * (table per hierarchy), subclass (table per subclass in hierarchy) and
 * concrete (table per class). With concrete the strategy can be changed lower
 * down. Also provides for the root class being defined as an interface and the
 * attributes remapped to the subclasses. This is useful in the concrete case
 * becuase it has limitations in the associations.
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
 */
public class HibernateEntityLogicImpl
    extends HibernateEntityLogic
{
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
     * Return all the business operations (ones that are inherited as well as
     * directly on the entity).
     *
     * @return all business operations
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getAllBusinessOperations()
     */
    protected Collection<OperationFacade> handleGetAllBusinessOperations()
    {
        Entity superElement = (Entity)this.getGeneralization();
        Collection<OperationFacade> result = this.getBusinessOperations();

        while (superElement != null)
        {
            result.addAll(superElement.getBusinessOperations());
            superElement = (Entity)superElement.getGeneralization();
        }

        return result;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateInheritanceStrategy()
     */
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
    public List<AttributeFacade> getProperties()
    {
        List<AttributeFacade> properties = this.getAttributes();
        List connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        class NavigableFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                AssociationEndFacade end = (AssociationEndFacade)object;

                return end.isNavigable() || (end.getOtherEnd().isChild() && isForeignHibernateGeneratorClass());
            }
        }
        CollectionUtils.filter(
            connectingEnds,
            new NavigableFilter());
        properties.addAll(connectingEnds);

        return properties;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceClass()
     */
    protected boolean handleIsHibernateInheritanceClass()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_CLASS);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceInterface()
     */
    protected boolean handleIsHibernateInheritanceInterface()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_INTERFACE);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceSubclass()
     */
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_SUBCLASS);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceConcrete()
     */
    protected boolean handleIsHibernateInheritanceConcrete()
    {
        return this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_CONCRETE);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateInheritanceUnionSubClass()
     */
    protected boolean handleIsHibernateInheritanceUnionSubClass()
    {
        String version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION);
        return (version.equals(HibernateGlobals.HIBERNATE_VERSION_3)) &&
        this.getHibernateInheritanceStrategy().equalsIgnoreCase(INHERITANCE_STRATEGY_UNION_SUBCLASS);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isLazy()
     */
    protected boolean handleIsLazy()
    {
        String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        if (StringUtils.isBlank(value))
        {
            String version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION);
            value = version.equals(HibernateGlobals.HIBERNATE_VERSION_2) ? "false" : "true";
        }
        return Boolean.valueOf(value).booleanValue();
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateCacheType()
     */
    protected String handleGetHibernateCacheType()
    {
        String cacheType = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ENTITY_CACHE);

        if (StringUtils.isBlank(cacheType))
        {
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ENTITY_CACHE));
        }

        return cacheType;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            null);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getFullyQualifiedEntityImplementationName()
     */
    protected String handleGetFullyQualifiedEntityImplementationName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityImplementationName(),
            null);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDefaultCascade()
     */
    protected String handleGetHibernateDefaultCascade()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(HIBERNATE_DEFAULT_CASCADE)));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateGeneratorClass()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isForeignHibernateGeneratorClass()
     */
    protected boolean handleIsForeignHibernateGeneratorClass()
    {
        // check to see if the entity is using a foreign identifier
        // OR if the actual hibernate generator class is set to foreign
        return this.isUsingForeignIdentifier() ||
        this.getHibernateGeneratorClass().equalsIgnoreCase(HIBERNATE_GENERATOR_CLASS_FOREIGN);
    }

    @Override
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isSequenceHibernateGeneratorClass()
     */
    protected boolean handleIsSequenceHibernateGeneratorClass()
    {
        return this.getHibernateGeneratorClass().equalsIgnoreCase(HIBERNATE_GENERATOR_CLASS_SEQUENCE);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        String entityNamePattern = (String)this.getConfiguredProperty(HibernateGlobals.ENTITY_NAME_PATTERN);

        return MessageFormat.format(
            entityNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityImplementationName()
     */
    protected String handleGetEntityImplementationName()
    {
        String implNamePattern =
            String.valueOf(this.getConfiguredProperty(HibernateGlobals.ENTITY_IMPLEMENTATION_NAME_PATTERN));

        return MessageFormat.format(
            implNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorColumn()
     */
    protected String handleGetHibernateDiscriminatorColumn()
    {
        String column = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_ENTITY_DISCRIMINATOR_COLUMN);

        if (StringUtils.isBlank(column))
        {
            column = String.valueOf(this.getConfiguredProperty(HibernateGlobals.ENTITY_DISCRIMINATOR_COLUMN));
        }

        return column;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorType()
     */
    protected String handleGetHibernateDiscriminatorType()
    {
        String type = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_ENTITY_DISCRIMINATOR_TYPE);

        if (StringUtils.isBlank(type))
        {
            type = String.valueOf(this.getConfiguredProperty(HibernateGlobals.ENTITY_DISCRIMINATOR_TYPE));
        }

        return type;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateDiscriminatorLength()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isBusinessOperationsPresent()
     */
    protected boolean handleIsBusinessOperationsPresent()
    {
        final Collection<OperationFacade> allBusinessOperations = this.getAllBusinessOperations();

        return (allBusinessOperations != null) && !allBusinessOperations.isEmpty();
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateProxy()
     */
    protected boolean handleIsHibernateProxy()
    {
        String hibernateProxy = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROXY);

        if (StringUtils.isBlank(hibernateProxy))
        {
            hibernateProxy = (String)this.getConfiguredProperty(HIBERNATE_PROXY);
        }

        return Boolean.valueOf(hibernateProxy).booleanValue();
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheMaxElementsInMemory()
     */
    protected int handleGetEhCacheMaxElementsInMemory()
    {
        String maxElements = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS);

        if (StringUtils.isBlank(maxElements))
        {
            maxElements = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_MAX_ELEMENTS);
        }

        return Integer.parseInt(StringUtils.trimToEmpty(maxElements));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEhCacheEternal()
     */
    protected boolean handleIsEhCacheEternal()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL);
        if (StringUtils.isBlank(eternal))
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_ETERNAL);
        }
        return Boolean.valueOf(StringUtils.trimToEmpty(eternal)).booleanValue();
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheTimeToIdleSeconds()
     */
    protected int handleGetEhCacheTimeToIdleSeconds()
    {
        String timeToIdle = null;
        timeToIdle = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE);

        if (StringUtils.isBlank(timeToIdle))
        {
            timeToIdle = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_IDLE);
        }

        return Integer.parseInt(StringUtils.trimToEmpty(timeToIdle));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEhCacheTimeToLiveSeconds()
     */
    protected int handleGetEhCacheTimeToLiveSeconds()
    {
        String timeToLive = null;
        timeToLive = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE);

        if (StringUtils.isBlank(timeToLive))
        {
            timeToLive = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_LIVE);
        }

        return Integer.parseInt(StringUtils.trimToEmpty(timeToLive));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isEhCacheOverflowToDisk()
     */
    protected boolean handleIsEhCacheOverflowToDisk()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);

        if (StringUtils.isBlank(eternal))
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        }

        return Boolean.valueOf(StringUtils.trimToEmpty(eternal)).booleanValue();
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isHibernateCacheDistributed()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isTableRequired()
     */
    protected boolean handleIsTableRequired()
    {
        return !this.isHibernateInheritanceClass() ||
        (this.isHibernateInheritanceClass() && (this.getGeneralization() == null));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getMappingClassName()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getSubclassKeyColumn()
     */
    protected String handleGetSubclassKeyColumn()
    {
        String column = null;
        final HibernateEntity superEntity = this.getSuperEntity();

        if ((superEntity != null) && superEntity.isHibernateInheritanceSubclass())
        {
            column = (this.getIdentifiers().iterator().next()).getColumnName();
        }

        return column;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRequiresMapping()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRequiresSpecializationMapping()
     */
    protected boolean handleIsRequiresSpecializationMapping()
    {
        return !HibernateUtils.mapSubclassesInSeparateFile(
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_MAPPING_STRATEGY)) && this.isRoot() &&
        (
            this.isHibernateInheritanceSubclass() || this.isHibernateInheritanceClass() ||
            this.isHibernateInheritanceUnionSubClass()
        );
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isDynamicInsert()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isDynamicUpdate()
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isMappingRequiresSuperProperties()
     */
    protected boolean handleIsMappingRequiresSuperProperties()
    {
        return this.isHibernateInheritanceInterface() || (this.isHibernateInheritanceConcrete() && this.isAbstract());
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateVersionProperty()
     */
    protected String handleGetHibernateVersionProperty()
    {
        String version = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_VERSION_PROPERTY);
        if (StringUtils.isBlank(version))
        {
            version = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION_PROPERTY);
        }
        return version;
    }
    
    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getHibernateVersionPropertySqlName()
     */
    protected String handleGetHibernateVersionPropertySqlName()
    {
        return EntityMetafacadeUtils.toSqlName(this.getHibernateVersionProperty(), this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getVersion()
     */
    protected int handleGetVersion()
    {
        return Integer.parseInt((String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION));
    }

    private boolean isXmlPersistenceActive()
    {
        return HibernateUtils.isXmlPersistenceActive(
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION),
            (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE));
    }

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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#hibernateDiscriminatorValue()
     */
    protected String handleGetHibernateDiscriminatorValue()
    {
        String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_ENTITY_DISCRIMINATOR_VALUE);

        if (StringUtils.isBlank(value))
        {
            value = getEntityImplementationName();
        }

        return value;
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getSequenceName()
     */
    protected String handleGetSequenceName()
    {
        String sequenceName = this.getTableName();
        final String sequenceSuffix = this.getSequenceSuffix();
        // Implicit conversion from short to int
        final int maxLength = this.getMaxSqlNameLength() - this.getSequenceSuffix().length();
        if (maxLength > -0)
        {
            sequenceName = EntityMetafacadeUtils.ensureMaximumNameLength(sequenceName, Integer.valueOf(maxLength).shortValue()) + sequenceSuffix;
        }
        return sequenceName;
    }

    private static final String SEQUENCE_IDENTIFIER_SUFFIX = "sequenceIdentifierSuffix";

    private String getSequenceSuffix()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(SEQUENCE_IDENTIFIER_SUFFIX));
    }
}