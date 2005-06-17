package org.andromda.android.ui.wizards.newproject;

import java.util.HashMap;
import java.util.Map;

import org.andromda.android.internal.core.build.maven.MavenRunner;
import org.andromda.android.internal.ui.wizards.BasicProjectInformationWizardPage;
import org.andromda.android.internal.ui.wizards.ProjectFeaturesWizardPage;
import org.andromda.android.internal.ui.wizards.ProjectMetaInformationWizardPage;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
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
public class NewAndroMDAProjectWizard
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
     * This will contain all project properties that the user has entered in the
     * wizard.
     */
    private Map projectProperties = new HashMap();

    private IProject projectHandle;

    /**
     * Constructor for NewAndroMDAProjectWizard.
     */
    public NewAndroMDAProjectWizard()
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
    public void init(IWorkbench workbench, IStructuredSelection selection)
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
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root= workspace.getRoot();
        IProject project= root.getProject(name);
        project.create(null);
        project.open(null);

        MavenRunner runner = new MavenRunner(projectProperties, project);
        runner.execute(monitor);
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

}