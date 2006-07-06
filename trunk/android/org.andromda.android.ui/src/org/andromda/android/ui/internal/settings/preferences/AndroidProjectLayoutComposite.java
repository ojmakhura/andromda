/**
 *
 */
package org.andromda.android.ui.internal.settings.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *
 * @author Peter Friese
 * @since 30.09.2005
 */
public class AndroidProjectLayoutComposite
        extends Composite
{


    private Label configurationLabel;

    private Text configurationText;

    public AndroidProjectLayoutComposite(Composite parent,
        int style)
    {
        super(parent, style);
        setLayout(new GridLayout());

        final Group locationsGroup = new Group(this, SWT.NONE);
        locationsGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        locationsGroup.setText("Relative Locations");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        locationsGroup.setLayout(gridLayout);

        configurationLabel = new Label(locationsGroup, SWT.NONE);
        configurationLabel.setText("Configuration");

        configurationText = new Text(locationsGroup, SWT.BORDER);
        configurationText.setToolTipText("The name of the file containing the AndroMDA configuration for the project, relative to the project root.");
        final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.widthHint = 219;
        configurationText.setLayoutData(gridData);

        //
    }

    /**
     * @return a reference to the text field containg the configuration path.
     */
    public Text getConfigurationText()
    {
        return configurationText;
    }

    /**
     * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        configurationLabel.setEnabled(enabled);
        configurationText.setEnabled(enabled);
    }

}
