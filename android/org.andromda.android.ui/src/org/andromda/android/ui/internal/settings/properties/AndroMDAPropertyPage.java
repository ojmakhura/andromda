package org.andromda.android.ui.internal.settings.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * 
 * @author Peter Friese
 * @since 28.11.2005
 */
public class AndroMDAPropertyPage
        extends PropertyPage
{

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        //
        return container;
    }

}
