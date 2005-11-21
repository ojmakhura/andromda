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
     *
     * @parameter expression="false"
     * @required
     */
    private boolean lastModifiedCheck;

    /**
     * The directory to which the build source is located (any generated source).
     *
     * @parameter expression="${project.build.directory}/src/main/java"
     */
    private String buildSourceDirectory;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute(final Configuration configuration)
        throws MojoExecutionException
    {
        boolean execute = true;
        if (this.lastModifiedCheck)
        {
            final URL configurationUri = ResourceUtils.toURL(this.configurationUri);
            final File directory = new File(this.buildSourceDirectory);
            execute = ResourceUtils.modifiedAfter(
                    ResourceUtils.getLastModifiedTime(configurationUri),
                    directory);
            if (!execute)
            {
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
                            execute = ResourceUtils.modifiedAfter(
                                    model.getLastModified(),
                                    directory);
                            if (execute)
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (execute)
        {
            final AndroMDA andromda = AndroMDA.newInstance();
            andromda.run(configuration);
            andromda.shutdown();
        }
        else
        {
            this.getLog().info("Files are up-to-date, skipping AndroMDA execution");
        }
        final File buildSourceDirectory =
            this.buildSourceDirectory != null ? new File(this.buildSourceDirectory) : null;
        if (buildSourceDirectory != null)
        {
            this.getProject().addCompileSourceRoot(buildSourceDirectory.toString());
        }
    }
}