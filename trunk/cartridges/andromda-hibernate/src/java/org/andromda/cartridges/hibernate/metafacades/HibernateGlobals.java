package org.andromda.cartridges.hibernate.metafacades;


/**
 * Stores Globals specific to the Hibernate cartridge.
 *
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class HibernateGlobals
{
    /**
     * Denotes whether or not subclasses require their own mapping file.
     */
    static public final String HIBERNATE_MAPPING_STRATEGY = "hibernateMappingStrategy";

    /**
     * A mapping file per subclass.
     */
    static public final String HIBERNATE_MAPPING_STRATEGY_SUBCLASS = "subclass";

    /**
     * A mapping file per hierarchy.
     */
    static public final String HIBERNATE_MAPPING_STRATEGY_HIERARCHY = "hierarchy";

    /**
     * Indicates if "XML Persistence" code generation is enabled. Requires Hibernate 3.
     */
    static public final String HIBERNATE_XML_PERSISTENCE = "hibernateXMLPersistence";

    /**
     * Determines if the identifier attribute on an entity is generated as an attribute
     * or as a separate element.
     */
    static final String HIBERNATE_XML_PERSISTENCE_ID_AS_ATTRIBUTE = "hibernateXMLPersistIDAsAttribute";

    /**
     * Hibernate version to use.
     */
    static public final String HIBERNATE_VERSION = "hibernateVersion";

    /**
     * The version for Hibernate 2.
     */
    public static final String HIBERNATE_VERSION_2 = "2";

    /**
     * The version for Hibernate 3.
     */
    public static final String HIBERNATE_VERSION_3 = "3";

    /**
     * EJB implementation class suffix.
     */
    final static String EJB_IMPLEMENTATION_SUFFIX = "Bean";

    /**
     * Represents the hibernate <code>delete</code> cascade option.
     */
    static final String HIBERNATE_CASCADE_DELETE = "delete";

    /**
     * Represents the hibernate <code>all-delete-orphan</code> cascade option.
     */
    static final String HIBERNATE_CASCADE_ALL_DELETE_ORPHAN = "all-delete-orphan";

    /**
     * Represents the hibernate <code>save-update</code> cascade option.
     */
    static final String HIBERNATE_CASCADE_SAVE_UPDATE = "save-update";

    /**
     * Represents the hibernate <code>all</code> cascade option.
     */
    static final String HIBERNATE_CASCADE_ALL = "all";

    /**
     * Represents the hibernate <code>none</code> cascade option.
     */
    static final String HIBERNATE_CASCADE_NONE = "none";

    /**
     * The property used to specify the implementation operation name prefix (on
     * services).
     */
    static final String PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX = "implementationOperationNamePrefix";

    /**
     * The namespace property storing default ehCache maxElementsInMemory
     * parameter
     */
    static final String HIBERNATE_EHCACHE_MAX_ELEMENTS = "ehCacheMaxElementsInMemory";

    /**
     * The namespace property storing default ehCache eternal parameter
     */
    static final String HIBERNATE_EHCACHE_ETERNAL = "ehCacheEternal";

    /**
     * The namespace property storing default ehCache eternal parameter
     */
    static final String HIBERNATE_EHCACHE_TIME_TO_IDLE = "ehCacheTimeToIdleSeconds";

    /**
     * The namespace property storing default ehCache eternal parameter
     */
    static final String HIBERNATE_EHCACHE_TIME_TO_LIVE = "ehCacheTimeToLiveSeconds";

    /**
     * The namespace property storing default ehCache eternal parameter
     */
    static final String HIBERNATE_EHCACHE_OVERFLOW_TO_DISK = "ehCacheOverflowToDisk";

    /**
     * The namespace property storing whether distributed caching is turned on at all.
     */
    static final String HIBERNATE_ENTITYCACHE_DISTRIBUTED = "hibernateEnableDistributedCache";

    /**
     * The namespace property storing default dynamic-insert parameter
     */
    static final String HIBERNATE_ENTITY_DYNAMIC_INSERT = "hibernateEntityDynamicInsert";

    /**
     * The namespace property storing default dynamic-update parameter
     */
    static final String HIBERNATE_ENTITY_DYNAMIC_UPDATE = "hibernateEntityDynamicUpdate";

    /**
     * The namespace property storing default collection type for associations
     */
    static final String HIBERNATE_ASSOCIATION_COLLECTION_TYPE = "hibernateAssociationCollectionType";

    /**
     * The namespace property storing default sort method for collections
     */
    static final String HIBERNATE_ASSOCIATION_SORT_TYPE = "hibernateAssociationSortType";

    /**
     * The namespace property to specify the pattern for determining the entity
     * name.
     */
    static final String ENTITY_NAME_PATTERN = "entityNamePattern";

    /**
     * The property which stores the pattern defining the entity implementation
     * name.
     */
    static final String ENTITY_IMPLEMENTATION_NAME_PATTERN = "entityImplementationNamePattern";

    /**
     * The property which stores the pattern defining the embedded value
     * implementation name.
     */
    static final String EMBEDDED_VALUE_IMPLEMENTATION_NAME_PATTERN = "embeddedValueImplementationNamePattern";

    /**
     * The property which defines a default value for hibernate entities
     * versioning.
     */
    static final String HIBERNATE_VERSION_PROPERTY = "versionProperty";

    /**
     * The 'list' type implementation to use.
     */
    static final String LIST_TYPE_IMPLEMENTATION = "listTypeImplementation";

    /**
     * The 'set' type implementation to use.
     */
    static final String SET_TYPE_IMPLEMENTATION = "setTypeImplementation";

    /**
     * The 'map' type implementation to use.
     */
    static final String MAP_TYPE_IMPLEMENTATION = "mapTypeImplementation";

    /**
     * The 'bag' type implementation to use.
     */
    static final String BAG_TYPE_IMPLEMENTATION = "bagTypeImplementation";

    /**
     * A flag indicating whether or not specific (java.util.Set, java.util.List,
     * etc) collection interfaces should be used in association mutators and
     * accessors or whether the generic java.util.Collection interface should be
     * used.
     */
    static final String SPECIFIC_COLLECTION_INTERFACES = "specificCollectionInterfaces";

    /**
     * The property that defines the default collection interface, this is the
     * interface used if the property defined by
     * {@link #SPECIFIC_COLLECTION_INTERFACES} is true.
     */
    static final String DEFAULT_COLLECTION_INTERFACE = "defaultCollectionInterface";

    /**
    * The default Hibernate discriminator column name
    */
    static final String ENTITY_DISCRIMINATOR_COLUMN = "defaultEntityDiscriminatorColumn";

    /**
    * The default Hibernate discriminator column type
    */
    static final String ENTITY_DISCRIMINATOR_TYPE = "defaultEntityDiscriminatorType";
    
    /**
     * Optional definition of the package name where all Hibernate enumeration types are
     * to be generated. This will overrride the default model package name where the
     * enumeration class itself will still be placed.
     */
    static final String ENUM_TYPES_PACKAGE = "enumTypesPackage";
}