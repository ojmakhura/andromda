package org.andromda.core.configuration;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ResourceUtils;


/**
 * Stores the processing information for each model that AndroMDA will process.
 *
 * @author Chad Brandon
 */
public class Model
    implements Serializable
{
    /**
     * Stores whether or not a last modified check
     * should be performed.
     */
    private boolean lastModifiedCheck = false;

    /**
     * Whether or not to perform a last modified check on the model.
     *
     * @return Returns the lastModifiedCheck.
     */
    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheck;
    }

    /**
     * Sets whether or not to perform a last modified check when processing the model. If
     * <code>true</code> the model will be checked for a timestamp before processing occurs.
     *
     * @param lastModifiedCheck true/false
     */
    public void setLastModifiedCheck(final boolean lastModifiedCheck)
    {
        this.lastModifiedCheck = lastModifiedCheck;
    }

    /**
     * Stores the informationj about what packages should and shouldn't
     * be processed.
     */
    private ModelPackages packages = new ModelPackages();

    /**
     * Sets the processAll flag on the interal model packages instance
     * of this model.
     *
     * @param processAllPackages
     */
    public void setProcessAllPackages(final boolean processAllPackages)
    {
        packages.setProcessAll(processAllPackages);
    }

    /**
     * Stores the information about what packages should/shouldn't be processed.
     *
     * @return Returns the packages.
     */
    public ModelPackages getPackages()
    {
        return this.packages;
    }

    /**
     * Sets the model packages for this model.  This indicates what
     * packages should and should not be processed from this model.
     *
     * @param packages the packages to process.
     */
    public void setPackages(final ModelPackages packages)
    {
        this.packages = packages;
    }

    /**
     * The URL to the model.
     */
    private URL uri;

    /**
     * The URL of the model.
     *
     * @return Returns the uri.
     */
    public URL getUri()
    {
        return uri;
    }

    /**
     * Sets the URL to the actual model file.
     * @param uri the model URL.
     */
    public void setUri(final String uri)
        throws Exception
    {
        try
        {
            this.uri = new URL(uri.replace('\\', '/'));
        }
        catch (final Throwable throwable)
        {
            throw new ConfigurationException(throwable);
        }
        try
        {
            // Get around the fact the URL won't be released until the JVM
            // has been terminated, when using the 'jar' uri protocol.
            this.uri.openConnection().setDefaultUseCaches(false);
        }
        catch (final IOException exception)
        {
            // ignore the exception
        }
    }

    /**
     * The locations in which to search for module.
     */
    private final Collection moduleSearchLocations = new ArrayList();

    /**
     * Adds a module search location (these are the locations
     * in which a search for module is performed).
     *
     * @param location a location path.
     * @see #addModuleSearchLocation(String)
     */
    public void addModuleSearchLocation(final Location location)
    {
        this.moduleSearchLocations.add(location);
    }

    /**
     * Adds a module search location path (a location
     * without a pattern defined).
     *
     * @param path a location path.
     * @see #addModuleSearchLocation(Location)
     */
    public void addModuleSearchLocation(final String path)
    {
        if (path != null)
        {
            final Location location = new Location();
            location.setPath(path);
            this.moduleSearchLocations.add(location);
        }
    }

    /**
     * Gets the module searach locations for this model instance.
     *
     * @return the module search locations.
     * @see #getModuleSearchLocationPaths()
     */
    public Location[] getModuleSearchLocations()
    {
        return (Location[])this.moduleSearchLocations.toArray(new Location[0]);
    }

    /**
     * Stores the path for each module search location in this configuration.
     */
    private String[] moduleSearchLocationPaths = null;

    /**
     * Gets all module search location paths for this model isntance.
     *
     * @return the module search location paths.
     * @see #getModuleSearchLocations()
     */
    public String[] getModuleSearchLocationPaths()
    {
        if (this.moduleSearchLocationPaths == null)
        {
            final Collection paths = new ArrayList();
            for (final Iterator iterator = this.moduleSearchLocations.iterator(); iterator.hasNext();)
            {
                paths.add(((Location)iterator.next()).getPath());
            }
            this.moduleSearchLocationPaths = (String[])paths.toArray(new String[0]);
        }
        return this.moduleSearchLocationPaths;
    }

    /**
     * Stores all files including all files found within the module search locations
     * as well as a file for the {@link #uri}.
     */
    private File[] moduleSearchLocationFiles = null;

    /**
     * Gets the accumulation of all files found when combining the contents
     * of all module search location paths and their patterns by which they
     * are filtered as well as the model URI.
     *
     * @return all module search location files.
     */
    private final File[] getModuleSearchLocationFiles()
    {
        if (this.moduleSearchLocationFiles == null)
        {
            final Collection allFiles = new ArrayList();
            final Location[] locations = this.getModuleSearchLocations();
            for (int ctr = 0; ctr < locations.length; ctr++)
            {
                File[] files = locations[ctr].getFiles();
                for (int fileCtr = 0; fileCtr < files.length; fileCtr++)
                {
                    allFiles.add(files[fileCtr]);
                }
            }
            this.moduleSearchLocationFiles = (File[])allFiles.toArray(new File[0]);
        }
        return this.moduleSearchLocationFiles;
    }

    /**
     * Gets the time as a <code>long</code> when this model was last modified. If it can not be determined
     * <code>0</code> is returned.
     *
     * @return the time this model was last modified
     */
    public long getLastModified()
    {
        return ResourceUtils.getLastModifiedTime(this.uri);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String toString = super.toString();
        if (this.uri != null)
        {
            toString = this.uri.toString();
        }
        return toString;
    }

    /**
     * Stores the last modified times for each model at the time
     * {@link #isChanged()} is called.
     */
    private static final Map modelModifiedTimes = new HashMap();

    /**
     * Creates the key used to retrieve the nodel last modified
     * time.
     *
     * @param uri the model uri.
     * @return the unique key
     */
    private final Object getModifiedKey(final String uri)
    {
        return new File(uri);
    }

    /**
     * Indicates whether or not the given <code>model</code>
     * has changed since the previous call to this method.
     *
     * @return true/false
     */
    public boolean isChanged()
    {
        boolean changed = this.getUri() != null;
        if (changed)
        {
            final Object modelKey = this.getModifiedKey(this.getUri().getFile());
            Map lastModifiedTimes = (Map)modelModifiedTimes.get(modelKey);

            // - load up the last modified times (from the model and all its modules)
            //   if they haven't been loaded yet
            if (lastModifiedTimes != null)
            {
                final long modelLastModified = ((Long)lastModifiedTimes.get(modelKey)).longValue();
                changed = this.getLastModified() > modelLastModified;
                if (!changed)
                {
                    // - check to see if any of the modules have changed if the model hasn't changed
                    final File[] files = this.getModuleSearchLocationFiles();
                    for (int ctr = 0; ctr < files.length; ctr++)
                    {
                        final File file = files[ctr];
                        final Long lastModified = (Long)lastModifiedTimes.get(file);
                        if (lastModified != null)
                        {
                            // - when we find the first modified module, break out
                            if (file.lastModified() > lastModified.longValue())
                            {
                                changed = true;
                                break;
                            }
                        }
                    }
                }
            }

            // - if our model (or modules) have changed re-load the last modified times
            if (changed)
            {
                this.loadLastModifiedTimes();
            }
        }
        return changed;
    }

    /**
     * Loads (or re-loads) the last modified times from the
     * {@link #uri} and the modules found on the module search path.
     */
    private final void loadLastModifiedTimes()
    {
        final Object modelKey = this.getModifiedKey(this.getUri().getFile());
        Map lastModifiedTimes = (Map)modelModifiedTimes.get(modelKey);
        if (lastModifiedTimes == null)
        {
            lastModifiedTimes = new HashMap();
        }
        else
        {
            lastModifiedTimes.clear();
        }
        final File[] files = this.getModuleSearchLocationFiles();
        for (int ctr = 0; ctr < files.length; ctr++)
        {
            final File file = files[ctr];
            lastModifiedTimes.put(
                file,
                new Long(file.lastModified()));
        }

        // - add the model key last so it overwrites any invalid ones
        //   we might have picked up from adding the module search location files.
        lastModifiedTimes.put(
            modelKey,
            new Long(this.getLastModified()));
        modelModifiedTimes.put(modelKey, lastModifiedTimes);
    }

    /**
     * Clears out the current last modified times.
     */
    static final void clearLastModifiedTimes()
    {
        modelModifiedTimes.clear();
    }
}