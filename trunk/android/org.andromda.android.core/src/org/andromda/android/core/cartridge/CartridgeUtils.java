package org.andromda.android.core.cartridge;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * Utilities that make working with cartridges easier.
 * 
 * @author Peter Friese
 * @since 01.02.2006
 */
public final class CartridgeUtils
{

    /**
     * Hidden.
     */
    private CartridgeUtils()
    {
    }

    /**
     * Finds the cartridge root folder for the given file.
     * 
     * @param templateFile The file to lookup the cartridge root for.
     * @return The root container of the cartridge. May either be an {@link IFolder} or an {@link IProject}.
     */
    public static IContainer findCartridgeRoot(final IFile templateFile)
    {
        IProject project = templateFile.getProject();
        IPath projectRelativePath = templateFile.getProjectRelativePath();

        int segments = projectRelativePath.segmentCount();
        int segment = segments;
        boolean found = false;
        while (!found && segment > 0)
        {
            String string = projectRelativePath.segment(segment - 1);
            found = string.equalsIgnoreCase("src");
            segment--;
        }

        IPath cartridgeRootPath = projectRelativePath.uptoSegment(segment);
        IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
        if (cartridgeRootPath.segmentCount() == 0)
        {
            cartridgeRootPath = project.getLocation();
        }
        return wRoot.getContainerForLocation(cartridgeRootPath);
    }

}
