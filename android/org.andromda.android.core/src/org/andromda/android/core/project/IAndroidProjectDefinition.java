package org.andromda.android.core.project;

/**
 * This interface provides access to the project definition for Android.
 * 
 * @author Peter Friese
 * @since 23.10.2005
 */
public interface IAndroidProjectDefinition
{

    String getConfigurationLocation();

    /**
     * Updates the location for the AndroMDA configuration file(s).
     * 
     * @param configurationLocation The new location. Must be withing the project.
     */
    void setConfigurationLocation(String configurationLocation);

}
