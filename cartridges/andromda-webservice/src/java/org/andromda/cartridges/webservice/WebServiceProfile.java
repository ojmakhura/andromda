package org.andromda.cartridges.webservice;


/**
 * <p>
 *  The Metafacade profile. Contains the profile information
 *  (tagged values, and stereotypes) for the Metafacade cartridge.
 * </p>
*/
public class WebServiceProfile {

	/**
	 * Shouldn't be instantiated.
	 */ 
    private WebServiceProfile(){}

    /* ----------------- Stereotypes -------------------- */
    
   /**
     * <p>
     *  Represents a generic service. If the an element is stereotyped
     *  with this it must also have the 'webserviceOperation'
     *  stereotype on each operation if it wishes to expose as a web
     *  service.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_SERVICE = "Service";
   
   /**
     * <p>
     *  Represents the "type mapping" object of an exposed webservice.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_VALUEOBJECT = "ValueObject";
   
   /**
     * <p>
     *  Represents a web service. Stereotype a class with this
     *  stereotype when you want everything on the class to be exposed
     *  as a web service.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_WEBSERVICE = "WebService";
   
   /**
     * <p>
     *  Stereotype an operation on a 'service' if you wish to expose
     *  the method.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_WEBSERVICE_OPERATION = "WebserviceOperation";
   
   /**
     * <p>
     *  Represents an enumeration type.
     * </p>   
    */
   public static final java.lang.String STEREOTYPE_ENUMERATION = "Enumeration";
   

    /* ---------------- Tagged Values ------------------- */
    
   /**
     * <p>
     *  Defines the style of the web service to be generated (i.e.
     *  wrapped, etc.)
     * </p>   
    */
   public static final java.lang.String TAGGEDVALUE_WEBSERVICE_STYLE = "webService.style";
   
   /**
     * <p>
     *  The use of the service to be generated (i.e. literal, encoded).
     * </p>   
    */
   public static final java.lang.String TAGGEDVALUE_WEBSERVICE_USE = "webService.use";
   
   /**
     * <p>
     *  Stores the provider of the service (i.e. RPC, EJB, etc)
     * </p>   
    */
   public static final java.lang.String TAGGEDVALUE_WEBSERVICE_PROVIDER = "webService.provider";
   
}
