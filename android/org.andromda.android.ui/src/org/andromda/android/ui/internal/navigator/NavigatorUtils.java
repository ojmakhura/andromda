package org.andromda.android.ui.internal.navigator;

import org.andromda.android.core.internal.AndroidModelManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

/**
 * Utility methods for the Navigator.
 * 
 * @author Peter Friese
 * @since 04.06.2006
 */
public class NavigatorUtils
{
    
    /**
     * Checks whether the given element is the AndroMDA configuration file for the current project.
     * 
     * @param element An element.
     * @return <code>true</code> if the element is the AndroMDA configuration file, <code>false</code> otherwise.
     */
    public static boolean isAndroMDAConfigurationFile(final Object element)
    {
        if (element instanceof IFile)
        {
            IFile file = (IFile)element;

            IProject project = file.getProject();
            IFile projectConfiguration = AndroidModelManager.getInstance().getAndroidModel().getProjectConfiguration(
                    project);
            if (file.equals(projectConfiguration))
            {
                return true;
            }
        }
        return false;
    }

}
