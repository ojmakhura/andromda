package org.andromda.core.metafacade;

import java.util.HashMap;
import java.util.Map;

/**
 * A global cache for metafacades. Used by the {@link MetafacadeFactory}when
 * constructing or retrieving metafacade instances. If the cache constains the
 * metafacade it should not be constructed again.
 * 
 * @author Chad Brandon
 */
public class MetafacadeCache
{

    /**
     * The shared metafacade cache instance.
     */
    private static final MetafacadeCache instance = new MetafacadeCache();

    /**
     * Gets the shared MetafacadeCache instance.
     * 
     * @return the shared instance.
     */
    public static MetafacadeCache instance()
    {
        return instance;
    }

    /**
     * The namespace to which the cache currently applies
     */
    private String namespace;

    /**
     * Sets the namespace to which the cache currently applies.
     * 
     * @param namespace the current namespace.
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * The cache for already created metafacades.
     */
    private final Map metafacadeCache = new HashMap();

    /**
     * <p>
     * Returns the metafacade from the metafacade cache. The Metafacades are
     * cached first by according to its <code>mappingObject</code>, next the
     * <code>metafacadeClass</code>, then according to to the given
     * <code>key</code> and finally by the current namespace.
     * </p>
     * <p>
     * Metafacades must be cached in order to keep track of the state of its
     * validation. If we keep creating a new one each time, we can never tell
     * whether or not a metafacade has been previously validated. Not to mention
     * tremendous performance gains.
     * </p>
     * 
     * @param mappingObject the object to which the mapping applies
     * @param metafacadeClass the class of the metafacade.
     * @param key the unique key for the given mappingObject
     * @return MetafacadeBase stored in the cache.
     */
    public MetafacadeBase get(
        Object mappingObject,
        Class metafacadeClass,
        Object key)
    {
        MetafacadeBase metafacade = null;
        Map namespaceMetafacadeCache = (Map)this.metafacadeCache
            .get(mappingObject);
        if (namespaceMetafacadeCache != null)
        {
            Map metafacadeCache = (Map)namespaceMetafacadeCache
                .get(metafacadeClass);
            if (metafacadeCache != null)
            {
                metafacade = (MetafacadeBase)metafacadeCache.get(this.namespace
                    + key);
            }
        }
        return metafacade;
    }

    /**
     * Adds the <code>metafacade</code> to the cache according to first
     * <code>mappingObject</code>, second the <code>metafacade</code> 
     * Class, third <code>key</code>, and finally by the current the current
     * namespace.
     * 
     * @param mappingObject the mappingObject for which to cache the metafacade.
     * @param key the unique key by which the metafacade is cached (within the
     *        scope of the <code>mappingObject</code>).
     * @param metafacade the metafacade to cache.
     */
    public void add(Object mappingObject, Object key, MetafacadeBase metafacade)
    {
        Map namespaceMetafacadeCache = (Map)this.metafacadeCache
            .get(mappingObject);
        if (namespaceMetafacadeCache == null)
        {
            namespaceMetafacadeCache = new HashMap();
        }
        Map metafacadeCache = (Map)namespaceMetafacadeCache.get(metafacade
            .getClass());
        if (metafacadeCache == null)
        {
            metafacadeCache = new HashMap();
        }
        metafacadeCache.put(this.namespace + key, metafacade);
        namespaceMetafacadeCache.put(metafacade.getClass(), metafacadeCache);
        this.metafacadeCache.put(mappingObject, namespaceMetafacadeCache);
    }

    /**
     * Performs shutdown procedures for the cache. This should be called
     * <strong>ONLY</code> when model processing has completed.
     */
    public void shutdown()
    {
        this.metafacadeCache.clear();
    }
}