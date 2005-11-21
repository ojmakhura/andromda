package org.andromda.andromdapp;

import java.util.ArrayList;
import java.util.List;

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
     * @return Returns the froms.
     */
    public List getFroms()
    {
        return froms;
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
