package org.andromda.android.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EditorUIPlugin
        extends AbstractUIPlugin
{

    /** The shared instance of this plug-in. */
    private static EditorUIPlugin plugin;

    /**
     * The constructor.
     */
    public EditorUIPlugin()
    {
        plugin = this;
    }

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(final BundleContext context) throws Exception
    {
        super.start(context);
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(final BundleContext context) throws Exception
    {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
     * 
     * @return the shared instance
     */
    public static EditorUIPlugin getDefault()
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
     * Logs to the error log.
     * 
     * @param status the status to log
     */
    public static void log(IStatus status)
    {
        getDefault().getLog().log(status);
    }

    /**
     * Logs to the error log.
     * 
     * @param message the message to log
     */
    public static void logErrorMessage(String message)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IEditorStatusConstants.INTERNAL_ERROR, message, null));
    }

    /**
     * Logs to the error log.
     * 
     * @param message the message to log
     * @param status the status to log
     */
    public static void logErrorStatus(final String message,
        final IStatus status)
    {
        if (status == null)
        {
            logErrorMessage(message);
            return;
        }
        MultiStatus multi = new MultiStatus(getPluginId(), IEditorStatusConstants.INTERNAL_ERROR, message, null);
        multi.add(status);
        log(multi);
    }

    /**
     * Logs to the error log.
     * 
     * @param e the exception to log
     */
    public static void log(final Throwable e)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IEditorStatusConstants.INTERNAL_ERROR, "Internal Error", e));
    }

}
