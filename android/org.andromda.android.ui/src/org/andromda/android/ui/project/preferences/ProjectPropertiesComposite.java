/**
 *
 */
package org.andromda.android.ui.project.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *
 * @author Peter Friese
 * @since 30.09.2005
 */
public class ProjectPropertiesComposite
        extends Composite
{

    private Button browseButton;
    private Label configurationLabel;
    private Text configurationText;

    public ProjectPropertiesComposite(Composite parent,
        int style)
    {
        super(parent, style);
        setLayout(new GridLayout());

        final Group locationsGroup = new Group(this, SWT.NONE);
        locationsGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        locationsGroup.setText("Locations");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        locationsGroup.setLayout(gridLayout);

        configurationLabel = new Label(locationsGroup, SWT.NONE);
        configurationLabel.setText("Configuration");

        configurationText = new Text(locationsGroup, SWT.BORDER);
        final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.widthHint = 219;
        configurationText.setLayoutData(gridData);

        browseButton = new Button(locationsGroup, SWT.FLAT);
        browseButton.setText("Browse...");

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
        browseButton.setEnabled(enabled);
    }

}
