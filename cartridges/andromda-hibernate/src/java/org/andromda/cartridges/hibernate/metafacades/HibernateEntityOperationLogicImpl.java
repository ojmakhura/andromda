package org.andromda.cartridges.hibernate.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateEntityOperation.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityOperation
 */
public class HibernateEntityOperationLogicImpl
       extends HibernateEntityOperationLogic
       implements org.andromda.cartridges.hibernate.metafacades.HibernateEntityOperation
{
    // ---------------- constructor -------------------------------

    public HibernateEntityOperationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityOperation#getImplementationName()
     */
    protected java.lang.String handleGetImplementationName()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityOperation#getImplementationCall()
     */
    protected java.lang.String handleGetImplementationCall()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getCall());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityOperation#getImplementationSignature()
     */
    protected java.lang.String handleGetImplementationSignature()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getSignature());
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
                    .getConfiguredProperty(HibernateGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX)));
    }
}
