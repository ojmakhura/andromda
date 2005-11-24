package org.andromda.android.ui.internal.project.wizard;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ProjectMetaInformationWizardPage
        extends WizardPage
{

    /**
     * This modifiy listener invokes a page validation after each modification.
     *
     * @author Peter Friese
     * @since 01.06.2005
     */
    private final class ValidatingTextModifyListener
            implements ModifyListener
    {
        public void modifyText(ModifyEvent e)
        {
            setPageComplete(validatePage());
        }
    }
    private ValidatingTextModifyListener validatingTextModifyListener = new ValidatingTextModifyListener();

    private Text inceptionYearText;

    private Text versionText;

    private Text basePackageNameText;

    private Text shortDescriptionText;

    private Text lastNameText;

    private Text firstNameText;

    /**
     * This map contains all information gathered by the wizard.
     */
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
        firstNameText.addModifyListener(validatingTextModifyListener);
        firstNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label lastNameLabel = new Label(grojectCreatorGroup, SWT.NONE);
        lastNameLabel.setText("&Last name:");

        lastNameText = new Text(grojectCreatorGroup, SWT.BORDER);
        lastNameText.addModifyListener(validatingTextModifyListener);
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
        shortDescriptionText.addModifyListener(validatingTextModifyListener);
        shortDescriptionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label basePackageNameLabel = new Label(projectMetaGroup, SWT.NONE);
        basePackageNameLabel.setText("Base &package name:");

        basePackageNameText = new Text(projectMetaGroup, SWT.BORDER);
        basePackageNameText.addModifyListener(validatingTextModifyListener);
        basePackageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label versionLabel = new Label(projectMetaGroup, SWT.NONE);
        versionLabel.setText("&Version:");

        versionText = new Text(projectMetaGroup, SWT.BORDER);
        versionText.addModifyListener(validatingTextModifyListener);
        versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Label inceptionYearLabel = new Label(projectMetaGroup, SWT.NONE);
        inceptionYearLabel.setText("Inception &year:");

        inceptionYearText = new Text(projectMetaGroup, SWT.BORDER);
        inceptionYearText.addModifyListener(validatingTextModifyListener);
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

    /**
     * Checks whether the page is valid or not.
     *
     * @return <code>true</code> if all fields have been filled,
     *         <code>false</code> otherwise.
     */
    public boolean validatePage()
    {

        setErrorMessage(null);
        setMessage(null);

        String firstName = getFirstName();
        if (firstName.equals(""))
        {
            setMessage("Please enter your first name.");
            return false;
        }

        String lastName = getLastName();
        if (lastName.equals(""))
        {
            setMessage("Please enter your last name.");
            return false;
        }

        String shortDescription = getShortDescription();
        if (shortDescription.equals(""))
        {
            setMessage("Please ener a short description for the project.");
            return false;
        }

        String basePackageName = getBasePackageName();
        if (basePackageName.equals(""))
        {
            setMessage("Please define a name for the base package.");
            return false;
        }

        String version = getVersion();
        if (version.equals(""))
        {
            setMessage("Please enter the version of the project (e.g. 0.1).");
            return false;
        }

        String inceptionYear = getInceptionYear();
        if (inceptionYear.equals(""))
        {
            setMessage("Please enter the inception year.");
            return false;
        }

        return true;

    }

}
