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
 * This composite contains controls that let the user configure the AndroMDA server.
 * 
 * @author Peter Friese
 * @since 07.12.2005
 */
public class ServerGeneralInformationComposite
        extends Composite
{

    /** The text entry field for the server port. */
    private Text portText;

    /** The text entry field for the server host. */
    private Text hostText;

    /**
     * Creates the composite.
     * 
     * @param parent The parent of the composite.
     * @param style The style.
     */
    public ServerGeneralInformationComposite(Composite parent,
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
        serverLocationSection.setDescription("Specify general AndroMDA server properties.");
        serverLocationSection.setText("General Information");

        final Composite serverLocationComposite = toolkit.createComposite(serverLocationSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        serverLocationComposite.setLayout(gridLayout_1);
        serverLocationSection.setClient(serverLocationComposite);
        toolkit.paintBordersFor(serverLocationComposite);

        final Label hostLabel = toolkit.createLabel(serverLocationComposite, "Host:", SWT.NONE);
        hostLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        hostText = toolkit.createText(serverLocationComposite, null, SWT.NONE);
        hostText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label portLabel = toolkit.createLabel(serverLocationComposite, "Port:", SWT.NONE);
        portLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        portText = toolkit.createText(serverLocationComposite, null, SWT.NONE);
        portText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

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
