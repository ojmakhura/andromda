package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.ui.internal.configuration.editor.ServerGeneralInformationComposite;
import org.andromda.android.ui.internal.configuration.editor.ServerLoadingComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This page contains controls that let the user configure the AndroMDA server.
 * 
 * @author Peter Friese
 * @since 07.12.2005
 */
public class ServerConfigurationPage
        extends FormPage
{

    /** The pae ID. */
    public static final String PAGE_ID = "server";

    /**
     * Creates the page.
     * 
     * @param editor The editor hosting this page.
     * @param id The ID of this page.
     * @param title The title of this page.
     */
    public ServerConfigurationPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
    }

    /**
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("AndroMDA Server");
        Composite body = form.getBody();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.numColumns = 2;
        body.setLayout(gridLayout);
        toolkit.paintBordersFor(body);

        final ServerGeneralInformationComposite serverGeneralInformationComposite = new ServerGeneralInformationComposite(
                body, SWT.NONE);
        serverGeneralInformationComposite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final ServerLoadingComposite serverLoadingComposite = new ServerLoadingComposite(body, SWT.NONE);
        serverLoadingComposite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

}
