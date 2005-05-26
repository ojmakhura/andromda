package org.andromda.android.internal.ui.wizards;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class ProjectFeaturesWizardPage
        extends WizardPage
{

    private Button ejbCentricRadio;

    private Button webCentricRadio;

    private Button includeWebAppCheck;

    private Button exposeWebservicesCheck;

    private final Map projectProperties;

    public ProjectFeaturesWizardPage(Map projectProperties)
    {
        super("wizardPage");
        this.projectProperties = projectProperties;
        setTitle("Project features");
        setDescription("Select the features for your new project.");
    }

    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);

        final Group projectTypeGroup = new Group(container, SWT.NONE);
        projectTypeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        projectTypeGroup.setLayout(gridLayout);
        projectTypeGroup.setText("Project type");

        ejbCentricRadio = new Button(projectTypeGroup, SWT.RADIO);
        ejbCentricRadio.setText("E&JB centric");

        webCentricRadio = new Button(projectTypeGroup, SWT.RADIO);
        webCentricRadio.setSelection(true);
        webCentricRadio.setText("&Web centric");

        final Group additionalFeaturesGroup = new Group(container, SWT.NONE);
        additionalFeaturesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        additionalFeaturesGroup.setText("Additional features");
        additionalFeaturesGroup.setLayout(new GridLayout());

        includeWebAppCheck = new Button(additionalFeaturesGroup, SWT.CHECK);
        includeWebAppCheck.setText("&Include web application");

        exposeWebservicesCheck = new Button(additionalFeaturesGroup, SWT.CHECK);
        exposeWebservicesCheck.setText("E&xpose services as web services");

        webCentricRadio.setFocus();
    }

    /**
     * @return Returns the exposeWebservicesCheck.
     */
    public boolean isExposeWebservices()
    {
        return exposeWebservicesCheck.getSelection();
    }

    /**
     * @return Returns the includeWebAppCheck.
     */
    public boolean getIncludeWebApp()
    {
        return includeWebAppCheck.getSelection();
    }

    /**
     * @return Returns the ejbCentricRadio.
     */
    public boolean isEjbCentric()
    {
        return ejbCentricRadio.getSelection();
    }

    /**
     * @return Returns the webCentricRadio.
     */
    public boolean isWebCentric()
    {
        return webCentricRadio.getSelection();
    }

    /**
     * @return
     */
    private String getProjectType()
    {
        if (isWebCentric())
        {
            return "war";
        }
        else
        {
            return "ejb";
        }
    }

    /**
     * @return
     */
    private String getExposeWebservices()
    {
        if (isExposeWebservices())
        {
            return "yes";
        }
        else
        {
            return "no";
        }
    }

    /**
     *
     */
    public void updateData()
    {
      projectProperties.put("projectType", getProjectType());
      projectProperties.put("exposeAsWebService", getExposeWebservices());
    }

}
