package org.andromda.maven.plugins.sablecc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.common.ResourceUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.sablecc.sablecc.SableCC;


/**
 * A Maven2 plugin for processing in SableCC files.
 *
 * @phase generate-sources
 * @goal generate
 * @description SableCC plugin
 * @author Chad Brandon
 */
public class SableCCMojo
    extends AbstractMojo
{
    /**
     * The directory that will contain the *.grammer files.
     *
     * @parameter expression="${basedir}/src/main/sablecc"
     */
    private String sourceDirectory;

    /**
     * Directory to which the build source is generated.
     *
     * @parameter expression="${project.build.directory}/src/maven/java"
     */
    private String buildSourceDirectory;

    /**
     * Maven project helper class for adding resources
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     */
    private MavenProjectHelper projectHelper;

    /**
     * The current project.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * The pattern to use to include all .dat files
     */
    private static final String DAT_FILE_PATTERN = "**/**.dat";

    /**
     * The pattern for the grammar files.
     */
    private static final String GRAMMAR_FILE_PATTERN = "**/*.grammar";

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final File buildSourceDirectory = new File(this.buildSourceDirectory);
            buildSourceDirectory.mkdirs();

            final List grammarPaths =
                ResourceUtils.getDirectoryContents(
                    ResourceUtils.toURL(this.sourceDirectory),
                    true,
                    new String[] {GRAMMAR_FILE_PATTERN});
            boolean execute = false;
            for (final Iterator iterator = grammarPaths.iterator(); iterator.hasNext();)
            {
                final URL grammar = ResourceUtils.toURL((String)iterator.next());
                if (grammar != null)
                {
                    execute =
                        ResourceUtils.modifiedAfter(
                            ResourceUtils.getLastModifiedTime(grammar),
                            buildSourceDirectory);
                    if (execute)
                    {
                        break;
                    }
                }
            }

            if (execute)
            {
                for (Iterator iterator = grammarPaths.iterator(); iterator.hasNext();)
                {
                    final URL resource = ResourceUtils.toURL(((String)iterator.next()));
                    SableCC.processGrammar(
                        new File(resource.getFile()),
                        buildSourceDirectory);
                }
            }
            else
            {
                this.getLog().info("Nothing to generate - all grammars are up to date");
            }

            projectHelper.addResource(
                project,
                this.buildSourceDirectory,
                Collections.singletonList(DAT_FILE_PATTERN),
                new ArrayList());
            project.addCompileSourceRoot(this.buildSourceDirectory);
        }
        catch (Throwable throwable)
        {
            throw new MojoExecutionException("An error occurred while attempting to process SableCC grammar(s)",
                throwable);
        }
    }
}