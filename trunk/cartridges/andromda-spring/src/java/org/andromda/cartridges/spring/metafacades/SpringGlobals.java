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
     * The prefix given to transformation method names.
     */
    final static String TRANSFORMATION_METHOD_PREFIX = "to";

    /**
     * Defines the prefix given to the transformation constants.
     */
    final static String TRANSFORMATION_CONSTANT_PREFIX = "TRANSFORM_";

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
     * The property used to specify the implementation operation name prefix (on
     * both services and DAOs).
     */
    static final String PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX = "implementationOperationNamePrefix";
    
    /**
     * Stores the default outerjoin setting for this association end.
     */
    static final String PROPERTY_ASSOCIATION_END_OUTERJOIN = "hibernateAssociationEndOuterJoin";
}