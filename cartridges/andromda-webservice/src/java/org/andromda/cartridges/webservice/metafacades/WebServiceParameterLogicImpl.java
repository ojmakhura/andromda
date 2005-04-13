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
        ClassifierFacade type = this.getType();
        if (type != null)
        {
            if (WSDLType.class.isAssignableFrom(type.getClass()))
            {
                WSDLType wsdlType = (WSDLType)type;
                ClassifierFacade service = this.getOperation().getOwner();
                if (service != null && WebService.class.isAssignableFrom(service.getClass()))
                {
                    WebService webService = (WebService)service;
                    if (!webService.isRpcStyle() && wsdlType.isArrayType())
                    {
                        testTypeName = webService.getTestPackageName() + '.' + wsdlType.getWsdlArrayName();
                    }
                    else if (!type.isDataType())
                    {
                        testTypeName = webService.getTestPackageName() + '.' + wsdlType.getName();
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