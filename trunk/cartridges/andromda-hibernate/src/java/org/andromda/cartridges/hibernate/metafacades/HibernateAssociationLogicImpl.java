package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateAssociation.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation
 */
public class HibernateAssociationLogicImpl
    extends HibernateAssociationLogic
{
    // ---------------- constructor -------------------------------

    public HibernateAssociationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#getHibernateCacheType()
     */
    protected java.lang.String handleGetHibernateCacheType() 
    {
        return (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE);
    }
    
}