package org.andromda.core;

import java.net.URL;

import org.andromda.core.common.ModelPackages;

/**
 * Stores the processing information for each model 
 * that AndroMDA will process.
 * 
 * @author Chad Brandon
 */
public class Model
{
    /**
     * The URL of the model.
     */
    private URL url;

    /**
     * Stores the information about what packages should/shouldn't be processed.
     */
    private ModelPackages packages = new ModelPackages();

    /**
     * Whether or not to perform a last modified check on the model.
     */
    private boolean lastModifiedCheck;

    /**
     * The path to search for the model's <em>modules</em> (i.e. models that
     * are referenced as HREF <code>modules</code> from within this model)
     */
    private String[] moduleSearchPath;

    /**
     * Constructs a new instance of this Model
     * 
     * @param url the URL to the model to process.
     * @param lastModifiedCheck whether or not to perform a last modified check
     *        when processing the model. If <code>true</code> the model will
     *        be checked for a timestamp before processing occurs.
     * @param packages the packages to include/ignore when processing the model.
     * @param moduleSearchPath any array of path's to search for any HREF
     *        modules (profile, etc) referenced from within ths model.
     */
    public Model(
        URL url,
        ModelPackages packages,
        boolean lastModifiedCheck,
        String[] moduleSearchPath)
    {
        this.lastModifiedCheck = lastModifiedCheck;
        this.url = url;
        this.packages = packages;
        this.moduleSearchPath = moduleSearchPath;
    }

    /**
     * @return Returns the lastModifiedCheck.
     */
    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheck;
    }

    /**
     * @return Returns the packages.
     */
    public ModelPackages getPackages()
    {
        return packages;
    }

    /**
     * @return Returns the url.
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * Gets the path to search for the model's <em>modules</em> (i.e. models
     * that are referenced as HREF <code>modules</code> from within this
     * model). Will be null if the path isn't specified.
     * 
     * @return Returns the moduleSearchPath.
     */
    public String[] getModuleSearchPath()
    {
        return moduleSearchPath;
    }
}