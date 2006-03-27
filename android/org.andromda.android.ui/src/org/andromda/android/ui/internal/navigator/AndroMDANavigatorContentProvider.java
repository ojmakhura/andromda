package org.andromda.android.ui.internal.navigator;

import org.andromda.android.core.internal.AndroidModelManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 *
 * @author Peter Friese
 * @since 23.03.2006
 */
public class AndroMDANavigatorContentProvider
        implements ITreeContentProvider
{

    /**
     * {@inheritDoc}
     */
    public Object[] getChildren(final Object parentElement)
    {
        if (parentElement instanceof IAdaptable)
        {
            IProject project = (IProject)((IAdaptable)parentElement).getAdapter(IProject.class);
            IFile projectConfiguration = AndroidModelManager.getInstance().getAndroidModel().getProjectConfiguration(
                    project);
            return new Object[] { projectConfiguration };
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Object getParent(final Object element)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChildren(final Object element)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] getElements(final Object inputElement)
    {
        return getChildren(inputElement);
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
    public void inputChanged(final Viewer viewer,
        final Object oldInput,
        final Object newInput)
    {
    }

}
