package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateServiceOperation.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateServiceOperation
 */
public class HibernateServiceOperationLogicImpl
       extends HibernateServiceOperationLogic
       implements org.andromda.cartridges.hibernate.metafacades.HibernateServiceOperation
{
    // ---------------- constructor -------------------------------

    public HibernateServiceOperationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    private static final String SERVICE_OPERATION_TRANSACTION_TYPE = "serviceOperationTransactionType";
    
    /**
     * Override so that we can provide the default.
     * 
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getTransactionType()
     */
    public String getTransactionType()
    {
        String transactionType = String.valueOf(this.getConfiguredProperty(SERVICE_OPERATION_TRANSACTION_TYPE));
        Object value = this
            .findTaggedValue(UMLProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        if (value != null)
        {
            transactionType = String.valueOf(value);
        }
        return transactionType;
    }
}
