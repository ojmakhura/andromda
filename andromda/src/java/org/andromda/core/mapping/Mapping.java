package org.andromda.core.mapping;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;

/**
 * A single child mapping instance belonging to a Mappings instance. It doesn't make sense to instantiate this class by
 * itself.
 *
 * @author Chad Brandon
 * @see org.andromda.core.mapping.Mappings
 */
public class Mapping
{
    /**
     * Stores the from elements.
     */
    private final Collection froms = new HashSet();

    /**
     * Stores the to mapping.
     */
    private String to;

    /**
     * Adds the <code>from</code> type to the mapping.
     *
     * @param from the type that we are mapping from.
     */
    public void addFrom(String from)
    {
        final String methodName = "Mappings.addFrom";
        ExceptionUtils.checkNull(methodName, "from", from);
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
     * Stores whether or not the value of the to element
     * should be extracted from the path.
     */
    private boolean path = false;
    
    /**
     * Sets whether or not the to value is a path.  If so, then an attempt
     * to extract the value for the <code>to</code> element is made
     * by loading the contents of the path.
     * 
     * @param path true/false
     */
    public void setPath(boolean path)
    {
        this.path = path;
    }

    /**
     * Returns the to type for this mapping.
     *
     * @return String the to type
     */
    public String getTo()
    {
        if (this.path)
        {
            try
            {
                this.to = ResourceUtils.getContents(new FileReader(this.getCompletePath(this.to)));
            }
            catch (FileNotFoundException exception)
            {
                throw new MappingsException(exception);
            }
        }
        return this.to;
    }

    /**
     * Sets the type for this mapping.
     *
     * @param to
     */
    public void setTo(String to)
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
    void setMappings(Mappings mappings)
    {
        this.mappings = mappings;    
    }
    
    /**
     * Caches the complete path.
     */
    private String completePath;
    
    /**
     * Constructs the complete path from the given <code>relativePath</code>
     * and the resource of the parent {@link Mappings#getResource()} as the root 
     * of the path.
     * 
     * @return the complete path.
     */
    private String getCompletePath(final String relativePath)
    {
        if (this.completePath == null)
        {
            final StringBuffer path = new StringBuffer();
            if (this.mappings != null)
            {
                URL resource = mappings.getResource();
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
            this.completePath = path.toString();
        }
        return this.completePath;
    }
}