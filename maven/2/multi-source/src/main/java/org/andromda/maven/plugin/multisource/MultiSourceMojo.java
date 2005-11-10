package org.andromda.maven.plugin.multisource;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * A Mojo who's sole purpose is to allow multiple source roots
 * to be added to the project (since the Maven Compiler Plugin
 * doesn't allow it), this plugin should be removed if they
 * allow it in the future.
 *
 * @author Chad Brandon
 * @goal add-source
 * @phase generate-sources
 */
public class MultiSourceMojo
    extends AbstractMojo
{
    /**
     * The source directories containing the sources to be compiled.
     *
     * @parameter
     * @required
     */
    private List sourceDirectories;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @description "the maven project to use"
     */
    private MavenProject project;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        this.project.getCompileSourceRoots().addAll(this.sourceDirectories);
    }
}