package org.andromda.android.ui.internal.properties;

import org.andromda.android.ui.internal.preferences.AndroMDALocationsComposite;
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
 * This property page allows users to configure the locations for the AndroMDA binaries for an Android project. 
 * 
 * @author Peter Friese
 * @since 28.11.2005
 */
public class AndroMDALocationsPropertyPage
        extends PropertyPage
{

    private AndroMDALocationsComposite androMDALocationsComposite;

    private static final String PREFERENCEPAGE_ID = "org.andromda.android.ui.internal.preferences.AndroMDALocationsPreferencePage";

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
                androMDALocationsComposite.setEnabled(projectSpecificEnabled);
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

        androMDALocationsComposite = new AndroMDALocationsComposite(composite, SWT.NONE);
        androMDALocationsComposite.setEnabled(false);
        androMDALocationsComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        //
        setupData();
        return container;
    }

    /**
     * 
     */
    private void setupData()
    {
        // TODO Auto-generated method stub

    }

}
