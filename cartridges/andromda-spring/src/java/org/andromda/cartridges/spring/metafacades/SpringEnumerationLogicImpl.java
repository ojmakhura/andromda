package org.andromda.cartridges.spring.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringEnumeration.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringEnumeration
 */
public class SpringEnumerationLogicImpl
    extends SpringEnumerationLogic
{
    // ---------------- constructor -------------------------------

    public SpringEnumerationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEnumeration#getFullyQualifiedHibernateType()
     */
    protected java.lang.String handleGetFullyQualifiedHibernateType() 
    {
        return getFullyQualifiedName();
    }
}
