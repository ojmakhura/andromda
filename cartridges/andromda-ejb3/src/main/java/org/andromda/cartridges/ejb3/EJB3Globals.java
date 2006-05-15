package org.andromda.cartridges.ejb3;

/**
 * Stores Globals specific to the EJB3 cartridge.
 *
 * @author Vance Karimi
 */
public class EJB3Globals
{
    
    /**
     * The property that stores the default EJB transaction type.
     */
    public static final String TRANSACTION_TYPE = "transactionType";
    
    /**
     * The pattern to use for determining the package name for EJBs.
     */
    public static final String JNDI_NAME_PREFIX = "jndiNamePrefix";
    
    /**
     * The property which stores the pattern defining the display attribute
     * label name when it's of type Collection.
     */
    public static final String LABEL_COLLECTION_NAME_PATTERN = "labelCollectionNamePattern";
    
    /**
     * The property which stores the pattern defining the display attribute
     * label name when it's not of type Collection.
     */
    public static final String LABEL_SINGLE_NAME_PATTERN = "labelSingleNamePattern";
    
    // --------------- Constants ---------------------
    
    /**
     * Represents the eager fetch type
     */
    public static final String FETCH_TYPE_EAGER = "EAGER";
    
    /**
     * Represents the lazy fetch type
     */
    public static final String FETCH_TYPE_LAZY = "LAZY";

    /**
     * Represents the clob lob type
     */
    public static final String LOB_TYPE_CLOB = "CLOB";
    
    /**
     * Represents the blob lob type
     */
    public static final String LOB_TYPE_BLOB = "BLOB";

    /**
     * Represents the table generator type
     */
    public static final String GENERATOR_TYPE_TABLE = "TABLE";
    
    /**
     * Represents the sequence generator type
     */
    public static final String GENERATOR_TYPE_SEQUENCE = "SEQUENCE";
    
    /**
     * Represents the identity generator type
     */
    public static final String GENERATOR_TYPE_IDENTITY = "IDENTITY";
    
    /**
     * Represents the auto generator type
     */
    public static final String GENERATOR_TYPE_AUTO = "AUTO";
    
    /**
     * Represents the none generator type
     */
    public static final String GENERATOR_TYPE_NONE = "NONE";

    /**
     * Represents the date temporal type
     */
    public static final String TEMPORAL_TYPE_DATE = "DATE";
    
    /**
     * Represents the time temporal type
     */
    public static final String TEMPORAL_TYPE_TIME = "TIME";
    
    /**
     * Represents the timestamp temporal type
     */
    public static final String TEMPORAL_TYPE_TIMESTAMP = "TIMESTAMP";
    
    /**
     * Represents the none temporal type
     */
    public static final String TEMPORAL_TYPE_NONE = "NONE";

    /**
     * Represents the finder method index type on the parameter
     */
    public static final String FINDER_RESULT_TYPE_FIRST = "First";
    
    /**
     * Represents the finder method max count on the parameter
     */
    public static final String FINDER_RESULT_TYPE_MAX = "Max";
    
    /**
     * Represents the view type for the entity POJO as both remote and local
     */
    public static final String VIEW_TYPE_BOTH = "both";
    
    /**
     * Represents the local view type for the entity POJO
     */
    public static final String VIEW_TYPE_LOCAL = "local";
    
    /**
     * Represents the remote view type for the entity POJO
     */
    public static final String VIEW_TYPE_REMOTE = "remote";
    
    /**
     * Represents the mandatory transaction type
     */
    public static final String TRANSACTION_TYPE_MANDATORY = "Manadatory";
    
    /**
     * Represents the never transaction type
     */
    public static final String TRANSACTION_TYPE_NEVER = "Never";
    
    /**
     * Represents the not supported transaction type
     */
    public static final String TRANSACTION_TYPE_NOT_SUPPORTED = "NotSupported";
    
    /**
     * Represents the required transaction type
     */
    public static final String TRANSACTION_TYPE_REQUIRED = "Required";
    
    /**
     * Represents the required transaction type
     */
    public static final String TRANSACTION_TYPE_REQUIRES_NEW = "RequiresNew";
    
    /**
     * Represents the supports transaction type
     */
    public static final String TRANSACTION_TYPE_SUPPORTS = "Supports";
    
    /**
     * Represents the stateless session bean
     */
    public static final String SERVICE_TYPE_STATELESS = "Stateless";
    
    /**
     * Represents the stateful session bean
     */
    public static final String SERVICE_TYPE_STATEFUL = "Stateful";

    /**
     * Represents the JBoss persistence container constant
     */
    public static final String PERSISTENCE_CONTAINER_JBOSS = "jboss";
    
    /**
     * Represents the Weblogic persistence container contant
     */
    public static final String PERSISTENCE_CONTAINER_WEBLOGIC = "weblogic";

    /**
     * Represents the default security domain
     */
    public static final String SECURITY_REALM = "securityRealm";

    /**
     * Represents the bean managed transaction demarcation
     */
    public static final String TRANSACTION_MANAGEMENT_BEAN = "bean";
    
    /**
     * Represents the container managed transaction demarcation
     */
    public static final String TRANSACTION_MANAGEMENT_CONTAINER = "container";

    /**
     * Represents the fully qualified topic destination type for the
     * JMS message driven bean
     */
    public static final String MDB_DESTINATION_TYPE_TOPIC = "javax.jms.Topic";
    
    /**
     * Represents the fully qualified queue destination type for the
     * JMS message driven bean
     */
    public static final String MDB_DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    
    /**
     * The namespace delimiter (seperates namespaces).
     */
    public static final char NAMESPACE_DELIMITER = '.';
    
    /**
     * Defines the prefix given to the transformation constants.
     */
    public final static String TRANSFORMATION_CONSTANT_PREFIX = "TRANSFORM_";
    
    /**
     * The suffix given to the no transformation constant.
     */
    public static final String NO_TRANSFORMATION_CONSTANT_SUFFIX = "NONE";
    
    /**
     * The prefix given to transformation method names.
     */
    public static final String TRANSFORMATION_METHOD_PREFIX = "to";

    /**
     * The suffix given to transformation method names.
     */
    public static final String TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX = "Collection";

    /**
     * The pattern used to construct the DAO name.
     */
    public static final String DAO_PATTERN = "daoNamePattern";

    /**
     * The property which stores the persistence context unit name associated with the default
     * Entity Manager.
     */
    public static final String PERSISTENCE_CONTEXT_UNIT_NAME = "persistenceContextUnitName";

    /**
     * The property used to specify the implementation operation name pattern 
     * on service and entity beans.
     */
    public static final String IMPLEMENTATION_OPERATION_NAME_PATTERN = 
        "implementationOperationNamePattern";
    
    /**
     * The property used to specify whether to enable query cache.
     */
    public static final String HIBERNATE_USER_QUERY_CACHE = "hibernateUseQueryCache";
}
