package org.andromda.maven.plugin.bootstrap;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.ResourceUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * Provides any initialization required by the AndroMDA build.
 *
 * @author Chad Brandon
 * @phase install
 * @goal initialize
 */
public class BootstrapInitializeMojo
    extends AbstractMojo
{
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @description "the maven project to use"
     */
    protected MavenProject project;

    /**
     * @parameter
     * @required
     * @description the directory where the bootstrap artifacts will be installed from.
     */
    protected String bootstrapDirectory;

    /**
     * @parameter expression="bootstrap"
     * @required
     * @readonly
     * @description the name root directory of the bootstrap artifacts.
     */
    protected String bootstrapDirectoryName;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File bootstrapDirectory = new File(this.bootstrapDirectory, this.bootstrapDirectoryName);
            final Collection contents = ResourceUtils.getDirectoryContents(bootstrapDirectory.toURL(), true, new String[] {"**/*"});
            for (final Iterator iterator = contents.iterator(); iterator.hasNext();)
            {
                final String path = (String)iterator.next();
                System.out.println("the path:" + path);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error creating bootstrap artifact", throwable);
        }
    }
}