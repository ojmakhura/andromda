package org.andromda.cartridges.spring;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The Spring profile. Contains the profile information (tagged values, and stereotypes) for the Spring cartridge.
 *
 * @author Chad Brandon
 * @author Peter Friese
 */
public class CartridgeSpringProfile
    extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile PROFILE = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    /**
     * Stores the service init-method stereotype.
     */
    public static final String STEREOTYPE_POST_CONSTRUCT_METHOD = PROFILE.get("POST_CONSTRUCT");

    /**
     * Stores the service destroy-method stereotype.
     */
    public static final String STEREOTYPE_PRE_DESTROY_METHOD = PROFILE.get("PRE_DESTROY");

    /**
     * 'PrePersist' Specifies the entity business operation as a pre-persist callback
     */
    public static final String STEREOTYPE_PRE_PERSIST = PROFILE.get("PRE_PERSIST");

    /**
     * 'PostPersist' Specifies the entity operation as a post-persist operation
     */
    public static final String STEREOTYPE_POST_PERSIST = PROFILE.get("POST_PERSIST");

    /**
     * 'PreRemove' Specifies the entity business operati operationre-remove operation
     */
    public static final String STEREOTYPE_PRE_REMOVE = PROFILE.get("PRE_REMOVE");

    /**
     * 'PostRemove' Specifies the entity business operation as a post-remove operation
     */
    public static final String STEREOTYPE_POST_REMOVE = PROFILE.get("POST_REMOVE");

    /**
     * 'PreUpdate' Specifies the entity business operation as a pre-update operation
     */
    public static final String STEREOTYPE_PRE_UPDATE = PROFILE.get("PRE_UPDATE");

    /**
     * 'PostUpdate' Specifies the entity business operation as a post-update operation
     */
    public static final String STEREOTYPE_POST_UPDATE = PROFILE.get("POST_UPDATE");

    /* ----------------- Tagged Values -------------------- */

    /**
     * Stores the EJB service transaction type.
     */
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = PROFILE.get("EJB_TRANSACTION_TYPE");

    /**
     * Stores the EJB service view type (local/remote).
     */
    public static final String TAGGEDVALUE_EJB_VIEW_TYPE = PROFILE.get("EJB_VIEW_TYPE");

    /**
     * Stores the Spring service transaction type.
     */
    public static final String TAGGEDVALUE_TRANSACTION_TYPE = PROFILE.get("TRANSACTION_TYPE");

    /**
     * Stores whether a criteria search attribute may be nullable.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_NULLABLE = PROFILE.get("HIBERNATE_CRITERIA_NULLABLE");

    /**
     * Stores a criteria search attribute path.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_ATTRIBUTE = PROFILE.get("HIBERNATE_CRITERIA_ATTRIBUTE");

    /**
     * Stores a criteria search comparator.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_COMPARATOR = PROFILE.get("HIBERNATE_CRITERIA_COMPARATOR");

    /**
     * Stores a hibernate matchmode.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_MATCHMODE = PROFILE.get("HIBERNATE_CRITERIA_MATCHMODE");

    /**
     * Stores the criteria search order direction.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_ORDER_DIRECTION =
        PROFILE.get("HIBERNATE_CRITERIA_ORDER_DIRECTION");

    /**
     * Stores the criteria search order relevance.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_ORDER_RELEVANCE =
        PROFILE.get("HIBERNATE_CRITERIA_ORDER_RELEVANCE");

    /**
     * Stores whether or not criteria like comparator's should ignore case.
     */
    public static final String TAGGEDVALUE_HIBERNATE_CRITERIA_COMPARATOR_IGNORE_CASE =
        PROFILE.get("HIBERNATE_CRITERIA_COMPARATOR_IGNORE_CASE");

    /**
     * Stores a hibernate query.
     */
    public static final String TAGGEDVALUE_HIBERNATE_QUERY = PROFILE.get("HIBERNATE_QUERY");

    /**
     * Stores the hibernate inheritance use for entities.
     */
    public static final String TAGGEDVALUE_HIBERNATE_INHERITANCE = PROFILE.get("HIBERNATE_INHERITANCE");

    /**
     * Define whether the marked finder will use named parameters or positional parameters.
     */
    public static final String TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS = PROFILE
            .get("HIBERNATE_USE_NAMED_PARAMETERS");

    /**
     * Defines the remoting type for spring services.
     */
    public static final String TAGGEDVALUE_SPRING_SERVICE_REMOTING_TYPE = PROFILE.get("SPRING_SERVICE_REMOTING_TYPE");

    /**
     * Defines the remote port for spring services.
     */
    public static final String TAGGEDVALUE_SPRING_SERVICE_REMOTE_PORT = PROFILE.get("SPRING_SERVICE_REMOTE_PORT");

    /**
     * Define additional spring interceptors
     */
    public static final String TAGGEDVALUE_SPRING_SERVICE_INTERCEPTORS = PROFILE.get("SPRING_SERVICE_INTERCEPTORS");

    /**
     * Define whether we will render only the configuration, but not the service itself.
     */
    public static final String TAGGEDVALUE_SERVICE_CONFIG_ONLY = PROFILE.get("SPRING_SERVICE_CONFIG_ONLY");

    /**
    * Define whether the service is private.
    */
    public static final String TAGGEDVALUE_SERVICE_PRIVATE = PROFILE.get("SPRING_SERVICE_PRIVATE");

    /**
     * Optimize acknowledge flag for ActiveMQ connections.
     */
    public static final String TAGGEDVALUEVALUE_ACTIVEMQ_OPTIMIZE_ACKNOWLEDGE = PROFILE.get("ACTIVEMQ_OPTIMIZE_ACKNOWLEDGE");

    /**
     * Session acknowledge mode for messaging (JMS).
     */
    public static final String TAGGEDVALUEVALUE_MESSAGING_SESSION_ACKNOWLEDGE_MODE = PROFILE.get("MESSAGING_SESSION_ACKNOWLEDGE_MODE");

    /* ----------------- Tagged Value Values ------------- */

    /**
     * The "like" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_LIKE = PROFILE.get("LIKE_COMPARATOR");

    /**
     * The "case insensitive like" comparator.
     */
    public static final String TAGGEDVALUEVALUE_INSENSITIVE_LIKE_COMPARATOR =
        PROFILE.get("INSENSITIVE_LIKE_COMPARATOR");

    /**
     * The "equals" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_EQUAL = PROFILE.get("EQUAL_COMPARATOR");

    /**
     * The "greater of even" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL =
        PROFILE.get("GREATER_THAN_OR_EQUAL_COMPARATOR");

    /**
     * The "greater" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_GREATER = PROFILE.get("GREATER_THAN_COMPARATOR");

    /**
     * The "less of even" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL = PROFILE.get("LESS_THAN_OR_EQUAL_COMPARATOR");

    /**
     * The "less" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_LESS = PROFILE.get("LESS_THAN_COMPARATOR");

    /**
     * The "in" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_IN = PROFILE.get("IN_COMPARATOR");

    /**
     * The "not equal" comparator.
     */
    public static final String TAGGEDVALUEVALUE_COMPARATOR_NOT_EQUAL = PROFILE.get("NOT_EQUAL_COMPARATOR");

    /**
     * PROFILE.get("MATCHMODE_ANYWHERE")
     * org.hibernate.expression.MatchMode#ANYWHERE match mode.
     */
    public static final String TAGGEDVALUEVALUE_MATCHMODE_ANYWHERE = PROFILE.get("MATCHMODE_ANYWHERE");

    /**
     * PROFILE.get("MATCHMODE_END")
     * org.hibernate.expression.MatchMode#END match mode.
     */
    public static final String TAGGEDVALUEVALUE_MATCHMODE_END = PROFILE.get("MATCHMODE_END");

    /**
     * PROFILE.get("MATCHMODE_EXACT")
     * org.hibernate.expression.MatchMode#EXACT match mode.
     */
    public static final String TAGGEDVALUEVALUE_MATCHMODE_EXACT = PROFILE.get("MATCHMODE_EXACT");

    /**
     * PROFILE.get("MATCHMODE_START")
     * org.hibernate.expression.MatchMode#START match mode.
     */
    public static final String TAGGEDVALUEVALUE_MATCHMODE_START = PROFILE.get("MATCHMODE_START");

    /**
     * Ascending sort order.
     */
    public static final String TAGGEDVALUEVALUE_ORDER_ASCENDING = PROFILE.get("ORDER_ASCENDING");

    /**
     * Descending sort order.
     */
    public static final String TAGGEDVALUEVALUE_ORDER_DESCENDING = PROFILE.get("ORDER_DESCENDING");
}