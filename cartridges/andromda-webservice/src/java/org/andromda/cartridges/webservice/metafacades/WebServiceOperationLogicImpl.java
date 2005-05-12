package org.andromda.cartridges.webservice.metafacades;

import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebServiceOperation.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation
 */
public class WebServiceOperationLogicImpl
    extends WebServiceOperationLogic
{
    public WebServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
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
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationName()
     */
    protected String handleGetTestImplementationName()
    {
        return this.getTestImplementationOperationNamePrefix() +
        StringUtils.capitalize(this.getTestName());
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestImplementationCall()
     */
    protected String handleGetTestImplementationCall()
    {
        return "this." + this.getTestImplementationSignature();
    }

    /**
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
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestName()
     */
    protected String handleGetTestName()
    {
        return TEST_NAME_PREFIX + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestCall()
     */
    protected String handleGetTestCall()
    {
        return "this." + this.getSignature();
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#getTestSignature()
     */
    protected String handleGetTestSignature()
    {
        return this.getTestName() + "()";
    }
}