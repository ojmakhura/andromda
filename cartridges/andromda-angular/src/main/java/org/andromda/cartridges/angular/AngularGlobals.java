package org.andromda.cartridges.angular;

/**
 * Stores globals for the JSF cartridge metafacades.
 *
 * @author Chad Brandon
 */
public class AngularGlobals {
    /**
     * Should generate Impl classes, always ?.
     */
    public static final String GENERATE_CRUD_IMPLS = "generateCrudImpls";

    /**
     * The suffix to append to the class names of CRUD value objects.
     */
    public static final String CRUD_VALUE_OBJECT_SUFFIX = "crudValueObjectSuffix";

    /**
     * A space-separated list of types to which displaytag table are to be exported by default.
     */
    public static final String PROPERTY_DEFAULT_TABLE_EXPORT_TYPES = "defaultTableExportTypes";

    /**
     * The default number of columns to render for input fields.
     */
    public static final String PROPERTY_DEFAULT_INPUT_COLUMN_COUNT = "defaultInputColumnCount";

    /**
     * The default number of rows to render for textarea fields.
     */
    public static final String PROPERTY_DEFAULT_INPUT_ROW_COUNT = "defaultInputRowCount";

    /**
     * Denotes the way application resource messages ought to be generated.
     * When messages are normalized it means that elements with the same name
     * will reuse the same label, even if they are located in entirely different
     * use-cases or pages.
     * <p/>
     * This results in resource bundles that are not only smaller in size but
     * also more straightforward to translate. The downside is that it will be
     * less evident to customize labels for certain fields (which is rarely the
     * case anyway).
     * <p/>
     */
    public static final String NORMALIZE_MESSAGES = "normalizeMessages";

    /**
     * The pattern for constructing the form name.
     */
    public static final String FORM_PATTERN = "formPattern";

    /**
     * Contains the default value for whether or not all forwards should perform a HTTP redirect or not.
     */
    public static final String DEFAULT_ACTION_REDIRECT = "defaultActionRedirect";

    /**
     * The pattern for constructing the form implementation name.
     */
    public static final String FORM_IMPLEMENTATION_PATTERN = "formImplementationPattern";

    /**
     * The pattern for constructing the bean name under which the form is stored.
     */
    public static final String FORM_BEAN_PATTERN = "formBeanPattern";

    /**
     * Stores the default form scope which can be overridden with a tagged value.
     */
    public static final String FORM_SCOPE = "formScope";

    /**
     * Stores the pattern used for constructing the controller implementation name.
     */
    public static final String CONTROLLER_IMPLEMENTATION_PATTERN = "controllerImplementationPattern";

    /**
     * The suffix given to title message keys.
     */
    public static final String TITLE_MESSAGE_KEY_SUFFIX = "title";

    /**
     * The suffix given to the documentation message keys.
     */
    public static final String DOCUMENTATION_MESSAGE_KEY_SUFFIX = "documentation";

    /**
     * The namespace property used to identify the pattern used to construct the backend service's accessor.
     */
    public static final String SERVICE_ACCESSOR_PATTERN = "serviceAccessorPattern";

    /**
     * The namespace property used to identify the pattern used to construct the backend service's package name.
     */
    public static final String SERVICE_PACKAGE_NAME_PATTERN = "servicePackageNamePattern";

    /**
     * Represents a hyperlink action type.
     */
    public static final String ACTION_TYPE_HYPERLINK = "hyperlink";

    /**
     * Represents a popup action type.
     */
    public static final String VIEW_TYPE_POPUP = "popup";

    /**
     * Represents a form action type.
     */
    public static final String ACTION_TYPE_FORM = "form";

    /**
     * Represents a table action type.
     */
    public static final String ACTION_TYPE_TABLE = "table";

    /**
     * Represents an image action type.
     */
    public static final String ACTION_TYPE_IMAGE = "image";


    public static final String ACTION_TYPE_POPUP = "popup";
    /**
     * Stores the default date format when dates are formatted.
     */
    public static final String PROPERTY_DEFAULT_DATEFORMAT = "defaultDateFormat";

    /**
     * Stores the default time format when times are formatted.
     */
    public static final String PROPERTY_DEFAULT_TIMEFORMAT = "defaultTimeFormat";

    /**
     * The default key under which the action form is stored.
     */
    public static final String ACTION_FORM_KEY = "actionFormKey";

    /**
     * The pattern used for constructing the name of the filter that performs view form population.
     */
    public static final String VIEW_POPULATOR_PATTERN = "viewPopulatorPattern";

    /**
     * The pattern used for constructing a parameter's backing list name.  A backing list
     * is used when you want to select the value of the parameter from a list (typically
     * used for drop-down select input types).
     */
    public static final String BACKING_LIST_PATTERN = "backingListPattern";

    /**
     * The pattern used for constructing a parameter's backing value name.  A backing value
     * is used when you want to select and submit values from a regular table (works well when
     * you have a list of complex items with values you need to submit).
     */
    public static final String BACKING_VALUE_PATTERN = "backingValuePattern";

    /**
     * The pattern used for constructing the label list name (stores the list
     * of possible parameter value labels).
     */
    public static final String LABEL_LIST_PATTERN = "labelListPattern";

    /**
     * The pattern used for constructing the values list name (stores the list of
     * possible parameter values when selecting from a list).
     */
    public static final String VALUE_LIST_PATTERN = "valueListPattern";

    /**
     * The item count for dummy arrays.
     */
    public static final int DUMMY_ARRAY_COUNT = 5;

    /**
     * The pattern used for constructing the name of JSF converter classes (i.e.
     * the enumeration converter).
     */
    public static final String CONVERTER_PATTERN = "converterPattern";

    /**
     * The "textarea" form input type.
     */
    public static final String INPUT_TEXTAREA = "textarea";

    /**
     * The "select" form input type.
     */
    public static final String INPUT_SELECT = "select";

    /**
     * The "password" form input type.
     */
    public static final String INPUT_PASSWORD = "password";

    /**
     * The "hidden" form input type.
     */
    public static final String INPUT_HIDDEN = "hidden";

    /**
     * The "radio" form input type.
     */
    public static final String INPUT_RADIO = "radio";

    /**
     * The "text" form input type.
     */
    public static final String INPUT_TEXT = "text";

    /**
     * The "multibox" form input type.
     */
    public static final String INPUT_MULTIBOX = "multibox";

    /**
     * The "table" form input type.
     */
    public static final String INPUT_TABLE = "table";

    /**
     * The "checkbox" form input type.
     */
    public static final String INPUT_CHECKBOX = "checkbox";

    /**
     * The "plain text" type.
     */
    public static final String PLAIN_TEXT = "plaintext";

    /**
     * The suffix to append to the forward name.
     */
    public static final String USECASE_FORWARD_NAME_SUFFIX = "-usecase";

    /**
     * The namespace delimiter (separates namespaces).
     */
    public static final char NAMESPACE_DELIMITER = '.';

    /**
     * Defines the property that stores the location of the schema type mappings URI.
     */
    public static final String SCHEMA_TYPE_MAPPINGS_URI = "schemaTypeMappingsUri";

    /**
     * Validate the incoming web service XML against the service wsdl/xsd schema.
     */
    public static final String XML_SCHEMA_VALIDATION = "andromda_schema_validation";

    /**
     * Overrides the default Jaxb simpleBindingMode for this service in wsdl2java. default=true.
     */
    public static final String JAXB_SIMPLE_BINDING_MODE = "andromda_jaxb_simpleBindingMode";

    /**
     * XML package namespace abbreviation, overrides default sequentially numbered nsX. Each must be unique.
     */
    public static final String XML_XMLNS = "andromda_xml_xmlns";

    /**
     * The prefix for the XSD namespace.
     */
    public static final String XSD_NAMESPACE_PREFIX = "xs:";

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
     * XML package namespace, overrides default reversed package name.
     */
    public static final String XML_NAMESPACE = "andromda_xml_namespace";

    /**
     * If backslash should be appended to all namespaces. Default=true, set to false for backwards compatibility.
     */
    public static final String ADD_NAMESPACE_BACKSLASH = "addNamespaceBackslash";

    /**
     * Use human readable mixed-case enumeration literal values in XSD, instead of literal key. Default=true, set to false for upper-underscore XSD enum values.
     */
    public static final String USE_ENUM_VALUE_IN_XSD = "useEnumValueInXSD";

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
     * REST: andromda_REST_response_status
     */
    public static final String REST_RESPONSE_STATUS = "andromda_REST_response_status";

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
}
