package org.andromda.maven.plugin.andromdapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
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
import org.codehaus.plexus.util.dag.CycleDetectedException;


/**
 * A Mojo used for executing the build goals from the top level project.
 *
 * @goal build
 * @author Chad Brandon
 */
public class BuildMojo
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
     * <em>-Dmodules=mda,core,common</em> or if you want to specify the goals
     * to execute as well:
     * <em>-Dmodules=mda:[goal1+goal2+goal3],core:[goal1]<em>.
     *
     * @parameter expression="${modules}"
     */
    private String modules;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * If defined starts the build console (i.e. keeps maven loaded and running)
     *
     * @parameter expression="${console}"
     */
    private String startConsole;

    /**
     * The default module goals to execute.
     *
     * @parameter
     */
    private List goals = new ArrayList(Arrays.asList(new String[] {"install"}));

    /**
     * The string used to quite the console;
     */
    private static final String EXIT = "exit";

    /**
     * Used to contruct Maven project instances from POMs.
     *
     * @component
     */
    private MavenProjectBuilder projectBuilder;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            if (this.startConsole != null)
            {
                boolean executed = false;
                this.printLine();
                String input;
                do
                {
                    this.printConsolePrompt();
                    input = this.readLine();
                    try
                    {
                        executed = this.executeModules(input);

                        // - if nothing was executed, try a goal in the current project
                        if (!EXIT.equals(input) && this.project != null && !executed && input != null &&
                            input.trim().length() > 0)
                        {
                            executed = true;
                            final List goals = Arrays.asList(input.split("\\s+"));
                            this.executeModules(
                                StringUtils.join(
                                    this.project.getModules().iterator(),
                                    ","),
                                goals,
                                true);
                        }
                    }
                    catch (final Throwable throwable)
                    {
                        throwable.printStackTrace();
                    }
                    if (executed)
                    {
                        this.printLine();
                    }
                }
                while (!EXIT.equals(input));
            }
            else
            {
                this.executeModules(this.modules);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error executing modules", throwable);
        }
    }

    /**
     * Prints the prompt for the console
     */
    private void printConsolePrompt()
    {
        if (this.project != null)
        {
            this.printText("");
            this.printText(this.project.getArtifactId() + " " + this.project.getVersion() + ">");
        }
    }

    /**
     * Prints text to the console.
     *
     * @param text the text to print to the console;
     */
    private void printText(final String text)
    {
        System.out.print(text);
        System.out.flush();
    }

    /**
     * Prints a line to standard output.
     */
    private void printLine()
    {
        System.out.println();
    }

    /**
     * Reads a line from standard input and returns the value.
     *
     * @return the value read from standard input.
     */
    private String readLine()
    {
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String inputString = null;
        try
        {
            inputString = input.readLine();
        }
        catch (final IOException exception)
        {
            inputString = null;
        }
        return inputString == null || inputString.trim().length() == 0 ? null : inputString;
    }

    /**
     * Creates all project modules and executes them.
     *
     * @param modules the comma seperated list of modules to execute.
     * @return true if any modules were executed, false otherwise.
     * @throws MojoExecutionException
     * @throws CycleDetectedException
     * @throws LifecycleExecutionException
     * @throws BuildFailureException
     */
    private boolean executeModules(final String modules)
        throws MojoExecutionException, CycleDetectedException, LifecycleExecutionException, BuildFailureException
    {
        return this.executeModules(
            modules,
            null,
            false);
    }

    /**
     * Creates all project modules and executes them.
     *
     * @param modules The list of modules to execute.
     * @param goals the list of goals to execute (if null, the, goals will be retrieved from project map).
     * @param sortProjects whether or not projects should be sorted and then executed or whether they should be executed in the
     *                     order they're in.
     * @throws CycleDetectedException
     * @throws LifecycleExecutionException
     * @throws MojoExecutionException
     * @throws BuildFailureException
     * @return true/false on whether or not any modules were executed
     */
    private boolean executeModules(
        final String modules,
        final List goals,
        boolean sortProjects)
        throws CycleDetectedException, LifecycleExecutionException, MojoExecutionException, BuildFailureException
    {
        final Map projects = this.collectProjects(modules);
        boolean executed = !projects.isEmpty();

        // - only execute if we have some projects
        if (executed)
        {
            if (!sortProjects)
            {
                for (final Iterator iterator = projects.keySet().iterator(); iterator.hasNext();)
                {
                    final MavenProject project = (MavenProject)iterator.next();
                    List projectGoals;
                    if (goals == null)
                    {
                        projectGoals = (List)projects.get(project);
                        if (projectGoals.isEmpty())
                        {
                            projectGoals.addAll(this.goals);
                        }
                    }
                    else
                    {
                        projectGoals = goals;
                    }
                    this.executeProjects(
                        Collections.singletonList(project),
                        projectGoals);
                }
            }
            else
            {
                this.executeProjects(
                    projects.keySet(),
                    goals);
            }
        }
        return executed;
    }

    /**
     * Executes the given maven <code>project</code>.
     *
     * @param project the project to execute.
     * @param goals the goals to execute on the project.
     * @throws CycleDetectedException
     * @throws LifecycleExecutionException
     * @throws BuildFailureException
     */
    private void executeProjects(
        final Collection projects,
        final List goals)
        throws CycleDetectedException, LifecycleExecutionException, BuildFailureException
    {
        if (goals.isEmpty())
        {
            goals.addAll(this.goals);
        }
        if (projects.size() > 1)
        {
            this.getLog().info("Reactor build order:");
        }
        final ReactorManager reactorManager = new ReactorManager(new ArrayList(projects));
        for (final Iterator iterator = reactorManager.getSortedProjects().iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
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
                new Properties(),
                this.session.getStartTime());

        projectSession.setUsingPOMsFromFilesystem(true);
        this.lifecycleExecutor.execute(
            projectSession,
            reactorManager,
            projectSession.getEventDispatcher());
    }

    /**
     * Collects all project modules to execute.
     *
     * @return the Map of collected projects (the key is the project, the value
     *         the goals).
     * @param The list of modules to execute.
     * @throws MojoExecutionException
     */
    private Map collectProjects(final String modules)
        throws MojoExecutionException
    {
        final Map projects = new LinkedHashMap();
        final Map poms = getModulePoms(modules);

        if (!poms.isEmpty())
        {
            for (final Iterator iterator = poms.keySet().iterator(); iterator.hasNext();)
            {
                final File pom = (File)iterator.next();
                try
                {
                    final MavenProject project = this.buildProject(pom);
                    getLog().debug("Adding project " + project.getId());
                    projects.put(
                        project,
                        poms.get(pom));
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
     * Builds a project for the given <code>pom</code>.
     * @param pom the pom from which to build the project.
     * @return the built project.
     * @throws ProjectBuildingException
     */
    private MavenProject buildProject(final File pom)
        throws ProjectBuildingException
    {
        return this.projectBuilder.build(
            pom,
            this.session.getLocalRepository(),
            new DefaultProfileManager(this.session.getContainer()));
    }

    /**
     * Gets all POMs for the modules specified.
     *
     * @param moduleList the list of modules to execute.
     * @return the list of module poms
     */
    private Map getModulePoms(final String moduleList)
    {
        final Map poms = new LinkedHashMap();
        final String[] modules = moduleList != null ? moduleList.split(",") : null;

        final String goalPrefix = ":";
        if (modules != null)
        {
            final int numberOfModules = modules.length;
            for (int ctr = 0; ctr < numberOfModules; ctr++)
            {
                String module = modules[ctr].trim();
                final List goalsList = new ArrayList();
                if (module.indexOf(goalPrefix) != -1)
                {
                    final String[] goals = module.replaceAll(
                            ".*(:\\[)|(\\])",
                            "").split("\\+");
                    if (goals != null)
                    {
                        final int numberOfGoals = goals.length;
                        for (int ctr2 = 0; ctr2 < numberOfGoals; ctr2++)
                        {
                            final String goal = goals[ctr2].trim();
                            goalsList.add(goal);
                        }
                    }
                }
                module = module.replaceAll(
                        goalPrefix + "\\[.*\\]",
                        "");
                final File pom = new File(this.baseDirectory, module + "/pom.xml");
                if (pom.isFile())
                {
                    poms.put(
                        pom,
                        goalsList);
                }
            }
        }
        return poms;
    }
}