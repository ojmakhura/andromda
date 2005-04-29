package org.andromda.cartridges.webservice.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebServiceParameter.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter
 */
public class WebServiceParameterLogicImpl
        extends WebServiceParameterLogic
{
    // ---------------- constructor -------------------------------

    public WebServiceParameterLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

    /**
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
}