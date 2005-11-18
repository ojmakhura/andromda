package org.andromda.andromdapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;


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
            AndroMDALogger.initialize();
            this.initialize();
            this.chooseTypeAndRun();
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
                            ".*(\\\\+|/)",
                            "").equals(DESCRIPTOR))
                    {
                        final XmlObjectFactory factory = XmlObjectFactory.getInstance(AndroMDApp.class);
                        final URL descriptorUri = ResourceUtils.toURL(uri);
                        final AndroMDAppType andromdapp = (AndroMDAppType)factory.getObject(descriptorUri);
                        andromdapp.setResource(descriptorUri);
                        final String type = andromdapp.getType();
                        AndroMDALogger.info("discovered andromdapp type --> '" + type + "'");
                        this.types.put(
                            type,
                            andromdapp);
                    }
                }
            }
        }
    }

    /**
     * Prompts the user to choose the type of application, and then runs that AndroMDAppType.
     */
    private void chooseTypeAndRun()
    {
        AndroMDAppType andromdapp = null;
        if (this.types.size() > 1)
        {
            final StringBuffer typesChoice = new StringBuffer("[");
            for (final Iterator iterator = this.types.keySet().iterator(); iterator.hasNext();)
            {
                final String type = (String)iterator.next();
                typesChoice.append(type);
                if (iterator.hasNext())
                {
                    typesChoice.append(", ");
                }
            }
            typesChoice.append("]");
            this.printText("Please choose the type of application to generate " + typesChoice);
            String selectedType = this.readLine();
            while (!this.types.containsKey(selectedType))
            {
                selectedType = this.readLine();
            }
            andromdapp = (AndroMDAppType)this.types.get(selectedType);
        }
        else if (!this.types.isEmpty())
        {
            andromdapp = (AndroMDAppType)((Map.Entry)this.types.entrySet().iterator().next()).getValue();
        }
        else
        {
            throw new AndroMDAppException("No '" + DESCRIPTOR + "' descriptor files could be found");
        }

        andromdapp.run();
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
}