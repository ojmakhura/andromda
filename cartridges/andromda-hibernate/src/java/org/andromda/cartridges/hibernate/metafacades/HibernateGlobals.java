package org.andromda.cartridges.hibernate.metafacades;

/**
 * Stores Globals specific to the Hibernate cartridge.
 * 
 * @author Chad Brandon
 */
class HibernateGlobals
{
    /**
     * POJO implementation class suffix.
     */
    final static String IMPLEMENTATION_SUFFIX = "Impl";

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

}