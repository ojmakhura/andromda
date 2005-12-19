package org.andromda.android.ui.internal.util;

import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * 
 * @author Peter Friese
 * @since 19.12.2005
 */
public class DialogUtils
{

    public static IFile selectResource(final Shell shell, final IProject project, String title, String message)
    {
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
                new WorkbenchContentProvider());
        dialog.setInput(project.getWorkspace());
        // IFile file = getFile();
        // if (file != null)
        // dialog.setInitialSelection(file);
        dialog.addFilter(new ViewerFilter()
        {
            public boolean select(Viewer viewer,
                Object parentElement,
                Object element)
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
            public IStatus validate(Object[] selection)
            {
                if (selection != null && selection.length > 0 && selection[0] instanceof IFile)
                    return new Status(IStatus.OK, AndroidUIPlugin.getPluginId(), IStatus.OK, "", null); //$NON-NLS-1$

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
