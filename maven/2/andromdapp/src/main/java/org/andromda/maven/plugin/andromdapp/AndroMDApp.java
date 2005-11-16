package org.andromda.maven.plugin.andromdapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang.ObjectUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;


/**
 * Represents an instance of the AndroMDA application
 * generator.
 *
 * @author Chad Brandon
 */
public class AndroMDApp
{
    /**
     * Runs the AndroMDApp generation process.
     */
    public void run()
    {
        try
        {
            this.initialize();
            this.promptUser();
        }
        catch (final Throwable throwable)
        {
            throw new AndroMDAppException(throwable);
        }
    }

    /**
     * The name of the AndroMDApp descriptor.
     */
    private static final String DESCRIPTOR = "andromdapp.xml";

    /**
     * The directory in which the descriptors are kept.
     */
    private static final String DESCRIPTOR_DIRECTORY = "META-INF/andromdapp";

    /**
     * The velocity template context.
     */
    private VelocityContext templateContext = null;

    /**
     * All types of discovered AndroMDApps
     */
    private Map types = new LinkedHashMap();

    /**
     * Performs any required initialization.
     * @throws Exception
     */
    private void initialize()
        throws Exception
    {
        final URL[] descriptorDirectories = ResourceFinder.findResources(DESCRIPTOR_DIRECTORY);
        if (descriptorDirectories != null && descriptorDirectories.length > 0)
        {
            final int numberOfDescriptorDirectories = descriptorDirectories.length;
            for (int ctr = 0; ctr < numberOfDescriptorDirectories; ctr++)
            {
                final List directoryContents =
                    ResourceUtils.getDirectoryContents(
                        descriptorDirectories[ctr],
                        true,
                        null);
                for (final Iterator iterator = directoryContents.iterator(); iterator.hasNext();)
                {
                    final String uri = (String)iterator.next();
                    if (uri.replaceAll(
                            ".*(\\+|/)",
                            "").equals(DESCRIPTOR))
                    {
                        final XmlObjectFactory factory = XmlObjectFactory.getInstance(AndroMDApp.class);
                        final AndroMDApp andromdapp = (AndroMDApp)factory.getObject(ResourceUtils.toURL(uri));
                        this.types.put(
                            andromdapp.getType(),
                            andromdapp);
                    }
                }
            }
        }

        final Properties properties = new Properties();
        properties.put(
            VelocityEngine.RESOURCE_LOADER,
            "classpath");
        properties.put(
            "classpath." + VelocityEngine.RESOURCE_LOADER + ".class",
            ClasspathResourceLoader.class.getName());

        Velocity.init(properties);
        this.templateContext = new VelocityContext();
    }

    /**
     * Prompts the user for the input required to generate an application with the correct information.
     */
    private void promptUser()
    {
        AndroMDApp andromdapp = null;
        if (this.types.size() > 1)
        {
            final List types = new ArrayList();
            final StringBuffer typesChoice = new StringBuffer("[");
            for (final Iterator iterator = this.types.keySet().iterator(); iterator.hasNext();)
            {
                final AndroMDApp type = (AndroMDApp)iterator.next();
                typesChoice.append(type.getType());
                types.add(types);
                if (iterator.hasNext())
                {
                    typesChoice.append("|");
                }
            }
            this.printText("Please choose the type of application to generate " + typesChoice);
            String selectedType = this.readLine();
            while (!types.contains(selectedType))
            {
                selectedType = this.readLine();
            }
            andromdapp = (AndroMDApp)this.types.get(selectedType);
        }
        else if (!this.types.isEmpty())
        {
            andromdapp = (AndroMDApp)((Map.Entry)this.types.entrySet().iterator().next()).getValue();
        }
        else
        {
            throw new AndroMDAppException("No '" + DESCRIPTOR + "' descriptor files could be found");
        }

        for (final Iterator iterator = andromdapp.prompts.iterator(); iterator.hasNext();)
        {
            final Prompt prompt = (Prompt)iterator.next();
            final String id = prompt.getId();
            
            boolean validPreconditions = true;
            for (final Iterator preconditionIterator = prompt.getPreconditions().iterator(); preconditionIterator.hasNext();)
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
     * The type of this AndroMDApp (i.e. 'j2ee', '.net', etc).
     */
    private String type;

    /**
     * Gets the type of this AndroMDApp.
     *
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of this AndroMDApp.
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
    private List prompts = new ArrayList();

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
}