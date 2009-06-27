package org.andromda.core.mapping;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;


/**
 * A single child mapping instance belonging to a Mappings instance. It doesn't make sense to instantiate this class by
 * itself.
 *
 * @author Chad Brandon
 * @author Wouter Zoons
 * @see org.andromda.core.mapping.Mappings
 */
public class Mapping
{
    /**
     * Stores the from elements.
     */
    private final Collection froms = new LinkedHashSet();

    /**
     * Adds the <code>from</code> type to the mapping.
     *
     * @param from the type that we are mapping from.
     */
    public void addFrom(final String from)
    {
        ExceptionUtils.checkNull("from", from);
        froms.add(from);
    }

    /**
     * Return the Collection of froms.
     *
     * @return Collection
     */
    public Collection getFroms()
    {
        return froms;
    }

    /**
     * Returns the to type for this mapping.
     *
     * @return String the to type
     */
    public String getTo()
    {
        final StringBuffer to = new StringBuffer();
        if (StringUtils.isNotBlank(this.to))
        {
            to.append(this.to);
        }
        if (!this.paths.isEmpty())
        {
            try
            {
                for (final Iterator iterator = this.paths.iterator(); iterator.hasNext();)
                {
                    to.append(
                        ResourceUtils.getContents(
                            new FileReader(this.mappings.getCompletePath((String)iterator.next()))));
                }
            }
            catch (final FileNotFoundException exception)
            {
                throw new MappingsException(exception);
            }
        }
        return to.toString();
    }

    /**
     * Stores any paths used by this mapping.
     */
    private final List paths = new ArrayList();

    /**
     * Adds the path to the listof paths.
     * @param path
     */
    public void addPath(final String path)
    {
        this.paths.add(path);
    }

    /**
     * Stores the to mapping.
     */
    private String to;

    /**
     * Sets the type for this mapping.
     *
     * @param to the value to which the from
     *        values are mapped.
     */
    public void setTo(final String to)
    {
        this.to = to;
    }

    /**
     * The parent of this instance.
     */
    private Mappings mappings;

    /**
     * Sets the mappings to which this Mapping instance
     * belongs.
     *
     * @param mappings the owning mappings.
     */
    final void setMappings(final Mappings mappings)
    {
        this.mappings = mappings;
    }

    /**
     * Returns a String representation of this mapping in the form of <code>from1, from2, from3 --> to</code>.
     *
     * @return a String representing the mapping instance
     */
    public String toString()
    {
        final StringBuffer buffer = new StringBuffer(512); // 512 should be enough for resizing not to occur

        for (Iterator fromIterator = this.froms.iterator(); fromIterator.hasNext();)
        {
            final String from = (String)fromIterator.next();
            buffer.append(from);

            if (fromIterator.hasNext())
            {
                buffer.append(", ");
            }
        }

        buffer.append(" --> ").append(this.to);

        return buffer.toString();
    }
}