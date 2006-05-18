package org.andromda.android.ui;

import org.andromda.android.core.IAndroidStatusConstants;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class AndroidUIPlugin
        extends AbstractUIPlugin
{

    /** The shared instance of this plug-in. */
    private static AndroidUIPlugin plugin;

    /**
     * The constructor.
     */
    public AndroidUIPlugin()
    {
        plugin = this;
    }

    /**
     * {@inheritDoc}
     */
    public void start(final BundleContext context) throws Exception
    {
        super.start(context);
    }

    /**
     * {@inheritDoc}
     */
    public void stop(final BundleContext context) throws Exception
    {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
     * 
     * @return The instance of this plug-in.
     */
    public static AndroidUIPlugin getDefault()
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
     * @return the workspace.
     */
    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * @return the active page
     */
    public static IWorkbenchPage getActivePage()
    {
        return getDefault().internalGetActivePage();
    }

    /**
     * @return the active workbench window.
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow()
    {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    /**
     * @return the active workbench shell.
     */
    public static Shell getActiveWorkbenchShell()
    {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        if (window != null)
        {
            return window.getShell();
        }
        return null;
    }

    /**
     * Return the active workbench page.
     * 
     * @return The active workbench page.
     */
    private IWorkbenchPage internalGetActivePage()
    {
        IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
        if (window == null)
        {
            return null;
        }
        return getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * Logs the given status.
     * 
     * @param status The status to log.
     */
    public static void log(final IStatus status)
    {
        getDefault().getLog().log(status);
    }

    /**
     * Logs the given error message.
     * 
     * @param message The error message.
     */
    public static void logErrorMessage(final String message)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, message, null));
    }

    /**
     * Logs the given error status.
     * 
     * @param message The message to log.
     * @param status The status to log.
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
     * Logs the given throwable.
     * 
     * @param e The throwable to log.
     */
    public static void log(final Throwable e)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, "Internal Error", e));
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path)
    {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.andromda.android.ui", path);
    }
}
