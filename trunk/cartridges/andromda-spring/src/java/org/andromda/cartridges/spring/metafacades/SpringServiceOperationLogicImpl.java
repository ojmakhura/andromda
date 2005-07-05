package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringServiceOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
 */
public class SpringServiceOperationLogicImpl
        extends SpringServiceOperationLogic
{

    public SpringServiceOperationLogicImpl(Object metaObject, String context)
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
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getSignature()));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationL#getImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * SpringGlobals#PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(
                SpringGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll("\\{0\\}", replacement);
    }

    /**
     * The transation type for Spring service operations.
     */
    private static final String SERVICE_OPERATION_TRANSACTION_TYPE = "serviceOperationTransactionType";

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getTransactionType()
     */
    public String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

    /**
     * The transaction type for EJB wrapped service operations..
     */
    private static final String EJB_SERVICE_OPERATION_TRANSACTION_TYPE = "ejbServiceOperationTransactionType";

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getEjbTransactionType()
     */
    protected String handleGetEjbTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJB_SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getThrowsClause()
     */
    protected String handleGetThrowsClause()
    {
        StringBuffer throwsClause = null;
        if (this.isExceptionsPresent())
        {
            throwsClause = new StringBuffer(this.getExceptionList());
        }
        if (throwsClause != null)
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause != null ? throwsClause.toString() : null;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getThrowsClause(java.lang.String)
     */
    protected String handleGetThrowsClause(String initialExceptions)
    {
        final StringBuffer throwsClause = new StringBuffer(initialExceptions);
        if (this.getThrowsClause() != null)
        {
            throwsClause.insert(0, ", ");
            throwsClause.insert(0, this.getThrowsClause());
        }
        else
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause.toString();
    }
}