package org.andromda.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Creates and returns Objects based on a set of 
 * Apache Digester rules in a consistent manner, providing validation
 * in the process.
 * 
 * This XML object factory allows us to define a consistent/clean of configuring
 * java objects from XML configuration files (i.e. it uses the class name of 
 * the java object to find what rule file and what XSD file to use).  It also 
 * allows us to define a consistent way in which schema validation is performed.  
 *  
 * It seperates each concern into one file, for example: to configure and perform validation on
 * the MetafacadeMappings class, we need 3 files 1.) the java object (MetafacadeMappings.java),
 * 2.) the rules file which tells the apache digester how to populate the java object from the
 * XML configuration file (MetafacadeMappings-Rules.xml), and 3.) the XSD schema validation 
 * file (MetafacadeMappings.xsd).  Note that each file is based on the name of the java 
 * object: 'java object name'.xsd  and'java object name'-Rules.xml'.  After you have these 
 * three files then you just need to call the method #getInstance(java.net.URL objectClass) 
 * in this class from the java object you want to configure.  This keeps the dependency 
 * to digester (or whatever XML configuration tool we are using at the time) to this single file.
 * 
 * In order to add/modify an existing element/attribute in your configuration file, first 
 * make the modification in your java object, then modify it's rules file 
 * to instruct the digester on how to configure your new attribute/method in the java object, 
 * and then modify your XSD file to provide correct validation for this new method/attribute.  
 * Please see the org.andromda.core.metafacade.MetafacadeMappings* files for an example on how
 * to do this.
 * 
 * @author Chad Brandon
 */
public class XmlObjectFactory {

	private static final Logger logger = Logger.getLogger(XmlObjectFactory.class);
	
	private static final String RULES_SUFFIX = "-Rules.xml";
    private static final String SCHEMA_SUFFIX = ".xsd";
    
    private Digester digester = null;
    
    private Class objectClass = null;
    private URL objectRulesXml = null;
    private URL schemaUri = null;
    
    /**
     * Whether or not validation should be turned
     * on by default when using this factory to load 
     * XML configuration files.
     */
    private static boolean defaultValidating = true;
    
    /**
     * Cache containing XmlObjectFactory instances
     * which have already been configured for given
     * objectRulesXml
     */
    private static Map factoryCache = new HashMap();
    
    /**
     * Creates an instance of this XmlObjectFactory with the
     * given <code>objectRulesXml</code>
     * @param objectRulesXml
     */
    private XmlObjectFactory(URL objectRulesXml) {
    	final String methodName = "XmlObjectFactory.XmlObjectFactory";
        if (logger.isDebugEnabled())
            logger.debug("performing " 
                + methodName
                + " with objectRulesXml '"
                + objectRulesXml + "'");
        ExceptionUtils.checkNull(methodName, "objectRulesXml", objectRulesXml);
        this.digester = DigesterLoader.createDigester(objectRulesXml);           
    }

    /**
     * Gets an instance of this XmlObjectFactory using
     * the digester rules belonging to the <code>objectClass</code>.
     * 
     * @param objectClass the Class of the object from which to configure this factory.
     * @return the XmlObjectFactoy instance.
     */    
    public static XmlObjectFactory getInstance(Class objectClass) {
        final String methodName = "XmlObjectFactory.getInstance";
        ExceptionUtils.checkNull(methodName, "objectClass", objectClass);
        
        XmlObjectFactory factory = (XmlObjectFactory)factoryCache.get(objectClass);
        if (factory == null) {
            URL objectRulesXml =
                XmlObjectFactory.class.getResource(
                    '/' + objectClass.getName().replace('.', '/') + RULES_SUFFIX);   
            if (objectRulesXml == null) {
                throw new XmlObjectFactoryException(
                    "No configuration rules found for class --> '" 
                    + objectClass + "'");   
            }
            factory = new XmlObjectFactory(objectRulesXml);
            factory.objectClass = objectClass;
            factory.objectRulesXml = objectRulesXml;
            // TODO: Allow graceful way to ignore schema validation if
            // the underlying parser doesn't support it.
            // If the underlying parser doesn't support schema validation 
            // it fails by trying to validate against a non existent DTD (this
            // can happen by running ant inside eclipse for example)
            factory.setValidating(defaultValidating);
            factoryCache.put(objectClass, factory);
        }
        
        return factory;    
    }
    
    /**
     * Allows us to set default validation to true/false for all instances
     * of objects instantiated by this factory.  This is necessary
     * in some cases where the underlying parser doesn't support
     * schema validation (such as when performing JUnit tests)
     * 
     * @param validating true/false
     */
    public static void setDefaultValidating(boolean validating) {
        defaultValidating = validating;
    }
    
    /**
     * Sets whether or not the XmlObjectFactory
     * should be validating, default is false.  If it IS set
     * to be validating, then there needs to be a schema named
     * objectClass.xsd in the same package as the objectClass 
     * that this factory was created from.
     * 
     * @param validating true/false 
     */
    public void setValidating(boolean validating) {
        this.digester.setValidating(validating);
        if (validating) {
            if (this.schemaUri == null) {
                String schemaLocation = 
                    '/' + this.objectClass.getName().replace('.', '/') + SCHEMA_SUFFIX;
                this.schemaUri =
                    XmlObjectFactory.class.getResource(schemaLocation);
                try {
                    if (this.schemaUri != null) {
                        InputStream stream = this.schemaUri.openStream();
                        stream.close();
                    }
                } catch (IOException ex) {
                    this.schemaUri = null;
                }
                if (this.schemaUri == null) {
                    logger.warn("WARNING! Was not able to find schemaUri --> '" 
                        + schemaLocation
                        + "' continuing in non validating mode");
                }
            }
           
            if (this.schemaUri != null) { 
                try {
                    this.digester.setSchema(this.schemaUri.toString());
                    this.digester.setErrorHandler(new XmlObjectValidator());
                } catch (Exception ex) {
                    logger.warn("WARNING! Your parser does NOT support the " 
                        + " schema validation continuing in non validation mode", ex);
                }
            }
        }
    }
    
	/**
	 * Returns a configured Object based on the objectXml configuration file
	 * 
	 * @param objectXml the path to the Object XML config file.
	 * @return Object the created instance.
	 */
	public Object getObject(URL objectXml) {
		final String methodName = "XmlObjectFactoryException.getObject";
		if (logger.isDebugEnabled())
			logger.debug("performing " 
				+ methodName
				+ " with objectXml '"
				+ objectXml 
				+ "'");

		ExceptionUtils.checkNull(methodName, "objectXml", objectXml);

		Object object = null;
		try {
			object = this.digester.parse(objectXml.openStream());
			if (object == null) {
				String errMsg = 
					"Was not able to instantiate an object using objectRulesXml '" 
					+ this.objectRulesXml 
					+ "' with objectXml '" 
					+ objectXml 
					+ "', please check either the objectXml "
					+ "or objectRulesXml file for inconsistencies";
				logger.error(errMsg);
				throw new XmlObjectFactoryException(errMsg);
			}
        } catch (SAXException ex) {
            String validationErrorMsg = "VALIDATION FAILED for --> '" 
                + objectXml + "' against SCHEMA --> '" 
                + this.schemaUri  + "' --> message: '" + ex.getMessage() + "'";
            logger.error(validationErrorMsg);
            throw new XmlObjectFactoryException(validationErrorMsg);
		} catch (Throwable th) {
			String errMsg = "Error performing " 
                + methodName 
                + ", XML resource could not be loaded --> '" 
                + objectXml + "'";
			logger.error(errMsg, th);
			throw new XmlObjectFactoryException(errMsg, th);	
		}
		return object;
	}
    
    /**
     * Returns a configured Object based on the objectXml configuration reader.
     * 
     * @param objectXml the path to the Object XML config file.
     * @return Object the created instance.
     */
    public Object getObject(Reader objectXml) {
        final String methodName = "XmlObjectFactoryException.getObject";
        if (logger.isDebugEnabled())
            logger.debug("performing " 
                + methodName
                + " with objectXml '"
                + objectXml 
                + "'");

        ExceptionUtils.checkNull(methodName, "objectXml", objectXml);

        Object object = null;
        try {
            object = this.digester.parse(objectXml);
            if (object == null) {
                String errMsg = 
                    "Was not able to instantiate an object using objectRulesXml '" 
                    + this.objectRulesXml 
                    + "' with objectXml '" 
                    + objectXml 
                    + "', please check either the objectXml "
                    + "or objectRulesXml file for inconsistencies";
                logger.error(errMsg);
                throw new XmlObjectFactoryException(errMsg);
            }
        } catch (SAXException ex) {
            String validationErrorMsg = "VALIDATION FAILED for --> '" 
                + objectXml + "' against SCHEMA --> '" 
                + this.schemaUri  + "' --> message: '" + ex.getMessage() + "'";
            logger.error(validationErrorMsg);
            throw new XmlObjectFactoryException(validationErrorMsg);
        } catch (Throwable th) {
            String errMsg = "Error performing " 
                + methodName 
                + ", XML resource could not be loaded --> '" 
                + objectXml + "'";
            logger.error(errMsg, th);
            throw new XmlObjectFactoryException(errMsg, th);    
        }
        return object;        
    }
    
    /**
     * Handles the validation errors.
     */ 
    protected class XmlObjectValidator implements org.xml.sax.ErrorHandler {
   
        public XmlObjectValidator() {
            final String methodName = "XmlObjectValidator";
            if (logger.isDebugEnabled()) {
                logger.debug("constructing new " + methodName); 
            }   
        }
        
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(exception.getMessage());
        }
        
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(exception.getMessage());
        }
        
        public void warning(SAXParseException exception) {
        	logger.warn("WARNING!: " + exception.toString());   
        }

    }

    
}
