package org.andromda.android.ui.internal.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This preference page serves as a parent element for all other Android preference pages. It is empty.
 * 
 * @author Peter Friese
 * @since 28.11.2005
 */
public class AndroMDAPreferencePage
        extends PreferencePage
        implements IWorkbenchPreferencePage
{

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent)
    {
        return null;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        noDefaultAndApplyButton();
    }

}
