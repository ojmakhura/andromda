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
     * The property used to specify the implementation operation name prefix (on
     * services).
     */
    static final String PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX = "implementationOperationNamePrefix";
}