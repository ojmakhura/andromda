package org.andromda.android.core;

import org.andromda.android.core.internal.AndroidModelManager;
import org.andromda.android.core.internal.settings.AndroidSettingsAccess;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.settings.IAndroidSettings;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

/**
 * The Android Core contains provides access to the UI-free part of Android.
 * 
 * @author Peter Friese
 * @since 29.09.2005
 */
public class AndroidCore
        extends Plugin
{

    /** The plug-in ID. */
    public static final String PLUGIN_ID = "org.andromda.android.core";

    /** The nature ID. */
    public static final String NATURE_ID = PLUGIN_ID + ".androidnature";

    /** The shared instance of this plug-in. */
    private static AndroidCore plugin;

    /**
     * The constructor.
     */
    public AndroidCore()
    {
        plugin = this;
    }

    /**
     * @return The default instance of the plug-in.
     */
    public static AndroidCore getDefault()
    {
        return plugin;
    }

    /**
     * @return the plug-ins ID.
     */
    public static String getPluginId()
    {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
     * Logs a status.
     * 
     * @param status The status.
     */
    public static void log(final IStatus status)
    {
        getDefault().getLog().log(status);
    }

    /**
     * Logs a message.
     * 
     * @param message The message.
     */
    public static void logErrorMessage(final String message)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, message, null));
    }

    /**
     * Logs a message and a status.
     * 
     * @param message The message.
     * @param status The status.
     */
    public static void logErrorStatus(final String message,
        final IStatus status)
    {
        if (status == null)
        {
            logErrorMessage(message);
            return;
        }
        MultiStatus multi = new MultiStatus(getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, message, null);
        multi.add(status);
        log(multi);
    }

    /**
     * Logs a throwable.
     * 
     * @param e The Throwable.
     */
    public static final void log(final Throwable e)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, "Internal Error", e));
    }

    /**
     * Returns the single instance of the Android Core. Equivalent to <code>getDefault()</code>.
     * 
     * @return the Android Core.
     */
    public static AndroidCore getAndroidCore()
    {
        return getDefault();
    }

    /**
     * Creates a handle for an existing project.
     * 
     * @param project The existing project.
     * @return An {@link IAndroidProject} handle to the existing project.
     */
    public static IAndroidProject create(final IProject project)
    {
        return create(project, false);
    }

    /**
     * Creates a handle for the given project. If the project does not yet existist, it will be created.
     * 
     * @param project The project.
     * @param force If <code>true</code>, the project will be created.
     * @return An {@link IAndroidProject} handle to the project. If the project is <code>null</code>,
     *         <code>null</code> will be returned.
     */
    public static IAndroidProject create(final IProject project,
        final boolean force)
    {
        if (project == null)
        {
            return null;
        }
        return AndroidModelManager.getInstance().getAndroidModel().getAndroidProject(project, force);
    }

    /**
     * @return The plug-in settings.
     */
    public static IAndroidSettings getAndroidSettings()
    {
        return AndroidSettingsAccess.instance();
    }

    /**
     * Retrieves the cartridge name space properties for the given project.
     * 
     * @param configurationNamespace The configuration namespace.
     * @param project The current project.
     * @return The name space properties.
     */
    public static PropertyGroup[] getCartridgePropertyGroups(final Namespace configurationNamespace,
        final IAndroidProject project) 
    {
        return AndroidModelManager.getInstance().getAndroidModel().getCartridgePropertyGroups(configurationNamespace,
                project);
    }

}
