package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.andromda.maven.plugin.andromdapp.utils.ProjectUtils;
import org.andromda.maven.plugin.andromdapp.utils.Projects;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Allows the execution of maven from a given project (searches
 * for nested pom.xml files and executes Maven)
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
     * @component role="org.apache.maven.lifecycle.LifecycleExecutor"
     */
    private LifecycleExecutor lifecycleExecutor;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Used to construct Maven project instances from POMs.
     *
     * @component
     */
    private MavenProjectBuilder projectBuilder;

    /**
     * @parameter
     */
    private String[] includes = new String[] {"*/**/pom.xml"};

    /**
     * Whether or not goals should be aggregated when executing the projects
     * (i.e. whether goals should be executed together per project or separate for
     * each project).
     *
     * @parameter
     */
    private boolean aggregateGoals = false;

    /**
     * @parameter
     */
    private String[] excludes = new String[] {"pom.xml"};

    /**
     * This is used to remove the 'version' property from the system
     * properties as it ends up being used in the POM interpolation (in other
     * words is a hack until they fix this Maven2 bug).
     */
    private static final String VERSION_PROPERTY = "version";

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        // - remove any "version" property as it seems to be picked up and used
        //   in maven2's POM interpolation (remove this when this issue is fixed).
        System.getProperties().remove(VERSION_PROPERTY);
        if (!Projects.instance().isPresent(this.project.getId()))
        {
            try
            {
                final List<MavenProject> projects = this.collectProjects();

                // - only execute if we have some projects
                if (!projects.isEmpty())
                {
                    final List<String> goals = this.session.getGoals();
                    if (goals.isEmpty())
                    {
                        if (this.project != null)
                        {
                            final String defaultGoal = this.project.getDefaultGoal();
                            if (StringUtils.isNotBlank(defaultGoal))
                            {
                                goals.add(defaultGoal);
                            }
                        }
                    }
                    if (this.aggregateGoals)
                    {
                        final ReactorManager reactorManager = new ReactorManager(projects);
                        if (projects.size() > 1)
                        {
                            this.getLog().info("Reactor build order:");
                        }
                        for (final MavenProject project : reactorManager.getSortedProjects())
                        {
                            this.getLog().info("  " + project.getName());
                        }
                        final MavenSession projectSession =
                            new MavenSession(
                                this.session.getContainer(),
                                this.session.getSettings(),
                                this.session.getLocalRepository(),
                                this.session.getEventDispatcher(),
                                reactorManager,
                                goals,
                                this.baseDirectory.toString(),
                                this.session.getExecutionProperties(),
                                this.session.getStartTime());

                        projectSession.setUsingPOMsFromFilesystem(true);
                        this.lifecycleExecutor.execute(
                            projectSession,
                            reactorManager,
                            projectSession.getEventDispatcher());
                    }
                    else
                    {
                        for (Object goal : this.session.getGoals())
                        {
                            final ReactorManager reactorManager = new ReactorManager(projects);
                            if (projects.size() > 1)
                            {
                                this.getLog().info("Reactor build order:");
                            }
                            for (final MavenProject project : reactorManager.getSortedProjects())
                            {
                                this.getLog().info("  " + project.getName());
                            }

                            final MavenSession projectSession =
                                new MavenSession(
                                    this.session.getContainer(),
                                    this.session.getSettings(),
                                    this.session.getLocalRepository(),
                                    this.session.getEventDispatcher(),
                                    reactorManager,
                                    Collections.singletonList((String)goal),
                                    this.baseDirectory.toString(),
                                    this.session.getExecutionProperties(),
                                    this.session.getStartTime());

                            //TODO This method is not supported by maven 3!
                            projectSession.setUsingPOMsFromFilesystem(true);

                            //TODO This method is not supported by maven 3!
                            this.lifecycleExecutor.execute(
                                projectSession,
                                reactorManager,
                                projectSession.getEventDispatcher());
                        }
                    }
                }
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("Execution failed", throwable);
            }
            Projects.instance().add(this.project.getId());
        }
    }

    /**
     * Collects all project modules to execute.
     *
     * @return the Map of collected projects (the key is the project, the value
     *         the goals).
     * @throws MojoExecutionException
     */
    private List<MavenProject> collectProjects()
        throws MojoExecutionException
    {
        final List<MavenProject> projects = new ArrayList<MavenProject>();
        final List<File> poms = this.getPoms();
        if (!poms.isEmpty())
        {
            for (final File pom : poms)
            {
                // - first attempt to get the existing project from the session
                try
                {
                    final MavenProject project = ProjectUtils.getProject(
                            this.projectBuilder,
                            this.session,
                            pom,
                            this.getLog());
                    if (project != null)
                    {
                        if (this.getLog().isDebugEnabled())
                        {
                            getLog().debug("Adding project " + project.getId());
                        }
                        projects.add(project);
                    }
                    else
                    {
                        if (this.getLog().isWarnEnabled())
                        {
                            this.getLog().warn("Could not load project from pom: " + pom + " - ignoring");
                        }
                    }
                }
                catch (ProjectBuildingException exception)
                {
                    throw new MojoExecutionException("Error loading POM --> '" + pom + '\'', exception);
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
    private List<File> getPoms()
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.baseDirectory);
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();
        final List<File> poms = new ArrayList<File>();
        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            poms.add(new File(
                    this.baseDirectory,
                    scanner.getIncludedFiles()[ctr]));
        }
        return poms;
    }
}