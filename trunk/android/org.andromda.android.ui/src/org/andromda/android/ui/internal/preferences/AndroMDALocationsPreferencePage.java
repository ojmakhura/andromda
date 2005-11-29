package org.andromda.android.ui.internal.preferences;

import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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

    /** The composite containing the input fields. */
    private AndroMDALocationsComposite androMDALocationsComposite;

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());

        androMDALocationsComposite = new AndroMDALocationsComposite(container, SWT.NONE);
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
        String cartridgesLocation = getPreferenceStore().getString(IPreferenceConstants.LOCATION_CARTRIDGES);
        String profilesLocation = getPreferenceStore().getString(IPreferenceConstants.LOCATION_PROFILES);

        androMDALocationsComposite.getProfilesText().setText(profilesLocation);
        androMDALocationsComposite.getCartridgesText().setText(cartridgesLocation);
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk()
    {
        getPreferenceStore().setValue(IPreferenceConstants.LOCATION_CARTRIDGES,
                androMDALocationsComposite.getCartridgesText().getText());
        getPreferenceStore().setValue(IPreferenceConstants.LOCATION_PROFILES,
                androMDALocationsComposite.getProfilesText().getText());

        return super.performOk();
    }

}
