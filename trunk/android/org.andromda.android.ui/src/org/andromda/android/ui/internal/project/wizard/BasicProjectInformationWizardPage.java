package org.andromda.android.ui.internal.project.wizard;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class BasicProjectInformationWizardPage
        extends WizardPage
{
    private Label directoryLabel;

    private Text createProjectInDirectoryText;

    private Button browseForProjectLocationButton;

    private Button createProjectExternalRadio;

    private Button createProjectInWorkspaceRadio;

    private Text projectFriendlyNameText;

    private Text projectIDText;

    private String completeProjectPath = "";

    private boolean createInWorkspace;

    private final Map projectProperties;

    public BasicProjectInformationWizardPage(Map projectProperties)
    {
        super("basicProjectInformationWizardPage");
        this.projectProperties = projectProperties;
        setDescription("Create an AndroMDA project in the workspace or in an external location.");
        setTitle("Create an AndroMDA project");
    }

    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);

        setControl(container);

        final Label projectIDLabel = new Label(container, SWT.NONE);
        projectIDLabel.setText("&Project name (ID):");

        projectIDText = new Text(container, SWT.BORDER);
        projectIDText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                boolean valid = validatePage();
                setPageComplete(valid);
                setCompleteProjectPath();
            }
        });
        projectIDText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label projectFriendlyNameLabel = new Label(container, SWT.NONE);
        projectFriendlyNameLabel.setText("Friendl&y project name:");

        projectFriendlyNameText = new Text(container, SWT.BORDER);
        projectFriendlyNameText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                boolean valid = validatePage();
                setPageComplete(valid);
            }

        });
        projectFriendlyNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Group contentsGroup = new Group(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        contentsGroup.setLayoutData(gridData);
        contentsGroup.setText("Contents");
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 3;
        contentsGroup.setLayout(gridLayout_1);

        createProjectInWorkspaceRadio = new Button(contentsGroup, SWT.RADIO);
        createProjectInWorkspaceRadio.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                updateProjectLocationState();
                setCompleteProjectPath();
            }

        });
        createProjectInWorkspaceRadio.setSelection(true);
        final GridData gridData_1 = new GridData();
        gridData_1.horizontalSpan = 3;
        createProjectInWorkspaceRadio.setLayoutData(gridData_1);
        createProjectInWorkspaceRadio.setText("Create new project in &workspace");

        createProjectExternalRadio = new Button(contentsGroup, SWT.RADIO);
        createProjectExternalRadio.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                updateProjectLocationState();
                setCompleteProjectPath();
            }
        });
        final GridData gridData_2 = new GridData();
        gridData_2.horizontalSpan = 3;
        createProjectExternalRadio.setLayoutData(gridData_2);
        createProjectExternalRadio.setText("Create project at e&xternal location:");

        directoryLabel = new Label(contentsGroup, SWT.NONE);
        directoryLabel.setText("Directory:");

        createProjectInDirectoryText = new Text(contentsGroup, SWT.BORDER);
        createProjectInDirectoryText.setEnabled(false);
        createProjectInDirectoryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createProjectInDirectoryText.setText(getWorkspacePath());

        browseForProjectLocationButton = new Button(contentsGroup, SWT.NONE);
        browseForProjectLocationButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleLocationBrowseButtonPressed();
            }
        });
        browseForProjectLocationButton.setEnabled(false);
        browseForProjectLocationButton.setText("&Browse...");

        updateProjectLocationState();
        setCompleteProjectPath();

        setPageComplete(validatePage());
        projectIDText.setFocus();
    }

    /**
     * Open an appropriate directory browser
     */
    void handleLocationBrowseButtonPressed()
    {
        DirectoryDialog dialog = new DirectoryDialog(createProjectInDirectoryText.getShell());
        dialog.setMessage("Select the project contents directory.");

        String dirName = getCompleteProjectPath();
        if (!dirName.equals(""))
        {
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null)
        {
            completeProjectPath = selectedDirectory;
            createProjectInDirectoryText.setText(completeProjectPath);
        }
    }

    /**
     *
     */
    protected void updateProjectLocation()
    {
        try
        {
            String absoluteProjectPath = getCompleteProjectPath();
            createProjectInDirectoryText.setText(absoluteProjectPath);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
    }

    private String getWorkspacePath()
    {
        Location instanceLocation = Platform.getInstanceLocation();
        URL url = instanceLocation.getURL();
        File file = new File(url.getPath());
        return file.getAbsolutePath();
    }

    /**
     * @return The complete path for the project.
     */
    public String getCompleteProjectPath()
    {
        return completeProjectPath;
    }

    private void setCompleteProjectPath()
    {
        if (createInWorkspace)
        {
            completeProjectPath = getWorkspacePath() + File.separator + projectIDText.getText();
            createProjectInDirectoryText.setText(completeProjectPath);
        }
    }

    private void updateProjectLocationState()
    {
        createInWorkspace = createProjectInWorkspaceRadio.getSelection();
        createProjectInDirectoryText.setEnabled(!createInWorkspace);
        browseForProjectLocationButton.setEnabled(!createInWorkspace);
        directoryLabel.setEnabled(!createInWorkspace);
    }

    /**
     * @return The project ID (it will become the name of the underlying Eclipse
     *         project).
     */
    public String getProjectID()
    {
        if (projectIDText == null)
            return "";

        return projectIDText.getText();
    }

    /**
     * @return The friendly (i.e human readable) name of the project. It will be
     *         used in the maven POM only.
     */
    public String getProjectFriendlyName()
    {
        if (projectFriendlyNameText == null)
            return "";

        return projectFriendlyNameText.getText();
    }

    /**
     * Creates a project resource handle for the current project name field
     * value.
     * <p>
     * This method does not create the project resource; this is the
     * responsibility of <code>IProject::create</code> invoked by the new
     * project resource wizard.
     * </p>
     *
     * @return the new project resource handle
     */
    public IProject getProjectHandle()
    {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectID());
    }

    /**
     * Checks whether the user entered all neccessary information.
     *
     * @return <code>true</code> if all information have been gathered and the
     *         page may be left.
     */
    private boolean validatePage()
    {
        String projectIDContents = getProjectID();
        if (projectIDContents.equals(""))
        {
            setErrorMessage("Enter a project name.");
            setMessage(null);
            return false;
        }

        String projectFriendlyName = getProjectFriendlyName();
        if (projectFriendlyName.equals(""))
        {
            setErrorMessage(null);
            setMessage("Please enter a friendly name for the project.");
            return false;
        }

        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    public void updateData()
    {
        projectProperties.put("projectName", getProjectFriendlyName());
        projectProperties.put("projectId", getProjectID());
        projectProperties.put("projectPath", getCompleteProjectPath());
    }

}
