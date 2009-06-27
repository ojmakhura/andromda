package org.andromda.cartridges.webservice.metafacades;

import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebServiceParameter.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter
 * @author Bob Fields
 */
public class WebServiceParameterLogicImpl
        extends WebServiceParameterLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public WebServiceParameterLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return !this.isRequired()
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

    /**
     * @return testTypeName
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter#getTestTypeName()
     */
    protected String handleGetTestTypeName()
    {
        String testTypeName = null;
        final ClassifierFacade type = this.getType();
        if (type instanceof WSDLType || type instanceof WSDLEnumerationType)
        {
            ClassifierFacade service = this.getOperation().getOwner();
            if (service instanceof WebService)
            {
                WebService webService = (WebService)service;
                final String testPackageName = webService.getTestPackageName();
                if (type instanceof WSDLType)
                {
                    final WSDLType wsdlType = (WSDLType)type;
                    if (!webService.isRpcStyle() && wsdlType.isArrayType())
                    {
                        testTypeName = testPackageName + '.' + wsdlType.getWsdlArrayName();
                    }
                    else if (!type.isDataType())
                    {
                        testTypeName = testPackageName + '.' + wsdlType.getName();
                    }
                }
                else
                {
                    final WSDLEnumerationType wsdlType = (WSDLEnumerationType)type;
                    if (!webService.isRpcStyle() && wsdlType.isArrayType())
                    {
                        testTypeName = testPackageName + '.' + wsdlType.getWsdlArrayName();
                    }
                    else if (!type.isDataType())
                    {
                        testTypeName = testPackageName + '.' + wsdlType.getName();
                    }                    
                }
            }
            if (testTypeName == null)
            {
                testTypeName = this.getType().getFullyQualifiedName();
            }
        }
        return testTypeName;
    }

    /**
     * The property defining the web service XML Adapter for Jaxb.
     */
    private static final String USE_ATTRIBUTES = "useAttributes";

    @Override
    protected boolean handleIsAttribute()
    {
        boolean isAttribute = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ATTRIBUTE);
        if (!isAttribute)
        {
            String attributes = String.valueOf(this.getConfiguredProperty(USE_ATTRIBUTES));
            if (StringUtils.isEmpty(attributes))
            {
                attributes = "true";
            }
            isAttribute = Boolean.parseBoolean(attributes);
        }
        return isAttribute;
    }

    @Override
    protected boolean handleIsElement()
    {
        boolean isAttribute = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ELEMENT);
        if (!isAttribute)
        {
            String attributes = String.valueOf(this.getConfiguredProperty(USE_ATTRIBUTES));
            if (StringUtils.isEmpty(attributes))
            {
                attributes = "true";
            }
            isAttribute = Boolean.parseBoolean(attributes);
        }
        return !isAttribute;
    }
}