package org.andromda.cartridges.ejb3;

/**
 * Stores Globals specific to the EJB3 cartridge.
 *
 * @author Chad Brandon
 */
public class EJB3Globals
{
    /**
     * Stores the default EJB transaction type.
     */
    public static final String TRANSACTION_TYPE = "transactionType";

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
     * The pattern to use for determining the package name for EJBs.
     */
    public static final String JNDI_NAME_PREFIX = "jndiNamePrefix";
    
    /**
     * The property that stores the default entity ID generator type
     */
    public static final String ENTITY_DEFAULT_GENERATOR_TYPE = "entityDefaultGeneratorType";

    /**
     * The property that stores the default generator initial value
     */
    public static final String ENTITY_DEFAULT_GENERATOR_INITIAL_VALUE = "entityDefaultGeneratorInitialValue";

    /**
     * The property that stores the default generator allocation size for incrementing ids
     */
    public static final String ENTITY_DEFAULT_GENERATOR_ALLOCATION_SIZE = "entityDefaultGeneratorAllocationSize";
    
    /**
     * The property that stores the default entity association optional attribute for Many-to-One and One-to-One
     */
    public static final String ENTITY_DEFAULT_ASSOCIATION_OPTIONAL = "entityDefaultAssociationOptional";
    
    /**
     * Stores whether or not named parameters should be used in EJB queries.
     */
    public static final String QUERY_USE_NAMED_PARAMETERS = "queryUseNamedParameters";
    
    /**
     * The default composite association cascade property
     */
    public static final String ENTITY_DEFAULT_COMPOSITE_CASCADE = "entityCompositeCascade";
    
    /**
     * The default aggregation association cascade property
     */
    public static final String ENTITY_DEFAULT_AGGREGATION_CASCADE = "entityAggergationCascade";
}
