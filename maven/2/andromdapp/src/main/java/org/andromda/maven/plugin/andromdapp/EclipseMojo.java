package org.andromda.maven.plugin.andromdapp;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.andromda.core.common.Constants;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.maven.plugin.andromdapp.eclipse.ClasspathWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Writes the necessary .classpath and .project files
 * for a new eclipse application.
 *
 * @goal eclipse
 * @author Chad Brandon
 */
public class EclipseMojo
    extends AbstractMojo
{
    /**
     * @parameter expression="${session}"
     */
    private MavenSession session;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter
     */
    private String[] includes = new String[] {"*/pom.xml"};

    /**
     * @parameter
     */
    private String[] excludes = new String[0];

    /**
     * The name of the variable that will store the maven repository location.
     *
     * @parameter expression="${repository.variable.name}
     */
    private String repositoryVariableName = "M2_REPO";

    /**
     * Artifact factory, needed to download source jars for inclusion in classpath.
     *
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;

    /**
     * Artifact resolver, needed to download source jars for inclusion in classpath.
     *
     * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"
     * @required
     * @readonly
     */
    private ArtifactResolver artifactResolver;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final ClasspathWriter writer = new ClasspathWriter(this.getLog());
            writer.write(
                this.project,
                this.collectProjects(),
                this.repositoryVariableName,
                this.artifactFactory,
                this.artifactResolver,
                this.localRepository);
        }
        catch (Throwable throwable)
        {
            throw new MojoExecutionException("Error creating eclipse configuration", throwable);
        }
    }

    /**
     * Collects all projects from all POMs within the current project.
     *
     * @return all collection Maven project instances.
     *
     * @throws MojoExecutionException
     */
    private List collectProjects()
        throws Exception
    {
        final List projects = new ArrayList();
        MavenProjectBuilder projectBuilder;
        try
        {
            projectBuilder = (MavenProjectBuilder)this.session.getContainer().lookup(MavenProjectBuilder.ROLE);
        }
        catch (ComponentLookupException exception)
        {
            throw new MojoExecutionException("Cannot get a MavenProjectBuilder instance", exception);
        }

        final List poms = this.getPoms();
        for (ListIterator iterator = poms.listIterator(); iterator.hasNext();)
        {
            final File pom = (File)iterator.next();
            try
            {
                final MavenProject project =
                    projectBuilder.build(
                        pom,
                        this.session.getLocalRepository(),
                        new DefaultProfileManager(this.session.getContainer()));
                if (this.getLog().isDebugEnabled())
                {
                    this.getLog().debug("Adding project " + project.getId());
                }
                projects.add(project);
            }
            catch (ProjectBuildingException exception)
            {
                throw new MojoExecutionException("Error loading " + pom, exception);
            }
        }

        return projects;
    }

    /**
     * Retrieves all the POMs for the given project.
     *
     * @return all poms found.
     */
    private List getPoms()
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.project.getBasedir());
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();

        List poms = new ArrayList();

        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            poms.add(new File(
                    this.project.getBasedir(),
                    scanner.getIncludedFiles()[ctr]));
        }

        return poms;
    }
}