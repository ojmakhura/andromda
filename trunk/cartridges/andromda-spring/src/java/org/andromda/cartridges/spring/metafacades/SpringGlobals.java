package org.andromda.cartridges.spring.metafacades;

/**
 * Stores Globals specific to the Spring cartridge.
 *
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class SpringGlobals
{
    /**
     * Denotes whether or not subclasses require their own mapping file.
     */
    static public final String HIBERNATE_MAPPING_STRATEGY = "hibernateMappingStrategy";

    /**
     * A mapping file per subclass.
     */
    static public final String HIBERNATE_MAPPING_STRATEGY_SUBCLASS = "subclass";

    /**
     * A mapping file per hierarchy.
     */
    static public final String HIBERNATE_MAPPING_STRATEGY_HIERARCHY = "hierachy";

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
     * The name prefix for all spring bean ids.   
     */
    final static String BEAN_NAME_PREFIX = "beanNamePrefix";
    
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
     * The suffix given to collection transformation method names.
     */
    static final String TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX = "Collection";

    /**
     * The suffix given to array method names.
     */
    static final String TRANSFORMATION_TO_ARRAY_METHOD_SUFFIX = "Array";

    /**
     * Defines the prefix given to the transformation constants.
     */
    final static String TRANSFORMATION_CONSTANT_PREFIX = "TRANSFORM_";

    /**
     * The property used to specify the implementation operation name pattern (on both services and DAOs).
     */
    static final String IMPLEMENTATION_OPERATION_NAME_PATTERN = "implementationOperationNamePattern";

    /**
     * The name pattern for service implementation class packages
     */
    static final String IMPLEMENTATION_PACKAGE_NAME_PATTERN = "implementationPackageNamePattern";
    
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

    /**
     * RMI protocol for remote services
     */
    static final String REMOTING_PROTOCOL_RMI = "rmi";

    /**
     * Hessian protocol for remote services
     */
    static final String REMOTING_PROTOCOL_HESSIAN = "hessian";

    /**
     * HttpInvoker protocol for remote services
     */
    static final String REMOTING_PROTOCOL_HTTPINVOKER = "httpinvoker";

    /**
     * Lingo protocol for remote services
     */
    static final String REMOTING_PROTOCOL_LINGO = "lingo";

    /**
     * Disable remoting
     */
    static final String REMOTING_PROTOCOL_NONE = "none";

    /**
     * Burlap protocol for remote services
     */
    static final String REMOTING_PROTOCOL_BURLAP = "burlap";

    /**
     * The suffix to append to the class names of CRUD value objects.
     */
    static final String CRUD_VALUE_OBJECT_SUFFIX = "crudValueObjectSuffix";

    /**
     * Prefix for configuration properties related to remoting.
     */
    public static final String CONFIG_PROPERTY_PREFIX = "configPropertyPrefix";

    /**
     * Default service exceptino name pattern
     */
    static final String DEFAULT_SERVICE_EXCEPTION_NAME_PATTERN = "defaultServiceExceptionNamePattern";

    /**
     * Default service exceptions
     */
    static final String DEFAULT_SERVICE_EXCEPTIONS = "defaultServiceExceptions";

    /**
     * Property for configuring the JMS destination template accessor name.
     */
    static final String JMS_DESTINATION_TEMPLATE_PATTERN = "jmsDestinationTemplatePattern";

    /**
     * Enable rich-client code generation ?
     */
    static final String RICH_CLIENT = "richClient";

    /**
     * The name pattern for EJB packages
     */
    static final String EJB_PACKAGE_NAME_PATTERN = "ejbPackageNamePattern";

    /**
     * The prefix to use when constructing ejb JNDI names
     */
    static final String EJB_JNDI_NAME_PREFIX = "ejbJndiNamePrefix";

    /**
     * Service interceptors
     */
    static final String SERVICE_INTERCEPTORS = "serviceInterceptors";

    /**
     * The remoting type to be used for services
     */
    static final String SERVICE_REMOTING_TYPE = "serviceRemotingType";

    /**
     * The server on which remote services are to be deployed
     */
    static final String SERVICE_REMOTE_SERVER = "serviceRemoteServer";

    /**
     * The remote port via which services are accessible
     */
    static final String SERVICE_REMOTE_PORT = "serviceRemotePort";

    /**
     * The remote context to which services are to be deployed
     */
    static final String SERVICE_REMOTE_CONTEXT = "serviceRemoteContext";

    /**
     * Indicates if "XML Persistence" code generation is enabled. Requires Hibernate 3.
     */
    static public final String HIBERNATE_XML_PERSISTENCE = "hibernateXMLPersistence";

    /**
     * Determines if the identifier attribute on an entity is generated as an attribute
     * or as a separate element.
     */
    static final String HIBERNATE_XML_PERSISTENCE_ID_AS_ATTRIBUTE = "hibernateXMLPersistIDAsAttribute";

    /**
     * Hibernate version to use.
     */
    static public final String HIBERNATE_VERSION = "hibernateVersion";

    /**
     * The version for Hibernate 3.
     */
    public static final String HIBERNATE_VERSION_3 = "3";
    
    /**
     * JMS Message types.
     */
    public static String[] JMS_MESSAGE_TYPES = new String[] { 
        "javax.jms.Message",
        "javax.jms.MapMessage", 
        "javax.jms.TextMessage", 
        "javax.jms.StreamMessage",
        "javax.jms.BytesMessage",
        "javax.jms.ObjectMessage"
        };

}