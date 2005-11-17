package org.andromda.andromdapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.ObjectUtils;


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
            this.processTemplates();
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
                    String response = this.promptForInput(prompt);
                    while (!prompt.isValidResponse(response))
                    {
                        response = this.promptForInput(prompt);
                    }

                    this.templateContext.put(
                        id,
                        response);
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
        this.printText(prompt.getText());
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
     * Processes the templates found in the templatesLocation.
     */
    private void processTemplates()
    {
        for (final Iterator iterator = this.templateLocations.iterator(); iterator.hasNext();)
        {
            final String location = (String)iterator.next();
            final URL[] resourceDirectories = ResourceFinder.findResources(location);
            if (resourceDirectories != null)
            {
                final int numberOfResourceDirectories = resourceDirectories.length;
                for (int ctr = 0; ctr < numberOfResourceDirectories; ctr++)
                {
                    final List contents = ResourceUtils.getDirectoryContents(
                            resourceDirectories[ctr],
                            false,
                            null);
                    for (final Iterator contentsIterator = contents.iterator(); contentsIterator.hasNext();)
                    {
                        final String path = (String)contentsIterator.next();
                    }
                }
            }
        }
    }

    /**
     * Prints text to the console.
     *
     * @param text the text to print to the console;
     */
    private void printText(final String text)
    {
        System.out.println();
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
        return type;
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
    private List templateLocations = new ArrayList();

    /**
     * Adds a location which to find templates.
     *
     * @param templateLocation the template location to add.
     */
    public void addTemplateLocation(final String templateLocation)
    {
        this.templateLocations.add(templateLocation);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getType() + "]";
    }
}