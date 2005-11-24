package org.andromda.android.ui.project.wizard;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.ui.internal.project.wizard.ProjectSelectionWizardPage;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

/**
 *
 * @author Peter Friese
 * @since 03.11.2005
 */
public class ConvertToAndroidProjectWizard
        extends Wizard
        implements IImportWizard
{

    private ProjectSelectionWizardPage projectSelectionPage;

    /**
     *
     */
    public ConvertToAndroidProjectWizard()
    {
        super();
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages()
    {
        super.addPages();
        projectSelectionPage = new ProjectSelectionWizardPage();
        addPage(projectSelectionPage);
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        IProject[] selectedProjects = projectSelectionPage.getSelectedProjects();
        for (int i = 0; i < selectedProjects.length; i++)
        {
            IProject project = selectedProjects[i];
            AndroidCore.create(project, true);
        }
        return true;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     *      org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench,
        IStructuredSelection selection)
    {
    }

}
