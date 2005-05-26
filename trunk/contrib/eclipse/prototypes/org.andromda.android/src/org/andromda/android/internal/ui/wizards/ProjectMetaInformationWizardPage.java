package org.andromda.android.internal.ui.wizards;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ProjectMetaInformationWizardPage
        extends WizardPage
{

    private Text inceptionYearText;

    private Text versionText;

    private Text basePackageNameText;

    private Text shortDescriptionText;

    private Text lastNameText;

    private Text firstNameText;

    private final Map projectProperties;

    public ProjectMetaInformationWizardPage(Map projectProperties)
    {
        super("projectMetaInformationWizardPage");
        this.projectProperties = projectProperties;
        setTitle("Project meta information");
        setDescription("Give us some detail about your project.");
    }

    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        container.setFont(parent.getFont());

        setControl(container);

        final Group grojectCreatorGroup = new Group(container, SWT.NONE);
        grojectCreatorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        grojectCreatorGroup.setLayout(gridLayout);
        grojectCreatorGroup.setText("Project creator");

        final Label firstNameLabel = new Label(grojectCreatorGroup, SWT.NONE);
        firstNameLabel.setText("Firs&t name:");

        firstNameText = new Text(grojectCreatorGroup, SWT.BORDER);
        firstNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label lastNameLabel = new Label(grojectCreatorGroup, SWT.NONE);
        lastNameLabel.setText("&Last name:");

        lastNameText = new Text(grojectCreatorGroup, SWT.BORDER);
        lastNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Group projectMetaGroup = new Group(container, SWT.NONE);
        projectMetaGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectMetaGroup.setText("Project meta information");
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        projectMetaGroup.setLayout(gridLayout_1);

        final Label shortDescriptionLabel = new Label(projectMetaGroup, SWT.NONE);
        shortDescriptionLabel.setText("&Short description:");

        shortDescriptionText = new Text(projectMetaGroup, SWT.BORDER);
        shortDescriptionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label basePackageNameLabel = new Label(projectMetaGroup, SWT.NONE);
        basePackageNameLabel.setText("Base &package name:");

        basePackageNameText = new Text(projectMetaGroup, SWT.BORDER);
        basePackageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label versionLabel = new Label(projectMetaGroup, SWT.NONE);
        versionLabel.setText("&Version:");

        versionText = new Text(projectMetaGroup, SWT.BORDER);
        versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label inceptionYearLabel = new Label(projectMetaGroup, SWT.NONE);
        inceptionYearLabel.setText("Inception &year:");

        inceptionYearText = new Text(projectMetaGroup, SWT.BORDER);
        inceptionYearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        firstNameText.setFocus();
    }

    /**
     * @return Returns the basePackageNameText.
     */
    public String getBasePackageName()
    {
        return basePackageNameText.getText();
    }

    /**
     * @return Returns the firstNameText.
     */
    public String getFirstName()
    {
        return firstNameText.getText();
    }

    /**
     * @return Returns the inceptionYearText.
     */
    public String getInceptionYear()
    {
        return inceptionYearText.getText();
    }

    /**
     * @return Returns the lastNameText.
     */
    public String getLastName()
    {
        return lastNameText.getText();
    }

    /**
     * @return
     */
    private String getProjectCreator()
    {
        return getFirstName() + " " + getLastName();
    }

    /**
     * @return Returns the shortDescriptionText.
     */
    public String getShortDescription()
    {
        return shortDescriptionText.getText();
    }

    /**
     * @return Returns the versionText.
     */
    public String getVersion()
    {
        return versionText.getText();
    }

    /**
     *
     */
    public void updateData()
    {
      projectProperties.put("projectCreator", getProjectCreator());
      projectProperties.put("projectVersion", getVersion());
      projectProperties.put("baseProjectPackage", getBasePackageName());
    }

}
