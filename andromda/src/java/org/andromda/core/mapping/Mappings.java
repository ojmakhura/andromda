package org.andromda.core.mapping;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p/>
 * An object responsible for mapping multiple <code>from</code> values to single <code>to</code>. The public constructor
 * should NOT be used to construct this instance. An instance of this object should be retrieved through the method
 * getInstance(java.net.URL). </p>
 * <p/>
 * The mappings will change based upon the language, database, etc being used.
 * <p/>
 *
 * @author Chad Brandon
 * @see org.andromda.core.common.XmlObjectFactory
 */
public class Mappings
{
    /**
     * Holds the name of this mapping. This corresponds usually to some language (i.e. Java, or a database such as
     * Oracle, Sql Server, etc).
     */
    private String name = null;

    /**
     * Contains the set of Mapping objects keyed by the 'type' element defined within the from type mapping XML file.
     */
    private Map mappings = new HashMap();

    /**
     * A static mapping containing all logical mappings currently available.
     */
    private static Map logicalMappings = new HashMap();

    /**
     * Holds the resource path from which this Mappings object was loaded.
     */
    private URL resource;

    /**
     * Returns a new configured instance of this Mappings configured from the mappings configuration URI string.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return Mappings the configured Mappings instance.
     * @throws MalformedURLException when the mappingsUri is invalid (not a valid URL).
     */
    public static Mappings getInstance(String mappingsUri)
    {
        final String methodName = "Mappings.getInstance";
        mappingsUri = StringUtils.trimToEmpty(mappingsUri);
        ExceptionUtils.checkEmpty(methodName, "mappingsUri", mappingsUri);
        try
        {
            Mappings mappings = (Mappings)logicalMappings.get(mappingsUri);
            if (mappings == null)
            {
                mappings = getInstance(new URL(mappingsUri));
            }
            return mappings;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            throw new MappingsException(errMsg, th);
        }
    }

    /**
     * Returns a new configured instance of this Mappings configured from the mappings configuration URI.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return Mappings the configured Mappings instance.
     */
    public static Mappings getInstance(URL mappingsUri)
    {
        final String methodName = "Mappings.getInstance";
        ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
        Mappings mappings = (Mappings)XmlObjectFactory.getInstance(Mappings.class).getObject(mappingsUri);
        mappings.resource = mappingsUri;
        return mappings;
    }

    /**
     * Returns the name name (this is the name for which the type mappings are for).
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
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Adds a Mapping object to the set of current mappings.
     *
     * @param mapping the Mapping instance.
     */
    public void addMapping(Mapping mapping)
    {
        final String methodName = "Mappings.addMapping";
        ExceptionUtils.checkNull(methodName, "mapping", mapping);

        Collection fromTypes = mapping.getFroms();
        ExceptionUtils.checkNull(methodName, "mapping.fromTypes", fromTypes);
        for (final Iterator typeIterator = fromTypes.iterator(); typeIterator.hasNext();)
        {
            mapping.setMappings(this);
            this.mappings.put(typeIterator.next(), mapping);
        }
    }

    /**
     * Adds the <code>mappings</code> instance to this Mappings instance overriding any mappings with duplicate names.
     *
     * @param mappings the Mappings instance to add this instance.
     */
    public void addMappings(Mappings mappings)
    {
        if (mappings != null && mappings.mappings != null)
        {
            this.mappings.putAll(mappings.mappings);
        }
    }

    /**
     * Returns the <code>to</code> mapping from a given <code>from</code> mapping.
     *
     * @param from the <code>from</code> mapping, this is the type/identifier that is in the model.
     * @return String to the <code>to</code> mapping (this is the mapping that can be retrieved if a corresponding
     *         'from' is found.
     */
    public String getTo(String from)
    {
        from = StringUtils.deleteWhitespace(StringUtils.trimToEmpty(from));
        final String initialFrom = from;
        String to = null;
        // first we check to see if there's an array
        // type mapping directly defined in the mappings
        Mapping mapping = this.getMapping(from);
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
     * Adds a mapping to the globally available mappings, these are used by this class to instatiate mappings from
     * logical names as opposed to physical names.
     *
     * @param mappings the Mappings to add to the globally available Mapping instances.
     */
    public static void addLogicalMappings(Mappings mappings)
    {
        logicalMappings.put(mappings.getName(), mappings);
    }

    /**
     * Returns true if the mapping contains the <code>from</code> value
     *
     * @param from the value of the from mapping.
     * @return true if it contains <code>from</code>, false otherwise.
     */
    public boolean containsFrom(String from)
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
        Collection mappingsSet = new ArrayList(this.mappings.entrySet());
        CollectionUtils.transform(mappingsSet, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((Map.Entry)object).getValue();
            }
        });
        return mappingsSet;
    }

    /**
     * Gets the mapping having the given <code>from</code>.
     *
     * @param from the <code>from</code> mapping.
     * @return the Mapping instance (or null if it doesn't exist).
     */
    public Mapping getMapping(String from)
    {
        return (Mapping)this.mappings.get(StringUtils.trimToEmpty(from));
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}