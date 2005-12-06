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
import org.osgi.framework.BundleContext;

/**
 * The Android Core contains provides access to the UI-free part of Android.
 * 
 * @author Peter Friese
 * @since 29.09.2005
 */
public class AndroidCore
        extends Plugin
{

    public static final String PLUGIN_ID = "org.andromda.android.core";

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
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
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

    public static void log(IStatus status)
    {
        getDefault().getLog().log(status);
    }

    public static void logErrorMessage(String message)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, message, null));
    }

    public static void logErrorStatus(String message,
        IStatus status)
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

    public static void log(Throwable e)
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

    public static IAndroidProject create(IProject project)
    {
        return create(project, false);
    }

    public static IAndroidProject create(IProject project,
        boolean force)
    {
        if (project == null)
        {
            return null;
        }
        return AndroidModelManager.getInstance().getAndroidModel().getAndroidProject(project, force);
    }

    public static IAndroidSettings getAndroidSettings()
    {
        return AndroidSettingsAccess.instance();
    }

    public static PropertyGroup[] getCartridgePropertyGroups(Namespace configurationNamespace)
    {
        return AndroidModelManager.getInstance().getAndroidModel().getCartridgePropertyGroups(configurationNamespace);
    }

}
