package org.andromda.cartridges.xmlschema;

/**
 * <p>
 *  The XML Schema cartridge profile. Contains the profile
 *  information (tagged values, and stereotypes) for the XML Schema
 *  cartridge.
 * </p>
*/
public class XmlSchemaProfile {

	/**
	 * Shouldn't be instantiated.
	 */ 
    private XmlSchemaProfile(){}

    /* ----------------- Stereotypes -------------------- */
    
   /**
     * <p>
     *  Represents a complex element type.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_XML_SCHEMA_TYPE = "XmlSchemaType";
   

    /* ---------------- Tagged Values ------------------- */
    
   /**
     * <p>
     *  Defines whether or not an attribute should be
     *  represented as an XML Schema attribute during generation time, if
     *  its either false, or not defined, then it will be assumed that
     *  the UML attribute should be represented as an XML Schema element
     *  instead.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_XML_ATTRIBUTE = "XmlAttribute";
   
}
