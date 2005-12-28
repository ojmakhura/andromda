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
    public static final String STEREOTYPE_CREATE_METHOD = "CreateMethod";
    public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";
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

    /* ----------------- Tagged Values -------------------- */
    public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
    public static final String TAGGEDVALUE_EJB_QUERY = "@andromda.ejb.query";
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "@andromda.ejb.viewType";
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = "@andromda.ejb.transaction.type";
    public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD = "@andromda.ejb.noSyntheticCreateMethod";
    
    public static final String TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_TEMPORAL_TYPE = "@andromda.persistence.finder.temporal.type";
    public static final String TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_RESULT_TYPE = "@andromda.persistence.finder.result.type";
    public static final String TAGGEDVALUE_PERSISTENCE_ENTITY_TABLE_NAME = "@andromda.persistence.entity.table.name";
    public static final String TAGGEDVALUE_PERSISTENCE_FETCH_TYPE = "@andromda.persistence.fetch.type";
	public static final String TAGGEDVALUE_PERSISTENCE_LOB_TYPE = "@andromda.persistence.lob.type";
    public static final String TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE = "@andromda.persistence.cascade.type";
	public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE = "@andromda.persistence.generator.type";
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME = "@andromda.persistence.generator.name";
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME = "@andromda.persistence.generator.source.name";
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE = "@andromda.persistence.generator.pkcolumn.value";
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE = "@andromda.persistence.generator.initial.value";
    public static final String TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE = "@andromda.persistence.generator.allocation.size";
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION = "@andromda.persistence.column.definition";
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION = "@andromda.persistence.column.precision";
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE = "@andromda.persistence.column.scale";
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE = "@andromda.persistence.column.nullable";
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
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_VALUE = profile.get("ENTITY_DISCRIMINATOR_VALUE");
    
    /**
     * The tagged value indicating the name of the column used for the discriminator
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN = profile.get("ENTITY_DISCRIMINATOR_COLUMN");
    
    /**
     * The tagged value representing the SQL used in generation of DDL for the discriminator column
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_DEFINITION = profile.get("ENTITY_DISCRIMINATOR_COLUMN_DEFINITION");
    
    /**
     * The tagged value representing the column length for the String discriminator column type.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_DISCRIMINATOR_COLUMN_LENGTH = profile.get("ENTITY_DISCRIMINATOR_COLUMN_LENGTH");
    
    /**
     * The tagged value representing the access type for the entity class
     */
    public static final String TAGGEDVALUE_PERSISTENCE_ACCESS_TYPE = profile.get("ENTITY_ACCESS_TYPE");
    
    /**
     * The tagged value representing whether this entity is an embeddable superclass
     */
    public static final String TAGGEDVALUE_PERSISTENCE_EMBEDDABLE_SUPERCLASS = profile.get("ENTITY_EMBEDDABLE_SUPERCLASS");
    
    /* ------------------- Fetch Types ------------------------*/
    public static final String FETCHTYPE_EAGER = "EAGER";
    public static final String FETCHTYPE_LAZY = "LAZY";

    /* --------------------- Lob Types ------------------------*/
    public static final String LOBTYPE_CLOB = "CLOB";
    public static final String LOBTYPE_BLOB = "BLOB";

    /* ---------------- Generator Types -----------------------*/
    public static final String GENERATORTYPE_TABLE = "TABLE";
    public static final String GENERATORTYPE_SEQUENCE = "SEQUENCE";
    public static final String GENERATORTYPE_IDENTITY = "IDENTITY";
    public static final String GENERATORTYPE_AUTO = "AUTO";
    public static final String GENERATORTYPE_NONE = "NONE";

    /* ---------------- Temporal Types ------------------------*/
    public static final String TEMPORALTYPE_DATE = "DATE";
    public static final String TEMPORALTYPE_TIME = "TIME";
    public static final String TEMPORALTYPE_TIMESTAMP = "TIMESTAMP";
    public static final String TEMPORALTYPE_NONE = "NONE";

    /* ------------ Finder Method Result Types ----------------*/
    public static final String FINDER_RESULTTYPE_FIRST = "First";
    public static final String FINDER_RESULTTYPE_MAX = "Max";
}