package org.andromda.cartridges.spring.metafacades;

/**
 * Stores Globals specific to the Spring cartridge.
 * 
 * @author Chad Brandon
 */
class SpringGlobals
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
     * DAO implementation class suffix.
     */
    final static String DAO_IMPLEMENTATION_SUFFIX = "DaoImpl";

    /**
     * The DAO class suffix.
     */
    final static String DAO_SUFFIX = "Dao";

    /**
     * The DAO base class suffix.
     */
    final static String DAO_BASE_SUFFIX = "DaoBase";

    /**
     * The service base class suffix.
     */
    final static String SERVICE_BASE_SUFFIX = "Base";

    /**
     * The bean name target suffix
     */
    final static String BEAN_NAME_TARGET_SUFFIX = "Target";

    /**
     * The service web service delegator.
     */
    final static String WEB_SERVICE_DELEGATOR_SUFFIX = "WSDelegator";

    /**
     * Represents the hibernate <code>delete</code> cascade option.
     */
    public static final String HIBERNATE_CASCADE_DELETE = "delete";

    /**
     * Represents the hibernate <code>all-delete-orphan</code> cascade option.
     */
    public static final String HIBERNATE_CASCADE_ALL_DELETE_ORPHAN = "all-delete-orphan";

    /**
     * Represents the hibernate <code>save-update</code> cascade option.
     */
    public static final String HIBERNATE_CASCADE_SAVE_UPDATE = "save-update";

    /**
     * The property used to specify the implementation operation name prefix (on
     * both services and DAOs).
     */
    public static final String PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX = "implementationOperationNamePrefix";
}