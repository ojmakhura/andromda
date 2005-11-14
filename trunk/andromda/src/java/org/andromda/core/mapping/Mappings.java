package org.andromda.core.mapping;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p> An object responsible for mapping multiple <code>from</code> values to
 * single <code>to</code>. The public constructor should NOT be used to
 * construct this instance. An instance of this object should be retrieved
 * through the method getInstance(java.net.URL).
 * </p>
 * <p> The mappings will change based upon the language, database, etc being
 * used. </p>
 *
 * @author Chad Brandon
 * @see org.andromda.core.common.XmlObjectFactory
 */
public class Mappings
{
    /**
     * Contains the set of Mapping objects keyed by the 'type' element defined
     * within the from type mapping XML file.
     */
    private Map mappings = new LinkedHashMap();

    /**
     * A static mapping containing all logical mappings currently available.
     */
    private static final Map logicalMappings = new LinkedHashMap();

    /**
     * Holds the resource path from which this Mappings object was loaded.
     */
    private URL resource;

    /**
     * Returns a new configured instance of this Mappings configured from the
     * mappings configuration URI string.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return Mappings the configured Mappings instance.
     */
    public static final Mappings getInstance(String mappingsUri)
    {
        mappingsUri = StringUtils.trimToEmpty(mappingsUri);
        ExceptionUtils.checkEmpty("mappingsUri", mappingsUri);
        try
        {
            Mappings mappings = (Mappings)logicalMappings.get(mappingsUri);
            if (mappings == null)
            {
                try
                {
                    mappings = getInstance(new URL(mappingsUri));
                }
                catch (final MalformedURLException exception)
                {
                    throw new MappingsException("The given URI --> '" + mappingsUri + "' is invalid", exception);
                }
            }
            return getInheritedMappings(mappings);
        }
        catch (final Throwable throwable)
        {
            throw new MappingsException(throwable);
        }
    }

    /**
     * Attempts to get any inherited mappings for the
     * given <code>mappings</code>.
     *
     * @param mappings the mappings instance for which to
     *        get the inherited mappings.
     * @return the Mappings populated with any inherited mappings
     *         or just the same mappings unchanged if the
     *         <code>mappings</code> doesn't extend anything.
     * @throws Exception if an exception occurs.
     */
    private static final Mappings getInheritedMappings(final Mappings mappings)
        throws Exception
    {
        return getInheritedMappings(mappings, false);
    }

    /**
     * Attempts to get any inherited mappings for the
     * given <code>mappings</code>.
     *
     * @param mappings the mappings instance for which to
     *        get the inherited mappings.
     * @param ignoreInheritanceFailure whether or not a failure retrieving the parent
     *        should be ignored (an exception will be thrown otherwise).
     * @return the Mappings populated with any inherited mappings
     *         or just the same mappings unchanged if the
     *         <code>mappings</code> doesn't extend anything.
     * @throws Exception if an exception occurs.
     */
    private static final Mappings getInheritedMappings(
        final Mappings mappings,
        final boolean ignoreInheritanceFailure)
        throws Exception
    {
        // if we have a parent then we add the child mappings to
        // the parent's (so we can override any duplicates in the
        // parent) and set the child mappings to the parent's
        if (mappings != null && StringUtils.isNotBlank(mappings.extendsUri))
        {
            Mappings parentMappings = (Mappings)logicalMappings.get(mappings.extendsUri);
            if (parentMappings == null)
            {
                try
                {
                    // since we didn't find the parent in the logical 
                    // mappings, try a relative path
                    parentMappings = getInstance(new File(mappings.getCompletePath(mappings.extendsUri)));
                }
                catch (final Exception exception)
                {
                    if (!ignoreInheritanceFailure)
                    {
                        throw exception;
                    }
                }
            }
            if (parentMappings != null)
            {
                final Map allMappings = new LinkedHashMap(parentMappings.mappings);
                allMappings.putAll(mappings.mappings);
                mappings.mappings = allMappings;
            }
        }
        return mappings;
    }

    /**
     * Returns a new configured instance of this Mappings configured from the
     * mappings configuration URI.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return Mappings the configured Mappings instance.
     */
    public static final Mappings getInstance(final URL mappingsUri)
    {
        return getInstance(mappingsUri, false);
    }

    /**
     * Returns a new configured instance of this Mappings configured from the
     * mappings configuration URI.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @param ignoreInheritanceFailure a flag indicating whether or not failures while attempting
     *        to retrieve the mapping's inheritance should be ignored.
     * @return Mappings the configured Mappings instance.
     */
    private static final Mappings getInstance(
        final URL mappingsUri,
        final boolean ignoreInheritanceFailure)
    {
        ExceptionUtils.checkNull("mappingsUri", mappingsUri);
        try
        {
            final Mappings mappings = (Mappings)XmlObjectFactory.getInstance(Mappings.class).getObject(mappingsUri);
            mappings.resource = mappingsUri;
            return getInheritedMappings(mappings, ignoreInheritanceFailure);
        }
        catch (final Throwable throwable)
        {
            throw new MappingsException(throwable);
        }
    }

    /**
     * Returns a new configured instance of this Mappings configured from the
     * mappingsFile.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return Mappings the configured Mappings instance.
     */
    private static final Mappings getInstance(final File mappingsFile)
        throws Exception
    {
        final Mappings mappings =
            (Mappings)XmlObjectFactory.getInstance(Mappings.class).getObject(new FileReader(mappingsFile));
        mappings.resource = mappingsFile.toURL();
        return mappings;
    }

    /**
     * This initializes all logical mappings that
     * are contained with global Mapping set.  This
     * <strong>MUST</strong> be called after all logical
     * mappings have been added through {@link #addLogicalMappings(Mappings)}
     * otherwise inheritance between logical mappings will not work correctly.
     */
    public static void initializeLogicalMappings()
    {
        final Map initialized = new LinkedHashMap();
        for (final Iterator nameIterator = logicalMappings.keySet().iterator(); nameIterator.hasNext();)
        {
            final String name = (String)nameIterator.next();
            final Mappings mappings = (Mappings)logicalMappings.get(name);
            initialized.put(
                name,
                getInstance(mappings.getResource()));
        }
        logicalMappings.putAll(initialized);
    }

    /**
     * Holds the name of this mapping. This corresponds usually to some language
     * (i.e. Java, or a database such as Oracle, Sql Server, etc).
     */
    private String name = null;

    /**
     * Returns the name name (this is the name for which the type mappings are
     * for).
     *
     * @return String the name name
     */
    public String getName()
    {
        final String methodName = "Mappings.getName";
        if (StringUtils.isEmpty(this.name))
        {
            throw new MappingsException(methodName + " - name can not be null or empty");
        }
        return name;
    }

    /**
     * Sets the name name.
     *
     * @param name
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Stores the URI that this mappings extends.
     */
    private String extendsUri;

    /**
     * Sets the name of the mappings which this
     * instance extends.
     *
     * @param extendsUri the URI of the mapping which
     *        this one extends.
     */
    public void setExtendsUri(final String extendsUri)
    {
        this.extendsUri = extendsUri;
    }

    /**
     * Adds a Mapping object to the set of current mappings.
     *
     * @param mapping the Mapping instance.
     */
    public void addMapping(final Mapping mapping)
    {
        ExceptionUtils.checkNull("mapping", mapping);
        final Collection fromTypes = mapping.getFroms();
        ExceptionUtils.checkNull("mapping.fromTypes", fromTypes);
        for (final Iterator typeIterator = fromTypes.iterator(); typeIterator.hasNext();)
        {
            mapping.setMappings(this);
            this.mappings.put(
                typeIterator.next(),
                mapping);
        }
    }

    /**
     * Adds the <code>mappings</code> instance to this Mappings instance
     * overriding any mappings with duplicate names.
     *
     * @param mappings the Mappings instance to add this instance.
     */
    public void addMappings(final Mappings mappings)
    {
        if (mappings != null && mappings.mappings != null)
        {
            this.mappings.putAll(mappings.mappings);
        }
    }

    /**
     * Returns the <code>to</code> mapping from a given <code>from</code>
     * mapping.
     *
     * @param from the <code>from</code> mapping, this is the type/identifier
     *        that is in the model.
     * @return String to the <code>to</code> mapping (this is the mapping that
     *         can be retrieved if a corresponding 'from' is found.
     */
    public String getTo(String from)
    {
        from = StringUtils.deleteWhitespace(StringUtils.trimToEmpty(from));
        final String initialFrom = from;
        String to = null;

        // first we check to see if there's an array
        // type mapping directly defined in the mappings
        final Mapping mapping = this.getMapping(from);
        if (mapping != null)
        {
            to = mapping.getTo();
        }
        if (to == null)
        {
            to = initialFrom;
        }
        return StringUtils.trimToEmpty(to);
    }

    /**
     * Adds a mapping to the globally available mappings, these are used by this
     * class to instatiate mappings from logical names as opposed to physical
     * names.
     *
     * @param mappings the Mappings to add to the globally available Mapping
     *        instances.
     */
    public static void addLogicalMappings(final URL mappingsUri)
    {
        final Mappings mappings = Mappings.getInstance(mappingsUri, true);
        logicalMappings.put(
            mappings.getName(),
            mappings);
    }

    /**
     * Returns true if the mapping contains the <code>from</code> value
     *
     * @param from the value of the from mapping.
     * @return true if it contains <code>from</code>, false otherwise.
     */
    public boolean containsFrom(final String from)
    {
        return this.getMapping(from) != null;
    }

    /**
     * Returns the resource URI from which this Mappings object was loaded.
     *
     * @return URL of the resource.
     */
    public URL getResource()
    {
        return this.resource;
    }

    /**
     * Gets all Mapping instances for for this Mappings instance.
     *
     * @return a collection containing <strong>all </strong> Mapping instances.
     */
    public Collection getMappings()
    {
        return this.mappings.values();
    }

    /**
     * Gets the mapping having the given <code>from</code>.
     *
     * @param from the <code>from</code> mapping.
     * @return the Mapping instance (or null if it doesn't exist).
     */
    public Mapping getMapping(final String from)
    {
        return (Mapping)this.mappings.get(StringUtils.trimToEmpty(from));
    }

    /**
     * Caches the complete path.
     */
    private final Map completePaths = new LinkedHashMap();

    /**
     * Constructs the complete path from the given <code>relativePath</code>
     * and the resource of the parent {@link Mappings#getResource()} as the root
     * of the path.
     *
     * @return the complete path.
     */
    final String getCompletePath(final String relativePath)
    {
        String completePath = (String)this.completePaths.get(relativePath);
        if (completePath == null)
        {
            final StringBuffer path = new StringBuffer();
            if (this.mappings != null)
            {
                final URL resource = this.getResource();
                if (resource != null)
                {
                    String rootPath = resource.getFile().replace('\\', '/');
                    rootPath = rootPath.substring(0, rootPath.lastIndexOf('/') + 1);
                    path.append(rootPath);
                }
            }
            if (relativePath != null)
            {
                path.append(StringUtils.trimToEmpty(relativePath));
            }
            completePath = path.toString();
            this.completePaths.put(relativePath, completePath);
        }
        return completePath;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}