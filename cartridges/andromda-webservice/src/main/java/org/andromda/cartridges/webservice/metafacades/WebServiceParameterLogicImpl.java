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

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic#handleIsAttribute()
     */
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

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic#handleIsElement()
     */
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

    private static final String DEFAULT = "default";
    private static final String EMPTY_STRING = "";
    private static final String BOOLEAN_FALSE = "false";
    //private static final String DEFAULT_TYPE = "PathParam";

    private static final String QUOTE = "\"";
    private static final String RPARENS = "(";
    private static final String LPARENS = ")";
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic#getRestParamType()
     */
    @Override
    protected String handleGetRestParamType()
    {
        String paramType = (String)this.findTaggedValue(WebServiceGlobals.REST_PARAM_TYPE);
        if (StringUtils.isBlank(paramType) || paramType.equals(DEFAULT))
        {
            paramType = EMPTY_STRING;
        }
        else
        {
            String pathSegment = handleGetRestPathSegment();
            if (StringUtils.isBlank(pathSegment))
            {
                // paramType always needed with annotation
                pathSegment = this.getName();
            }
            paramType = "@javax.ws.rs." + paramType + RPARENS + QUOTE + pathSegment + QUOTE + LPARENS;
        }

        return paramType;
    }

    private static final String AT = "@";
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic#getRestPathParam()
     */
    @Override
    protected String handleGetRestPathParam()
    {
        String pathParam = (String)this.findTaggedValue(WebServiceGlobals.REST_PATH_PARAM);
        if (StringUtils.isBlank(pathParam) || pathParam.equals(DEFAULT))
        {
            pathParam = this.getName();
        }
        pathParam = AT + handleGetRestParamType() + "(\"" + pathParam + "\")";
        return pathParam;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic#getRestPathSegment()
     */
    @Override
    protected String handleGetRestPathSegment()
    {
        String pathSegment = (String)this.findTaggedValue(WebServiceGlobals.REST_PATH_SEGMENT);
        if (StringUtils.isBlank(pathSegment) || pathSegment.equals(DEFAULT))
        {
            pathSegment = EMPTY_STRING;
        }
        return pathSegment;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic#isRestEncoded()
     */
    @Override
    protected boolean handleIsRestEncoded()
    {
        String restEncoded = (String)this.findTaggedValue(WebServiceGlobals.REST_ENCODED);
        if (StringUtils.isBlank(restEncoded) || restEncoded.equals(DEFAULT))
        {
            restEncoded = BOOLEAN_FALSE;
        }
        return Boolean.valueOf(restEncoded);
    }
}