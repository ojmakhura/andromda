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
     * Stores the default cache strategy for associations.
     */
    private static final String HIBERNATE_ASSOCIATION_CACHE = "hibernateAssociationCache";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#getHibernateCacheType()
     */
    protected java.lang.String handleGetHibernateCacheType() 
    {
        String cacheType = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE);
        if (cacheType == null)
        {
            cacheType = String.valueOf(this
                .getConfiguredProperty(HIBERNATE_ASSOCIATION_CACHE));
        }
        return cacheType;
    }
    
}