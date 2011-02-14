package org.andromda.cartridges.webservice;

/**
 * Stores global variables for the WebService cartridge metafacades.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class WebServiceGlobals
{
    /**
     * The prefix for the XSD namespace.
     */
    public final static String XSD_NAMESPACE_PREFIX = "xs:";

    /**
     * Defines the property that stores the location of the schema type mappings URI.
     */
    public static final String SCHEMA_TYPE_MAPPINGS_URI = "schemaTypeMappingsUri";

    /**
     * The prefix given to wrapped style WSDL array types.
     */
    public static final String ARRAY_NAME_PREFIX = "arrayNamePrefix";

    /**
     * The namespace delimiter (separates namespaces).
     */
    public static final char NAMESPACE_DELIMITER = '.';

    /**
     * Defines the style of the web service to be generated (i.e. wrapped, document, rpc). Default=wrapped.
     */
    public static final String WEB_SERVICE_STYLE = "andromda_webservice_style";

    /**
     * Defines the parameter style of the web service to be generated (i.e. bare, wrapped). Default=wrapped.
     */
    public static final String WEB_SERVICE_PARAMETER_STYLE = "andromda_webservice_parameter_style";

    /**
     * The use of the service to be generated (i.e. literal, encoded). Default=literal.
     */
    public static final String WEB_SERVICE_USE = "andromda_webservice_use";

    /**
     * The provider to use for the service, by default is <code>RPC</code> which will use a plain java object as the provider. Another value
     * that can be specified is <code>EJB</code>, if this is specified, then its expected that either the EJB or Hibernate cartridge is
     * being used with this cartridge. Use <code>JAX-WS</code> for jaxws or cxf.
     */
    public static final String WEB_SERVICE_PROVIDER = "andromda_webservice_provider";

    /**
     * Service can be exposed with a different service operation name, allowing overloaded service methods.
     * Defaults to operation.name if blank. All service names must be unique.
     */
    public static final String WEB_SERVICE_NAME = "andromda_webservice_operationName";

    /**
     * The WSDL SOAP Address to use for the service, by default is <code>$webserviceHost:$webservicePort/$webContext/services</code>
     */
    public static final String WEB_WSDL_SOAP_ADDRESS = "andromda_webservice_wsdlSoapAddress";

    /**
     * Optionally allows you to define the name of the role (if it needs to be different than the name of the actor that defines the role).
     */
    public static final String ROLE_NAME = "andromda_role_name";

    /**
     * XML attributeFormDefault value for package-info.java on XmlSchema stereotype. XmlNsForm=QUALIFIED, UNQUALIFIED, default=UNQUALIFIED
     */
    public static final String ATTRIBUTE_FORM_DEFAULT = "andromda_xml_attributeFormDefault";

    /**
     * XML elementFormDefault value for package-info.java on XmlSchema stereotype. XmlNsForm=QUALIFIED, UNQUALIFIED, default=QUALIFIED
     */
    public static final String ELEMENT_FORM_DEFAULT = "andromda_xml_elementFormDefault";

    /**
     * Validate the incoming web service XML against the service wsdl/xsd schema.
     */
    public static final String XML_SCHEMA_VALIDATION = "andromda_schema_validation";

    /**
     * XML package namespace, overrides default reversed package name.
     */
    public static final String XML_NAMESPACE = "andromda_xml_namespace";

    /**
     * XML package namespace abbreviation, overrides default sequentially numbered nsX. Each must be unique.
     */
    public static final String XML_XMLNS = "andromda_xml_xmlns";

    /**
     * XML attribute/element name, overrides default attribute/parameter name.
     */
    public static final String XML_NAME = "andromda_xml_name";

    /**
     * Supplies type value for @XmlAdapter Jaxb annotation for attribute or element. Overrides global default for date, time, dateTime,
     * integer XML types
     */
    public static final String XML_ADAPTER = "andromda_xml_adapter";

    /**
     * Prevents the mapping of a Java type to XML.
     */
    public static final String XML_TRANSIENT = "andromda_xml_transient";

    /**
     * Overrides the default schema type for this property or parameter. i.e. mapping between from UML type to XML
     */
    public static final String XML_TYPE = "andromda_xml_type";

    /**
     * Overrides the default Jaxb simpleBindingMode for this service in wsdl2java. default=true.
     */
    public static final String JAXB_SIMPLE_BINDING_MODE = "andromda_jaxb_simpleBindingMode";

    /**
     * Overrides the default Jaxb XJC arguments for this service in wsdl2java.
     */
    public static final String JAXB_XJC_ARGUMENTS = "andromda_jaxb_xjcArguments";

    /**
     * REST: Is this a REST implementation?
     */
    public static final String REST = "andromda_REST";

    /**
     * REST: andromda_cache_type
     */
    public static final String CACHE_TYPE = "andromda_cache_type";

    /**
     * REST: andromda_REST_consumes
     */
    public static final String REST_CONSUMES = "andromda_REST_consumes";

    /**
     * REST: andromda_REST_context
     */
    public static final String REST_CONTEXT = "andromda_REST_context";

    /**
     * REST: andromda_REST_http_method
     */
    public static final String REST_HTTP_METHOD = "andromda_REST_http_method";

    /**
     * REST: andromda_REST_path
     */
    public static final String REST_PATH = "andromda_REST_path";

    /**
     * REST: andromda_REST_produces
     */
    public static final String REST_PRODUCES = "andromda_REST_produces";

    /**
     * REST: andromda_REST_provider
     */
    public static final String REST_PROVIDER = "andromda_REST_provider";

    /**
     * REST: andromda_REST_request_type
     */
    public static final String REST_REQUEST_TYPE = "andromda_REST_request_type";

    /**
     * REST: andromda_REST_retention
     */
    public static final String REST_RETENTION = "andromda_REST_retention";

    /**
     * REST: andromda_REST_target
     */
    public static final String REST_TARGET = "andromda_REST_target";

    /**
     * REST: andromda_REST_encoded
     */
    public static final String REST_ENCODED = "andromda_REST_encoded";

    /**
     * REST: andromda_REST_part_type
     */
    public static final String REST_PART_TYPE = "andromda_REST_part_type";

    /**
     * REST: andromda_REST_roles_allowed
     */
    public static final String REST_ROLES_ALLOWED = "andromda_REST_roles_allowed";

    /**
     * REST: andromda_REST_suspend
     */
    public static final String REST_SUSPEND = "andromda_REST_suspend";

    /**
     * REST: andromda_REST_parameter_URL
     */
    public static final String REST_PARAMETER_URL = "andromda_REST_parameter_URL";

    /**
     * REST: andromda_REST_param_type
     */
    public static final String REST_PARAM_TYPE = "andromda_REST_param_type";

    /**
     * REST: andromda_REST_path_param
     */
    public static final String REST_PATH_PARAM = "andromda_REST_path_param";

    /**
     * REST: andromda_REST_path_segment
     */
    public static final String REST_PATH_SEGMENT = "andromda_REST_path_segment";

    /**
     * Overrides the default attribute/element output for this element.
     */
    public static final String STEREOTYPE_XML_ATTRIBUTE = "XmlAttribute";

    /**
     * Overrides the default attribute/element output for this element.
     */
    public static final String STEREOTYPE_XML_ELEMENT = "XmlElement";

    /**
     * Set WebService Security in wsdl definition and service implementation.
     */
    public static final String STEREOTYPE_WEBSERVICE_SECURITY = "WSSecurity";

    /**
     * Security namespace abbreviation: andromda_security_abbr
     */
    public static final String SECURITY_ABBR = "andromda_security_abbr";

    /**
     * Security namespace URL: andromda_security_namespace
     */
    public static final String SECURITY_NAMESPACE = "andromda_security_namespace";

    /**
     * Security xsd location (relative reference): andromda_security_XSDlocation
     */
    public static final String SECURITY_XSD_LOCATION = "andromda_security_XSDlocation";

    /**
     * Security element partName: andromda_wsdl_security_partName
     */
    public static final String SECURITY_PARTNAME = "andromda_wsdl_security_partName";

    /**
     * Security element name reference: andromda_wsdl_security_element
     */
    public static final String SECURITY_ELEMENT = "andromda_wsdl_security_element";

    /**
     * Security use literal/encoded: andromda_wsdl_security_use
     */
    public static final String SECURITY_USE = "andromda_wsdl_security_use";

    /**
     * Set customized WebService Header in wsdl definition and service implementation.
     */
    public static final String STEREOTYPE_WEBSERVICE_HEADER = "WSCustomHeader";

    /**
     * Custom webservice header namespace abbreviation: andromda_header_abbr
     */
    public static final String HEADER_ABBR = "andromda_header_abbr";

    /**
     * Custom webservice header namespace URL: andromda_header_namespace
     */
    public static final String HEADER_NAMESPACE = "andromda_header_namespace";

    /**
     * Custom webservice header xsd location (relative reference): andromda_header_XSDlocation
     */
    public static final String HEADER_XSD_LOCATION = "andromda_header_XSDlocation";

    /**
     * Custom webservice header element partName: andromda_wsdl_header_partName
     */
    public static final String HEADER_PARTNAME = "andromda_wsdl_header_partName";

    /**
     * Custom webservice header element name reference: andromda_wsdl_header_element
     */
    public static final String HEADER_ELEMENT = "andromda_wsdl_header_element";

    /**
     * Custom webservice header use literal/encoded: andromda_wsdl_header_use
     */
    public static final String HEADER_USE = "andromda_wsdl_header_use";
}
