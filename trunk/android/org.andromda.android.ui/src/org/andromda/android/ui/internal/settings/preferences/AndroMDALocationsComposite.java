package org.andromda.android.ui.internal.settings.preferences;

import org.andromda.android.ui.internal.util.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This composite allows users to configure the location of the AndroMDA binary files. It is intented to be used on
 * preference and property pages.
 *
 * @author Peter Friese
 * @since 27.11.2005
 */
public class AndroMDALocationsComposite
        extends Composite
{

    /** Label for cartridges location. */
    private Label cartridgesLabel;

    /** Text field for cartridges location. */
    private Text cartridgesText;

    /** Browse button for cartidges location. */
    private Button cartridgesBrowseButton;

    /**
     * Create the composite.
     *
     * @param parent The parent for this composite.
     * @param style The SWT style for this composite.
     */
public AndroMDALocationsComposite(final Composite parent,
        final int style)
    {
        super(parent, style);
        setLayout(new GridLayout());

        final Group locationsGroup = new Group(this, SWT.NONE);
        locationsGroup.setText("Locations");
        locationsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 3;
        locationsGroup.setLayout(gridLayout_1);

        cartridgesLabel = new Label(locationsGroup, SWT.NONE);
        cartridgesLabel.setText("Cartridges:");

        cartridgesText = new Text(locationsGroup, SWT.BORDER);
        cartridgesText.setToolTipText("Specify where your cartridges are located. Android will perform a recursive scan for your cartridges. \nYou might want to specify your Maven repository root.");
        cartridgesText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        cartridgesText.setText("cartridges");

        cartridgesBrowseButton = new Button(locationsGroup, SWT.NONE);
        cartridgesBrowseButton.setToolTipText("Choose a directory.");
        cartridgesBrowseButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                String directoryName = DialogUtils.selectDirectory(getShell(),
                        "Select location for AndroMDA libraries.",
                        "Please choose the folder that contains the AndroMDA libraries.", cartridgesText.getText());
                if (directoryName != null)
                {
                    cartridgesText.setText(directoryName);
                }
            }
        });
        cartridgesBrowseButton.setText("Browse...");
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(final boolean enabled)
    {
        super.setEnabled(enabled);
        cartridgesLabel.setEnabled(enabled);
        cartridgesText.setEnabled(enabled);
        cartridgesBrowseButton.setEnabled(enabled);
    }

    /**
     * @return Returns the cartridgesText.
     */
    public Text getCartridgesText()
    {
        return cartridgesText;
    }



}
