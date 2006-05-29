package org.andromda.android.ui.project.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.project.AndroidProjectFactory;
import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.project.wizard.BasicProjectInformationWizardPage;
import org.andromda.android.ui.internal.project.wizard.ChooseProjectCartridgeWizardPage;
import org.andromda.android.ui.internal.project.wizard.ProjectFeaturesWizardPage;
import org.andromda.android.ui.internal.project.wizard.ProjectMetaInformationWizardPage;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * The <code>New AndroMDA Project Wizard</code>.
 * 
 * @author Peter Friese
 * @since 22.05.2005
 */
public class NewAndroidProjectWizard
        extends Wizard
        implements INewWizard, IExecutableExtension
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

    private JavaCapabilityConfigurationPage javaCapabilityConfigurationPage;

    private IConfigurationElement config;

    /**
     * Constructor for NewAndroMDAProjectWizard.
     */
    public NewAndroidProjectWizard()
    {
        super();
        setWindowTitle("New AndroMDA Project");
        setNeedsProgressMonitor(true);
    }

    /**
     * {@inheritDoc}
     */
    public void init(IWorkbench workbench,
        IStructuredSelection selection)
    {
        this.workbench = workbench;
        this.selection = selection;
    }

    /**
     * {@inheritDoc}
     */
    public void addPages()
    {
        super.addPages();
        basicProjectInformationPage = new BasicProjectInformationWizardPage(projectProperties);
        addPage(basicProjectInformationPage);

        ChooseProjectCartridgeWizardPage chooseProjectCatridgeWizardPage = new ChooseProjectCartridgeWizardPage(
                projectProperties);
        addPage(chooseProjectCatridgeWizardPage);
    }

    protected void finishPage() throws InterruptedException, CoreException, InvocationTargetException
    {
        WorkspaceModifyOperation op = new WorkspaceModifyOperation()
        {
            protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
                    InterruptedException
            {
                String name = (String)projectProperties.get(IProjectCartridgeDescriptor.PROPERTY_PROJECT_ID);
                AndroidProjectFactory.createAndroidProject(monitor, name, projectProperties);
            }
        };
        
        getContainer().run(true, true, op);
    }

    /**
     * {@inheritDoc}
     */
    public boolean performFinish()
    {
        basicProjectInformationPage.updateData();

        try
        {
            finishPage();
        }
        catch (Exception e)
        {
            AndroidUIPlugin.log(e);
            return false;
        }
        BasicNewProjectResourceWizard.updatePerspective(config);
        return true;
    }

    /**
     * @return
     */
    public IJavaElement getCreatedElement()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setInitializationData(IConfigurationElement config,
        String propertyName,
        Object data) throws CoreException
    {
        this.config = config;
    }

}