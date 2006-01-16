package org.andromda.android.ui.internal.util;

import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * This class supplies some utility methods that facilitate dialog handling.
 *
 * @author Peter Friese
 * @since 19.12.2005
 */
public final class DialogUtils
{

    /**
     * Private cosntructor. Must not be instantiated, since this is a utility class.
     */
    private DialogUtils()
    {
        // hide.
    }

    /**
     * Let the user choose a directory on the file system.
     *
     * @param shell The shell.
     * @param title The title for the dialog.
     * @param message A message displayed at the top of the dialog area.
     * @param filterPath If not-null, this apth will be preselected in the dialog.
     * @return A {@link String} indicating the selected path. If nothing was selected, <code>null</code> will be
     *         returned.
     */
    public static String selectDirectory(final Shell shell,
        final String title,
        final String message,
        final String filterPath)
    {
        DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
        dialog.setText(title);
        dialog.setMessage(message);
        dialog.setFilterPath(filterPath);
        return dialog.open();
    }

    /**
     * Let the user choose a resource in the workspace.
     *
     * @param shell The shell.
     * @param project The project.
     * @param title The title.
     * @param message A message shwon at the top of the dialog.
     * @return A {@link IFile} indicating the resource the user selected.
     */
    public static IFile selectResource(final Shell shell,
        final IProject project,
        final String title,
        final String message)
    {
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
                new WorkbenchContentProvider());
        dialog.setInput(project.getWorkspace());
        // IFile file = getFile();
        // if (file != null)
        // dialog.setInitialSelection(file);
        dialog.addFilter(new ViewerFilter()
        {
            public boolean select(final Viewer viewer,
                final Object parentElement,
                final Object element)
            {
                if (element instanceof IProject)
                {
                    return ((IProject)element).equals(project);
                }
                else if (element instanceof IFile)
                {
                    String fileExtension = ((IFile)element).getFileExtension();
                    return fileExtension.endsWith("xmi");
                }
                return true;
            }
        });
        dialog.setAllowMultiple(true);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setValidator(new ISelectionStatusValidator()
        {
            public IStatus validate(final Object[] selection)
            {
                if (selection != null && selection.length > 0 && selection[0] instanceof IFile)
                {
                    return new Status(IStatus.OK, AndroidUIPlugin.getPluginId(), IStatus.OK, "", null); //$NON-NLS-1$
                }
                return new Status(IStatus.ERROR, AndroidUIPlugin.getPluginId(), IStatus.ERROR, "", null); //$NON-NLS-1$
            }
        });

        if (dialog.open() == ElementTreeSelectionDialog.OK)
        {
            IFile file = (IFile)dialog.getFirstResult();
            String value = file.getProjectRelativePath().toString();
            return file;
        }
        return null;

    }

}
