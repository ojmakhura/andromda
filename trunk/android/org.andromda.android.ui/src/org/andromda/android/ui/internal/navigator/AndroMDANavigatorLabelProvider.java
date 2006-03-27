package org.andromda.android.ui.internal.navigator;

import org.andromda.android.core.internal.AndroidModelManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 *
 * @author Peter Friese
 * @since 24.03.2006
 */
public class AndroMDANavigatorLabelProvider
        implements ILabelProvider
{

    /**
     * {@inheritDoc}
     */
    public String getText(final Object element)
    {
        if (element instanceof IFile)
        {
            IFile file = (IFile)element;

            IProject project = file.getProject();
            IFile projectConfiguration = AndroidModelManager.getInstance().getAndroidModel().getProjectConfiguration(
                    project);
            if (file.equals(projectConfiguration)) {
                return "AndroMDA Configuration";
            }
            // TODO remove
            IPath projectRelativePath = file.getProjectRelativePath();
            return projectRelativePath.toString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Image getImage(final Object element)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(final ILabelProviderListener listener)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(final ILabelProviderListener listener)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void dispose()
    {
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLabelProperty(final Object element,
        final String property)
    {
        return false;
    }

}
