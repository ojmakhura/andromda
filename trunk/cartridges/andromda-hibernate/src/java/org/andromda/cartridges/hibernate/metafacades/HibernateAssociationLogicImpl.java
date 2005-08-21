package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateAssociation.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation
 */
public class HibernateAssociationLogicImpl
    extends HibernateAssociationLogic
{
    // ---------------- constructor -------------------------------
    public HibernateAssociationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
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
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ASSOCIATION_CACHE));
        }
        return StringUtils.trimToEmpty(cacheType);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#getEhCacheMaxElementsInMemory()
     */
    protected int handleGetEhCacheMaxElementsInMemory()
    {
        String maxElements = null;
        maxElements = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_MAX_ELEMENTS);
        if (StringUtils.isBlank(maxElements))
        {
            maxElements = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_MAX_ELEMENTS);
        }
        return Integer.parseInt(maxElements);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#isEhCacheEternal()
     */
    protected boolean handleIsEhCacheEternal()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL);
        if (eternal == null)
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_ETERNAL);
        }
        return Boolean.valueOf(eternal).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#getEhCacheTimeToIdleSeconds()
     */
    protected int handleGetEhCacheTimeToIdleSeconds()
    {
        String timeToIdle = null;
        timeToIdle = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_IDLE);
        if (StringUtils.isBlank(timeToIdle))
        {
            timeToIdle = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_IDLE);
        }
        return Integer.parseInt(timeToIdle);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#getEhCacheTimeToLiveSeconds()
     */
    protected int handleGetEhCacheTimeToLiveSeconds()
    {
        String timeToLive = null;
        timeToLive = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_TIME_TO_LIVE);
        if (StringUtils.isBlank(timeToLive))
        {
            timeToLive = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_TIME_TO_LIVE);
        }
        return Integer.parseInt(timeToLive);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#isEhCacheOverflowToDisk()
     */
    protected boolean handleIsEhCacheOverflowToDisk()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        if (eternal == null)
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        }
        return Boolean.valueOf(eternal).booleanValue();
    }
}