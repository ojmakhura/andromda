/**
 *
 */
package org.andromda.android.ui.project.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 *
 * @author Peter Friese
 * @since 05.10.2005
 */
public class AndroidProjectPreferencePage
        extends PreferencePage
        implements IWorkbenchPreferencePage
{

    public AndroidProjectPreferencePage()
    {
        super();
        setTitle("Project Layout");
    }

    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());

        final ProjectPropertiesComposite projectPropertiesComposite = new ProjectPropertiesComposite(container, SWT.NONE);
        //
        return container;
    }

    public void init(IWorkbench workbench)
    {
    }

}
