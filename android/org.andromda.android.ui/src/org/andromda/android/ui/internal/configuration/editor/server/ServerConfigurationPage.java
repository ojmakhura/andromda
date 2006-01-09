package org.andromda.android.ui.internal.configuration.editor.server;

import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelFormPage;
import org.andromda.android.ui.internal.editor.AbstractModelFormEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This page contains controls that let the user configure the AndroMDA server.
 *
 * @author Peter Friese
 * @since 07.12.2005
 */
public class ServerConfigurationPage
        extends AbstractAndromdaModelFormPage
{

    /** The page ID. */
    public static final String PAGE_ID = "server";

    /**
     * Creates the page.
     *
     * @param editor The editor hosting this page.
     * @param id The ID of this page.
     * @param title The title of this page.
     */
    public ServerConfigurationPage(AbstractModelFormEditor editor,
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

        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.numColumns = 2;
        Composite body = form.getBody();
        body.setLayout(gridLayout);

        toolkit.paintBordersFor(body);

        // general information section
        final ServerGeneralInformationSection serverGeneralInformationSection = new ServerGeneralInformationSection(this);
        managedForm.addPart(serverGeneralInformationSection);

        // model loading section
        final ServerLoadingSection serverLoadingSection = new ServerLoadingSection(this);
        managedForm.addPart(serverLoadingSection);
    }

    /**
     * @see org.andromda.android.ui.editor.AbstractFormEditorPage#doUpdatePage(org.eclipse.jface.text.IDocument)
     */
    protected void doUpdatePage(IDocument document)
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.andromda.android.ui.editor.AbstractFormEditorPage#doUpdateDocument(org.eclipse.jface.text.IDocument)
     */
    protected void doUpdateDocument(IDocument document)
    {
        // TODO Auto-generated method stub

    }

}
