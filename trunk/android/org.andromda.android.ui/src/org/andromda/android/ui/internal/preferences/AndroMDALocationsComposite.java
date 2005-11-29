package org.andromda.android.ui.internal.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This composite allows users to configure the location of the AndroMDA binary files. 
 * It is intented to be used on preference and property pages. 
 * 
 * @author Peter Friese
 * @since 27.11.2005
 */
public class AndroMDALocationsComposite
        extends Composite
{

    private Button cartridgesBrowseButton;
    private Button profilesBrowseButton;

    private Label profilesLabel;

    private Label cartridgesLabel;

    private Text profilesText;

    private Text cartridgesText;

    /**
     * Create the composite
     * 
     * @param parent
     * @param style
     */
    public AndroMDALocationsComposite(Composite parent,
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

        cartridgesLabel = new Label(locationsGroup, SWT.NONE);
        cartridgesLabel.setText("Cartridges:");

        cartridgesText = new Text(locationsGroup, SWT.BORDER);
        cartridgesText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        cartridgesText.setText("cartridges");

        cartridgesBrowseButton = new Button(locationsGroup, SWT.NONE);
        cartridgesBrowseButton.setText("Browse...");

        profilesLabel = new Label(locationsGroup, SWT.NONE);
        profilesLabel.setText("Profiles:");

        profilesText = new Text(locationsGroup, SWT.BORDER);
        profilesText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        profilesText.setText("profiles");

        profilesBrowseButton = new Button(locationsGroup, SWT.NONE);
        profilesBrowseButton.setText("Browse...");

        //
    }

    public void dispose()
    {
        super.dispose();
    }

    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        cartridgesLabel.setEnabled(enabled);
        cartridgesText.setEnabled(enabled);
        cartridgesBrowseButton.setEnabled(enabled);
        profilesLabel.setEnabled(enabled);
        profilesText.setEnabled(enabled);
        profilesBrowseButton.setEnabled(enabled);
    }
    
    /**
     * @return Returns the cartridgesText.
     */
    public Text getCartridgesText()
    {
        return cartridgesText;
    }
    
    /**
     * @return Returns the profilesText.
     */
    public Text getProfilesText()
    {
        return profilesText;
    }

}
