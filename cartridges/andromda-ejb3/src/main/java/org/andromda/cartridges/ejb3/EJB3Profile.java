package org.andromda.cartridges.ejb3;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The EJB3 profile. Contains the profile information (tagged values, and stereotypes) for the EJB3 cartridge.
 *
 * @author Vance Karimi
 */
public class EJB3Profile
    extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();
    
    /* ----------------- Stereotypes -------------------- */
    
    /**
     * Specifies the entity bean stereotype.
     */
    public static final String STEREOTYPE_ENTITY = profile.get("ENTITY");
    
    /**
     * Specifies this class as a mapped/embeddable super class.
     */
    public static final String STEREOTYPE_MAPPED_SUPERCLASS = profile.get("MAPPED_SUPERCLASS");
    
    /**
     * Specifies the service bean stereotype.
     */
    public static final String STEREOTYPE_SERVICE = profile.get("SERVICE");
    
    /**
     * Specifies the JMS message driven bean stereotype.
     */
    public static final String STEREOTYPE_MESSAGE_DRIVEN = profile.get("MESSAGE_DRIVEN");
    
    /**
     * Specifies the create method stereotype - used in entity POJO
     * and session bean.
     */
    public static final String STEREOTYPE_CREATE_METHOD = profile.get("CREATE_METHOD");
    
    /**
     * Specifies the select method stereotype - used in EJB2.x
     * This may be deprecated in future releases.
     */
    public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";
    
    /**
     * Specifies the environment entry stereotype used on static variables
     * to permit Resource injection.
     */
    public static final String STEREOTYPE_ENV_ENTRY = profile.get("ENV_ENTRY");
    
    /**
     * Specifies the version stereotype - optimistic lock value of an attribute of an entity.
     */
    public static final String STEREOTYPE_VERSION = profile.get("VERSION");

    /**
     * Represents a transient entity attribute - non persistent.
     */
    public static final String STEREOTYPE_TRANSIENT = profile.get("TRANSIENT");
    
    /**
     * Represents a reference to a value object - used in EJB2.x
     * This may be deprecated in furture releases.
     */
    public static final String STEREOTYPE_VALUE_REF = profile.get("VALUE_REF");
    
    /**
     * Represents a persistence context instance referenced from a session bean.
     */
    public static final String STEREOTYPE_PERSISTENCE_CONTEXT = profile.get("PERSISTENCE_CONTEXT");
    
    /**
     * Represents a reference to a resource ie UserTransaction or DataSource.
     */
    public static final String STEREOTYPE_RESOURCE_REF = profile.get("RESOURCE_REF");
    
    /**
     * Represents a class used to inject a javax.transaction.UserTransaction as a resource.
     */
    public static final String STEREOTYPE_USER_TRANSACTION = profile.get("USER_TRANSACTION");

    /**
     * Represents a class used to inject a javax.sql.DataSource as a resource.
     */
    public static final String STEREOTYPE_DATA_SOURCE = profile.get("DATA_SOURCE");
    
    /**
     * Represents an  class for a session bean.
     */
    public static final String STEREOTYPE_INTERCEPTOR = profile.get("INTERCEPTOR");
    
    /**
     * Represents a dependency from an actor that is identified to
     * apply a run-as identity to the bean when making calls.
     */
    public static final String STEREOTYPE_SECURITY_RUNAS = profile.get("SECURITY_RUN_AS");
    
    /**
     * Represents a callback listener class for entity, session and 
     * message driven bean classes.
     */
    public static final String STEREOTYPE_LISTENER = profile.get("LISTENER");

    /**
     * Specifies the session bean operation as a
     * Timer Service timeout callback method.
     */
    public static final String STEREOTYPE_SERVICE_TIMER_TIMEOUT = profile.get("SERVICE_TIMER_TIMEOUT");
    
    /**
     * Represents a LOB type attribute.
     */
    public static final String STEREOTYPE_LOB = profile.get("LOB");

    /**
     * The tagged value indicating the finder method parameter result type
     * is assigned to be the first/index.
     */
    public static final String STEREOTYPE_FINDER_RESULT_TYPE_FIRST = profile.get("RESULT_TYPE_FIRST");
    
    /**
     * The tagged value indicating the finder method parameter result type 
     * is assigned to be the max results to return.
     */
    public static final String STEREOTYPE_FINDER_RESULT_TYPE_MAX = profile.get("RESULT_TYPE_MAX");
    
    /* ----------------- Tagged Values -------------------- */
    
    /**
     * 
     */
    public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
    
    /**
     * The tagged value indicating the EJB query.
     */
    public static final String TAGGEDVALUE_EJB_QUERY = profile.get("QUERY");
    
    /**
     * The tagged value indicating the view type for the 
     * class or operation.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = profile.get("VIEW_TYPE");
    
    /**
     * The tagged value indicating the transaction property.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = profile.get("TRANSACTION_TYPE");
    
    /**
     * The tagged value indicating the transaction demarcation
     * strategy.  This only applies at the class level of a 
     * session and message-driven bean.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT = profile.get("TRANSACTION_MANAGEMENT");
    
    /**
     * The tagged value indicating whether to not allow synthetic
     * (auto generated) create/constructors.
     */
    public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD = profile.get("NO_SYNTHETIC_CREATE_METHOD");
    
    /**
     * The tagged value indicating the temporal type specified on attributes or
     * finder method arguments of temporal nature.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE = profile.get("TEMPORAL_TYPE");
    
    /**
     * The tagged value indicating the entity table name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ENTITY_TABLE_NAME = profile.get("TABLE");
    
    /**
     * The tagged value indicating the fetch type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FETCH_TYPE = profile.get("FETCH_TYPE");
    
    /**
     * The tagged value indicating the cascade type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE = profile.get("CASCADE_TYPE");

    /**
     * The tagged value indicating the enumeration type (ORDINAL, STRING).
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ENUMERATION_TYPE = profile.get("ENUMERATION_TYPE");
    
    /**
     * The tagged value indicating the generator type.
     */
	public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE = profile.get("GENERATOR_TYPE");
    
    /**
     * The tagged value indicating the generator name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME = profile.get("GENERATOR_NAME");
    
    /**
     * The tagged value indicating the generator source name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME = profile.get("GENERATOR_SOURCE_NAME");
    
    /**
     * The tagged value indicating the primary key column value for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE = 
        profile.get("GENERATOR_PK_COLUMN_VALUE");
    
    /**
     * The tagged value indicating the initial value for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE = profile.get("GENERATOR_INITIAL_VALUE");
    
    /**
     * The tagged value indicating the step size for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE = 
        profile.get("GENERATOR_ALLOCATION_SIZE");
    
    /**
     * The tagged value indicating the SQL definition for a column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION = profile.get("COLUMN_DEFINITION");
    
    /**
     * The tagged value for the precision in a float/double column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION = profile.get("COLUMN_PRECISION");
    
    /**
     * The tagged value for the scale in a float/double column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE = profile.get("COLUMN_SCALE");
    
    /**
     * The tagged value to represent a column that is nullable.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE = profile.get("COLUMN_NULLABLE");
    
    /**
     * The tagged value that specifies whether a mapped column should be 
     * included in SQL INSERT statements.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_INSERT = profile.get("COLUMN_INSERT");

    /**
     * The tagged value that specifies whether a mapped column should be included 
     * in SQL UPDATE statements.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_UPDATE = profile.get("COLUMN_UPDATE");
    
    /**
     * The tagged value that indicates the order by logic on the 
     * Many side of the One-to-Many and Many-to-Many relationships.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ORDERBY = profile.get("ORDER_BY");
    
    /**
     * The tagged value indicating the underlying relationship may 
     * be NULL.  If set to false, non-null relationship must always 
     * exist.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_OPTIONAL = profile.get("ATTRIBUTE_PERSISTENCE_OPTIONAL");
    
    /**
     * Support for entity inheritance strategy with permitted values:
     * <ul><li>SINGLE_TABLE : one table per hierarchy</li>
     * <li>TABLE_PER_CLASS : one table per class in hierarchy</li> 
     * <li>JOINED : one table per class</li></ul> 
     * See EJB 3.0 documentation for specific details.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_INHERITANCE = profile.get("ENTITY_INHERITANCE");
    
    /**
     * For the inheritance SINGLE_TABLE and JOINED strategies, the persistence
     * provider will use a specified discriminator type column.  The supported
     * discriminator types are:
     * <ul><li>STRING</li><li>CHAR</li><li>INTEGER</li></ul>
     * See the EJB 3.0 documentation for specific details.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_TYPE = profile.get("ENTITY_DISCRIMINATOR_TYPE");
    
    /**
     * The tagged value indicating that the row is an entity of 
     * the annotated entity type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_VALUE = 
        profile.get("ENTITY_DISCRIMINATOR_VALUE");
    
    /**
     * The tagged value indicating the name of the column used 
     * for the discriminator
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN = 
        profile.get("ENTITY_DISCRIMINATOR_COLUMN");
    
    /**
     * The tagged value representing the SQL used in generation 
     * of DDL for the discriminator column
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_DEFINITION = 
        profile.get("ENTITY_DISCRIMINATOR_COLUMN_DEFINITION");
    
    /**
     * The tagged value representing the column length for the 
     * String discriminator column type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_LENGTH = 
        profile.get("ENTITY_DISCRIMINATOR_COLUMN_LENGTH");

    /**
     * The tagged value representing the persistence context 
     * unit name (EntityManager)
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME = 
        profile.get("SERVICE_PERSISTENCE_CONTEXT_UNIT_NAME");

    /**
     * The tagged value representing the persistence context 
     * transaction/extended type
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE = 
        profile.get("SERVICE_PERSISTENCE_CONTEXT_TYPE");

    /**
     * The tagged value representing the flush mode on bean operation.
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_FLUSH_MODE = profile.get("SERVICE_PERSISTENCE_FLUSH_MODE");
    
    /**
     * The tagged value representing the session EJB type (Stateless or Stateful)
     */
    public static final String TAGGEDVALUE_EJB_SESSION_TYPE = profile.get("SERVICE_TYPE");

    /**
     * The tagged value representing whether to permit all roles to execute
     * operations in the bean.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_PERMIT_ALL = profile.get("SECURITY_PERMIT_ALL");

    /**
     * The tagged value representing whether to deny all roles access rights
     * to execute operations in the bean.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_DENY_ALL = profile.get("SECURITY_DENY_ALL");
    
    /**
     * The tagged value representing the security domain to sepecify at
     * the session bean class level.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_REALM = profile.get("SECURITY_REALM");

    /**
     * The tagged value representing the JMS message driven bean
     * acknowledge mode.
     */
    public static final String TAGGEDVALUE_EJB_MDB_ACKNOWLEDGE_MODE = profile.get("MDB_ACKNOWLEDGE_MODE");

    /**
     * The tagged value representing the JMS message driven bean
     * destination JNDI name.
     */
    public static final String TAGGEDVALUE_EJB_MDB_DESTINATION = profile.get("MDB_DESTINATION");

    /**
     * The tagged value representing the JMS message driven bean
     * destination type.
     */
    public static final String TAGGEDVALUE_EJB_MDB_DESTINATION_TYPE = profile.get("MDB_DESTINATION_TYPE");

    /**
     * The tagged value representing the JMS message driven bean
     * selector logic.
     */
    public static final String TAGGEDVALUE_EJB_MDB_SELECTOR = profile.get("MDB_SELECTOR");

    /**
     * The tagged value representing the JMS message driven bean
     * topic subscription durability mode.
     */
    public static final String TAGGEDVALUE_EJB_MDB_DURABILITY = profile.get("MDB_SUBSCRIPTION_DURABILITY");

    /**
     * The tagged value representing the session bean remote interface
     * JNDI name.
     */
    public static final String TAGGEDVALUE_EJB_SESSION_JNDI_NAME_REMOTE = profile.get("SERVICE_JNDI_NAME_REMOTE");

    /**
     * The tagged value representing the session bean local interface
     * JNDI name.
     */
    public static final String TAGGEDVALUE_EJB_SESSION_JNDI_NAME_Local = profile.get("SERVICE_JNDI_NAME_LOCAL");

    /**
     * The tagged value indicating the MDB or session bean
     * container configuration name specific to JBoss.
     */
    public static final String TAGGEDVALUE_EJB_CONTAINER_CONFIGURATION = profile.get("CONTAINER_CONFIGURATION");

    /**
     * Defines the association collection type
     */
    public static final String TAGGEDVALUE_ASSOCIATION_COLLECTION_TYPE = profile.get("ASSOCIATION_COLLECTION_TYPE");

    /**
     * Defines whether to exclude the default interceptors for the session operation. 
     */
    public static final String TAGGEDVALUE_SERVICE_INTERCEPTOR_EXCLUDE_DEFAULT = 
        profile.get("EXCLUDE_DEFAULT_INTERCEPTORS");
    
    /**
     * Defines whether to exclude the class interceptors for the session operation. 
     */
    public static final String TAGGEDVALUE_SERVICE_INTERCEPTOR_EXCLUDE_CLASS = 
        profile.get("EXCLUDE_CLASS_INTERCEPTORS");

}