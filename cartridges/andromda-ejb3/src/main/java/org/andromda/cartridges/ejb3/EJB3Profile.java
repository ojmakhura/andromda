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
     * 'Entity' Specifies the entity bean stereotype.
     */
    public static final String STEREOTYPE_ENTITY =
        profile.get("ENTITY");

    /**
     * 'MappedSuperclass' Specifies this class as a mapped/embeddable super class.
     */
    public static final String STEREOTYPE_MAPPED_SUPERCLASS =
        profile.get("MAPPED_SUPERCLASS");

    /**
     * 'Service' Specifies the service bean stereotype.
     */
    public static final String STEREOTYPE_SERVICE =
        profile.get("SERVICE");

    /**
     * 'MessageDriven' Specifies the JMS message driven bean stereotype.
     */
    public static final String STEREOTYPE_MESSAGE_DRIVEN =
        profile.get("MESSAGE_DRIVEN");

    /**
     * 'CreateMethod' Specifies the create method stereotype - used in entity POJO
     * and session bean.
     */
    public static final String STEREOTYPE_CREATE_METHOD =
        profile.get("CREATE_METHOD");

    /**
     * Specifies the select method stereotype - used in EJB2.x
     * This may be deprecated in future releases.
     */
    public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";

    /**
     * 'EnvEntry' Specifies the environment entry stereotype used on static variables
     * to permit Resource injection.
     */
    public static final String STEREOTYPE_ENV_ENTRY =
        profile.get("ENV_ENTRY");

    /**
     * 'Version' Specifies the version stereotype - optimistic lock value of an attribute of an entity.
     */
    public static final String STEREOTYPE_VERSION =
        profile.get("VERSION");

    /**
     * 'ValueRef' Represents a reference to a value object - used in EJB2.x
     * This may be deprecated in future releases.
     */
    public static final String STEREOTYPE_VALUE_REF =
        profile.get("VALUE_REF");

    /**
     * 'PersistenceContext' Represents a persistence context instance referenced from a session bean.
     */
    public static final String STEREOTYPE_PERSISTENCE_CONTEXT =
        profile.get("PERSISTENCE_CONTEXT");

    /**
     * 'ResourceRef' Represents a reference to a resource i.e. UserTransaction or DataSource.
     */
    public static final String STEREOTYPE_RESOURCE_REF =
        profile.get("RESOURCE_REF");

    /**
     * 'UserTransaction' Represents a class used to inject a javax.transaction.UserTransaction as a resource.
     */
    public static final String STEREOTYPE_USER_TRANSACTION =
        profile.get("USER_TRANSACTION");

    /**
     * 'DataSource' Represents a class used to inject a javax.sql.DataSource as a resource.
     */
    public static final String STEREOTYPE_DATA_SOURCE =
        profile.get("DATA_SOURCE");

    /**
     * 'Interceptor' Represents an interceptor class for a session or message-driven bean.
     */
    public static final String STEREOTYPE_INTERCEPTOR =
        profile.get("INTERCEPTOR");

    /**
     * 'RunAs' Represents a dependency from an actor that is identified to
     * apply a run-as identity to the bean when making calls.
     */
    public static final String STEREOTYPE_SECURITY_RUNAS =
        profile.get("SECURITY_RUN_AS");

    /**
     * 'Listener' Represents a callback listener class for entity, session and
     * message driven bean classes.
     */
    public static final String STEREOTYPE_LISTENER =
        profile.get("LISTENER");

    /**
     * 'Timeout' Specifies the session bean operation as a
     * Timer Service timeout callback method.
     */
    public static final String STEREOTYPE_SERVICE_TIMER_TIMEOUT =
        profile.get("SERVICE_TIMER_TIMEOUT");

    /**
     * 'FirstResult' The stereotype indicating the finder method parameter result type
     * is assigned to be the first/index.
     */
    public static final String STEREOTYPE_FINDER_RESULT_TYPE_FIRST =
        profile.get("RESULT_TYPE_FIRST");

    /**
     * 'MaxResult' The stereotype indicating the finder method parameter result type
     * is assigned to be the max results to return.
     */
    public static final String STEREOTYPE_FINDER_RESULT_TYPE_MAX =
        profile.get("RESULT_TYPE_MAX");

    /**
     * 'PostConstruct' Specifies the session/message-driven bean operation as a post-construct callback
     */
    public static final String STEREOTYPE_POST_CONSTRUCT =
        profile.get("POST_CONSTRUCT");

    /**
     * 'PreDestroy' Specifies the session/message-driven bean operation as a pre-destroy callback
     */
    public static final String STEREOTYPE_PRE_DESTROY =
        profile.get("PRE_DESTROY");

    /**
     * 'PostActivate' Specifies the session bean operation as a post-activate callback
     */
    public static final String STEREOTYPE_POST_ACTIVATE =
        profile.get("POST_ACTIVATE");

    /**
     * 'PrePassivate' Specifies the session bean operation as a pre-passivate callback
     */
    public static final String STEREOTYPE_PRE_PASSIVATE =
        profile.get("PRE_PASSIVATE");

    /**
     * 'PrePersist' Specifies the entity bean operation as a pre-persist callback
     */
    public static final String STEREOTYPE_PRE_PERSIST =
        profile.get("PRE_PERSIST");

    /**
     * 'PostPersist' Specifies the entity operation as a post-persist callback
     */
    public static final String STEREOTYPE_POST_PERSIST =
        profile.get("POST_PERSIST");

    /**
     * 'PreRemove' Specifies the entity bean operation as a pre-remove callback
     */
    public static final String STEREOTYPE_PRE_REMOVE =
        profile.get("PRE_REMOVE");

    /**
     * 'PostRemove' Specifies the entity bean operation as a post-remove callback
     */
    public static final String STEREOTYPE_POST_REMOVE =
        profile.get("POST_REMOVE");

    /**
     * 'PreUpdate' Specifies the entity bean operation as a pre-update callback
     */
    public static final String STEREOTYPE_PRE_UPDATE =
        profile.get("PRE_UPDATE");

    /**
     * 'PostUpdate' Specifies the entity bean operation as a post-update callback
     */
    public static final String STEREOTYPE_POST_UPDATE =
        profile.get("POST_UPDATE");

    /**
     * 'PostLoad' Specifies the entity bean operation as a post-load callback
     */
    public static final String STEREOTYPE_POST_LOAD =
        profile.get("POST_LOAD");

    /**
     * 'Seam' Specifies the bean is a Seam component
     */
    public static final String STEREOTYPE_SEAM_COMPONENT =
        profile.get("SEAM_COMPONENT");

    /**
     * 'Startup' Specifies that a session scope component is started
     * immediately at session creation time, unless dependencies
     * are specified.
     */
    public static final String STEREOTYPE_SEAM_COMPONENT_STARTUP =
        profile.get("SEAM_COMPONENT_STARTUP");

    /**
     * 'In' Specifies the bean attribute is a component attribute to be injected from a context variable
     */
    public static final String STEREOTYPE_SEAM_BIJECTION_IN =
        profile.get("SEAM_BIJECTION_IN");

    /**
     * 'Out' Specifies the bean attribute is a component attribute to be outjected from a context variable
     */
    public static final String STEREOTYPE_SEAM_BIJECTION_OUT =
        profile.get("SEAM_BIJECTION_OUT");

    /**
     *  'Unwrap' Specifies that the object returned by the annotated getter method
     *  is the thing that is injected instead of the component instance itself.
     */
    public static final String STEREOTYPE_SEAM_BIJECTION_UNWRAP =
        profile.get("SEAM_BIJECTION_UNWRAP");

    /**
     * 'Factory' When this stereotype is used on an operation which return void
     * that operation will be used to initialize the value of the named
     * context variable, when the context variable has no value.
     *
     * When it is used on an operation that returns a value then Seam
     * should use that value to initialize the value of the named context
     * variable, when the context variable has no value.
     *
     * The context variable is specified by tagged value
     * andromda.seam.bijection.factory.value. If the method is a getter
     * method, default to the JavaBeans property name.
     *
     * If no scope is explicitly specified by tagged value
     * andromda.seam.bijection.factory.scope, the scope of the component
     * with the @Factory method is used (unless the component is stateless,
     * in which case the EVENT context is used).
     */
    public static final String STEREOTYPE_SEAM_BIJECTION_FACTORY =
        profile.get("SEAM_BIJECTION_FACTORY");

    /**
     * 'Logger' Specifies that a component field is to be injected with an instance
     * of org.jboss.seam.log.Log.
     */
    public static final String STEREOTYPE_SEAM_BIJECTION_LOGGER =
        profile.get("SEAM_BIJECTION_LOGGER");

    /**
     * 'RequestParameter' Specifies that a component attribute is to be injected with the
     * value of a request parameter. Basic type conversions are performed
     * automatically.
     */
    public static final String STEREOTYPE_SEAM_BIJECTION_REQUEST_PARAMETER =
        profile.get("SEAM_BIJECTION_REQUEST_PARAMETER");

    /**
     * 'Create' Indicates that the method is a Seam component lifecycle operation
     * and should be called when an instance of the component is instantiated by
     * Seam.
     */
    public static final String STEREOTYPE_SEAM_LIFECYCLE_CREATE =
        profile.get("SEAM_LIFECYCLE_CREATE");

    /**
     * 'Destroy' Indicates that the method is a Seam component lifecycle operation
     * and should be called when the context ends and its context variables
     * are destroyed.
     * All SFSB components must define a Destroy method to guarantee
     * destruction of the SFSB when the context ends.
     */
    public static final String STEREOTYPE_SEAM_LIFECYCLE_DESTROY =
        profile.get("SEAM_LIFECYCLE_DESTROY");

    /**
     * 'Begin' Specifies that a long-running conversation begins when this method
     * returns a non-null outcome without exception.
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_BEGIN =
        profile.get("SEAM_CONVERSATION_BEGIN");

    /**
     * 'End' Specifies that a long-running conversation ends when this method
     * returns a non-null outcome without exception.
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_END =
        profile.get("SEAM_CONVERSATION_END");

    /**
     * 'BeginTask' Specifies that a long-running conversation ends when this method
     * returns a non-null outcome without exception.
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_BEGIN_TASK =
        profile.get("SEAM_CONVERSATION_BEGIN_TASK");

    /**
     * 'StartTask' "Starts" a jBPM task. Specifies that a long-running conversation
     * begins when this method returns a non-null outcome without exception.
     *
     * This conversation is associated with the jBPM task specified in the
     * named request parameter. Within the context of this conversation, a
     * business process context is also defined, for the business process
     * instance of the task instance.
     *
     * The jBPM TaskInstance will be available in a request context variable
     * named taskInstance. The jPBM ProcessInstance will be available in a
     * request context variable named processInstance. (Of course, these
     * objects are available for injection via @In.)
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_START_TASK =
        profile.get("SEAM_CONVERSATION_START_TASK");

    /**
     * 'EndTask' "Ends" a jBPM task. Specifies that a long-running conversation ends
     * when this method returns a non-null outcome, and that the current
     * task is complete. Triggers a jBPM transition. The actual transition
     * triggered will be the default transition unless the application has
     * called Transition.setName() on the built-in component named
     * transition.
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_END_TASK =
        profile.get("SEAM_CONVERSATION_END_TASK");

    /**
     * 'CreateProcess' Creates a new jBPM process instance when the method returns a
     * non-null outcome without exception. The ProcessInstance object will
     * be available in a context variable named processInstance.
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_CREATE_PROCESS =
        profile.get("SEAM_CONVERSATION_CREATE_PROCESS");

    /**
     * 'ResumeProcess' Re-enters the scope of an existing jBPM process instance when the
     * method returns a non-null outcome without exception. The
     * ProcessInstance object will be available in a context variable
     * named processInstance.
     */
    public static final String STEREOTYPE_SEAM_CONVERSATION_RESUME_PROCESS =
        profile.get("SEAM_CONVERSATION_RESUME_PROCESS");

    /**
     * 'Transactional' Specifies that a JavaBean component should have a similar
     * transactional behavior to the default behavior of a session bean
     * component. i.e. method invocations should take place in a
     * transaction, and if no transaction exists when the method is
     * called, a transaction will be started just for that method. This
     * annotation may be applied at either class or method level.
     */
    public static final String STEREOTYPE_SEAM_TRANSACTION_TRANSACTIONAL =
        profile.get("SEAM_TRANSACTION_TRANSACTIONAL");

    /**
     * 'Rollback' If the outcome of the method matches any of the listed outcomes,
     * or if no outcomes are listed, set the transaction to rollback only
     * when the method completes.
     */
    public static final String STEREOTYPE_SEAM_TRANSACTION_ROLLBACK =
        profile.get("SEAM_TRANSACTION_ROLLBACK");

    /**
     * 'Valid' Specifies that the Hibernate Validator should validate this and related
     * component attributes before an action listener Seam component method is
     * invoked.
     */
    public static final String STEREOTYPE_SEAM_VALIDATION_VALID =
        profile.get("SEAM_VALIDATION_VALID");

    /**
     * 'Validator' Specifies that the validator should validate all Seam components marked
     * with @Valid annotation before the method is invoked. Use of
     * tagged values for outcome when validation fails and for refreshing
     * entities when validation fails is provided.
     */
    public static final String STEREOTYPE_SEAM_VALIDATION_VALIDATOR =
        profile.get("SEAM_VALIDATION_VALIDATOR");

    /**
     * 'WebRemote' Indicates that the annotated method may be called from client-side
     * JavaScript. The exclude property is optional and allows objects to
     * be excluded from the result's object graph.
     */
    public static final String STEREOTYPE_SEAM_WEBREMOTE =
        profile.get("SEAM_WEBREMOTE");

    /**
     * 'Interceptor' This stereotype appear on Seam interceptor classes.
     *
     * Please refer to the documentation for the EJB 3.0 specification
     * for information about the annotations required for EJB interceptor
     * definition.
     */
    public static final String STEREOTYPE_SEAM_INTERCEPTOR =
        profile.get("SEAM_INTERCEPTOR");

    /**
     * 'Asynchronous' Specifies that the method call is processed asynchronously.
     */
    public static final String STEREOTYPE_SEAM_ASYNCHRONOUS =
        profile.get("SEAM_ASYNCHRONOUS");

    /**
     * 'Duration' Specifies that a parameter of the asynchronous call is the duration
     * before the call is processed (or first processed for recurring
     * calls).
     */
    public static final String STEREOTYPE_SEAM_ASYNCHRONOUS_DURATION =
        profile.get("SEAM_ASYNCHRONOUS_DURATION");

    /**
     * 'Expiration' Specifies that a parameter of the asynchronous call is the datetime
     * at which the call is processed (or first processed for recurring
     * calls).
     */
    public static final String STEREOTYPE_SEAM_ASYNCHRONOUS_EXPIRATION =
        profile.get("SEAM_ASYNCHRONOUS_EXPIRATION");

    /**
     * 'IntervalDuration' Specifies that an asynchronous method call recurs, and that the
     * annotated parameter is duration between recurrences.
     */
    public static final String STEREOTYPE_SEAM_ASYNCHRONOUS_INTERVAL_DURATION =
        profile.get("SEAM_ASYNCHRONOUS_INTERVAL_DURATION");

    /**
     * 'DataModel' Exposes an attribute of type List, Map, Set or Object[] as a JSF
     * DataModel into the scope of the owning component (or the EVENT
     * scope if the owning component is STATELESS). In the case of Map,
     * each row of the DataModel is a Map.Entry.
     */
    public static final String STEREOTYPE_SEAM_DATA_DATAMODEL =
        profile.get("SEAM_DATA_DATAMODEL");

    /**
     * 'DataModelSelection' Injects the selected value from the JSF DataModel (this is the
     * element of the underlying collection, or the map value).
     */
    public static final String STEREOTYPE_SEAM_DATA_DATAMODEL_SELECTION =
        profile.get("SEAM_DATA_DATAMODEL_SELECTION");

    /**
     * 'DataModelSelectionIndex' Exposes the selection index of the JSF DataModel as an attribute
     * of the component (this is the row number of the underlying
     * collection, or the map key).
     */
    public static final String STEREOTYPE_SEAM_DATA_DATAMODEL_SELECTION_INDEX =
        profile.get("SEAM_DATA_DATAMODEL_SELECTION_INDEX");

    /* ----------------- Tagged Values -------------------- */

    /**
     * andromda_ejb_generateCMR
     */
    public static final String TAGGEDVALUE_GENERATE_CMR = "andromda_ejb_generateCMR";

    /**
     * 'andromda_ejb_query' The tagged value indicating the EJB query.
     */
    public static final String TAGGEDVALUE_EJB_QUERY =
        profile.get("QUERY");

    /**
     * 'andromda_ejb_viewType' The tagged value indicating the view type for the
     * class or operation.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE =
        profile.get("VIEW_TYPE");

    /**
     * 'andromda_ejb_transaction_type' The tagged value indicating the transaction property.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE =
        profile.get("TRANSACTION_TYPE");

    /**
     * 'andromda_ejb_transaction_management' The tagged value indicating the transaction demarcation
     * strategy.  This only applies at the class level of a
     * session and message-driven bean.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT =
        profile.get("TRANSACTION_MANAGEMENT");

    /**
     * 'andromda_ejb_noSyntheticCreateMethod' The tagged value indicating whether to not allow synthetic
     * (auto generated) create/constructors.
     */
    public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD =
        profile.get("NO_SYNTHETIC_CREATE_METHOD");

    /**
     * 'andromda_persistence_temporal_type' The tagged value indicating the temporal type specified on attributes or
     * finder method arguments of temporal nature.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE =
        profile.get("TEMPORAL_TYPE");

    /**
     * 'andromda_hibernate_type' The tagged value indicating the overridden type specified on attributes or
     * finder method arguments , to generate @org.hibernate.annotations.Type annotations.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_OVERRIDE_TYPE =
        profile.get("OVERRIDE_TYPE");

    /**
     * 'andromda_persistence_table' The tagged value indicating the entity table name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ENTITY_TABLE_NAME =
        profile.get("TABLE");

    /**
     * 'andromda_persistence_fetch_type' The tagged value indicating the fetch type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FETCH_TYPE =
        profile.get("FETCH_TYPE");

    /**
     * 'andromda_persistence_cascade_type' The tagged value indicating the cascade type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE =
        profile.get("CASCADE_TYPE");

    /**
     * 'andromda_persistence_enumeration_type' The tagged value indicating the enumeration type (ORDINAL, STRING).
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ENUMERATION_TYPE =
        profile.get("ENUMERATION_TYPE");

    /**
     * 'andromda_persistence_generator_type' The tagged value indicating the generator type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE =
        profile.get("GENERATOR_TYPE");

    /**
     * 'andromda_persistence_generator_name' The tagged value indicating the generator name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME =
        profile.get("GENERATOR_NAME");

    /**
     * 'andromda_persistence_generator_genericStrategy' The tagged value indicating the generator generic strategy.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_GENERIC_STRATEGY =
        profile.get("GENERATOR_GENERIC_STRATEGY");

    /**
     * 'andromda_persistence_generator_source_name' The tagged value indicating the generator source name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME =
        profile.get("GENERATOR_SOURCE_NAME");

    /**
     * 'andromda_persistence_generator_pkcolumn_value' The tagged value indicating the primary key column value for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE =
        profile.get("GENERATOR_PK_COLUMN_VALUE");

    /**
     * 'andromda_persistence_generator_initial_value' The tagged value indicating the initial value for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE =
        profile.get("GENERATOR_INITIAL_VALUE");

    /**
     * 'andromda_persistence_generator_allocation_size' The tagged value indicating the step size for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE =
        profile.get("GENERATOR_ALLOCATION_SIZE");

    /**
     * 'andromda_persistence_column_definition' The tagged value indicating the SQL definition for a column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION =
        profile.get("COLUMN_DEFINITION");

    /**
     * 'andromda_persistence_column_precision' The tagged value for the precision in a float/double column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION =
        profile.get("COLUMN_PRECISION");

    /**
     * 'andromda_persistence_column_scale' The tagged value for the scale in a float/double column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE =
        profile.get("COLUMN_SCALE");

    /**
     * 'andromda_persistence_column_nullable' The tagged value to represent a column that is nullable.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE =
        profile.get("COLUMN_NULLABLE");

    /**
     * 'andromda_persistence_column_insert' The tagged value that specifies whether a mapped column should be
     * included in SQL INSERT statements.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_INSERT =
        profile.get("COLUMN_INSERT");

    /**
     * 'andromda_persistence_column_update' The tagged value that specifies whether a mapped column should be included
     * in SQL UPDATE statements.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_UPDATE =
        profile.get("COLUMN_UPDATE");

    /**
     * 'andromda_persistence_orderBy' The tagged value that indicates the order by logic on the
     * Many side of the One-to-Many and Many-to-Many relationships.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ORDERBY =
        profile.get("ORDER_BY");

    /**
     * 'andromda_persistence_optional' The tagged value indicating the underlying relationship may
     * be NULL.  If set to false, non-null relationship must always
     * exist.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_OPTIONAL =
        profile.get("ATTRIBUTE_PERSISTENCE_OPTIONAL");

    /**
     * 'andromda_persistence_inheritance' Support for entity inheritance strategy with permitted values:
     * <ul><li>SINGLE_TABLE : one table per hierarchy</li>
     * <li>TABLE_PER_CLASS : one table per class in hierarchy</li>
     * <li>JOINED : one table per class</li></ul>
     * See EJB 3.0 documentation for specific details.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_INHERITANCE =
        profile.get("ENTITY_INHERITANCE");

    /**
     * 'andromda_persistence_discriminator_type' For the inheritance SINGLE_TABLE and JOINED strategies, the persistence
     * provider will use a specified discriminator type column.  The supported
     * discriminator types are:
     * <ul><li>STRING</li><li>CHAR</li><li>INTEGER</li></ul>
     * See the EJB 3.0 documentation for specific details.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_TYPE =
        profile.get("ENTITY_DISCRIMINATOR_TYPE");

    /**
     * 'andromda_persistence_discriminator_value' The tagged value indicating that the row is an entity of
     * the annotated entity type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_VALUE =
        profile.get("ENTITY_DISCRIMINATOR_VALUE");

    /**
     * 'andromda_persistence_discriminator_column_name'
     * The tagged value indicating the name of the column used
     * for the discriminator
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN =
        profile.get("ENTITY_DISCRIMINATOR_COLUMN");

    /**
     * 'andromda_persistence_discriminator_column_definition'
     * The tagged value representing the SQL used in generation
     * of DDL for the discriminator column
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_DEFINITION =
        profile.get("ENTITY_DISCRIMINATOR_COLUMN_DEFINITION");

    /**
     * 'andromda_persistence_discriminator_column_length'
     * The tagged value representing the column length for the
     * String discriminator column type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_LENGTH =
        profile.get("ENTITY_DISCRIMINATOR_COLUMN_LENGTH");

    /**
     * 'andromda_service_persistence_context_unit_name'
     * The tagged value representing the persistence context
     * unit name (EntityManager)
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME =
        profile.get("SERVICE_PERSISTENCE_CONTEXT_UNIT_NAME");

    /**
     * 'andromda_service_persistence_context_unit_type'
     * The tagged value representing the persistence context
     * transaction/extended type
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE =
        profile.get("SERVICE_PERSISTENCE_CONTEXT_TYPE");

    /**
     * 'andromda_service_persistence_context_datasource'
     * The tagged value representing the persistence context
     * datasource JNDI name
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_DATASOURCE =
        profile.get("SERVICE_PERSISTENCE_CONTEXT_DATASOURCE");

    /**
     * 'andromda_service_persistence_flush_mode'
     * The tagged value representing the flush mode on bean operation.
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_FLUSH_MODE =
        profile.get("SERVICE_PERSISTENCE_FLUSH_MODE");

    /**
     * 'andromda_persistence_lob_type'
     * The tagged value overriding the default LOB type for attribute.
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_LOB_TYPE =
        profile.get("LOB_TYPE");

    /**
     * 'andromda_service_type'
     * The tagged value representing the session EJB type (Stateless or Stateful)
     */
    public static final String TAGGEDVALUE_EJB_SESSION_TYPE =
        profile.get("SERVICE_TYPE");

    /**
     * 'andromda_service_security_permitAll'
     * The tagged value representing whether to permit all roles to execute
     * operations in the bean.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_PERMIT_ALL =
        profile.get("SECURITY_PERMIT_ALL");

    /**
     * 'andromda_service_security_denyAll'
     * The tagged value representing whether to deny all roles access rights
     * to execute operations in the bean.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_DENY_ALL =
        profile.get("SECURITY_DENY_ALL");

    /**
     * 'andromda_service_security_realm'
     * The tagged value representing the security domain to specify at
     * the session bean class level.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_REALM =
        profile.get("SECURITY_REALM");

    /**
     * 'andromda_ejb_mdb_acknowledge_mode'
     * The tagged value representing the JMS message driven bean
     * acknowledge mode.
     */
    public static final String TAGGEDVALUE_EJB_MDB_ACKNOWLEDGE_MODE =
        profile.get("MDB_ACKNOWLEDGE_MODE");

    /**
     * 'andromda_ejb_mdb_destination'
     * The tagged value representing the JMS message driven bean
     * destination JNDI name.
     */
    public static final String TAGGEDVALUE_EJB_MDB_DESTINATION =
        profile.get("MDB_DESTINATION");

    /**
     * 'andromda_ejb_mdb_destination_type'
     * The tagged value representing the JMS message driven bean
     * destination type.
     */
    public static final String TAGGEDVALUE_EJB_MDB_DESTINATION_TYPE =
        profile.get("MDB_DESTINATION_TYPE");

    /**
     * 'andromda_ejb_mdb_selector'
     * The tagged value representing the JMS message driven bean
     * selector logic.
     */
    public static final String TAGGEDVALUE_EJB_MDB_SELECTOR =
        profile.get("MDB_SELECTOR");

    /**
     * 'andromda_ejb_mdb_subscription_durability'
     * The tagged value representing the JMS message driven bean
     * topic subscription durability mode.
     */
    public static final String TAGGEDVALUE_EJB_MDB_DURABILITY =
        profile.get("MDB_SUBSCRIPTION_DURABILITY");

    /**
     * 'andromda_service_jndi_remote'
     * The tagged value representing the session bean remote interface
     * JNDI name.
     */
    public static final String TAGGEDVALUE_EJB_SESSION_JNDI_NAME_REMOTE =
        profile.get("SERVICE_JNDI_NAME_REMOTE");

    /**
     * 'andromda_service_jndi_local'
     * The tagged value representing the session bean local interface
     * JNDI name.
     */
    public static final String TAGGEDVALUE_EJB_SESSION_JNDI_NAME_Local =
        profile.get("SERVICE_JNDI_NAME_LOCAL");

    /**
     * 'andromda_ejb_mdb_pool_size_min'
     * The tagged value indicating the bean minimum pool size
     */
    public static final String TAGGEDVALUE_EJB_MDB_MINIMUM_POOL_SIZE =
        profile.get("MDB_MINIMUM_POOL_SIZE");

    /**
     * 'andromda_ejb_mdb_pool_size_max'
     * The tagged value indicating the bean maximum pool size
     */
    public static final String TAGGEDVALUE_EJB_MDB_MAXIMUM_POOL_SIZE =
        profile.get("MDB_MAXIMUM_POOL_SIZE");

    /**
     * 'andromda_persistence_collection_type'
     * Defines the association collection type
     */
    public static final String TAGGEDVALUE_ASSOCIATION_COLLECTION_TYPE =
        profile.get("ASSOCIATION_COLLECTION_TYPE");

    /**
     * 'andromda_persistence_collection_index_type'
     * Defines the index column type for ejb3 indexed collections
     */
    public static final String TAGGEDVALUE_ASSOCIATION_INDEX_TYPE =
        profile.get("ASSOCIATION_INDEX_TYPE");

    /**
     * 'andromda_persistence_collection_index'
     * Defines the index column for ejb3 indexed collections
     */
    public static final String TAGGEDVALUE_ASSOCIATION_INDEX =
        profile.get("ASSOCIATION_INDEX");

    /**
     * 'andromda_service_interceptor_excludeDefault'
     * Defines whether to exclude the default interceptors for the session operation.
     */
    public static final String TAGGEDVALUE_SERVICE_INTERCEPTOR_EXCLUDE_DEFAULT =
        profile.get("EXCLUDE_DEFAULT_INTERCEPTORS");

    /**
     * 'andromda_service_interceptor_excludeClass'
     * Defines whether to exclude the class interceptors for the session operation.
     */
    public static final String TAGGEDVALUE_SERVICE_INTERCEPTOR_EXCLUDE_CLASS =
        profile.get("EXCLUDE_CLASS_INTERCEPTORS");

    /**
     * 'andromda_service_interceptor_default'
     * Defines a default interceptor for a session or message-driven bean.
     */
    public static final String TAGGEDVALUE_DEFAULT_INTERCEPTOR =
        profile.get("DEFAULT_INTERCEPTOR");

    /**
     * 'andromda_persistence_entity_cache'
     * Defines the cache type for the Entity.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_CACHE =
        profile.get("ENTITY_CACHE");

    /**
     * 'andromda_persistence_association_cache'
     * Defines the cache type for a relationship collection.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE =
        profile.get("ASSOCIATION_CACHE");

    /**
     * 'andromda_persistence_associationEnd_primary'
     * Defines the owning side of a bidirectional navigable relationship.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ASSOCIATION_END_PRIMARY =
        profile.get("ASSOCIATION_END_PRIMARY");

    /**
     * 'andromda_hibernate_cascade'
     * Defines the tagged value for hibernate cascade on an association end
     */
    public static final String TAGGEDVALUE_HIBERNATE_CASCADE =
        profile.get("HIBERNATE_CASCADE_TYPE");

    /**
     * 'andromda_ejb_query_useCache'
     * Defines whether query cache is enable for query.
     */
    public static final String TAGGEDVALUE_EJB_USE_QUERY_CACHE =
        profile.get("USE_QUERY_CACHE");

    /**
     * 'andromda_webservice_parameter_style'
     * Defines the webservice parameter style
     */
    public static final String TAGGEDVALUE_WEBSERVICE_PARAMETER_STYLE =
        profile.get("WEBSERVICE_PARAMETER_STYLE");

    /**
     * 'andromda_webservice_operation_oneway'
     * Defines the webservice method as oneway
     */
    public static final String TAGGEDVALUE_WEBSERVICE_OPERATION_ONEWAY =
        profile.get("WEBSERVICE_OPERATION_ONEWAY");

    /**
     * 'andromda_webservice_operation_name'
     * Defines the webservice method name
     */
    public static final String TAGGEDVALUE_WEBSERVICE_OPERATION_NAME =
        profile.get("WEBSERVICE_OPERATION_NAME");

    /**
     * 'andromda_webservice_operation_result_name'
     * Defines the webservice method result name
     */
    public static final String TAGGEDVALUE_WEBSERVICE_OPERATION_RESULT_NAME =
        profile.get("WEBSERVICE_OPERATION_RESULT_NAME");

    /**
     * 'andromda_seam_component_name'
     * Defines the Seam component name
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_NAME =
        profile.get("SEAM_COMPONENT_NAME");

    /**
     * 'andromda_seam_component_role_scope'
     * Defines the JBoss Seam component scope type
     */
    public static final String TAGGEDVALUE_SEAM_SCOPE_TYPE =
        profile.get("SEAM_COMPONENT_SCOPE_TYPE");

    /**
     * 'andromda_seam_component_role_name'
     * Allows a Seam component to be bound to multiple contexts variables.
     * The @Name/@Scope annotations define a "default role". Each @Role
     * annotation defines an additional role.
     *
     * This tagged value specifies the context variable name.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_ROLE_NAME =
        profile.get("SEAM_COMPONENT_ROLE_NAME");

    /**
     * The context variable scope. When no scope is explicitly specified,
     * the default depends upon the component type, as above.
     *
     * Note! If multiple roles are specified then the list of scopes must
     * be in the same order as the role names.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_ROLE_SCOPE_TYPE =
        profile.get("SEAM_COMPONENT_ROLE_SCOPE_TYPE");

    /**
     * 'andromda_seam_component_intercept'
     * Determines when Seam interceptors are active. When no interception
     * type is explicitly specified, the default depends upon the
     * component type. For entity beans, the default is NEVER. For session
     * beans, message driven beans and JavaBeans, the default is ALWAYS.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_INTERCEPT =
        profile.get("SEAM_COMPONENT_INTERCEPT");

    /**
     * 'andromda_seam_component_jndiname'
     * Specifies the JNDI name that Seam will use to look up the EJB
     * component. If no JNDI name is explicitly specified, Seam will use
     * the JNDI pattern specified by org.jboss.seam.core.init.jndiPattern.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_JNDI_NAME =
        profile.get("SEAM_COMPONENT_JNDI_NAME");

    /**
     * 'andromda_seam_component_conversional_ifnotbegunoutcome'
     * Specifies that a conversation scope component is conversational,
     * meaning that no method of the component can be called unless a
     * long-running conversation started by this component is active
     * (unless the method would begin a new long-running conversation).
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_CONVERSIONAL_IFNOTBEGUNOUTCOME =
        profile.get("SEAM_COMPONENT_CONVERSIONAL_IFNOTBEGUNOUTCOME");

    /**
     * 'andromda_seam_component_startup_depends'
     * named components must be started first, if they are installed.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_STARTUP_DEPENDS =
        profile.get("SEAM_COMPONENT_STARTUP_DEPENDS");

    /**
     * 'andromda_seam_component_synchronized_timeout'
     * Specifies that a component is accessed concurrently by multiple
     * clients, and that Seam should serialize requests. If a request is
     * not able to obtain its lock on the component in the given timeout
     * period, an exception will be raised.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_SYNCHRONIZED_TIMEOUT =
        profile.get("SEAM_COMPONENT_SYNCHRONIZED_TIMEOUT");

    /**
     * 'andromda_seam_component_readonly'
     * Specifies that a JavaBean component or component method does not
     * require state replication at the end of the invocation.
     */
    public static final String TAGGEDVALUE_SEAM_COMPONENT_READONLY =
        profile.get("SEAM_COMPONENT_READONLY");

    /**
     * 'andromda_seam_bijection_in_create'
     * Specifies that Seam should instantiate the component with the same
     * name as the context variable if the context variable is undefined
     * (null) in all contexts. Default to false.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_IN_CREATE =
        profile.get("SEAM_BIJECTION_IN_CREATE");

    /**
     * 'andromda_seam_bijection_in_value'
     * Specifies the name of the context variable. Default to the name of
     * the component attribute. Alternatively, specifies a JSF EL
     * expression, surrounded by #{...}.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_IN_VALUE =
        profile.get("SEAM_BIJECTION_IN_VALUE");

    /**
     * 'andromda_seam_bijection_out_value'
     * Specifies the name of the context variable explicitly, instead of
     * using the annotated instance variable name.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_OUT_VALUE =
        profile.get("SEAM_BIJECTION_OUT_VALUE");

    /**
     * 'andromda_seam_bijection_out_scope'
     * Specifies that a component attribute that is not a Seam component
     * type is to be outjected to a specific scope at the end of the
     * invocation.
     *
     * Alternatively, if no scope is explicitly specified, the scope of
     * the component with the @Out attribute is used (or the EVENT scope
     * if the component is stateless).
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_OUT_SCOPE_TYPE =
        profile.get("SEAM_BIJECTION_OUT_SCOPE_TYPE");

    /**
     * 'andromda_seam_bijection_factory_value'
     * Specifies the name of the context variable. If the method is a
     * getter method, default to the JavaBeans property name.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_FACTORY_VALUE =
        profile.get("SEAM_BIJECTION_FACTORY_VALUE");

    /**
     * 'andromda_seam_bijection_factory_scope'
     * Specifies the scope that Seam should bind the returned value to.
     * Only meaningful for factory methods which return a value.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_FACTORY_SCOPE_TYPE =
        profile.get("SEAM_BIJECTION_FACTORY_SCOPE_TYPE");

    /**
     * 'andromda_seam_bijection_logger_value'
     * Specifies the name of the log category. Default to the name of the
     * component class.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_LOGGER_VALUE =
        profile.get("SEAM_BIJECTION_LOGGER_VALUE");

    /**
     * 'andromda_seam_bijection_requestparameter_value'
     * Specifies the name of the request parameter. Default to the name
     * of the component attribute.
     */
    public static final String TAGGEDVALUE_SEAM_BIJECTION_REQUEST_PARAMETER_VALUE =
        profile.get("SEAM_BIJECTION_REQUEST_PARAMETER_VALUE");

    /**
     * 'andromda_seam_lifecycle_observer_event'
     * Specifies that the method should be called when a component-driven
     * event of the specified type occurs.
     */
    public static final String TAGGEDVALUE_SEAM_LIFECYCLE_OBSERVER_EVENT =
        profile.get("SEAM_LIFECYCLE_OBSERVER_EVENT");

    /**
     * 'andromda_seam_conversation_begin_ifoutcome'
     * Specifies that a long-running conversation begins when this action
     * listener method returns with one of the given outcomes.
     *
     * Example: @Begin(ifOutcome={"success", "continue"})
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_IF_OUTCOME =
        profile.get("SEAM_CONVERSATION_BEGIN_IF_OUTCOME");

    /**
     * 'andromda_seam_conversation_begin_join'
     * Specifies that if a long-running conversation is already in
     * progress, the conversation context is simply propagated.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_JOIN =
        profile.get("SEAM_CONVERSATION_BEGIN_JOIN");

    /**
     * 'andromda_seam_conversation_begin_nested'
     * Specifies that if a long-running conversation is already in
     * progress, a new nested conversation context begins. The nested
     * conversation will end when the next @End  is encountered, and the
     * outer conversation will resume. It is perfectly legal for multiple
     * nested conversations to exist concurrently in the same outer
     * conversation.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_NESTED =
        profile.get("SEAM_CONVERSATION_BEGIN_NESTED");

    /**
     * 'andromda_seam_conversation_begin_flushmode'
     * Specify the flush mode of any Seam-managed persistence contexts.
     * flushMode=FlushModeType.MANUAL supports the use of atomic
     * conversations where all write operations are queued in the
     * conversation context until an explicit call to flush() (which
     * usually occurs at the end of the conversation).
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_FLUSH_MODE =
        profile.get("SEAM_CONVERSATION_BEGIN_FLUSH_MODE");

    /**
     * 'andromda_seam_conversation_begin_pageflow'
     * Specifies a jBPM process definition name that defines the pageflow
     * for this conversation.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_PAGEFLOW =
        profile.get("SEAM_CONVERSATION_BEGIN_PAGEFLOW");

    /**
     * 'andromda_seam_conversation_end_ifoutcome'
     * Specifies that a long-running conversation ends when this action
     * listener method returns with one of the given outcomes.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_END_IF_OUTCOME =
        profile.get("SEAM_CONVERSATION_END_IF_OUTCOME");

    /**
     * 'andromda_seam_conversation_end_beforeredirect'
     * By default, the conversation will not actually be destroyed until
     * after any redirect has occurred. Setting beforeRedirect=true
     * specifies that the conversation should be destroyed at the end of
     * the current request, and that the redirect will be processed in a
     * new temporary conversation context.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_END_BEFORE_REDIRECT =
        profile.get("SEAM_CONVERSATION_END_BEFORE_REDIRECT");

    /**
     * 'andromda_seam_conversation_end_evenifexception'
     * Specifies that a long-running conversation ends when this action
     * listener method throws one of the specified classes of exception.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_END_EVEN_IF_EXCEPTION =
        profile.get("SEAM_CONVERSATION_END_EVEN_IF_EXCEPTION");

    /**
     * 'andromda_seam_conversation_begintask_taskidparameter'
     * The name of a request parameter which holds the id of the task.
     * Default to "taskId", which is also the default used by the Seam
     * taskList JSF component.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_START_TASK_ID_PARAMETER =
        profile.get("SEAM_CONVERSATION_START_TASK_ID_PARAMETER");

    /**
     * 'andromda_seam_conversation_starttask_flushmode'
     * Set the flush mode of any Seam-managed Hibernate sessions or JPA
     * persistence contexts that are created during this conversation.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_START_TASK_FLUSH_MODE =
        profile.get("SEAM_CONVERSATION_START_TASK_FLUSH_MODE");

    /**
     * 'andromda_seam_conversation_begintask_taskidparameter'
     * The name of a request parameter which holds the id of the task.
     * Default to "taskId", which is also the default used by the Seam
     * taskList JSF component.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_TASK_ID_PARAMETER =
        profile.get("SEAM_CONVERSATION_BEGIN_TASK_ID_PARAMETER");

    /**
     * 'andromda_seam_conversation_begintask_flushmode'
     * Set the flush mode of any Seam-managed Hibernate sessions or JPA
     * persistence contexts that are created during this conversation.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_TASK_FLUSH_MODE =
        profile.get("SEAM_CONVERSATION_BEGIN_TASK_FLUSH_MODE");

    /**
     * 'andromda_seam_conversation_endtask_transition_name'
     * Triggers the given jBPM transition.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_TRANSITION_NAME =
        profile.get("SEAM_CONVERSATION_END_TASK_TRANSITION_NAME");

    /**
     * 'andromda_seam_conversation_endtask_ifoutcome'
     * Specifies the JSF outcome or outcomes that result in the end of
     * the task.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_IF_OUTCOME =
        profile.get("SEAM_CONVERSATION_END_TASK_IF_OUTCOME");

    /**
     * 'andromda_seam_conversation_endtask_beforeredirect'
     * By default, the conversation will not actually be destroyed until
     * after any redirect has occurred. Setting beforeRedirect=true
     * specifies that the conversation should be destroyed at the end of
     * the current request, and that the redirect will be processed in a
     * new temporary conversation context.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_BEFORE_REDIRECT =
        profile.get("SEAM_CONVERSATION_END_TASK_BEFORE_REDIRECT");

    /**
     * 'andromda_seam_conversation_createprocess_definition'
     * The name of the jBPM process definition deployed via
     * org.jboss.seam.core.jbpm.processDefinitions.
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_CREATE_PROCESS_DEFINITION =
        profile.get("SEAM_CONVERSATION_CREATE_PROCESS_DEFINITION");

    /**
     * 'andromda_seam_conversation_resumeprocess_processIdParameter'
     * The name a request parameter holding the process id. Default to
     * "processId".
     */
    public static final String TAGGEDVALUE_SEAM_CONVERSATION_RESUME_PROCESS_PROCESS_ID_PARAMETER =
        profile.get("SEAM_CONVERSATION_RESUME_PROCESS_PROCESS_ID_PARAMETER");

    /**
     * 'andromda_seam_transaction_rollback_ifoutcome'
     * The JSF outcomes that cause a transaction rollback (no outcomes is
     * interpreted to mean any outcome).
     */
    public static final String TAGGEDVALUE_SEAM_TRANSACTION_ROLLBACK_IF_OUTCOME =
        profile.get("SEAM_TRANSACTION_ROLLBACK_IF_OUTCOME");

    /**
     * 'andromda_seam_validation_outcome'
     * Returns the JSF outcome when validation fails.
     */
    public static final String TAGGEDVALUE_SEAM_VALIDATION_OUTCOME =
        profile.get("SEAM_VALIDATION_OUTCOME");

    /**
     * 'andromda_seam_validation_refreshEntities'
     * Specifies whether invalid entities in the managed state
     * should be refreshed from the database when validation fails.
     */
    public static final String TAGGEDVALUE_SEAM_VALIDATION_REFRESH_ENTITIES =
        profile.get("SEAM_VALIDATION_REFRESH_ENTITIES");

    /**
     * 'andromda_seam_webremote_exclude'
     * Excludes objects from the WebRemote call result's object graph
     * (see the Remoting chapter in Seam doc for more details).
     */
    public static final String TAGGEDVALUE_SEAM_WEBREMOTE_EXCLUDE =
        profile.get("SEAM_WEBREMOTE_EXCLUDE");

    /**
     * 'andromda_seam_interceptor_stateless'
     * Specifies that this interceptor is stateless and Seam may optimize
     * replication.
     */
    public static final String TAGGEDVALUE_SEAM_INTERCEPTOR_STATELESS =
        profile.get("SEAM_INTERCEPTOR_STATELESS");

    /**
     * 'andromda_seam_interceptor_type'
     * Specifies that this interceptor is a "client-side" interceptor
     * that is called before the EJB container.
     */
    public static final String TAGGEDVALUE_SEAM_INTERCEPTOR_TYPE =
        profile.get("SEAM_INTERCEPTOR_TYPE");

    /**
     * 'andromda_seam_interceptor_around'
     * Specifies that this interceptor is positioned higher in the stack
     * than the given interceptors.
     */
    public static final String TAGGEDVALUE_SEAM_INTERCEPTOR_AROUND =
        profile.get("SEAM_INTERCEPTOR_AROUND");

    /**
     * 'andromda_seam_interceptor_within'
     * Specifies that this interceptor is positioned deeper in the stack
     * than the given interceptors.
     */
    public static final String TAGGEDVALUE_SEAM_INTERCEPTOR_WITHIN =
        profile.get("SEAM_INTERCEPTOR_WITHIN");

    /**
     * 'andromda_seam_data_datamodel_value'
     * Name of the conversation context variable. Default to the attribute name.
     */
    public static final String TAGGEDVALUE_SEAM_DATA_DATAMODEL_VALUE =
        profile.get("SEAM_DATA_DATAMODEL_VALUE");

    /**
     * 'andromda_seam_data_datamodel_scope'
     * If scope=ScopeType.PAGE is explicitly specified, the DataModel will
     * be kept in the PAGE context.
     */
    public static final String TAGGEDVALUE_SEAM_DATA_DATAMODEL_SCOPE_TYPE =
        profile.get("SEAM_DATA_DATAMODEL_SCOPE_TYPE");

    /**
     * 'andromda_seam_data_datamodelselection_value'
     * Name of the conversation context variable. Not needed if there is
     * exactly one @DataModel in the component.
     */
    public static final String TAGGEDVALUE_SEAM_DATA_DATAMODEL_SELECTION_VALUE =
        profile.get("SEAM_DATA_DATAMODEL_SELECTION_VALUE");

    /**
     * 'andromda_seam_data_datamodelselectionindex_value'
     * Name of the conversation context variable. Not needed if there is
     * exactly one @DataModel in the component.
     */
    public static final String TAGGEDVALUE_SEAM_DATA_DATAMODEL_SELECTION_INDEX_VALUE =
        profile.get("SEAM_DATA_DATAMODEL_SELECTION_INDEX_VALUE");

    /**
     * 'andromda_seam_data_databinderclass'
     * This meta-annotation make it possible to implement similar
     * functionality to @DataModel and @DataModelSelection for other
     * data structures apart from lists.
     *
     * The class name of the DataModelBinder class
     */
    public static final String TAGGEDVALUE_SEAM_DATA_DATABINDER_CLASS =
        profile.get("SEAM_DATA_DATABINDER_CLASS");

    /**
     * 'andromda_seam_data_dataselectorclass'
     * This meta-annotation make it possible to implement similar
     * functionality to @DataModel and @DataModelSelection for other
     * datastructures apart from lists.
     *
     * The DataModelSelector class.
     */
    public static final String TAGGEDVALUE_SEAM_DATA_DATASELECTOR_CLASS =
        profile.get("SEAM_DATA_DATASELECTOR_CLASS");
}
