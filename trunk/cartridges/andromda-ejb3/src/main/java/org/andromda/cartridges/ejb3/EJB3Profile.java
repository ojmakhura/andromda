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
     * Specifies the create method stereotype - used in entity POJO
     * and session bean.
     */
    public static final String STEREOTYPE_CREATE_METHOD = "CreateMethod";
    
    /**
     * Specifies the select method stereotype - used in EJB2.x
     */
    public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";
    
    /**
     * Specifies the environment entry stereotype - used in EJB2.x
     */
    public static final String STEREOTYPE_ENV_ENTRY = "EnvEntry";
    
    /**
     * Specifies the version stereotype - optimistic lock value of an attribute of an entity.
     */
    public static final String STEREOTYPE_VERSION = profile.get("VERSION");

    /**
     * Represents a transient entity attribute - non persistent.
     */
    public static final String STEREOTYPE_TRANSIENT = profile.get("TRANSIENT");
    
    /**
     * Represents a reference to a value object.
     */
    public static final String STEREOTYPE_VALUE_REF = "ValueRef";
    
    /**
     * Represents a persistence context instance referenced from a session bean.
     */
    public static final String STEREOTYPE_PERSISTENCE_CONTEXT = profile.get("PERSISTENCE_CONTEXT");

    /* ----------------- Tagged Values -------------------- */
    
    /**
     * 
     */
    public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
    
    /**
     * The tagged value indicating the EJB query.
     */
    public static final String TAGGEDVALUE_EJB_QUERY = "@andromda.ejb.query";
    
    /**
     * The tagged value indicating the view type for the class or operation.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "@andromda.ejb.viewType";
    
    /**
     * The tagged value indicating the transaction property.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = "@andromda.ejb.transaction.type";
    
    /**
     * 
     */
    public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD = "@andromda.ejb.noSyntheticCreateMethod";
    
    /**
     * The tagged value indicating the finder temporal type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_TEMPORAL_TYPE = 
        "@andromda.persistence.finder.temporal.type";
    
    /**
     * The tagged value indicating the finder result type (First or Max).
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_RESULT_TYPE = 
        "@andromda.persistence.finder.result.type";
    
    /**
     * The tagged value indicating the entity table name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ENTITY_TABLE_NAME = "@andromda.persistence.entity.table.name";
    
    /**
     * The tagged value indicating the fetch type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_FETCH_TYPE = "@andromda.persistence.fetch.type";
    
    /**
     * The tagged value indicating the LOB type.
     */
	public static final String TAGGEDVALUE_PERSISTENCE_LOB_TYPE = "@andromda.persistence.lob.type";
    
    /**
     * The tagged value indicating the cascade type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE = "@andromda.persistence.cascade.type";
    
    /**
     * The tagged value indicating the generator type.
     */
	public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE = "@andromda.persistence.generator.type";
    
    /**
     * The tagged value indicating the generator name.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME = "@andromda.persistence.generator.name";
    
    /**
     * The tagged value indicating the generator source name
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME = 
        "@andromda.persistence.generator.source.name";
    
    /**
     * The tagged value indicating the primary key column value for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE = 
        "@andromda.persistence.generator.pkcolumn.value";
    
    /**
     * The tagged value indicating the initial value for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE = 
        "@andromda.persistence.generator.initial.value";
    
    /**
     * The tagged value indicating the step size for the generator.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE = 
        "@andromda.persistence.generator.allocation.size";
    
    /**
     * The tagged value indicating the SQL definition for a column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION = 
        "@andromda.persistence.column.definition";
    
    /**
     * The tagged value for the precision in a float/double column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION = "@andromda.persistence.column.precision";
    
    /**
     * The tagged value for the scale in a float/double column.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE = "@andromda.persistence.column.scale";
    
    /**
     * The tagged value to represent a column that is nullable.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE = "@andromda.persistence.column.nullable";
    
    /**
     * The tagged value that indicates the order by logic on the Many side of the 
     * One-to-Many and Many-to-Many relationships.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ORDERBY = "@andromda.persistence.orderBy";
    
    /**
     * The tagged value indicating the underlying relationship may be NULL.  If set to false,
     * non-null relationship must always exist.
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
     * The tagged value indicating that the row is an entity of the annotated entity type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_VALUE = 
        profile.get("ENTITY_DISCRIMINATOR_VALUE");
    
    /**
     * The tagged value indicating the name of the column used for the discriminator
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN = 
        profile.get("ENTITY_DISCRIMINATOR_COLUMN");
    
    /**
     * The tagged value representing the SQL used in generation of DDL for the discriminator column
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_DEFINITION = 
        profile.get("ENTITY_DISCRIMINATOR_COLUMN_DEFINITION");
    
    /**
     * The tagged value representing the column length for the String discriminator column type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_LENGTH = 
        profile.get("ENTITY_DISCRIMINATOR_COLUMN_LENGTH");
    
    /**
     * The tagged value representing the access type for the entity class
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ACCESS_TYPE = profile.get("ENTITY_ACCESS_TYPE");
    
    /**
     * The tagged value representing whether this entity is an embeddable superclass
     */
    public static final String TAGGEDVALUE_PERSISTENCE_EMBEDDABLE_SUPERCLASS = 
        profile.get("ENTITY_EMBEDDABLE_SUPERCLASS");

    /**
     * The tagged value representing the persistence context unit name (EntityManager)
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME = 
        profile.get("SERVICE_PERSISTENCE_CONTEXT_UNIT_NAME");

    /**
     * The tagged value representing the persistence context transaction/extended type
     */
    public static final String TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE = 
        profile.get("SERVICE_PERSISTENCE_CONTEXT_TYPE");

    /**
     * The tagged value representing the session EJB type (Stateless or Stateful)
     */
    public static final String TAGGEDVALUE_EJB_SESSION_TYPE = profile.get("SERVICE_TYPE");

    /**
     * The tagged value representing the comma separated list of security roles
     * permitted to execute operations in a session bean.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_ROLES_ALLOWED = profile.get("SECURITY_ROLES_ALLOWED");

    /**
     * The tagged value representing whether to permit all roles to execute
     * operations in a session bean.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_PERMIT_ALL = profile.get("SECURITY_PERMIT_ALL");

    /**
     * The tagged value representing the security domain to sepecify at
     * the session bean class level.
     */
    public static final String TAGGEDVALUE_EJB_SECURITY_DOMAIN = profile.get("SECURITY_DOMAIN");
}