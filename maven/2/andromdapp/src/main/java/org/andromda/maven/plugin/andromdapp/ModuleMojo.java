package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.BuildFailureException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.dag.CycleDetectedException;


/**
 * A Mojo used for execution modules within a given project.
 *
 * @goal module
 * @author Chad Brandon
 */
public class ModuleMojo
    extends AbstractMojo
{
    /**
     * @parameter expression="${component.org.apache.maven.lifecycle.LifecycleExecutor}"
     */
    private LifecycleExecutor lifecycleExecutor;

    /**
     * @parameter expression="${session}"
     */
    private MavenSession session;

    /**
     * @parameter expression="${project.basedir}"
     */
    private File baseDirectory;

    /**
     * A comma seperated list of modules to execute in the form:
     * <em>-Dmodules=mda,core,common</em> or if you want to specify the
     * goals to execute as well: <em>-Dmodules=mda=[goal1 goal2],core=[goal1]<em>.
     *
     * @parameter expression="${modules}"
     */
    private String modules;

    /**
     * The default module goals to execute.
     *
     * @parameter
     */
    private List goals = new ArrayList(Arrays.asList(new String[] {"install"}));

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            executeModules();
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error executing modules", throwable);
        }
    }

    private void executeModules()
        throws CycleDetectedException, LifecycleExecutionException, MojoExecutionException, BuildFailureException
    {
        final List projects = this.collectProjects();

        // - only execute if we have some projects
        if (!projects.isEmpty())
        {
            final ReactorManager reactorManager = new ReactorManager(projects);
            MavenSession projectSession =
                new MavenSession(
                    this.session.getContainer(),
                    this.session.getSettings(),
                    this.session.getLocalRepository(),
                    this.session.getEventDispatcher(),
                    reactorManager,
                    goals,
                    baseDirectory.toString(),
                    new Properties(),
                    this.session.getStartTime());

            projectSession.setUsingPOMsFromFilesystem(true);
            this.lifecycleExecutor.execute(
                projectSession,
                reactorManager,
                projectSession.getEventDispatcher());
        }
    }

    /**
     * Collects all project modules to execute.
     *
     * @return the list of collected projects.
     *
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

        final List poms = getModulePoms();

        if (!poms.isEmpty())
        {
            for (final Iterator iterator = poms.iterator(); iterator.hasNext();)
            {
                File pom = (File)iterator.next();

                try
                {
                    MavenProject project =
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
    private List getModulePoms()
    {
        final List poms = new ArrayList();
        final String[] modules = this.modules != null ? this.modules.split(",") : null;

        if (modules != null)
        {
            final int numberOfModules = modules.length;
            for (int ctr = 0; ctr < numberOfModules; ctr++)
            {
                final String module = modules[ctr].trim();
                final File pom = new File(this.baseDirectory, module + "/pom.xml");
                if (pom.isFile())
                {
                    poms.add(pom);
                }
            }
        }
        return poms;
    }
}