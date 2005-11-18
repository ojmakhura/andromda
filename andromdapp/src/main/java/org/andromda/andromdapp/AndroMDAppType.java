package org.andromda.andromdapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
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
    private Map templateContext = null;

    /**
     * Performs any required initialization for the type.
     *
     * @throws Exception
     */
    private void initialize()
        throws Exception
    {
        this.getTemplateEngine().initialize(NAMESPACE);
        this.templateContext = new LinkedHashMap();
    }

    /**
     * Runs the AndroMDApp generation process.
     */
    public void run()
    {
        try
        {
            this.initialize();
            this.promptUser();
            this.processResources();
        }
        catch (final Throwable throwable)
        {
            throw new AndroMDAppException(throwable);
        }
    }

    /**
     * The namespace used to initialize the template engine.
     */
    private static final String NAMESPACE = "andromdapp";

    /**
     * Prompts the user for the input required to generate an application with the correct information.
     */
    private void promptUser()
    {
        for (final Iterator iterator = this.prompts.iterator(); iterator.hasNext();)
        {
            final Prompt prompt = (Prompt)iterator.next();
            final String id = prompt.getId();

            boolean validPreconditions = true;
            for (final Iterator preconditionIterator = prompt.getPreconditions().iterator();
                preconditionIterator.hasNext();)
            {
                final Condition precondition = (Condition)preconditionIterator.next();
                final String value = ObjectUtils.toString(this.templateContext.get(precondition.getId()));
                final String equalCondition = precondition.getEqual();
                if (equalCondition != null && !equalCondition.equals(value))
                {
                    validPreconditions = false;
                    break;
                }
                final String notEqualCondition = precondition.getNotEqual();
                if (notEqualCondition != null && notEqualCondition.equals(value))
                {
                    validPreconditions = false;
                    break;
                }
            }

            if (validPreconditions)
            {
                // - only prompt when the id isn't already in the context
                if (!this.templateContext.containsKey(id))
                {
                    String response;
                    do
                    {
                        response = this.promptForInput(prompt);
                    }
                    while (!prompt.isValidResponse(response));

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
        }
    }

    /**
     * Prompts the user for the information contained in the given <code>prompt</code>.
     *
     * @param prompt the prompt from which to format the prompt text.
     * @return the response of the prompt.
     */
    private String promptForInput(final Prompt prompt)
    {
        this.printPromptText(prompt.getText());
        final String input = this.readLine();
        for (final Iterator iterator = prompt.getConditions().iterator(); iterator.hasNext();)
        {
            final Condition condition = (Condition)iterator.next();
            final String equalCondition = condition.getEqual();
            if (equalCondition != null && equalCondition.equals(input))
            {
                this.setPromptValues(condition);
            }
            final String notEqualCondition = condition.getNotEqual();
            if (notEqualCondition != null && !notEqualCondition.equals(input))
            {
                this.setPromptValues(condition);
            }
        }
        return input;
    }

    /**
     * Sets the prompt values from the given <code>condition</code>.
     *
     * @param condition the condition from which to populate the values.
     */
    private void setPromptValues(final Condition condition)
    {
        if (condition != null)
        {
            final Map values = condition.getPromptValues();
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
     * @param templateEngineClass the Class of the template engine implementation.
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
     */
    private TemplateEngine getTemplateEngine()
    {
        if (this.templateEngine == null)
        {
            this.templateEngine =
                (TemplateEngine)ComponentContainer.instance().newComponent(
                    this.templateEngineClass,
                    TemplateEngine.class);
        }
        return this.templateEngine;
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
     * Processes the files for the project.
     * @throws Exception 
     */
    private void processResources() throws Exception
    {
        final File rootDirectory =
            this.verifyRootDirectory(
                new File(this.getTemplateEngine().getEvaluatedExpression(
                        this.getRoot(),
                        this.templateContext)));
        this.printLine();
        this.printText(MARGIN + "G e n e r a t i n g   A n d r o M D A   P o w e r e d   A p p l i c a t i o n");
        this.printLine();
        rootDirectory.mkdirs();
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
                    for (final Iterator contentsIterator = contents.iterator(); contentsIterator.hasNext();)
                    {
                        final String path = (String)contentsIterator.next();
                        String projectRelativePath = StringUtils.replace(path, location, "");
                        if (this.hasTemplateExtension(path))
                        {
                            final File outputFile = new File(rootDirectory.getAbsolutePath(), this.trimTemplateExtension(projectRelativePath));
                            final StringWriter writer = new StringWriter();
                            this.getTemplateEngine().processTemplate(path, this.templateContext, writer);
                            writer.flush();
                            ResourceWriter.instance().writeStringToFile(writer.toString(), outputFile);
                            this.printText(MARGIN + "Output : '" + outputFile.toURL() + "'");
                        }
                        else if(!path.endsWith("/"))
                        {
                            final File outputFile = new File(rootDirectory.getAbsolutePath(), projectRelativePath);
                            final URL resource = ClassUtils.getClassLoader().getResource(path);
                            if (resource != null)
                            {
                                ResourceWriter.instance().writeUrlToFile(resource, outputFile.toString());
                                this.printText(MARGIN + "Output : '" + outputFile.toURL() + "'");
                            }
                        }
                    }
                    for (final Iterator directoryIterator = this.directories.iterator(); directoryIterator.hasNext();)
                    {
                        final File directory = new File(rootDirectory, (String)directoryIterator.next());
                        directory.mkdirs();
                        this.printText(MARGIN + "Output : '" + directory.toURL() + "'");
                    }
                }
            }
        }
        this.printLine();
        this.printText(MARGIN + "New application generated to --> '" + rootDirectory.toURL() + "'");
        this.printLine();
    }
    
    /**
     * Indicates whether or not the given <code>path</code>
     * matches at least one of the file extensions stored in the
     * {@link #templateExtensions}.
     * 
     * @param path the path to check.
     * @return true/false
     */
    private boolean hasTemplateExtension(final String path)
    {
        boolean hasTemplateExtension = false;
        if (this.templateExtensions != null)
        {
            final int numberOfExtensions = this.templateExtensions.length;
            for (int ctr = 0; ctr < numberOfExtensions; ctr++)
            {
                final String extension = '.' + this.templateExtensions[ctr];
                if (extension != null && path.endsWith(extension))
                {
                    hasTemplateExtension = true;
                    break;
                }
            }
        }
        return hasTemplateExtension;
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
                final String extension =  '.' +this.templateExtensions[ctr];
                if (extension != null && path.endsWith(extension))
                {
                    path = path.substring(0, path.length() - extension.length());
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
     * to make sure its ok if we generate over it, otherwise the user can change his/her
     * application directory.
     *
     * @param rootDirectory the root directory that will be verified.
     * @return the appropriate root directory.
     */
    private File verifyRootDirectory(final File rootDirectory)
    {
        File applicationRoot = rootDirectory;
        if (rootDirectory.exists())
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
                String rootName = this.readLine();
                applicationRoot = this.verifyRootDirectory(new File(rootName));
            }
        }
        return applicationRoot;
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
     * Stores the available prompts for this andromdapp.
     */
    private final List prompts = new ArrayList();

    /**
     * Adds a prompt to the collection of prompts
     * contained within this instance.
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
    public List getPrompts()
    {
        return this.prompts;
    }

    /**
     * The locations where templates are stored.
     */
    private List resourceLocations = new ArrayList();

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
     * Stores the patterns of the templates that the template engine should
     * process.
     */
    private String[] templateExtensions;

    /**
     * @param templateExtensions The templateExtensions to set.
     */
    public void setTemplateExtensions(String templateExtensions)
    {
        this.templateExtensions = templateExtensions != null ? templateExtensions.split(EXTENSION_DELIMITER) : null;
    }

    /**
     * The delimiter for seperating location patterns.
     */
    private static final String EXTENSION_DELIMITER = ",";
    
    /**
     * The any empty directories that should be created when generating the application.
     */
    private List directories = new ArrayList();
    
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
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getType() + "]";
    }
}