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
     * The suffix given to transformation method names.
     */
    static final String TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX = "Collection";

    /**
     * Defines the prefix given to the transformation constants.
     */
    final static String TRANSFORMATION_CONSTANT_PREFIX = "TRANSFORM_";

    /**
     * The property used to specify the implementation operation name pattern (on both services and DAOs).
     */
    static final String IMPLEMENTATION_OPERATION_NAME_PATTERN = "implementationOperationNamePattern";

    /**
     * The pattern used to construct the DAO base name.
     */
    static final String DAO_BASE_PATTERN = "daoBaseNamePattern";

    /**
     * The pattern used to construct the DAO name.
     */
    static final String DAO_PATTERN = "daoNamePattern";

    /**
     * The pattern used to construct the DAO implementation name.
     */
    static final String DAO_IMPLEMENTATION_PATTERN = "daoImplementationNamePattern";
    
    /**
     * The pattern used to indicate whether or not ejb transactions are enabled.
     */
    static final String EJB_TRANSACTIONS_ENABLED = "ejbTransactionsEnabled";
    
    /**
     * The pattern used to construct the web service outgoing attachment handler call.
     */
    static final String WEBSERVICE_OUTGOING_ATTACHMENT_HANDLER_CALL_PATTERN = "webServiceOutgoingAttachmentHandlerCallPattern";
    
    /**
     * The pattern used to construct the web service incoming attachment handler call.
     */
    static final String WEBSERVICE_INCOMING_ATTACHMENT_HANDLER_CALL_PATTERN = "webServiceIncomingAttachmentHandlerCallPattern";

}