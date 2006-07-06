package org.andromda.android.core.internal.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.settings.IAndroidSettings;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Provides acces to all settings concerning Android.
 * 
 * @author Peter Friese
 * @since 05.12.2005
 */
public final class AndroidSettings
        implements IAndroidSettings
{

    /** Access to the preference service. */
    private IPreferencesService preferencesService;

    /** Singleton instance. */
    private static final AndroidSettings instance = new AndroidSettings();

    /**
     * Creates a new AndroidSettingsAccess.
     */
    private AndroidSettings()
    {
        super();
        preferencesService = Platform.getPreferencesService();
    }

    /**
     * Returns the singleton instance.
     * 
     * @return The singleton instance.
     */
    public static AndroidSettings instance()
    {
        return instance;
    }

    /**
     * Saves a preference node.
     * 
     * @param node The node to save.
     * @return result of save request.
     */
    private boolean saveNode(final Preferences node)
    {
        try
        {
            node.flush();
            return true;
        }
        catch (BackingStoreException e)
        {
            AndroidCore.log(e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getConfigurationLocation(final IProject project)
    {
        IScopeContext[] contexts = null;
        if (project != null)
        {
            contexts = new IScopeContext[] { new ProjectScope(project) };
        }
        return preferencesService.getString(AndroidCore.PLUGIN_ID, CONFIGURATION_LOCATION, "", contexts);
    }

    /**
     * {@inheritDoc}
     */
    public String getConfigurationLocation()
    {
        return getConfigurationLocation(null);
    }

    /**
     * {@inheritDoc}
     */
    public void setConfigurationsLocation(final IProject project,
        final String location)
    {
        IScopeContext scope = new ProjectScope(project);
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        if (location != null)
        {
            androidPreferences.put(CONFIGURATION_LOCATION, location);
        }
        else
        {
            androidPreferences.remove(CONFIGURATION_LOCATION);
        }
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public void setConfigurationLocation(final String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(CONFIGURATION_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * Sets the default configuration location.
     * 
     * @param location The configuration location.
     */
    public void setDefaultConfigurationLocation(final String location)
    {
        IScopeContext scope = new DefaultScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(CONFIGURATION_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public String getAndroMDACartridgesLocation()
    {
        return getAndroMDACartridgesLocation(null);
    }

    /**
     * Retrieves the cartridges location for the given project.
     * 
     * @param project The project to retrieve the cartridge location for.
     * @return The cartridge location.
     */
    private String getAndroMDACartridgesLocation(final IProject project)
    {
        IScopeContext[] contexts = null;
        if (project != null)
        {
            contexts = new IScopeContext[] { new ProjectScope(project) };
        }
        return preferencesService.getString(AndroidCore.PLUGIN_ID, CARTRIDGES_LOCATION, "", contexts);
    }

    /**
     * {@inheritDoc}
     */
    public String getAndroMDAProfilesLocation()
    {
        return getAndroMDAProfilesLocation(null);
    }

    /**
     * Retrieves the profiles location for the given project.
     * 
     * @param project The project to retrieve the profiles location for.
     * @return The profiles location.
     */
    private String getAndroMDAProfilesLocation(final IProject project)
    {
        IScopeContext[] contexts = null;
        if (project != null)
        {
            contexts = new IScopeContext[] { new ProjectScope(project) };
        }
        return preferencesService.getString(AndroidCore.PLUGIN_ID, PROFILES_LOCATION, "", contexts);
    }

    /**
     * {@inheritDoc}
     */
    public void setAndroMDACartridgesLocation(final String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(CARTRIDGES_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public void setAndroMDAProfilesLocation(final String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(PROFILES_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public String getMavenLocation()
    {
        return preferencesService.getString(AndroidCore.PLUGIN_ID, MAVEN_LOCATION, "", null);
    }

    /**
     * {@inheritDoc}
     */
    public void setMavenLocation(final String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(MAVEN_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public String getAndroMDAPreferredVersion()
    {
        return preferencesService.getString(AndroidCore.PLUGIN_ID, ANDROMDA_PREFERRED_VERSION, "", null);
    }

    /**
     * {@inheritDoc}
     */
    public void setAndroMDAPreferredVersion(final String preferredVersion)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(ANDROMDA_PREFERRED_VERSION, preferredVersion);
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultAndroMDAPreferredVersion(final String preferredVersion)
    {
        IScopeContext scope = new DefaultScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(ANDROMDA_PREFERRED_VERSION, preferredVersion);
        saveNode(androidPreferences);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isConfigurationValid()
    {
        List requiredSettings = new ArrayList();
        requiredSettings.add(getAndroMDACartridgesLocation());
        requiredSettings.add(getConfigurationLocation());

        for (Iterator iter = requiredSettings.iterator(); iter.hasNext();)
        {
            String setting = (String)iter.next();
            if (StringUtils.trimToNull(setting) == null)
            {
                return false;
            }
        }
        return true;
    }

}
