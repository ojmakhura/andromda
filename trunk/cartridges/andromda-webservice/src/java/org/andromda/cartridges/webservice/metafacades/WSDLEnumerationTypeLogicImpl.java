package org.andromda.cartridges.webservice.metafacades;

import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.cartridges.webservice.WebServiceUtils;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType
 */
public class WSDLEnumerationTypeLogicImpl
        extends WSDLEnumerationTypeLogic
{
    // ---------------- constructor -------------------------------

    public WSDLEnumerationTypeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getSchemaType()
     */
    protected java.lang.String handleGetSchemaType()
    {
        return this.getSchemaType(true, true);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getQName()
     */
    protected String handleGetQName()
    {
        return this.getQualfiedNameLocalPartPattern().replaceAll("\\{0\\}", StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getNamespacePrefix()
     */
    protected String handleGetNamespacePrefix()
    {
        return (String)this.getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PREFIX);
    }

    /**
     * Gets the <code>qualifiedNameLocalPartPattern</code> for this WSDL enumeration.
     */
    protected String getQualfiedNameLocalPartPattern()
    {
        return (String)this.getConfiguredProperty(WebServiceLogicImpl.QNAME_LOCAL_PART_PATTERN);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getNamespace()
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
     * Gets the <code>namespacePattern</code> for this type.
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
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(WebServiceLogicImpl.REVERSE_NAMESPACE)))
                .booleanValue();
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getSchemaType(boolean, boolean)
     */
    public java.lang.String handleGetSchemaType(boolean withPrefix, boolean preserveArray)
    {
        return WebServiceUtils.getSchemaType(this, this.getSchemaTypeMappings(), this.getNamespacePrefix(),
                this.getQName(), this.getWsdlArrayNamePrefix(), withPrefix, preserveArray);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getWsdlArrayNamePrefix()
     */
    protected String handleGetWsdlArrayNamePrefix()
    {
        return String.valueOf(this.getConfiguredProperty(WebServiceGlobals.ARRAY_NAME_PREFIX));
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType#getWsdlArrayName()
     */
    protected String handleGetWsdlArrayName()
    {
        StringBuffer name = new StringBuffer(StringUtils.trimToEmpty(this.getQName()).replaceAll("\\[\\]", ""));
        name.insert(0, this.getWsdlArrayNamePrefix());
        return name.toString();
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
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance((String)property);
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
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
}