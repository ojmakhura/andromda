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
    /**
     * @param metaObject
     * @param context
     */
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#getHibernateCacheType()
     */
    protected String handleGetHibernateCacheType()
    {
        String cacheType = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE);
        if (StringUtils.isBlank(cacheType))
        {
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ASSOCIATION_CACHE));
        }
        return StringUtils.trimToEmpty(cacheType);
    }

    @Override
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#isEhCacheEternal()
     */
    protected boolean handleIsEhCacheEternal()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_ETERNAL);
        if (StringUtils.isBlank(eternal))
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_ETERNAL);
        }
        return Boolean.valueOf(eternal).booleanValue();
    }

    @Override
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

    @Override
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

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#isEhCacheOverflowToDisk()
     */
    protected boolean handleIsEhCacheOverflowToDisk()
    {
        String eternal = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        if (StringUtils.isBlank(eternal))
        {
            eternal = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_EHCACHE_OVERFLOW_TO_DISK);
        }
        return Boolean.valueOf(eternal).booleanValue();
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociation#isHibernateCacheDistributed()
     */
    protected boolean handleIsHibernateCacheDistributed()
    {
        {
            String distributed = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_ENTITYCACHE_DISTRIBUTED);
            boolean distributedCachingEnabled = Boolean.valueOf(StringUtils.trimToEmpty(distributed)).booleanValue();

            if (distributedCachingEnabled)
            {
                String entityCacheDistributed =
                    (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATIONCACHE_DISTRIBUTED);
                return Boolean.valueOf(StringUtils.trimToEmpty(entityCacheDistributed)).booleanValue();
            }
            return false;
        }
    }
}