package org.andromda.maven;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.jelly.JellyContext;
import org.apache.maven.jelly.MavenJellyContext;

/**
 * Contains some utilities for use within
 * the Maven plugins that run AndroMDA.
 * 
 * @author Chad Brandon
 */
public class AndroMDAMavenUtils
{
    /**
     * The maven jelly context.
     */
    private MavenJellyContext context;

    /**
     * Sets the maven jelly context for this instance.
     */
    public void setContext(MavenJellyContext context)
    {
        this.context = context;
    }

    /**
     * Gets all property names.
     *
     * @return the property names.
     */
    public String[] getPropertyNames()
    {
        final Collection properties = new ArrayList();
        for (JellyContext context = this.context; context != null; context = context.getParent())
        {
            CollectionUtils.addAll(
                properties,
                context.getVariableNames());
        }
        return (String[])properties.toArray(new String[0]);
    }
    
    /**
     * The property reference pattern.
     */
    private static final String PROPERTY_REFERENCE = "\\$\\{.*\\}";
    
    /**
     * Removes any ${some.property} type references from the string
     * and returns the modifed string.
     * @param string the string from which to remove the property 
     *        references
     *        
     * @return the modified string.
     */
    public static String removePropertyReferences(String string)
    {
        if (string != null)
        {
            string = string.replaceAll(PROPERTY_REFERENCE, "");
        }
        return string;
    }
}