package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringServiceOperation.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
 */
public class SpringServiceOperationLogicImpl
    extends SpringServiceOperationLogic
{
    // ---------------- constructor -------------------------------

    public SpringServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#isWebserviceExposed()
     */
    protected boolean handleIsWebserviceExposed()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getSignature());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationL#getImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getCall());
    }

    /**
     * Retrieves the implementationNamePrefix property from the namespace.
     * 
     * @return the implementation name prefix
     */
    private String getImplementationNamePrefix()
    {
        return StringUtils
            .trimToEmpty(String
                .valueOf(this
                    .getConfiguredProperty(SpringGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX)));
    }
    
    /**
     * The transation type for Spring service.
     */
    private static final String TRANSACTION_TYPE = "transactionType";
        
    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getTransactionType()
     */
    public String getTransactionType()
    {
        String transactionType = super.getTransactionType();
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(TRANSACTION_TYPE));
        }
        return transactionType;
    }

    private static final String EJB_TRANSACTION_TYPE = "ejbTransactionType";
    
    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getEjbTransactionType()
     */
    protected String handleGetEjbTransactionType()
    {
        String transactionType = super.getTransactionType();
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJB_TRANSACTION_TYPE));
        }
        return transactionType;        
    }

}