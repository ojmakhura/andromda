/**
 *
 */
package org.andromda.android.ui.project.preferences;

import java.io.IOException;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.project.IAndroidProjectDefinition;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.ui.dialogs.PropertyPage;

/**
 *
 * @author Peter Friese
 * @since 30.09.2005
 */
public class AndroidProjectPropertyPage
        extends PropertyPage
{

    private static final String PREFERENCEPAGE_ID = "org.andromda.android.ui.project.preferences.AndroidProjectPreferencePage";

    private ProjectPropertiesComposite projectPropertiesPage;

    public AndroidProjectPropertyPage()
    {
        super();
        setTitle("Project Layout");
    }

    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());

        final Composite composite = new Composite(container, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);

        final Button enableProjectSpecificButton = new Button(composite, SWT.CHECK);
        enableProjectSpecificButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                boolean projectSpecificEnabled = enableProjectSpecificButton.getSelection();
                projectPropertiesPage.setEnabled(projectSpecificEnabled);
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

        projectPropertiesPage = new ProjectPropertiesComposite(composite, SWT.NONE);
        projectPropertiesPage.setEnabled(false);
        projectPropertiesPage.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        //
        setupData();
        return container;
    }

    private void setupData()
    {
        IProject project = (IProject)getElement().getAdapter(IProject.class);
        IAndroidProject androidProject = AndroidCore.create(project);
        try
        {
            IAndroidProjectDefinition projectDefinition = androidProject.getProjectDefinition();
            String configurationLocation = projectDefinition.getConfigurationLocation();
            projectPropertiesPage.getConfigurationText().setText((configurationLocation));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
