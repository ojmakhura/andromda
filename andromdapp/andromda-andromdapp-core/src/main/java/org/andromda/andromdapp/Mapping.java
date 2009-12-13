package org.andromda.andromdapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a mapping within an AndroMDApp descriptor.
 * 
 * @author Chad Brandon
 */
public class Mapping
{
    /**
     * Stores the mappings from which the output is mapped.
     */
    private final List froms = new ArrayList();
   
    /**
     * Adds a from to this mapping's list of from mappings.
     * 
     * @param from the from mapping.
     */
    public void addFrom(final String from)
    {
        this.froms.add(from);
    }
    
    /**
     * Attempts to match the given <code>path</code> on one of the 
     * the from values, if a match can be made, the new path value is returned,
     * otherwise null is returned.
     * 
     * @return true/false
     */
    public String getMatch(final String path)
    {
        String match = null;
        for (final Iterator iterator = this.froms.iterator(); iterator.hasNext();)
        {
            final String from = (String)iterator.next();
            if (path.contains(from))
            {
                match = StringUtils.replace(path, from, to);
            }
        }
        return match;
    }
    
    
    private String to;

    /**
     * @return Returns the to.
     */
    public String getTo()
    {
        return to;
    }

    /**
     * @param to The to to set.
     */
    public void setTo(String to)
    {
        this.to = to;
    }
}
