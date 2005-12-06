package org.andromda.android.core.internal.project;

import org.andromda.android.core.internal.settings.AndroidSettingsAccess;
import org.andromda.android.core.project.IAndroidProjectDefinition;
import org.eclipse.core.resources.IProject;

/**
 * The definition of an Android project.
 * 
 * @author Peter Friese
 * @since 11.10.2005
 */
public class AndroidProjectDefinition
        implements IAndroidProjectDefinition
{
    /** Setting name for the location of the AndroMDA configuration. */
    private static final String CONFIGURATION_LOCATION = "configuration.location";

    private final IProject project;

    /**
     * Create a new definition object and load the definition for the given project.
     * 
     * @param project the project to load the definition for.
     */
    public AndroidProjectDefinition(IProject project)
    {
        super();
        this.project = project;
    }

    /**
     * @return the location of the AndroMDA configuration file.
     */
    public String getConfigurationLocation()
    {
        return AndroidSettingsAccess.instance().getConfigurationLocation(project);
    }

    /**
     * @see org.andromda.android.core.project.IAndroidProjectDefinition#setConfigurationLocation(java.lang.String)
     */
    public void setConfigurationLocation(String configurationLocation)
    {
        AndroidSettingsAccess.instance().setConfigurationsLocation(project, configurationLocation);
    }

    /**
     * @see org.andromda.android.core.project.IAndroidProjectDefinition#getAndroMDACartridgesLocation()
     */
    public String getAndroMDACartridgesLocation()
    {
        return AndroidSettingsAccess.instance().getAndroMDACartridgesLocation();
    }

}
