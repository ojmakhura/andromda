package org.andromda.andromdapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.Constants;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Represents an AndroMDApp type (j2ee, .net, etc).
 *
 * @author Chad Brandon
 */
public class AndroMDAppType
{
    /**
     * The velocity template context.
     */
    private final Map templateContext = new LinkedHashMap();

    /**
     * The namespace used to initialize the template engine.
     */
    private static final String NAMESPACE = "andromdapp";

    /**
     * The location to which temporary merge location files are written.
     */
    private static final String TEMPORARY_MERGE_LOCATION = Constants.TEMPORARY_DIRECTORY + '/' + NAMESPACE;

    /**
     * Performs any required initialization for the type.
     *
     * @throws Exception
     */
    protected void initialize()
        throws Exception
    {
        if (this.configurations != null)
        {
            for (final Iterator iterator = this.configurations.iterator(); iterator.hasNext();)
            {
                this.templateContext.putAll(((Configuration)iterator.next()).getAllProperties());
            }
        }
    }

    /**
     * Prompts the user for the input required to generate an application with
     * the correct information and returns the descriptor contents after being interpolated by all
     * properties in the template context.
     *
     * @return the results of the interpolated descriptor.
     * @throws Exception
     */
    protected String promptUser()
        throws Exception
    {
        for (final Iterator iterator = this.getPrompts().iterator(); iterator.hasNext();)
        {
            final Prompt prompt = (Prompt)iterator.next();
            final String id = prompt.getId();

            boolean validPreconditions = true;
            for (final Iterator preconditionsIterator = prompt.getPreconditions().iterator();
                preconditionsIterator.hasNext();)
            {
                final Conditions preconditions = (Conditions)preconditionsIterator.next();
                final String conditionsType = preconditions.getType();
                for (final Iterator conditionIterator = preconditions.getConditions().iterator();
                    conditionIterator.hasNext();)
                {
                    final Condition precondition = (Condition)conditionIterator.next();
                    validPreconditions = precondition.evaluate(this.templateContext.get(precondition.getId()));

                    // - if we're 'anding' the conditions, we break at the first false
                    if (Conditions.TYPE_AND.equals(conditionsType))
                    {
                        if (!validPreconditions)
                        {
                            break;
                        }
                    }
                    else
                    {
                        // otherwise we break at the first true condition
                        if (validPreconditions)
                        {
                            break;
                        }
                    }
                }
            }

            if (validPreconditions)
            {
                Object response = this.templateContext.get(id);

                // - only prompt when the id isn't already in the context
                if (response == null)
                {
                    do
                    {
                        response = this.promptForInput(prompt);
                    }
                    while (!prompt.isValidResponse(ObjectUtils.toString(response)));
                }
                this.setConditionalProperties(
                    prompt.getConditions(),
                    response);
                if (prompt.isSetResponseAsTrue())
                {
                    this.templateContext.put(
                        response,
                        Boolean.TRUE);
                }
                this.templateContext.put(
                    id,
                    prompt.getResponse(response));
            }
        }
        return this.getTemplateEngine().getEvaluatedExpression(
            ResourceUtils.getContents(this.resource),
            this.templateContext);
    }

    /**
     * Prompts the user for the information contained in the given
     * <code>prompt</code>.
     *
     * @param prompt the prompt from which to format the prompt text.
     * @return the response of the prompt.
     */
    private String promptForInput(final Prompt prompt)
    {
        this.printPromptText(prompt.getText());
        final String input = this.readLine();
        return input;
    }

    /**
     * Prompts the user for the information contained in the given
     * <code>prompt</code>.
     *
     * @param prompt the prompt from which to format the prompt text.
     */
    private void setConditionalProperties(
        final List<Condition> conditions,
        final Object value)
    {
        for (final Iterator iterator = conditions.iterator(); iterator.hasNext();)
        {
            final Condition condition = (Condition)iterator.next();
            final String equalCondition = condition.getEqual();
            if (equalCondition != null && equalCondition.equals(value))
            {
                this.setProperties(condition);
            }
            final String notEqualCondition = condition.getNotEqual();
            if (notEqualCondition != null && !notEqualCondition.equals(value))
            {
                this.setProperties(condition);
            }
        }
    }

    /**
     * Sets the prompt values from the given <code>condition</code>.
     *
     * @param condition the condition from which to populate the values.
     */
    private void setProperties(final Condition condition)
    {
        if (condition != null)
        {
            final Map values = condition.getProperties();
            for (final Iterator valueIterator = values.keySet().iterator(); valueIterator.hasNext();)
            {
                final String id = (String)valueIterator.next();
                this.templateContext.put(
                    id,
                    values.get(id));
            }
        }
    }

    /**
     * The template engine class.
     */
    private String templateEngineClass;

    /**
     * Sets the class of the template engine to use.
     *
     * @param templateEngineClass the Class of the template engine
     *        implementation.
     */
    public void setTemplateEngineClass(final String templateEngineClass)
    {
        this.templateEngineClass = templateEngineClass;
    }

    /**
     * The template engine that this plugin will use.
     */
    private TemplateEngine templateEngine = null;

    /**
     * Gets the template that that will process the templates.
     *
     * @return the template engine instance.
     * @throws Exception
     */
    private TemplateEngine getTemplateEngine()
        throws Exception
    {
        if (this.templateEngine == null)
        {
            this.templateEngine =
                (TemplateEngine)ComponentContainer.instance().newComponent(
                    this.templateEngineClass,
                    TemplateEngine.class);
            this.getTemplateEngine().setMergeLocation(TEMPORARY_MERGE_LOCATION);
            this.getTemplateEngine().initialize(NAMESPACE);
        }
        return this.templateEngine;
    }

    /**
     * Stores the template engine exclusions.
     */
    private Map templateEngineExclusions = new LinkedHashMap();

    /**
     * Adds a template engine exclusion (these are the things that the template engine
     * will exclude when processing templates)
     *
     * @param path the path to the resulting output
     * @param patterns any patterns to which the conditions should apply
     */
    public void addTemplateEngineExclusion(
        final String path,
        final String patterns)
    {
        this.templateEngineExclusions.put(
            path,
            AndroMDAppUtils.stringToArray(patterns));
    }

    /**
     * Gets the template engine exclusions.
     *
     * @return the map of template engine exclusion paths and its patterns (if it has any defined).
     */
    final Map getTemplateEngineExclusions()
    {
        return this.templateEngineExclusions;
    }

    /**
     * The 'yes' response.
     */
    private static final String RESPONSE_YES = "yes";

    /**
     * The 'no' response.
     */
    private static final String RESPONSE_NO = "no";

    /**
     * A margin consisting of some whitespace.
     */
    private static final String MARGIN = "    ";

    /**
     * Stores the forward slash character.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * Processes the files for the project.
     *
     * @param write whether or not the resources should be written when collected.
     * @return processedResources
     * @throws Exception
     */
    protected List<File> processResources(boolean write)
        throws Exception
    {
        // - all resources that have been processed.
        final List<File> processedResources = new ArrayList<File>();
        final File rootDirectory = this.verifyRootDirectory(new File(this.getRoot()));
        final String bannerStart = write ? "G e n e r a t i n g" : "R e m o v i n g";
        this.printLine();
        this.printText(MARGIN + bannerStart + "   A n d r o M D A   P o w e r e d   A p p l i c a t i o n");
        this.printLine();
        rootDirectory.mkdirs();

        final Map locations = new LinkedHashMap();

        // - first write any mapped resources
        for (final Iterator iterator = this.resourceLocations.iterator(); iterator.hasNext();)
        {
            final String location = (String)iterator.next();
            final URL[] resourceDirectories = ResourceFinder.findResources(location);
            if (resourceDirectories != null)
            {
                final int numberOfResourceDirectories = resourceDirectories.length;
                for (int ctr = 0; ctr < numberOfResourceDirectories; ctr++)
                {
                    final URL resourceDirectory = resourceDirectories[ctr];
                    final List contents = ResourceUtils.getDirectoryContents(
                            resourceDirectory,
                            false,
                            null);
                    final Set newContents = new LinkedHashSet();
                    locations.put(
                        location,
                        newContents);
                    for (final ListIterator contentsIterator = contents.listIterator(); contentsIterator.hasNext();)
                    {
                        final String path = (String)contentsIterator.next();
                        if (path!=null && !path.endsWith(FORWARD_SLASH))
                        {
                            boolean hasNewPath = false;
                            for (final Iterator<Mapping> mappingIterator = this.mappings.iterator(); mappingIterator.hasNext();)
                            {
                                final Mapping mapping = mappingIterator.next();
                                String newPath = mapping.getMatch(path);
                                if (StringUtils.isNotEmpty(newPath))
                                {
                                    final URL absolutePath = ResourceUtils.getResource(path);
                                    if (absolutePath != null)
                                    {
                                        newPath =
                                            this.getTemplateEngine().getEvaluatedExpression(
                                                newPath,
                                                this.templateContext);
                                        ResourceWriter.instance().writeUrlToFile(
                                            absolutePath,
                                            ResourceUtils.normalizePath(TEMPORARY_MERGE_LOCATION + '/' + newPath));
                                        newContents.add(newPath);
                                        hasNewPath = true;
                                    }
                                }
                            }
                            if (!hasNewPath)
                            {
                                newContents.add(path);
                            }
                        }
                    }
                }
            }
        }

        // - second process and write any output from the defined resource locations.
        for (final Iterator<String> iterator = locations.keySet().iterator(); iterator.hasNext();)
        {
            final String location = (String)iterator.next();
            final Collection contents = (Collection)locations.get(location);
            if (contents != null)
            {
                for (final Iterator<String> contentsIterator = contents.iterator(); contentsIterator.hasNext();)
                {
                    final String path = (String)contentsIterator.next();
                    final String projectRelativePath = StringUtils.replace(
                            path,
                            location,
                            "");
                    if (this.isWriteable(projectRelativePath))
                    {
                        if (this.isValidTemplate(path))
                        {
                            final File outputFile =
                                new File(
                                    rootDirectory.getAbsolutePath(),
                                    this.trimTemplateExtension(projectRelativePath));
                            if (write)
                            {
                                final StringWriter writer = new StringWriter();
                                try
                                {
                                    this.getTemplateEngine().processTemplate(
                                        path,
                                        this.templateContext,
                                        writer);
                                }
                                catch (final Throwable throwable)
                                {
                                    throw new AndroMDAppException("An error occured while processing template --> '" +
                                        path + "' with template context '" + this.templateContext + "'", throwable);
                                }
                                writer.flush();
                                this.printText(MARGIN + "Output: '" + outputFile.toURI().toURL() + "'");
                                ResourceWriter.instance().writeStringToFile(
                                    writer.toString(),
                                    outputFile);
                            }
                            processedResources.add(outputFile);
                        }
                        else if (!path.endsWith(FORWARD_SLASH))
                        {
                            final File outputFile = new File(
                                    rootDirectory.getAbsolutePath(),
                                    projectRelativePath);

                            // - try the template engine merge location first
                            URL resource =
                                ResourceUtils.toURL(ResourceUtils.normalizePath(TEMPORARY_MERGE_LOCATION + '/' + path));
                            if (resource == null)
                            {
                                // - if we didn't find the file in the merge location, try the classpath
                                resource = ClassUtils.getClassLoader().getResource(path);
                            }
                            if (resource != null)
                            {
                                if (write)
                                {
                                    ResourceWriter.instance().writeUrlToFile(
                                        resource,
                                        outputFile.toString());
                                    this.printText(MARGIN + "Output: '" + outputFile.toURI().toURL() + "'");
                                }
                                processedResources.add(outputFile);
                            }
                        }
                    }
                }
            }
        }

        // - write any directories that are defined.
        for (final Iterator directoryIterator = this.directories.iterator(); directoryIterator.hasNext();)
        {
            final String directoryPath = ((String)directoryIterator.next()).trim();
            final File directory = new File(rootDirectory, directoryPath);
            if (this.isWriteable(directoryPath))
            {
                directory.mkdirs();
                this.printText(MARGIN + "Output: '" + directory.toURI().toURL() + "'");
            }
        }

        if (write)
        {
            // - write the "instructions can be found" information
            this.printLine();
            this.printText(MARGIN + "New application generated to --> '" + rootDirectory.toURI().toURL() + "'");
            if (StringUtils.isNotBlank(this.instructions))
            {
                File instructions = new File(
                        rootDirectory.getAbsolutePath(),
                        this.instructions);
                if (!instructions.exists())
                {
                    throw new AndroMDAppException("No instructions are available at --> '" + instructions +
                        "', please make sure you have the correct instructions defined in your descriptor --> '" +
                        this.resource + "'");
                }
                this.printText(MARGIN + "Instructions for your new application --> '" + instructions.toURI().toURL() + "'");
            }
            this.printLine();
        }
        return processedResources;
    }

    /**
     * Indicates whether or not this path is <em>writable</em>
     * based on the path and any output conditions that may be specified.
     *
     * @param path the path to check.
     * @return true/false
     */
    private boolean isWriteable(String path)
    {
        path = path.replaceAll(
                "\\\\+",
                FORWARD_SLASH);
        if (path.startsWith(FORWARD_SLASH))
        {
            path = path.substring(
                    1,
                    path.length());
        }

        Boolean writable = null;

        final Map evaluatedPaths = new LinkedHashMap();
        for (final Iterator iterator = this.outputConditions.iterator(); iterator.hasNext();)
        {
            final Conditions conditions = (Conditions)iterator.next();
            final Map outputPaths = conditions.getOutputPaths();
            final String conditionsType = conditions.getType();
            int ctr = 0;
            for (final Iterator pathIterator = outputPaths.keySet().iterator(); pathIterator.hasNext(); ctr++)
            {
                final String outputPath = (String)pathIterator.next();

                // - only evaluate if we haven't yet evaluated
                writable = (Boolean)evaluatedPaths.get(path);
                if (writable == null)
                {
                    if (path.startsWith(outputPath))
                    {
                        final String[] patterns = (String[])outputPaths.get(outputPath);
                        if (ResourceUtils.matchesAtLeastOnePattern(
                                path,
                                patterns))
                        {
                            // - assume writable is false, since the path matches at least one conditions path.
                            for (final Iterator conditionIterator = conditions.getConditions().iterator();
                                conditionIterator.hasNext();)
                            {
                                final Condition condition = (Condition)conditionIterator.next();
                                final String id = condition.getId();
                                if (StringUtils.isNotBlank(id))
                                {
                                    final boolean result = condition.evaluate(this.templateContext.get(id));
                                    writable = Boolean.valueOf(result);
                                    if (Conditions.TYPE_AND.equals(conditionsType) && !result)
                                    {
                                        // - if we 'and' the conditions, we break at the first false
                                        break;
                                    }
                                    else if (Conditions.TYPE_OR.equals(conditionsType) && result)
                                    {
                                        // - otherwise we break at the first true condition
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (writable != null)
                    {
                        evaluatedPaths.put(
                            path,
                            writable);
                    }
                }
            }
        }

        // - if writable is still null, set to true
        if (writable == null)
        {
            writable = Boolean.TRUE;
        }
        return writable.booleanValue();
    }

    /**
     * Indicates whether or not the given <code>path</code> matches at least
     * one of the file extensions stored in the {@link #templateExtensions}
     * and isn't in the template engine exclusions.
     *
     * @param path the path to check.
     * @return true/false
     */
    private boolean isValidTemplate(final String path)
    {
        boolean exclude = false;
        final Map exclusions = this.getTemplateEngineExclusions();
        for (final Iterator pathIterator = exclusions.keySet().iterator(); pathIterator.hasNext();)
        {
            final String exclusionPath = (String)pathIterator.next();
            if (path.startsWith(exclusionPath))
            {
                final String[] patterns = (String[])exclusions.get(exclusionPath);
                exclude = ResourceUtils.matchesAtLeastOnePattern(
                    exclusionPath,
                    patterns);
                if (exclude)
                {
                    break;
                }
            }
        }
        boolean validTemplate = false;
        if (!exclude)
        {
            if (this.templateExtensions != null)
            {
                final int numberOfExtensions = this.templateExtensions.length;
                for (int ctr = 0; ctr < numberOfExtensions; ctr++)
                {
                    final String extension = '.' + this.templateExtensions[ctr];
                    validTemplate = path.endsWith(extension);
                    if (validTemplate)
                    {
                        break;
                    }
                }
            }
        }
        return validTemplate;
    }

    /**
     * Trims the first template extension it encounters and returns.
     *
     * @param path the path of which to trim the extension.
     * @return the trimmed path.
     */
    private String trimTemplateExtension(String path)
    {
        if (this.templateExtensions != null)
        {
            final int numberOfExtensions = this.templateExtensions.length;
            for (int ctr = 0; ctr < numberOfExtensions; ctr++)
            {
                final String extension = '.' + this.templateExtensions[ctr];
                if (path.endsWith(extension))
                {
                    path = path.substring(
                            0,
                            path.length() - extension.length());
                    break;
                }
            }
        }
        return path;
    }

    /**
     * Prints a line separator.
     */
    private void printLine()
    {
        this.printText("-------------------------------------------------------------------------------------");
    }

    /**
     * Verifies that if the root directory already exists, the user is prompted
     * to make sure its ok if we generate over it, otherwise the user can change
     * his/her application directory.
     *
     * @param rootDirectory the root directory that will be verified.
     * @return the appropriate root directory.
     */
    private File verifyRootDirectory(final File rootDirectory)
    {
        File applicationRoot = rootDirectory;
        if (rootDirectory.exists() && !this.isOvewrite())
        {
            this.printPromptText(
                "'" + rootDirectory.getAbsolutePath() +
                "' already exists, would you like to try a new name? [yes, no]: ");
            String response = this.readLine();
            while (!RESPONSE_YES.equals(response) && !RESPONSE_NO.equals(response))
            {
                response = this.readLine();
            }
            if (RESPONSE_YES.equals(response))
            {
                this.printPromptText("Please enter the name for your application root directory: ");
                String rootName;
                do
                {
                    rootName = this.readLine();
                }
                while (StringUtils.isBlank(rootName));
                applicationRoot = this.verifyRootDirectory(new File(rootName));
            }
        }
        return applicationRoot;
    }

    /**
     * Indicates whether or not this andromdapp type should overwrite any
     * previous applications with the same name.  This returns true on the first
     * configuration that has that flag set to true.
     *
     * @return true/false
     */
    private boolean isOvewrite()
    {
        boolean overwrite = false;
        if (this.configurations != null)
        {
            for (final Iterator iterator = this.configurations.iterator(); iterator.hasNext();)
            {
                final Configuration configuration = (Configuration)iterator.next();
                overwrite = configuration.isOverwrite();
                if (overwrite)
                {
                    break;
                }
            }
        }
        return overwrite;
    }

    /**
     * Prints text to the console.
     *
     * @param text the text to print to the console;
     */
    private void printPromptText(final String text)
    {
        System.out.println();
        this.printText(text);
    }

    /**
     * Prints text to the console.
     *
     * @param text the text to print to the console;
     */
    private void printText(final String text)
    {
        System.out.println(text);
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
        String inputString;
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
     * The type of this AndroMDAppType (i.e. 'j2ee', '.net', etc).
     */
    private String type;

    /**
     * Gets the type of this AndroMDAppType.
     *
     * @return Returns the type.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Sets the type of this AndroMDAppType.
     *
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * The root directory in which the application will be created.
     */
    private String root;

    /**
     * Gets the application root directory name.
     *
     * @return Returns the root.
     */
    public String getRoot()
    {
        return this.root;
    }

    /**
     * Sets the application root directory name.
     *
     * @param root The root to set.
     */
    public void setRoot(String root)
    {
        this.root = root;
    }

    /**
     * Stores any configuration information used when running this type.
     */
    private List<Configuration> configurations;

    /**
     * Sets the configuration instance for this type.
     *
     * @param configuration the optional configuration instance.
     */
    final void setConfigurations(final List<Configuration> configurations)
    {
        this.configurations = configurations;
    }

    /**
     * Stores the available prompts for this andromdapp.
     */
    private final List<Prompt> prompts = new ArrayList<Prompt>();

    /**
     * Adds a prompt to the collection of prompts contained within this
     * instance.
     *
     * @param prompt the prompt to add.
     */
    public void addPrompt(final Prompt prompt)
    {
        this.prompts.add(prompt);
    }

    /**
     * Gets all available prompts.
     *
     * @return the list of prompts.
     */
    public List<Prompt> getPrompts()
    {
        return this.prompts;
    }

    /**
     * The locations where templates are stored.
     */
    private List<String> resourceLocations = new ArrayList<String>();

    /**
     * Adds a location where templates and or project files are located.
     *
     * @param resourceLocation the path to location.
     */
    public void addResourceLocation(final String resourceLocation)
    {
        this.resourceLocations.add(resourceLocation);
    }

    /**
     * The any empty directories that should be created when generating the
     * application.
     */
    private List<String> directories = new ArrayList<String>();

    /**
     * The relative path to the directory to be created.
     *
     * @param directory the path to the directory.
     */
    public void addDirectory(final String directory)
    {
        this.directories.add(directory);
    }

    /**
     * Stores the output conditions (that is the conditions
     * that must apply for the defined output to be written).
     */
    private List<Conditions> outputConditions = new ArrayList<Conditions>();

    /**
     * Adds an conditions element to the output conditions..
     * @param outputConditions the output conditions to add.
     */
    public void addOutputConditions(final Conditions outputConditions)
    {
        this.outputConditions.add(outputConditions);
    }

    /**
     * Stores the patterns of the templates that the template engine should
     * process.
     */
    private String[] templateExtensions;

    /**
     * @param templateExtensions The templateExtensions to set.
     */
    public void setTemplateExtensions(final String templateExtensions)
    {
        this.templateExtensions = AndroMDAppUtils.stringToArray(templateExtensions);
    }

    /**
     * The path to the instructions on how to operation the build of the new
     * application.
     */
    private String instructions;

    /**
     * Sets the path to the instructions (i.e.could be a path to a readme file).
     *
     * @param instructions the path to the instructions.
     */
    public void setInstructions(final String instructions)
    {
        this.instructions = instructions;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getType() + "]";
    }

    /**
     * The resource that configured this AndroMDAppType instance.
     */
    URL resource;

    /**
     * Sets the resource that configured this AndroMDAppType instance.
     *
     * @param resource the resource.
     */
    final void setResource(final URL resource)
    {
        this.resource = resource;
    }

    /**
     * Gets the resource that configured this instance.
     *
     * @return the resource.
     */
    final URL getResource()
    {
        return this.resource;
    }

    /**
     * Stores any of the mappings available to this type.
     */
    private List<Mapping> mappings = new ArrayList<Mapping>();

    /**
     * Adds a new mapping to this type.
     *
     * @param mapping the mapping which maps the new output paths.
     */
    public void addMapping(final Mapping mapping)
    {
        this.mappings.add(mapping);
    }

    /**
     * Adds the given map of properties to the current template context.
     *
     * @param map the map of properties.
     */
    final void addToTemplateContext(final Map map)
    {
        this.templateContext.putAll(map);
    }

    /**
     * Gets the current template context for this instance.
     *
     * @return the template context.
     */
    final Map getTemplateContext()
    {
        return this.templateContext;
    }

    /**
     * Instantiates the template object with the given <code>className</code> and adds
     * it to the current template context.
     *
     * @param name the name of the template variable.
     * @param className the name of the class to instantiate.
     */
    public void addTemplateObject(
        final String name,
        final String className)
    {
        this.templateContext.put(
            name,
            ClassUtils.newInstance(className));
    }
}