package org.andromda.cartridges.webservice.metafacades;

import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebServiceOperation.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation
 * @author Bob Fields
 */
public class WebServiceOperationLogicImpl
    extends WebServiceOperationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public WebServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) or hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION)
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#isExposed()
     */
    protected boolean handleIsExposed()
    {
        return this.getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) ||
        this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }
    
    /**
     * The prefix given to the test implementation operation names.
     */
    private static final String TEST_IMPLEMENTATION_OPERATION_NAME_PREFIX =
        "testImplementationOperationNamePrefix";

    /**
     * Gets the test implementation operation name prefix.
     */
    private String getTestImplementationOperationNamePrefix()
    {
        return String.valueOf(
            this.getConfiguredProperty(TEST_IMPLEMENTATION_OPERATION_NAME_PREFIX));
    }

    /**
     * @return getTestImplementationOperationNamePrefix() + StringUtils.capitalize(this.getTestName())
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationName()
     */
    protected String handleGetTestImplementationName()
    {
        return this.getTestImplementationOperationNamePrefix() +
        StringUtils.capitalize(this.getTestName());
    }

    /**
     * @return "this." + this.getTestImplementationSignature()
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationCall()
     */
    protected String handleGetTestImplementationCall()
    {
        return "this." + this.getTestImplementationSignature();
    }

    /**
     * @return this.getTestImplementationOperationNamePrefix() + StringUtils.capitalize(this.getTestSignature())
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationSignature()
     */
    protected String handleGetTestImplementationSignature()
    {
        return this.getTestImplementationOperationNamePrefix() +
        StringUtils.capitalize(this.getTestSignature());
    }
    
    /**
     * The prefix given to the junit test operations.
     */
    private static final String TEST_NAME_PREFIX = "test";

    /**
     * @return TEST_NAME_PREFIX + StringUtils.capitalize(this.getName())
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestName()
     */
    protected String handleGetTestName()
    {
        return TEST_NAME_PREFIX + StringUtils.capitalize(this.getName());
    }

    /**
     * @return "this." + this.getSignature()
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestCall()
     */
    protected String handleGetTestCall()
    {
        return "this." + this.getSignature();
    }

    /**
     * @return this.getTestName() + "()"
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestSignature()
     */
    protected String handleGetTestSignature()
    {
        return this.getTestName() + "()";
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_PARAMETER_STYLE = "defaultParameterStyle";
    private static final String DEFAULT = "default";

    @Override
    protected String handleGetParameterStyle()
    {
        String style = (String)this.findTaggedValue(WebServiceGlobals.WEB_SERVICE_PARAMETER_STYLE);
        if (StringUtils.isEmpty(style) || style.equals(DEFAULT))
        {
            style = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_PARAMETER_STYLE));
        }
        if (StringUtils.isEmpty(style) || style.equals(DEFAULT))
        {
            style = "wrapped";
        }
        return style;
    }
}