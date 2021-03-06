package org.andromda.maven.plugin;

import java.io.File;
import java.net.URL;
import org.andromda.core.AndroMDA;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Repository;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * A Maven2 plugin to run AndroMDA.
 *
 * @author Chad Brandon
 * @goal run
 * @phase generate-sources
 * @requiresDependencyResolution runtime
 */
public class AndroMDAMojo
    extends AbstractAndroMDAMojo
{
    /**
     * Whether or not a last modified check should be performed before running AndroMDA again.
     * Checks files in buildSourceDirectory against configurationUri and referenced model dates.
     *
     * @parameter expression="${lastModifiedCheck}"
     *   default-value=true
     */
    private boolean lastModifiedCheck = true;

    /**
     * Whether or not processing should be skipped (this is if you just want to force AndroMDA
     * not to run on your model).
     *
     * @parameter expression="${andromda.run.skip}"
     *   default-value=false
     */
    private boolean skipProcessing = false;

    /**
     * The directory to which the build source is located (any generated source).
     *
     * @parameter expression="${project.build.directory}/src/main/java"
     */
    private File buildSourceDirectory;

    /**
     * The directory where the model generation output history is located
     * (Modelname file containing a list of files generated by that model).
     *
     * @parameter expression="${project.build.directory}/history"
     */
    private File modelOutputHistory;

    /**
     * @param lastModifiedCheck the lastModifiedCheck to set
     */
    public void setLastModifiedCheck(boolean lastModifiedCheck)
    {
        this.lastModifiedCheck = lastModifiedCheck;
    }

    /**
     * @param buildSourceDirectory the buildSourceDirectory to set
     */
    public void setBuildSourceDirectory(File buildSourceDirectory)
    {
        this.buildSourceDirectory = buildSourceDirectory;
    }

    /**
     * @param modelOutputHistory the modelOutputHistory to set
     */
    public void setModelOutputHistory(File modelOutputHistory)
    {
        this.modelOutputHistory = modelOutputHistory;
    }

    /**
     * @see org.andromda.maven.plugin.AbstractAndroMDAMojo#execute(org.andromda.core.configuration.Configuration)
     */
    public void execute(final Configuration configuration)
        throws MojoExecutionException
    {
        if (getLog().isDebugEnabled())
        {
            getLog().debug("lastModifiedCheck="+this.lastModifiedCheck + " skipProcessing="+this.skipProcessing 
                    + " modelOutputHistory="+this.modelOutputHistory + " allowMultipleRuns="+this.allowMultipleRuns);            
        }
        if (!this.skipProcessing)
        {
            boolean execute = true;
            if (this.lastModifiedCheck)
            {
                long date = this.getLastModelConfigDate(configuration);
                execute = ResourceUtils.modifiedAfter(date,
                        this.buildSourceDirectory);
                if (getLog().isDebugEnabled())
                {
                    getLog().debug("this.lastModifiedCheck="+this.lastModifiedCheck + " execute="+execute + " date="+date);            
                }
            }
            if (execute)
            {
                final AndroMDA andromda = AndroMDA.newInstance();
                final String historyPath = this.modelOutputHistory!=null ? this.modelOutputHistory.getAbsolutePath() : null;
                andromda.run(configuration, lastModifiedCheck,  historyPath);
                andromda.shutdown();
            }
            else
            {
                this.getLog().info("Files are up-to-date, skipping AndroMDA execution");
            }
        }
        if (this.buildSourceDirectory != null)
        {
            this.getProject().addCompileSourceRoot(this.buildSourceDirectory.getPath());
        }
    }

    /**
     * Find the latest date from the configurationUri (andromda.xml) and all referenced models.
     * @param configuration The model code generation configuration (model list)
     */
    private long getLastModelConfigDate(final Configuration configuration)
    {
        final URL configurationUri = ResourceUtils.toURL(this.configurationUri);
        long date = ResourceUtils.getLastModifiedTime(configurationUri);
        final Repository[] repositories = configuration.getRepositories();
        int repositoryCount = repositories.length;
        for (int ctr = 0; ctr < repositoryCount; ctr++)
        {
            final Repository repository = repositories[ctr];
            if (repository != null)
            {
                final Model[] models = repository.getModels();
                final int modelCount = models.length;
                for (int ctr2 = 0; ctr2 < modelCount; ctr2++)
                {
                    final Model model = models[ctr2];
                    long newDate = model.getLastModified();
                    if (newDate > date)
                    {
                        date = newDate;
                    }
                }
            }
        }
        return date;
    }
}