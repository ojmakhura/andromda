package org.andromda.cartridges.hibernate.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration
 */
public class HibernateEnumerationLogicImpl
       extends HibernateEnumerationLogic
       implements org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration
{
    // ---------------- constructor -------------------------------

    public HibernateEnumerationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getFullyQualifiedHibernateType()
     */
    protected java.lang.String handleGetFullyQualifiedHibernateType() 
    {
        return getFullyQualifiedName();
    }
    
}
