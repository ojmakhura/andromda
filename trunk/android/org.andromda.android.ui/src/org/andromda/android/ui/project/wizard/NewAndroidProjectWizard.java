package org.andromda.android.ui.project.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.project.AndroidProjectFactory;
import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.project.wizard.BasicProjectInformationWizardPage;
import org.andromda.android.ui.internal.project.wizard.ChooseProjectCartridgeWizardPage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
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

    /** Wizard page showing basic project properties. */
    private BasicProjectInformationWizardPage basicProjectInformationPage;

    /** This will contain all project properties that the user has entered in the wizard. */
    private Map projectProperties = new HashMap();

    /** The configuration element used to trigger this execution. */
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
    public void init(final IWorkbench workbench,
        final IStructuredSelection selection)
    {
        // no need to store workbench or selection.
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

    /**
     * @throws InterruptedException if project creation is aborted by user.
     * @throws CoreException if some problem occurrs during project creation.
     * @throws InvocationTargetException if there is a problem when invoking the project creation.
     */
    protected void finishPage() throws InterruptedException, CoreException, InvocationTargetException
    {
        WorkspaceModifyOperation op = new WorkspaceModifyOperation()
        {
            protected void execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException,
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
     * @return null
     */
    public IJavaElement getCreatedElement()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setInitializationData(final IConfigurationElement config,
        final String propertyName,
        final Object data) throws CoreException
    {
        this.config = config;
    }

}
