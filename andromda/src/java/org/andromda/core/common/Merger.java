package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.mapping.Mapping;
import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * A class that performs the merging abilities for the AndroMDA core.
 * </p>
 * <p>
 * merging takes place when the {@link NamespaceProperties#MERGE_MAPPINGS_URI}
 * is found within the <code>namespace</code> and merge mappings are used to
 * replace any matching patterns in the given <code>string</code>.
 * </p>
 * 
 * @author Chad Brandon
 */
public class Merger
{
    /**
     * The shared instance
     */
    private static Merger instance = null;

    /**
     * Gets the shared Merger instance. Normally you'll want to retrieve the
     * instance through this method.
     * 
     * @return the shared instance.
     */
    public static Merger instance()
    {
        if (instance == null)
        {
            instance = new Merger();
        }
        return instance;
    }

    private String namespace = null;

    /**
     * Keeps us from getting infinite recursion since the mappings are
     * configured with files that are merged and this class uses the
     * {@link org.andromda.core.mapping.Mappings}instances.
     */
    private Collection mergedStringCache = new ArrayList();

    /**
     * The namespace to use when performing any merging during
     * {@link #getMergedString(String, String)}. This namespace is searched
     * when attempting to find the
     * {@link NamespaceProperties#MERGE_MAPPINGS_URI}.
     * 
     * @param namespace the namespace to set.
     */
    public void setNamespace(String namespace)
    {
        // clear out the cache of merged strings.
        this.mergedStringCache.clear();
        this.namespace = namespace;
    }

    /**
     * Stores the cached merge mappings already found (so we don't need to
     * reconstrut again each time).
     */
    private Map mergeMappingsCache = new HashMap();

    /**
     * <p>
     * Retrieves the <em>merged</em> string. The merging takes place when the
     * {@link NamespaceProperties#MERGE_MAPPINGS_URI}is found within the
     * <code>namespace</code> and the merge mappings are used to replace any
     * matching patterns in the given <code>string</code>.
     * </p>
     * 
     * @param string the String to be replaced
     * @return the replaced String.
     */
    public String getMergedString(String string)
    {
        // avoid any possible infinite recursion with the mergedStringCache
        // check (may need to refactor the mergedStringCache solution)
        if (this.namespace != null && string != null
            && !mergedStringCache.contains(string))
        {
            this.mergedStringCache.add(string);
            Mappings mergeMappings = this.getMergeMappings(namespace);
            if (mergeMappings != null)
            {
                Collection mappings = mergeMappings.getMappings();
                if (mappings != null)
                {
                    for (Iterator mappingsIterator = mappings.iterator(); mappingsIterator
                        .hasNext();)
                    {
                        Mapping mapping = (Mapping)mappingsIterator.next();
                        Collection froms = mapping.getFroms();
                        if (froms != null)
                        {
                            for (Iterator fromsIterator = froms.iterator(); fromsIterator
                                .hasNext();)
                            {
                                String from = StringUtils
                                    .trimToEmpty((String)fromsIterator.next());
                                if (StringUtils.isNotEmpty(from))
                                {
                                    String to = StringUtils.trimToEmpty(mapping
                                        .getTo());
                                    string = StringUtils.replace(
                                        string,
                                        from,
                                        to);
                                }
                            }
                        }
                    }
                }
            }
        }
        return string;
    }

    /**
     * Attempts to retrieve the Mappings instance for the given
     * <code>mergeMappingsUri</code> belonging to the given
     * <code>namespace</code>.
     * 
     * @param namespace the namespace to which the mappings belong.
     * @return the Mappings instance.
     */
    private Mappings getMergeMappings(String namespace)
    {
        Mappings mergeMappings = null;
        if (StringUtils.isNotBlank(namespace))
        {
            Property mergeMappingsUri = Namespaces.instance()
                .findNamespaceProperty(
                    namespace,
                    NamespaceProperties.MERGE_MAPPINGS_URI,
                    false);
            String mergeMappingsUriValue = mergeMappingsUri != null
                ? mergeMappingsUri.getValue()
                : null;
            mergeMappingsUriValue = StringUtils
                .trimToEmpty(mergeMappingsUriValue);
            if (StringUtils.isNotEmpty(mergeMappingsUriValue))
            {
                mergeMappings = (Mappings)this.mergeMappingsCache
                    .get(mergeMappingsUriValue);
                if (mergeMappings == null)
                {
                    mergeMappings = Mappings.getInstance(mergeMappingsUriValue);
                    this.mergeMappingsCache.put(
                        mergeMappingsUriValue,
                        mergeMappings);
                }
            }
        }
        return mergeMappings;
    }
}