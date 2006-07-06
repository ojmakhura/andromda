package org.andromda.android.ui.internal.settings.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This composite allows users to configure the version of the AndroMDA binary files. It is intented to be used on
 * preference and property pages.
 *
 * @author Peter Friese
 * @since 14.6.2005
 */
public class AndroMDAVersionsComposite
        extends Composite
{

    /** Label for cartridges location. */
    private Label preferredVersionLabel;

    /** Text field for cartridges location. */
    private Text preferredVersionText;

    /**
     * Create the composite.
     *
     * @param parent The parent for this composite.
     * @param style The SWT style for this composite.
     */
    public AndroMDAVersionsComposite(final Composite parent,
        final int style)
    {
        super(parent, style);
        setLayout(new GridLayout());

        final Group versionsGroup = new Group(this, SWT.NONE);
        versionsGroup.setText("Versions:");
        versionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        versionsGroup.setLayout(gridLayout_1);

        preferredVersionLabel = new Label(versionsGroup, SWT.NONE);
        preferredVersionLabel.setToolTipText("Specify the preferred AndroMDA version.");
        preferredVersionLabel.setText("Preferred version:");

        preferredVersionText = new Text(versionsGroup, SWT.BORDER);
        preferredVersionText.setToolTipText("Specify the preferred AndroMDA version.");
        preferredVersionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        preferredVersionText.setText("(version read from preference store)");
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(final boolean enabled)
    {
        super.setEnabled(enabled);
        preferredVersionLabel.setEnabled(enabled);
        preferredVersionText.setEnabled(enabled);
    }

    /**
     * Sets the preferred version.
     *
     * @param version The preferred version.
     */
    public void setPreferredVersion(String version)
    {
        preferredVersionText.setText(version);
    }

    /**
     * @return The preferred version.
     */
    public String getPreferredVersion()
    {
        return preferredVersionText.getText();
    }

}
