package org.andromda.maven.plugin.mavenexecute;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Allows the execution of maven from a given project (searches
 * for nested pom.xml and executes Maven
 *
 * @author Chad Brandon
 * @goal maven
 */
public class MavenExecuteMojo
    extends AbstractMojo
{
    /**
     * @parameter expression="${session}"
     */
    private MavenSession session;

    /**
     * @parameter expression="${project.basedir}"
     */
    private File baseDirectory;

    /**
     * @parameter expression="${component.org.apache.maven.lifecycle.LifecycleExecutor}"
     */
    private LifecycleExecutor lifecycleExecutor;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter
     */
    private String[] includes = new String[] {"**/pom.xml"};

    /**
     * @parameter
     */
    private String[] excludes = new String[] {"pom.xml"};

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final List projects = this.collectProjects();

            // - only execute if we have some projects
            if (!projects.isEmpty())
            {
                final ReactorManager reactorManager = new ReactorManager(projects);
                if (projects.size() > 1)
                {
                    this.getLog().info("Reactor build order:");
                }
                for (final Iterator iterator = reactorManager.getSortedProjects().iterator(); iterator.hasNext();)
                {
                    final MavenProject project = (MavenProject)iterator.next();
                    this.getLog().info("  " + project.getName());
                }
                final List goals = this.session.getGoals();
                if (goals.isEmpty())
                {
                    if (this.project != null)
                    {
                        final String defaultGoal = this.project.getDefaultGoal();
                        if (defaultGoal != null && defaultGoal.trim().length() > 0)
                        {
                            goals.add(defaultGoal);
                        }
                    }
                }
                final MavenSession projectSession =
                    new MavenSession(
                        this.session.getContainer(),
                        this.session.getSettings(),
                        this.session.getLocalRepository(),
                        this.session.getEventDispatcher(),
                        reactorManager,
                        this.session.getGoals(),
                        this.baseDirectory.toString(),
                        new Properties(),
                        this.session.getStartTime());

                projectSession.setUsingPOMsFromFilesystem(true);
                this.lifecycleExecutor.execute(
                    projectSession,
                    reactorManager,
                    projectSession.getEventDispatcher());
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Execution failed", throwable);
        }
    }

    /**
     * Collects all project modules to execute.
     *
     * @return the Map of collected projects (the key is the project, the value
     *         the goals).
     * @throws MojoExecutionException
     */
    private List collectProjects()
        throws MojoExecutionException
    {
        final List projects = new ArrayList();
        MavenProjectBuilder projectBuilder;

        try
        {
            projectBuilder = (MavenProjectBuilder)session.getContainer().lookup(MavenProjectBuilder.ROLE);
        }
        catch (ComponentLookupException exception)
        {
            throw new MojoExecutionException("Cannot get a MavenProjectBuilder", exception);
        }

        final List poms = this.getPoms();
        if (!poms.isEmpty())
        {
            for (final Iterator iterator = poms.iterator(); iterator.hasNext();)
            {
                final File pom = (File)iterator.next();

                try
                {
                    final MavenProject project =
                        projectBuilder.build(
                            pom,
                            session.getLocalRepository(),
                            new DefaultProfileManager(session.getContainer()));
                    getLog().debug("Adding project " + project.getId());
                    projects.add(project);
                }
                catch (ProjectBuildingException exception)
                {
                    throw new MojoExecutionException("Error loading POM --> '" + pom + "'", exception);
                }
            }
        }
        return projects;
    }

    /**
     * Gets all POMs for the modules specified.
     *
     * @return the list of module poms
     */
    private List getPoms()
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.baseDirectory);
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();
        final List poms = new ArrayList();
        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            poms.add(new File(
                    this.baseDirectory,
                    scanner.getIncludedFiles()[ctr]));
        }
        return poms;
    }
}