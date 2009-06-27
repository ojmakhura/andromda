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
     * Overrides the default attribute/element output for this element.
     */
    public static final String STEREOTYPE_XML_ATTRIBUTE = "XmlAttribute";

    /**
     * Overrides the default attribute/element output for this element.
     */
    public static final String STEREOTYPE_XML_ELEMENT = "XmlElement";

}
