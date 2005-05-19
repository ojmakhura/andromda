package org.andromda.core;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.ModelPackages;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Stores the processing information for each model that AndroMDA will process.
 *
 * @author Chad Brandon
 */
public class Model
{
    /**
     * Constructs a new instance of this Model
     *
     * @param url the URL to the model to process.
     * @param lastModifiedCheck whether or not to perform a last modified check when processing the model. If
     *                          <code>true</code> the model will be checked for a timestamp before processing occurs.
     * @param packages the packages to include/ignore when processing the model.
     * @param moduleSearchPath any array of path's to search for any HREF modules (profile, etc) referenced from within
     *                         ths model.
     */
    public Model(final URL url, final ModelPackages packages, final boolean lastModifiedCheck, final String[] moduleSearchPath)
    {
        final String constructorName = "Model.Model";
        ExceptionUtils.checkNull(constructorName, "url", url);
        this.lastModifiedCheck = lastModifiedCheck;
        this.url = url;
        try
        {
            // Get around the fact the URL won't be released until the JVM
            // has been terminated, when using the 'jar' url protocol.
            url.openConnection().setDefaultUseCaches(false);
        }
        catch (IOException ex)
        {
            // ignore the exception
        }
        this.packages = packages;
        this.moduleSearchPath = moduleSearchPath;
    }

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

    private ModelPackages packages = new ModelPackages();

    /**
     * Stores the information about what packages should/shouldn't be processed.
     *
     * @return Returns the packages.
     */
    public ModelPackages getPackages()
    {
        return packages;
    }

    private URL url;

    /**
     * The URL of the model.
     *
     * @return Returns the url.
     */
    public URL getUrl()
    {
        return url;
    }

    private String[] moduleSearchPath;

    /**
     * Gets the path to search for the model's <em>modules</em> (i.e. models that are referenced as HREF
     * <code>modules</code> from within this model). Will be null if the path isn't specified.
     *
     * @return Returns the moduleSearchPath.
     */
    public String[] getModuleSearchPath()
    {
        return moduleSearchPath;
    }

    /**
     * Gets the time as a <code>long</code> when this model was last modified. If it can not be determined
     * <code>0</code> is returned.
     *
     * @return the time this model was last modified
     */
    public long getLastModified()
    {
        long lastModified;
        try
        {
            URLConnection urlConnection = url.openConnection();
            lastModified = urlConnection.getLastModified();
            urlConnection = null;
        }
        catch (Exception ex)
        {
            lastModified = 0;
        }
        return lastModified;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String toString = super.toString();
        if (this.url != null)
        {
            toString = this.url.toString();
        }
        return toString;
    }
}