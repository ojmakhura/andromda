package org.andromda.android.core.internal.project;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.andromda.android.core.project.IAndroidProjectDefinition;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * The definition of an Android project.
 *
 * @author Peter Friese
 * @since 11.10.2005
 */
public class AndroidProjectDefinition implements IAndroidProjectDefinition
{
    /** Setting name for the location of te AndroMDA configuration. */
    private static final String CONFIGURATION_LOCATION = "configuration.location";

    /** Name of the Android project definition file. */
    private static final String ANDROID_FILENAME = ".android";

    /** Header for the configuration file. */
    private static final String ANDROID_CONFIG_HEADER = "Android project definition file.";

    /** This properties collection holds the project definition loaded from the definition file. */
    private Properties definition;

    /** The file backing the project definition. */
    private IFile definitionFile;

    /**
     * Create a new definition object and load the definition for the given project.
     *
     * @param project the project to load the definition for.
     */
    public AndroidProjectDefinition(IProject project)
    {
        super();
        load(project);
    }

    /**
     * Load the configuration for the given project.
     *
     * @param project the project to load the configuration for.
     */
    public void load(IProject project)
    {
        definition = new Properties();

        initializeProperties(definition);
        definitionFile = project.getFile(ANDROID_FILENAME);

        try
        {
            if (!definitionFile.exists())
            {
                save();
            }
            definition.load(definitionFile.getContents());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Save the configuration to the config file.
     */
    public void save()
    {
        try
        {
            definition.store(new FileOutputStream(definitionFile.getLocation().toFile()), ANDROID_CONFIG_HEADER);
            definitionFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }

    }

    protected void initializeProperties(Properties properties)
    {
        properties.setProperty(CONFIGURATION_LOCATION, "mda/conf/andromda.xml");
    }

    /**
     * @return the location of the AndroMDA configuration file.
     */
    public String getConfigurationLocation()
    {
        return definition.getProperty(CONFIGURATION_LOCATION);
    }

}
