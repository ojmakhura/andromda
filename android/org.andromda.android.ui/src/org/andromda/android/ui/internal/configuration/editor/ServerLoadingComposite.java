package org.andromda.android.ui.internal.configuration.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This composite contains control that let the user configure the model loading behaviour of the server.
 * 
 * @author Peter Friese
 * @since 07.12.2005
 */
public class ServerLoadingComposite
        extends Composite
{

    /** The text entry field for the maximum number of load failures. */
    private Text maximumFailedLoadAttemptsText;

    /** The text entry field for the load interval. */
    private Text loadIntervalText;

    /**
     * Creates the composite.
     * 
     * @param parent The parent container.
     * @param style The style.
     */
    public ServerLoadingComposite(Composite parent,
        int style)
    {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final Section serverLocationSection = toolkit.createSection(this, Section.DESCRIPTION | Section.TITLE_BAR);
        serverLocationSection.setLayoutData(new GridData(GridData.FILL_BOTH));
        serverLocationSection.setDescription("Specify the server's loading behaviour.");
        serverLocationSection.setText("Loading behaviour");

        final Composite serverLocationComposite = toolkit.createComposite(serverLocationSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        serverLocationComposite.setLayout(gridLayout_1);
        serverLocationSection.setClient(serverLocationComposite);
        toolkit.paintBordersFor(serverLocationComposite);

        final Label loadIntervalLabel = toolkit.createLabel(serverLocationComposite, "Load interval:", SWT.NONE);
        loadIntervalLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        loadIntervalText = toolkit.createText(serverLocationComposite, null, SWT.NONE);
        loadIntervalText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label maximumFailedLoadAttemptsLabel = toolkit.createLabel(serverLocationComposite, "Maximum failed load attempts:", SWT.NONE);
        maximumFailedLoadAttemptsLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        maximumFailedLoadAttemptsText = toolkit.createText(serverLocationComposite, null, SWT.NONE);
        maximumFailedLoadAttemptsText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        //
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose()
    {
        super.dispose();
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#checkSubclass()
     */
    protected void checkSubclass()
    {
    }

}
