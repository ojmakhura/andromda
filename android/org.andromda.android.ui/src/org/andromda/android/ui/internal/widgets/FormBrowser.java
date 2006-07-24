package org.andromda.android.ui.internal.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledFormText;

/**
 * A simple browser component.
 *
 * @author Peter Friese
 * @since 24.07.2006
 */
public class FormBrowser
        extends Composite
{
    /** The form toolkit. */
    private FormToolkit toolkit = new FormToolkit(Display.getCurrent());

    /** The host for the browser. */
    private ScrolledFormText formText;

    /**
     * Create the composite.
     *
     * @param parent The parent composite.
     * @param style SWT style bits.
     */
    public FormBrowser(final Composite parent,
        final int style)
    {
        super(parent, style);
        setLayout(new GridLayout());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        formText = new ScrolledFormText(this, SWT.NONE, true);
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = 20;
        gridData.widthHint = 20;
        formText.setLayoutData(gridData);
        formText.setBackground(toolkit.getColors().getBackground());
        formText.setForeground(toolkit.getColors().getForeground());
        toolkit.adapt(formText, true, true);
        //
    }

    /**
     * {@inheritDoc}
     */
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * @param text The text to be set.
     */
    public void setText(final String text)
    {
        formText.setText(text);
    }

}
