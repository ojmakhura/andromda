package org.andromda.maven.plugin.andromdapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.maven.plugin.andromdapp.utils.ProjectUtils;
import org.andromda.maven.plugin.andromdapp.utils.Projects;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.BuildFailureException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
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
     * A comma separated list of modules to execute in the form:
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
     * Any execution properties.
     *
     * @parameter
     */
    private Properties executionProperties = new Properties();

    /**
     * Identifies system properties when running in console mode.
     */
    private static final String EXECUTION_PROPERTY_TOKEN = "-D";

    /**
     * Lists all execution properties when running in console mode.
     */
    private static final String LIST_PROPERTIES = "-list";

    /**
     * Clears all execution properties.
     */
    private static final String CLEAR_PROPERTIES = "-clear";

    /**
     * Explicity calls the garbage collector.
     */
    private static final String GARBAGE_COLLECT = "-gc";

    /**
     * The prefix environment variables must have.
     *
     * @parameter expression="env."
     */
    private String environmentVariablePrefix;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final Map environment = this.getEnvironment();
            if (this.startConsole != null && !this.startConsole.equals(Boolean.FALSE.toString()))
            {
                boolean executed = false;
                this.printLine();
                while (true)
                {
                    this.printConsolePrompt();
                    String input = StringUtils.trimToEmpty(this.readLine());
                    if (EXIT.equals(input))
                    {
                        break;
                    }
                    if (input.startsWith(EXECUTION_PROPERTY_TOKEN))
                    {
                        input = input.replaceFirst(
                                EXECUTION_PROPERTY_TOKEN,
                                "");
                        int index = input.indexOf("=");
                        String name;
                        String value;
                        if (index <= 0)
                        {
                            name = input.trim();
                            value = "true";
                        }
                        else
                        {
                            name = input.substring(
                                    0,
                                    index).trim();
                            value = input.substring(index + 1).trim();
                        }
                        if (value.startsWith(this.environmentVariablePrefix))
                        {
                            value = StringUtils.replace(
                                    value,
                                    this.environmentVariablePrefix,
                                    "");
                            if (environment.containsKey(value))
                            {
                                value = ObjectUtils.toString(environment.get(value)).trim();
                            }
                        }
                        this.executionProperties.put(
                            name,
                            value);
                        System.setProperty(
                            name,
                            value);
                        this.printExecutionProperties();
                    }
                    else if (LIST_PROPERTIES.equals(input))
                    {
                        this.printExecutionProperties();
                    }
                    else if (CLEAR_PROPERTIES.equals(input))
                    {
                        this.executionProperties.clear();
                        this.printExecutionProperties();
                    }
                    else if (GARBAGE_COLLECT.equals(input))
                    {
                        System.gc();
                    }
                    else
                    {
                        try
                        {
                            executed = this.executeModules(input);

                            // - if nothing was executed, try a goal in the current project
                            if (this.project != null && !executed && input != null && input.trim().length() > 0)
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
                        Projects.instance().clear();
                    }
                }
            }
            else
            {
                this.executionProperties.putAll(this.session.getExecutionProperties());
                this.executeModules(this.modules);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error executing modules", throwable);
        }
    }

    private static final String GET_ENVIRONMENT_METHOD = "getenv";

    /**
     * Retrieves the environment variables (will only work when running jdk5 or above).
     *
     * @return the environment variables.
     */
    private Map getEnvironment()
    {
        final Map variables = new HashMap();
        try
        {
            final Method method = System.class.getMethod(
                    GET_ENVIRONMENT_METHOD,
                    null);
            final Object result = method.invoke(
                    System.class,
                    null);
            if (result instanceof Map)
            {
                variables.putAll((Map)result);
            }
        }
        catch (Exception exception)
        {
            // - ignore (means we can't retrieve the environment with the current JDK).
        }
        return variables;
    }

    private void printExecutionProperties()
    {
        this.printLine();
        this.printTextWithLine("| ------------- execution properties ------------- |");
        for (final Iterator iterator = this.executionProperties.keySet().iterator(); iterator.hasNext();)
        {
            final String name = (String)iterator.next();
            System.out.println("    " + name + " = " + this.executionProperties.getProperty(name));
        }
        this.printTextWithLine("| ------------------------------------------------ |");
        this.printLine();
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
     * Prints text with a new line to the console.
     *
     * @param text the text to print to the console.
     */
    private void printTextWithLine(final String text)
    {
        System.out.println(text);
        System.out.flush();
    }

    /**
     * Prints a line to standard output.
     */
    private void printLine()
    {
        System.out.println();
        System.out.flush();
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
        return StringUtils.trimToNull(inputString);
    }

    /**
     * Creates all project modules and executes them.
     *
     * @param modules the comma separated list of modules to execute.
     * @return true if any modules were executed, false otherwise.
     * @throws MojoExecutionException
     * @throws CycleDetectedException
     * @throws LifecycleExecutionException
     * @throws BuildFailureException
     */
    private boolean executeModules(final String modules)
        throws MojoExecutionException
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
        throws MojoExecutionException
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
     * @throws MojoExecutionException
     * @throws CycleDetectedException
     * @throws LifecycleExecutionException
     * @throws BuildFailureException
     */
    private void executeProjects(
        final Collection projects,
        final List goals)
        throws MojoExecutionException
    {
        try
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
                    this.executionProperties,
                    this.session.getStartTime());

            projectSession.setUsingPOMsFromFilesystem(true);
            this.lifecycleExecutor.execute(
                projectSession,
                reactorManager,
                projectSession.getEventDispatcher());
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("An error occured while attempting to execute projects", throwable);
        }
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
                    final MavenProject project = ProjectUtils.getProject(
                            this.projectBuilder,
                            this.session,
                            pom,
                            this.getLog());
                    if (project != null)
                    {
                        if (this.getLog().isDebugEnabled())
                        {
                            this.getLog().debug("Adding project " + project.getId());
                        }
                        projects.put(
                            project,
                            poms.get(pom));
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
                    throw new MojoExecutionException("Error loading POM --> '" + pom + "'", exception);
                }
            }
        }
        return projects;
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