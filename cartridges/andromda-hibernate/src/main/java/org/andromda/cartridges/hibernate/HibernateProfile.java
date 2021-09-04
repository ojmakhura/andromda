package org.andromda.cartridges.hibernate;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * The Hibernate profile. Contains the profile information (tagged values, and
 * stereotypes) for the Hibernate cartridge.
 *
 * @author Chad Brandon
 * @author Carlos Cuenca
 */
public class HibernateProfile extends UMLProfile {
        /**
         * The Profile instance from which we retrieve the mapped profile names.
         */
        private static final Profile profile = Profile.instance();

        /* ----------------- Stereotypes -------------------- */
        /* ----------------- Tagged Values -------------------- */

        /**
         * andromda_hibernate_xml_tagName Tag attached to entities, attributes, and/or
         * association ends to specify the name of the XML node to generate when v3 XML
         * Persistence is activated.
         */
        public static final String TAGGEDVALUE_HIBERNATE_XML_TAG_NAME = profile.get("HIBERNATE_XML_TAG_NAME");

        /**
         * andromda_hibernate_xml_embed Tag attached to association ends to determine if
         * the associated entity should be embed in as XML when v3 XML Persistence is
         * activated.
         */
        public static final String TAGGEDVALUE_HIBERNATE_XML_EMBED = profile.get("HIBERNATE_XML_EMBED");

        /**
         * andromda_hibernate_generator_class: Stores the hibernate generator class.
         */
        public static final String TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS = profile.get("HIBERNATE_GENERATOR_CLASS");

        /**
         * andromda_hibernate_query: Stores a hibernate query string.
         */
        public static final String TAGGEDVALUE_HIBERNATE_QUERY = profile.get("HIBERNATE_QUERY");

        /**
         * andromda_hibernate_query_useNamedParameters Define whether the marked finder
         * will use named parameters or positional parameters.
         */
        public static final String TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS = profile
                        .get("HIBERNATE_USE_NAMED_PARAMETERS");

        /**
         * andromda_ejb_viewType: Stores the viewtype of the Hibernate Session EJB.
         */
        public static final String TAGGEDVALUE_EJB_VIEWTYPE = profile.get("EJB_VIEWTYPE");

        /**
         * andromda_ejb_transaction_type: Stores the EJB service transaction type.
         */
        public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = profile.get("EJB_TRANSACTION_TYPE");

        /**
         * andromda_hibernate_lazy Stores the aggregation kind (lazy/eager) of the
         * Hibernate Session EJB.
         */
        public static final String TAGGEDVALUE_HIBERNATE_LAZY = profile.get("HIBERNATE_LAZY");

        /**
         * andromda_hibernate_inheritance Support for hibernate inheritance strategy,
         * supported values are
         * <ul>
         * <li>class : one table per base class</li>
         * <li>subclass : one table per subclass</li>
         * <li>concrete : one table per class, subclasses may implement subclass or
         * joined-subclass</li>
         * <li>union-subclass: generate per concrete class with union-subclass
         * mapping</li>
         * <li>interface : generate interface and put attributes etc on subclasses</li>
         * </ul>
         * See Hibernate documentation for specific details.
         */
        public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = profile.get("HIBERNATE_INHERITANCE");

        /**
         * andromda_hibernate_outerjoin Defines outer join fetching on many to one and
         * one to one associations
         */
        public static final String TAGGEDVALUE_HIBERNATE_OUTER_JOIN = profile.get("HIBERNATE_OUTER_JOIN");

        /**
         * andromda_hibernate_query_useCache Defines if a query within a finder method
         * should use the cache
         */
        public static final String TAGGEDVALUE_HIBERNATE_USE_QUERY_CACHE = profile.get("HIBERNATE_USE_QUERY_CACHE");

        /**
         * andromda_hibernate_entity_cache Defines the cache type for the Entity
         */
        public static final String TAGGEDVALUE_HIBERNATE_ENTITY_CACHE = profile.get("HIBERNATE_ENTITY_CACHE");

        /**
         * andromda_hibernate_entity_dynamicInsert Defines if the entity will limit the
         * SQL insert statement to properties with values
         */
        public static final String TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_INSERT = profile
                        .get("HIBERNATE_ENTITY_DYNAMIC_INSERT");

        /**
         * andromda_hibernate_entity_dynamicUpdate Defines if the entity will limit the
         * SQL update statements to properties that are modified
         */
        public static final String TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_UPDATE = profile
                        .get("HIBERNATE_ENTITY_DYNAMIC_UPDATE");

        /**
         * andromda_hibernate_association_cache Defines the cache type for an
         * association
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE = profile.get("HIBERNATE_ASSOCIATION_CACHE");

        /**
         * andromda_hibernate_entity_proxy: Defines if the entity has a proxy
         */
        public static final String TAGGEDVALUE_HIBERNATE_PROXY = profile.get("HIBERNATE_PROXY");

        /**
         * andromda_hibernate_ehcache_maxElementsInMemory Defines the maximum number of
         * objects that will be created in memory
         */
        public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS = profile
                        .get("HIBERNATE_EHCACHE_MAX_ELEMENTS");

        /**
         * andromda_hibernate_ehcache_eternal Defines if elements are eternal.
         */
        public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL = profile.get("HIBERNATE_EHCACHE_ETERNAL");

        /**
         * andromda_hibernate_ehcache_timeToIdleSeconds Defines the time to idle for an
         * element before it expires.
         */
        public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE = profile
                        .get("HIBERNATE_EHCACHE_TIME_TO_IDLE");

        /**
         * andromda_hibernate_ehcache_timeToLiveSeconds Defines the time to live for an
         * element before it expires.
         */
        public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE = profile
                        .get("HIBERNATE_EHCACHE_TIME_TO_LIVE");

        /**
         * andromda_hibernate_ehcache_overflowToDisk Defines if elements can overflow to
         * disk.
         */
        public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK = profile
                        .get("HIBERNATE_EHCACHE_OVERFLOW_TO_DISK");

        /**
         * andromda_hibernate_entity_cache_distributed Defines if the cache for this
         * entity is to be distributed.
         */
        public static final String TAGGEDVALUE_HIBERNATE_ENTITYCACHE_DISTRIBUTED = profile
                        .get("HIBERNATE_ENTITYCACHE_DISTRIBUTED");

        /**
         * andromda_hibernate_association_cache_distributed Defines if the cache for
         * this association is to be distributed.
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED = profile
                        .get("HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED");

        /**
         * andromda_hibernate_collection_type Defines the association collection type
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_COLLECTION_TYPE = profile
                        .get("HIBERNATE_ASSOCIATION_COLLECTION_TYPE");

        /**
         * andromda_hibernate_sort_type: Defines the association sort type.
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_SORT_TYPE = profile
                        .get("HIBERNATE_ASSOCIATION_SORT_TYPE");

        /**
         * andromda_hibernate_orderByColumns Defines the association order by columns
         * names.
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS = profile
                        .get("HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS");

        /**
         * andromda_hibernate_whereClause: Defines the association where clause.
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_WHERE_CLAUSE = profile
                        .get("HIBERNATE_ASSOCIATION_WHERE_CLAUSE");

        /**
         * andromda_hibernate_collection_index Defines the index column for hibernate
         * indexed collections
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX = profile.get("HIBERNATE_ASSOCIATION_INDEX");

        /**
         * andromda_hibernate_collection_index_type Defines the index column type for
         * hibernate indexed collections
         */
        public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX_TYPE = profile
                        .get("HIBERNATE_ASSOCIATION_INDEX_TYPE");

        /**
         * andromda_hibernate_version Defines the tagged value for hibernate version
         * property on entities
         */
        public static final String TAGGEDVALUE_HIBERNATE_VERSION_PROPERTY = profile.get("HIBERNATE_VERSION_PROPERTY");

        /**
         * andromda_hibernate_cascade Defines the tagged value for hibernate cascade on
         * an association end
         */
        public static final String TAGGEDVALUE_HIBERNATE_CASCADE = profile.get("HIBERNATE_CASCADE");

        /**
         * andromda_hibernate_formula: Stores sql formula for an attribute.
         */
        public static final String TAGGEDVALUE_HIBERNATE_FORMULA = profile.get("HIBERNATE_FORMULA");

        /**
         * andromda_persistence_discriminator_column_name Column name of the
         * discriminator-column
         */
        public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_COLUMN = profile.get("ENTITY_DISCRIMINATOR_COLUMN");

        /**
         * andromda_persistence_discriminator_type Column type of the
         * discriminator-column
         */
        public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_TYPE = profile.get("ENTITY_DISCRIMINATOR_TYPE");

        /**
         * andromda_persistence_discriminator_value Value of the class for the
         * discriminator-column
         */
        public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_VALUE = profile.get("ENTITY_DISCRIMINATOR_VALUE");

        /**
         * andromda_hibernate_property_insert Specifies whether a mapped column should
         * be included in SQL INSERT statements.
         */
        public static final String TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT = profile.get("HIBERNATE_PROPERTY_INSERT");

        /**
         * andromda_hibernate_property_update Specifies whether a mapped column should
         * be included in SQL UPDATE statements.
         */
        public static final String TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE = profile.get("HIBERNATE_PROPERTY_UPDATE");

        /**
         * Specifies the select method stereotype - used in EJB2.x This may be
         * deprecated in future releases.
         */
        public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";

        /**
         * 'PrePersist' Specifies the entity bean operation as a pre-persist callback
         */
        public static final String STEREOTYPE_PRE_PERSIST = profile.get("PRE_PERSIST");

        public static final String STEREOTYPE_POST_PERSIST = profile.get("POST_PERSIST");;

        /**
         * 'PreRemove' Specifies the entity bean operation as a pre-remove callback
         */
        public static final String STEREOTYPE_PRE_REMOVE = profile.get("PRE_REMOVE");

        /**
         * 'PostRemove' Specifies the entity bean operation as a post-remove callback
         */
        public static final String STEREOTYPE_POST_REMOVE = profile.get("POST_REMOVE");

        /**
         * 'PreUpdate' Specifies the entity bean operation as a pre-update callback
         */
        public static final String STEREOTYPE_PRE_UPDATE = profile.get("PRE_UPDATE");

        /**
         * 'PostUpdate' Specifies the entity bean operation as a post-update callback
         */
        public static final String STEREOTYPE_POST_UPDATE = profile.get("POST_UPDATE");

        /**
         * 'PostLoad' Specifies the entity bean operation as a post-load callback
         */
        public static final String STEREOTYPE_POST_LOAD = profile.get("POST_LOAD");

        /**
         * 'CreateMethod' Specifies the create method stereotype - used in entity POJO
         * and session bean.
         */
        public static final String STEREOTYPE_CREATE_METHOD = profile.get("CREATE_METHOD");

        /**
         * 'andromda_persistence_lob_type' The tagged value overriding the default LOB
         * type for attribute.
         */
        public static final String TAGGEDVALUE_EJB_PERSISTENCE_LOB_TYPE = profile.get("LOB_TYPE");

        /**
         * 'andromda_hibernate_type' The tagged value indicating the overridden type
         * specified on attributes or finder method arguments , to
         * generate @org.hibernate.annotations.Type annotations.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_OVERRIDE_TYPE = profile.get("OVERRIDE_TYPE");

        /**
         * 'andromda_persistence_table' The tagged value indicating the entity table
         * name.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_ENTITY_TABLE_NAME = profile.get("TABLE");

        /**
         * 'andromda_persistence_fetch_type' The tagged value indicating the fetch type.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_FETCH_TYPE = profile.get("FETCH_TYPE");

        /**
         * 'andromda_persistence_cascade_type' The tagged value indicating the cascade
         * type.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE = profile.get("CASCADE_TYPE");

        /**
         * 'andromda_persistence_generator_type' The tagged value indicating the
         * generator type.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE = profile.get("GENERATOR_TYPE");

        /**
         * 'andromda_persistence_generator_name' The tagged value indicating the
         * generator name.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME = profile.get("GENERATOR_NAME");

        /**
         * 'andromda_persistence_generator_genericStrategy' The tagged value indicating
         * the generator generic strategy.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_GENERIC_STRATEGY = profile
                        .get("GENERATOR_GENERIC_STRATEGY");

        /**
         * 'andromda_persistence_generator_source_name' The tagged value indicating the
         * generator source name.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME = profile.get("GENERATOR_SOURCE_NAME");

        /**
         * 'andromda_persistence_generator_pkcolumn_value' The tagged value indicating
         * the primary key column value for the generator.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE = profile
                        .get("GENERATOR_PK_COLUMN_VALUE");

        /**
         * 'andromda_persistence_generator_initial_value' The tagged value indicating
         * the initial value for the generator.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE = profile
                        .get("GENERATOR_INITIAL_VALUE");

        /**
         * 'andromda_persistence_generator_allocation_size' The tagged value indicating
         * the step size for the generator.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE = profile
                        .get("GENERATOR_ALLOCATION_SIZE");

        /**
         * 'andromda_persistence_column_definition' The tagged value indicating the SQL
         * definition for a column.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION = profile.get("COLUMN_DEFINITION");

        /**
         * 'andromda_persistence_column_precision' The tagged value for the precision in
         * a float/double column.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION = profile.get("COLUMN_PRECISION");

        /**
         * 'andromda_persistence_column_scale' The tagged value for the scale in a
         * float/double column.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE = profile.get("COLUMN_SCALE");

        /**
         * 'andromda_persistence_column_nullable' The tagged value to represent a column
         * that is nullable.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE = profile.get("COLUMN_NULLABLE");

        /**
         * 'andromda_persistence_column_insert' The tagged value that specifies whether
         * a mapped column should be included in SQL INSERT statements.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_INSERT = profile.get("COLUMN_INSERT");

        /**
         * 'andromda_persistence_column_update' The tagged value that specifies whether
         * a mapped column should be included in SQL UPDATE statements.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_UPDATE = profile.get("COLUMN_UPDATE");

        /**
         * 'andromda_persistence_orderBy' The tagged value that indicates the order by
         * logic on the Many side of the One-to-Many and Many-to-Many relationships.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_ORDERBY = profile.get("ORDER_BY");

        /**
         * 'Version' Specifies the version stereotype - optimistic lock value of an
         * attribute of an entity.
         */
        public static final String STEREOTYPE_VERSION = profile.get("VERSION");

        /**
         * 'ValueRef' Represents a reference to a value object - used in EJB2.x This may
         * be deprecated in future releases.
         */
        public static final String STEREOTYPE_VALUE_REF = profile.get("VALUE_REF");

        /**
         * 'PersistenceContext' Represents a persistence context instance referenced
         * from a session bean.
         */
        public static final String STEREOTYPE_PERSISTENCE_CONTEXT = profile.get("PERSISTENCE_CONTEXT");

        /**
         * 'ResourceRef' Represents a reference to a resource i.e. UserTransaction or
         * DataSource.
         */
        public static final String STEREOTYPE_RESOURCE_REF = profile.get("RESOURCE_REF");

        /**
         * 'UserTransaction' Represents a class used to inject a
         * javax.transaction.UserTransaction as a resource.
         */
        public static final String STEREOTYPE_USER_TRANSACTION = profile.get("USER_TRANSACTION");

        /**
         * 'DataSource' Represents a class used to inject a javax.sql.DataSource as a
         * resource.
         */
        public static final String STEREOTYPE_DATA_SOURCE = profile.get("DATA_SOURCE");

        /**
         * 'Interceptor' Represents an interceptor class for a session or message-driven
         * bean.
         */
        public static final String STEREOTYPE_INTERCEPTOR = profile.get("INTERCEPTOR");

        /**
         * 'RunAs' Represents a dependency from an actor that is identified to apply a
         * run-as identity to the bean when making calls.
         */
        public static final String STEREOTYPE_SECURITY_RUNAS = profile.get("SECURITY_RUN_AS");

        /**
         * 'Listener' Represents a callback listener class for entity, session and
         * message driven bean classes.
         */
        public static final String STEREOTYPE_LISTENER = profile.get("LISTENER");

        /**
         * 'andromda_ejb_transaction_management' The tagged value indicating the
         * transaction demarcation strategy. This only applies at the class level of a
         * session and message-driven bean.
         */
        public static final String TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT = profile.get("TRANSACTION_MANAGEMENT");

        /**
         * 'andromda_ejb_noSyntheticCreateMethod' The tagged value indicating whether to
         * not allow synthetic (auto generated) create/constructors.
         */
        public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD = profile
                        .get("NO_SYNTHETIC_CREATE_METHOD");

        /**
         * 'andromda_persistence_temporal_type' The tagged value indicating the temporal
         * type specified on attributes or finder method arguments of temporal nature.
         */
        public static final String TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE = profile.get("TEMPORAL_TYPE");

        /**
         * 'andromda_persistence_enumeration_type' The tagged value indicating the
         * enumeration type (ORDINAL, STRING).
         */
        public static final String TAGGEDVALUE_PERSISTENCE_ENUMERATION_TYPE = profile.get("ENUMERATION_TYPE");
}