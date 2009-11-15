package org.andromda.cartridges.hibernate;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The Hibernate profile. Contains the profile information (tagged values, and stereotypes) for the Hibernate
 * cartridge.
 *
 * @author Chad Brandon
 * @author Carlos Cuenca
 */
public class HibernateProfile
    extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    /* ----------------- Tagged Values -------------------- */

    /**
     * andromda_hibernate_xml_tagName
     * Tag attached to entities, attributes, and/or association ends to specify the
     * name of the XML node to generate when v3 XML Persistence is activated.
     */
    public static final String TAGGEDVALUE_HIBERNATE_XML_TAG_NAME = profile.get("HIBERNATE_XML_TAG_NAME");

    /**
     * andromda_hibernate_xml_embed
     * Tag attached to association ends to determine if the associated entity should be embed
     * in as XML when v3 XML Persistence is activated.
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
     * andromda_hibernate_query_useNamedParameters
     * Define whether the marked finder will use named parameters or positional parameters.
     */
    public static final String TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS =
        profile.get("HIBERNATE_USE_NAMED_PARAMETERS");

    /**
     * andromda_ejb_viewType: Stores the viewtype of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = profile.get("EJB_VIEWTYPE");

    /**
     * andromda_ejb_transaction_type: Stores the EJB service transaction type.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = profile.get("EJB_TRANSACTION_TYPE");

    /**
     * andromda_hibernate_lazy 
     * Stores the aggregation kind (lazy/eager) of the Hibernate Session EJB.
     */
    public static final String TAGGEDVALUE_HIBERNATE_LAZY = profile.get("HIBERNATE_LAZY");

    /**
     * andromda_hibernate_inheritance
     * Support for hibernate inheritance strategy, supported values are <ul> <li>class : one table per base class</li>
     * <li>subclass : one table per subclass</li> <li>concrete : one table per class, subclasses may implement subclass
     * or joined-subclass</li> <li>union-subclass: generate per concrete class with union-subclass mapping</li> <li>
     * interface : generate interface and put attributes etc on subclasses</li> </ul> See  Hibernate documentation for
     * specific details.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = profile.get("HIBERNATE_INHERITANCE");

    /**
     * andromda_hibernate_outerjoin
     * Defines outer join fetching on many to one and one to one associations
     */
    public static final String TAGGEDVALUE_HIBERNATE_OUTER_JOIN = profile.get("HIBERNATE_OUTER_JOIN");

    /**
     * andromda_hibernate_query_useCache
     * Defines if a query within a finder method should use the cache
     */
    public static final String TAGGEDVALUE_HIBERNATE_USE_QUERY_CACHE = profile.get("HIBERNATE_USE_QUERY_CACHE");

    /**
     * andromda_hibernate_entity_cache
     * Defines the cache type for the Entity
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_CACHE = profile.get("HIBERNATE_ENTITY_CACHE");

    /**
     * andromda_hibernate_entity_dynamicInsert
     * Defines if the entity will limit the SQL insert statement to properties with values
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_INSERT =
        profile.get("HIBERNATE_ENTITY_DYNAMIC_INSERT");

    /**
     * andromda_hibernate_entity_dynamicUpdate
     * Defines if the entity will limit the SQL update statements to properties that are modified
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITY_DYNAMIC_UPDATE =
        profile.get("HIBERNATE_ENTITY_DYNAMIC_UPDATE");

    /**
     * andromda_hibernate_association_cache
     * Defines the cache type for an association
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE = profile.get("HIBERNATE_ASSOCIATION_CACHE");

    /**
     * andromda_hibernate_entity_proxy: Defines if the entity has a proxy
     */
    public static final String TAGGEDVALUE_HIBERNATE_PROXY = profile.get("HIBERNATE_PROXY");

    /**
     * andromda_hibernate_ehcache_maxElementsInMemory
     * Defines the maximum number of objects that will be created in memory
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS =
        profile.get("HIBERNATE_EHCACHE_MAX_ELEMENTS");

    /**
     * andromda_hibernate_ehcache_eternal
     * Defines if elements are eternal.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL = profile.get("HIBERNATE_EHCACHE_ETERNAL");

    /**
     * andromda_hibernate_ehcache_timeToIdleSeconds
     * Defines the time to idle for an element before it expires.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE =
        profile.get("HIBERNATE_EHCACHE_TIME_TO_IDLE");

    /**
     * andromda_hibernate_ehcache_timeToLiveSeconds
     * Defines the time to live for an element before it expires.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE =
        profile.get("HIBERNATE_EHCACHE_TIME_TO_LIVE");

    /**
     * andromda_hibernate_ehcache_overflowToDisk
     * Defines if elements can overflow to disk.
     */
    public static final String TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK =
        profile.get("HIBERNATE_EHCACHE_OVERFLOW_TO_DISK");

    /**
     * andromda_hibernate_entity_cache_distributed
     * Defines if the cache for this entity is to be distributed.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ENTITYCACHE_DISTRIBUTED =
        profile.get("HIBERNATE_ENTITYCACHE_DISTRIBUTED");

    /**
     * andromda_hibernate_association_cache_distributed
     * Defines if the cache for this association is to be distributed.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED =
        profile.get("HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED");

    /**
     * andromda_hibernate_collection_type
     * Defines the association collection type
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_COLLECTION_TYPE =
        profile.get("HIBERNATE_ASSOCIATION_COLLECTION_TYPE");

    /**
     * andromda_hibernate_sort_type: Defines the association sort type.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_SORT_TYPE =
        profile.get("HIBERNATE_ASSOCIATION_SORT_TYPE");

    /**
     * andromda_hibernate_orderByColumns
     * Defines the association order by columns names.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS =
        profile.get("HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS");

    /**
     * andromda_hibernate_whereClause: Defines the association where clause.
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_WHERE_CLAUSE =
        profile.get("HIBERNATE_ASSOCIATION_WHERE_CLAUSE");

    /**
     * andromda_hibernate_collection_index
     * Defines the index column for hibernate indexed collections
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX = profile.get("HIBERNATE_ASSOCIATION_INDEX");

    /**
     * andromda_hibernate_collection_index_type
     * Defines the index column type for hibernate indexed collections
     */
    public static final String TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX_TYPE =
        profile.get("HIBERNATE_ASSOCIATION_INDEX_TYPE");

    /**
     * andromda_hibernate_version
     * Defines the tagged value for hibernate version property on entities
     */
    public static final String TAGGEDVALUE_HIBERNATE_VERSION_PROPERTY = profile.get("HIBERNATE_VERSION_PROPERTY");

    /**
     * andromda_hibernate_cascade
     * Defines the tagged value for hibernate cascade on an association end
     */
    public static final String TAGGEDVALUE_HIBERNATE_CASCADE = profile.get("HIBERNATE_CASCADE");

    /**
     * andromda_hibernate_formula: Stores sql formula for an attribute.
     */
    public static final String TAGGEDVALUE_HIBERNATE_FORMULA = profile.get("HIBERNATE_FORMULA");

    /**
    * andromda_persistence_discriminator_column_name
    * Column name of the discriminator-column
    */
    public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_COLUMN =
        profile.get("ENTITY_DISCRIMINATOR_COLUMN");

    /**
    * andromda_persistence_discriminator_type
    * Column type of the discriminator-column
    */
    public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_TYPE = profile.get("ENTITY_DISCRIMINATOR_TYPE");

    /**
    * andromda_persistence_discriminator_value
    * Value of the class for the discriminator-column
    */
    public static final String TAGGEDVALUE_ENTITY_DISCRIMINATOR_VALUE = profile.get("ENTITY_DISCRIMINATOR_VALUE");

    /**
     * andromda_hibernate_property_insert
     * Specifies whether a mapped column should be included in SQL INSERT statements.
     */
    public static final String TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT = profile.get("HIBERNATE_PROPERTY_INSERT");

    /**
     * andromda_hibernate_property_update
     * Specifies whether a mapped column should be included in SQL UPDATE statements.
     */
    public static final String TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE = profile.get("HIBERNATE_PROPERTY_UPDATE");

}