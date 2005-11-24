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
public class AndroidUIPlugin extends AbstractUIPlugin {

	/** The shared instance of this plug-in. */
    private static AndroidUIPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public AndroidUIPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static AndroidUIPlugin getDefault() {
		return plugin;
	}
    
    /**
     * @return the plug-ins ID.
     */
    public static String getPluginId() {
        return getDefault().getBundle().getSymbolicName();
    }
    
    /**
     * @return the workspace.
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }
    
    /**
     * @return the active page
     */
    public static IWorkbenchPage getActivePage() {
        return getDefault().internalGetActivePage();
    }
    
    /**
     * @return the active workbench window.
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }
    
    /**
     * @return the active workbench shell.
     */
    public static Shell getActiveWorkbenchShell() {
         IWorkbenchWindow window= getActiveWorkbenchWindow();
         if (window != null) {
            return window.getShell();
         }
         return null;
    }

    private IWorkbenchPage internalGetActivePage() {
        IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
        if (window == null)
            return null;
        return getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }
    
    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }
    
    public static void logErrorMessage(String message) {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, message, null));
    }

    public static void logErrorStatus(String message, IStatus status) {
        if (status == null) {
            logErrorMessage(message);
            return;
        }
        MultiStatus multi= new MultiStatus(getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, message, null);
        multi.add(status);
        log(multi);
    }
    
    public static void log(Throwable e) {
        log(new Status(IStatus.ERROR, getPluginId(), IAndroidStatusConstants.INTERNAL_ERROR, "Internal Error", e)); 
    }
    
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.andromda.android.ui", path);
	}
}
