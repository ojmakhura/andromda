package org.andromda.cartridges.webservice.metafacades;

import java.util.ArrayList;
import java.util.List;
import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.cartridges.webservice.WebServiceUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
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
    private static final long serialVersionUID = 34L;
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
                this.getName(), this.getWsdlArrayNamePrefix(), withPrefix, preserveArray);
    }

    /**
     * @return getName().insert(0, this.getWsdlArrayNamePrefix())
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getWsdlArrayName()
     */
    protected String handleGetWsdlArrayName()
    {
        StringBuilder name = new StringBuilder(StringUtils.trimToEmpty(this.getName()).replaceAll("\\[\\]", ""));
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
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(WebServiceLogicImpl.REVERSE_NAMESPACE)))
                .booleanValue();
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic#handleIsWebFaultAnException()
     */
    @Override
    protected boolean handleIsWebFaultAnException()
    {
        boolean result = true;
        if (this.hasStereotype(UMLProfile.STEREOTYPE_WEB_FAULT))
        {
            if (!this.hasStereotype(UMLProfile.STEREOTYPE_APPLICATION_EXCEPTION) &&
                !this.hasStereotype(UMLProfile.STEREOTYPE_UNEXPECTED_EXCEPTION) &&
                !this.hasStereotype(UMLProfile.STEREOTYPE_EXCEPTION))
            {
                result = false;
            }
        }
        return result;
    }

    private static final List<WebServiceOperation> weboperations = new ArrayList<WebServiceOperation>();
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic#handleIsWebFaultThrown()
     */
    @Override
    protected boolean handleIsWebFaultThrown()
    {
        boolean result = true;
        if (this.hasStereotype(UMLProfile.STEREOTYPE_WEB_FAULT))
        {
            result = false;
            // collect all webservice operations for the entire model in a static list, only once for all wsdl types
            if (weboperations.isEmpty())
            {
                for (ClassifierFacade classifier : this.getModel().getAllClasses())
                {
                    boolean isService = classifier.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE);
                    for (OperationFacade operation : classifier.getOperations())
                    {
                        boolean visibility = operation.getVisibility().equals("public") || operation.getVisibility().equals("protected");
                        if (visibility && (isService || operation.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE)))
                        {
                            weboperations.add((WebServiceOperation)operation);
                        }
                    }
                }
            }
            for (WebServiceOperation op : weboperations)
            {
                for (Object exception : op.getExceptions())
                {
                    if (((ModelElementFacade)exception).getFullyQualifiedName().equals(this.getFullyQualifiedName()))
                    {
                        // WebFault is actually thrown by a service - OK
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }
}