package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.mapping.Mapping;
import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * <p/>
 * A class that performs the merging abilities for the AndroMDA core. </p>
 * <p/>
 * Merging takes place when the {@link NamespaceProperties#MERGE_MAPPINGS_URI} is found within the
 * <code>namespace</code> and merge mappings are used to replace any matching patterns in the given <code>string</code>.
 * </p>
 *
 * @author Chad Brandon
 */
public class Merger
{
    private static final Logger logger = Logger.getLogger(Merger.class);

    /**
     * The shared instance
     */
    private static final Merger instance = new Merger();

    /**
     * Stores the cached merge mappings already found (so we don't need to reconstruct again each time).
     */
    private final Map mergeMappingsCache = new LinkedHashMap();

    /**
     * Gets the shared Merger instance. Normally you'll want to retrieve the instance through this method.
     *
     * @return the shared instance.
     */
    public static Merger instance()
    {
        return instance;
    }

    /**
     * <p>
     * Retrieves the <em>merged</em> string. The merging takes place when
     * the {@link NamespaceProperties#MERGE_MAPPINGS_URI}is found within the
     * <code>namespace</code> and the merge mappings are used to replace any
     * matching patterns in the given <code>string</code>.
     * </p>
     *
     * @param string the String to be replaced
     * @param namespace This namespace is searched when attempting to find the
     *        {@link NamespaceProperties#MERGE_MAPPINGS_URI}.
     * @return the replaced String.
     */
    public String getMergedString(
        String string,
        final String namespace)
    {
        // avoid any possible infinite recursion with the mergedStringCache
        // check (may need to refactor the mergedStringCache solution)
        if (namespace != null && string != null)
        {
            final Collection<Mappings> mappingInstances = this.getMergeMappings(namespace);
            for (final Mappings mergeMappings : mappingInstances)
            {
                final Collection<Mapping> mappings = mergeMappings.getMappings();
                if ((mappings != null) && !mappings.isEmpty())
                {
                    for (final Mapping mapping : mappings)
                    {
                        final Collection<String> froms = mapping.getFroms();
                        for (String from : froms)
                       {
                            from = StringUtils.trimToEmpty(from);
                            if (StringUtils.isNotEmpty(from) && string.contains(from))
                            {
                                final String to = StringUtils.trimToEmpty(mapping.getTo());
                                string = StringUtils.replace(string, from, to);
                            }
                        }
                    }
                }
            }
        }
        return string;
    }

    /**
     * Retrieves the <em>merged</em> string. The merging takes place when
     * the {@link NamespaceProperties#MERGE_MAPPINGS_URI}is found within the
     * <code>namespace</code> and the merge mappings are used to replace any
     * matching patterns in the given <code>inputStream</code>.
     * </p>
     *
     * @param inputStream the InputStream instance which is first converted
     *        to a String and then merged.
     * @param namespace This namespace is searched when attempting to find the
     *        {@link NamespaceProperties#MERGE_MAPPINGS_URI}.
     * @return the replaced String.
     */
    public String getMergedString(
        final InputStream inputStream,
        final String namespace)
    {
        try
        {
            StringWriter writer = new StringWriter();
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
            for (int ctr = inputReader.read(); ctr != -1; ctr = inputReader.read())
            {
                writer.write(ctr);
            }
            inputReader.close();

            final String string = writer.toString();
            writer.close();

            return this.getMergedString(string, namespace);
        }
        catch (final Exception exception)
        {
            throw new MergerException(exception);
        }
    }

    /**
     * Indicates whether or not the given <code>namespace</code>
     * requires a merge.
     * @param namespace the namespace to evaluate.
     * @return true/false
     */
    public boolean requiresMerge(final String namespace)
    {
        boolean requiresMerge = false;
        final Collection<Mappings> mergeMappings = this.getMergeMappings(namespace);
        for (final Mappings mappings : mergeMappings)
        {
            requiresMerge = !mappings.getMappings().isEmpty();
            if (requiresMerge)
            {
                break;
            }
        }
        return requiresMerge;
    }

    /**
     * Attempts to retrieve the Mappings instance for the given <code>mergeMappingsUri</code> belonging to the given
     * <code>namespace</code>.
     *
     * @param namespace the namespace to which the mappings belong.
     * @return the Mappings instance.
     */
    private Collection<Mappings> getMergeMappings(final String namespace)
    {
        final Collection<Mappings> mappings = new ArrayList<Mappings>();
        if (StringUtils.isNotBlank(namespace))
        {
            final Collection<Property> mergeMappingsUris =
                Namespaces.instance().getProperties(namespace, NamespaceProperties.MERGE_MAPPINGS_URI, false);
            if (mergeMappingsUris != null)
            {
                for (final Property mergeMappingsUri : mergeMappingsUris)
                {
                    String mergeMappingsUriValue = (mergeMappingsUri != null) ? mergeMappingsUri.getValue() : null;
                    if (StringUtils.isNotBlank(mergeMappingsUriValue))
                    {
                        Mappings mergeMappings = (Mappings)this.mergeMappingsCache.get(mergeMappingsUriValue);
                        if (mergeMappings == null)
                        {
                            try
                            {
                                mergeMappings = Mappings.getInstance(mergeMappingsUriValue);
                                this.mergeMappingsCache.put(mergeMappingsUriValue, mergeMappings);
                            }
                            catch (Exception exception)
                            {
                                if (ExceptionUtils.getRootCause(exception) instanceof FileNotFoundException)
                                {
                                    if (logger.isDebugEnabled())
                                    {
                                        logger.debug(exception);
                                    }
                                }
                                else
                                {
                                    throw new MergerException(exception);
                                }
                            }
                        }
                        if (mergeMappings != null)
                        {
                            mappings.add(mergeMappings);
                        }
                    }
                }
            }
        }
        return mappings;
    }
}