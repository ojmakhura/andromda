/**
 *
 */
package org.andromda.android.ui.internal.settings.preferences;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.settings.IAndroidSettings;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This preference page allows the user to define the project layout for Android projects.
 * 
 * @author Peter Friese
 * @since 05.10.2005
 */
public class AndroidProjectLayoutPreferencePage
        extends PreferencePage
        implements IWorkbenchPreferencePage
{

    /** The composite containing the input fields. */
    private AndroidProjectLayoutComposite projectPropertiesComposite;
    /** The ID of the preference page that is used to configure the project layout on a gloal level. */
    public static final String PREFERENCEPAGE_ID = "org.andromda.android.ui.internal.settings.preferences.AndroidProjectLayoutPreferencePage";

    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());

        projectPropertiesComposite = new AndroidProjectLayoutComposite(container, SWT.NONE);
        //
        setupData();
        return container;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
    }

    /**
     * Initialize the GUI controls with data read from the preference store.
     */
    private void setupData()
    {
        IAndroidSettings androidSettings = AndroidCore.getAndroidSettings();
        String configurationLocation = androidSettings.getConfigurationLocation();
        projectPropertiesComposite.getConfigurationText().setText(configurationLocation);
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk()
    {
        IAndroidSettings androidSettings = AndroidCore.getAndroidSettings();
        androidSettings.setConfigurationLocation(projectPropertiesComposite.getConfigurationText().getText());
        return super.performOk();
    }

}
