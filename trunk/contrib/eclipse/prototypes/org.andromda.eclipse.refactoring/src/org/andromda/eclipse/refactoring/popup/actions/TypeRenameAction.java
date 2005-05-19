package org.andromda.eclipse.refactoring.popup.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jdt.internal.corext.refactoring.tagging.INameUpdating;
import org.eclipse.jdt.internal.corext.refactoring.tagging.IReferenceUpdating;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusEntry;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class TypeRenameAction
        implements IObjectActionDelegate
{

    /**
     * The type in the tree on which the plug-in has to operate.
     */
    private IType type;

    /**
     * Constructor for Action1.
     */
    public TypeRenameAction()
    {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
    }

    private static void setNewName(INameUpdating refactoring, String newName)
    {
        if (newName != null)
            refactoring.setNewElementName(newName);
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action)
    {
        Shell shell = new Shell();
        // MessageDialog.openInformation(shell, "Refactoring Plug-in",
        // "Rename... was executed.");
        if (this.type != null)
        {
            // MessageDialog.openInformation(shell, "Refactoring Plug-in",
            // "Rename... on " + unit.getElementName() + " was attempted.");
            InputDialog dialog = new InputDialog(shell, "New type name",
                    "Enter new name for type", "MyNewClass", null);
            int dialogResult = dialog.open();
            if (dialogResult == Window.OK)
            {
                String newName = dialog.getValue();
                try
                {
                    JavaRenameProcessor processor = new RenameTypeProcessor(
                            this.type);
                    processor.setNewElementName(newName);

                    RenameRefactoring refactoring = new RenameRefactoring(
                            processor);

                    setNewName((INameUpdating)refactoring
                            .getAdapter(INameUpdating.class), newName);

                    IReferenceUpdating reference = (IReferenceUpdating)refactoring
                            .getAdapter(IReferenceUpdating.class);
                    if (reference != null)
                    {
                        reference.setUpdateReferences(true);
                    }

                    final PerformRefactoringOperation operation = new PerformRefactoringOperation(
                            refactoring,
                            CheckConditionsOperation.ALL_CONDITIONS);

                    /**
                     * IRunnableWithProgress r = new IRunnableWithProgress() {
                     * public void run(IProgressMonitor monitor) throws
                     * InvocationTargetException, InterruptedException { try {
                     * operation.run(monitor); } catch (CoreException e) { throw
                     * new InvocationTargetException(e); } } };
                     * PlatformUI.getWorkbench().getProgressService().run(true,
                     * true, r);
                     */

                    operation.run(new DummyProgressMonitor());

                    RefactoringStatus conditionStatus = operation
                            .getConditionStatus();
                    if (conditionStatus.hasError())
                    {
                        String errorMessage = "Rename "
                                + this.type.getElementName() + " to " + newName
                                + " has errors!";
                        RefactoringStatusEntry[] entries = conditionStatus
                                .getEntries();
                        for (int i = 0; i < entries.length; i++)
                        {
                            RefactoringStatusEntry entry = entries[i];
                            errorMessage += "\n>>>" + entry.getMessage();
                        }
                        MessageDialog.openInformation(shell,
                                "Refactoring Plug-in", errorMessage);
                    }

                    // RenameSupport rs = RenameSupport.create(this.unit,
                    // newName,
                    // RenameSupport.UPDATE_REFERENCES);
                    // rs.perform(shell,
                    // PlatformUI.getWorkbench().getProgressService());

                }
                /**
                catch (InterruptedException e)
                {
                    MessageDialog.openInformation(shell, "Refactoring Plug-in",
                            "Exception: " + e.toString());
                    e.printStackTrace();
                } catch (InvocationTargetException e)
                {
                    MessageDialog.openInformation(shell, "Refactoring Plug-in",
                            "Exception: " + e.toString());
                    e.printStackTrace();
                }
                **/ 
                catch (CoreException e)
                {
                    MessageDialog.openInformation(shell, "Refactoring Plug-in",
                            "Exception: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        this.type = null;
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection iss = (IStructuredSelection)selection;
            Object selected = iss.getFirstElement();
            if (selected instanceof IType)
            {
                this.type = (IType)selected;
            }
        }
    }

}
