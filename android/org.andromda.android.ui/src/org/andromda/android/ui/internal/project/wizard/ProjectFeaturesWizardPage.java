package org.andromda.android.ui.internal.project.wizard;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class ProjectFeaturesWizardPage
        extends WizardPage
{

    private Button jsfRadio;

    private Button strutsRadio;

    private static final int HIBERNATEVERSIONCOMBO_HIBERNATE2 = 0;

    private static final int HIBERNATEVERSIONCOMBO_HIBERNATE3 = 1;

    private Button useJBPM;

    private Combo hibernateVersionCombo;

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
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        container.setLayout(gridLayout_1);
        setControl(container);

        final Group projectTypeGroup = new Group(container, SWT.NONE);
        projectTypeGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        projectTypeGroup.setLayout(gridLayout);
        projectTypeGroup.setText("Project type");

        ejbCentricRadio = new Button(projectTypeGroup, SWT.RADIO);
        ejbCentricRadio.setText("E&JB centric");

        webCentricRadio = new Button(projectTypeGroup, SWT.RADIO);
        webCentricRadio.setSelection(true);
        webCentricRadio.setText("&Web centric");

        final Group frontendGroup = new Group(container, SWT.NONE);
        frontendGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        frontendGroup.setText("Frontend");
        frontendGroup.setLayout(new GridLayout());

        strutsRadio = new Button(frontendGroup, SWT.RADIO);
        strutsRadio.setText("&Struts");

        jsfRadio = new Button(frontendGroup, SWT.RADIO);
        jsfRadio.setText("Java Server &Faces (JSF)");

        webCentricRadio.setFocus();

        final Group hibernateVersionGroup = new Group(container, SWT.NONE);
        hibernateVersionGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        hibernateVersionGroup.setText("&Hibernate");
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        hibernateVersionGroup.setLayout(gridLayout_2);

        final Label hibernateVersionLabel = new Label(hibernateVersionGroup, SWT.NONE);
        hibernateVersionLabel.setText("&Version");

        hibernateVersionCombo = new Combo(hibernateVersionGroup, SWT.READ_ONLY);
        hibernateVersionCombo.setItems(new String[] { "2.0.x", "3.0.x" });
        hibernateVersionCombo.setLayoutData(new GridData(50, SWT.DEFAULT));
        hibernateVersionCombo.select(0);

        final Group additionalFeaturesGroup = new Group(container, SWT.NONE);
        additionalFeaturesGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        additionalFeaturesGroup.setText("Additional &features");
        additionalFeaturesGroup.setLayout(new GridLayout());

        useJBPM = new Button(additionalFeaturesGroup, SWT.CHECK);
        useJBPM.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                updateSelectionStates();
            }
        });
        useJBPM.setText("Enable J&BPM workflow engine");

        includeWebAppCheck = new Button(additionalFeaturesGroup, SWT.CHECK);
        includeWebAppCheck.setText("&Include web application");

        exposeWebservicesCheck = new Button(additionalFeaturesGroup, SWT.CHECK);
        exposeWebservicesCheck.setText("E&xpose services as web services");

        updateSelectionStates();
    }

    /**
     * 
     */
    protected void updateSelectionStates()
    {
        if (isUseJBPM())
        {
            hibernateVersionCombo.select(HIBERNATEVERSIONCOMBO_HIBERNATE3);
        }
        else
        {
            hibernateVersionCombo.select(HIBERNATEVERSIONCOMBO_HIBERNATE2);
        }
    }

    public boolean isUseJBPM()
    {
        return useJBPM.getSelection();
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
    public boolean isIncludeWebApp()
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

    private String getUseJBPM()
    {
        if (isUseJBPM())
        {
            return "yes";
        }
        else
        {
            return "no";
        }
    }

    private String getHibernateVersion()
    {
        if (hibernateVersionCombo.getSelectionIndex() == HIBERNATEVERSIONCOMBO_HIBERNATE2)
        {
            return "2";
        }
        else if (hibernateVersionCombo.getSelectionIndex() == HIBERNATEVERSIONCOMBO_HIBERNATE3)
        {
            return "3";
        }
        return "0";
    }

    private String getWebComponent()
    {
        if (isIncludeWebApp())
        {
            return "yes";
        }
        else
        {
            return "no";
        }
    }

    private boolean isStruts()
    {
        return strutsRadio.getSelection();
    }

    private boolean isJSF()
    {
        return jsfRadio.getSelection();
    }

    private String getWebComponentType()
    {
        if (isStruts())
        {
            return "struts";
        }
        else
        {
            return "jsf";
        }
    }

    public void updateData()
    {
        projectProperties.put("projectType", getProjectType());
        projectProperties.put("exposeAsWebService", getExposeWebservices());
        projectProperties.put("jbpm", getUseJBPM());
        projectProperties.put("hibernateVersion", getHibernateVersion());
        projectProperties.put("webComponent", getWebComponent());
        projectProperties.put("webComponentType", getWebComponentType());
    }

}
