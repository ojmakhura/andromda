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

    private static final String MAVEN_LOCATION = "maven.location";

    private IPreferencesService preferencesService;

    private static final AndroidSettingsAccess instance = new AndroidSettingsAccess();

    /**
     * Creates a new AndroidSettingsAccess.
     */
    private AndroidSettingsAccess()
    {
        super();
        preferencesService = Platform.getPreferencesService();
    }

    /**
     * @return
     */
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

    /**
     * {@inheritDoc}
     */
    public String getConfigurationLocation(IProject project)
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
    public void setConfigurationsLocation(String location)
    {
        IScopeContext scope = new InstanceScope();
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
     * @param project
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
     * {@inheritDoc}
     */
    public String getAndroMDAProfilesLocation()
    {
        return getAndroMDAProfilesLocation(null);
    }

    /**
     * @param project
     * @return
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

}
