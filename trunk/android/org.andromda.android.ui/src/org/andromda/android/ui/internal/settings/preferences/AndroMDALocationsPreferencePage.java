package org.andromda.android.ui.internal.settings.preferences;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.settings.IAndroidSettings;
import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This preference page lets users configure the lcoations for the AndroMDA binary files such as cartridges and
 * profiles.
 *
 * @author Peter Friese
 * @since 28.11.2005
 */
public class AndroMDALocationsPreferencePage
        extends PreferencePage
        implements IWorkbenchPreferencePage
{

    private AndroMDAVersionsComposite androMDAVersionsComposite;
    /** The composite containing the input fields. */
    private AndroMDALocationsComposite androMDALocationsComposite;
    public static final String PREFERENCEPAGE_ID = "org.andromda.android.ui.internal.settings.preferences.AndroMDALocationsPreferencePage";

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        container.setLayout(gridLayout);

        androMDALocationsComposite = new AndroMDALocationsComposite(container, SWT.NONE);
        androMDALocationsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        androMDAVersionsComposite = new AndroMDAVersionsComposite(container, SWT.NONE);
        androMDAVersionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        //
        setupData();
        return container;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(AndroidUIPlugin.getDefault().getPreferenceStore());
    }

    /**
     * Initialize the GUI controls with data read from the preference store.
     */
    private void setupData()
    {
        IAndroidSettings androidSettings = AndroidCore.getAndroidSettings();

        // locations
        String cartridgesLocation = androidSettings.getAndroMDACartridgesLocation();
        String profilesLocation = androidSettings.getAndroMDAProfilesLocation();
        String mavenLocation = androidSettings.getMavenLocation();
        androMDALocationsComposite.setCartridgesLocation(cartridgesLocation);

        // versions
        String androMDAPreferredVersion = androidSettings.getAndroMDAPreferredVersion();
        androMDAVersionsComposite.setPreferredVersion(androMDAPreferredVersion);
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk()
    {
        IAndroidSettings androidSettings = AndroidCore.getAndroidSettings();
        androidSettings.setAndroMDACartridgesLocation(androMDALocationsComposite.getCartridgesLocation());

        return super.performOk();
    }

}
