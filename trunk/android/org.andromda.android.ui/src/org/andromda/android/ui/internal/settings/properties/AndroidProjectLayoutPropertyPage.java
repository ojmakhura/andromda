/**
 *
 */
package org.andromda.android.ui.internal.settings.properties;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.project.IAndroidProjectDefinition;
import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.settings.preferences.AndroidProjectLayoutComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * This property page lets the user configure the project layout for the selected project.
 * 
 * @author Peter Friese
 * @since 30.09.2005
 */
public class AndroidProjectLayoutPropertyPage
        extends AbstractAndroidPropertyPage
{

    /** The ID of the preference page that is used to configure the project layout on a gloal level. */
    private static final String PREFERENCEPAGE_ID = "org.andromda.android.ui.internal.settings.preferences.AndroidProjectLayoutPreferencePage";

    /** The "enable project specific settings" check box. */
    protected Button enableProjectSpecificButton;

    /** This composite contains the input control used to configure the project layout settings. */
    private AndroidProjectLayoutComposite projectPropertiesPage;

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());

        final Composite composite = new Composite(container, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);

        enableProjectSpecificButton = new Button(composite, SWT.CHECK);
        enableProjectSpecificButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                boolean projectSpecificEnabled = enableProjectSpecificButton.getSelection();
                setUseProjectSpecificSettings(projectSpecificEnabled);
            }
        });
        enableProjectSpecificButton.setText("Enable project specific settings");

        final Link link = new Link(composite, SWT.NONE);
        link.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                PreferencesUtil.createPreferenceDialogOn(getShell(), PREFERENCEPAGE_ID,
                        new String[] { PREFERENCEPAGE_ID }, null).open();
            }
        });
        link.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
        link.setText("<a>Configure Workspace defaults...</a>");

        final Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

        projectPropertiesPage = new AndroidProjectLayoutComposite(composite, SWT.NONE);
        projectPropertiesPage.setEnabled(false);
        projectPropertiesPage.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        //
        setupData();
        return container;
    }

    /**
     * Updates the GUI controls as needed for the project specific state.
     * 
     * @param useProjectSpecificSettings <code>true</code> if project specific settings are used, <code>false</code>
     *            if not.
     */
    private void setUseProjectSpecificSettings(boolean useProjectSpecificSettings)
    {
        enableProjectSpecificButton.setSelection(useProjectSpecificSettings);
        projectPropertiesPage.setEnabled(useProjectSpecificSettings);

        // if no project specific settings: restore default values
        if (!useProjectSpecificSettings)
        {
            String configurationLocation = AndroidCore.getAndroidSettings().getConfigurationLocation();
            projectPropertiesPage.getConfigurationText().setText(configurationLocation);
        }
    }

    /**
     * Finds out whether project specific settings are used.
     * 
     * @return <code>true</code> if project specific settings are used, <code>false</code> if not.
     */
    private boolean isUseProjectSpecificSettings()
    {
        return enableProjectSpecificButton.getSelection();
    }

    /**
     * Fills the GUI controls with data retrieved from the configuration store.
     */
    private void setupData()
    {
        IAndroidProject androidProject = getAndroidProject();
        try
        {
            IAndroidProjectDefinition projectDefinition = androidProject.getProjectDefinition();

            // configuration location
            String projectConfigurationLocation = projectDefinition.getConfigurationLocation();
            String globalConfigurationLocation = AndroidCore.getAndroidSettings().getConfigurationLocation();
            projectPropertiesPage.getConfigurationText().setText((projectConfigurationLocation));

            // use project specific settings?
            boolean useProjectSpecificSettings = !projectConfigurationLocation.equals(globalConfigurationLocation);
            setUseProjectSpecificSettings(useProjectSpecificSettings);
        }
        catch (Exception e)
        {
            AndroidUIPlugin.log(e);
        }
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk()
    {
        try
        {
            IAndroidProject androidProject = getAndroidProject();
            IAndroidProjectDefinition projectDefinition = androidProject.getProjectDefinition();

            // configuration location
            String configurationLocation = projectPropertiesPage.getConfigurationText().getText();
            String globalConfigurationLocation = AndroidCore.getAndroidSettings().getConfigurationLocation();
            if (!configurationLocation.equals(globalConfigurationLocation))
            {
                projectDefinition.setConfigurationLocation(configurationLocation);
            }
            else
            {
                projectDefinition.setConfigurationLocation(null);
            }
        }
        catch (Exception e)
        {
            AndroidUIPlugin.log(e);
        }
        return super.performOk();
    }

}
