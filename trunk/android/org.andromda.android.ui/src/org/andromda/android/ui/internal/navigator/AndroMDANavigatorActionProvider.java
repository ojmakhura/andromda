package org.andromda.android.ui.internal.navigator;

import org.andromda.android.ui.internal.project.runner.AndroMDARunAction;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;

/**
 *
 * @author Peter Friese
 * @since 03.06.2006
 */
public class AndroMDANavigatorActionProvider
        extends CommonActionProvider
{

    private Object selectedElement;

    /**
     * {@inheritDoc}
     */
    public void fillContextMenu(IMenuManager menu)
    {
        super.fillContextMenu(menu);
        if (NavigatorUtils.isAndroMDAConfigurationFile(selectedElement)) {
            IFile file = (IFile)selectedElement;
            IAction action = new AndroMDARunAction(file);
            menu.appendToGroup("group.build", action);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setContext(ActionContext context)
    {
        super.setContext(context);
        if (context != null) {
            ISelection selection = context.getSelection();
            if (selection instanceof IStructuredSelection)
            {
                IStructuredSelection structuredSelection = (IStructuredSelection)selection;
                selectedElement = structuredSelection.getFirstElement();
            }
        }
    }
    
}
