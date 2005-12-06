package org.andromda.android.ui.project.wizard;

import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.project.AndroidProjectFactory;
import org.andromda.android.ui.internal.project.wizard.BasicProjectInformationWizardPage;
import org.andromda.android.ui.internal.project.wizard.ProjectFeaturesWizardPage;
import org.andromda.android.ui.internal.project.wizard.ProjectMetaInformationWizardPage;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * The <code>New AndroMDA Project Wizard</code>.
 * 
 * @author Peter Friese
 * @since 22.05.2005
 */
public class NewAndroidProjectWizard
        extends NewElementWizard
        implements INewWizard
{

    private BasicProjectInformationWizardPage basicProjectInformationPage;

    private ProjectMetaInformationWizardPage projectMetaInformationWizardPage;

    private IWorkbench workbench;

    private IStructuredSelection selection;

    private ProjectFeaturesWizardPage projectFeaturesWizardPage;

    private static final String ID_ANDROMDAPP_PROCESS_TYPE = "andromdapp";

    /**
     * This will contain all project properties that the user has entered in the wizard.
     */
    private Map projectProperties = new HashMap();

    private IProject projectHandle;

    /**
     * Constructor for NewAndroMDAProjectWizard.
     */
    public NewAndroidProjectWizard()
    {
        super();
        setWindowTitle("New AndroMDA Project");
        setNeedsProgressMonitor(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     *      org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench,
        IStructuredSelection selection)
    {
        this.workbench = workbench;
        this.selection = selection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages()
    {
        super.addPages();
        basicProjectInformationPage = new BasicProjectInformationWizardPage(projectProperties);
        addPage(basicProjectInformationPage);
        projectMetaInformationWizardPage = new ProjectMetaInformationWizardPage(projectProperties);
        addPage(projectMetaInformationWizardPage);
        projectFeaturesWizardPage = new ProjectFeaturesWizardPage(projectProperties);
        addPage(projectFeaturesWizardPage);
    }

    protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException
    {
        String name = (String)projectProperties.get("projectId");
        AndroidProjectFactory.createAndroidProject(monitor, name, projectProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#performFinish()
     */
    public boolean performFinish()
    {
        // have all pages write their gathered information into the project
        // properties map
        basicProjectInformationPage.updateData();
        projectMetaInformationWizardPage.updateData();
        projectFeaturesWizardPage.updateData();

        return super.performFinish();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#getCreatedElement()
     */
    public IJavaElement getCreatedElement()
    {
        return null;
    }

}