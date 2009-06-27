package org.andromda.andromdapp;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.ResourceUtils;


/**
 * Represents the configuration of an AndroMDAppType.
 *
 * @author Chad Brandon
 * @see AndroMDAppType
 */
public class Configuration
{
    /**
     * Stores any properties defined in this configuration.
     */
    private final Map properties = new LinkedHashMap();

    /**
     * Adds a property with the name and value to the current properties
     * map.
     *
     * @param name the name of the property to add.
     * @param value the value of the property.
     */
    public void addProperty(
        final String name,
        final String value)
    {
        this.properties.put(
            name,
            value);
    }

    /**
     * Stores any locations to property files.
     */
    private final List locations = new ArrayList();

    /**
     * Adds a location to this configuration.
     *
     * @param location the path of the location.
     */
    public void addLocation(final String location)
    {
        this.locations.add(location);
    }

    /**
     * The patterns to use for the locations
     */
    private static final String[] LOCATION_PATTERNS = new String[] {"**/*.properties"};

    /**
     * Retrieves all properties including all those found in the given locations.
     *
     * @return the map containing all properties
     */
    public Map getAllProperties()
    {
        final Map allProperties = new LinkedHashMap();
        for (final Iterator iterator = this.locations.iterator(); iterator.hasNext();)
        {
            final String location = (String)iterator.next();
            final List resources =
                ResourceUtils.getDirectoryContents(
                    ResourceUtils.toURL(location),
                    true,
                    LOCATION_PATTERNS);
            if (resources != null)
            {
                for (final Iterator resourceIterator = resources.iterator(); resourceIterator.hasNext();)
                {
                    final String path = (String)resourceIterator.next();
                    final URL resource = ResourceUtils.toURL(path);
                    final Properties properties = new Properties();
                    InputStream stream = null;
                    try
                    {
                        stream = resource.openStream();
                        properties.load(stream);
                        allProperties.putAll(properties);
                    }
                    catch (final Exception exception)
                    {
                        // - ignore
                    }
                    finally
                    {
                        try
                        {
                            stream.close();
                            stream = null;
                        }
                        catch (IOException exception)
                        {
                            // - ignore 
                        }
                    }
                }
            }
        }
        allProperties.putAll(this.properties);
        return allProperties;
    }
    
    /**
     * Stores whether or not the application should be overwritten if it previously existed.
     */
    private boolean ovewrite;
    
    /**
     * Whether or not the application should be overwritten if it already exits.
     * 
     * @return true/false
     */
    public boolean isOverwrite()
    {
        return this.ovewrite;
    }
    /**
     * Sets whether or not the application should be overwritten if it previously existed.
     * 
     * @param overwrite true/false
     */
    public void setOverwrite(final boolean overwrite)
    {
        this.ovewrite = overwrite;
    }
}