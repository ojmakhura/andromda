package org.andromda.android.core.internal.settings;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.settings.IAndroidSettings;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * 
 * @author Peter Friese
 * @since 05.12.2005
 */
public class AndroidSettingsAccess
        implements IAndroidSettings
{

    private static final String CONFIGURATION_LOCATION = "configuration.location";

    private static final String CARTRIDGES_LOCATION = "cartidges.location";

    private static final String PROFILES_LOCATION = "profiles.location";

    private IPreferencesService preferencesService;

    private static final AndroidSettingsAccess instance = new AndroidSettingsAccess();

    private AndroidSettingsAccess()
    {
        super();
        preferencesService = Platform.getPreferencesService();
    }

    public static final AndroidSettingsAccess instance()
    {
        return instance;
    }

    /**
     * @return result of save request.
     */
    private boolean saveNode(Preferences node)
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

    public String getConfigurationLocation(IProject project)
    {
        IScopeContext[] contexts = null;
        if (project != null)
        {
            contexts = new IScopeContext[] { new ProjectScope(project) };
        }
        return preferencesService.getString(AndroidCore.PLUGIN_ID, CONFIGURATION_LOCATION, "", contexts);
    }

    public String getConfigurationLocation()
    {
        return getConfigurationLocation(null);
    }

    /**
     * @see org.andromda.android.core.settings.IAndroidSettings#setConfigurationsLocation(org.eclipse.core.resources.IProject,
     *      java.lang.String)
     */
    public void setConfigurationsLocation(IProject project,
        String location)
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
     * @see org.andromda.android.core.settings.IAndroidSettings#setConfigurationsLocation(java.lang.String)
     */
    public void setConfigurationsLocation(String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(CONFIGURATION_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * @see org.andromda.android.core.settings.IAndroidSettings#getAndroMDACartridgesLocation()
     */
    public String getAndroMDACartridgesLocation()
    {
        return getAndroMDACartridgesLocation(null);
    }

    /**
     * @param object
     * @return
     */
    private String getAndroMDACartridgesLocation(IProject project)
    {
        IScopeContext[] contexts = null;
        if (project != null)
        {
            contexts = new IScopeContext[] { new ProjectScope(project) };
        }
        return preferencesService.getString(AndroidCore.PLUGIN_ID, CARTRIDGES_LOCATION, "", contexts);
    }

    /**
     * @see org.andromda.android.core.settings.IAndroidSettings#getAndroMDAProfilesLocation()
     */
    public String getAndroMDAProfilesLocation()
    {
        return getAndroMDAProfilesLocation(null);
    }

    /**
     * @param object
     * @return
     */
    private String getAndroMDAProfilesLocation(IProject project)
    {
        IScopeContext[] contexts = null;
        if (project != null)
        {
            contexts = new IScopeContext[] { new ProjectScope(project) };
        }
        return preferencesService.getString(AndroidCore.PLUGIN_ID, PROFILES_LOCATION, "", contexts);
    }

    /**
     * @see org.andromda.android.core.settings.IAndroidSettings#setAndroMDACartridgesLocation(java.lang.String)
     */
    public void setAndroMDACartridgesLocation(String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(CARTRIDGES_LOCATION, location);
        saveNode(androidPreferences);
    }

    /**
     * @see org.andromda.android.core.settings.IAndroidSettings#setAndroMDAProfilesLocation(java.lang.String)
     */
    public void setAndroMDAProfilesLocation(String location)
    {
        IScopeContext scope = new InstanceScope();
        IEclipsePreferences androidPreferences = scope.getNode(AndroidCore.PLUGIN_ID);
        androidPreferences.put(PROFILES_LOCATION, location);
        saveNode(androidPreferences);
    }

}
