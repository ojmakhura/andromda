package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateServiceOperation.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateServiceOperation
 */
public class HibernateServiceOperationLogicImpl
    extends HibernateServiceOperationLogic
{
    // ---------------- constructor -------------------------------
    public HibernateServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Stores the default transaction type for service operations.
     */
    private static final String SERVICE_OPERATION_TRANSACTION_TYPE = "serviceOperationTransactionType";

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getTransactionType()
     */
    public String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType =
                transactionType = String.valueOf(this.getConfiguredProperty(SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }
}