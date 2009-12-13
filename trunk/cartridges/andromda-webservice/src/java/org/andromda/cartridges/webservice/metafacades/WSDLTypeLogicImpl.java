package org.andromda.cartridges.webservice.metafacades;

import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.cartridges.webservice.WebServiceUtils;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLType.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLType
 * @author Bob Fields
 */
public class WSDLTypeLogicImpl
        extends WSDLTypeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public WSDLTypeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(WSDLTypeLogicImpl.class);

    /**
     * @return String this.getSchemaType(true, true)
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getSchemaType()
     */
    public String handleGetSchemaType()
    {
        return this.getSchemaType(true, true);
    }

    /**
     * @param withPrefix 
     * @param preserveArray 
     * @return String WebServiceUtils.getSchemaType
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getSchemaType(boolean, boolean)
     */
    public String handleGetSchemaType(boolean withPrefix, boolean preserveArray)
    {
        return WebServiceUtils.getSchemaType(this, this.getSchemaTypeMappings(), this.getNamespacePrefix(),
                this.getQName(), this.getWsdlArrayNamePrefix(), withPrefix, preserveArray);
    }

    /**
     * @return getQName().insert(0, this.getWsdlArrayNamePrefix())
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getWsdlArrayName()
     */
    protected String handleGetWsdlArrayName()
    {
        StringBuffer name = new StringBuffer(StringUtils.trimToEmpty(this.getQName()).replaceAll("\\[\\]", ""));
        name.insert(0, this.getWsdlArrayNamePrefix());
        return name.toString();
    }

    /**
     * @return String getConfiguredProperty(WebServiceGlobals.ARRAY_NAME_PREFIX)
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getWsdlArrayNamePrefix()
     */
    protected String handleGetWsdlArrayNamePrefix()
    {
        return String.valueOf(this.getConfiguredProperty(WebServiceGlobals.ARRAY_NAME_PREFIX));
    }

    /**
     * @return String this.getQualfiedNameLocalPartPattern().replaceAll("\\{0\\}")
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getQName()
     */
    protected String handleGetQName()
    {
        return this.getQualfiedNameLocalPartPattern().replaceAll("\\{0\\}", StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @return String WebServiceUtils.reversePackage(packageName)
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getNamespace()
     */
    protected String handleGetNamespace()
    {
        String packageName = this.getPackageName();
        if (this.isReverseNamespace())
        {
            packageName = WebServiceUtils.reversePackage(packageName);
        }
        return this.getNamespacePattern().replaceAll("\\{0\\}", StringUtils.trimToEmpty(packageName));
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * Gets the schemaType mappings that have been set for this schema type.
     *
     * @return the TypeMappings instance.
     */
    private TypeMappings getSchemaTypeMappings()
    {
        final String propertyName = WebServiceGlobals.SCHEMA_TYPE_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                mappings.setArraySuffix(this.getArraySuffix());
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + '\'';
                logger.error(errMsg, th);
                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic#handleGetNamespacePrefix()
     */
    protected String handleGetNamespacePrefix()
    {
        return (String)this.getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PREFIX);
    }

    /**
     * Gets the <code>qualifiedNameLocalPartPattern</code> for this WSDL type.
     * @return String getConfiguredProperty(WebServiceLogicImpl.QNAME_LOCAL_PART_PATTERN)
     */
    protected String getQualfiedNameLocalPartPattern()
    {
        return (String)this.getConfiguredProperty(WebServiceLogicImpl.QNAME_LOCAL_PART_PATTERN);
    }

    /**
     * Gets the <code>namespacePattern</code> for this type.
     * @return getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PATTERN)
     */
    protected String getNamespacePattern()
    {
        return (String)this.getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PATTERN);
    }

    /**
     * Gets whether or not <code>reverseNamespace</code> is true/false for this type.
     *
     * @return boolean true/false
     */
    private boolean isReverseNamespace()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(WebServiceLogicImpl.REVERSE_NAMESPACE)));
    }
}