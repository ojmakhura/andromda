package org.andromda.cartridges.webservice.metafacades;

import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.webservice.metafacades.WSDLType.
 * 
 * @see org.andromda.cartridges.webservice.metafacades.WSDLType
 */
public class WSDLTypeLogicImpl
    extends WSDLTypeLogic
    implements org.andromda.cartridges.webservice.metafacades.WSDLType
{
    // ---------------- constructor -------------------------------

    public WSDLTypeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class WSDLType ...
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getSchemaType()
     */
    public java.lang.String handleGetSchemaType()
    {
        return this.getSchemaType(true, true);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getSchemaType(boolean,
     *      boolean)
     */
    public java.lang.String handleGetSchemaType(
        boolean withPrefix,
        boolean preserveArray)
    {
        try {
            StringBuffer schemaType = new StringBuffer();
            String modelName = this.getFullyQualifiedName(true);
            if (this.getSchemaTypeMappings() != null)
            {
                String namespacePrefix = this.getNamespacePrefix() + ':';
                
                String mappedValue = this.getSchemaTypeMappings().getTo(modelName);
                if (!mappedValue.equals(modelName))
                {
                    schemaType.append(mappedValue);
                }
                else
                {
                    if (withPrefix)
                    {
                        schemaType.append(namespacePrefix);
                    }
                    if (this.getNonArray() != null)
                    {
                        schemaType.append(this.getNonArray().getName());
                    }
                }
                // remove any array '[]' suffix
                schemaType = new StringBuffer(schemaType.toString().replaceAll(
                    "\\[\\]",
                    ""));
                if (preserveArray && this.isArrayType())
                {
                    int insertIndex = namespacePrefix.length();
                    if (!schemaType.toString().startsWith(namespacePrefix))
                    {
                        if (withPrefix)
                        {
                            // add the prefix for any normal XSD types
                            // that may not have been set above
                            schemaType.insert(0, namespacePrefix);
                        }
                        else
                        {
                            // since we aren't adding the prefix, set
                            // the correct insert index
                            insertIndex = 0;
                        }
                    }
                    schemaType.insert(insertIndex, "ArrayOf");
                }
                if (withPrefix && !schemaType.toString().startsWith(namespacePrefix))
                {
                    schemaType.insert(0, "xsd:");
                }
            }
            return schemaType.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Schema type mappings property reference.
     */
    private static final String SCHEMA_TYPE_MAPPINGS_URI = "schemaTypeMappingsUri";

    /**
     * Allows the MetafacadeFactory to populate the schemaType mappings for this
     * model element.
     * 
     * @param mappingUri the URI of the schemaType mappings resource.
     */
    public void setSchemaTypeMappingsUri(String mappingUri)
    {
        try
        {
            // register the mappings with the component container.
            this.registerConfiguredProperty(SCHEMA_TYPE_MAPPINGS_URI, Mappings
                .getInstance(mappingUri));
        }
        catch (Throwable th)
        {
            String errMsg = "Error setting '" + SCHEMA_TYPE_MAPPINGS_URI
                + "' --> '" + mappingUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
    }

    /**
     * Gets the schemaType mappings that have been set for this
     * schema type.
     * 
     * @return the Mappings instance.
     */
    public Mappings getSchemaTypeMappings()
    {
        return (Mappings)this.getConfiguredProperty(SCHEMA_TYPE_MAPPINGS_URI);
    }
    
    /**
     * Sets the <code>namespacePrefix</code> for the WSDLs type.
     *
     * @param namespacePrefix the namespace prefix to use for these types.
     */
    public void setNamespacePrefix(String namespacePrefix) 
    {
        this.registerConfiguredProperty(
            WebServiceLogicImpl.NAMESPACE_PREFIX,
            StringUtils.trimToEmpty(namespacePrefix));
    }
    
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getNamespacePrefix()
     */
    public String handleGetNamespacePrefix() 
    {
        return (String)this.getConfiguredProperty(WebServiceLogicImpl.NAMESPACE_PREFIX);
    }    
}