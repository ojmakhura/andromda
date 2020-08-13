package org.andromda.andromdapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents an instance of the AndroMDA application
 * generator.
 *
 * @author Chad Brandon
 */
public class AndroMDApp
{
    /**
     * An AndroMDApp configuration that contains some internal configuration information (like the AndroMDA
     * version, and other common properties).
     */
    private static final String INTERNAL_CONFIGURATION_URI = "META-INF/andromdapp/configuration.xml";

    /**
     * Runs the AndroMDApp generation process.
     */
    public void run()
    {
        try
        {
            AndroMDALogger.initialize();
            final URL internalConfiguration = ResourceUtils.getResource(INTERNAL_CONFIGURATION_URI);
            if (internalConfiguration == null)
            {
                throw new AndroMDAppException("No configuration could be loaded from --> '" +
                    INTERNAL_CONFIGURATION_URI + '\'');
            }
            this.addConfigurationUri(internalConfiguration.toString());
            this.initialize();
            this.chooseTypeAndRun(true);
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof AndroMDAppException)
            {
                throw (AndroMDAppException)throwable;
            }
            throw new AndroMDAppException(throwable);
        }
    }

    /**
     * The name of the AndroMDApp descriptor.
     */
    static final String DESCRIPTOR = "andromdapp.xml";

    /**
     * The directory in which the descriptors are kept.
     */
    private static final String DESCRIPTOR_DIRECTORY = "META-INF/andromdapp";

    /**
     * All types of discovered AndroMDApps
     */
    private final Map<String, AndroMDAppType> types = new LinkedHashMap<String, AndroMDAppType>();

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
                final List<String> directoryContents =
                    ResourceUtils.getDirectoryContents(
                        descriptorDirectories[ctr],
                        true,
                        null);
                for (final String uri : directoryContents)
                {
                    if (uri.replaceAll(".*(\\\\+|/)","").equals(DESCRIPTOR))
                    {
                        final XmlObjectFactory factory = XmlObjectFactory.getInstance(AndroMDApp.class);
                        final URL descriptorUri = ResourceUtils.toURL(uri);
                        final AndroMDAppType andromdapp = (AndroMDAppType) factory.getObject(descriptorUri);
                        andromdapp.setResource(descriptorUri);
                        final String type = andromdapp.getType();
                        AndroMDALogger.info("discovered andromdapp type --> '" + type + '\'');
                        this.types.put(
                                type,
                                andromdapp);
                    }
                }
            }
        }
    }

    /**
     * Stores the optional configuration instance.
     */
    private final List<Configuration> configurations = new ArrayList<Configuration>();

    /**
     * Adds the URI for an optional configuration  These are useful if you want
     * to preconfigure the andromdapp when any properties, etc.
     *
     * @param configurationUri the URI to the configuration.
     */
    public void addConfigurationUri(final String configurationUri)
    {
        if (StringUtils.isNotBlank(configurationUri))
        {
            final XmlObjectFactory factory = XmlObjectFactory.getInstance(Configuration.class);
            final URL configurationUrl = ResourceUtils.toURL(configurationUri);
            if (configurationUrl == null)
            {
                throw new AndroMDAppException("configuriationUri is invalid --> '" + configurationUri + '\'');
            }
            this.configurations.add((Configuration)factory.getObject(configurationUrl));
        }
    }

    /**
     * Adds the configuration contents stored as a String.
     *
     * @param configuration the configuration contents as a string.
     */
    public void addConfiguration(final String configuration)
    {
        if (StringUtils.isNotBlank(configuration))
        {
            final XmlObjectFactory factory = XmlObjectFactory.getInstance(Configuration.class);
            this.configurations.add((Configuration)factory.getObject(configuration));
        }
    }

    /**
     * Prompts the user to choose the type of application, and then runs that AndroMDAppType.
     * @throws Exception
     */
    private List<File> chooseTypeAndRun(final boolean write)
        throws Exception
    {

        if (this.types.isEmpty())
        {
            throw new AndroMDAppException("No '" + DESCRIPTOR + "' descriptor files could be found");
        }
        final Map<String, String> properties = new LinkedHashMap<String, String>();
        for (Configuration configuration : this.configurations)
        {
            properties.putAll(configuration.getAllProperties());
        }
        final String applicationType = properties.get(APPLICATION_TYPE);
        final Set<String> validTypes = this.types.keySet();
        AndroMDAppType andromdapp = this.types.get(applicationType);
        if (andromdapp == null)
        {
            if (this.types.size() > 1)
            {
                final StringBuilder typesChoice = new StringBuilder("[");
                for (final Iterator<String> iterator = validTypes.iterator(); iterator.hasNext();)
                {
                    final String type = iterator.next();
                    typesChoice.append(type);
                    if (iterator.hasNext())
                    {
                        typesChoice.append(", ");
                    }
                }
                typesChoice.append(']');
                this.printText("Please choose the type of application to generate " + typesChoice);
                String selectedType = this.readLine();
                while (!this.types.containsKey(selectedType))
                {
                    selectedType = this.readLine();
                }
                andromdapp = this.types.get(selectedType);
            }
            else if (!this.types.isEmpty())
            {
                andromdapp = this.types.entrySet().iterator().next().getValue();
            }
        }

        if (andromdapp == null)
        {
            throw new AndroMDAppException("AndromdaPP is null " + this.types.entrySet());
        }

        andromdapp.setConfigurations(this.configurations);
        andromdapp.initialize();

        final Map templateContext = andromdapp.getTemplateContext();

        final XmlObjectFactory factory = XmlObjectFactory.getInstance(AndroMDApp.class);
        final String contents = andromdapp.promptUser();
        // - evaluate all properties in the descriptor and recreate the AndroMDAppType
        andromdapp = (AndroMDAppType)factory.getObject(contents);
        andromdapp.setConfigurations(this.configurations);
        andromdapp.addToTemplateContext(templateContext);
        return andromdapp.processResources(write);
    }

    /**
     * Identifies the AndroMDApp type (used to override the prompting of the type).
     */
    private static final String APPLICATION_TYPE = "andromdappType";

    /**
     * Removes all structure generated from the previous run.
     */
    public void clean()
    {
        try
        {
            AndroMDALogger.initialize();
            this.initialize();
            final List<File> list = this.chooseTypeAndRun(false);
            for (final File file : list)
            {
                this.deleteFile(file);
            }
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof AndroMDAppException)
            {
                throw (AndroMDAppException)throwable;
            }
            throw new AndroMDAppException(throwable);
        }
    }

    /**
     * Deletes the given file and any empty parent directories
     * that the file might be contained within.
     *
     * @param file the file to remove.
     * @throws MalformedURLException
     */
    private void deleteFile(final File file) throws MalformedURLException
    {
        if (file != null && file.exists())
        {
            final File[] children = file.listFiles();
            if (children == null || children.length == 0)
            {
                if (file.delete())
                {
                    AndroMDALogger.info("Removed: '" + file.toURI().toURL() + '\'');
                }
                this.deleteFile(file.getParentFile());
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
        System.out.println();  // NOPMD - have to print to console prompt
        System.out.println(text);  // NOPMD - have to print to console prompt
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
            AndroMDALogger.info("AndroMDApp Error reading inputLine from System.in");
        }
        return StringUtils.trimToNull(inputString);
    }
}
